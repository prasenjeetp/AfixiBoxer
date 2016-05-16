package com.boxer.browser;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.BreakIterator;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.MailTo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boxer.util.Constant;

public class LightningView implements Serializable {

	private transient Title mTitle;

	private transient WebView mWebView;

	private transient BrowserController mBrowserController;

	private transient GestureDetector mGestureDetector;

	private transient Activity mActivity;

	private transient WebSettings mSettings;

	private static transient int API = android.os.Build.VERSION.SDK_INT;

	private static transient String mHomepage;

	private static transient String mDefaultUserAgent;

	private static transient Bitmap mWebpageBitmap;

	private static transient SharedPreferences mPreferences;

	private static transient boolean mWideViewPort;

	private transient AdBlock mAdBlock;

	private transient boolean isForegroundTab;

	private transient IntentUtils mIntentUtils;
	transient Context context;
	transient ProgressDialog pd;
	boolean isListLoadingFromDB;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public LightningView(final Activity activity, String url, boolean isListLoading) {
		this.isListLoadingFromDB = isListLoading;
		int count = 1;
		mActivity = activity;
		mWebView = new WebView(activity);

		mWebView.getSettings().setUserAgentString(
				mWebView.getSettings().getUserAgentString() + " Boxer Browser/" + Constant.App_Version);
		String userAgent = mWebView.getSettings().getUserAgentString();
		Log.e("user agent", "not null " + userAgent);
		mTitle = new Title(activity);
		mAdBlock = new AdBlock(activity);
		activity.getPackageName();
		pd = new ProgressDialog(mActivity);
		mWebpageBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_webpage);

		try {
			mBrowserController = (BrowserController) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity + " must implement BrowserController");
		}
		mIntentUtils = new IntentUtils(mBrowserController);
		mWebView.setDrawingCacheBackgroundColor(0x00000000);
		mWebView.setFocusableInTouchMode(true);
		mWebView.setFocusable(true);
		mWebView.setAnimationCacheEnabled(false);
		mWebView.setDrawingCacheEnabled(true);
		mWebView.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
		if (API > 15) {
			mWebView.getRootView().setBackground(null);
		} else {
			mWebView.getRootView().setBackgroundDrawable(null);
		}
		mWebView.setWillNotCacheDrawing(false);
		mWebView.setAlwaysDrawnWithCacheEnabled(true);
		mWebView.setScrollbarFadingEnabled(true);
		mWebView.setSaveEnabled(true);
		// mWebView.loadUrl(url);
		mWebView.setWebChromeClient(new LightningChromeClient(activity));
		
		mWebView.setWebViewClient(new LightningWebClient(activity));
		mWebView.setDownloadListener(new LightningDownloadListener(activity));
		mGestureDetector = new GestureDetector(activity, new CustomGestureListener());
		mWebView.setOnTouchListener(new OnTouchListener() {

			float mLocation;

			float mY;

			int mAction;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View view, MotionEvent arg1) {
				if (view != null && !view.hasFocus()) {
					view.requestFocus();
				}
				mAction = arg1.getAction();
				mY = arg1.getY();
				if (mAction == MotionEvent.ACTION_DOWN) {
					mLocation = mY;
				} else if (mAction == MotionEvent.ACTION_UP) {
					if ((mY - mLocation) > 10) {
						mBrowserController.showActionBar();
					} else if ((mY - mLocation) < -10) {
						mBrowserController.hideActionBar();
					}
					mLocation = 0;
				}
				mGestureDetector.onTouchEvent(arg1);
				return false;
			}

		});
		mDefaultUserAgent = mWebView.getSettings().getUserAgentString();
		mSettings = mWebView.getSettings();
		initializeSettings(mWebView.getSettings(), activity);
		initializePreferences(activity);

		System.out.println(" url=" + url);
		System.out.println(" main url=" + MainActivity.mainUrl);

		if (url != null) {
			if (!url.trim().isEmpty()) {
				if (isListLoadingFromDB) {
					System.out.println("List is loading" + MainActivity.mainUrl);
					if (url.contains("market://") || url.contains("gammo") || url.contains("binjinn")) {
						
						Log.i("MANTU", "url1="+url);
						
					} else {
						mWebView.loadUrl(url);
						Log.i("MANTU", "url2="+url);
					}

				} else if (url.contains("market://")) {
					Log.i("MANTU", "url3="+url);
					mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

				} else {
					Log.i("MANTU", "url4="+url);
					mWebView.loadUrl(url);
				}

			} else {
				// don't load anything, the user is looking for a blank tab
				Log.i("MANTU", "url5="+url);
			}
		} else {
			Log.i("MANTU", "url6="+url);
			if (url == null || url == "") {
				Log.i("MANTU", "url7="+url);
				mWebView.loadUrl(getHomepage());
			}
			if (mHomepage.startsWith("about:home")) {
				Log.i("MANTU", "url8="+url);
				mSettings.setUseWideViewPort(false);

				if (MainActivity.mainUrl != null) {
					Log.i("MANTU", "url9="+url);
					mWebView.loadUrl(MainActivity.mainUrl);
				} else {
					Log.i("MANTU", "url10="+url);
					mWebView.loadUrl(getHomepage());
				}

				MainActivity.mainUrl = null;

			} else if (mHomepage.startsWith("about:bookmarks")) {
				Log.i("MANTU", "url11="+url);
				mBrowserController.openBookmarkPage(mWebView);
			} else {
				Log.i("MANTU", "url12="+url);
				mWebView.loadUrl(mHomepage);
			}
		}
		mWebView.setWebViewClient(new WebViewClient() {
			private boolean isRedirected;
			int counter = 0;
			int pageCounter = 0;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				pageCounter++;
				if (!isRedirected) {
					// Do something you want when starts loading
				}
				Log.e("On page strated ", "Lighting view");
				Log.e("On page strated  url", "" + url);
				isRedirected = false;
				// if (mActivity instanceof B_BrowserActivity) {
				// if (mActivity instanceof B_BrowserActivity) {
				// if (isListLoadingFromDB) {
				// Log.e("List is Loading ", "" + isListLoadingFromDB
				// + " url" + url);
				// pageCounter = 3;
				//
				// isListLoadingFromDB = false;
				//
				// } else if (pageCounter == 1) {
				// ((B_BrowserActivity) mActivity).onPageStrated(view,
				// url, "ADD");
				// } else if (pageCounter >= 2) {
				//
				// ((B_BrowserActivity) mActivity).onPageStrated(view,
				// url, "UPDATE");
				// }
				//
				// }
				// Log.e("Tag Count onPageStrated ", pageCounter + "");
				// // do your stuff here
				// Log.e("Lighting view ", "onPageStrated " + url);
				// }
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				
				// TODO Auto-generated method stub
				if (mActivity instanceof B_BrowserActivity) {
					((B_BrowserActivity) mActivity).overrideUrl(url);
				}

				isRedirected = true;
				Log.e("links ", "" + url);
				if (url.contains("rtsp://")) {
					// mWebView.loadUrl(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					mActivity.startActivity(intent);

				} else if (url.contains("market://")) {
					view.stopLoading();
					mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					return true;
				} else
					return super.shouldOverrideUrlLoading(view, url);
				return true;

			}

			public void onPageFinished(WebView view, String url) {
				
				Log.e("BIBHU", "onPageFinished actual class =======================================");
				if (view.getTitle() == null) {
					mTitle.setTitle(mActivity.getString(R.string.untitled));
				} else if (!view.getTitle().isEmpty()) {
					mTitle.setTitle(view.getTitle());
				} else {
					mTitle.setTitle(mActivity.getString(R.string.untitled));
				}
				
				counter++;
				// Do something you want when finished loading

				if (mActivity instanceof B_BrowserActivity) {
					if (isListLoadingFromDB) {
						Log.e("List is Loading ", "" + isListLoadingFromDB + "   url" + url);
						counter = 3;

						isListLoadingFromDB = false;

					} else if (counter == 1) {
						((B_BrowserActivity) mActivity).onPageFinished(view, url, "ADD");

					//	Log.e("Lighting view ", "onPageFinished ADD =======================================");

					} else if (counter >= 2) {

						((B_BrowserActivity) mActivity).onPageFinished(view, url, "UPDATE");
					//	Log.e("Lighting view ", "onPageFinished UPDATE =======================================");
					}

				}
				Log.e("Tag Count ", counter + "");
				
			}
		});

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

	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	public synchronized void initializePreferences(Context context) {
		mPreferences = context.getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
		mHomepage = mPreferences.getString(PreferenceConstants.HOMEPAGE, B_Constants.HOMEPAGE);
		mAdBlock.updatePreference();
		if (mSettings == null && mWebView != null) {
			mSettings = mWebView.getSettings();
		} else if (mSettings == null) {
			return;
		}
		mSettings.setGeolocationEnabled(mPreferences.getBoolean(PreferenceConstants.LOCATION, false));
		if (API < 19) {
			switch (mPreferences.getInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0)) {
			case 0:
				mSettings.setPluginState(PluginState.OFF);
				break;
			case 1:
				mSettings.setPluginState(PluginState.ON_DEMAND);
				break;
			case 2:
				mSettings.setPluginState(PluginState.ON);
				break;
			default:
				break;
			}
		}

		switch (mPreferences.getInt(PreferenceConstants.USER_AGENT, 1)) {
		case 1:
			if (API > 16) {
				mSettings.setUserAgentString(mDefaultUserAgent);
			} else {
				mSettings.setUserAgentString(mDefaultUserAgent);
			}
			break;
		case 2:
			mSettings.setUserAgentString(mDefaultUserAgent);
			break;
		case 3:
			mSettings.setUserAgentString(mDefaultUserAgent);
			break;
		case 4:
			mSettings.setUserAgentString(mDefaultUserAgent);
			break;
		}

		if (mPreferences.getBoolean(PreferenceConstants.SAVE_PASSWORDS, false)) {
			if (API < 18) {
				mSettings.setSavePassword(true);
			}
			mSettings.setSaveFormData(true);
		}

		if (mPreferences.getBoolean(PreferenceConstants.JAVASCRIPT, true)) {
			mSettings.setJavaScriptEnabled(true);
			mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		}

		if (mPreferences.getBoolean(PreferenceConstants.TEXT_REFLOW, false)) {
			mSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		} else if (API >= android.os.Build.VERSION_CODES.CUPCAKE) {
			mSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		} else {
			mSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		}

		mSettings.setBlockNetworkImage(mPreferences.getBoolean(PreferenceConstants.BLOCK_IMAGES, false));
		mSettings.setSupportMultipleWindows(mPreferences.getBoolean(PreferenceConstants.POPUPS, true));
		mSettings.setUseWideViewPort(mPreferences.getBoolean(PreferenceConstants.USE_WIDE_VIEWPORT, true));
		mWideViewPort = mPreferences.getBoolean(PreferenceConstants.USE_WIDE_VIEWPORT, true);
		mSettings.setLoadWithOverviewMode(mPreferences.getBoolean(PreferenceConstants.OVERVIEW_MODE, true));
		switch (mPreferences.getInt(PreferenceConstants.TEXT_SIZE, 3)) {
		case 1:
			mSettings.setTextZoom(200);
			break;
		case 2:
			mSettings.setTextZoom(150);
			break;
		case 3:
			mSettings.setTextZoom(100);
			break;
		case 4:
			mSettings.setTextZoom(75);
			break;
		case 5:
			mSettings.setTextZoom(50);
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	public void initializeSettings(WebSettings settings, Context context) {
		if (API < 18) {
			settings.setAppCacheMaxSize(Long.MAX_VALUE);
		}
		if (API < 17) {
			settings.setEnableSmoothTransition(true);
		}
		if (API > 16) {
			settings.setMediaPlaybackRequiresUserGesture(true);
		}
		if (API < 19) {
			settings.setDatabasePath(context.getFilesDir().getAbsolutePath() + "/databases");
		}
		settings.setDomStorageEnabled(true);

		settings.setAppCachePath(context.getCacheDir().toString());
		settings.setAppCacheEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		settings.setGeolocationDatabasePath(context.getCacheDir().getAbsolutePath());
		settings.setAllowFileAccess(true);
		settings.setDatabaseEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		settings.setAllowContentAccess(true);
		settings.setDefaultTextEncodingName("utf-8");
		if (API > 16) {
			settings.setAllowFileAccessFromFileURLs(false);
			settings.setAllowUniversalAccessFromFileURLs(false);
		}
	}

	public boolean isShown() {
		return mWebView != null && mWebView.isShown();
	}

	public synchronized void onPause() {
		if (mWebView != null) {
			mWebView.onPause();
		}
	}

	public synchronized void onResume() {
		if (mWebView != null) {
			mWebView.onResume();
		}
	}

	public void setForegroundTab(boolean isForeground) {
		isForegroundTab = isForeground;
		mBrowserController.update();
	}

	public boolean isForegroundTab() {
		return isForegroundTab;
	}

	public int getProgress() {
		if (mWebView != null) {
			return mWebView.getProgress();
		} else {
			return 100;
		}
	}

	public synchronized void stopLoading() {
		if (mWebView != null) {
			mWebView.stopLoading();
		}
	}

	public synchronized void pauseTimers() {
		if (mWebView != null) {
			mWebView.pauseTimers();
		}
	}

	public synchronized void resumeTimers() {
		if (mWebView != null) {
			mWebView.resumeTimers();
		}
	}

	public void requestFocus() {
		if (mWebView != null && !mWebView.hasFocus()) {
			mWebView.requestFocus();
		}
	}

	public void setVisibility(int visible) {
		if (mWebView != null) {
			mWebView.setVisibility(visible);
		}
	}

	public void clearCache(boolean disk) {
		if (mWebView != null) {
			mWebView.clearCache(disk);
		}
	}

	public synchronized void reload() {
		if (mWebView != null) {
			mWebView.reload();
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public synchronized void find(String text) {
		if (mWebView != null) {
			if (API > 16) {
				mWebView.findAllAsync(text);
			} else {
				mWebView.findAll(text);
			}
		}
	}

	public Activity getActivity() {
		return mActivity;
	}

	public synchronized void onDestroy() {
		if (mWebView != null) {
			mWebView.stopLoading();
			mWebView.onPause();
			mWebView.clearHistory();
			mWebView.setVisibility(View.GONE);
			mWebView.removeAllViews();
			mWebView.destroyDrawingCache();
			// mWebView.destroy(); //this is causing the segfault
			mWebView = null;
		}
	}

	public synchronized void goBack() {
		if (mWebView != null) {
			Log.v("BIBHU", "===============mWebView.goBack(); called==============");
			mWebView.goBack();
		}
	}

	public String getUserAgent() {
		if (mWebView != null) {
			return mWebView.getSettings().getUserAgentString();
		} else {
			return "";
		}
	}

	public synchronized void goForward() {
		if (mWebView != null) {
			mWebView.goForward();
		}
	}

	public boolean canGoBack() {
		return mWebView != null && mWebView.canGoBack();
	}

	public boolean canGoForward() {
		return mWebView != null && mWebView.canGoForward();
	}

	public WebView getWebView() {
		return mWebView;
	}

	public Bitmap getFavicon() {
		return mTitle.getFavicon();
	}

	public synchronized void loadUrl(String url) {
		if (mWebView != null) {
			mWebView.loadUrl(url);
		}
	}

	public synchronized void invalidate() {
		if (mWebView != null) {
			mWebView.invalidate();
		}
	}

	public String getTitle() {
		return mTitle.getTitle();
	}

	public String getUrl() {
		if (mWebView != null) {
			return mWebView.getUrl();
		} else {
			return "";
		}
	}

	public class LightningWebClient extends WebViewClient {

		Context mActivity;

		LightningWebClient(Context context) {
			mActivity = context;

		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			
			Log.e("BIBHU", "onPageFinished other class 2=======================================");
			
			if (mAdBlock.isAd(url)) {
				ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
				return new WebResourceResponse("text/plain", "utf-8", EMPTY);
			}

			boolean useProxy = mPreferences.getBoolean(PreferenceConstants.USE_PROXY, false);
			boolean mDoLeakHardening = false;

			if (!useProxy) {
				return null;
			}

			if (!mDoLeakHardening) {
				return null;
			}

			// now we are going to proxy!
			try {

				URL uURl = new URL(url);

				Proxy proxy = null;

				String host = mPreferences.getString(PreferenceConstants.USE_PROXY_HOST, "localhost");
				int port = mPreferences.getInt(PreferenceConstants.USE_PROXY_PORT, 8118);
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));

				HttpURLConnection.setFollowRedirects(true);
				HttpURLConnection conn = (HttpURLConnection) uURl.openConnection(proxy);
				conn.setInstanceFollowRedirects(true);
				conn.setRequestProperty("User-Agent", mSettings.getUserAgentString());

				// conn.setRequestProperty("Transfer-Encoding", "chunked");
				// conn.setUseCaches(false);

				final int bufferSize = 1024 * 32;
				conn.setChunkedStreamingMode(bufferSize);

				String cType = conn.getContentType();
				String cEnc = conn.getContentEncoding();
				int connLen = conn.getContentLength();

				if (cType != null) {
					String[] ctArray = cType.split(";");
					cType = ctArray[0].trim();

					if (cEnc == null && ctArray.length > 1) {
						cEnc = ctArray[1];
						if (cEnc.indexOf('=') != -1) {
							cEnc = cEnc.split("=")[1].trim();
						}
					}
				}

				if (connLen <= 0) {
					connLen = 2048;
				}

				if (cType != null && cType.startsWith("text")) {
					InputStream fStream;

					BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
					ByteArrayBuffer baf = new ByteArrayBuffer(connLen);
					int read;
					int bufSize = 2048;
					byte[] buffer = new byte[bufSize];
					while (true) {
						read = bis.read(buffer);
						if (read == -1) {
							break;
						}
						baf.append(buffer, 0, read);
					}
					byte[] plainText = baf.toByteArray();

					fStream = new ByteArrayInputStream(plainText);

					fStream = new ReplacingInputStream(new ByteArrayInputStream(plainText), "poster=".getBytes(),
							"foo=".getBytes());
					fStream = new ReplacingInputStream(fStream, "Poster=".getBytes(), "foo=".getBytes());
					fStream = new ReplacingInputStream(fStream, "Poster=".getBytes(), "foo=".getBytes());
					fStream = new ReplacingInputStream(fStream, ".poster".getBytes(), ".foo".getBytes());
					fStream = new ReplacingInputStream(fStream, "\"poster\"".getBytes(), "\"foo\"".getBytes());

					return new WebResourceResponse(cType, cEnc, fStream);
				} /**
					 * else if (mDoLeakHardening) { WebResourceResponse response
					 * = new WebResourceResponse( cType, cEnc,
					 * conn.getInputStream());
					 * 
					 * return response;
					 * 
					 * }
					 */
				else {
					return null; // let webkit handle it
				}
			} catch (Exception e) {
				Log.e(B_Constants.TAG, "Error filtering stream", e);
				ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());
				return new WebResourceResponse("text/plain", "utf-8", EMPTY);
			}
		}

		
		@Override
		public void onPageFinished(WebView view, String url) {
			
			
			Log.e("BIBHU", "onPageFinished other class 1=======================================");
			
			if (pd != null) {
				if (pd.isShowing()) {
					pd.dismiss();
				}

			}
			if (view.isShown()) {
				view.invalidate();
			}
			if (view.getTitle() == null) {
				mTitle.setTitle(mActivity.getString(R.string.untitled));
			} else if (!view.getTitle().isEmpty()) {
				mTitle.setTitle(view.getTitle());
			} else {
				mTitle.setTitle(mActivity.getString(R.string.untitled));
			}
			mBrowserController.update();
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.e("BIBHU", "onPageFinished other class 3=======================================");
			
			pd = new ProgressDialog(mActivity);
			if (pd != null) {
				pd.setMessage("Loading...");
				// pd.show();

			}
			if (!mSettings.getUseWideViewPort()) {
				mSettings.setUseWideViewPort(mWideViewPort);
			}
			if (isShown()) {
				mBrowserController.updateUrl(url);
				mBrowserController.showActionBar();
			}
			mTitle.setFavicon(mWebpageBitmap);
			mBrowserController.update();
		}

		@Override
		public void onReceivedHttpAuthRequest(final WebView view, final HttpAuthHandler handler, final String host,
				final String realm) {
			Log.i("BIBHU","This function is called");	

			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			final EditText name = new EditText(mActivity);
			final EditText password = new EditText(mActivity);
			LinearLayout passLayout = new LinearLayout(mActivity);
			passLayout.setOrientation(LinearLayout.VERTICAL);

			passLayout.addView(name);
			passLayout.addView(password);

			name.setHint(mActivity.getString(R.string.hint_username));
			password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			password.setTransformationMethod(new PasswordTransformationMethod());
			password.setHint(mActivity.getString(R.string.hint_password));
			builder.setTitle(mActivity.getString(R.string.title_sign_in));
			builder.setView(passLayout);
			builder.setCancelable(true).setPositiveButton(mActivity.getString(R.string.title_sign_in),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							String user = name.getText().toString();
							String pass = password.getText().toString();
							handler.proceed(user.trim(), pass.trim());
							Log.i(B_Constants.TAG, "Request Login");

						}
					}).setNegativeButton(mActivity.getString(R.string.action_cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									handler.cancel();

								}
							});
			AlertDialog alert = builder.create();
			alert.show();

		}

		@Override
		public void onScaleChanged(WebView view, float oldScale, float newScale) {
			if (view.isShown()) {
				view.invalidate();
			}
		}

		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(mActivity.getString(R.string.title_warning));
			builder.setMessage(mActivity.getString(R.string.message_untrusted_certificate)).setCancelable(true)
					.setPositiveButton(mActivity.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							handler.proceed();
						}
					})
					.setNegativeButton(mActivity.getString(R.string.action_no), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							handler.cancel();
						}
					});
			AlertDialog alert = builder.create();
			if (error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
				alert.show();
			} else {
				handler.proceed();
			}

		}

		@Override
		public void onFormResubmission(WebView view, final Message dontResend, final Message resend) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(mActivity.getString(R.string.title_form_resubmission));
			builder.setMessage(mActivity.getString(R.string.message_form_resubmission)).setCancelable(true)
					.setPositiveButton(mActivity.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							resend.sendToTarget();
						}
					})
					.setNegativeButton(mActivity.getString(R.string.action_no), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {

							dontResend.sendToTarget();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}

		// changed return type on intent null ;;;;
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			Log.e("shouldOverrideUrlLoading", url);
			if (url.startsWith("about:")) {
				return super.shouldOverrideUrlLoading(view, url);
			}
			if (url.contains("mailto:")) {
				MailTo mailTo = MailTo.parse(url);
				Intent i = B_Utils.newEmailIntent(mActivity, mailTo.getTo(), mailTo.getSubject(), mailTo.getBody(),
						mailTo.getCc());
				mActivity.startActivity(i);
				view.reload();
				return true;
			} else if (url.startsWith("intent://")) {
				Intent intent = null;
				try {
					intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
				} catch (URISyntaxException ex) {
					return false;
				}
				if (intent != null) {
					try {
						mActivity.startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Log.e(B_Constants.TAG, "ActivityNotFoundException");
					}
					return false;
				}
			} else if (url.startsWith("http://")) {
				return false;
			} else if (url.startsWith("https://")) {
				return false;
			}
			return mIntentUtils.startActivityForUrl(mWebView, url);
		}
	}

	public class LightningChromeClient extends WebChromeClient {

		Context mActivity;

		LightningChromeClient(Context context) {
			mActivity = context;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (isShown()) {
				mBrowserController.updateProgress(newProgress);
			}
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			mTitle.setFavicon(icon);
			mBrowserController.update();
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			if (!title.isEmpty()) {
				mTitle.setTitle(title);
			} else {
				mTitle.setTitle(mActivity.getString(R.string.untitled));
			}
			mBrowserController.update();
			mBrowserController.updateHistory(title, view.getUrl());
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(final String origin,
				final GeolocationPermissions.Callback callback) {
			final boolean remember = true;
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(mActivity.getString(R.string.location));
			String org = null;
			if (origin.length() > 50) {
				org = origin.subSequence(0, 50) + "...";
			} else {
				org = origin;
			}
			builder.setMessage(org + mActivity.getString(R.string.message_location)).setCancelable(true)
					.setPositiveButton(mActivity.getString(R.string.action_allow),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									callback.invoke(origin, true, remember);
								}
							})
					.setNegativeButton(mActivity.getString(R.string.action_dont_allow),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									callback.invoke(origin, false, remember);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

		}

		@Override
		public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
			mBrowserController.onCreateWindow(isUserGesture, resultMsg);
			return true;
		}

		@Override
		public void onCloseWindow(WebView window) {
			// TODO Auto-generated method stub
			super.onCloseWindow(window);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mBrowserController.openFileChooser(uploadMsg);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			mBrowserController.openFileChooser(uploadMsg);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mBrowserController.openFileChooser(uploadMsg);
		}

		@Override
		public Bitmap getDefaultVideoPoster() {
			return mBrowserController.getDefaultVideoPoster();
		}

		@Override
		public View getVideoLoadingProgressView() {
			return mBrowserController.getVideoLoadingProgressView();
		}

		@Override
		public void onHideCustomView() {
			mBrowserController.onHideCustomView();
			super.onHideCustomView();
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// While these lines might look like they work, in practice,
			// Full-screen videos won't work correctly. I may test this out some
			// more
			// if (view instanceof FrameLayout) {
			// FrameLayout frame = (FrameLayout) view;
			// if (frame.getFocusedChild() instanceof VideoView) {
			// VideoView video = (VideoView) frame.getFocusedChild();
			// video.stopPlayback();
			// frame.removeView(video);
			// video.setVisibility(View.GONE);
			// }
			// } else {
			Activity activity = mBrowserController.getActivity();
			mBrowserController.onShowCustomView(view, activity.getRequestedOrientation(), callback);

			// }

			super.onShowCustomView(view, callback);
		}

		@SuppressLint("NewApi")
		@Override
		@Deprecated
		public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
			// While these lines might look like they work, in practice,
			// Full-screen videos won't work correctly. I may test this out some
			// more
			// if (view instanceof FrameLayout) {
			// FrameLayout frame = (FrameLayout) view;
			// if (frame.getFocusedChild() instanceof VideoView) {
			// VideoView video = (VideoView) frame.getFocusedChild();
			// video.stopPlayback();
			// frame.removeView(video);
			// video.setVisibility(View.GONE);
			// }
			// } else {
			mBrowserController.onShowCustomView(view, requestedOrientation, callback);

			// }

			super.onShowCustomView(view, requestedOrientation, callback);
		}
	}

	public class Title {

		private Bitmap mFavicon;

		private String mTitle;

		private Bitmap mDefaultIcon;

		public Title(Context context) {
			mDefaultIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_webpage);
			mFavicon = mDefaultIcon;
			mTitle = mActivity.getString(R.string.action_new_tab);
		}

		public void setFavicon(Bitmap favicon) {
			mFavicon = favicon;
			if (mFavicon == null) {
				mFavicon = mDefaultIcon;
			}
		}

		public void setTitle(String title) {
			if (title == null) {
				mTitle = "";
			} else {
				mTitle = title;
			}
		}

		public void setTitleAndFavicon(String title, Bitmap favicon) {
			mTitle = title;
			mFavicon = favicon;
			if (mFavicon == null) {
				mFavicon = mDefaultIcon;
			}
		}

		public String getTitle() {
			return mTitle;
		}

		public Bitmap getFavicon() {
			return mFavicon;
		}
	}

	private class CustomGestureListener extends SimpleOnGestureListener {

		@Override
		public void onLongPress(MotionEvent e) {
			mBrowserController.onLongPress();
		}

	}

	private void playVideo(String contentLink) {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		Uri data = Uri.parse(contentLink);
		intent.setDataAndType(data, "video/mp4");
		((Activity) mActivity).startActivity(intent);
		// Log.e(TAG, "playVideo Line 1" + contentLink);
		// Uri uri = Uri.fromFile(new File(contentLink));
		// Log.e(TAG, "playVideo Line 2 " + uri);
		// Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(uri,
		// "video/mp4");
		// ((Activity) activity).startActivity(intent);

		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.parse(contentLink), "video/mp4");
		//
		// ((Activity)activity).startActivity(intent);

	}
}