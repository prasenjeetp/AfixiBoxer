package com.boxer.browser;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.boxer.util.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;



public class ShareExternalServer {
	
	
	
	
	public static final String MY_PREFS_NAME = "MyPrefsFile";
	public String shareRegIdWithAppServer(final Context context,
			final String regId) {
		
		SharedPreferences sharedPreferences = PreferenceManager
				             .getDefaultSharedPreferences(context);
 int getShareCount=sharedPreferences.getInt("shareCount", -1);
  System.out.println("GETSHARECOUNT---------->"+getShareCount);
		String result = "";
		Map<String, String> paramsMap = new HashMap<String, String>();
		String brand=android.os.Build.BRAND;
		String model=android.os.Build.MODEL;
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID =telephonyManager.getDeviceId();
		
        paramsMap.put("imei", deviceID);
		paramsMap.put("regId", regId);
		paramsMap.put("brandName", brand);
		paramsMap.put("model", model);
		paramsMap.put("appId", Constant.AppID);
		paramsMap.put("uuid", android.os.Build.SERIAL);
		 if(getShareCount!=1){
		try {
			URL serverUrl = null;
			try {
				serverUrl = new URL(Constant.APP_SERVER_URL);
			} catch (MalformedURLException e) {
				Log.e("AppUtil", "URL Connection Error: "
						+ Constant.APP_SERVER_URL, e);
				result = "Invalid URL: " + Constant.APP_SERVER_URL;
			}
			
			StringBuilder postBody = new StringBuilder();
			Iterator<Entry<String, String>> iterator = paramsMap.entrySet()
					.iterator();

			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				postBody.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					postBody.append('&');
				}
			}
			String body = postBody.toString();
			byte[] bytes = body.getBytes();
			HttpURLConnection httpCon = null;
			
			try {
				httpCon = (HttpURLConnection) serverUrl.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setUseCaches(false);
				httpCon.setFixedLengthStreamingMode(bytes.length);
				httpCon.setRequestMethod("POST");
				httpCon.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				OutputStream out = httpCon.getOutputStream();
				out.write(bytes);
				out.close();

				int status = httpCon.getResponseCode();
				System.out.println("STATUS CODE"+status);
				if (status == 200) {
					result = "RegId shared with Application Server. RegId: "
							+ regId;
					System.out.println("RegId shared with Application Server. RegId:");
					
					int sharecount=1;
					 SharedPreferences sharedPreferences1 = PreferenceManager
							          .getDefaultSharedPreferences(context);
					 Editor editor = sharedPreferences1.edit();
					 editor.putInt("shareCount", sharecount);
					 editor.commit();


					 
					
				} else {
					result = "Post Failure." + " Status: " + status;
				}
			} finally {
				if (httpCon != null) {
					httpCon.disconnect();
				}
			}

		} catch (IOException e) {
			result = "Post Failure. Error in sharing with App Server.";
			Log.e("AppUtil", "Error in sharing with App Server: " + e);
		}
		}  else {
			result = "RegId ALREADy shared with Application Server";
			System.out.println("RegId ALREADy shared with Application Server");
					
			
		}
		return result;
	}


}