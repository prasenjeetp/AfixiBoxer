package com.manishkpr.slidingtabsexample.adapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.boxer.browser.AppDetail;
import com.boxer.browser.B_BrowserActivity;
import com.boxer.browser.B_MainActivity;
import com.boxer.browser.MainActivity;
import com.boxer.browser.NoInternetConnection;
import com.boxer.browser.R;
import com.boxer.browser.Splash;
import com.boxer.util.Constant;
import com.boxer.util.HashGenerator;
import com.manishkpr.slidingtabsexample.DealProduct;
import com.manishkpr.slidingtabsexample.Tabs;
import com.mobileapptracker.MATEvent;
import com.mobileapptracker.MobileAppTracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class TabsPagerAdapter extends PagerAdapter {
	// String tabs[] = { "Todays Best", "Previous App", "Fantastic",
	// "Hello Tunes", "Try This", };
	
	
	String imagePath = "", titel = "", text = "", url = "", downloadLink = "",
			download_size = "";
	String mainUrl;
	String responceString = "";
	public static boolean iconClicked;
	List<Tabs> listTab = Splash.listTab;
	String tabs1[];
	boolean isLoded;

	ProgressDialog pd;
	DownloadFile downloadFile;
	Activity activity;
	String AID = "";
	ProgressDialog dialog;
	String jsonStr;
	private List<DealProduct> listTop3 = new ArrayList<DealProduct>();
	private List<DealProduct> listPrevious = new ArrayList<DealProduct>();
	private List<DealProduct> listTop2 = new ArrayList<DealProduct>();
	private List<DealProduct> listTop1 = new ArrayList<DealProduct>();
	private List<DealProduct> listCurrent = new ArrayList<DealProduct>();// not
																			// used
	JSONObject jsObjectArr = null;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	String engine;

	public TabsPagerAdapter(Activity activity) {
		this.activity = activity;
		AID = Secure
				.getString(activity.getContentResolver(), Secure.ANDROID_ID);
		Log.e("Aid", AID);
		if (listTab != null) {
			tabs1 = new String[listTab.size()];
		} else {
			Intent intent = new Intent(activity, NoInternetConnection.class);
			activity.startActivity(intent);
			activity.finish();
		}
		if (listTab != null) {
			for (int i = 0; i < listTab.size(); i++) {
				tabs1[i] = listTab.get(i).getName();
				Log.e("Adding    ==== aT   " + i, "added = "
						+ listTab.get(i).getName());
			}
		} else {
			Intent intent = new Intent(activity, NoInternetConnection.class);
			activity.startActivity(intent);
			activity.finish();
		}

	}

	@Override
	public int getCount() {
		if (tabs1 != null) {
			return tabs1.length;
		} else {
			Intent intent = new Intent(activity, Splash.class);
			activity.startActivity(intent);
			activity.finish();
			return 0;
		}

	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return o == view;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabs1[position];
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// Inflate a new layout from our resources
		View view = null;
		switch (position) {
		case 0:
			// Current
			if (Constant.AppID.equals("Google")) {
				view = activity.getLayoutInflater().inflate(
						R.layout.homepage_karbon, container, false);
				container.addView(view);
				showHomePageKARBON(view);
			} else {
				view = activity.getLayoutInflater().inflate(R.layout.homepage,
						container, false);
				container.addView(view);
				if (isNetworkAvailable()) {
					String switchStatus = PreferenceManager
							.getDefaultSharedPreferences(activity).getString(
									"MYSWITCH", "defaultStringIfNothingFound");

					if (switchStatus.equals("OFF")) {
						showHomePageWhenSwitchOff(view);
					} else {
						new DownloadHomePageData(view).execute();
					}

				} else {
					Intent intent = new Intent(activity,
							NoInternetConnection.class);
					activity.startActivity(intent);
					activity.finish();
				}
			}

			break;
		case 1:
			// Current
			view = activity.getLayoutInflater().inflate(R.layout.current_app,
					container, false);

			container.addView(view);

			if (PreferenceManager.getDefaultSharedPreferences(activity)
					.getString("getCurrent", "NothingFound")
					.equals("NothingFound")) {
				if (isNetworkAvailable()) {
					Log.e("Boxer ", "Loading ...");
					new GetCurrent(view).execute();
				} else {
					Intent intent = new Intent(activity,
							NoInternetConnection.class);
					activity.startActivity(intent);
					activity.finish();
				}

			} else {
				Log.e("Boxer ", "from prefrences");
				try {
					JSONObject jsonObject = new JSONObject(PreferenceManager
							.getDefaultSharedPreferences(activity).getString(
									"getCurrent", "NothingFound"));
					if (jsonObject != null) {

						JSONArray jsonArrayCurrent = jsonObject
								.optJSONArray("Data");
						for (int i = 0; i < jsonArrayCurrent.length(); i++) {
							jsObjectArr = jsonArrayCurrent.getJSONObject(i);
							loadCurrent(jsObjectArr, view);
						}
					} else {
						Toast.makeText(
								activity,
								activity.getResources().getString(
										R.string.Something_went_wrong), 3)
								.show();
						new GetCurrent(view).execute();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(
							activity,
							activity.getResources().getString(
									R.string.Something_went_wrong), 3).show();
					new GetCurrent(view).execute();
					e.printStackTrace();
				}

			}

			break;
		case 2:
			// Previous

			view = activity.getLayoutInflater().inflate(R.layout.pager_item,
					container, false);
			container.addView(view);
			GridView gridViewPrevious = (GridView) view
					.findViewById(R.id.gridView);
			if (PreferenceManager.getDefaultSharedPreferences(activity)
					.getString("listPrevious", "NothingFound")
					.equals("NothingFound")) {
				if (isNetworkAvailable()) {
					listPrevious.clear();
					new GetProduct(view, Constant.serverPath + "/Previous.php",
							gridViewPrevious, listPrevious).execute();
				} else {
					Intent intent = new Intent(activity,
							NoInternetConnection.class);
					activity.startActivity(intent);
					activity.finish();
				}

			} else {
				listPrevious.clear();
				try {
					JSONObject jsonObject = new JSONObject(PreferenceManager
							.getDefaultSharedPreferences(activity).getString(
									"listPrevious", "NothingFound"));
					JSONArray jsonArrayTopContent3 = jsonObject
							.optJSONArray("Data");
					for (int i = 0; i < jsonArrayTopContent3.length(); i++) {
						JSONObject jsObjectArr = jsonArrayTopContent3
								.getJSONObject(i);
						DealProduct dealProduct = new DealProduct();

						dealProduct.setCategory(jsObjectArr
								.optString("category"));
						dealProduct.setIcon(Constant.fileFolder
								+ jsObjectArr.optString("icon"));
						dealProduct.setId(jsObjectArr.optString("id"));
						dealProduct.setTitle(jsObjectArr.optString("title"));
						dealProduct.setPack(jsObjectArr.optString("package"));
						dealProduct.setRank(jsObjectArr.optString("Rank"));
						dealProduct.setUrl(jsObjectArr.optString("glink"));
						dealProduct.setDescription(jsObjectArr
								.optString("description"));
						dealProduct.setPrice(jsObjectArr.optString("price"));
						dealProduct.setDate(jsObjectArr.optString("date"));
						dealProduct
								.setCompany(jsObjectArr.optString("company"));
						dealProduct.setScreen(jsObjectArr.optString("screen"));
						dealProduct.setApk(jsObjectArr.optString("apk"));
						dealProduct.setBoxerversion(jsObjectArr
								.optString("Boxerversion"));
						dealProduct.setEmail(jsObjectArr.optString("email"));
						dealProduct.setAppid(jsObjectArr.optString("appid"));

						listPrevious.add(dealProduct);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DealProductAdapter dealProductAdapter = new DealProductAdapter(
						activity, listPrevious);
				gridViewPrevious.setAdapter(dealProductAdapter);
			}
			gridViewPrevious.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.i("BIBHU", "handle click 1");
					handleItemClick(listPrevious, position, "pro");
				}
			});

			break;
		case 3:
			// HelloTunes
			view = activity.getLayoutInflater().inflate(R.layout.pager_item,
					container, false);
			container.addView(view);
			GridView gridViewHelloTunes = (GridView) view
					.findViewById(R.id.gridView);

			if (PreferenceManager.getDefaultSharedPreferences(activity)
					.getString("listTop1", "NothingFound")
					.equals("NothingFound")) {
				listTop1.clear();
				String url = Constant.serverPath + "/Top_content1.php";
				if (isNetworkAvailable()) {
					new GetProduct(view, url, gridViewHelloTunes, listTop1)
							.execute();
				} else {
					Intent intent = new Intent(activity,
							NoInternetConnection.class);
					activity.startActivity(intent);
					activity.finish();
				}

			} else {
				listTop1.clear();
				try {
					JSONObject jsonObject = new JSONObject(PreferenceManager
							.getDefaultSharedPreferences(activity).getString(
									"listTop1", "NothingFound"));
					JSONArray jsonArrayTopContent3 = jsonObject
							.optJSONArray("Data");
					for (int i = 0; i < jsonArrayTopContent3.length(); i++) {
						JSONObject jsObjectArr = jsonArrayTopContent3
								.getJSONObject(i);
						DealProduct dealProduct = new DealProduct();

						dealProduct.setCategory(jsObjectArr
								.optString("category"));
						dealProduct.setIcon(Constant.fileFolder
								+ jsObjectArr.optString("icon"));
						dealProduct.setId(jsObjectArr.optString("id"));
						dealProduct.setTitle(jsObjectArr.optString("title"));
						dealProduct.setPack(jsObjectArr.optString("package"));
						dealProduct.setRank(jsObjectArr.optString("Rank"));
						dealProduct.setUrl(jsObjectArr.optString("glink"));
						dealProduct.setDescription(jsObjectArr
								.optString("description"));
						dealProduct.setPrice(jsObjectArr.optString("price"));
						dealProduct.setDate(jsObjectArr.optString("date"));
						dealProduct
								.setCompany(jsObjectArr.optString("company"));
						dealProduct.setScreen(jsObjectArr.optString("screen"));
						dealProduct.setApk(jsObjectArr.optString("apk"));
						dealProduct.setBoxerversion(jsObjectArr
								.optString("Boxerversion"));
						dealProduct.setEmail(jsObjectArr.optString("email"));
						dealProduct.setAppid(jsObjectArr.optString("appid"));

						listTop1.add(dealProduct);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DealProductAdapter dealProductAdapter = new DealProductAdapter(
						activity, listTop1);
				gridViewHelloTunes.setAdapter(dealProductAdapter);
			}
			gridViewHelloTunes
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Log.i("BIBHU", "handle click 2");
							handleItemClick(listTop1, position, "top");
						}
					});

			break;
		case 4:
			// Fantastics
			view = activity.getLayoutInflater().inflate(R.layout.pager_item,
					container, false);
			container.addView(view);
			GridView gridViewFantastics = (GridView) view
					.findViewById(R.id.gridView);
			if (PreferenceManager.getDefaultSharedPreferences(activity)
					.getString("listTop2", "NothingFound")
					.equals("NothingFound")) {

				if (isNetworkAvailable()) {
					listTop2.clear();
					new GetProduct(view, Constant.serverPath
							+ "/Top_content2.php", gridViewFantastics, listTop2)
							.execute();
				} else {
					Intent intent = new Intent(activity,
							NoInternetConnection.class);
					activity.startActivity(intent);
					activity.finish();
				}
			} else {
				listTop2.clear();
				try {
					JSONObject jsonObject = new JSONObject(PreferenceManager
							.getDefaultSharedPreferences(activity).getString(
									"listTop2", "NothingFound"));
					JSONArray jsonArrayTopContent3 = jsonObject
							.optJSONArray("Data");
					for (int i = 0; i < jsonArrayTopContent3.length(); i++) {
						JSONObject jsObjectArr = jsonArrayTopContent3
								.getJSONObject(i);
						DealProduct dealProduct = new DealProduct();

						dealProduct.setCategory(jsObjectArr
								.optString("category"));
						dealProduct.setIcon(Constant.fileFolder
								+ jsObjectArr.optString("icon"));
						dealProduct.setId(jsObjectArr.optString("id"));
						dealProduct.setTitle(jsObjectArr.optString("title"));
						dealProduct.setPack(jsObjectArr.optString("package"));
						dealProduct.setRank(jsObjectArr.optString("Rank"));
						dealProduct.setUrl(jsObjectArr.optString("glink"));
						dealProduct.setDescription(jsObjectArr
								.optString("description"));
						dealProduct.setPrice(jsObjectArr.optString("price"));
						dealProduct.setDate(jsObjectArr.optString("date"));
						dealProduct
								.setCompany(jsObjectArr.optString("company"));
						dealProduct.setScreen(jsObjectArr.optString("screen"));
						dealProduct.setApk(jsObjectArr.optString("apk"));
						dealProduct.setBoxerversion(jsObjectArr
								.optString("Boxerversion"));
						dealProduct.setEmail(jsObjectArr.optString("email"));
						dealProduct.setAppid(jsObjectArr.optString("appid"));

						listTop2.add(dealProduct);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DealProductAdapter dealProductAdapter = new DealProductAdapter(
						activity, listTop2);
				gridViewFantastics.setAdapter(dealProductAdapter);
			}
			gridViewFantastics
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Log.i("BIBHU", "handle click 3");
							handleItemClick(listTop2, position, "top");
						}
					});

			break;
		case 5:
			// Try This
			view = activity.getLayoutInflater().inflate(R.layout.pager_item,
					container, false);
			container.addView(view);
			GridView gridView = (GridView) view.findViewById(R.id.gridView);
			if (PreferenceManager.getDefaultSharedPreferences(activity)
					.getString("listTop3", "NothingFound")
					.equals("NothingFound")) {

				if (isNetworkAvailable()) {
					listTop3.clear();
					new GetProduct(view, Constant.serverPath
							+ "/Top_content3.php", gridView, listTop3)
							.execute();
				} else {
					Intent intent = new Intent(activity,
							NoInternetConnection.class);
					activity.startActivity(intent);
					activity.finish();
				}
			} else {
				listTop3.clear();
				try {
					JSONObject jsonObject = new JSONObject(PreferenceManager
							.getDefaultSharedPreferences(activity).getString(
									"listTop3", "NothingFound"));
					JSONArray jsonArrayTopContent3 = jsonObject
							.optJSONArray("Data");
					for (int i = 0; i < jsonArrayTopContent3.length(); i++) {
						JSONObject jsObjectArr = jsonArrayTopContent3
								.getJSONObject(i);
						DealProduct dealProduct = new DealProduct();

						dealProduct.setCategory(jsObjectArr
								.optString("category"));
						dealProduct.setIcon(Constant.fileFolder
								+ jsObjectArr.optString("icon"));
						dealProduct.setId(jsObjectArr.optString("id"));
						dealProduct.setTitle(jsObjectArr.optString("title"));
						dealProduct.setPack(jsObjectArr.optString("package"));
						dealProduct.setRank(jsObjectArr.optString("Rank"));
						dealProduct.setUrl(jsObjectArr.optString("glink"));
						dealProduct.setDescription(jsObjectArr
								.optString("description"));
						dealProduct.setPrice(jsObjectArr.optString("price"));
						dealProduct.setDate(jsObjectArr.optString("date"));
						dealProduct
								.setCompany(jsObjectArr.optString("company"));
						dealProduct.setScreen(jsObjectArr.optString("screen"));
						dealProduct.setApk(jsObjectArr.optString("apk"));
						dealProduct.setBoxerversion(jsObjectArr
								.optString("Boxerversion"));
						dealProduct.setEmail(jsObjectArr.optString("email"));
						dealProduct.setAppid(jsObjectArr.optString("appid"));

						listTop3.add(dealProduct);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DealProductAdapter dealProductAdapter = new DealProductAdapter(
						activity, listTop3);
				gridView.setAdapter(dealProductAdapter);
			}
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.i("BIBHU", "handle click 4");
					handleItemClick(listTop3, position, "top");
				}
			});

			break;
		default:
			view = activity.getLayoutInflater().inflate(R.layout.pager_item_2,
					container, false);
			container.addView(view);

			// Retrieve a TextView from the inflated View, and update it's text
			TextView title = (TextView) view.findViewById(R.id.item_title);
			title.setText(tabs1[position]);
			break;
		}

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	class GetProduct extends AsyncTask<Void, Void, Void> {
		GridView gridView;
		View view;
		String link, arrayName;
		List<DealProduct> list;
		ProgressBar progressBar;

		public GetProduct(View view, String link, GridView gridView,
				List<DealProduct> list) {
			// TODO Auto-generated constructor stub
			this.view = view;
			this.gridView = gridView;
			this.list = list;
			this.link = link;
			this.arrayName = arrayName;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar = (ProgressBar) view
					.findViewById(R.id.circularProgressBar);
			progressBar.setVisibility(View.VISIBLE);
			// dialog = ProgressDialog.show(activity, "", "Loading next...",
			// false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// ServiceHandler sh = new ServiceHandler();
			// jsonStr = sh.makeServiceCall(link, ServiceHandler.GET);
			postData(link);
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				JSONArray jsonArrayTopContent3 = jsonObject
						.optJSONArray("Data");
				for (int i = 0; i < jsonArrayTopContent3.length(); i++) {
					JSONObject jsObjectArr = jsonArrayTopContent3
							.getJSONObject(i);
					DealProduct dealProduct = new DealProduct();

					dealProduct.setCategory(jsObjectArr.optString("category"));
					dealProduct.setIcon(Constant.fileFolder
							+ jsObjectArr.optString("icon"));
					dealProduct.setId(jsObjectArr.optString("id"));
					dealProduct.setTitle(jsObjectArr.optString("title"));
					dealProduct.setPack(jsObjectArr.optString("package"));
					dealProduct.setRank(jsObjectArr.optString("Rank"));
					dealProduct.setUrl(jsObjectArr.optString("glink"));
					dealProduct.setDescription(jsObjectArr
							.optString("description"));
					dealProduct.setPrice(jsObjectArr.optString("price"));
					dealProduct.setDate(jsObjectArr.optString("date"));
					dealProduct.setCompany(jsObjectArr.optString("company"));
					dealProduct.setScreen(jsObjectArr.optString("screen"));
					dealProduct.setApk(jsObjectArr.optString("apk"));
					dealProduct.setBoxerversion(jsObjectArr
							.optString("Boxerversion"));
					dealProduct.setEmail(jsObjectArr.optString("email"));
					dealProduct.setAppid(jsObjectArr.optString("appid"));

					this.list.add(dealProduct);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);
			// if (dialog.isShowing()) {
			// dialog.dismiss();
			// }
			Log.e("Json Found post execute", "" + jsonStr);

			DealProductAdapter dealProductAdapter = new DealProductAdapter(
					activity, list);
			gridView.setAdapter(dealProductAdapter);
			if (list == listPrevious) {
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("listPrevious", "" + jsonStr).commit();
			} else if (list == listTop1) {
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("listTop1", "" + jsonStr).commit();
			} else if (list == listTop2) {
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("listTop2", "" + jsonStr).commit();
			} else if (list == listTop3) {
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("listTop3", "" + jsonStr).commit();
			}
		}
	}

	class GetCurrent extends AsyncTask<Void, Void, Void> {
		View view;
		ProgressBar progressBar;

		public GetCurrent(View view) {
			// TODO Auto-generated constructor stub
			this.view = view;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar = (ProgressBar) view
					.findViewById(R.id.circularProgressBar);
			progressBar.setVisibility(View.VISIBLE);
			// dialog = ProgressDialog.show(activity, "", "Loading...", false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// ServiceHandler sh = new ServiceHandler();
			// jsonStr = sh.makeServiceCall(Constant.serverPath +
			// "/Current.php",
			// ServiceHandler.GET);
			Log.e("link", Constant.serverPath + "/Current.php");
			postData(Constant.serverPath + "/Current.php");
			Log.e("Current App ", "I am json of current app " + jsonStr);

			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				if (jsonObject != null) {

					JSONArray jsonArrayCurrent = jsonObject
							.optJSONArray("Data");
					for (int i = 0; i < jsonArrayCurrent.length(); i++) {
						jsObjectArr = jsonArrayCurrent.getJSONObject(i);

					}
				} else {

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);
			PreferenceManager.getDefaultSharedPreferences(activity).edit()
					.putString("getCurrent", "" + jsonStr).commit();
			if (jsObjectArr != null) {

				loadCurrent(jsObjectArr, view);
			}
		}
	}

	void postData(String url) {
		Log.e("link ", url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("appId", Constant.AppID));
			nameValuePairs.add(new BasicNameValuePair("BoxerVersion",
					Constant.App_Version));
			nameValuePairs.add(new BasicNameValuePair("locale", Locale
					.getDefault().getLanguage()));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.e("Post Data ", nameValuePairs.toString());
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			jsonStr = new BasicResponseHandler().handleResponse(response);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	void handleItemClick(List<DealProduct> list, int position, String script) {
		Log.e("product id is ", "" + list.get(position).getId());
		Log.e("App id is ", "" + list.get(position).getAppid());

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
		// if (script.equals("pro")) {
		// new ClickCountAsyncTask(list.get(position).getId(), "ProClick.php")
		// .execute();
		// } else {
		// new ClickCountAsyncTask(list.get(position).getId(), "topClick.php")
		// .execute();
		// }
		//pd = ProgressDialog.show(activity, "", "Please Wait", true);

		if (list.get(position).getUrl().equalsIgnoreCase("")) {
			
			Toast.makeText(activity,"download ing appp 1", Toast.LENGTH_LONG).show();
			Intent in = new Intent(activity, AppDetail.class);
			in.putExtra("pack", list.get(position).getPack());
			activity.startActivity(in);

		} else {
			if (list.get(position).getUrl().contains("market://details?id=")) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(list.get(position).getUrl())));
				Toast.makeText(activity,"download ing appp 2", Toast.LENGTH_LONG).show();
				
				return;
			}
			
			//Toast.makeText(activity,"download ing appp 3", Toast.LENGTH_LONG).show();
			
			MainActivity.mainUrl = list.get(position).getUrl();
			Intent intent = new Intent(activity, B_MainActivity.class);
			activity.startActivity(intent);

			// Intent intent = new Intent(activity, MyWebView.class);
			// intent.putExtra("webLink", list.get(position).getUrl());
			// activity.startActivity(intent);

		}

	}

	class ClickCountAsyncTask extends AsyncTask<Void, Void, Void> {
		String responce = "";
		String imei = "";
		String scriptName = "";
		String Id = "";

		public ClickCountAsyncTask(String id, String scriptName) {
			// TODO Auto-generated constructor stub
			this.Id = id;
			this.scriptName = scriptName;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(activity, "", "Please Wait", true);
		}

		public void postData() {
			// Toast.makeText(getApplicationContext(), ""+email+" "+feedback,
			// Toast.LENGTH_SHORT).show();

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost httppost = new
			// HttpPost(Constant.serverPath+"/feedback.php");
			HttpPost httppost = new HttpPost(Constant.serverPath + "/"
					+ scriptName);
			// a. IMEI
			// b. GID
			// c. AID
			// d. Country
			// e. Handset

			try {
				// Add your data

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);

				nameValuePairs.add(new BasicNameValuePair("id", Id));
				nameValuePairs.add(new BasicNameValuePair("BoxerVersion",
						Constant.App_Version));
				nameValuePairs.add(new BasicNameValuePair("appId",
						Constant.AppID));
				nameValuePairs.add(new BasicNameValuePair("AID", HashGenerator
						.encrypt(AID, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Lat", HashGenerator
						.encrypt("" + Splash.lat, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Lang", HashGenerator
						.encrypt("" + Splash.lng, Constant.EncriptionKey)));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				responce = new BasicResponseHandler().handleResponse(response);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			postData();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null) {
				if (pd.isShowing()) {
					pd.dismiss();
				}
			}

			Log.e("Click Responce of " + scriptName, "Responce " + responce);
			// Toast.makeText(activity, "Responce " + responce, 3).show();

		}
	}

	void loadCurrent(final JSONObject jsObjectArr, View view) {
		Typeface tfRegular = Typeface.createFromAsset(activity.getAssets(),
				Constant.FontTitilliumWebSemiBold);
		Typeface tfBold = Typeface.createFromAsset(activity.getAssets(),
				Constant.FontTitilliumWebBold);
		if (jsObjectArr != null) {
			isLoded = true;
			Log.e("Json Found", "" + jsonStr);

			TextView tvAppTitel = (TextView) view.findViewById(R.id.tvAppTitel);
			tvAppTitel.setTypeface(tfBold);
			tvAppTitel.setText(jsObjectArr.optString("title"));
			TextView tvAppDesc = (TextView) view.findViewById(R.id.tvAppDesc);
			tvAppDesc.setTypeface(tfRegular);
			Log.e("length", "" + jsObjectArr.optString("description").length());
			tvAppDesc.setText(jsObjectArr.optString("description"));
			TextView tvAppRate = (TextView) view.findViewById(R.id.tvAppRate);
			tvAppRate.setTypeface(tfBold);
			if (jsObjectArr.optString("price").equals("Free")
					|| jsObjectArr.optString("price").equals("0")) {
				tvAppRate.setText(activity.getResources().getString(
						R.string.Free));
			} else
				tvAppRate.setText("₹" + jsObjectArr.optString("price"));

			tvAppRate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// handleItemClick(list, position)

					new ClickCountAsyncTask(jsObjectArr.optString("id"),
							"ProClick.php").execute();

					if (jsObjectArr.optString("glink").equalsIgnoreCase("")) {
						Intent in = new Intent(activity, AppDetail.class);
						in.putExtra("pack", jsObjectArr.optString("package"));
						activity.startActivity(in);

					} else {
						if (jsObjectArr.optString("glink").contains("market")) {
							Intent i = new Intent(Intent.ACTION_VIEW);
							activity.startActivity(new Intent(
									Intent.ACTION_VIEW, Uri.parse(jsObjectArr
											.optString("glink"))));
							return;
						}
						MainActivity.mainUrl = jsObjectArr.optString("glink");
						Intent intent = new Intent(activity,
								B_MainActivity.class);
						activity.startActivity(intent);

						// Intent intent = new Intent(activity,
						// MyWebView.class);
						// intent.putExtra("webLink",
						// jsObjectArr.optString("glink"));
						// activity.startActivity(intent);

					}

				}
			});
			view.findViewById(R.id.llMainCurrent).setOnTouchListener(
					new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							InputMethodManager inputMethodManager = (InputMethodManager) activity
									.getSystemService(Activity.INPUT_METHOD_SERVICE);
							inputMethodManager.hideSoftInputFromWindow(activity
									.getCurrentFocus().getWindowToken(), 0);
							return false;
						}
					});
			ImageView ivImageCurrent = (ImageView) view
					.findViewById(R.id.ivImageCurrent);
			ivImageCurrent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					// handleItemClick(list, position)

					new ClickCountAsyncTask(jsObjectArr.optString("id"),
							"ProClick.php").execute();

					if (jsObjectArr.optString("glink").equalsIgnoreCase("")) {
						Intent in = new Intent(activity, AppDetail.class);
						in.putExtra("pack", jsObjectArr.optString("package"));
						activity.startActivity(in);

					} else {
						if (jsObjectArr.optString("glink").contains("market")) {
							Intent i = new Intent(Intent.ACTION_VIEW);
							activity.startActivity(new Intent(
									Intent.ACTION_VIEW, Uri.parse(jsObjectArr
											.optString("glink"))));
							return;
						}
						MainActivity.mainUrl = jsObjectArr.optString("glink");
						Intent intent = new Intent(activity,
								B_MainActivity.class);
						activity.startActivity(intent);
						// Intent intent = new Intent(activity,
						// MyWebView.class);
						// intent.putExtra("webLink",
						// jsObjectArr.optString("glink"));
						// activity.startActivity(intent);

					}

				}
			});
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.displayer(new RoundedBitmapDisplayer(15))
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			imageLoader.displayImage(
					Constant.fileFolder + jsObjectArr.optString("icon"),
					ivImageCurrent, options);
		}
	}

	@SuppressWarnings("deprecation")
	private void showHomePageWhenSwitchOff(final View view) {

		Typeface tfRegular = Typeface.createFromAsset(activity.getAssets(),
				Constant.FontTitilliumWebSemiBold);
		Typeface tfBold = Typeface.createFromAsset(activity.getAssets(),
				Constant.FontTitilliumWebBold);
		Typeface tfRegularPlain = Typeface.createFromAsset(
				activity.getAssets(), Constant.FontTitilliumWebRegular);
		LinearLayout llHomeSearch = (LinearLayout) view
				.findViewById(R.id.llHomeSearch);
		// TODO Auto-generated method stub
		ImageView ivHomeBG = (ImageView) view.findViewById(R.id.ivHomePageBg);
		// LinearLayout llText = (LinearLayout) view.findViewById(R.id.llText);
		TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
		TextView tvHeaderTouchPoint = (TextView) view
				.findViewById(R.id.tvHeaderTouchPoint);
		TextView tvMySwitchText = (TextView) view
				.findViewById(R.id.tvMySwitchText);
		tvMySwitchText.setTypeface(tfRegular);
		SlidingDrawer slidingDrawer1 = (SlidingDrawer) view
				.findViewById(R.id.slidingDrawer1);
		if (Constant.AppID.equals("Karbon")) {
			llHomeSearch.setGravity(Gravity.CENTER_VERTICAL);
			slidingDrawer1.setVisibility(View.GONE);
		}
		final ImageView ivDrawerIcon = (ImageView) view
				.findViewById(R.id.ivDrawerIcon);
		ivDrawerIcon.setImageResource(R.drawable.arrow_up);
		TextView tvThemeText = (TextView) view.findViewById(R.id.tvThemeText);
		TextView tvReadMore = (TextView) view.findViewById(R.id.tvReadMore);
		tvThemeText.setTypeface(tfRegular);
		TextView tvDownload = (TextView) view.findViewById(R.id.tvDownload);
		tvDownload.setTypeface(tfRegular);
		tvDownload.setVisibility(View.GONE);
		tvReadMore.setTypeface(tfBold);
		tvReadMore.setVisibility(View.GONE);
		TextView tvDownloadHere = (TextView) view
				.findViewById(R.id.tvDownloadHere);
		tvDownloadHere.setTypeface(tfRegular);
		tvDownloadHere.setVisibility(View.GONE);
		tvHeader.setText("Search what you want, go Online or just Swipe. Discover the fascinating World of Internet on Boxer Internet");
		tvHeaderTouchPoint.setText("India's Internet");

		final EditText edtSearchHomepage = (EditText) view
				.findViewById(R.id.edtSearchAtHomepage);
		ImageView ivButtonSearch = (ImageView) view
				.findViewById(R.id.ivBtnSearch);
		final ImageView ivTheme = (ImageView) view.findViewById(R.id.ivTheme);
		final ImageView ivTheme2 = (ImageView) view.findViewById(R.id.ivTheme2);
		final ImageView ivTheme3 = (ImageView) view.findViewById(R.id.ivTheme3);
		final ImageView ivTheme4 = (ImageView) view.findViewById(R.id.ivTheme4);
		final ImageView ivTheme5 = (ImageView) view.findViewById(R.id.ivTheme5);
		final LinearLayout llTouchPoint = (LinearLayout) view
				.findViewById(R.id.llTouchPoint);
		final LinearLayout llContent = (LinearLayout) view
				.findViewById(R.id.content);
		final LinearLayout llDownload = (LinearLayout) view
				.findViewById(R.id.llDownload);

		@SuppressWarnings("deprecation")
		SlidingDrawer slidingDrawer = (SlidingDrawer) view
				.findViewById(R.id.slidingDrawer1);

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				ivDrawerIcon.setImageResource(R.drawable.arrow_down);
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);

				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatDrawerOpen)
						.withCurrencyCode("INR")
						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
			}
		});
		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				ivDrawerIcon.setImageResource(R.drawable.arrow_up);

			}
		});
		String selected_colr = PreferenceManager.getDefaultSharedPreferences(
				activity).getString("MYCOLOR", "3");
		if (selected_colr.equals("1")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme));
			llContent.invalidate();
		} else if (selected_colr.equals("2")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme2));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme2));
			llContent.invalidate();
		} else if (selected_colr.equals("3")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme3));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme3));
			llContent.invalidate();
		} else if (selected_colr.equals("4")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme4));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme4));
			llContent.invalidate();
		} else if (selected_colr.equals("5")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme5));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme5));
			llContent.invalidate();
		}
		tvHeader.setTypeface(tfBold);
		tvHeaderTouchPoint.setTypeface(tfBold);
		// tvButtonSearch.setTypeface(tfBold);
		edtSearchHomepage.setTypeface(tfRegularPlain);
		edtSearchHomepage
				.setOnEditorActionListener(new OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {

						// TODO Auto-generated method stub

						MobileAppTracker mobileAppTracker = MobileAppTracker
								.getInstance();
						mobileAppTracker.setUserId("userId");
						mobileAppTracker.setFacebookUserId("facebookUserId");
						mobileAppTracker.setGoogleUserId("googleUserId");
						mobileAppTracker.setTwitterUserId("twitterUserId");
						mobileAppTracker.measureEvent(new MATEvent(
								MATEvent.SEARCH)
								.withCurrencyCode("INR")

								.withSearchString(
										edtSearchHomepage.getText().toString())
								.withDate1(
										new GregorianCalendar(2015, 4, 21)
												.getTime())
								.withDate2(
										new GregorianCalendar(2015, 4, 23)
												.getTime()).withQuantity(3));

						SharedPreferences sharedPreferences1 = PreferenceManager
								.getDefaultSharedPreferences(activity);
						engine = sharedPreferences1.getString("defaultengine",
								Constant.defaultEngine);

						System.out.println("defaultengine" + engine);

						String url = edtSearchHomepage.getText().toString();
						if (url.contains(".")) {
							mainUrl = "http://"
									+ edtSearchHomepage.getText().toString();
						} else {

							if (engine.equalsIgnoreCase("google")) {
								mainUrl = "https://google.co.in/search?as_q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());

							}

							else if (engine.equalsIgnoreCase("bing")) {
								mainUrl = "https://www.bing.com/search?q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							} else if (engine.equalsIgnoreCase("yahoo")) {
								mainUrl = "https://in.search.yahoo.com/search?p="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							} else if (engine.equalsIgnoreCase("ask")) {
								mainUrl = "http://www.ask.com/web?q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							}
						}
						MainActivity.mainUrl = mainUrl;
						Log.e("fetched text  ", "url " + mainUrl);
						Intent intent = new Intent(activity,
								B_MainActivity.class);
						activity.startActivity(intent);

						return false;
					}
				});
		ivTheme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);

				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "1").commit();
			}
		});
		ivTheme2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme2));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme2));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "2").commit();
			}
		});
		ivTheme3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme3));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme3));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "3").commit();
			}
		});
		ivTheme4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme4));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme4));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "4").commit();
			}
		});
		ivTheme5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme5));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme5));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "5").commit();
			}
		});

		ivButtonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();
				mobileAppTracker.setUserId("userId");
				mobileAppTracker.setFacebookUserId("facebookUserId");
				mobileAppTracker.setGoogleUserId("googleUserId");
				mobileAppTracker.setTwitterUserId("twitterUserId");
				mobileAppTracker.measureEvent(new MATEvent(MATEvent.SEARCH)
						.withCurrencyCode("INR")

						.withSearchString(
								edtSearchHomepage.getText().toString())
						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));

				SharedPreferences sharedPreferences1 = PreferenceManager
						.getDefaultSharedPreferences(activity);
				engine = sharedPreferences1.getString("defaultengine", "bing");

				System.out.println("defaultengine" + engine);

				String url = edtSearchHomepage.getText().toString();
				if (url.contains(".")) {
					mainUrl = "http://"
							+ edtSearchHomepage.getText().toString();
				} else {

					if (engine.equalsIgnoreCase("google")) {
						mainUrl = "https://google.co.in/search?as_q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());

					}

					else if (engine.equalsIgnoreCase("bing")) {
						mainUrl = "https://www.bing.com/search?q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					} else if (engine.equalsIgnoreCase("yahoo")) {
						mainUrl = "https://in.search.yahoo.com/search?p="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					} else if (engine.equalsIgnoreCase("ask")) {
						mainUrl = "http://www.ask.com/web?q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					}
				}
				MainActivity.mainUrl = mainUrl;
				Log.e("fetched text  ", "url " + mainUrl);
				Intent intent = new Intent(activity, B_MainActivity.class);
				activity.startActivity(intent);

			}
		});
		tvHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		Switch mySwitch;
		mySwitch = (Switch) view.findViewById(R.id.mySwitch);
		String switchStatus1 = PreferenceManager.getDefaultSharedPreferences(
				activity).getString("MYSWITCH", "defaultStringIfNothingFound");
		if (switchStatus1.equals("ON")) {
			mySwitch.setChecked(true);
		} else if (switchStatus1.equals("OFF")) {
			mySwitch.setChecked(false);
		} else {
			mySwitch.setChecked(true);
		}

		// set the switch to ON

		// attach a listener to check for changes in state
		mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					Toast.makeText(
							activity,
							activity.getResources().getString(
									R.string.Wallpaper_On), 3).show();
					PreferenceManager.getDefaultSharedPreferences(activity)
							.edit().putString("MYSWITCH", "ON").commit();
					new DownloadHomePageData(view).execute();
				} else {
					Toast.makeText(
							activity,
							activity.getResources().getString(
									R.string.Wallpaper_Off), 3).show();
					PreferenceManager.getDefaultSharedPreferences(activity)
							.edit().putString("MYSWITCH", "OFF").commit();
				}

			}
		});
	}

	@SuppressWarnings("deprecation")
	private void showHomePage(View view) {

		try {
			JSONObject homeObject = new JSONObject(responceString);
			if (homeObject != null) {

				imagePath = homeObject.optString("image");
				download_size = homeObject.optString("download_size");
				text = homeObject.optString("text");
				titel = homeObject.optString("title");
				url = homeObject.optString("url");
				downloadLink = homeObject.optString("download_link");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Typeface tfRegular = Typeface.createFromAsset(activity.getAssets(),
				Constant.FontTitilliumWebSemiBold);
		Typeface tfRegularPlain = Typeface.createFromAsset(
				activity.getAssets(), Constant.FontTitilliumWebRegular);

		Typeface tfBold = Typeface.createFromAsset(activity.getAssets(),
				Constant.FontTitilliumWebBold);
		final ImageView ivDrawerIcon = (ImageView) view
				.findViewById(R.id.ivDrawerIcon);
		ivDrawerIcon.setImageResource(R.drawable.arrow_up);

		// TODO Auto-generated method stub
		ImageView ivHomeBG = (ImageView) view.findViewById(R.id.ivHomePageBg);

		// LinearLayout llText = (LinearLayout) view.findViewById(R.id.llText);
		final TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
		TextView tvHeaderTouchPoint = (TextView) view
				.findViewById(R.id.tvHeaderTouchPoint);
		TextView tvMySwitchText = (TextView) view
				.findViewById(R.id.tvMySwitchText);
		TextView tvThemeText = (TextView) view.findViewById(R.id.tvThemeText);
		final TextView tvReadMore = (TextView) view
				.findViewById(R.id.tvReadMore);
		tvThemeText.setTypeface(tfRegular);
		tvReadMore.setTypeface(tfBold);
		tvMySwitchText.setTypeface(tfRegular);
		TextView tvDownload = (TextView) view.findViewById(R.id.tvDownload);
		tvDownload.setTypeface(tfRegular);
		TextView tvDownloadHere = (TextView) view
				.findViewById(R.id.tvDownloadHere);
		tvDownloadHere.setTypeface(tfRegular);
		tvDownloadHere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// z
				if (downloadLink.equals("")) {

				} else {

					downloadFile = new DownloadFile();
					downloadFile.execute(Constant.fileFolder + downloadLink);
				}

			}
		});
		SlidingDrawer slidingDrawer = (SlidingDrawer) view
				.findViewById(R.id.slidingDrawer1);

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				ivDrawerIcon.setImageResource(R.drawable.arrow_down);

				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatDrawerOpen)
						.withCurrencyCode("INR")
						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
			}
		});
		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				ivDrawerIcon.setImageResource(R.drawable.arrow_up);

			}
		});
		if (text.equals("false")) {

		} else {
			tvHeader.setText(text);
			tvHeaderTouchPoint.setText(titel);
		}

		final EditText edtSearchHomepage = (EditText) view
				.findViewById(R.id.edtSearchAtHomepage);
		ImageView ivButtonSearch = (ImageView) view
				.findViewById(R.id.ivBtnSearch);
		final ImageView ivTheme = (ImageView) view.findViewById(R.id.ivTheme);
		final ImageView ivTheme2 = (ImageView) view.findViewById(R.id.ivTheme2);
		final ImageView ivTheme3 = (ImageView) view.findViewById(R.id.ivTheme3);
		final ImageView ivTheme4 = (ImageView) view.findViewById(R.id.ivTheme4);
		final ImageView ivTheme5 = (ImageView) view.findViewById(R.id.ivTheme5);
		final LinearLayout llTouchPoint = (LinearLayout) view
				.findViewById(R.id.llTouchPoint);
		final LinearLayout llContent = (LinearLayout) view
				.findViewById(R.id.content);
		final LinearLayout llText = (LinearLayout) view
				.findViewById(R.id.llText);
		String selected_colr = PreferenceManager.getDefaultSharedPreferences(
				activity).getString("MYCOLOR", "3");
		if (selected_colr.equals("1")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme));
			llContent.invalidate();
		} else if (selected_colr.equals("2")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme2));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme2));
			llContent.invalidate();
		} else if (selected_colr.equals("3")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme3));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme3));
			llContent.invalidate();
		} else if (selected_colr.equals("4")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme4));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme4));
			llContent.invalidate();
		} else if (selected_colr.equals("5")) {
			llTouchPoint.setBackgroundColor(activity.getResources().getColor(
					R.color.theme5));
			llTouchPoint.invalidate();
			llContent.setBackgroundColor(activity.getResources().getColor(
					R.color.theme5));
			llContent.invalidate();
		}
		tvHeader.setTypeface(tfBold);
		tvHeaderTouchPoint.setTypeface(tfBold);
		// tvButtonSearch.setTypeface(tfBold);
		edtSearchHomepage.setTypeface(tfRegularPlain);

		edtSearchHomepage
				.setOnEditorActionListener(new OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {

						// TODO Auto-generated method stub

						MobileAppTracker mobileAppTracker = MobileAppTracker
								.getInstance();
						mobileAppTracker.setUserId("userId");
						mobileAppTracker.setFacebookUserId("facebookUserId");
						mobileAppTracker.setGoogleUserId("googleUserId");
						mobileAppTracker.setTwitterUserId("twitterUserId");
						mobileAppTracker.measureEvent(new MATEvent(
								MATEvent.SEARCH)
								.withCurrencyCode("INR")

								.withSearchString(
										edtSearchHomepage.getText().toString())
								.withDate1(
										new GregorianCalendar(2015, 4, 21)
												.getTime())
								.withDate2(
										new GregorianCalendar(2015, 4, 23)
												.getTime()).withQuantity(3));

						SharedPreferences sharedPreferences1 = PreferenceManager
								.getDefaultSharedPreferences(activity);
						engine = sharedPreferences1.getString("defaultengine",
								Constant.defaultEngine);

						System.out.println("defaultengine" + engine);

						String url = edtSearchHomepage.getText().toString();
						if (url.contains(".")) {
							mainUrl = "http://"
									+ edtSearchHomepage.getText().toString();
						} else {

							if (engine.equalsIgnoreCase("google")) {
								mainUrl = "https://google.co.in/search?as_q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());

							}

							else if (engine.equalsIgnoreCase("bing")) {
								mainUrl = "https://www.bing.com/search?q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							} else if (engine.equalsIgnoreCase("yahoo")) {
								mainUrl = "https://in.search.yahoo.com/search?p="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							} else if (engine.equalsIgnoreCase("ask")) {
								mainUrl = "http://www.ask.com/web?q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							}
						}
						MainActivity.mainUrl = mainUrl;
						Log.e("fetched text  ", "url " + mainUrl);
						Intent intent = new Intent(activity,
								B_MainActivity.class);
						activity.startActivity(intent);

						return false;
					}
				});
		// ivHomeBG.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// Log.e("onTouch ", "Calling");
		//
		// View view = activity.getCurrentFocus();
		// if (view != null) {
		// InputMethodManager imm = (InputMethodManager) activity
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		// }
		// return false;
		// }
		// });
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.white_bg)
				.showImageForEmptyUri(R.drawable.white_bg)
				.showImageOnFail(R.drawable.white_bg).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		String switchStatus = PreferenceManager.getDefaultSharedPreferences(
				activity).getString("MYSWITCH", "defaultStringIfNothingFound");
		if (switchStatus.equals("OFF")) {

		} else {
			if (imagePath != null) {
				ivHomeBG.setVisibility(View.VISIBLE);
				try {
					imageLoader.displayImage(

					Constant.fileFolder + imagePath, ivHomeBG, options);
					ivHomeBG.setScaleType(ImageView.ScaleType.CENTER_CROP);
				} catch (Exception e) {
					// TODO: handle exception
					ivHomeBG.setVisibility(View.GONE);
				}

			}
		}
		ivTheme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "1").commit();
			}
		});
		ivTheme2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();
				mobileAppTracker.setUserId("userId");

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme2));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme2));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "2").commit();
			}
		});
		ivTheme3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme3));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme3));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "3").commit();
			}
		});
		ivTheme4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme4));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme4));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "4").commit();
			}
		});
		ivTheme5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				llTouchPoint.setBackgroundColor(activity.getResources()
						.getColor(R.color.theme5));
				llTouchPoint.invalidate();
				llContent.setBackgroundColor(activity.getResources().getColor(
						R.color.theme5));
				llContent.invalidate();
				PreferenceManager.getDefaultSharedPreferences(activity).edit()
						.putString("MYCOLOR", "5").commit();
			}
		});

		ivButtonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();
				mobileAppTracker.setUserId("userId");
				mobileAppTracker.setFacebookUserId("facebookUserId");
				mobileAppTracker.setGoogleUserId("googleUserId");
				mobileAppTracker.setTwitterUserId("twitterUserId");
				mobileAppTracker.measureEvent(new MATEvent(MATEvent.SEARCH)
						.withCurrencyCode("INR")

						.withSearchString(
								edtSearchHomepage.getText().toString())
						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));

				SharedPreferences sharedPreferences1 = PreferenceManager
						.getDefaultSharedPreferences(activity);
				engine = sharedPreferences1.getString("defaultengine",
						Constant.defaultEngine);

				System.out.println("defaultengine" + engine);

				String url = edtSearchHomepage.getText().toString();
				if (url.contains(".")) {
					mainUrl = "http://"
							+ edtSearchHomepage.getText().toString();
				} else {

					if (engine.equalsIgnoreCase("google")) {
						mainUrl = "https://google.co.in/search?as_q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());

					}

					else if (engine.equalsIgnoreCase("bing")) {
						mainUrl = "https://www.bing.com/search?q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					} else if (engine.equalsIgnoreCase("yahoo")) {
						mainUrl = "https://in.search.yahoo.com/search?p="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					} else if (engine.equalsIgnoreCase("ask")) {
						mainUrl = "http://www.ask.com/web?q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					}
				}
				MainActivity.mainUrl = mainUrl;
				Log.e("fetched text  ", "url " + mainUrl);
				Intent intent = new Intent(activity, B_MainActivity.class);
				activity.startActivity(intent);

			}
		});
		llText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);

				mobileAppTracker.measureEvent(new MATEvent(
						Constant.MatColorPicked)
						.withCurrencyCode("INR")

						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));
				tvHeader.setSelected(true);
				tvReadMore.setSelected(true);
				if (url.contains(".")) {
					mainUrl = url;
				} else {

					if (engine.equalsIgnoreCase("google")) {
						mainUrl = "https://google.co.in/search?as_q="
								+ Uri.encode(url);

					}

					else if (engine.equalsIgnoreCase("bing")) {
						mainUrl = "https://www.bing.com/search?q="
								+ Uri.encode(url);
					} else if (engine.equalsIgnoreCase("yahoo")) {
						mainUrl = "https://in.search.yahoo.com/search?p="
								+ Uri.encode(url);
					} else if (engine.equalsIgnoreCase("ask")) {
						mainUrl = "http://www.ask.com/web?q=" + Uri.encode(url);
					}
				}
				MainActivity.mainUrl = mainUrl;
				Log.e("fetched text  ", "url " + mainUrl);
				Intent intent = new Intent(activity, B_MainActivity.class);
				activity.startActivity(intent);
			}
		});
		Switch mySwitch;
		mySwitch = (Switch) view.findViewById(R.id.mySwitch);
		String switchStatus1 = PreferenceManager.getDefaultSharedPreferences(
				activity).getString("MYSWITCH", "defaultStringIfNothingFound");
		if (switchStatus1.equals("ON")) {
			mySwitch.setChecked(true);
		} else if (switchStatus1.equals("OFF")) {
			mySwitch.setChecked(false);
		} else {
			mySwitch.setChecked(true);
		}
		Log.e("mySwitch status", switchStatus);
		// set the switch to ON

		// attach a listener to check for changes in state
		mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					Toast.makeText(activity, activity.getResources().getString(
							R.string.Wallpaper_On), 3).show();
					PreferenceManager.getDefaultSharedPreferences(activity)
							.edit().putString("MYSWITCH", "ON").commit();
				} else {
					Toast.makeText(activity, activity.getResources().getString(
							R.string.Wallpaper_Off), 3).show();
					PreferenceManager.getDefaultSharedPreferences(activity)
							.edit().putString("MYSWITCH", "OFF").commit();
				}

			}
		});
	}

	private void showHomePageKARBON(final View view) {

		Typeface tfRegularPlain = Typeface.createFromAsset(
				activity.getAssets(), Constant.FontTitilliumWebRegular);

		final EditText edtSearchHomepage = (EditText) view
				.findViewById(R.id.edtSearchAtHomepage);
		ImageView ivButtonSearch = (ImageView) view
				.findViewById(R.id.ivBtnSearch);
		edtSearchHomepage.setTypeface(tfRegularPlain);
		edtSearchHomepage
				.setOnEditorActionListener(new OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {

						// TODO Auto-generated method stub

						MobileAppTracker mobileAppTracker = MobileAppTracker
								.getInstance();

						mobileAppTracker.setGoogleUserId(Splash.email);

						mobileAppTracker.measureEvent(new MATEvent(
								MATEvent.SEARCH)
								.withCurrencyCode("INR")

								.withSearchString(
										edtSearchHomepage.getText().toString())
								.withDate1(
										new GregorianCalendar(2015, 4, 21)
												.getTime())
								.withDate2(
										new GregorianCalendar(2015, 4, 23)
												.getTime()).withQuantity(3));

						SharedPreferences sharedPreferences1 = PreferenceManager
								.getDefaultSharedPreferences(activity);
						engine = sharedPreferences1.getString("defaultengine",
								Constant.defaultEngine);

						System.out.println("defaultengine" + engine);

						String url = edtSearchHomepage.getText().toString();
						if (url.contains(".")) {
							mainUrl = "http://"
									+ edtSearchHomepage.getText().toString();
						} else {

							if (engine.equalsIgnoreCase("google")) {
								mainUrl = "https://google.co.in/search?as_q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());

							}

							else if (engine.equalsIgnoreCase("bing")) {
								mainUrl = "https://www.bing.com/search?q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							} else if (engine.equalsIgnoreCase("yahoo")) {
								mainUrl = "https://in.search.yahoo.com/search?p="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							} else if (engine.equalsIgnoreCase("ask")) {
								mainUrl = "http://www.ask.com/web?q="
										+ Uri.encode(edtSearchHomepage
												.getText().toString());
							}
						}
						MainActivity.mainUrl = mainUrl;
						Log.e("fetched text  ", "url " + mainUrl);
						Intent intent = new Intent(activity,
								B_MainActivity.class);
						activity.startActivity(intent);

						return false;
					}
				});
		ivButtonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);
				mobileAppTracker.measureEvent(new MATEvent(MATEvent.SEARCH)
						.withCurrencyCode("INR")

						.withSearchString(
								edtSearchHomepage.getText().toString())
						.withDate1(new GregorianCalendar(2015, 4, 21).getTime())
						.withDate2(new GregorianCalendar(2015, 4, 23).getTime())
						.withQuantity(3));

				SharedPreferences sharedPreferences1 = PreferenceManager
						.getDefaultSharedPreferences(activity);
				engine = sharedPreferences1.getString("defaultengine",
						Constant.defaultEngine);

				System.out.println("defaultengine" + engine);

				String url = edtSearchHomepage.getText().toString();
				if (url.contains(".")) {
					mainUrl = "http://"
							+ edtSearchHomepage.getText().toString();
				} else {

					if (engine.equalsIgnoreCase("google")) {
						mainUrl = "https://google.co.in/search?as_q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());

					}

					else if (engine.equalsIgnoreCase("bing")) {
						mainUrl = "https://www.bing.com/search?q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					} else if (engine.equalsIgnoreCase("yahoo")) {
						mainUrl = "https://in.search.yahoo.com/search?p="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					} else if (engine.equalsIgnoreCase("ask")) {
						mainUrl = "http://www.ask.com/web?q="
								+ Uri.encode(edtSearchHomepage.getText()
										.toString());
					}
				}
				MainActivity.mainUrl = mainUrl;
				Log.e("fetched text  ", "url " + mainUrl);
				Intent intent = new Intent(activity, B_MainActivity.class);
				activity.startActivity(intent);

			}
		});

	}

	class DownloadHomePageData extends AsyncTask<Void, Void, Void> {

		View view;

		DownloadHomePageData(View v) {
			this.view = v;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandler serHandler = new ServiceHandler();
			ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
			responceString = serHandler.makeServiceCall(Constant.serverPath
					+ "/homepage.php?appId=" + Constant.AppID + "&locale="
					+ Locale.getDefault().getLanguage(), Constant.GET,
					arrayList);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if (responceString != null) {
				showHomePage(view);
				Log.e("Homepage String id =========", " Responce is "
						+ responceString);

			} else {
				Log.e("Homepage String id =========", " Responce is null");
			}
			super.onPostExecute(result);
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	class DownloadFile extends AsyncTask<String, Integer, Long> {
		ProgressDialog mProgressDialog = new ProgressDialog(activity);// Change
																		// Mainactivity.this
																		// with
																		// your
																		// activity
																		// name.
		String strFolderName, imagePathResource;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.setMessage("Downloading " + "(" + download_size
					+ ")");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);

			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mProgressDialog.dismiss();
							downloadFile.cancel(true);
							Toast.makeText(
									activity,
									activity.getResources().getString(
											R.string.Download_Cancelled), 3)
									.show();
						}
					});
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mProgressDialog.dismiss();
						}
					});
			mProgressDialog.show();

		}

		@Override
		protected Long doInBackground(String... aurl) {
			int count;
			try {
				URL url = new URL((String) aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();
				String targetFileName = "/"
						+ downloadLink.substring(
								downloadLink.lastIndexOf("/") + 1,
								downloadLink.length());
				int lenghtOfFile = conexion.getContentLength();
				String PATH = Environment.getExternalStorageDirectory()
						+ "/Boxer";
				File folder = new File(PATH);
				if (!folder.exists()) {
					folder.mkdir();// If there is no folder it will be created.
				}
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(PATH
						+ targetFileName);
				imagePathResource = PATH + targetFileName;
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress((int) (total * 100 / lenghtOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			mProgressDialog.setProgress(progress[0]);
			if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
				mProgressDialog.dismiss();
				Toast.makeText(activity,
						activity.getResources().getString(
								R.string.FileDownloadedTo) + imagePathResource,
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.e("here in postexecute", "File Downloaded to "
					+ imagePathResource);
			Toast.makeText(activity,activity.getResources().getString(
					R.string.FileDownloadedTo)  + imagePathResource,
					Toast.LENGTH_LONG).show();
		}
	}
}