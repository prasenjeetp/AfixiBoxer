package com.boxer.browser;

import com.boxer.browser.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	String fLaunch;
	public static final String FIRSTICON = "firstIcon";
	public static final String MyPREFERENCES = "MyIcon";
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e("service recivesd", "here");
		SharedPreferences shared = context.getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		 
		fLaunch = (shared.getString(FIRSTICON, ""));
		if (fLaunch.equals("NO")) {
			Log.e("Inside Receiver", "2nd or more tym");
		}else {
			Log.e("Inside Reciver", "1 tym");
			addShortcut(context);
		}
		SharedPreferences.Editor editor = shared.edit();

		editor.putString(FIRSTICON,  "NO");
		editor.commit();
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMNotificationIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}

	private void addShortcut(Context context) {
		// Adding shortcut for MainActivity
		// on Home screen
		Intent shortcutIntent = new Intent(context, Splash.class);

		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Boxer Internet");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(context,
						R.drawable.app_icon));

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(addIntent);
	}
}