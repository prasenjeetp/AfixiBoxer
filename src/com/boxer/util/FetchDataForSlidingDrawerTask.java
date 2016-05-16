package com.boxer.util;


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
import com.boxer.browser.FetchDataListener;

import android.os.AsyncTask;

public class FetchDataForSlidingDrawerTask extends AsyncTask<String, Void, String>{
	
	 public List<WrtualBean> slidingDrawerAppInfoList = new ArrayList<WrtualBean>();
  private final FetchDataListener listener;
  private String msg;
  
  public FetchDataForSlidingDrawerTask(FetchDataListener listener) {
      this.listener = listener;
  }
  
  @Override
  protected String doInBackground(String... params) {
      if(params == null) return null;
      
      // get url from params
      String url = params[0];
      
      try {
          // create http connection
          HttpClient client = new DefaultHttpClient();
          HttpGet httpget = new HttpGet(url);
          
          // connect
          HttpResponse response = client.execute(httpget);
          
          // get response
          HttpEntity entity = response.getEntity();
          
          if(entity == null) {
              msg = "No response from server";
              return null;        
          }
       
          // get response content and convert it to json string
          InputStream is = entity.getContent();
          return streamToString(is);
      }
      catch(IOException e){
          msg = "No Network Connection";
      }
      
      return null;
  }
  
  @Override
  protected void onPostExecute(String sJson) {
      if(sJson == null) {
          if(listener != null) listener.onFetchFailure(msg);
          return;
      }        
      
      try {
          // convert json string to json array
          JSONArray aJson = new JSONArray(sJson);
          
          System.out.println("@@@@@@@@@@@@@------------> "+aJson);
          // create apps list
         
          
          for(int i=0; i<aJson.length(); i++) {
              JSONObject json = aJson.getJSONObject(i);
              WrtualBean appInfo = new WrtualBean();
              
              String se=json.getString("category");
              appInfo.setAppName(se);
            /*  appInfo.setAppDis(json.getString("description"));
              appInfo.setAppPrice(json.getString("price"));
              appInfo.setAppCompanyName(json.getString("company"));
              appInfo.setAppIconUrl(json.getString("icon"));
              appInfo.setAppScreenOne(json.getString("screen1"));
              appInfo.setAppScreenTwo(json.getString("screen2"));
              appInfo.setAppScreenThree(json.getString("screen3"));
              
              */
              
              slidingDrawerAppInfoList.add(appInfo);
          }
          
          //notify the activity that fetch data has been complete
          if(listener != null) listener.onSlidingDrawerDataFetchComplete(slidingDrawerAppInfoList);
      } catch (JSONException e) {
          msg = "Invalid response";
          if(listener != null) listener.onFetchFailure(msg);
          return;
      }        
  }
  
  /**
   * This function will convert response stream into json string
   * @param is respons string
   * @return json string
   * @throws IOException
   */
  public String streamToString(final InputStream is) throws IOException{
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder(); 
      String line = null;
      
      try {
          while ((line = reader.readLine()) != null) {
              sb.append(line + "\n");
          }
      } 
      catch (IOException e) {
          throw e;
      } 
      finally {           
          try {
              is.close();
          } 
          catch (IOException e) {
              throw e;
          }
      }
      
      return sb.toString();
  }
}
