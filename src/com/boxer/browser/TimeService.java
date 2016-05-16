package com.boxer.browser;


import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.boxer.browser.MainActivity.MyAsyncTask;
import com.boxer.util.Constant;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.widget.Toast;


public class TimeService extends Service {

	String currentDay;
    boolean count=false;
	java.util.Date currentTime;
	String currentTimeStr="";
	String endTime="";
	int currentProfileId=0;
	String strCurrentDate="";
    TimeService context=this;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

	//	Toast.makeText(getApplicationContext(), "Service Created", 1).show();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "Service Destroy", 1).show();
		super.onDestroy();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		Calendar calendar = Calendar.getInstance();
		currentDay = dayFormat.format(calendar.getTime());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		   //get current date time with Date()
		   Date date = new Date(startId, startId, startId);
		   System.out.println(dateFormat.format(date));
		   long tim=System.currentTimeMillis();
		   SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
		   String curTime =df.format(tim);
		   System.out.println("Time : "+curTime);
		   this.currentTimeStr=curTime;
		 

		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    strCurrentDate = sdf.format(cal.getTime());
		   // System.out.println("Current date in String Format: "+strCurrentDate);
		   // Toast.makeText(getApplicationContext(),"d = \n"+strCurrentDate+"="+curTime,Toast.LENGTH_LONG).show();
			
		    
		   if(curTime.equals("10:30 AM")){
			   new MyAsyncTask().execute();
			   //count=true;
		   }
		    
			 //  new MyAsyncTask().execute();
				   
			
				
			
		return super.onStartCommand(intent, flags, startId);
	}
	
	 public class MyAsyncTask extends AsyncTask<Void, Void, Void>{
		    public void postData() {
		    	

		        // Create a new HttpClient and Post Header
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(Constant.serviceNotificationUrl+"/TimeGcm");

		        try {
		            // Add your data
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		            nameValuePairs.add(new BasicNameValuePair("date",strCurrentDate));
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
	

	
	

}