package com.boxer.browser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boxer.bean.WrtualBean;
import com.boxer.util.Constant;
import com.boxer.util.ServiceHandler;



import android.os.AsyncTask;
import android.util.Log;

public class FetchDataTask extends AsyncTask<Void, Void, Void>{
	 String url = Constant.serverPath+"/Previous";
	 public List<WrtualBean> previousAppInfoList = new ArrayList<WrtualBean>();
   private final FetchDataListener listener;
   private String msg;
   JSONArray jArray = null;
   WrtualBean previousAppObj;
   
   public FetchDataTask(FetchDataListener listener) {
       this.listener = listener;
   }
   
   @Override
   protected Void doInBackground(Void... arg0) {
	   
	// Creating service handler class instance
       ServiceHandler sh = new ServiceHandler();

       // Making a request to url and getting response
       String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST);

       Log.d("Response:fetch data ", "> " + jsonStr);

       final String TAG_PREVIOUS = "previous";
       
       if (jsonStr != null) {
           try {
               JSONObject jsonObj = new JSONObject(jsonStr);
                
               // Getting JSON Array node
               jArray = jsonObj.getJSONArray(TAG_PREVIOUS);
              
               // looping through All Contacts
               for (int i = 0; i < jArray.length(); i++) {
            	   previousAppObj=new WrtualBean();
                   JSONObject json = jArray.getJSONObject(i);
                   previousAppObj.setAppName(json.getString("title"));
                   previousAppObj.setAppDis(json.getString("description"));
                   previousAppObj.setAppPrice(json.getString("price"));
                   previousAppObj.setAppCompanyName(json.getString("company"));
                   previousAppObj.setAppIconUrl(json.getString("icon"));
                   previousAppObj.setAppScreenOne(json.getString("screen1"));
                   previousAppObj.setAppScreenTwo(json.getString("screen2"));
                   previousAppObj.setAppScreenThree(json.getString("screen3"));
                   previousAppObj.setAppLinkUrl(json.getString("google_link"));
                   previousAppInfoList.add(previousAppObj);
                 
               }
               
               if(listener != null) listener.onFetchComplete(previousAppInfoList);
           } catch (JSONException e) {
               e.printStackTrace();
              
           }
       }
           else {
               Log.e("ServiceHandler", "Couldn't get any data from the url");
           }

           return null;
       }
   
   
 }
