package com.boxer.browser;

import info.guardianproject.onionkit.ui.OrbotHelper;
import info.guardianproject.onionkit.web.WebkitProxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewDatabase;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.boxer.util.Constant;
import com.boxer.util.DatabaseURLHandler;
import com.boxer.util.HashGenerator;
import com.boxer.util.ID;
import com.boxer.util.Url;

public class B_BrowserActivity extends Activity implements BrowserController {

	public static boolean isTabOpen = false;
	private DrawerLayout mDrawerLayout;
	File file;
	boolean isReloading = false;
	int count = 0;
	String tempUrl = "";
	private ListView mDrawerList;

	private RelativeLayout mDrawer;

	private LinearLayout mDrawerRight;

	private ListView mDrawerListRight;

	private RelativeLayout mNewTab;

	private ActionBarDrawerToggle mDrawerToggle;

	private List<LightningView> mWebViews = new ArrayList<LightningView>();

	private List<Integer> mIdList = new ArrayList<Integer>();
	private List<String> mUrlArray = new ArrayList<String>();
	private List<ID> mUrl_ID_Array = new ArrayList<ID>();

	private LightningView mCurrentView;

	private int mIdGenerator;
	private int tabPosition = 0;

	private LightningViewAdapter mTitleAdapter;

	private List<HistoryItem> mBookmarkList;

	private BookmarkViewAdapter mBookmarkAdapter;

	private AutoCompleteTextView mSearch;

	private ClickHandler mClickHandler;

	private ProgressBar mProgressBar;

	private boolean mSystemBrowser = false;

	private ValueCallback<Uri> mUploadMessage;

	private View mCustomView;

	private int mOriginalOrientation;

	private int mActionBarSize;

	private ActionBar mActionBar;

	private boolean mFullScreen;

	private FrameLayout mBrowserFrame;

	private FullscreenHolder mFullscreenContainer;

	private CustomViewCallback mCustomViewCallback;

	private final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

	private Bitmap mDefaultVideoPoster;

	private View mVideoProgressView;

	private DatabaseHandler mHistoryHandler;

	private SQLiteDatabase mHistoryDatabase;

	private SharedPreferences mPreferences;

	private SharedPreferences.Editor mEditPrefs;

	private Context mContext;

	private Bitmap mWebpageBitmap;

	private String mSearchText;

	private Activity mActivity;

	private final int API = android.os.Build.VERSION.SDK_INT;

	private Drawable mDeleteIcon;

	private Drawable mRefreshIcon;

	private Drawable mCopyIcon;

	private Drawable mIcon;

	private int mActionBarSizeDp;

	private int mNumberIconColor;

	private String mHomepage;

	private boolean mIsNewIntent = false;

	private VideoView mVideoView;
	public static boolean isListLoading;
	private static SearchAdapter mSearchAdapter;
	TextView text1;
	Typeface tfSemiBoldItalic, tfRegular, tfPlain;
	String androidID, SearchString;
	DatabaseURLHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isTabOpen = true;
		db = new DatabaseURLHandler(this);
		// file = new File(Environment.getExternalStorageDirectory()
		// + File.separator + "list.ser");
		initialize();
		handleFooter();
		androidID = "" + Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		tfSemiBoldItalic = Typeface.createFromAsset(mActivity.getAssets(), Constant.FontTitilliumWebBold);
		tfRegular = Typeface.createFromAsset(this.getAssets(), Constant.FontTitilliumWebItalic);
		tfPlain = Typeface.createFromAsset(this.getAssets(), Constant.FontTitilliumWebRegular);
		String userAgent = new WebView(this).getSettings().getUserAgentString();
		// Log.e("user agent", "not null " + userAgent);
		handleDrawerMenu();
	}

	@SuppressWarnings("deprecation")
	private synchronized void initialize() {
		setContentView(R.layout.startbrowser);
		TypedValue typedValue = new TypedValue();
		Theme theme = getTheme();
		theme.resolveAttribute(R.attr.numberColor, typedValue, true);
		mNumberIconColor = typedValue.data;
		mPreferences = getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
		mEditPrefs = mPreferences.edit();
		mContext = this;
		if (mIdList != null) {
			mIdList.clear();
		} else {
			mIdList = new ArrayList<Integer>();
		}
		if (mWebViews != null) {
			mWebViews.clear();
		} else {
			mWebViews = new ArrayList<LightningView>();
		}

		mActivity = this;
		mClickHandler = new ClickHandler(this);
		mBrowserFrame = (FrameLayout) findViewById(R.id.content_frame);
		mProgressBar = (ProgressBar) findViewById(R.id.activity_bar);
		mProgressBar.setVisibility(View.VISIBLE);
		mNewTab = (RelativeLayout) findViewById(R.id.new_tab_button);

		text1 = (TextView) findViewById(R.id.text1);
		text1.setTypeface(tfSemiBoldItalic);
		mDrawer = (RelativeLayout) findViewById(R.id.drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setDivider(null);
		mDrawerList.setDividerHeight(0);
		mDrawerRight = (LinearLayout) findViewById(R.id.right_drawer);
		mDrawerListRight = (ListView) findViewById(R.id.right_drawer_list);
		mDrawerListRight.setDivider(null);
		mDrawerListRight.setDividerHeight(0);
		setNavigationDrawerWidth();
		mWebpageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_webpage);
		mActionBar = getActionBar();
		final TypedArray styledAttributes = mContext.getTheme()
				.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
		mActionBarSize = (int) styledAttributes.getDimension(0, 0);
		if (pixelsToDp(mActionBarSize) < 48) {
			mActionBarSize = getDp(48);
		}
		mActionBarSizeDp = pixelsToDp(mActionBarSize);
		styledAttributes.recycle();

		mHomepage = mPreferences.getString(PreferenceConstants.HOMEPAGE, B_Constants.HOMEPAGE);

		mTitleAdapter = new LightningViewAdapter(this, R.layout.tab_list_item, mWebViews);
		mDrawerList.setAdapter(mTitleAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerList.setOnItemLongClickListener(new DrawerItemLongClickListener());

		mBookmarkList = getBookmarks();
		mBookmarkAdapter = new BookmarkViewAdapter(this, R.layout.bookmark_list_item, mBookmarkList);
		mDrawerListRight.setAdapter(mBookmarkAdapter);
		mDrawerListRight.setOnItemClickListener(new BookmarkItemClickListener());
		mDrawerListRight.setOnItemLongClickListener(new BookmarkItemLongClickListener());

		if (mHistoryHandler == null) {
			mHistoryHandler = new DatabaseHandler(this);
		} else if (!mHistoryHandler.isOpen()) {
			mHistoryHandler = new DatabaseHandler(this);
		}
		mHistoryDatabase = mHistoryHandler.getReadableDatabase();

		// set display options of the ActionBar
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);

		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setCustomView(R.layout.b_search);// // action bar xml

		RelativeLayout back = (RelativeLayout) findViewById(R.id.action_back);
		RelativeLayout forward = (RelativeLayout) findViewById(R.id.action_forward);
		if (back != null) {
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mDrawerLayout.closeDrawer(mDrawer);
					if (mCurrentView != null) {
						if (mCurrentView.canGoBack()) {
							mCurrentView.goBack();
						} else {
							// deleteTab(mDrawerList.getCheckedItemPosition());
						}
					}
				}

			});
		}
		if (forward != null) {
			forward.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mDrawerLayout.closeDrawer(mDrawer);
					if (mCurrentView != null) {
						if (mCurrentView.canGoForward()) {
							mCurrentView.goForward();
						}
					}
				}

			});
		}

		// create the search EditText in the ActionBarm FontTitilliumWebRegular
		mSearch = (AutoCompleteTextView) mActionBar.getCustomView().findViewById(R.id.search);
		mSearch.setTypeface(tfPlain);
		mDeleteIcon = getResources().getDrawable(R.drawable.ic_action_delete);
		mDeleteIcon.setBounds(0, 0, B_Utils.convertToDensityPixels(mContext, 24),
				B_Utils.convertToDensityPixels(mContext, 24));
		mRefreshIcon = getResources().getDrawable(R.drawable.refresh_icon);
		mRefreshIcon.setBounds(0, 0, B_Utils.convertToDensityPixels(mContext, 18),
				B_Utils.convertToDensityPixels(mContext, 18));
		mCopyIcon = getResources().getDrawable(R.drawable.ic_action_copy);
		mCopyIcon.setBounds(0, 0, B_Utils.convertToDensityPixels(mContext, 24),
				B_Utils.convertToDensityPixels(mContext, 24));
		mIcon = mRefreshIcon;
		mSearch.setCompoundDrawables(null, null, mRefreshIcon, null);
		mSearch.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

				switch (arg1) {
				case KeyEvent.KEYCODE_ENTER:
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
					searchTheWeb(mSearch.getText().toString());

					if (mCurrentView != null) {
						mCurrentView.requestFocus();
					}
					return true;
				default:
					break;
				}
				return false;
			}

		});
		mSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus && mCurrentView != null) {
					if (mCurrentView != null) {
						if (mCurrentView.getProgress() < 100) {
							setIsLoading();
						} else {
							setIsFinishedLoading();
						}
					}
					updateUrl(mCurrentView.getUrl());
				} else if (hasFocus) {
					mIcon = mCopyIcon;
					mSearch.setCompoundDrawables(null, null, mCopyIcon, null);
				}
			}
		});
		mSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				// hide the keyboard and search the web when the enter key
				// button is pressed
				if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE
						|| actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND
						|| actionId == EditorInfo.IME_ACTION_SEARCH || (arg2.getAction() == KeyEvent.KEYCODE_ENTER)) {
					Log.e("not tab ", "false");

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
					searchTheWeb(mSearch.getText().toString());
					if (mCurrentView != null) {
						mCurrentView.requestFocus();
					}
					return true;
				}
				return false;
			}

		});

		mSearch.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mDrawerLayout.isDrawerOpen(mDrawer)) {
					mDrawerLayout.closeDrawer(mDrawer);
				}
				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
				if (mSearch.getCompoundDrawables()[2] != null) {
					boolean tappedX = event
							.getX() > (mSearch.getWidth() - mSearch.getPaddingRight() - mIcon.getIntrinsicWidth());
					if (tappedX) {
						if (event.getAction() == MotionEvent.ACTION_UP) {
							if (mSearch.hasFocus()) {
								ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
								ClipData clip = ClipData.newPlainText("label", mSearch.getText().toString());
								clipboard.setPrimaryClip(clip);
								B_Utils.showToast(mContext,
										mContext.getResources().getString(R.string.message_text_copied));
							} else {
								refreshOrStop();
							}
						}
						return true;
					}
				}
				return false;
			}

		});

		mSystemBrowser = getSystemBrowser();
		Thread initialize = new Thread(new Runnable() {

			@Override
			public void run() {
				initializeSearchSuggestions(mSearch);
			}

		});
		initialize.run();
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /*
										 * nav drawer image to replace 'Up' caret
										 */
				R.string.drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
				R.string.drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				if (view.equals(mDrawer)) {
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawerRight);
				} else if (view.equals(mDrawerRight)) {
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawer);
				}
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (drawerView.equals(mDrawer)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerRight);
				} else if (drawerView.equals(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawer);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawer);
				}
			}

		};

		mNewTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				newTab(getHomepage(), true);
				if (mDrawerLayout.isDrawerOpen(mDrawer)) {
					mDrawerLayout.closeDrawer(mDrawer);
				}
			}

		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_right_shadow, GravityCompat.END);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		initializePreferences();
		initializeTabs();

		if (API < 19) {
			WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
		}

		checkForTor();

	}

	/*
	 * If Orbot/Tor is installed, prompt the user if they want to enable
	 * proxying for this session
	 */
	public boolean checkForTor() {
		boolean useProxy = mPreferences.getBoolean(PreferenceConstants.USE_PROXY, false);

		OrbotHelper oh = new OrbotHelper(this);
		if (oh.isOrbotInstalled() && !mPreferences.getBoolean(PreferenceConstants.INITIAL_CHECK_FOR_TOR, false)) {
			mEditPrefs.putBoolean(PreferenceConstants.INITIAL_CHECK_FOR_TOR, true);
			mEditPrefs.apply();
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						mPreferences.edit().putBoolean(PreferenceConstants.USE_PROXY, true).apply();

						initializeTor();
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						mPreferences.edit().putBoolean(PreferenceConstants.USE_PROXY, false).apply();
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.use_tor_prompt).setPositiveButton(R.string.yes, dialogClickListener)
					.setNegativeButton(R.string.no, dialogClickListener).show();

			return true;
		} else if (oh.isOrbotInstalled() & useProxy == true) {
			initializeTor();
			return true;
		} else {
			mEditPrefs.putBoolean(PreferenceConstants.USE_PROXY, false);
			mEditPrefs.apply();
			return false;
		}
	}

	/*
	 * Initialize WebKit Proxying for Tor
	 */
	public void initializeTor() {

		OrbotHelper oh = new OrbotHelper(this);
		if (!oh.isOrbotRunning()) {
			oh.requestOrbotStart(this);
		}
		try {
			String host = mPreferences.getString(PreferenceConstants.USE_PROXY_HOST, "localhost");
			int port = mPreferences.getInt(PreferenceConstants.USE_PROXY_PORT, 8118);
			WebkitProxy.setProxy("acr.browser.lightning.BrowserApp", getApplicationContext(), host, port);
		} catch (Exception e) {
			Log.d(B_Constants.TAG, "error enabling web proxying", e);
		}

	}

	public void setNavigationDrawerWidth() {
		int width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
		int maxWidth = B_Utils.convertToDensityPixels(mContext, 300);
		if (width > maxWidth) {
			DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawer
					.getLayoutParams();
			params.width = maxWidth;
			mDrawer.setLayoutParams(params);
			DrawerLayout.LayoutParams paramsRight = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerRight
					.getLayoutParams();
			paramsRight.width = maxWidth;
			mDrawerRight.setLayoutParams(paramsRight);
		} else {
			DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawer
					.getLayoutParams();
			params.width = width;
			mDrawer.setLayoutParams(params);
			DrawerLayout.LayoutParams paramsRight = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerRight
					.getLayoutParams();
			paramsRight.width = width;
			mDrawerRight.setLayoutParams(paramsRight);
		}
	}

	/*
	 * Override this class
	 */
	public synchronized void initializeTabs() {

	}

	public void restoreOrNewTab() {
		mIdGenerator = 0;

		String url = null;
		if (getIntent() != null) {
			url = getIntent().getDataString();
			if (url != null) {
				if (url.startsWith(B_Constants.FILE)) {
					B_Utils.showToast(this, getResources().getString(R.string.message_blocked_local));
					url = null;
				}
			}
		}
		if (mPreferences.getBoolean(PreferenceConstants.RESTORE_LOST_TABS, true)) {
			String mem = mPreferences.getString(PreferenceConstants.URL_MEMORY, "");
			mEditPrefs.putString(PreferenceConstants.URL_MEMORY, "");
			String[] array = B_Utils.getArray(mem);
			int count = 0;
			for (int n = 0; n < array.length; n++) {
				if (array[n].length() > 0) {
					newTab(array[n], true);
					count++;
				}
			}
			if (url != null) {
				newTab(url, true);
			} else if (count == 0) {
				//Toast.makeText(B_BrowserActivity.this,"new tab======= 3", Toast.LENGTH_LONG).show();
				newTab(null, true);
			}
		} else {
			newTab(url, true);
		}
	}

	public void initializePreferences() {
		if (mPreferences == null) {
			mPreferences = getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
		}
		mFullScreen = mPreferences.getBoolean(PreferenceConstants.FULL_SCREEN, false);
		if (mPreferences.getBoolean(PreferenceConstants.HIDE_STATUS_BAR, false)) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String engine = sharedPreferences1.getString("defaultengine", Constant.defaultEngine);
		int pos = 0;
		if (engine.equals("google")) {
			pos = 1;
		} else if (engine.equals("bing")) {
			pos = 3;
		} else if (engine.equals("yahoo")) {
			pos = 4;
		} else if (engine.equals("ask")) {
			pos = 11;
		}
		if (pos == 0) {
			pos = 1;
		}
		switch (mPreferences.getInt(PreferenceConstants.SEARCH, pos)) {
		case 0:
			mSearchText = mPreferences.getString(PreferenceConstants.SEARCH_URL, B_Constants.GOOGLE_SEARCH);
			if (!mSearchText.startsWith(B_Constants.HTTP) && !mSearchText.startsWith(B_Constants.HTTPS)) {
				mSearchText = B_Constants.GOOGLE_SEARCH;
			}
			break;
		case 1:
			mSearchText = B_Constants.GOOGLE_SEARCH;
			break;
		case 2:
			mSearchText = B_Constants.ANDROID_SEARCH;
			break;
		case 3:
			mSearchText = B_Constants.BING_SEARCH;
			break;
		case 4:
			mSearchText = B_Constants.YAHOO_SEARCH;
			break;
		case 5:
			mSearchText = B_Constants.STARTPAGE_SEARCH;
			break;
		case 6:
			mSearchText = B_Constants.STARTPAGE_MOBILE_SEARCH;
			break;
		case 7:
			mSearchText = B_Constants.DUCK_SEARCH;
			break;
		case 8:
			mSearchText = B_Constants.DUCK_LITE_SEARCH;
			break;
		case 9:
			mSearchText = B_Constants.BAIDU_SEARCH;
			break;
		case 10:
			mSearchText = B_Constants.YANDEX_SEARCH;
			break;
		case 11:
			mSearchText = B_Constants.ASK_SEARCH;
			break;
		}

		updateCookiePreference();
		if (mPreferences.getBoolean(PreferenceConstants.USE_PROXY, false)) {
			initializeTor();
		} else {
			try {
				WebkitProxy.resetProxy("acr.browser.lightning.BrowserApp", getApplicationContext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * Override this if class overrides BrowserActivity
	 */
	public void updateCookiePreference() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			if (mSearch.hasFocus()) {
				searchTheWeb(mSearch.getText().toString());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // TODO Auto-generated method stub
	// return super.onPrepareOptionsMenu(menu);
	// }

	// /// Toggel Menu is here///////
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // The action bar home/up action should open or close the drawer.
	// // ActionBarDrawerToggle will take care of this.
	// if (mDrawerToggle.onOptionsItemSelected(item)) {
	// if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
	// mDrawerLayout.closeDrawer(mDrawerRight);
	// mDrawerLayout.openDrawer(mDrawer);
	// } else if (mDrawerLayout.isDrawerOpen(mDrawer)) {
	// mDrawerLayout.closeDrawer(mDrawer);
	// }
	// mDrawerToggle.syncState();
	// return true;
	// }
	// // Handle action buttons
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
	// mDrawerLayout.closeDrawer(mDrawerRight);
	// }
	// mDrawerToggle.syncState();
	// return true;
	// case R.id.action_back:
	// if (mCurrentView != null) {
	// if (mCurrentView.canGoBack()) {
	// mCurrentView.goBack();
	// }
	// }
	// return true;
	// case R.id.action_forward:
	// if (mCurrentView != null) {
	// if (mCurrentView.canGoForward()) {
	// mCurrentView.goForward();
	// }
	// }
	// return true;
	// case R.id.action_new_tab:
	// newTab(null, true);
	// return true;
	// /*
	// * case R.id.action_incognito: startActivity(new Intent(this,
	// * IncognitoActivity.class)); return true;
	// */
	//
	// case R.id.action_bookmarks:
	// openBookmarks();
	// return true;
	// case R.id.action_copy:
	// if (mCurrentView != null) {
	// if (!mCurrentView.getUrl().startsWith(B_Constants.FILE)) {
	// ClipboardManager clipboard = (ClipboardManager)
	// getSystemService(CLIPBOARD_SERVICE);
	// ClipData clip = ClipData.newPlainText("label", mCurrentView
	// .getUrl().toString());
	// clipboard.setPrimaryClip(clip);
	// B_Utils.showToast(mContext, mContext.getResources()
	// .getString(R.string.message_link_copied));
	// }
	// }
	// return true;
	// case R.id.action_settings:
	// startActivity(new Intent(this, B_SettingsActivity.class));
	// return true;
	// case R.id.action_history:
	// openHistory();
	// return true;
	// case R.id.action_add_bookmark:
	// if (!mCurrentView.getUrl().startsWith(B_Constants.FILE)) {
	// addBookmark(this, mCurrentView.getTitle(),
	// mCurrentView.getUrl());
	// }
	// return true;
	// case R.id.action_find:
	// findInPage();
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	/**
	 * refreshes the underlying list of the Bookmark adapter since the bookmark
	 * adapter doesn't always change when notifyDataChanged gets called.
	 */
	private void notifyBookmarkDataSetChanged() {
		mBookmarkAdapter.clear();
		mBookmarkAdapter.addAll(mBookmarkList);
		mBookmarkAdapter.notifyDataSetChanged();
	}

	/**
	 * method that shows a dialog asking what string the user wishes to search
	 * for. It highlights the text entered.
	 */

	private void findInPage() {

		final Dialog dialogClear = new Dialog(B_BrowserActivity.this);

		dialogClear.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogClear.setContentView(R.layout.dialog_findinpage);
		TextView textView1 = (TextView) dialogClear.findViewById(R.id.textView1);
		textView1.setTypeface(tfSemiBoldItalic);
		textView1.setText(getResources().getString(R.string.action_find));
		final EditText editText = (EditText) dialogClear.findViewById(R.id.edtFind);
		editText.setTypeface(tfRegular);

		Button btnCnacle = (Button) dialogClear.findViewById(R.id.btncancel);
		btnCnacle.setText(getResources().getString(R.string.action_no));
		btnCnacle.setTypeface(tfSemiBoldItalic);
		btnCnacle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogClear.dismiss();
			}
		});

		Button btnOK = (Button) dialogClear.findViewById(R.id.btnok);
		btnOK.setText(getResources().getString(R.string.action_yes));
		btnOK.setTypeface(tfSemiBoldItalic);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = editText.getText().toString();
				if (mCurrentView != null) {
					mCurrentView.find(text);
				}
				dialogClear.dismiss();
			}
		});
		dialogClear.show();

		// ////
		// final AlertDialog.Builder finder = new
		// AlertDialog.Builder(mActivity);
		// finder.setTitle(getResources().getString(R.string.action_find));
		// final EditText getHome = new EditText(this);
		// getHome.setHint(getResources().getString(R.string.search_hint));
		// finder.setView(getHome);
		// finder.setPositiveButton(
		// getResources().getString(R.string.search_hint),
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// String text = getHome.getText().toString();
		// if (mCurrentView != null) {
		// mCurrentView.find(text);
		// }
		// }
		// });
		// finder.show();
	}

	/**
	 * The click listener for ListView in the navigation drawer
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mIsNewIntent = false;
			selectItem(position);
		}
	}

	/**
	 * long click listener for Navigation Drawer
	 */
	private class DrawerItemLongClickListener implements ListView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			deleteTab(position);
			return false;
		}
	}

	private class BookmarkItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (mCurrentView != null) {
				mCurrentView.loadUrl(mBookmarkList.get(position).getUrl());
			}
			// keep any jank from happening when the drawer is closed after the
			// URL starts to load
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
			}, 150);
		}
	}

	private class BookmarkItemLongClickListener implements ListView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(mContext.getResources().getString(R.string.action_bookmarks));
			builder.setMessage(getResources().getString(R.string.dialog_bookmark)).setCancelable(true)
					.setPositiveButton(getResources().getString(R.string.action_new_tab),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									newTab(mBookmarkList.get(position).getUrl(), false);
									mDrawerLayout.closeDrawers();
								}
							})
					.setNegativeButton(getResources().getString(R.string.action_delete),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									deleteBookmark(mBookmarkList.get(position).getUrl());
								}
							})
					.setNeutralButton(getResources().getString(R.string.action_edit),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									editBookmark(position);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}
	}

	/**
	 * Takes in the id of which bookmark was selected and shows a dialog that
	 * allows the user to rename and change the url of the bookmark
	 * 
	 * @param id
	 *            which id in the list was chosen
	 */
	public synchronized void editBookmark(final int id) {
		final AlertDialog.Builder homePicker = new AlertDialog.Builder(mActivity);
		homePicker.setTitle(getResources().getString(R.string.title_edit_bookmark));
		final EditText getTitle = new EditText(mContext);
		getTitle.setHint(getResources().getString(R.string.hint_title));
		getTitle.setText(mBookmarkList.get(id).getTitle());
		getTitle.setSingleLine();
		final EditText getUrl = new EditText(mContext);
		getUrl.setHint(getResources().getString(R.string.hint_url));
		getUrl.setText(mBookmarkList.get(id).getUrl());
		getUrl.setSingleLine();
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(getTitle);
		layout.addView(getUrl);
		homePicker.setView(layout);
		homePicker.setPositiveButton(getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mBookmarkList.get(id).setTitle(getTitle.getText().toString());
						mBookmarkList.get(id).setUrl(getUrl.getText().toString());
						notifyBookmarkDataSetChanged();
						File book = new File(getFilesDir(), "bookmarks");
						File bookUrl = new File(getFilesDir(), "bookurl");
						try {
							BufferedWriter bookWriter = new BufferedWriter(new FileWriter(book));
							BufferedWriter urlWriter = new BufferedWriter(new FileWriter(bookUrl));
							Iterator<HistoryItem> iter = mBookmarkList.iterator();
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
						if (mCurrentView != null) {
							if (mCurrentView.getUrl().startsWith(B_Constants.FILE)
									&& mCurrentView.getUrl().endsWith("bookmarks.html")) {
									openBookmarkPage(mCurrentView.getWebView());
							}
						}
					}
				});
		homePicker.show();
	}

	/**
	 * displays the WebView contained in the LightningView Also handles the
	 * removal of previous views
	 * 
	 * @param view
	 *            the LightningView to show
	 */
	private synchronized void showTab(LightningView view, boolean isRelodingFromDB) {
		// Log.e("Binit ", "msg from showTab");
		isReloading = isRelodingFromDB;
		count = count + 1;

		if (view == null) {

			return;
		} else {
			if (view.getUrl() != null)
				mSearch.setText(view.getUrl());
		}
		if (mCurrentView != null) {
			mCurrentView.setForegroundTab(false);
			mCurrentView.onPause();
		}
		mCurrentView = view;
		mCurrentView.setForegroundTab(true);
		if (view.getUrl() != null) {

			// if (view.getWebView() != null) {
			// if (view.getUrl().equals("null")
			// || view.getUrl().equals("about:blank")) {
			//
			// } else {
			// mUrlArray.add(tabPosition - 1, view.getUrl());
			// }
			// }about:02-29 15:07:08.822: E/List is Loading(18685): true
			// urlfile:///data/data/com.boxer.browser/cache/homepage.html

			updateUrl(view.getUrl());
			updateProgress(view.getProgress());
		} else {
			updateUrl("");
			updateProgress(0);
		}

		mBrowserFrame.removeAllViews();
		mCurrentView.onResume();
		mBrowserFrame.addView(view.getWebView());

	}

	/**
	 * creates a new tab with the passed in URL if it isn't null
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	public void handleNewIntent(Intent intent) {

		if (mCurrentView == null) {
			initialize();
		}

		String url = null;
		if (intent != null) {
			url = intent.getDataString();
		}
		int num = 0;
		if (intent != null && intent.getExtras() != null) {
			num = intent.getExtras().getInt(getPackageName() + ".Origin");
		}
		if (num == 1) {
			mCurrentView.loadUrl(url);
		} else if (url != null) {
			if (url.startsWith(B_Constants.FILE)) {
				B_Utils.showToast(this, getResources().getString(R.string.message_blocked_local));
				url = null;
			}
			newTab(url, true);
			mIsNewIntent = true;
		}
	}

	@Override
	public void closeEmptyTab() {
		if (mCurrentView != null && mCurrentView.getWebView().copyBackForwardList().getSize() == 0) {
			closeCurrentTab();
		}
	}

	private void closeCurrentTab() {
		// don't delete the tab because the browser will close and mess stuff up
	}

	private void selectItem(final int position) {
		// update selected item and title, then close the drawer
		isReloading = true;
		mDrawerList.setItemChecked(position, true);
		showTab(mWebViews.get(position), true);

		// Use a delayed handler to make the transition smooth
		// otherwise it will get caught up with the showTab code
		// and cause a janky motion
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mDrawerLayout.closeDrawer(mDrawer);
			}
		}, 150);

	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// Debug point
	@SuppressLint("NewApi")
	protected synchronized void newTab(String url, boolean show) {
		// Log.e("Binit ", "msg from new tab");
		count = 0;
		count = count + 1;
		List<Url> arrUrls = db.getAllUrl();
		Log.e("Item in db ", arrUrls.size() + "");

		List<LightningView> mWebViewsRetrive = null;
		ArrayList<String> retrivedArrayList = new ArrayList<String>();
		if (mDrawerList.getChildCount() == 0) {

			for (int i = 0; i < arrUrls.size(); i++) {
				retrivedArrayList.add(arrUrls.get(i).getName());
				Log.e("retrived url from db at == " + i, "" + arrUrls.get(i).getName());
				Log.e("adding position", arrUrls.get(i).getID() + "");

			}
			db.deleteDB_Data();// // Deleting database entries
		}
		if (mDrawerList.getChildCount() == 0) {
			if (retrivedArrayList.size() > 0) {

				for (int i = 0; i < retrivedArrayList.size(); i++) {

					isReloading = true;
					isListLoading = true;
					// Toast.makeText(mActivity, retrivedArrayList.get(i), 4)
					// .show();
					LightningView startingTab1 = new LightningView(mActivity, retrivedArrayList.get(i), true);
					mIdList.add(mIdGenerator);
					mIdGenerator++;
					Log.e("Listing with url ", retrivedArrayList.get(i));
					mWebViews.add(startingTab1);
					tabPosition++;
					Drawable icon = writeOnDrawable(mWebViews.size());
					mActionBar.setIcon(icon);
					mTitleAdapter.notifyDataSetChanged();

					mDrawerList.setItemChecked(mWebViews.size() - 1, true);
					ID id = new ID();
					id.setId(i);
					mUrl_ID_Array.add(id);
					db.addUrl(new Url(i, retrivedArrayList.get(i)));
					// showTab(startingTab1, true);
					// }
				}
			}
		}
		isReloading = false;
		mIsNewIntent = false;
		LightningView startingTab = new LightningView(mActivity, url, false);
		if (mIdGenerator == 0) {
			startingTab.resumeTimers();

		}
		mIdList.add(mIdGenerator);
		tabPosition++;
		mIdGenerator++;
		mWebViews.add(startingTab);

		Drawable icon = writeOnDrawable(mWebViews.size());
		mActionBar.setIcon(icon);
		mTitleAdapter.notifyDataSetChanged();
		// mUrlArray.add(tabPosition - 1, url);
		if (show) {
			mDrawerList.setItemChecked(mWebViews.size() - 1, true);
			showTab(startingTab, false);
		}

	}

	@SuppressLint("NewApi")
	private synchronized void deleteTab(int position) {
		// mUrlArray.remove(mIdGenerator);
		// tabPosition--;
		// Url demo = db.getUrl(position);
		int tabID = mUrl_ID_Array.get(position).getId();
		db.deleteContact(new Url(tabID, ""));
		mUrl_ID_Array.remove(position);

		Log.e("My ID is", "" + mIdGenerator + "  and position is  " + position);

		try {

			if (mWebViews == null || mDrawerList == null) {
				finish();
				return;
			}
			if (position >= mWebViews.size()) {

				return;
			}

			int current = mDrawerList.getCheckedItemPosition();
			LightningView reference = mWebViews.get(position);
			if (reference == null) {
				return;
			}
			boolean isShown = reference.isShown();

			if (current == 0 && position == 0 && mWebViews.size() == 1) {

				System.out.println("mwebview print kerwa ra hu3==" + mWebViews.size() + " current=" + current
						+ "position=" + position);

				finish();
				// Intent goMain = new Intent(B_BrowserActivity.this,
				// MainActivity.class);
				// startActivity(goMain);
				// overridePendingTransition(R.anim.pull_in_left,
				// R.anim.push_out_right);

			}

			if (mWebViews.size() == 1) {

				finish();
				Intent goMain = new Intent(B_BrowserActivity.this, MainActivity.class);
				startActivity(goMain);
				overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

			}

			if (current > position) {
				mIdList.remove(position);
				mWebViews.remove(position);
				mDrawerList.setItemChecked(current - 1, true);
				reference.onDestroy();
			} else if (mWebViews.size() > position + 1) {
				mIdList.remove(position);
				showTab(mWebViews.get(position + 1), false);
				mWebViews.remove(position);
				mDrawerList.setItemChecked(position, true);
				reference.onDestroy();
			} else if (mWebViews.size() > 1) {
				mIdList.remove(position);
				showTab(mWebViews.get(position - 1), false);
				mWebViews.remove(position);
				mDrawerList.setItemChecked(position - 1, true);
				reference.onDestroy();
			} else {
				if (!mCurrentView.getUrl().equals(null)) {
					if (mCurrentView.getUrl().startsWith(B_Constants.FILE) || mCurrentView.getUrl().equals(mHomepage)) {

					} else {
						mIdList.remove(position);
						mWebViews.remove(position);
						if (mPreferences.getBoolean(PreferenceConstants.CLEAR_CACHE_EXIT, false) && mCurrentView != null
								&& !isIncognito()) {
							mCurrentView.clearCache(true);
							Log.i(B_Constants.TAG, "Cache Cleared");

						}
						if (mPreferences.getBoolean(PreferenceConstants.CLEAR_HISTORY_EXIT, false) && !isIncognito()) {
							clearHistory();
							Log.i(B_Constants.TAG, "History Cleared");

						}
						if (mPreferences.getBoolean(PreferenceConstants.CLEAR_COOKIES_EXIT, false) && !isIncognito()) {
							clearCookies();
							Log.i(B_Constants.TAG, "Cookies Cleared");

						}
						if (reference != null) {
							reference.pauseTimers();
							reference.onDestroy();
						}
						mCurrentView = null;
						mTitleAdapter.notifyDataSetChanged();
						finish();

					}
				} else {
					// Toast.makeText(B_BrowserActivity.this, "gdhjkl",
					// 3).show();
				}

			}
			mTitleAdapter.notifyDataSetChanged();
			Drawable icon = writeOnDrawable(mWebViews.size());
			mActionBar.setIcon(icon);

			if (mIsNewIntent && isShown) {
				mIsNewIntent = false;
				// closeActivity();
			}

		} catch (NullPointerException e) {
			// TODO: handle exception
			Log.e("hello Sir ", "null pointer");

			this.finish();
		}
		Log.e("deleting postion is", "" + (position));
		try {
			// mUrlArray.remove(position);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// JSONObject json = new JSONObject();
		// try {
		// json.put("uniqueArrays", new JSONArray(mUrlArray));
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// String arrayList = json.toString();
		// PreferenceManager.getDefaultSharedPreferences(this).edit()
		// .putString("MYLABEL", arrayList).commit();
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mPreferences.getBoolean(PreferenceConstants.CLEAR_CACHE_EXIT, false) && mCurrentView != null
					&& !isIncognito()) {
				mCurrentView.clearCache(true);
				Log.i(B_Constants.TAG, "Cache Cleared");

			}
			if (mPreferences.getBoolean(PreferenceConstants.CLEAR_HISTORY_EXIT, false) && !isIncognito()) {
				clearHistory();
				Log.i(B_Constants.TAG, "History Cleared");

			}
			if (mPreferences.getBoolean(PreferenceConstants.CLEAR_COOKIES_EXIT, false) && !isIncognito()) {
				clearCookies();
				Log.i(B_Constants.TAG, "Cookies Cleared");

			}
			mCurrentView = null;
			for (int n = 0; n < mWebViews.size(); n++) {
				if (mWebViews.get(n) != null) {
					mWebViews.get(n).onDestroy();
				}
			}
			mWebViews.clear();
			mTitleAdapter.notifyDataSetChanged();
			finish();
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public void clearHistory() {
		this.deleteDatabase(DatabaseHandler.DATABASE_NAME);
		WebViewDatabase m = WebViewDatabase.getInstance(this);
		m.clearFormData();
		m.clearHttpAuthUsernamePassword();
		if (API < 18) {
			m.clearUsernamePassword();
			WebIconDatabase.getInstance().removeAllIcons();
		}
		if (mSystemBrowser) {
			try {
				Browser.clearHistory(getContentResolver());
			} catch (NullPointerException ignored) {
			}
		}
		SettingsController.setClearHistory(true);
		B_Utils.trimCache(this);
	}

	public void clearCookies() {
		CookieManager c = CookieManager.getInstance();
		CookieSyncManager.createInstance(this);
		c.removeAllCookie();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (!mActionBar.isShowing()) {
			mActionBar.show();
		}
		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
			mDrawerLayout.closeDrawer(mDrawer);
		} else if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
			mDrawerLayout.closeDrawer(mDrawerRight);
		} else {
			if (mCurrentView != null) {
				Log.i(B_Constants.TAG, "onBackPressed");
				if (mCurrentView.canGoBack()) {
					if (!mCurrentView.isShown()) {
						onHideCustomView();
					} else {
						// mCurrentView.goBack();
					}
				} else {
					// deleteTab(mDrawerList.getCheckedItemPosition());
				}
			} else {
				Log.e(B_Constants.TAG, "So madness. Much confusion. Why happen.");

			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(" B_browser ===", "onPause");
		Log.e(" B_browser === tempurl", "" + tempUrl);

		if (mCurrentView != null) {
			mCurrentView.pauseTimers();
			mCurrentView.onPause();
		}
		if (mHistoryDatabase != null) {
			if (mHistoryDatabase.isOpen()) {
				mHistoryDatabase.close();
			}
		}
		if (mHistoryHandler != null) {
			if (mHistoryHandler.isOpen()) {
				mHistoryHandler.close();
			}
		}

		// try {
		// FileOutputStream fileOut = new FileOutputStream(file);
		// ObjectOutputStream out = new ObjectOutputStream(fileOut);
		// out.writeObject(mWebViews);
		// out.close();
		// fileOut.close();
		// Log.e("Trying to save object ",
		// "Serialized data is saved in list.ser");
		// } catch (IOException i) {
		// i.printStackTrace();
		// }

	}

	public void saveOpenTabs() {
		if (mPreferences.getBoolean(PreferenceConstants.RESTORE_LOST_TABS, true)) {
			String s = "";
			for (int n = 0; n < mWebViews.size(); n++) {
				if (mWebViews.get(n).getUrl() != null) {
					s = s + mWebViews.get(n).getUrl() + "|$|SEPARATOR|$|";
				}
			}
			mEditPrefs.putString(PreferenceConstants.URL_MEMORY, s);
			// mEditPrefs.commit();
		}
	}

	@Override
	protected void onDestroy() {

		Log.i(B_Constants.TAG, "onDestroy");
		if (mHistoryDatabase != null) {
			if (mHistoryDatabase.isOpen()) {
				mHistoryDatabase.close();
			}
		}
		if (mHistoryHandler != null) {
			if (mHistoryHandler.isOpen()) {
				mHistoryHandler.close();
			}
		}
		super.onDestroy();
		isTabOpen = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(B_Constants.TAG, "onResume");
		if (SettingsController.getClearHistory()) {
		}
		if (mSearchAdapter != null) {
			mSearchAdapter.refreshPreferences();
			mSearchAdapter.refreshBookmarks();
		}
		if (mActionBar != null) {
			if (!mActionBar.isShowing()) {
				mActionBar.show();
			}
		}
		if (mCurrentView != null) {
			mCurrentView.resumeTimers();
			mCurrentView.onResume();

			if (mHistoryHandler == null) {
				mHistoryHandler = new DatabaseHandler(this);
			} else if (!mHistoryHandler.isOpen()) {
				mHistoryHandler = new DatabaseHandler(this);
			}
			mHistoryDatabase = mHistoryHandler.getReadableDatabase();
			mBookmarkList = getBookmarks();
			notifyBookmarkDataSetChanged();
		} else {
			initialize();
		}
		initializePreferences();
		if (mWebViews != null) {
			for (int n = 0; n < mWebViews.size(); n++) {
				if (mWebViews.get(n) != null) {
					mWebViews.get(n).initializePreferences(this);
				} else {
					mWebViews.remove(n);
				}
			}
		} else {
			initialize();
		}
	}

	/**
	 * searches the web for the query fixing any and all problems with the input
	 * checks if it is a search, url, etc.
	 */
	void searchTheWeb(String query) {
		if (query.equals("")) {
			return;
		}
		String SEARCH = mSearchText;
		query = query.trim();
		mCurrentView.stopLoading();

		if (query.startsWith("www.")) {
			query = B_Constants.HTTP + query;
		} else if (query.startsWith("ftp.")) {
			query = "ftp://" + query;
		}

		boolean containsPeriod = query.contains(".");
		boolean isIPAddress = (TextUtils.isDigitsOnly(query.replace(".", "")) && (query.replace(".", "").length() >= 4)
				&& query.contains("."));
		boolean aboutScheme = query.contains("about:");
		boolean validURL = (query.startsWith("ftp://") || query.startsWith(B_Constants.HTTP)
				|| query.startsWith(B_Constants.FILE) || query.startsWith(B_Constants.HTTPS)) || isIPAddress;
		boolean isSearch = ((query.contains(" ") || !containsPeriod) && !aboutScheme);

		if (isIPAddress && (!query.startsWith(B_Constants.HTTP) || !query.startsWith(B_Constants.HTTPS))) {
			query = B_Constants.HTTP + query;
		}

		if (isSearch) {
			try {
				query = URLEncoder.encode(query, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			mCurrentView.loadUrl(SEARCH + query);
		} else if (!validURL) {
			if (query.contains("market://")) {
				mCurrentView.loadUrl(query);
			} else {
				mCurrentView.loadUrl(B_Constants.HTTP + query);
			}
		} else {
			mCurrentView.loadUrl(query);
		}
	}

	public void deleteBookmark(String url) {
		File book = new File(getFilesDir(), "bookmarks");
		File bookUrl = new File(getFilesDir(), "bookurl");
		try {
			BufferedWriter bookWriter = new BufferedWriter(new FileWriter(book));
			BufferedWriter urlWriter = new BufferedWriter(new FileWriter(bookUrl));
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
		mSearchAdapter.refreshBookmarks();
		openBookmarks();
	}

	/**
	 * converts the int num into density pixels
	 * 
	 * @return density pixels
	 */
	private int getDp(int num) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (num * scale + 0.5f);
	}

	private int pixelsToDp(int num) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) ((num - 0.5f) / scale);
	}

	/**
	 * writes the number of open tabs on the icon.
	 */
	public BitmapDrawable writeOnDrawable(int number) {

		Bitmap bm = Bitmap.createBitmap(mActionBarSize, mActionBarSize, Config.ARGB_8888);
		String text = number + "";
		Paint paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setColor(mNumberIconColor);
		if (number > 99) {
			number = 99;
		}
		// pixels, 36 dp
		if (mActionBarSizeDp < 50) {
			if (number > 9) {
				paint.setTextSize(mActionBarSize * 3 / 4); // originally
				// 40
				// pixels,
				// 24 dp
			} else {
				paint.setTextSize(mActionBarSize * 9 / 10); // originally 50
				// pixels, 30 dp
			}
		} else {
			paint.setTextSize(mActionBarSize * 3 / 4);
		}
		Canvas canvas = new Canvas(bm);
		// originally only vertical padding of 5 pixels

		int xPos = (canvas.getWidth() / 2);
		int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

		canvas.drawText(text, xPos, yPos, paint);

		return new BitmapDrawable(getResources(), bm);
	}

	public class LightningViewAdapter extends ArrayAdapter<LightningView> {

		Context context;
		// Typeface tfSemiBoldItalic;
		int layoutResourceId;

		List<LightningView> data = null;

		public LightningViewAdapter(Context context, int layoutResourceId, List<LightningView> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			// tfSemiBoldItalic = Typeface.createFromAsset(context.getAssets(),
			// Constant.FontTitilliumWebBold);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View row = convertView;
			// tfSemiBoldItalic = Typeface.createFromAsset(context.getAssets(),
			// Constant.FontTitilliumWebBold);
			LightningViewHolder holder = null;
			if (row == null) {

				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new LightningViewHolder();
				holder.txtTitle = (TextView) row.findViewById(R.id.text1);

				holder.favicon = (ImageView) row.findViewById(R.id.favicon1);
				holder.exit = (ImageView) row.findViewById(R.id.delete1);
				holder.exit.setTag(position);
				row.setTag(holder);
			} else {
				holder = (LightningViewHolder) row.getTag();
			}

			holder.exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					deleteTab(position);
				}

			});

			LightningView web = data.get(position);
			holder.txtTitle.setTypeface(tfSemiBoldItalic);
			holder.txtTitle.setText(web.getTitle());
			// if (web.isForegroundTab()) {
			// holder.txtTitle.setTextAppearance(context, R.style.boldText);
			// } else {
			// holder.txtTitle.setTextAppearance(context, R.style.normalText);
			// }

			Bitmap favicon = web.getFavicon();
			holder.favicon.setImageBitmap(favicon);
			return row;
		}

		class LightningViewHolder {

			TextView txtTitle;

			ImageView favicon;

			ImageView exit;
		}
	}

	public class BookmarkViewAdapter extends ArrayAdapter<HistoryItem> {

		Context context;

		int layoutResourceId;

		List<HistoryItem> data = null;

		public BookmarkViewAdapter(Context context, int layoutResourceId, List<HistoryItem> data) {
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
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new BookmarkViewHolder();
				holder.txtTitle = (TextView) row.findViewById(R.id.text1);
				holder.favicon = (ImageView) row.findViewById(R.id.favicon1);
				row.setTag(holder);
			} else {
				holder = (BookmarkViewHolder) row.getTag();
			}

			HistoryItem web = data.get(position);
			holder.txtTitle.setTypeface(tfRegular);
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
			new DownloadImageTask(image, web).execute(B_Constants.HTTP + getDomainName(web.getUrl()) + "/favicon.ico");
		} catch (URISyntaxException e) {
			new DownloadImageTask(image, web).execute("https://www.google.com/s2/favicons?domain_url=" + web.getUrl());
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
			File image = new File(mContext.getCacheDir(), hash + ".png");
			// checks to see if the image exists
			if (!image.exists()) {
				try {
					// if not, download it...
					URL url = new URL(urldisplay);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
					InputStream in = new java.net.URL("https://www.google.com/s2/favicons?domain_url=" + urldisplay)
							.openStream();

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

	static String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		if (domain == null) {
			return url;
		}
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}

	@Override
	public void updateUrl(String url) {
		// Log.e("Binit ", "msg from updateUrl");
		count = count + 1;
		if (url == null) {
			return;
		}
		url = url.replaceFirst(B_Constants.HTTP, "");
		if (url.startsWith(B_Constants.FILE)) {
			url = "";
		}

		// tempUrl = url;
		//
		// Log.e("Temp Url ==", "" + tempUrl);
		// if (count >= 3) {
		//
		// } else {
		// Log.e("Binit updating url ", "" + count);
		// mUrlArray.set(mDrawerList.getCheckedItemPosition(), url);
		// }
		mSearch.setText(url);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("on stop", "on stop");
	}

	@Override
	public void updateProgress(int n) {

		if (n > mProgressBar.getProgress()) {
			ObjectAnimator animator = ObjectAnimator.ofInt(mProgressBar, "progress", n);
			animator.setDuration(200);
			animator.setInterpolator(new DecelerateInterpolator());
			animator.start();
		} else if (n < mProgressBar.getProgress()) {
			ObjectAnimator animator = ObjectAnimator.ofInt(mProgressBar, "progress", 0, n);
			animator.setDuration(200);
			animator.setInterpolator(new DecelerateInterpolator());
			animator.start();
		}
		if (n >= 100) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mProgressBar.setVisibility(View.INVISIBLE);
					setIsFinishedLoading();
				}
			}, 200);

		} else {
			mProgressBar.setVisibility(View.VISIBLE);
			setIsLoading();
		}
	}

	@Override
	public void updateHistory(final String title, final String url) {

	}

	public void addItemToHistory(final String title, final String url) {

		SearchString = title;
		// Log.e("Added ", SearchString);
		// if (SearchString!=null) {
		// new SearchReportAsyncTask().execute();
		// }
		Runnable update = new Runnable() {
			@Override
			public void run() {
				if (isSystemBrowserAvailable()) {
					try {
						Browser.updateVisitedHistory(getContentResolver(), url, true);
					} catch (NullPointerException ignored) {
					}
				}

				try {
					StringBuilder sb = new StringBuilder("url" + " = ");
					DatabaseUtils.appendEscapedSQLString(sb, url);

					if (mHistoryHandler == null) {
						mHistoryHandler = new DatabaseHandler(mContext);
						mHistoryDatabase = mHistoryHandler.getReadableDatabase();
					} else if (!mHistoryHandler.isOpen()) {
						mHistoryHandler = new DatabaseHandler(mContext);
						mHistoryDatabase = mHistoryHandler.getReadableDatabase();
					} else if (mHistoryDatabase == null) {
						mHistoryDatabase = mHistoryHandler.getReadableDatabase();
					} else if (!mHistoryDatabase.isOpen()) {
						mHistoryDatabase = mHistoryHandler.getReadableDatabase();
					}
					Cursor cursor = mHistoryDatabase.query(DatabaseHandler.TABLE_HISTORY,
							new String[] { "id", "url", "title" }, sb.toString(), null, null, null, null);
					if (!cursor.moveToFirst()) {
						mHistoryHandler.addHistoryItem(new HistoryItem(url, title));
					} else {
						mHistoryHandler.delete(url);
						mHistoryHandler.addHistoryItem(new HistoryItem(url, title));
					}
					cursor.close();
					cursor = null;
				} catch (IllegalStateException e) {
					Log.e(B_Constants.TAG, "IllegalStateException in updateHistory");
				} catch (NullPointerException e) {
					Log.e(B_Constants.TAG, "NullPointerException in updateHistory");
				} catch (SQLiteException e) {
					Log.e(B_Constants.TAG, "SQLiteException in updateHistory");
				}
			}
		};
		if (url != null) {
			if (!url.startsWith(B_Constants.FILE)) {
				new Thread(update).start();

			}
		}

	}

	/**
	 * 1, 2, 3, testing... is there a system browser that has some nice
	 * bookmarks for us?
	 */
	public boolean isSystemBrowserAvailable() {
		return mSystemBrowser;
	}

	/**
	 * 1, 2, 3, testing... is there a system browser that has some nice
	 * bookmarks for us? helper method for isSystemBrowserAvailable
	 */
	public boolean getSystemBrowser() {
		Cursor c = null;
		String[] columns = new String[] { "url", "title" };
		boolean browserFlag = false;
		try {

			Uri bookmarks = Browser.BOOKMARKS_URI;
			c = getContentResolver().query(bookmarks, columns, null, null, null);
		} catch (SQLiteException ignored) {
		} catch (IllegalStateException ignored) {
		} catch (NullPointerException ignored) {
		}

		if (c != null) {
			Log.i("Browser", "System Browser Available");
			browserFlag = true;
		} else {
			Log.e("Browser", "System Browser Unavailable");
			browserFlag = false;
		}
		if (c != null) {
			c.close();
			c = null;
		}
		mEditPrefs.putBoolean("SystemBrowser", browserFlag);
		mEditPrefs.commit();
		return browserFlag;
	}

	/**
	 * method to generate search suggestions for the AutoCompleteTextView from
	 * previously searched URLs
	 */
	private void initializeSearchSuggestions(final AutoCompleteTextView getUrl) {

		getUrl.setThreshold(1);
		getUrl.setDropDownWidth(-1);
		getUrl.setDropDownAnchor(R.id.progressWrapper);
		getUrl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String url;
					url = ((TextView) arg1.findViewById(R.id.url)).getText().toString();
					if (url.startsWith(mContext.getString(R.string.suggestion))) {
						url = ((TextView) arg1.findViewById(R.id.title)).getText().toString();
					} else {
						getUrl.setText(url);
					}
					searchTheWeb(url);
					url = null;
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getUrl.getWindowToken(), 0);
					if (mCurrentView != null) {
						mCurrentView.requestFocus();
					}
				} catch (NullPointerException e) {
					Log.e("Browser Error: ", "NullPointerException on item click");
				}
			}

		});

		getUrl.setSelectAllOnFocus(true);
		mSearchAdapter = new SearchAdapter(mContext, isIncognito());
		getUrl.setAdapter(mSearchAdapter);
	}

	public boolean isIncognito() {
		return false;
	}

	// Damn it, I regret not using SQLite in the first place for this
	private List<HistoryItem> getBookmarks() {
		List<HistoryItem> bookmarks = new ArrayList<HistoryItem>();
		File bookUrl = new File(getApplicationContext().getFilesDir(), "bookurl");
		File book = new File(getApplicationContext().getFilesDir(), "bookmarks");
		try {
			BufferedReader readUrl = new BufferedReader(new FileReader(bookUrl));
			BufferedReader readBook = new BufferedReader(new FileReader(book));
			String u, t;
			while ((u = readUrl.readLine()) != null && (t = readBook.readLine()) != null) {
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

	/**
	 * returns a list of HistoryItems
	 */
	private List<HistoryItem> getLatestHistory() {
		DatabaseHandler historyHandler = new DatabaseHandler(mContext);
		return historyHandler.getLastHundredItems();
	}

	/**
	 * function that opens the HTML history page in the browser
	 */
	private void openHistory() {

		Thread history = new Thread(new Runnable() {

			@Override
			public void run() {
				String historyHtml = HistoryPageVariables.Heading;
				List<HistoryItem> historyList = getLatestHistory();
				Iterator<HistoryItem> it = historyList.iterator();
				HistoryItem helper;
				while (it.hasNext()) {
					helper = it.next();
					historyHtml += HistoryPageVariables.Part1 + helper.getUrl() + HistoryPageVariables.Part2
							+ helper.getTitle() + HistoryPageVariables.Part3 + helper.getUrl()
							+ HistoryPageVariables.Part4;
				}

				historyHtml += HistoryPageVariables.End;
				File historyWebPage = new File(getFilesDir(), "history.html");
				try {
					FileWriter hWriter = new FileWriter(historyWebPage, false);
					hWriter.write(historyHtml);
					hWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mCurrentView.loadUrl(B_Constants.FILE + historyWebPage);
				mSearch.setText("");
			}

		});
		history.run();
	}

	/**
	 * helper function that opens the bookmark drawer
	 */
	private void openBookmarks() {
		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
			mDrawerLayout.closeDrawers();
		}
		mDrawerToggle.syncState();
		mDrawerLayout.openDrawer(mDrawerRight);
	}

	public void closeDrawers() {
		mDrawerLayout.closeDrawers();
	}

	@Override
	/**
	 * open the HTML bookmarks page, parameter view is the WebView that should
	 * show the page
	 */
	public void openBookmarkPage(WebView view) {
		String bookmarkHtml = BookmarkPageVariables.Heading;
		Iterator<HistoryItem> iter = mBookmarkList.iterator();
		HistoryItem helper;
		while (iter.hasNext()) {
			helper = iter.next();
			bookmarkHtml += (BookmarkPageVariables.Part1 + helper.getUrl() + BookmarkPageVariables.Part2
					+ helper.getUrl() + BookmarkPageVariables.Part3 + helper.getTitle() + BookmarkPageVariables.Part4);
		}
		bookmarkHtml += BookmarkPageVariables.End;
		File bookmarkWebPage = new File(mContext.getCacheDir(), "bookmarks.html");
		try {
			FileWriter bookWriter = new FileWriter(bookmarkWebPage, false);
			bookWriter.write(bookmarkHtml);
			bookWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		view.loadUrl(B_Constants.FILE + bookmarkWebPage);
	}

	/**
	 * adds a bookmark with a title and url. Simple.
	 */
	public void addBookmark(Context context, String title, String url) {
		File book = new File(context.getFilesDir(), "bookmarks");
		File bookUrl = new File(context.getFilesDir(), "bookurl");
		HistoryItem bookmark = new HistoryItem(url, title);

		try {
			BufferedReader readUrlRead = new BufferedReader(new FileReader(bookUrl));
			String u;
			while ((u = readUrlRead.readLine()) != null) {
				if (u.contentEquals(url)) {
					readUrlRead.close();
					return;
				}
			}
			readUrlRead.close();

		} catch (FileNotFoundException ignored) {
		} catch (IOException ignored) {
		} catch (NullPointerException ignored) {
		}
		try {
			BufferedWriter bookWriter = new BufferedWriter(new FileWriter(book, true));
			BufferedWriter urlWriter = new BufferedWriter(new FileWriter(bookUrl, true));
			bookWriter.write(title);
			urlWriter.write(url);
			bookWriter.newLine();
			urlWriter.newLine();
			bookWriter.close();
			urlWriter.close();
			mBookmarkList.add(bookmark);
			Collections.sort(mBookmarkList, new SortIgnoreCase());
			notifyBookmarkDataSetChanged();
		} catch (FileNotFoundException ignored) {
		} catch (IOException ignored) {
		} catch (NullPointerException ignored) {
		}
		mSearchAdapter.refreshBookmarks();
	}

	@Override
	public void update() {
		mTitleAdapter.notifyDataSetChanged();
	}

	@Override
	/**
	 * opens a file chooser param ValueCallback is the message from the WebView
	 * indicating a file chooser should be opened
	 */
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");
		startActivityForResult(Intent.createChooser(i, "File Chooser"), 1);
	}

	@Override
	/**
	 * used to allow uploading into the browser, doesn't get called in KitKat :(
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 1) {
			if (null == mUploadMessage) {
				return;
			}
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;

		}
	}

	@Override
	/**
	 * handles long presses for the browser, tries to get the url of the item
	 * that was clicked and sends it (it can be null) to the click handler that
	 * does cool stuff with it
	 */
	public void onLongPress() {
		if (mClickHandler == null) {
			mClickHandler = new ClickHandler(mContext);
		}
		Message click = mClickHandler.obtainMessage();
		if (click != null) {
			click.setTarget(mClickHandler);
		}
		mCurrentView.getWebView().requestFocusNodeHref(click);
	}

	@Override
	public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
		if (view == null) {
			return;
		}
		if (mCustomView != null && callback != null) {
			callback.onCustomViewHidden();
			return;
		}
		view.setKeepScreenOn(true);
		mOriginalOrientation = getRequestedOrientation();
		FrameLayout decor = (FrameLayout) getWindow().getDecorView();
		mFullscreenContainer = new FullscreenHolder(this);
		mCustomView = view;
		mFullscreenContainer.addView(mCustomView, COVER_SCREEN_PARAMS);
		decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
		setFullscreen(true);
		mCurrentView.setVisibility(View.GONE);
		if (view instanceof FrameLayout) {
			if (((FrameLayout) view).getFocusedChild() instanceof VideoView) {
				mVideoView = (VideoView) ((FrameLayout) view).getFocusedChild();
				mVideoView.setOnErrorListener(new VideoCompletionListener());
				mVideoView.setOnCompletionListener(new VideoCompletionListener());
			}
		}
		mCustomViewCallback = callback;
	}

	@Override
	public void onHideCustomView() {
		if (mCustomView == null || mCustomViewCallback == null || mCurrentView == null) {
			return;
		}
		Log.i(B_Constants.TAG, "onHideCustomView");
		mCurrentView.setVisibility(View.VISIBLE);
		mCustomView.setKeepScreenOn(false);
		setFullscreen(mPreferences.getBoolean(PreferenceConstants.HIDE_STATUS_BAR, false));
		FrameLayout decor = (FrameLayout) getWindow().getDecorView();
		if (decor != null) {
			decor.removeView(mFullscreenContainer);
		}

		if (API < 19) {
			try {
				mCustomViewCallback.onCustomViewHidden();
			} catch (Throwable ignored) {

			}
		}
		mFullscreenContainer = null;
		mCustomView = null;
		if (mVideoView != null) {
			mVideoView.setOnErrorListener(null);
			mVideoView.setOnCompletionListener(null);
			mVideoView = null;
		}
		setRequestedOrientation(mOriginalOrientation);
	}

	private class VideoCompletionListener implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return false;
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			onHideCustomView();
		}

	}

	/**
	 * turns on fullscreen mode in the app
	 * 
	 * @param enabled
	 *            whether to enable fullscreen or not
	 */
	public void setFullscreen(boolean enabled) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (enabled) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
			if (mCustomView != null) {
				mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			} else {
				mBrowserFrame.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			}
		}
		win.setAttributes(winParams);
	}

	/**
	 * a class extending FramLayout used to display fullscreen videos
	 */
	static class FullscreenHolder extends FrameLayout {

		public FullscreenHolder(Context ctx) {
			super(ctx);
			setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			return true;
		}

	}

	@Override
	/**
	 * a stupid method that returns the bitmap image to display in place of a
	 * loading video
	 */
	public Bitmap getDefaultVideoPoster() {
		if (mDefaultVideoPoster == null) {
			mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_play);
		}
		return mDefaultVideoPoster;
	}

	@SuppressLint("InflateParams")
	@Override
	/**
	 * dumb method that returns the loading progress for a video
	 */
	public View getVideoLoadingProgressView() {
		if (mVideoProgressView == null) {
			LayoutInflater inflater = LayoutInflater.from(this);
			mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
		}
		return mVideoProgressView;
	}

	@Override
	/**
	 * handles javascript requests to create a new window in the browser
	 */
	public void onCreateWindow(boolean isUserGesture, Message resultMsg) {
		if (resultMsg == null) {
			return;
		}
		newTab("", true);
		WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		transport.setWebView(mCurrentView.getWebView());
		resultMsg.sendToTarget();
	}

	@Override
	/**
	 * returns the Activity instance for this activity, very helpful when
	 * creating things in other classes... I think
	 */
	public Activity getActivity() {
		return mActivity;
	}

	/**
	 * it hides the action bar, seriously what else were you expecting
	 */
	@Override
	public void hideActionBar() {
		if (mActionBar.isShowing() && mFullScreen) {
			mActionBar.hide();
		}
	}

	@Override
	/**
	 * obviously it shows the action bar if it's hidden
	 */
	public void showActionBar() {
		if (!mActionBar.isShowing() && mFullScreen) {
			mActionBar.show();
		}
	}

	@Override
	/**
	 * handles a long click on the page, parameter String url is the url that
	 * should have been obtained from the WebView touch node thingy, if it is
	 * null, this method tries to deal with it and find a workaround
	 */
	public void longClickPage(final String url) {
		HitTestResult result = null;
		if (mCurrentView.getWebView() != null) {
			result = mCurrentView.getWebView().getHitTestResult();
		}
		if (url != null) {
			if (result != null) {
				if (result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE
						|| result.getType() == HitTestResult.IMAGE_TYPE) {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:

								mDrawerLayout.openDrawer(mDrawer);
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										newTab(url, false);
										mDrawerLayout.closeDrawer(mDrawer);
									}
								}, 200);

								break;

							case DialogInterface.BUTTON_NEGATIVE:
								mCurrentView.loadUrl(url);
								break;

							case DialogInterface.BUTTON_NEUTRAL:
								if (API > 8) {
									B_Utils.downloadFile(mActivity, url, mCurrentView.getUserAgent(), "attachment",
											false);
								}
								break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
					builder.setTitle(url.replace(B_Constants.HTTP, ""))
							.setMessage(getResources().getString(R.string.dialog_image))
							.setPositiveButton(getResources().getString(R.string.action_new_tab), dialogClickListener)
							.setNegativeButton(getResources().getString(R.string.action_open), dialogClickListener)
							.setNeutralButton(getResources().getString(R.string.action_download), dialogClickListener)
							.show();

				} else {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								mDrawerLayout.openDrawer(mDrawer);
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										newTab(url, false);
										mDrawerLayout.closeDrawer(mDrawer);
									}
								}, 100);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								mCurrentView.loadUrl(url);
								break;

							case DialogInterface.BUTTON_NEUTRAL:
								ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
								ClipData clip = ClipData.newPlainText("label", url);
								clipboard.setPrimaryClip(clip);
								break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
					builder.setTitle(url).setMessage(getResources().getString(R.string.dialog_link))
							.setPositiveButton(getResources().getString(R.string.action_new_tab), dialogClickListener)
							.setNegativeButton(getResources().getString(R.string.action_open), dialogClickListener)
							.setNeutralButton(getResources().getString(R.string.action_copy), dialogClickListener)
							.show();
				}
			} else {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							newTab(url, false);
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							mCurrentView.loadUrl(url);
							break;

						case DialogInterface.BUTTON_NEUTRAL:
							ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							ClipData clip = ClipData.newPlainText("label", url);
							clipboard.setPrimaryClip(clip);

							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
				builder.setTitle(url).setMessage(getResources().getString(R.string.dialog_link))
						.setPositiveButton(getResources().getString(R.string.action_new_tab), dialogClickListener)
						.setNegativeButton(getResources().getString(R.string.action_open), dialogClickListener)
						.setNeutralButton(getResources().getString(R.string.action_copy), dialogClickListener).show();
			}
		} else if (result != null) {
			if (result.getExtra() != null) {
				final String newUrl = result.getExtra();
				if (result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE
						|| result.getType() == HitTestResult.IMAGE_TYPE) {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								newTab(newUrl, false);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								mCurrentView.loadUrl(newUrl);
								break;

							case DialogInterface.BUTTON_NEUTRAL:
								if (API > 8) {
									B_Utils.downloadFile(mActivity, newUrl, mCurrentView.getUserAgent(), "attachment",
											false);
								}
								break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
					builder.setTitle(newUrl.replace(B_Constants.HTTP, ""))
							.setMessage(getResources().getString(R.string.dialog_image))
							.setPositiveButton(getResources().getString(R.string.action_new_tab), dialogClickListener)
							.setNegativeButton(getResources().getString(R.string.action_open), dialogClickListener)
							.setNeutralButton(getResources().getString(R.string.action_download), dialogClickListener)
							.show();

				} else {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								newTab(newUrl, false);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								mCurrentView.loadUrl(newUrl);
								break;

							case DialogInterface.BUTTON_NEUTRAL:
								ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
								ClipData clip = ClipData.newPlainText("label", newUrl);
								clipboard.setPrimaryClip(clip);

								break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
					builder.setTitle(newUrl).setMessage(getResources().getString(R.string.dialog_link))
							.setPositiveButton(getResources().getString(R.string.action_new_tab), dialogClickListener)
							.setNegativeButton(getResources().getString(R.string.action_open), dialogClickListener)
							.setNeutralButton(getResources().getString(R.string.action_copy), dialogClickListener)
							.show();
				}

			}

		}

	}

	/**
	 * This method lets the search bar know that the page is currently loading
	 * and that it should display the stop icon to indicate to the user that
	 * pressing it stops the page from loading
	 */
	public void setIsLoading() {
		System.out.println("ISLOADING 1");
		if (!mSearch.hasFocus()) {
			System.out.println("ISLOADING 2");
			mIcon = mDeleteIcon;
			mSearch.setCompoundDrawables(null, null, mDeleteIcon, null);
		}
	}

	/**
	 * This tells the search bar that the page is finished loading and it should
	 * display the refresh icon
	 */
	public void setIsFinishedLoading() {
		System.out.println("setIsFinishedLoading 1");
		if (!mSearch.hasFocus()) {
			System.out.println("setIsFinishedLoading 2");
			mIcon = mRefreshIcon;
			mSearch.setCompoundDrawables(null, null, mRefreshIcon, null);
		}
	}

	/**
	 * handle presses on the refresh icon in the search bar, if the page is
	 * loading, stop the page, if it is done loading refresh the page.
	 * 
	 * See setIsFinishedLoading and setIsLoading for displaying the correct icon
	 */
	public void refreshOrStop() {
		System.out.println("refreshOrStop 1");
		if (mCurrentView != null) {
			System.out.println("refreshOrStop 1");
			if (mCurrentView.getProgress() < 100) {
				mCurrentView.stopLoading();
			} else {
				mCurrentView.reload();
			}
		}
	}

	@Override
	public boolean isActionBarShowing() {
		if (mActionBar != null) {
			return mActionBar.isShowing();
		} else {
			return false;
		}
	}

	public class SortIgnoreCase implements Comparator<HistoryItem> {

		public int compare(HistoryItem o1, HistoryItem o2) {
			return o1.getTitle().toLowerCase(Locale.getDefault())
					.compareTo(o2.getTitle().toLowerCase(Locale.getDefault()));
		}

	}

	void handleFooter() {
		final ImageView ivBack = (ImageView) findViewById(R.id.imageView_BackwardIcon);
		final ImageView ivNext = (ImageView) findViewById(R.id.imageView_ForWardIcon);
		final ImageView ivTab = (ImageView) findViewById(R.id.ivTabIcon);
		final ImageView ivSetting = (ImageView) findViewById(R.id.settingIcon);
		final ImageView ivHome = (ImageView) findViewById(R.id.imageView_Home);
		ivNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ivTab.setSelected(false);
				ivSetting.setSelected(false);
				ivHome.setSelected(false);
				mDrawerLayout.closeDrawer(mDrawer);
				if (mCurrentView != null) {
					if (mCurrentView.canGoForward()) {
						mCurrentView.goForward();
						ivBack.setVisibility(View.VISIBLE);
					} else {
						Toast.makeText(mContext, "Can't go forward", 3).show();
					}
				}

			}
		});
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ivTab.setSelected(false);
				ivSetting.setSelected(false);
				ivHome.setSelected(false);
				mDrawerLayout.closeDrawer(mDrawer);
				if (mCurrentView != null) {
					if (mCurrentView.canGoBack()) {
						mCurrentView.goBack();
						ivNext.setVisibility(View.VISIBLE);
					} else {
						Toast.makeText(mContext, "Can't go backward", 3).show();
					}
				}

			}
		});
		ivSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ivTab.setSelected(false);
				ivHome.setSelected(false);
				if (mDrawerLayout.isDrawerOpen(mDrawer)) {
					mDrawerLayout.closeDrawer(mDrawer);
				}
				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
					ivSetting.setSelected(false);
				} else {
					mDrawerLayout.openDrawer(mDrawerRight);
					ivSetting.setSelected(true);
				}

			}
		});
		ivTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
				ivSetting.setSelected(false);
				ivHome.setSelected(false);
				if (mDrawerLayout.isDrawerOpen(mDrawer)) {
					mDrawerLayout.closeDrawer(mDrawer);
					ivTab.setSelected(false);
				} else {
					mDrawerLayout.openDrawer(mDrawer);
					ivTab.setSelected(true);
				}
			}
		});
		ivHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MainActivity.isMainCalled) {
					Intent intHome = new Intent(B_BrowserActivity.this, MainActivity.class);
					startActivity(intHome);
					if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
						mDrawerLayout.closeDrawer(mDrawerRight);
					}
					ivSetting.setSelected(false);
					ivTab.setSelected(false);
					if (ivHome.isSelected()) {
						ivHome.setSelected(false);
					} else {
						ivHome.setSelected(true);
					}
					if (mDrawerLayout.isDrawerOpen(mDrawer)) {
						mDrawerLayout.closeDrawer(mDrawer);

					}
				} else {
					Intent intHome = new Intent(B_BrowserActivity.this, Splash.class);
					startActivity(intHome);
					if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
						mDrawerLayout.closeDrawer(mDrawerRight);
					}
					ivSetting.setSelected(false);
					ivTab.setSelected(false);
					if (ivHome.isSelected()) {
						ivHome.setSelected(false);
					} else {
						ivHome.setSelected(true);
					}
					if (mDrawerLayout.isDrawerOpen(mDrawer)) {
						mDrawerLayout.closeDrawer(mDrawer);

					}
				}

			}
		});
	}

	void handleDrawerMenu() {
		TextView tvHistory = (TextView) findViewById(R.id.tvHistory);
		TextView tvFindInPage = (TextView) findViewById(R.id.tvFindInPage);
		TextView tvCopyLink = (TextView) findViewById(R.id.tvCopyLink);
		TextView tvAddBookMarks = (TextView) findViewById(R.id.tvAddBookMarks);
		TextView tvBookMarks = (TextView) findViewById(R.id.text2);
		tvBookMarks.setTypeface(tfSemiBoldItalic);

		tvAddBookMarks.setTypeface(tfSemiBoldItalic);
		tvHistory.setTypeface(tfSemiBoldItalic);
		tvCopyLink.setTypeface(tfSemiBoldItalic);
		tvFindInPage.setTypeface(tfSemiBoldItalic);
		tvBookMarks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
				mDrawerListRight.setVisibility(View.VISIBLE);

			}
		});
		tvHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
				newTab(getHomepage(), true);
				openHistory();

			}
		});
		tvFindInPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
				findInPage();
			}
		});
		tvCopyLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
				if (mCurrentView != null) {
					if (!mCurrentView.getUrl().startsWith(B_Constants.FILE)) {
						ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						ClipData clip = ClipData.newPlainText("label", mCurrentView.getUrl().toString());
						clipboard.setPrimaryClip(clip);
						B_Utils.showToast(mContext, mContext.getResources().getString(R.string.message_link_copied));
					}
				}

			}
		});
		tvAddBookMarks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
					mDrawerLayout.closeDrawer(mDrawerRight);
				}
				// TODO Auto-generated method stub
				if (!mCurrentView.getUrl().startsWith(B_Constants.FILE)) {
					addBookmark(B_BrowserActivity.this, mCurrentView.getTitle(), mCurrentView.getUrl());
					Toast.makeText(B_BrowserActivity.this, getResources().getString(R.string.bookmark_added), 3).show();
				}
			}
		});

	}

	class SearchReportAsyncTaskxsxxx extends AsyncTask<Void, Void, Void> {
		String responce = "";
		String imei = "";

		public void postData() {
			// Toast.makeText(getApplicationContext(), ""+email+" "+feedback,
			// Toast.LENGTH_SHORT).show();

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost httppost = new
			// HttpPost(Constant.serverPath+"/feedback.php");
			HttpPost httppost = new HttpPost(Constant.serverPath + "/SearchReport.php");
			// a. IMEI
			// b. GID
			// c. AID
			// d. Country
			// e. Handset

			try {
				// Add your data

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

				nameValuePairs
						.add(new BasicNameValuePair("AID", HashGenerator.encrypt(androidID, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Search",
						HashGenerator.encrypt("" + SearchString, Constant.EncriptionKey)));
				nameValuePairs.add(
						new BasicNameValuePair("Lat", HashGenerator.encrypt(MainActivity.lat, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Lang",
						HashGenerator.encrypt(MainActivity.lng, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("appId", Constant.AppID));
				nameValuePairs.add(new BasicNameValuePair("BoxerVersion", Constant.App_Version));
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
			if (responce != null) {
				Log.e("Search Responce ", "Responce " + responce);
			}
		}
	}

	public void overrideUrl(String url) {
		// TODO Auto-generated method stub
		mSearch.setText(url);
	}

	public void onPageStrated(WebView view, String url, String tag) {
		mSearch.setText(url);
		Log.e("Tag is", tag);
		Log.e("Tag is", "onPageStrated Checked Tab is " + mDrawerList.getCheckedItemPosition() + "  getAllUrl().size()"
				+ db.getAllUrl().size());

		if (tag.equals("UPDATE")) {
			Log.e("Updating Url " + mDrawerList.getCheckedItemPosition(), "" + url);
			db.updateContact(new Url(mDrawerList.getCheckedItemPosition(), url));

		} else {
			DatabaseURLHandler db = new DatabaseURLHandler(mActivity);
			Log.e("Adding URL in DB at " + (db.getAllUrl().size()), "" + url);
			ID id = new ID();
			id.setId(db.getAllUrl().size());
			mUrl_ID_Array.add(id);
			db.addUrl(new Url(db.getAllUrl().size(), url));

		}
	}
	
	public void delete_download_tab()
	{
		if(CommonUtilities.delete_app_download_tab)
		{
			CommonUtilities.delete_app_download_tab = false;
			deleteTab(mDrawerList.getCheckedItemPosition());
			//Toast.makeText(B_BrowserActivity.this, "Tab Deleted",Toast.LENGTH_LONG).show();
		}
	}

	public void onPageFinished(WebView view, String url, String tag) {
		mSearch.setText(url);
		Log.e("Tag is", tag);
		Log.e("Tag is", "Checked Tab is " + mDrawerList.getCheckedItemPosition() + "  getAllUrl().size()"
				+ db.getAllUrl().size());

		if (tag.equals("UPDATE")) {
			Log.e("Updating Url " + mDrawerList.getCheckedItemPosition(), "" + url);
			db.updateContact(new Url(mDrawerList.getCheckedItemPosition(), url));

		} else {

			DatabaseURLHandler db = new DatabaseURLHandler(mActivity);
			Log.e("Adding URL in DB at " + (db.getAllUrl().size()), "" + url);
			ID id = new ID();
			id.setId(db.getAllUrl().size());
			mUrl_ID_Array.add(id);
			db.addUrl(new Url(db.getAllUrl().size(), url));

		}
		
		//Toast.makeText(B_BrowserActivity.this, "Before Tab Deleted",Toast.LENGTH_LONG).show();
		
//		if(CommonUtilities.delete_app_download_tab)
//		{
//			CommonUtilities.delete_app_download_tab = false;
//			deleteTab(mDrawerList.getCheckedItemPosition());
//			Toast.makeText(B_BrowserActivity.this, "Tab Deleted",Toast.LENGTH_LONG).show();
//		}

		// mTitleAdapter.notifyDataSetChanged();

		// Log.e("Binit", "Activity onPageFinished " + url);
		//
		// if (isReloading) {
		// // isReloading = false;
		// Log.e("Binit", "isReloading " + isReloading);
		// if (db.getAllUrl().size() > mDrawerList.getCheckedItemPosition()) {
		// // mUrlArray.set(mDrawerList.getCheckedItemPosition(), url);
		// Log.e("print ", mDrawerList.getCheckedItemPosition() + " "
		// + url);
		//
		// } else {
		// Log.e("Else part ", "new check point");
		// }
		//
		// } else {
		//
		// Log.e("Binit", "isReloading " + isReloading);
		//
		// // mUrlArray.add(mUrlArray.size(), url);
		//
		// // Log.e("Binit", "isReloading " + isReloading);
		//
		// // if (tabPosition > tabPosition) {
		//
		// // try {
		// // // Log.e("Binit", "reseting url " + url);
		// // mUrlArray.set(mDrawerList.getCheckedItemPosition(), url);
		// // Log.e("print ", mDrawerList.getCheckedItemPosition() + "");
		// // } catch (Exception e) {
		// // // TODO: handle exception
		// // // Log.e("========adding url", " in exception " + url);
		// // mUrlArray.add(mUrlArray.size(), url);
		// // Log.e("adding at" + " " + mUrlArray.size() + "", url);
		// // }
		// // Log.e("Array Size ", mUrlArray.size() + "");
		// // for (int i = 0; i < mUrlArray.size(); i++) {
		// // Log.e(" Item at " + i, mUrlArray.get(i));
		// // }
		// // JSONObject json = new JSONObject();
		// // try {
		// // json.put("uniqueArrays", new JSONArray(mUrlArray));
		// //
		// // } catch (JSONException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// // String arrayList = json.toString();
		// // Log.e("Json Array saving values", arrayList);
		// // PreferenceManager.getDefaultSharedPreferences(this).edit()
		// // .putString("MYLABEL", arrayList).commit();
		// }
	}

	public String getHomepage() {

		String home;
		home = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta content=\"en-us\" http-equiv=\"Content-Language\" /><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0\"><title>"
				+ "Homepage" + "</title></head>"
				+ "<style>body{background:#f2f2f2;text-align:center;}#search_input{height:35px; width:100%;outline:none;border:none;font-size: 16px;background-color:transparent;}span { display: block; overflow: hidden; padding-left:5px;vertical-align:middle;}.search_bar{display:table;vertical-align:middle;width:90%;height:35px;max-width:500px;margin:0 auto;background-color:#fff;box-shadow: 0px 3px rgba( 0, 0, 0, 0.1 );font-family: Arial;color: #444;-moz-border-radius: 2px;-webkit-border-radius: 2px;border-radius: 2px;}#search_submit{outline:none;height:37px;float:right;color:#404040;font-size:16px;font-weight:bold;border:none;background-color:transparent;}div.center{vertical-align:middle;height:100%;width:100%;max-height:300px; position: absolute; top:0; bottom: 0; left: 0; right: 0; margin: auto;}img.smaller{width:50%;max-width:300px;}.box { vertical-align:middle;position:relative; display: block; margin: 10px;padding-left:10px;padding-right:10px;padding-top:5px;padding-bottom:5px; background-color:#fff;box-shadow: 0px 3px rgba( 0, 0, 0, 0.1 );font-family: Arial;color: #444;font-size: 12px;-moz-border-radius: 2px;-webkit-border-radius: 2px;border-radius: 2px;}</style><body>"
				+ "<div class=\"center\"><img class=\"smaller\" src=\"";
		SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String engine = sharedPreferences1.getString("defaultengine", Constant.defaultEngine);
		int pos = 0;
		if (engine.equals("google")) {
			pos = 1;
		} else if (engine.equals("bing")) {
			pos = 3;
		} else if (engine.equals("yahoo")) {
			pos = 4;
		} else if (engine.equals("ask")) {
			pos = 11;
		}
		if (pos == 0) {
			pos = 1;
		}
		switch (mPreferences.getInt(PreferenceConstants.SEARCH, pos)) {
		case 0:
			// CUSTOM SEARCH
			home = home + "file:///android_asset/lightning.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + mPreferences.getString(PreferenceConstants.SEARCH_URL, B_Constants.GOOGLE_SEARCH);
			break;
		case 1:
			// GOOGLE_SEARCH;
			home = home + "file:///android_asset/google.png";
			// + "https://www.google.com/images/srpr/logo11w.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.GOOGLE_SEARCH;
			break;
		case 2:
			// ANDROID SEARCH;
			home = home + "file:///android_asset/lightning.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.ANDROID_SEARCH;
			break;
		case 3:
			// BING_SEARCH;
			home = home + "file:///android_asset/bing.png";
			// +
			// "http://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Bing_logo_%282013%29.svg/500px-Bing_logo_%282013%29.svg.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.BING_SEARCH;
			break;
		case 4:
			// YAHOO_SEARCH;
			home = home + "file:///android_asset/yahoo.png";
			// +
			// "http://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Yahoo%21_logo.svg/799px-Yahoo%21_logo.svg.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.YAHOO_SEARCH;
			break;
		case 5:
			// STARTPAGE_SEARCH;
			home = home + "file:///android_asset/startpage.png";
			// + "https://startpage.com/graphics/startp_logo.gif";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.STARTPAGE_SEARCH;
			break;
		case 6:
			// STARTPAGE_MOBILE
			home = home + "file:///android_asset/startpage.png";
			// + "https://startpage.com/graphics/startp_logo.gif";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.STARTPAGE_MOBILE_SEARCH;
		case 7:
			// DUCK_SEARCH;
			home = home + "file:///android_asset/duckduckgo.png";
			// +
			// "https://duckduckgo.com/assets/logo_homepage.normal.v101.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.DUCK_SEARCH;
			break;
		case 8:
			// DUCK_LITE_SEARCH;
			home = home + "file:///android_asset/duckduckgo.png";
			// +
			// "https://duckduckgo.com/assets/logo_homepage.normal.v101.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.DUCK_LITE_SEARCH;
			break;
		case 9:
			// BAIDU_SEARCH;
			home = home + "file:///android_asset/baidu.png";
			// + "http://www.baidu.com/img/bdlogo.gif";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.BAIDU_SEARCH;
			break;
		case 10:
			// YANDEX_SEARCH;
			home = home + "file:///android_asset/yandex.png";
			// +
			// "http://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Yandex.svg/600px-Yandex.svg.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.YANDEX_SEARCH;
			break;
		case 11:
			// YANDEX_SEARCH;
			home = home + "file:///android_asset/Ask.png";
			// +
			// "http://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Yandex.svg/600px-Yandex.svg.png";
			home = home + HomepageVariables.MIDDLE;
			home = home + B_Constants.ASK_SEARCH;
			break;

		}

		home = home + HomepageVariables.END;

		File homepage = new File(mActivity.getCacheDir(), "homepage.html");
		try {
			FileWriter hWriter = new FileWriter(homepage, false);
			hWriter.write(home);
			hWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return B_Constants.FILE + homepage;
	}

}
