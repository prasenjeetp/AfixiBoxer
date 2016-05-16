package com.boxer.browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.boxer.bean.WrtualBean;
import com.boxer.util.Constant;
import com.boxer.util.HashGenerator;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.manishkpr.slidingtabsexample.adapter.TabsPagerAdapter;
import com.manishkpr.slidingtabsexample.common.view.SlidingTabLayout;
import com.mobileapptracker.MobileAppTracker;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements OnClickListener,
		OnItemClickListener, OnDrawerCloseListener, OnDrawerOpenListener,
		OnEditorActionListener {

	public MobileAppTracker mobileAppTracker = null;

	private SlidingTabLayout mSlidingTabLayout;
	public static boolean isMainCalled;

	private ViewPager mViewPager;
	// //////////////////////////////////
	public static String mainUrl = "";
	public static int l = 1;
	private int START_DELAY = 1;
	private PendingIntent tracking;
	boolean doubleBackToExitPressedOnce = false;
	private AlarmManager alarms, alarmTime;
	CheckBox notificationBox;
	ShareExternalServer appUtil;
	String regId;
	RadioButton rbtn;
	static String engine;
	private LocationManager locationManager;
	Location location;
	static boolean isIntentFired = false;

	// Progress Dialog Bar

	ProgressDialog prgDialog1;
	AsyncTask<Void, Void, String> shareRegidTask;

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";

	GoogleCloudMessaging gcm;

	GcmBroadcastReceiver broadCastReceiver = new GcmBroadcastReceiver();
	String apk = "";
	String thirdPartyUrl = "";
	String currentAppId = "";
	String SearchString = "";
	public static String lat, lng;

	String packageName, packageName1, packageName2, packageName3, packageName4,
			packageName5, packageName6, packageName7, packageName8,
			packageName9, packageName10, packageName11, packageName12,
			packageName13, packageName14, packageName15, packageName16,
			packageName17, packageName18, packageName19, packageName20,
			packageName21, packageName22, packageName23;

	LinearLayout linear_item1, linear_item2, linear_item3, linear_item4,
			linear_item5, linear_item6, linear1_item1, linear1_item2,
			linear1_item3, linear1_item4, linear1_item5, linear1_item6,
			linear2_item1, linear2_item2, linear2_item3, linear2_item4,
			linear2_item5, linear2_item6, linear3_item1, linear3_item2,
			linear3_item3, linear3_item4, linear3_item5, linear3_item6;

	String companyName, paidAppPrice, preappImage, app_describ, companyName1,
			paidAppPrice1, preappImage1, app_describ1, companyName2,
			paidAppPrice2, preappImage2, app_describ2, companyName3,
			paidAppPrice3, preappImage3, app_describ3, companyName4,
			paidAppPrice4, preappImage4, app_describ4, companyName5,
			paidAppPrice5, preappImage5, app_describ5, cat1_companyName,
			cat1_paidAppPrice, cat1_preappImage, cat1_app_describ,
			cat1_companyName1, cat1_paidAppPrice1, cat1_preappImage1,
			cat1_app_describ1, cat1_companyName2, cat1_paidAppPrice2,
			cat1_preappImage2, cat1_app_describ2, cat1_companyName3,
			cat1_paidAppPrice3, cat1_preappImage3, cat1_app_describ3,
			cat1_companyName4, cat1_paidAppPrice4, cat1_preappImage4,
			cat1_app_describ4, cat1_companyName5, cat1_paidAppPrice5,
			cat1_preappImage5, cat1_app_describ5,

			cat2_companyName, cat2_paidAppPrice, cat2_preappImage,
			cat2_app_describ, cat2_companyName1, cat2_paidAppPrice1,
			cat2_preappImage1, cat2_app_describ1, cat2_companyName2,
			cat2_paidAppPrice2, cat2_preappImage2, cat2_app_describ2,
			cat2_companyName3, cat2_paidAppPrice3, cat2_preappImage3,
			cat2_app_describ3, cat2_companyName4, cat2_paidAppPrice4,
			cat2_preappImage4, cat2_app_describ4, cat2_companyName5,
			cat2_paidAppPrice5, cat2_preappImage5, cat2_app_describ5,

			cat3_companyName, cat3_paidAppPrice, cat3_preappImage,
			cat3_app_describ, cat3_companyName1, cat3_paidAppPrice1,
			cat3_preappImage1, cat3_app_describ1, cat3_companyName2,
			cat3_paidAppPrice2, cat3_preappImage2, cat3_app_describ2,
			cat3_companyName3, cat3_paidAppPrice3, cat3_preappImage3,
			cat3_app_describ3, link1, link2, link3, link4, link5, link6,
			link11, link12, link13, link14, link15, link16, link21, link22,
			link23, link24, link25, link26, link31, link32, link33, link34,
			link35, link36, linkPreOne, linkPreTwo, linkPreThree, linkPreFour,
			linkPreFive, linkPreSix;

	ImageView imageViewSearchIcon, imageViewCloseSearch;
	int imgHeight;

	AQuery a;
	// action bar
	public ActionBar actionBar;
	// Refresh menu item
	private MenuItem refreshMenuItem;
	ImageView settingIcon, shareIcon, freeAppIcon, ivTabIcon, ivHome;
	Dialog dialog;
	SearchView searchView;
	TextView selectCat, appNameOne, freeAppName, appCompany, appPrice;
	Context context;
	FrameLayout frameLayout;
	Animation animationUp, animationLeft;
	private ImageSwitcher is;
	ImageButton btnNext;
	private SharedPreferences mPreferences;
	// Button shareAppButton;

	HorizontalScrollView hScrollView;
	String appImage[];
	String freeApp_logoimage[];
	// apk file name

	String apkName = "";

	// AppImageAdapter adapter;
	int curIndex;
	WebView webView;

	public static int countTopAppOne = 0;

	// array of image url

	DetailOnPageChangeListener listener;
	// LazyImageLoader lazyAdapter;

	// topContent one app name

	String topContentOne = "";
	String freeAppPackageName = "";
	// feed back varibale

	String appName = "";
	String email = "";
	String feedback = "";
	// edit text for feed back
	EditText editTextUserEmail, editTextFeedback, editTextSearch;

	// download app variable
	ProgressBar pb;
	Dialog downloadDialog;
	int downloadedSize = 0;
	int totalSize = 0;
	TextView cur_val;
	String dwnload_file_path;

	public static final int DIALOG_LOADING = 1;
	private ArrayList<WrtualBean> appInfoList = new ArrayList<WrtualBean>();
	private ArrayList<WrtualBean> slidingDrawerDataList = new ArrayList<WrtualBean>();

	// app screen list
	public ArrayList<String> appScreenUrlList = new ArrayList<String>();
	ArrayList<Bitmap> appScreenbitmapList = new ArrayList<Bitmap>();
	ArrayList<String> free_appicon = new ArrayList<String>();

	// fatch data from server as string
	String appNameStr, appDisStr;
	public static String androidID = "";

	private static final String STATE_POSITION = "STATE_POSITION";
	ViewPager myPager;

	Bitmap appIcon, appScreenBitMap, appScreenOneBitMap, appScreenTwoBitMap,
			appScreenThreeBitMap, itemicon1, itemicon2, itemicon3, itemicon4,
			itemicon5, itemicon6, itemicon7, itemicon8, itemicon9, itemicon10,
			itemicon11, itemicon12, itemicon13, itemicon14, itemicon15,
			itemicon16, itemicon17, itemicon18;
	WrtualBean appInfo = null;

	// array of image url
	String[] imageUrls;

	Button downloadAppButton;
	Typeface tfRegular;
	// TextView topContentOneAppOne, topContentOneAppTwo, topContentOneAppThree,
	// topContentOneAppFour, topContentOneAppFive, topContentOneAppSix,
	// topLinkOneName, topLinkTwoName, topLinkThreeName, topLinkFourName,
	// topLinkFiveName, topLinkSixName;
	private ImageLoader imageLoader;
	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawerRight;
	private FrameLayout mBrowserFrame;

	// ShareExternalServer appUtil;
	// String regId;
	// AsyncTask<Void, Void, String> shareRegidTask;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		setupUI(findViewById(R.id.llMain));
		Log.e("MainActivity", "Calling");
		isMainCalled = true;
		mPreferences = getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
		tfRegular = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebRegular);
		mobileAppTracker = MobileAppTracker.init(getApplicationContext(),
				Constant.MatAdvID, Constant.MatConvKey);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);
		// setRetainInstance(true);
		alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		// Utility util=new Utility();
		// util.copyUserDataBase(context);
		// userDb=new UserDbUtil(context);
		androidID = ""
				+ Secure.getString(MainActivity.this.getContentResolver(),
						Secure.ANDROID_ID);
		// shareAppButton = (Button) findViewById(R.id.button_ShareApp);
		editTextSearch = (EditText) findViewById(R.id.editText_searchText);

		editTextSearch.setTypeface(tfRegular);
		imageViewSearchIcon = (ImageView) findViewById(R.id.imageView_searchIcon);
		imageViewCloseSearch = (ImageView) findViewById(R.id.imageView_closeSearch);
		settingIcon = (ImageView) findViewById(R.id.settingIcon);
		ivHome = (ImageView) findViewById(R.id.imageView_Home);
		ivHome.setSelected(true);
		ivTabIcon = (ImageView) findViewById(R.id.ivTabIcon);
		freeAppName = (TextView) findViewById(R.id.textView_FreeAppName);
		appCompany = (TextView) findViewById(R.id.textView_appCompany);
		appPrice = (TextView) findViewById(R.id.textView_appPrice);
		freeAppIcon = (ImageView) findViewById(R.id.imageView_FreeAppIcon);
		// freeAppScreen = (ImageView)
		// findViewById(R.id.imageView_freeAppScreen);
		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
		// editTextSearch.setVisibility(View.GONE);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);

		imageViewCloseSearch.setVisibility(View.GONE);

		editTextSearch.setCursorVisible(true);
		editTextSearch.setInputType(InputType.TYPE_NULL);

		// download app button

		downloadAppButton = (Button) findViewById(R.id.button_DownloadApp);

		System.out.println("Brand----------> " + android.os.Build.BRAND);
		System.out.println("Brand----------> " + android.os.Build.MODEL);
		System.out.println("Brand----------> " + android.os.Build.MANUFACTURER);

		// initView();
		System.out.println("height =" + imgHeight);

		try {
			// // free app
			// freeApp_async_post_entity();
			//
			// // top content 2
			// async_post_entity1();
			//
			// // previous app
			// async_post_entity2();
			//
			// // top content 1
			// async_post_entity3();
			//
			// // TopContent 3
			// async_post_entity4();
			register_post_entity();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		onClickListenerOnView();
		setRecurringAlarmTime(getBaseContext());

		imageViewSearchIcon.setOnClickListener(this);

		editTextSearch.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Log.e("Tag ", "go");
				MobileAppTracker mobileAppTracker = MobileAppTracker
						.getInstance();

				mobileAppTracker.setGoogleUserId(Splash.email);

				mobileAppTracker.setLocation(location);

				mobileAppTracker.measureEvent(Constant.MatSrchEventID); // Tracking
																		// event
				mobileAppTracker.measureSession(); // of
													// search
				editTextSearch.setCursorVisible(true);
				// finish();

				SharedPreferences sharedPreferences1 = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);
				engine = sharedPreferences1.getString("defaultengine",
						Constant.defaultEngine);

				System.out.println("defaultengine" + engine);

				try {
					if (actionId == EditorInfo.IME_ACTION_GO) {

						String url = editTextSearch.getText().toString();
						if (url.contains("market://details?")) {
							mainUrl = editTextSearch.getText().toString();
						} else if (url.contains(".")) {
							mainUrl = "http://"
									+ editTextSearch.getText().toString();
						} else {

							if (engine.equalsIgnoreCase("google")) {
								mainUrl = "https://google.co.in/search?as_q="
										+ Uri.encode(editTextSearch.getText()
												.toString());

							}

							else if (engine.equalsIgnoreCase("bing")) {
								mainUrl = "https://www.bing.com/search?q="
										+ Uri.encode(editTextSearch.getText()
												.toString());
							} else if (engine.equalsIgnoreCase("yahoo")) {
								mainUrl = "https://in.search.yahoo.com/search?p="
										+ Uri.encode(editTextSearch.getText()
												.toString());
							} else if (engine.equalsIgnoreCase("ask")) {
								mainUrl = "http://www.ask.com/web?q="
										+ Uri.encode(editTextSearch.getText()
												.toString());
							}
							SearchString = editTextSearch.getText().toString();
							SearchString = SearchString.replaceAll("[-+.^:,]",
									"");
							lat = "" + Splash.lat;
							lng = "" + Splash.lng;
							InputMethodManager inputManager = (InputMethodManager) MainActivity.this
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							inputManager.hideSoftInputFromWindow(
									MainActivity.this.getCurrentFocus()
											.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

							// mainUrl =
							// "http://www.google.co.in/?gfe_rd=ctrl&ei=4j4DU_KsCOaJ8QfyhYHYCQ&gws_rd=cr#q="
							// + editTextSearch.getText().toString();
						}
						String activity = "MainActivity";

						// String url =
						// "https://www.google.co.in/?gfe_rd=ctrl&ei=4j4DU_KsCOaJ8QfyhYHYCQ&gws_rd=cr#q="+editTextSearch.getText().toString();
						editTextSearch.setText("");
						Intent intent = new Intent(MainActivity.this,
								B_MainActivity.class);
						// intent.putExtra("url", mainUrl);
						// intent.putExtra("activity",activity);
						// System.out.println("m ="+mainUrl);
						isIntentFired = true;
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_right);

						new SearchReportAsyncTask().execute();

						Log.e("Tag ", "gone");
						// InputMethodManager imm = (InputMethodManager)
						// getSystemService(Activity.INPUT_METHOD_SERVICE);
						// imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,
						// 0);
						// editTextSearch.setVisibility(View.GONE);
						// imageViewSearchIcon.setVisibility(View.VISIBLE);

						// mainUrl="";
						// l=2;
					}
				} catch (Exception e) {
				}
				return false;
			}
		});
		// editTextSearch.setFocusable(false);

		editTextSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editTextSearch.setCursorVisible(true);
				// TODO Auto-generated method stub
				editTextSearch.requestFocus();
				editTextSearch.setText("");
				editTextSearch.setInputType(InputType.TYPE_CLASS_TEXT);

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(editTextSearch,
						InputMethodManager.SHOW_IMPLICIT);
			}
		});
		// ///////////////////////////////////TABS////////////////////////////////
		setUpPager();
		setUpTabColor();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("Main Activity", "onResume");
		ivHome.setSelected(true);
		settingIcon.setSelected(false);
	}

	private void checkAppInstallOrNot(String freeAppPackageName) {
		System.out.println("name =" + freeAppPackageName);

		boolean installed = appInstalledOrNot(freeAppPackageName);
		System.out.println("b =" + installed);
		if (installed) {
			downloadAppButton.setText("Open");
			openApplication();
		} else {
			downloadAppButton.setText("Free");
			installApp();

		}

	}

	private void installApp() {

		downloadAppButton.setOnClickListener(this);
	}

	private void openApplication() {

		downloadAppButton.setOnClickListener(this);
	}

	// check app install or not
	public boolean appInstalledOrNot(String freeAppPackageName) {

		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(freeAppPackageName, PackageManager.GET_ACTIVITIES);
			app_installed = true;
			// downloadAppButton.setText("open");
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	private void getImageList() {
		for (int i = 0; i < appScreenUrlList.size(); i++) {
			String url = appScreenUrlList.get(i).toString();
			System.out.println("bit map =" + url);
			// downloadAppScreenBitmap(url);

		}
	}

	private void onClickListenerOnView() {
		// textView.setOnClickListener(this);
		settingIcon.setOnClickListener(this);
		ivHome.setOnClickListener(this);
		ivTabIcon.setOnClickListener(this);
		// shareAppButton.setOnClickListener(this);
		imageViewSearchIcon.setOnClickListener(this);
		// editTextSearch.setOnEditorActionListener(this);
		imageViewCloseSearch.setOnClickListener(this);

	}

	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void onClick(View view) {
		if (view != editTextSearch) {

			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
		switch (view.getId()) {
		case R.id.settingIcon:
			settingIcon.setSelected(true);
			ivTabIcon.setSelected(false);
			ivHome.setSelected(false);
			Intent intent = new Intent(MainActivity.this, MenuActivity.class);
			startActivity(intent);
			break;
		case R.id.ivTabIcon:
			ivTabIcon.setSelected(true);
			settingIcon.setSelected(false);
			ivHome.setSelected(false);
			Toast.makeText(context, getResources().getString(R.string.Loading),
					6).show();

			if (B_BrowserActivity.isTabOpen) {
				this.finish();
			} else {
				// Toast.makeText(this,
				// getResources().getString(R.string.no_tab_open), 3)
				// .show();
				mainUrl = getHomepage();
				Intent intentssss = new Intent(MainActivity.this,
						B_MainActivity.class);

				startActivity(intentssss);
			}

			break;
		case R.id.imageView_Home:
			ivTabIcon.setSelected(false);
			settingIcon.setSelected(false);
			ivHome.setSelected(true);

			break;

		default:
			break;
		}

		if (view == downloadAppButton
				&& downloadAppButton.getText().equals("Open")) {

			new ClickCountAsyncTask(currentAppId, "ProClick.php").execute();

			// This intent will help you to launch if the package is already
			// installed
			System.out.println("jpack" + freeAppPackageName);
			Intent LaunchIntent = getPackageManager()
					.getLaunchIntentForPackage(freeAppPackageName);
			if (LaunchIntent == null) {
				// Toast.makeText(getApplicationContext(),
				// "no",Toast.LENGTH_SHORT).show();
				downloadAppButton.setText("Free");
			} else {
				startActivity(LaunchIntent);
			}

			System.out.println("App already installed on your phone");
		}
		if (view == downloadAppButton
				&& downloadAppButton.getText().equals("Free")) {
			new ClickCountAsyncTask(currentAppId, "ProClick.php").execute();
			boolean check = isNetworkAvailable();
			if (check != true) {
				Intent intent = new Intent(MainActivity.this,
						NoInternetConnection.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_right);
				// finish();
				return;
			}
			if (!thirdPartyUrl.equals("")) {

				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(thirdPartyUrl));
				startActivity(intent);
				return;
			}

			DownloadConform(15);
		}

		// if (view == settingIcon) {
		//
		// openDialog(1);// open dialog
		// }
		if (view == selectCat) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.OK), Toast.LENGTH_SHORT)
					.show();

		}
		// if (view == shareAppButton) {
		//
		// boolean check = isNetworkAvailable();
		// if (check != true) {
		// Intent intent = new Intent(MainActivity.this,
		// NoInternetConnection.class);
		// startActivity(intent);
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_right);
		// // finish();
		// return;
		// }
		//
		// Intent sharingIntent = new Intent(
		// android.content.Intent.ACTION_SEND);
		// sharingIntent.setType("text/plain");
		// // System.out.println(" link ===" + appInfo.getAppLinkUrl());
		// System.out.println("qget kerra hu=" + thirdPartyUrl);
		//
		// String sd = thirdPartyUrl.substring(0);
		//
		// System.out
		// .println("This is finalurl h==https://play.google.com/store/apps"
		// + sd);
		//
		// String shareBody = ""
		// + appNameStr
		// + "\napplication is free for today.You can download from-\n"
		// + thirdPartyUrl;
		//
		// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		// "Today's Free App ");
		// sharingIntent
		// .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		// startActivity(Intent.createChooser(sharingIntent, "Share via"));
		// }

		if (view == imageViewSearchIcon) {
			// editTextSearch.setVisibility(View.VISIBLE);
			// imageViewSearchIcon.setVisibility(View.GONE);
			// imageViewCloseSearch.setVisibility(View.VISIBLE);
			// editTextSearch.requestFocus();
			// finish();
			// Intent ip=new Intent(MainActivity.this,B_MainActivity.class);
			// startActivity(ip);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// editTextSearch.setFocusableInTouchMode(true);
			// editTextSearch.setRawInputType(InputType.TYPE_TEXT_VARIATION_URI);
			// editTextSearch.setTextIsSelectable(true);
			// editTextSearch.setImeOptions(EditorInfo.IME_ACTION_GO);

			// InputMethodManager imm = (InputMethodManager)
			// getSystemService(Context.INPUT_METHOD_SERVICE);
			// imm.showSoftInput(editTextSearch,
			// InputMethodManager.SHOW_IMPLICIT);
		}

		if (view == imageViewCloseSearch) {
			imageViewCloseSearch.setVisibility(View.GONE);
			editTextSearch.setVisibility(View.GONE);
			imageViewSearchIcon.setVisibility(View.VISIBLE);
			// InputMethodManager imm = (InputMethodManager)
			// getSystemService(Activity.INPUT_METHOD_SERVICE);
			// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		}

	}

	private void DownloadConform(int i) {
		if (i == 15) {
			final Dialog dialog = new Dialog(MainActivity.this);

			// dialog.setTitle(" Feed back");
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_conform_download);
			dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			dialog.show();

			Button exitYes = (Button) dialog.findViewById(R.id.button_exitYes);
			Button exitNo = (Button) dialog.findViewById(R.id.button_exitNo);
			exitYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					dialog.dismiss();
					dwnload_file_path = Constant.fileFolder + apkName + ".apk";
					showProgress(dwnload_file_path);
					new Thread(new Runnable() {
						public void run() {
							downloadFile();
						}

						void downloadFile() {

							try {

								URL url = new URL(dwnload_file_path);
								HttpURLConnection urlConnection = (HttpURLConnection) url
										.openConnection();
								urlConnection.setRequestMethod("GET");
								urlConnection.setDoOutput(true);

								// connect
								urlConnection.connect();

								// set the path where we want to save the file
								final File SDCardRoot = Environment
										.getExternalStorageDirectory();
								// create a new file, to save the downloaded
								// file
								File file = new File(SDCardRoot, apkName
										+ ".apk");

								FileOutputStream fileOutput = new FileOutputStream(
										file);

								// Stream used for reading the data from the
								// internet
								InputStream inputStream = urlConnection
										.getInputStream();

								// this is the total size of the file which we
								// are downloading
								totalSize = urlConnection.getContentLength();

								runOnUiThread(new Runnable() {
									public void run() {
										pb.setMax(totalSize);
									}
								});

								// create a buffer...
								byte[] buffer = new byte[1024];
								int bufferLength = 0;

								while ((bufferLength = inputStream.read(buffer)) > 0) {
									fileOutput.write(buffer, 0, bufferLength);
									downloadedSize += bufferLength;
									// update the progressbar //
									runOnUiThread(new Runnable() {
										public void run() {
											pb.setProgress(downloadedSize);
											float per = ((float) downloadedSize / totalSize) * 100;
											cur_val.setText("Downloading "
													+ downloadedSize + "KB / "
													+ totalSize + "KB ("
													+ (int) per + "%)");

										}
									});
								}
								// close the output stream when complete //
								fileOutput.close();

								runOnUiThread(new Runnable() {
									public void run() {
										dialog.dismiss(); // if you want close
															// it..
										// this.checkUnknownSourceEnability();
										// this.initiateInstallation();
										installApk();

									}

									private void installApk() {
										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										// new
										// File(Environment.getExternalStorageDirectory()
										// +"Fashionist.apk")
										Uri uri = Uri.fromFile(new File(
												"/sdcard/" + apkName + ".apk"));
										intent.setDataAndType(uri,
												"application/vnd.android.package-archive");
										startActivityForResult(intent, 10);
										checkAppInstallOrNot(freeAppPackageName);
										// Toast.makeText(getApplicationContext(),
										// "delete", Toast.LENGTH_SHORT).show();

									}
								});

							} catch (final MalformedURLException e) {
								showError("Error : MalformedURLException " + e);
								e.printStackTrace();
							} catch (final IOException e) {
								showError("Error : IOException " + e);
								e.printStackTrace();
							}

							catch (final Exception e) {
								showError("Error : Please check your internet connection "
										+ e);
							}

						}

						void showError(final String err) {
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(MainActivity.this, err,
											Toast.LENGTH_LONG).show();
								}
							});
						}
					}).start();
				}
			});
			exitNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

		}

	}

	void showProgress(String file_path) {
		dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.download_progress_dialog);
		dialog.setTitle("Download Progress");

		TextView text = (TextView) dialog.findViewById(R.id.tv1);
		text.setText("" + appNameStr);
		cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
		dialog.show();

		pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
		pb.setProgress(0);
		pb.setProgressDrawable(getResources().getDrawable(
				R.drawable.download_app_progress));
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	private void openDialog(int i) {

		if (i == 1) {

			final Dialog dialog = new Dialog(MainActivity.this);

			dialog.setTitle(" Settings");
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.main_menu);
			// notificationBox = (CheckBox) dialog
			// .findViewById(R.id.checkBox_dialog);
			//
			// SharedPreferences sharedPreferences = PreferenceManager
			// .getDefaultSharedPreferences(this);
			// boolean checkBoxValue = sharedPreferences.getBoolean(
			// "CheckBox_Value", true);
			//
			// if (checkBoxValue) {
			// notificationBox.setChecked(true);
			// } else {
			// notificationBox.setChecked(false);
			// }
			//
			// dialog.getWindow().getAttributes().windowAnimations =
			// R.style.DialogAnimation;
			dialog.show();
			//
			// TextView textViewAbout = (TextView) dialog
			// .findViewById(R.id.textView_About);
			// textViewAbout.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, About.class);
			// startActivityForResult(intent, 0);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// });
			//
			// TextView textViewHelp = (TextView) dialog
			// .findViewById(R.id.textView_Help);
			// textViewHelp.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, Help.class);
			// startActivityForResult(intent, 0);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// });
			//
			// notificationBox.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// savePreferences("CheckBox_Value",
			// notificationBox.isChecked());
			//
			// }
			// });
			//
			// TextView textView_PP = (TextView) dialog
			// .findViewById(R.id.textView_PP);
			// textView_PP.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, PrivecyAndPolicy.class);
			// startActivityForResult(intent, 0);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			//
			// }
			// });
			// TextView textViewLicense = (TextView) dialog
			// .findViewById(R.id.textView_License);
			// textViewLicense.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, License.class);
			// startActivityForResult(intent, 0);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			//
			// }
			// });
			// TextView textViewBrowser = (TextView) dialog
			// .findViewById(R.id.textView_Browser);
			// textViewBrowser.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// final Dialog dialogSearch = new Dialog(MainActivity.this);
			//
			// dialogSearch.setTitle("Search Engines");
			// dialogSearch.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// dialogSearch.setContentView(R.layout.dialog_searchengine);
			// DisplayMetrics metrics = context.getResources()
			// .getDisplayMetrics();
			//
			// int width = metrics.widthPixels;
			// int height = metrics.heightPixels;
			// dialogSearch.getWindow().setLayout((7 * width) / 9,
			// (4 * height) / 7);
			// dialogSearch.show();
			//
			// SharedPreferences sharedPreferences1 = PreferenceManager
			// .getDefaultSharedPreferences(MainActivity.this);
			// engine = sharedPreferences1.getString("defaultengine",
			// "bing");
			// Log.e("shared pref", engine);
			// final RadioGroup rg = (RadioGroup) dialogSearch
			// .findViewById(R.id.radiogrp);
			//
			// RadioButton rbGoogle = (RadioButton) dialogSearch
			// .findViewById(R.id.radiogoogle);
			// RadioButton rbask = (RadioButton) dialogSearch
			// .findViewById(R.id.radioask);
			// RadioButton rbbing = (RadioButton) dialogSearch
			// .findViewById(R.id.radioBing);
			// RadioButton rbyahoo = (RadioButton) dialogSearch
			// .findViewById(R.id.radioyahoo);
			//
			// if (engine.equalsIgnoreCase("google")) {
			// // Toggle status of checkbox selection
			// rbGoogle.setChecked(true);
			// } else if (engine.equalsIgnoreCase("bing")) {
			// rbbing.setChecked(true);
			//
			// } else if (engine.equalsIgnoreCase("yahoo")) {
			// rbyahoo.setChecked(true);
			//
			// }
			//
			// else if (engine.equalsIgnoreCase("ask")) {
			// rbask.setChecked(true);
			//
			// }
			//
			// Button cancelbtn = (Button) dialogSearch
			// .findViewById(R.id.btncancel);
			//
			// cancelbtn.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// dialogSearch.dismiss();
			//
			// }
			// });
			//
			// Button okbtn = (Button) dialogSearch
			// .findViewById(R.id.btnok);
			//
			// okbtn.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			//
			// rbtn = (RadioButton) dialogSearch.findViewById(rg
			// .getCheckedRadioButtonId());
			// String valueradio = rbtn.getText().toString();
			// String lower = valueradio.toLowerCase();
			//
			// // System.out.println(lower);
			//
			// SharedPreferences sharedPreferences = PreferenceManager
			// .getDefaultSharedPreferences(MainActivity.this);
			// Editor editor = sharedPreferences.edit();
			// editor.putString("defaultengine", lower);
			// if (engine.equalsIgnoreCase("google")) {
			// // Toggle status of checkbox selection
			// editor.putInt(PreferenceConstants.SEARCH, 1);
			// } else if (engine.equalsIgnoreCase("bing")) {
			// editor.putInt(PreferenceConstants.SEARCH, 3);
			// } else if (engine.equalsIgnoreCase("yahoo")) {
			// editor.putInt(PreferenceConstants.SEARCH, 4);
			// }
			//
			// else if (engine.equalsIgnoreCase("ask")) {
			// editor.putInt(PreferenceConstants.SEARCH, 2);
			// }
			// editor.commit();
			//
			// dialogSearch.dismiss();
			//
			// }
			// });
			// dialog.dismiss();
			// }
			// });
			//
			// TextView textViewFeedback = (TextView) dialog
			// .findViewById(R.id.textView_Feedback);
			// textViewFeedback.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// dialog.dismiss();
			// openDialogFeedBack(2);
			//
			// }
			//
			// });
			//
		}

	}

	// private void openDialogFeedBack(int i) {
	//
	// if (i == 2) {
	// final Dialog dialog = new Dialog(MainActivity.this);
	//
	// dialog.setTitle(" Feed back");
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.dialog_layout_feedback);
	// dialog.getWindow().getAttributes().windowAnimations =
	// R.style.DialogAnimation;
	// dialog.show();
	//
	// EditText editTextAppName = (EditText) dialog
	// .findViewById(R.id.editText_AppName);
	// editTextAppName.setText("");
	// appName = editTextAppName.getText().toString();
	// editTextUserEmail = (EditText) dialog
	// .findViewById(R.id.editText_UserEmail);
	// editTextUserEmail.setText("claymmedia@gmail.com");
	//
	// editTextUserEmail.setFocusable(false);
	//
	// email = editTextUserEmail.getText().toString();
	// editTextFeedback = (EditText) dialog
	// .findViewById(R.id.editText_writeFeedback);
	// feedback = editTextFeedback.getText().toString();
	// final Button submitFeedBack = (Button) dialog
	// .findViewById(R.id.button_submitFeedBack);
	// submitFeedBack.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View view) {
	// boolean validationResult = formValidator();
	// if (validationResult == false) {
	// return;
	// }
	// boolean check = isNetworkAvailable();
	// if (check != true) {
	// Intent intent = new Intent(MainActivity.this,
	// NoInternetConnection.class);
	// startActivity(intent);
	// overridePendingTransition(R.anim.slide_in_right,
	// R.anim.slide_out_right);
	// // finish();
	// return;
	// } else {
	// new FeedbackAsyncTask().execute();
	// Toast.makeText(getApplicationContext(),
	// "Successfully Submitted", Toast.LENGTH_SHORT)
	// .show();
	// editTextFeedback.setText("");
	// editTextUserEmail.setText("");
	// dialog.dismiss();
	// openDialog(1);
	// }
	//
	// }
	//
	// private boolean formValidator() {
	// String strFeedback = editTextFeedback.getText().toString()
	// .trim();
	// String strEmail = editTextUserEmail.getText().toString()
	// .trim();
	//
	// boolean b3 = isEmpty(strFeedback, editTextFeedback,
	// "You can't leave this empty.");
	// boolean b2 = isValid(strEmail, editTextUserEmail,
	// "Enter valid email.");
	// boolean b1 = isEmpty(strEmail, editTextUserEmail,
	// "You can't leave this empty.");
	//
	// if (b1 && b2 && b3) {
	// return true;
	// }
	//
	// return false;
	// }
	//
	// private boolean isValid(String strEmail,
	// EditText editTextUserEmail, String errorMsg) {
	//
	// String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	// int ecolor = Color.RED;
	// String estring = errorMsg;
	//
	// ForegroundColorSpan fgcspan = new ForegroundColorSpan(
	// ecolor);
	// SpannableStringBuilder msg = new SpannableStringBuilder(
	// estring);
	// msg.setSpan(fgcspan, 0, estring.length(), 0);
	// CharSequence inputStr = strEmail;
	// Pattern pattern = Pattern.compile(expression,
	// Pattern.CASE_INSENSITIVE);
	// Matcher matcher = pattern.matcher(inputStr);
	// if (matcher.matches()) {
	// return true;
	// } else {
	// editTextUserEmail.setError(msg);
	// return false;
	// }
	// }
	//
	// private boolean isEmpty(CharSequence charSequence,
	// EditText feedback, String errorMsg) {
	// if (charSequence.equals("")) {
	// int ecolor = Color.RED;
	// String estring = errorMsg;
	// ForegroundColorSpan fgcspan = new ForegroundColorSpan(
	// ecolor);
	// SpannableStringBuilder msg = new SpannableStringBuilder(
	// estring);
	// msg.setSpan(fgcspan, 0, estring.length(), 0);
	// feedback.setError(msg);
	// return false;
	// } else {
	// return true;
	// }
	//
	// }
	// });
	// }
	//
	// }

	public class MyAsyncTask extends AsyncTask<Void, Void, Void> {
		public void postData() {

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constant.serverPath + "/report");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs
						.add(new BasicNameValuePair("name", topContentOne));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

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
	}

	@Override
	public void onDrawerClosed() {

	}

	RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	boolean sliderFlag = true;

	@Override
	public void onDrawerOpened() {
		// TODO Auto-generated method stub
		if (sliderFlag) {
			System.out.println("on drawer close listner call");
			sliderFlag = false;
		}
	}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			System.out.println("RegId already available. RegId: " + regId);

		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Constant.GOOGLE_PROJECT_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				System.out.println("Registered with GCM Server." + msg);

			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

	public void register_post_entity() throws Exception {

		context = getApplicationContext();
		if (TextUtils.isEmpty(regId)) {
			regId = registerGCM();
			Log.d("RegisterActivity", "GCM RegId: " + regId);
		} else {
			System.out.println("Already Registered with GCM Server!");

		}

		if (regId != null) {
			appUtil = new ShareExternalServer();

			Log.d("MainActivity", "regId: " + regId);

			final Context context = this;
			shareRegidTask = new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					String result = appUtil.shareRegIdWithAppServer(context,
							regId);
					return result;
				}

				@Override
				protected void onPostExecute(String result) {
					shareRegidTask = null;

				}

			};

			shareRegidTask.execute(null, null, null);

		}

	}

	public void freeApp_async_post_entity() throws Exception {
		appInfo = new WrtualBean();
		String url = Constant.serverPath + "/Current.php";
		// String mail = "Kamal";

		a = new AQuery(getApplicationContext());
		// prgDialog = new ProgressDialog(this); // Instantiate Progress Dialog
		// Bar
		// prgDialog.setMessage("Please wait..."); // Set Progress Dialog Bar
		// // message
		// prgDialog.setIndeterminate(false);
		// prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress
		// // Bar style
		// prgDialog.setCancelable(false); // Progress Bar cannot be cancelable
		// Display progress dialog bar
		a.ajax(url, JSONObject.class, this, "jsonCallbackFreeApp");
		// a.progress(R.id.progressBar).ajax(url, JSONObject.class,
		// this,"jsonCallbackFreeApp");
		// new GetContacts().execute();
	}

	public void jsonCallbackFreeApp(String url, JSONObject json,
			AjaxStatus status) {
		System.out.println("current wala== " + json);

		// freeApp_image = (ImageView) findViewById(R.id.imageView_FreeAppIcon);
		// freeApp_Name = (TextView) findViewById(R.id.textView_FreeAppName);
		// freeApp_CompanyName = (TextView)
		// findViewById(R.id.textView_appCompany);
		// freeApp_Price=(TextView) findViewById(R.id.textView_appPrice);
		// freeAppScreen = (ImageView)
		// findViewById(R.id.imageView_freeAppScreen);

		appInfo = new WrtualBean();

		try {
			JSONArray jArray = json.getJSONArray("Current");
			JSONObject item = jArray.getJSONObject(0);
			System.out.println("current " + jArray);
			currentAppId = item.optString("id");
			// freeApp_Name.setText(item.getString("title"));
			// appNameStr = freeApp_Name.getText().toString();
			System.out.println("title =" + item.getString("title"));
			System.out.println("title =" + item.getString("icon"));
			companyName = (item.getString("company")).toString();
			// freeApp_CompanyName.setText(item.getString("company"));
			paidAppPrice = (item.getString("price")).toString();
			preappImage = item.getString("icon").toString();
			thirdPartyUrl = item.getString("glink");
			System.out.println("GGGlink==" + thirdPartyUrl);

			appInfo.setAppLinkUrl(thirdPartyUrl);

			System.out.println("qget kerra hu=" + appInfo.getAppLinkUrl());

			// freeApp_Price.setText("$"+paidAppPrice);
			freeAppPackageName = item.getString("package");
			// this.freeAppPackageName=packName;
			// a.id(freeApp_image).progress(dialog)
			// .image(Constant.serverPathImage + item.getString("icon"));
			app_describ = (item.getString("description")).toString();
			app_describ = app_describ.replaceAll("%", "&#37");

			System.out.println("des =" + app_describ);
			String appDis = "<body font-size: 5px; color: #000000;}</style>"
					+ "<p align=\"justify\">" + app_describ + "</body>";
			// WebView webView = (WebView) findViewById(R.id.webView1);
			// WebSettings settings = webView.getSettings();
			// settings.setDefaultFontSize(14);
			// webView.loadData(String.format(appDis, null), "text/html",
			// "utf-8");
			// a.id(freeAppScreen).progress(dialog)
			// .image(Constant.serverPathImage + item.getString("screen"));
			// s String
			// imageurl=Constant.serverPathImage+item.getString("screen");

			// a.id(freeAppScreen).progress(dialog).image("http://indiabigevents.files.wordpress.com/2012/09/lord-ganesh-pics.jpg");

			apk = item.getString("apk").toString();
			System.out.println("apk name " + apk);
			String[] parts = apk.split("\\.");
			this.apkName = parts[0];
			System.out.println("apk name s " + parts[0]);
			checkAppInstallOrNot(freeAppPackageName);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void async_post_entity3() throws Exception {
		a = new AQuery(getApplicationContext());
		String url = Constant.serverPath + "/Top_content1.php";
		a.progress(dialog).ajax(url, JSONObject.class, this,
				"jsonCallbackCat_1");

	}

	public void jsonCallbackCat_1(String url, JSONObject json, AjaxStatus status) {

		// ll7 = (LinearLayout) findViewById(R.id.ll_topAppOne);
		// ll8 = (LinearLayout) findViewById(R.id.ll_topApp2);
		// ll9 = (LinearLayout) findViewById(R.id.ll_topAppThree);
		// ll10 = (LinearLayout) findViewById(R.id.ll_topApp4);
		// ll11 = (LinearLayout) findViewById(R.id.ll_topAppFive);
		// ll12 = (LinearLayout) findViewById(R.id.ll_topAppSix);

		// txt_category1 = (TextView)
		// findViewById(R.id.textView_TopAppsHeading);
		// txt_category11 = (TextView)
		// findViewById(R.id.textView_TopAppOneName);
		// txt_category12 = (TextView)
		// findViewById(R.id.textView_TopAppTwoName);
		// txt_category13 = (TextView)
		// findViewById(R.id.textView_TopAppThreeName);
		// txt_category14 = (TextView)
		// findViewById(R.id.textView_TopAppFourName);
		// txt_category15 = (TextView)
		// findViewById(R.id.textView_TopAppFiveName);
		// txt_category16 = (TextView)
		// findViewById(R.id.textView_TopAppSixName);

		// image11 = (ImageView) findViewById(R.id.imageView_TopAppOneImage);
		// image12 = (ImageView) findViewById(R.id.imageView_TopAppTwoImage);
		// image13 = (ImageView) findViewById(R.id.imageView_TopAppThreeImage);
		// image14 = (ImageView) findViewById(R.id.imageView_TopAppFourImage);
		// image15 = (ImageView) findViewById(R.id.imageView_TopAppFiveImage);
		// image16 = (ImageView) findViewById(R.id.imageView_TopAppSixImage);

		System.out.println("TOp1---------------->" + json);

		try {
			JSONArray jarray = json.getJSONArray("TopContent1");
			final JSONObject item = jarray.getJSONObject(0);
			// txt_category1.setText("  " + item.getString("category"));
			// txt_category11.setText(item.getString("name"));
			// a.id(image11)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item.getString("icon").toString());
			link11 = item.getString("url");
			packageName6 = item.getString("pack");
			// free_appicon.add("http://192.168.1.3/sujoyDemo/uploads/"+item.getString("screen1").toString());
			// ll7.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method
			// //
			// stubhttps://github.com/nostra13/Android-Universal-Image-Loader/issues/138
			// new ClickCountAsyncTask(item.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link11.equalsIgnoreCase("")) {
			//
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName6);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category11.getText().toString();
			// new MyAsyncTask().execute();
			// if (link11.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link11)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link11);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item1 = jarray.getJSONObject(1);
			// txt_category12.setText(item1.getString("name"));
			// a.id(image12)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item1.getString("icon").toString());
			link12 = item1.getString("url");

			// fr
			packageName7 = item1.getString("pack");
			// ll8.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item1.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link12.equalsIgnoreCase("")) {
			//
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName7);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			// topContentOne = txt_category12.getText().toString();
			// new MyAsyncTask().execute();
			// if (link12.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link12)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			//
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link12);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item2 = jarray.getJSONObject(2);
			// txt_category13.setText(item2.getString("name"));
			// a.id(image13)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item2.getString("icon").toString());

			link13 = item2.getString("url");
			packageName8 = item2.getString("pack");
			// fr

			// ll9.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item2.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link13.equalsIgnoreCase("")) {
			//
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName8);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			//
			// // topContentOne = txt_category13.getText().toString();
			// new MyAsyncTask().execute();
			// if (link13.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link13)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link13);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });
			final JSONObject item3 = jarray.getJSONObject(3);
			// txt_category14.setText(item3.getString("name"));
			// a.id(image14)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item3.getString("icon").toString());
			link14 = item3.getString("url");
			packageName9 = item3.getString("pack");
			// ll10.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item3.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link14.equalsIgnoreCase("")) {
			//
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName9);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			//
			// topContentOne = txt_category14.getText().toString();
			// new MyAsyncTask().execute();
			// if (link14.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link14)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link14);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item4 = jarray.getJSONObject(4);
			// txt_category15.setText(item4.getString("name"));
			// a.id(image15)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item4.getString("icon").toString());
			link15 = item4.getString("url");
			packageName10 = item4.getString("pack");
			// ll11.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item4.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link15.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName10);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category15.getText().toString();
			// new MyAsyncTask().execute();
			// if (link15.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link15)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link15);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item5 = jarray.getJSONObject(5);
			// txt_category16.setText(item5.getString("name"));
			// a.id(image16)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item5.getString("icon").toString());
			link16 = item5.getString("url");
			packageName11 = item5.getString("pack");
			// ll12.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item5.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link16.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName11);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// // topContentOne = txt_category16.getText().toString();
			// new MyAsyncTask().execute();
			// if (link16.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link16)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link16);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void async_post_entity4() throws Exception {
		String url = Constant.serverPath + "/Top_content3.php";
		a = new AQuery(getApplicationContext());
		a.progress(dialog).ajax(url, JSONObject.class, this,
				"jsonCallbackCat_3");

	}

	// //////////////////////Shop
	// Now////////////////////////////////////////////////////
	public void jsonCallbackCat_3(String url, JSONObject json, AjaxStatus status) {// ///
																					// Shop
																					// Now
																					//
		// ll19 = (LinearLayout) findViewById(R.id.ll_TopLinkOne);
		// ll20 = (LinearLayout) findViewById(R.id.ll_TopLinkTwo);
		// ll21 = (LinearLayout) findViewById(R.id.ll_TopLinkThree);
		// ll22 = (LinearLayout) findViewById(R.id.ll_TopLinkFour);
		// ll23 = (LinearLayout) findViewById(R.id.ll_TopLinkFive);
		// ll24 = (LinearLayout) findViewById(R.id.ll_TopLinkSix);
		//
		// txt_category2 = (TextView)
		// findViewById(R.id.textView_TopLinkHeading);
		// txt_category21 = (TextView)
		// findViewById(R.id.textView_TopLinkOneName);
		// txt_category22 = (TextView)
		// findViewById(R.id.textView_TopLinkTwoName);
		// txt_category23 = (TextView)
		// findViewById(R.id.textView_TopLinkThreeName);
		// txt_category24 = (TextView)
		// findViewById(R.id.textView_TopLinkFourName);
		// txt_category25 = (TextView)
		// findViewById(R.id.textView_TopLinkFiveName);
		// txt_category26 = (TextView)
		// findViewById(R.id.textView_TopLinkSixName);
		//
		// image21 = (ImageView) findViewById(R.id.imageView_TopLinkOneImage);
		// image22 = (ImageView) findViewById(R.id.imageView_TopLinkTwoImage);
		// image23 = (ImageView) findViewById(R.id.imageView_TopLinkThreeImage);
		// image24 = (ImageView) findViewById(R.id.imageView_TopLinkFourImage);
		// image25 = (ImageView) findViewById(R.id.imageView_TopLinkFiveImage);
		// image26 = (ImageView) findViewById(R.id.imageView_TopLinkSixImage);
		System.out.println("@@@@@@@Shop Now" + json);

		try {
			JSONArray jarray = json.getJSONArray("TopContent3");// ////////Shop
																// Now
			final JSONObject item = jarray.getJSONObject(0);
			// txt_category2.setText("  " + item.getString("category"));
			// txt_category21.setText(item.getString("name"));
			// a.id(image21)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item.getString("icon").toString());
			link31 = item.getString("url");
			packageName12 = item.getString("pack");
			// ll19.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link31.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName12);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category21.getText().toString();
			// new MyAsyncTask().execute();
			// if (link31.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link31)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			//
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link31);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item1 = jarray.getJSONObject(1);
			// txt_category22.setText(item1.getString("name"));
			// a.id(image22)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item1.getString("icon").toString());
			link32 = item1.getString("url");
			packageName13 = item1.getString("pack");
			// ll20.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item1.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link32.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName13);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			// // topContentOne = txt_category22.getText().toString();
			// new MyAsyncTask().execute();
			// if (link32.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link32)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link32);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item2 = jarray.getJSONObject(2);
			// txt_category23.setText(item2.getString("name"));
			// a.id(image23)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item2.getString("icon").toString());
			link33 = item2.getString("url");
			packageName14 = item2.getString("pack");
			// ll21.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// new ClickCountAsyncTask(item2.optString("id"),
			// "topClick.php").execute();
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link33.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName14);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category23.getText().toString();
			// // System.out.println("adff ="+topContentOne);
			// new MyAsyncTask().execute();
			//
			// if (link33.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link33)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link33);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });
			final JSONObject item3 = jarray.getJSONObject(3);
			// txt_category24.setText(item3.getString("name"));
			// a.id(image24)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item3.getString("icon").toString());
			link34 = item3.getString("url");
			packageName15 = item3.getString("pack");
			// ll22.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item3.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link34.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName15);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category23.getText().toString();
			// // System.out.println("adff ="+topContentOne);
			// new MyAsyncTask().execute();
			//
			// if (link34.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link34)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link34);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// topContentOne = txt_category24.getText().toString();
			// new MyAsyncTask().execute();
			//
			// }
			// }
			// });

			final JSONObject item4 = jarray.getJSONObject(4);
			// txt_category25.setText(item4.getString("name"));
			// a.id(image25)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item4.getString("icon").toString());
			link35 = item4.getString("url");
			packageName16 = item4.getString("pack");
			// ll23.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item4.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link35.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName16);
			// startActivity(in);
			// } else {
			// topContentOne = txt_category25.getText().toString();
			// new MyAsyncTask().execute();
			// if (link35.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link35)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link35);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item5 = jarray.getJSONObject(5);
			// txt_category26.setText(item5.getString("name"));
			// a.id(image26)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item5.getString("icon").toString());
			link36 = item5.getString("url");
			packageName17 = item5.getString("pack");
			// ll24.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item5.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link36.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName17);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category26.getText().toString();
			// new MyAsyncTask().execute();
			//
			// if (link36.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link36)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link36);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void async_post_entity2() throws Exception {
		String url = Constant.serverPath + "/Previous.php";
		a = new AQuery(getApplicationContext());
		a.progress(dialog).ajax(url, JSONObject.class, this, "jsonCallback");

	}

	// //////////////////====prevous apps=====//////////////////////////////////
	public void jsonCallback(String url, JSONObject json, AjaxStatus status) {
		// llPriAppOne = (LinearLayout) findViewById(R.id.ll_PreAppOne);
		// llPriAppTwo = (LinearLayout) findViewById(R.id.ll_preAppTwo);
		// llPriAppThree = (LinearLayout) findViewById(R.id.ll_PreAppThree);
		// llPriAppFour = (LinearLayout) findViewById(R.id.ll_PreAppFour);
		// llPriAppFive = (LinearLayout) findViewById(R.id.ll_PreAppFive);
		// llPriAppSix = (LinearLayout) findViewById(R.id.ll_PreAppSix);

		// txt_preapp1 = (TextView) findViewById(R.id.textView_preAppOneName);
		// txt_preapp2 = (TextView) findViewById(R.id.textView_preAppTwoName);
		// txt_preapp3 = (TextView) findViewById(R.id.textView_preAppThreeName);
		// txt_preapp4 = (TextView) findViewById(R.id.textView_preAppFourName);
		// txt_preapp5 = (TextView) findViewById(R.id.textView_preAppFiveName);
		// txt_preapp6 = (TextView) findViewById(R.id.textView_preAppSixName);
		// txt_preappheading = (TextView)
		// findViewById(R.id.textView_AppsHeading);
		// txt_preappdate1 = (TextView)
		// findViewById(R.id.textView_preAppOneDate);
		// txt_preappdate2 = (TextView)
		// findViewById(R.id.textView_preAppTwoDate);
		// txt_preappdate3 = (TextView)
		// findViewById(R.id.textView_preAppThreeDate);
		// txt_preappdate4 = (TextView)
		// findViewById(R.id.textView_preAppFourDate);
		// txt_preappdate5 = (TextView)
		// findViewById(R.id.textView_preAppFiveDate);
		// txt_preappdate6 = (TextView)
		// findViewById(R.id.textView_preAppSixDate);
		//
		// image1 = (ImageView) findViewById(R.id.imageView_preAppOneImage);
		// image2 = (ImageView) findViewById(R.id.imageView_PreAppTwoImage);
		// image3 = (ImageView) findViewById(R.id.imageView_preAppThreeImage);
		// image4 = (ImageView) findViewById(R.id.imageView_preAppFourImage);
		// image5 = (ImageView) findViewById(R.id.imageView_preAppFiveImage);
		// image6 = (ImageView) findViewById(R.id.imageView_preAppSixImage);
		//
		// txt_preAppBuy1 = (TextView)
		// findViewById(R.id.textView_preAppOnePaid);
		// txt_preAppBuy2 = (TextView)
		// findViewById(R.id.textView_preAppTwoPaid);
		// txt_preAppBuy3 = (TextView)
		// findViewById(R.id.textView_preAppThreePaid);
		// txt_preAppBuy4 = (TextView)
		// findViewById(R.id.textView_preAppFourPaid);
		// txt_preAppBuy5 = (TextView)
		// findViewById(R.id.textView_preAppFivePaid);
		// txt_preAppBuy6 = (TextView)
		// findViewById(R.id.textView_preAppSixPaid);
		//
		// txt_preappheading.setText("  Previous App");

		System.out.println("Pre-------->>>>>>" + json);
		try {
			JSONArray jArray = json.getJSONArray("Previous");
			final JSONObject item = jArray.getJSONObject(0);

			// txt_preapp1.setText(item.getString("title"));
			System.out.println("title =" + item.getString("title"));
			System.out.println("title =" + item.getString("icon"));
			companyName = (item.getString("company")).toString();
			paidAppPrice = (item.getString("price")).toString();
			preappImage = item.getString("icon").toString();
			// a.id(image1).progress(dialog)
			// .image(Constant.serverPathImage + item.getString("icon"));
			app_describ = (item.getString("description")).toString();
			packageName = (item.getString("package")).toString();
			// txt_preAppBuy1.setText(item.getString("buy"));
			// txt_preAppBuy1.setText("Free");

			linkPreOne = item.getString("glink").toString();

			try {
				SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd", Locale.ENGLISH);
				System.out.println("DATE->>>>>>>>>>>>> "
						+ item.getString("date"));
				Date date = formatter.parse(item.getString("date"));
				formatter.applyPattern("EE, dd MMM");
				// txt_preappdate1.setText(formatter.format(date));
				// txt_preappdate1.setText("Yesterday");

				System.out.println("FOR----------" + formatter.format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// llPriAppOne.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View view) {
			// new ClickCountAsyncTask(item.optString("id"),
			// "ProClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// } else {
			// if (linkPreOne.equalsIgnoreCase("")) {
			//
			// // TODO Auto-generated method stub
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// /*
			// * in.putExtra("Title",
			// * txt_preapp1.getText().toString());
			// * in.putExtra("Company Name", companyName);
			// * in.putExtra("AppPrice", paidAppPrice);
			// * in.putExtra("Image",
			// * Constant.serverPath+"/uploads/"+preappImage);
			// * in.putExtra("Description", app_describ);
			// */
			// in.putExtra("pack", packageName);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			//
			// if (linkPreOne.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW,
			// Uri.parse(linkPreOne)));
			// overridePendingTransition(
			// R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", linkPreOne);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// }
			// }
			//
			// }
			//
			// });

			final JSONObject item1 = jArray.getJSONObject(1);
			// txt_preapp2.setText(item1.getString("title"));
			try {
				SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd", Locale.ENGLISH);
				System.out.println("DATE->>>>>>>>>>>>> "
						+ item.getString("date"));
				Date date = formatter.parse(item1.getString("date"));

				formatter.applyPattern("EE, dd MMM");

				// txt_preappdate2.setText(formatter.format(date));
				System.out.println("FOR2----------" + formatter.format(date));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// a.id(image2).progress(dialog)
			// .image(Constant.serverPathImage + item1.getString("icon"));
			companyName1 = (item1.getString("company")).toString();
			paidAppPrice1 = (item1.getString("price")).toString();
			preappImage1 = item1.getString("icon").toString();
			app_describ1 = (item1.getString("description")).toString();
			packageName1 = (item1.getString("package")).toString();
			// txt_preAppBuy2.setText(item.getString("buy"));
			// txt_preAppBuy2.setText("Free");

			linkPreTwo = item1.getString("glink").toString();
			// llPriAppTwo.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View view) {
			// new ClickCountAsyncTask(item1.optString("id"),
			// "ProClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (linkPreTwo.equalsIgnoreCase("")) {
			// // TODO Auto-generated method stub
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// /*
			// * in.putExtra("Title",
			// * txt_preapp2.getText().toString());
			// * in.putExtra("Company Name", companyName1);
			// * in.putExtra("AppPrice", paidAppPrice1);
			// * in.putExtra("Image",
			// * Constant.serverPath+"/uploads/"+preappImage1);
			// * in.putExtra("Description", app_describ1);
			// */
			// in.putExtra("pack", packageName1);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			//
			// if (linkPreTwo.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(linkPreTwo)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			//
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", linkPreTwo);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			//
			// }
			// });

			final JSONObject item2 = jArray.getJSONObject(2);

			// txt_preapp3.setText(item2.getString("title"));

			try {
				SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd", Locale.ENGLISH);
				System.out.println("DATE->>>>>>>>>>>>> "
						+ item.getString("date"));
				Date date = formatter.parse(item2.getString("date"));

				formatter.applyPattern("EE, dd MMM");

				// txt_preappdate3.setText(formatter.format(date));
				System.out.println("FOR2----------" + formatter.format(date));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// a.id(image3)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item2.getString("icon").toString());

			packageName2 = item2.getString("package");
			// txt_preAppBuy3.setText(item.getString("buy"));
			// txt_preAppBuy3.setText("Free");

			linkPreThree = item2.getString("glink").toString();
			// llPriAppThree.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View view) {
			// new ClickCountAsyncTask(item2.optString("id"),
			// "ProClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (linkPreThree.equalsIgnoreCase("")) {
			// // TODO Auto-generated method stub
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// /*
			// * in.putExtra("Title",
			// * txt_preapp3.getText().toString());
			// * in.putExtra("Company Name", companyName2);
			// * in.putExtra("AppPrice", paidAppPrice2);
			// * in.putExtra("Image",
			// * Constant.serverPath+"/uploads/"+preappImage2);
			// * in.putExtra("Description", app_describ2);
			// */
			// in.putExtra("pack", packageName2);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			//
			// if (linkPreThree.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(linkPreThree)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			//
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", linkPreThree);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			//
			// }
			// });

			final JSONObject item3 = jArray.getJSONObject(3);
			// txt_preapp4.setText(item3.getString("title"));
			// txt_preAppBuy4.setText(item.getString("buy"));
			// txt_preAppBuy4.setText("free");

			try {
				SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd", Locale.ENGLISH);
				System.out.println("DATE->>>>>>>>>>>>> "
						+ item.getString("date"));
				Date date = formatter.parse(item3.getString("date"));

				formatter.applyPattern("EE, dd MMM");

				// txt_preappdate4.setText(formatter.format(date));
				System.out.println("FOR2----------" + formatter.format(date));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// a.id(image4)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item3.getString("icon").toString());
			packageName3 = item3.getString("package");
			// txt_preAppBuy4.setText(item.getString("buy"));
			// txt_preAppBuy4.setText("Free");
			linkPreFour = item3.getString("glink").toString();

			// llPriAppFour.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View view) {
			// new ClickCountAsyncTask(item3.optString("id"),
			// "ProClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (linkPreFour.equals("")) {
			// // TODO Auto-generated method stub
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// /*
			// * in.putExtra("Title",
			// * txt_preapp4.getText().toString());
			// * in.putExtra("Company Name", companyName3);
			// * in.putExtra("AppPrice", paidAppPrice3);
			// * in.putExtra("Image",
			// * Constant.serverPath+"/uploads/"+preappImage3);
			// * in.putExtra("Description", app_describ3);
			// */
			// in.putExtra("pack", packageName3);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			//
			// if (linkPreFour.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(linkPreFour)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			//
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", linkPreFour);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			//
			// }
			// });

			final JSONObject item4 = jArray.getJSONObject(4);

			// txt_preapp5.setText(item4.getString("title"));

			try {
				SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd", Locale.ENGLISH);
				System.out.println("DATE->>>>>>>>>>>>> "
						+ item.getString("date"));
				Date date = formatter.parse(item4.getString("date"));

				formatter.applyPattern("EE, dd MMM");

				// txt_preappdate5.setText(formatter.format(date));
				System.out.println("FOR2----------" + formatter.format(date));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// a.id(image5)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item4.getString("icon").toString());
			packageName4 = item4.getString("package");
			// txt_preAppBuy5.setText(item.getString("buy"));
			// txt_preAppBuy5.setText("Free");
			linkPreFive = item4.getString("glink").toString();
			// llPriAppFive.setOnClickListener(new OnClickListener() {

			// @Override
			// public void onClick(View view) {
			// new ClickCountAsyncTask(item4.optString("id"),
			// "ProClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (linkPreFive.equals("")) {
			// // TODO Auto-generated method stub
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// // in.putExtra("Title",
			// // txt_preapp5.getText().toString());
			// // in.putExtra("Company Name", companyName4);
			// // in.putExtra("AppPrice", paidAppPrice4);
			// // in.putExtra("Image",
			// // Constant.serverPath+"/uploads/"+preappImage4);
			// // in.putExtra("Description", app_describ4);
			// in.putExtra("pack", packageName4);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			//
			// if (linkPreFive.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(linkPreFive)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			//
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", linkPreFive);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			//
			// }
			// });
			final JSONObject item5 = jArray.getJSONObject(5);
			// txt_preapp6.setText(item5.getString("title"));

			try {
				SimpleDateFormat formatter = new java.text.SimpleDateFormat(
						"yyyy-MM-dd", Locale.ENGLISH);
				System.out.println("DATE->>>>>>>>>>>>> "
						+ item.getString("date"));
				Date date = formatter.parse(item5.getString("date"));

				formatter.applyPattern("EE, dd MMM");

				// txt_preappdate6.setText(formatter.format(date));
				System.out.println("FOR2----------" + formatter.format(date));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// a.id(image6)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item5.getString("icon").toString());
			packageName5 = item5.getString("package");
			// txt_preAppBuy6.setText(item.getString("buy"));
			// txt_preAppBuy6.setText("Free");
			linkPreSix = item5.getString("glink").toString();

			// llPriAppSix.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View view) {
			// new ClickCountAsyncTask(item5.optString("id"),
			// "ProClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (linkPreSix.equals("")) {
			// // TODO Auto-generated method stub
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// /*
			// * in.putExtra("Title",
			// * txt_preapp6.getText().toString());
			// * in.putExtra("Company Name", companyName5);
			// * in.putExtra("AppPrice", paidAppPrice5);
			// * in.putExtra("Image",
			// * Constant.serverPath+"/uploads/"+preappImage5);
			// * in.putExtra("Description", app_describ5);
			// */
			// in.putExtra("pack", packageName5);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			//
			// if (linkPreSix.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(linkPreSix)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			//
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", linkPreSix);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			//
			// }
			// });

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void async_post_entity1() throws Exception {

		String url = Constant.serverPath + "/Top_content2.php";
		Log.e("urlPath COntent2======", url);
		a = new AQuery(getApplicationContext());
		a.progress(dialog).ajax(url, JSONObject.class, this,
				"jsonCallbackCat_2");
	}

	public void jsonCallbackCat_2(String url, JSONObject json, AjaxStatus status) { // /
																					// hello
																					// tunes

		// ll13 = (LinearLayout) findViewById(R.id.ll_TopMusicOne);
		// ll14 = (LinearLayout) findViewById(R.id.ll_TopMusicTwo);
		// ll15 = (LinearLayout) findViewById(R.id.ll_TopMusicThree);
		// ll16 = (LinearLayout) findViewById(R.id.ll_TopMusicFour);
		// ll17 = (LinearLayout) findViewById(R.id.ll_TopMusicFive);
		// ll18 = (LinearLayout) findViewById(R.id.ll_TopMusicSix);
		//
		// txt_category3 = (TextView)
		// findViewById(R.id.textView_TopMusicHeading);
		// txt_category31 = (TextView)
		// findViewById(R.id.textView_TopMusicOneName);
		// txt_category32 = (TextView)
		// findViewById(R.id.textView_TopMusicTwoName);
		// txt_category33 = (TextView)
		// findViewById(R.id.textView_TopMusicThreeName);
		// txt_category34 = (TextView)
		// findViewById(R.id.textView_TopMusicFourName);
		// txt_category35 = (TextView)
		// findViewById(R.id.textView_TopMusicFiveName);
		// txt_category36 = (TextView)
		// findViewById(R.id.textView_TopMusicSixName);
		// image31 = (ImageView) findViewById(R.id.imageView_TopMusicOneImage);
		// image32 = (ImageView) findViewById(R.id.imageView_TopMusicTwoImage);
		// image33 = (ImageView)
		// findViewById(R.id.imageView_TopMusicThreeImage);
		// image34 = (ImageView) findViewById(R.id.imageView_TopMusicFourImage);
		// image35 = (ImageView) findViewById(R.id.imageView_TopMusicFiveImage);
		// image36 = (ImageView) findViewById(R.id.imageView_TopMusicSixImage);

		System.out.println("@@@@@@@" + json);

		try {
			JSONArray jarray = json.getJSONArray("TopContent2");// /// Hello
																// Tunes
			final JSONObject item = jarray.getJSONObject(0);
			// txt_category3.setText("  " + item.getString("category"));
			//
			// txt_category31.setText(item.getString("name"));
			// a.id(image31)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item.getString("icon").toString());

			link21 = item.getString("url");
			packageName18 = item.getString("pack");

			// ll13.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			//
			// if (link21.equalsIgnoreCase("")) {
			//
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName18);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			// topContentOne = txt_category31.getText().toString();
			// // System.out.println("topvalue ="+topContentOne);
			// new MyAsyncTask().execute();
			// if (link21.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link21)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link21);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item1 = jarray.getJSONObject(1);
			// txt_category32.setText(item1.getString("name"));
			// a.id(image32)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item1.getString("icon").toString());
			link22 = item1.getString("url");
			packageName19 = item1.getString("pack");
			// ll14.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item1.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link22.equalsIgnoreCase("")) {
			//
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName19);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category32.getText().toString();
			// // System.out.println("topvalue ="+topContentOne);
			// new MyAsyncTask().execute();
			// if (link22.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link22)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link22);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// }
			// }
			// });
			final JSONObject item2 = jarray.getJSONObject(2);
			// txt_category33.setText(item2.getString("name"));
			// a.id(image33)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item2.getString("icon").toString());
			link23 = item2.getString("url");
			packageName20 = item2.getString("pack");
			// ll15.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item2.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link23.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName20);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			// topContentOne = txt_category33.getText().toString();
			// // System.out.println("topvalue ="+topContentOne);
			// new MyAsyncTask().execute();
			// if (link23.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link23)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link23);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// }
			// }
			// });

			final JSONObject item3 = jarray.getJSONObject(3);
			// txt_category34.setText(item3.getString("name"));
			// a.id(image34)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item3.getString("icon").toString());
			link24 = item3.getString("url");
			packageName21 = item3.getString("pack");
			// ll16.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item3.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link24.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName21);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// } else {
			//
			// topContentOne = txt_category34.getText().toString();
			// // System.out.println("topvalue ="+topContentOne);
			// new MyAsyncTask().execute();
			// if (link24.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link24)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link24);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });

			final JSONObject item4 = jarray.getJSONObject(4);
			// txt_category35.setText(item4.getString("name"));
			// a.id(image35)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item4.getString("icon").toString());
			link25 = item4.getString("url");
			packageName22 = item4.getString("pack");
			// ll17.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item4.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link25.equalsIgnoreCase("")) {
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName22);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category35.getText().toString();
			// // System.out.println("topvalue ="+topContentOne);
			// new MyAsyncTask().execute();
			// if (link25.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link25)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link25);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });
			final JSONObject item5 = jarray.getJSONObject(5);
			// txt_category36.setText(item5.getString("name"));
			// a.id(image36)
			// .progress(dialog)
			// .image(Constant.serverPathImage
			// + item5.getString("icon").toString());
			link26 = item5.getString("url");
			packageName23 = item5.getString("pack");
			// ll18.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// new ClickCountAsyncTask(item5.optString("id"),
			// "topClick.php").execute();
			// boolean check = isNetworkAvailable();
			// if (check != true) {
			// Intent intent = new Intent(MainActivity.this,
			// NoInternetConnection.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// // finish();
			// return;
			// }
			// if (link26.equalsIgnoreCase("")) {
			//
			// Intent in = new Intent(MainActivity.this,
			// AppDetail.class);
			// in.putExtra("pack", packageName23);
			// startActivity(in);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// } else {
			// topContentOne = txt_category36.getText().toString();
			// // System.out.println("topvalue ="+topContentOne);
			// new MyAsyncTask().execute();
			// if (link26.contains("market")) {
			// Intent i = new Intent(Intent.ACTION_VIEW);
			// // final String
			// // appPackageName="com.yasoftocean.fashion.fashionist&hl=en";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri
			// .parse(link26)));
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			// return;
			// }
			// Intent intent = new Intent(MainActivity.this,
			// MyWebView.class);
			// intent.putExtra("webLink", link26);
			// startActivity(intent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_right);
			//
			// }
			// }
			// });
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_LOADING:
			final Dialog dialog = new Dialog(this, R.style.NewDialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// here we set layout of progress dialog
			dialog.setContentView(R.layout.progress_bar);

			return dialog;

		default:
			return null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode == 1 means the result for package-installer activity
		if (requestCode == 10) {
			// resultCode == RESULT_CANCELED means user pressed `Done` button
			if (resultCode == RESULT_CANCELED) {
				downloadAppButton.setText("Open");
				openApplication();
				// Toast.makeText(this, "User pressed 'Done' button",
				// Toast.LENGTH_SHORT);
				System.out.println("user pressed open button =" + apkName
						+ ".apk");
				File file = new File("/sdcard/" + apkName + ".apk");
				boolean deleted = file.delete();
				this.topContentOne = apkName;

				if (isConnected()) {
					new MyAsyncTaskForDownload().execute();
					// downloadAppButton.setBackgroundColor(Color.parseColor("#7cAA2d"));
					// checkAppInstallOrNot(freeAppPackageName);

				} else {
					Toast.makeText(
							getApplicationContext(),
							getResources()
									.getString(
											R.string.title_activity_no_internet_connection),
							Toast.LENGTH_SHORT).show();
				}

			}

		}

	}

	public class MyAsyncTaskForDownload extends AsyncTask<Void, Void, Void> {
		public void postData() {

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constant.serverPath + "/Download");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair("name", "FlipKart"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

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
	}

	class TheTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				appIcon = downloadBitmap(Constant.serverPath + "/uploads/"
						+ appInfo.getAppIconUrl().toString() + "");

				WrtualBean appScreens, screen = null;
				String appScreenOne, appScreenTwo, appScreenThree;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// pd.dismiss();
			if (appIcon != null) {
				freeAppIcon.setImageBitmap(appIcon);
				// getImageList();

			}

		}
	}

	private Bitmap downloadBitmap(String url) {
		// initilize the default HTTP client object
		final DefaultHttpClient client = new DefaultHttpClient();

		// forming a HttoGet request
		final HttpGet getRequest = new HttpGet(url);
		try {

			HttpResponse response = client.execute(getRequest);

			// check 200 OK for success
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;

			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					// getting contents from the stream
					inputStream = entity.getContent();

					// decoding stream data back into image Bitmap that android
					// understands
					appIcon = BitmapFactory.decodeStream(inputStream);

				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// You Could provide a more explicit error message for IOException
			getRequest.abort();
			Log.e("ImageDownloader", "Something went wrong while"
					+ " retrieving bitmap from " + url + e.toString());
		}

		return appIcon;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	public class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {

		private int currentPage;

		@Override
		public void onPageSelected(int position) {

			currentPage = position;
		}

		public int getCurrentPage() {
			return currentPage;
		}
	}

	public void onBackPressed() {

		// exitDialog(11);

		if (doubleBackToExitPressedOnce) {
			// super.onBackPressed();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return;
		}

		doubleBackToExitPressedOnce = true;
		Toast.makeText(this,
				getResources().getString(R.string.Pressagaintoexit),
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);

		/*
		 * new
		 * AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert
		 * ).setTitle("Exit") .setMessage("Are you sure?")
		 * .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * MainActivity.super.onBackPressed(); Intent intent = new
		 * Intent(Intent.ACTION_MAIN); intent.addCategory(Intent.CATEGORY_HOME);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * startActivity(intent); finish(); } }).setNegativeButton("No",
		 * null).show();
		 */
	}

	private void exitDialog(int i) {

		if (i == 11) {
			final Dialog dialog = new Dialog(MainActivity.this);

			dialog.setTitle(" Feed back");
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.exit_dialog);
			dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			dialog.show();

			Button exitYes = (Button) dialog.findViewById(R.id.button_exitYes);
			Button exitNo = (Button) dialog.findViewById(R.id.button_exitNo);
			exitYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					MainActivity.super.onBackPressed();
					/*
					 * Intent intent = new Intent(Intent.ACTION_MAIN);
					 * intent.addCategory(Intent.CATEGORY_HOME);
					 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					 * startActivity(intent);
					 */finish();
					dialog.dismiss();
				}
			});
			exitNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

		}
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

		try {
			if (actionId == EditorInfo.IME_ACTION_GO) {
				System.out.println("yes");
				String activity = "MainActivity";
				String url = "https://www.google.co.in/?gfe_rd=ctrl&ei=4j4DU_KsCOaJ8QfyhYHYCQ&gws_rd=cr#q="
						+ editTextSearch.getText().toString();
				Intent intent = new Intent(MainActivity.this,
						BrowserActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("activity", activity);
				startActivity(intent);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	private void setRecurringAlarmTime(Context baseContext) {
		// get a Calendar object with current time
		Calendar cal = Calendar.getInstance();
		// add 5 minutes to the calendar object
		cal.add(Calendar.SECOND, START_DELAY);

		Intent intent = new Intent(context, AlarmReceiverTime.class);

		tracking = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// PendingIntent pendingToRun = PendingIntent.getBroadcast(this, 0,
		// toRun, 0);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(), 25000, tracking);

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	class SearchReportAsyncTask extends AsyncTask<Void, Void, Void> {
		String responce = "";
		String imei = "";

		public void postData() {
			// Toast.makeText(getApplicationContext(), ""+email+" "+feedback,
			// Toast.LENGTH_SHORT).show();

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost httppost = new
			// HttpPost(Constant.serverPath+"/feedback.php");
			HttpPost httppost = new HttpPost(Constant.serverPath
					+ "/SearchReport.php");
			// a. IMEI
			// b. GID
			// c. AID
			// d. Country
			// e. Handset

			try {
				// Add your data

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);

				nameValuePairs.add(new BasicNameValuePair("AID", HashGenerator
						.encrypt(androidID, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Search",
						HashGenerator.encrypt("" + SearchString,
								Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Lat", HashGenerator
						.encrypt(lat, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Lang", HashGenerator
						.encrypt(lng, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("appId",
						Constant.AppID));
				nameValuePairs.add(new BasicNameValuePair("BoxerVersion",
						Constant.App_Version));
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

			Log.e("Search Responce ", "Responce " + responce);

		}
	}

	// //////////////////////////////// Click Conut
	// AsctncTasks////////////////////////////////////
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
			Log.e("Click Responce of " + scriptName, "Responce " + responce);

		}
	}

	// /////////////////////////////===================88888888888888888888-------------------------------------
	void setUpPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new TabsPagerAdapter(this));
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mViewPager);
	}

	void setUpTabColor() {
		mSlidingTabLayout
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
					@Override
					public int getIndicatorColor(int position) {
						// TODO Auto-generated method stub
						return MainActivity.this.getResources().getColor(
								R.color.red);
					}

					@Override
					public int getDividerColor(int position) {
						// TODO Auto-generated method stub
						return MainActivity.this.getResources().getColor(
								R.color.trans);
					}
				});
	}

	// //// To outside get outside click
	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hideSoftKeyboard(MainActivity.this);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	public String getHomepage() {

		String home;
		home = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta content=\"en-us\" http-equiv=\"Content-Language\" /><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0\"><title>"
				+ "Homepage"
				+ "</title></head>"
				+ "<style>body{background:#f2f2f2;text-align:center;}#search_input{height:35px; width:100%;outline:none;border:none;font-size: 16px;background-color:transparent;}span { display: block; overflow: hidden; padding-left:5px;vertical-align:middle;}.search_bar{display:table;vertical-align:middle;width:90%;height:35px;max-width:500px;margin:0 auto;background-color:#fff;box-shadow: 0px 3px rgba( 0, 0, 0, 0.1 );font-family: Arial;color: #444;-moz-border-radius: 2px;-webkit-border-radius: 2px;border-radius: 2px;}#search_submit{outline:none;height:37px;float:right;color:#404040;font-size:16px;font-weight:bold;border:none;background-color:transparent;}div.center{vertical-align:middle;height:100%;width:100%;max-height:300px; position: absolute; top:0; bottom: 0; left: 0; right: 0; margin: auto;}img.smaller{width:50%;max-width:300px;}.box { vertical-align:middle;position:relative; display: block; margin: 10px;padding-left:10px;padding-right:10px;padding-top:5px;padding-bottom:5px; background-color:#fff;box-shadow: 0px 3px rgba( 0, 0, 0, 0.1 );font-family: Arial;color: #444;font-size: 12px;-moz-border-radius: 2px;-webkit-border-radius: 2px;border-radius: 2px;}</style><body>"
				+ "<div class=\"center\"><img class=\"smaller\" src=\"";
		SharedPreferences sharedPreferences1 = PreferenceManager
				.getDefaultSharedPreferences(this);
		String engine = sharedPreferences1.getString("defaultengine",
				Constant.defaultEngine);
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
			home = home
					+ mPreferences.getString(PreferenceConstants.SEARCH_URL,
							B_Constants.GOOGLE_SEARCH);
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

		File homepage = new File(getCacheDir(), "homepage.html");
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
