package com.boxer;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.mobileapptracker.MobileAppTracker;

public class MobileAppTracking extends Activity {
	public MobileAppTracker mobileAppTracker = null;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main); 
 
        // Initialize MAT
        mobileAppTracker = MobileAppTracker.init(getApplicationContext(),
                                                 "19408",
                                                 "3f25fcac41ed6d4bc8503dc9bc55ada0");
 
        mobileAppTracker.setAndroidId(Secure.getString(getContentResolver(), Secure.ANDROID_ID));
        
        String deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        mobileAppTracker.setDeviceId(deviceId);
        
     // WifiManager objects may be null
        try {
            WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            mobileAppTracker.setMacAddress(wm.getConnectionInfo().getMacAddress());
        } catch (NullPointerException e) {
        }
        
        // If your app already has a pre-existing user base before you implement the MAT SDK, then
        // identify the pre-existing users with this code snippet.
        // Otherwise, MAT counts your pre-existing users as new installs the first time they run your app.
        // Omit this section if you're upgrading to a newer version of the MAT SDK.
        // This section only applies to NEW implementations of the MAT SDK.
        //boolean isExistingUser = ...
        //if (isExistingUser) {
        //    mobileAppTracker.setExistingUser(true); 
        //}
    }
 
    @Override
    public void onResume() {
        super.onResume();
        // Get source of open for app re-engagement
        mobileAppTracker.setReferralSources(this);
        // MAT will not function unless the measureSession call is included
        mobileAppTracker.measureSession();
    }
	
	
	
}
