package com.boxer.browser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BookmarksListActivity extends Activity {
	private ListView mDrawerListRight;
	private List<HistoryItem> mBookmarkList;
	private BookmarkViewAdapter mBookmarkAdapter;
	private LightningView mCurrentView;
	private Bitmap mWebpageBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmarks_list);
		mWebpageBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_webpage);
		mDrawerListRight = (ListView) findViewById(R.id.right_drawer_list);
		mDrawerListRight.setDivider(null);
		mDrawerListRight.setDividerHeight(0);
		mBookmarkList = getBookmarks();
		mBookmarkAdapter = new BookmarkViewAdapter(this,
				R.layout.bookmark_list_item, mBookmarkList);
		mDrawerListRight.setAdapter(mBookmarkAdapter);
		mDrawerListRight
				.setOnItemClickListener(new BookmarkItemClickListener());
		mDrawerListRight
				.setOnItemLongClickListener(new BookmarkItemLongClickListener());
	}

	// Damn it, I regret not using SQLite in the first place for this
	private List<HistoryItem> getBookmarks() {
		List<HistoryItem> bookmarks = new ArrayList<HistoryItem>();
		File bookUrl = new File(getApplicationContext().getFilesDir(),
				"bookurl");
		File book = new File(getApplicationContext().getFilesDir(), "bookmarks");
		try {
			BufferedReader readUrl = new BufferedReader(new FileReader(bookUrl));
			BufferedReader readBook = new BufferedReader(new FileReader(book));
			String u, t;
			while ((u = readUrl.readLine()) != null
					&& (t = readBook.readLine()) != null) {
				HistoryItem map = new HistoryItem(u, t);
				bookmarks.add(map);
			}
			readBook.close();
			readUrl.close();
		} catch (FileNotFoundException ignored) {
		} catch (IOException ignored) {
		}
		Collections.sort(bookmarks, new SortIgnoreCase());
		return bookmarks;
	}

	public class SortIgnoreCase implements Comparator<HistoryItem> {

		public int compare(HistoryItem o1, HistoryItem o2) {
			return o1.getTitle().toLowerCase(Locale.getDefault())
					.compareTo(o2.getTitle().toLowerCase(Locale.getDefault()));
		}

	}

	private class BookmarkItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mCurrentView != null) {
				mCurrentView.loadUrl(mBookmarkList.get(position).getUrl());
			}
			// keep any jank from happening when the drawer is closed after the
			// URL starts to load
			 
		}
	}

	private class BookmarkItemLongClickListener implements
			ListView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int position, long arg3) {

			AlertDialog.Builder builder = new AlertDialog.Builder(BookmarksListActivity.this);
			builder.setTitle(BookmarksListActivity.this.getResources().getString(
					R.string.action_bookmarks));
			builder.setMessage(
					getResources().getString(R.string.dialog_bookmark))
					.setCancelable(true)
					.setPositiveButton(getResources().getString(R.string.action_edit),
							new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							editBookmark(position);
						}
					})
					.setNegativeButton(
							getResources().getString(R.string.action_delete),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									deleteBookmark(mBookmarkList.get(position)
											.getUrl());
								}
							});
					 
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}
	}

	public class BookmarkViewAdapter extends ArrayAdapter<HistoryItem> {

		Context context;

		int layoutResourceId;

		List<HistoryItem> data = null;

		public BookmarkViewAdapter(Context context, int layoutResourceId,
				List<HistoryItem> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			BookmarkViewHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new BookmarkViewHolder();
				holder.txtTitle = (TextView) row.findViewById(R.id.text1);
				holder.favicon = (ImageView) row.findViewById(R.id.favicon1);
				row.setTag(holder);
			} else {
				holder = (BookmarkViewHolder) row.getTag();
			}

			HistoryItem web = data.get(position);
			holder.txtTitle.setText(web.getTitle());
			holder.favicon.setImageBitmap(mWebpageBitmap);
			if (web.getBitmap() == null) {
				getImage(holder.favicon, web);
			} else {
				holder.favicon.setImageBitmap(web.getBitmap());
			}
			return row;
		}

		class BookmarkViewHolder {

			TextView txtTitle;

			ImageView favicon;
		}
	}

	public void getImage(ImageView image, HistoryItem web) {
		try {
			new DownloadImageTask(image, web).execute(B_Constants.HTTP
					+ getDomainName(web.getUrl()) + "/favicon.ico");
		} catch (URISyntaxException e) {
			new DownloadImageTask(image, web)
					.execute("https://www.google.com/s2/favicons?domain_url="
							+ web.getUrl());
			e.printStackTrace();
		}
	}
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

			ImageView bmImage;

			HistoryItem mWeb;

			public DownloadImageTask(ImageView bmImage, HistoryItem web) {
				this.bmImage = bmImage;
				this.mWeb = web;
			}

			protected Bitmap doInBackground(String... urls) {
				String urldisplay = urls[0];
				Bitmap mIcon = null;
				// unique path for each url that is bookmarked.
				String hash = String.valueOf(urldisplay.hashCode());
				File image = new File(BookmarksListActivity.this.getCacheDir(), hash + ".png");
				// checks to see if the image exists
				if (!image.exists()) {
					try {
						// if not, download it...
						URL url = new URL(urldisplay);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream in = connection.getInputStream();

						if (in != null) {
							mIcon = BitmapFactory.decodeStream(in);
						}
						// ...and cache it
						if (mIcon != null) {
							FileOutputStream fos = new FileOutputStream(image);
							mIcon.compress(Bitmap.CompressFormat.PNG, 100, fos);
							fos.flush();
							fos.close();
							Log.i(B_Constants.TAG, "Downloaded: " + urldisplay);
						}

					} catch (Exception e) {
					} finally {

					}
				} else {
					// if it exists, retrieve it from the cache
					mIcon = BitmapFactory.decodeFile(image.getPath());
				}
				if (mIcon == null) {
					try {
						// if not, download it...
						InputStream in = new java.net.URL(
								"https://www.google.com/s2/favicons?domain_url="
										+ urldisplay).openStream();

						if (in != null) {
							mIcon = BitmapFactory.decodeStream(in);
						}
						// ...and cache it
						if (mIcon != null) {
							FileOutputStream fos = new FileOutputStream(image);
							mIcon.compress(Bitmap.CompressFormat.PNG, 100, fos);
							fos.flush();
							fos.close();
						}

					} catch (Exception e) {
					}
				}
				if (mIcon == null) {
					return mWebpageBitmap;
				} else {
					return mIcon;
				}
			}

			protected void onPostExecute(Bitmap result) {
				bmImage.setImageBitmap(result);
				mWeb.setBitmap(result);
				notifyBookmarkDataSetChanged();
			}
		}
	

	private void notifyBookmarkDataSetChanged() {
		mBookmarkAdapter.clear();
		mBookmarkAdapter.addAll(mBookmarkList);
		mBookmarkAdapter.notifyDataSetChanged();
	}
	public synchronized void editBookmark(final int id) {
		final AlertDialog.Builder homePicker = new AlertDialog.Builder(
				BookmarksListActivity.this);
		homePicker.setTitle(getResources().getString(
				R.string.title_edit_bookmark));
		final EditText getTitle = new EditText(BookmarksListActivity.this);
		getTitle.setHint(getResources().getString(R.string.hint_title));
		getTitle.setText(mBookmarkList.get(id).getTitle());
		getTitle.setSingleLine();
		final EditText getUrl = new EditText(BookmarksListActivity.this);
		getUrl.setHint(getResources().getString(R.string.hint_url));
		getUrl.setText(mBookmarkList.get(id).getUrl());
		getUrl.setSingleLine();
		LinearLayout layout = new LinearLayout(BookmarksListActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(getTitle);
		layout.addView(getUrl);
		homePicker.setView(layout);
		homePicker.setPositiveButton(
				getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mBookmarkList.get(id).setTitle(
								getTitle.getText().toString());
						mBookmarkList.get(id).setUrl(
								getUrl.getText().toString());
						notifyBookmarkDataSetChanged();
						File book = new File(getFilesDir(), "bookmarks");
						File bookUrl = new File(getFilesDir(), "bookurl");
						try {
							BufferedWriter bookWriter = new BufferedWriter(
									new FileWriter(book));
							BufferedWriter urlWriter = new BufferedWriter(
									new FileWriter(bookUrl));
							Iterator<HistoryItem> iter = mBookmarkList
									.iterator();
							HistoryItem item;
							while (iter.hasNext()) {
								item = iter.next();

								bookWriter.write(item.getTitle());
								urlWriter.write(item.getUrl());
								bookWriter.newLine();
								urlWriter.newLine();

							}

							bookWriter.close();
							urlWriter.close();
						} catch (FileNotFoundException e) {
						} catch (IOException e) {
						}
						Collections.sort(mBookmarkList, new SortIgnoreCase());
						notifyBookmarkDataSetChanged();
//						if (mCurrentView != null) {
//							if (mCurrentView.getUrl().startsWith(
//									B_Constants.FILE)
//									&& mCurrentView.getUrl().endsWith(
//											"bookmarks.html")) {
//								openBookmarkPage(mCurrentView.getWebView());
//							}
//						}
					}
				});
		homePicker.show();
	}
	public void deleteBookmark(String url) {
		File book = new File(getFilesDir(), "bookmarks");
		File bookUrl = new File(getFilesDir(), "bookurl");
		try {
			BufferedWriter bookWriter = new BufferedWriter(new FileWriter(book));
			BufferedWriter urlWriter = new BufferedWriter(new FileWriter(
					bookUrl));
			Iterator<HistoryItem> iter = mBookmarkList.iterator();
			HistoryItem item;
			int num = 0;
			int deleteIndex = -1;
			while (iter.hasNext()) {
				item = iter.next();
				if (!item.getUrl().equalsIgnoreCase(url)) {
					bookWriter.write(item.getTitle());
					urlWriter.write(item.getUrl());
					bookWriter.newLine();
					urlWriter.newLine();
				} else {
					deleteIndex = num;
				}
				num++;
			}
			if (deleteIndex != -1) {
				mBookmarkList.remove(deleteIndex);
			}
			bookWriter.close();
			urlWriter.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		notifyBookmarkDataSetChanged();
//		mSearchAdapter.refreshBookmarks();
		 
	}
	static String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		if (domain == null) {
			return url;
		}
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}
