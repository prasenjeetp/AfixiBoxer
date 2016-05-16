package com.boxer.browser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;

import com.boxer.browser.R;
import com.boxer.util.Constant;
import com.google.android.gms.gcm.GoogleCloudMessaging;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GCMNotificationIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	String msg = "";
	String bannerUrl = "";

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				for (int i = 0; i < 3; i++) {
					Log.i(TAG,
							"Working... " + (i + 1) + "/5 @ "
									+ SystemClock.elapsedRealtime());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}

				}

				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(this);
				boolean checkBoxValue = sharedPreferences.getBoolean(
						"CheckBox_Value", true);
				System.out
						.println("CheckBoxStatus----------->" + checkBoxValue);
				System.out.println("JSON DATA---------->" + extras.toString());

				msg = (String) extras.get(Constant.MESSAGE_KEY);

				Log.e("json data of notification == ", " " + msg);
				try {
					JSONObject jsonObject= new JSONObject(msg);
					Log.e("json data of notification", "link is "+jsonObject.optString("image"));
					if (jsonObject.optString("web").equals("")) {
						sendNotification(jsonObject.optString("msg")) ;
					}else if (jsonObject.optString("image").equals("null")) {
						sendNotification(jsonObject.optString("msg")) ;
					 
						
					}else {
						sendBigPictureStyleNotification();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				sendBigPictureStyleNotification();
				// boolean value = msg.matches("Today's(.*)");
				// if (value == true) {
				// if (checkBoxValue == true) {
				// sendNotification("" + msg);
				// Log.i(TAG, "Received: " + extras.toString());
				// }
				// }
				// boolean link = msg.contains("link");
				// if (link == true) {
				// // String[] parts = msg.split("-");
				// // String part1 = parts[0]; // 004
				// // String part2 = parts[1];
				//
				// // System.out.println("msg ="+msg);
				// // System.out.println("part1 ="+part1);
				// // System.out.println("part2 ="+part2);
				// sendNotification1(msg);
				// sendBigPictureStyleNotification();
				// } else {
				// sendNotification(msg);
				// sendBigPictureStyleNotification();
				// }

				/*
				 * else if(value==false){
				 * 
				 * String[]parts = msg.split("-"); String partOne = parts[0];
				 * String partTwo = parts[1];// 004 //String partTwo = parts[1];
				 * System.out.println("msg ="+msg);
				 * System.out.println("part ="+partTwo);
				 * 
				 * boolean linkvalue=partOne.matches("https://(.*)");
				 * 
				 * 
				 * if(linkvalue==true){ sendNotification1(""+partOne); } else
				 * if(!linkvalue==true){ System.out.println("c "+linkvalue);
				 * sendNotification(partOne); } }
				 */

			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		Log.d(TAG, "Preparing to send notification...: " + msg);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		 
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.app_icon);
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Splash.class), 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.app_icon).setLargeIcon(bitmap)
				.setContentTitle("Boxer Internet") 
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mBuilder.setSound(alarmSound);
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		Log.d(TAG, "Notification sent successfully.");
	}

	public void sendBigPictureStyleNotification() {
		TaskStackBuilder stackBuilder = TaskStackBuilder
				.create(getApplicationContext());
		String linkUrl = null;
		JSONObject notifiJsonObject = null;
		try {
			notifiJsonObject = new JSONObject(msg);
			bannerUrl = Constant.fileFolder
					+ notifiJsonObject.optString("image");
			linkUrl = notifiJsonObject.optString("web");
			Log.e("got url ", linkUrl);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap remote_picture = null;
		try {
			remote_picture = BitmapFactory.decodeStream((InputStream) new URL(
					bannerUrl).getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.app_icon);
		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Intent nIntent = new Intent(this, NotificationWebView.class);
		Bundle basket = new Bundle();
		basket.putString("webLink", linkUrl);
		nIntent.putExtras(basket);
		stackBuilder.addParentStack(NotificationWebView.class);
		stackBuilder.addNextIntent(nIntent);
		Builder builder = new Notification.Builder(this);
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(1,
				PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentTitle("Boxer Internet").setContentIntent(pendingIntent)
				.setLargeIcon(bitmap)
				// Notification title
				.setContentText(notifiJsonObject.optString("msg"))
				.setSound(soundUri)
				// you can put subject line.
				.setSmallIcon(R.drawable.app_icon);

		// Set your notification icon here.

		// .addAction(R.drawable.app_image, "Open ", pi);

		// Now create the Big picture notification.
		Notification notification = new Notification.BigPictureStyle(builder)
				.bigPicture(remote_picture).setSummaryText(notifiJsonObject.optString("msg")).build();
		// Put the auto cancel notification flag
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}

	private void sendNotification1(String link) {

		 
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Log.d(TAG, "Preparing to send notification...: " + msg);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		 

		Intent notificationIntent = new Intent(Intent.ACTION_VIEW);

		notificationIntent.setData(Uri.parse(link));
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.app_icon)
				.setContentTitle("Boxer Internet")
				// .setLargeIcon(remote_picture)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mBuilder.setSound(alarmSound);
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	private void sendNotification2(String partOne) {

		Bitmap remote_picture = null;
		try {
			remote_picture = BitmapFactory
					.decodeStream((InputStream) new URL(
							"http://7-themes.com/data_images/out/68/7004205-cool-black-backgrounds-27640.jpg")
							.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "Preparing to send notification...: " + partOne);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.app_icon)
				.setContentTitle("Boxer")
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(partOne))
				.setContentText(partOne);

		mBuilder.setContentIntent(contentIntent);
		mBuilder.setSound(alarmSound);
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		Log.d(TAG, "Notification sent successfully.");
	}

}