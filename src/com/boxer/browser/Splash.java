package com.boxer.browser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boxer.util.Constant;
import com.boxer.util.HashGenerator;
import com.boxer.util.ServiceHandler;
import com.google.android.gms.location.LocationListener;
import com.manishkpr.slidingtabsexample.Tabs;
import com.mobileapptracker.MobileAppTracker;

public class Splash extends Activity implements LocationListener,
		android.location.LocationListener {
	private long ms = 0;
	private long splashTime = 1500;
	long minTime = (long) 100.00;
	float minDist = 10.5f;
	private boolean splashActive = true;
	private boolean paused = false;
	public static List<Tabs> listTab;
	String tabResponse;
	TextView appName;
	Location location;
	boolean isDownloade = false;
	Context contexts;
	public static String email = "";
	public MobileAppTracker mobileAppTracker = null;
	String androidID = "", imei = "", handset = "", osVersion = "",
			mPhoneNumber1 = "", mPhoneNumber2 = "", defltLang = "",
			avlLnag = "", dImei = "", version = "", ver, mac = "";
	public static Double lat = 0.00, lng = 0.00;
	String responce = "";
	boolean isIdPosted = false;
	private LocationManager locationManager;
	private String provider;
	TextView tvTnC;
	Button btnContinue;
	LinearLayout llStratUp;
	boolean isFirstLaunch = true;
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String FIRSTLAUNCH = "fistlaunch";
	CheckBox chkBoxTnC;
	ServiceHandler sh;
	String fLaunch;
	Typeface tfSemiBoldItalic, tfRegular, tfPlain;
	TextView tvWelcome, tvBoxer, tvIndiasInternet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Fabric.with(this, new Crashlytics());

		// Hides the titlebar" "
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		tfSemiBoldItalic = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebBold);
		tfRegular = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebItalic);
		tfPlain = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebRegular);
		sh = new ServiceHandler();
		SharedPreferences shared = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		fLaunch = (shared.getString(FIRSTLAUNCH, ""));

		if (isNetworkAvailable()) {
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putString("getCurrent", "NothingFound").commit();

			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putString("listPrevious", "NothingFound").commit();
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putString("listTop1", "NothingFound").commit();
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putString("listTop2", "NothingFound").commit();
			PreferenceManager.getDefaultSharedPreferences(this).edit()
					.putString("listTop3", "NothingFound").commit();
		}
		if (!isNetworkAvailable()) {
			Intent intent = new Intent(Splash.this, NoInternetConnection.class);
			startActivity(intent);
			finish();
		} else {
			new DownloadResponce().execute();
		}

		// Get the location manager
		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		tvBoxer = (TextView) findViewById(R.id.tvBoxer);
		tvIndiasInternet = (TextView) findViewById(R.id.tvIndiasInternet);
		tvWelcome = (TextView) findViewById(R.id.tvWelcome);

		tvBoxer.setTypeface(tfSemiBoldItalic);
		tvWelcome.setTypeface(tfSemiBoldItalic);
		tvIndiasInternet.setTypeface(tfRegular);
		btnContinue = (Button) findViewById(R.id.btnContinue);
		btnContinue.setTypeface(tfSemiBoldItalic);
		tvTnC = (TextView) findViewById(R.id.tvTnC);
		tvTnC.setTypeface(tfPlain);
		llStratUp = (LinearLayout) findViewById(R.id.llStratUp);
		chkBoxTnC = (CheckBox) findViewById(R.id.chkTnC);
		chkBoxTnC.setTypeface(tfPlain);
		chkBoxTnC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// is chkIos checked?
				if (((CheckBox) v).isChecked()) {

					btnContinue.setSelected(true);
				} else {
					btnContinue.setSelected(false);
				}

			}
		});
		btnContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (chkBoxTnC.isChecked()) {

					isFirstLaunch = false;
					doPrintIDs();

					new IDsAsyncTask().execute();
					SharedPreferences.Editor editor = sharedpreferences.edit();

					editor.putString(FIRSTLAUNCH, isFirstLaunch + "");
					editor.commit();
					Intent intent = new Intent(Splash.this, MainActivity.class);
					startActivity(intent);
					Splash.this.finish();
				} else {
					Toast.makeText(Splash.this,
							getResources().getString(R.string.check_TnC), 3)
							.show();
				}
			}
		});
		tvTnC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Splash.this, License.class);
				startActivity(intent);

			}
		});

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		}
		doPrintIDs();
		// if (!isNetworkAvailable()) {
		// Intent intent = new Intent(Splash.this, NoInternetConnection.class);
		// startActivity(intent);
		// finish();
		// } else {
		// new IDsAsyncTask().execute();
		// }

		// Typeface typeface =
		// Typeface.createFromAsset(tv.getContext().getAssets(),
		// "SoulMission.ttf");
		// tv.setTypeface(typeface);
		// mobileAppTracker = MobileAppTracker.init(context, advertiserId,
		// conversionKey);

		mobileAppTracker = MobileAppTracker.init(getApplicationContext(),
				Constant.MatAdvID, Constant.MatConvKey);

		mobileAppTracker.setAndroidId(Secure.getString(getContentResolver(),
				Secure.ANDROID_ID));
		mobileAppTracker.setGoogleUserId("" + getEmail(Splash.this));
		String deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
		mobileAppTracker.setDeviceId(deviceId);

		// WifiManager objects may be null
		try {
			WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			mobileAppTracker.setMacAddress(wm.getConnectionInfo()
					.getMacAddress());
		} catch (NullPointerException e) {
		}

		// Thread mythread = new Thread() {
		// public void run() {
		// try {
		// while (splashActive && ms < splashTime) {
		// if (!paused)
		// ms = ms + 100;
		// sleep(100);
		//
		// }
		// } catch (Exception e) {
		// } finally {
		//
		// checkInternet();
		//
		// }
		// }
		//
		// private void checkInternet() {
		// boolean check = isNetworkAvailable();
		//
		// if (check != true) {
		// Intent intent = new Intent(Splash.this,
		// NoInternetConnection.class);
		// startActivity(intent);
		// finish();
		// } else {
		//
		// Intent intent = new Intent(Splash.this, MainActivity.class);
		// startActivity(intent);
		//
		// finish();
		// }
		// }
		//
		// };

		if (fLaunch.equals("")) {

		} else {
			llStratUp.setVisibility(View.GONE);
			// mythread.start();
		}

		// setRecurringAlarmTime(getBaseContext());
		getLocation();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Get source of open for app re-engagement
		mobileAppTracker.setReferralSources(this);
		// MAT will not function unless the measureSession call is included
		mobileAppTracker.measureSession();
	}

	void doPrintIDs() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		mac = wInfo.getMacAddress();
		email = getEmail(Splash.this);
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		androidID = Secure.getString(Splash.this.getContentResolver(),
				Secure.ANDROID_ID);
		handset = Build.MANUFACTURER + " " + Build.MODEL;
		osVersion = Build.VERSION.RELEASE;
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mPhoneNumber1 = tMgr.getLine1Number();
		defltLang = Locale.getDefault().getDisplayLanguage();
		Log.e("ids ===", "Email==" + email + " android ID==" + androidID
				+ " Imei=== " + imei + " mobile Nuber " + mPhoneNumber1
				+ "  selected Lang = "
				+ Locale.getDefault().getDisplayLanguage() + " mac " + mac);
		// String enCriptedImei = null,dCriptedImei = null;
		// try {
		// enCriptedImei = EncodeDecodeAES.encrypt(imei);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// Log.e("Encryption test ",
		// "Encryption  "
		// + enCriptedImei
		// + "Decryption="
		// + EncodeDecodeAES.decrypt(enCriptedImei));
		Locale[] avlLnagString = Locale.getAvailableLocales();
		for (int i = 0; i < 5; i++) {
			Log.e(" " + i, avlLnagString[i].toString());
			avlLnag = avlLnag + " , " + avlLnagString[i].toString();
			PackageInfo pInfo;
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				version = pInfo.versionName;
				ver = "" + pInfo.versionCode;
				Log.e("version codes", "  " + version + " " + " " + ver);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

	class DownloadResponce extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			tabResponse = sh.makeServiceCall(Constant.serverPath
					+ "/Responses.php?locale="
					+ Locale.getDefault().getLanguage(), ServiceHandler.GET);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (tabResponse != null) {
				if (!tabResponse.equals("")) {

					isDownloade = true;
					Log.e("Responce === ", tabResponse);
					listTab = new ArrayList<Tabs>();

					try {
						JSONObject tabObject = new JSONObject(tabResponse);
						JSONArray jsonArrayTabs = tabObject
								.optJSONArray("Response");
						for (int i = 0; i < jsonArrayTabs.length(); i++) {
							Tabs tabs = new Tabs();
							tabs.setContentType(jsonArrayTabs.optJSONObject(i)
									.getString("contentType"));
							tabs.setName(jsonArrayTabs.optJSONObject(i)
									.getString("Name"));
							tabs.setUrl(jsonArrayTabs.optJSONObject(i)
									.getString("Url"));
							listTab.add(tabs);

						}
						Tabs tabsHome = new Tabs();
						tabsHome.setContentType("HomePage");
						tabsHome.setName(getResources().getString(
								R.string.Internet));
						tabsHome.setUrl("Search");
						listTab.add(tabsHome);
						if (listTab.size() > 3) {
							Collections.reverse(listTab);
							if (fLaunch.equals("")) {

							} else {
								Intent intent = new Intent(Splash.this,
										MainActivity.class);
								startActivity(intent);
								Splash.this.finish();
							}
						} else {
							Intent intent = new Intent(Splash.this,
									NoInternetConnection.class);
							startActivity(intent);
							finish();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Intent intent = new Intent(Splash.this,
							NoInternetConnection.class);
					startActivity(intent);
					finish();
				}
			} else {
				Intent intent = new Intent(Splash.this,
						NoInternetConnection.class);
				startActivity(intent);
				finish();
			}
		}

	}

	class IDsAsyncTask extends AsyncTask<Void, Void, Void> {
		public void postData() {
			// Toast.makeText(getApplicationContext(), ""+email+" "+feedback,
			// Toast.LENGTH_SHORT).show();

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost httppost = new
			// HttpPost(Constant.serverPath+"/feedback.php");
			HttpPost httppost = new HttpPost(Constant.serverPath
					+ "/mReport.php");
			// a. IMEI
			// b. GID
			// c. AID
			// d. Country
			// e. Handset

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("activeimei",
						HashGenerator.encrypt(imei, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("GID", HashGenerator
						.encrypt(email, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("AID", HashGenerator
						.encrypt(androidID, Constant.EncriptionKey)));
				nameValuePairs
						.add(new BasicNameValuePair("Country", HashGenerator
								.encrypt("India", Constant.EncriptionKey)));
				nameValuePairs
						.add(new BasicNameValuePair("Handset", HashGenerator
								.encrypt(handset, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("OS", HashGenerator
						.encrypt(osVersion, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("deactive_imei", ""));
				nameValuePairs.add(new BasicNameValuePair("default_Language",
						HashGenerator
								.encrypt(defltLang, Constant.EncriptionKey)));
				nameValuePairs
						.add(new BasicNameValuePair("avl_Language",
								HashGenerator.encrypt(avlLnag,
										Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Lat", HashGenerator
						.encrypt("" + lat, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("Lang", HashGenerator
						.encrypt("" + lng, Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("MobileNumber1",
						HashGenerator.encrypt(mPhoneNumber1,
								Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("MobileNumber2",
						HashGenerator.encrypt(mPhoneNumber2,
								Constant.EncriptionKey)));
				nameValuePairs.add(new BasicNameValuePair("appid",
						Constant.AppID));
				nameValuePairs.add(new BasicNameValuePair("BoxerVersion",
						Constant.App_Version));
				nameValuePairs
						.add(new BasicNameValuePair("macAddress", HashGenerator
								.encrypt("" + mac, Constant.EncriptionKey)));
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
			if (!isIdPosted) {
				postData();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Log.e("TabRespoce", "i m here "+tabResponse);
			try {
				if (!responce.equals("")) {
					JSONObject responceObject = new JSONObject(responce);
					Log.e("responce ", responce);
					// Toast.makeText(Splash.this,
					// "status=== " + responceObject.optString("status"),
					// 3).show();

					if (responceObject.optString("status").equals("true")) {
						isIdPosted = true;
					} else if (responceObject.optString("status").equals(
							"false")) {
						isIdPosted = false;
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("splash id responce", responce);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		lat = (location.getLatitude());
		lng = (location.getLongitude());
		Log.e("hello", "" + lat + "   " + lng);
	}

	public Location getLocation() {
		boolean isGPSEnabled, isNetworkEnabled;
		try {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// locationManager = (LocationManager)
			// getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				// this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, minTime, minDist,
							Splash.this);
					Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							lng = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, minTime, minDist,
								Splash.this);
						Log.d("GPS", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lng = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("fron here", lat + " " + lng);

		return location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}