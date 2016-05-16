package com.boxer.browser;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.boxer.browser.R;
import com.boxer.util.Constant;
public class AppDetail extends Activity implements OnClickListener {
	AQuery a;
	
//	int appImage[]={R.drawable.pre_app_image1,R.drawable.pre_app_image2,R.drawable.pre_app_image3};
//    int length=appImage.length;
    // to keep current Index of ImageID array
    int currentIndex=0;
    TextView appName, companyName, appPrice;
    ImageView img,preAppScreen;
    ProgressDialog dialog1;
    Bundle app_Name= new Bundle();
    String s, app_description;
    int curIndex;
    HorizontalScrollView hScrollView;
    String freeAppPackageName="";
    String appTitle="";
    String shareLink="";
    Dialog dialog;
    String apkName="";
    Button shareAppButton,downloadAppButton;
 // Progress Dialog Bar
 		ProgressDialog prgDialog;
 		
 		 String apk="";
		  String thirdPartyUrl="";
		  
		//download app variable
		    ProgressBar pb;
		    Dialog downloadDialog;
		    int downloadedSize = 0;
		    int totalSize = 0;
		    TextView cur_val;
		    String dwnload_file_path;
		    String appNameStr,appDisStr;
		    String topContentOne=""; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_detail);
		// hScrollView=(HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		  shareAppButton=(Button)findViewById(R.id.button_ShareApp);
		    downloadAppButton=(Button)findViewById(R.id.button_DownloadApp);
		    
		    downloadAppButton.setOnClickListener(this);
			shareAppButton.setOnClickListener(this);
		try {
			async_post_entity1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//a.id(img).progress(dialog).image(s);
	}

	
	
	 public void async_post_entity1() throws Exception {
		 
		
		
		
		 

			String url = Constant.serverPath+"/Previous.php";
			 a= new AQuery(getApplicationContext());
			 prgDialog = new ProgressDialog(this); // Instantiate Progress Dialog Bar
				prgDialog.setMessage("Please wait..."); // Set Progress Dialog Bar message
				prgDialog.setIndeterminate(false);  
				prgDialog.setMax(1000); // Progress Bar max limit
				prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Bar style
				prgDialog.setCancelable(false); // Progress Bar cannot be cancelable
		        a.progress(prgDialog).ajax(url, JSONObject.class, this,"jsonCallback");
		
					}
	 
	 public void jsonCallback(String url, JSONObject json, AjaxStatus status){
		 appName=(TextView) findViewById(R.id.textView_FreeAppName);
			companyName=(TextView) findViewById(R.id.textView_appCompany);
			appPrice=(TextView) findViewById(R.id.textView_appPrice);
			img=(ImageView) findViewById(R.id.imageView_FreeAppIcon);
			preAppScreen=(ImageView)findViewById(R.id.imageView_preAppScreen);
			app_Name=getIntent().getExtras();
			System.out.println("pack = "+app_Name.getString("pack").toString());
		    
			freeAppPackageName=app_Name.getString("pack").toString();
		    //shareLink=app_Name.getString("link").toString();
		    //appTitle=app_Name.getString("title").toString();
		    //System.out.println("pack = "+packName);
		    //System.out.println("link = "+shareLink);
		  //  System.out.println("title = "+appTitle);
		try {
			System.out.println("JSON DATA------------->>>>"+json);
			JSONArray jArray=json.getJSONArray("Previous");
		for(int i=0; i<=jArray.length();i++){
			
			JSONObject jobj=jArray.getJSONObject(i);
		    if(freeAppPackageName.equalsIgnoreCase(jobj.getString("package"))){
		    	
		    	System.out.println("pack  = "+jobj.getString("package"));
		    	appName.setText(jobj.getString("title"));
		    	 appNameStr=appName.getText().toString();
		    	System.out.println(">>>>>>>>>> "+jobj.getString("title"));
		    	companyName.setText(jobj.getString("company"));
		    	//appPrice.setText("$"+jobj.getString("price"));
		    	
		    	 
		    	// thirdPartyUrl=jobj.getString("google");
		    	appTitle=jobj.getString("title");
		    	s=jobj.getString("icon");
		    	
		    	System.out.println("s--------"+s);
				
					app_description=jobj.getString("description");
		    	
					
		String htmlText = "<body font-size: 5px; color: #000000;}</style>"+"<p align=\"justify\">"+app_description+
					        		"</body>";
					 
					      WebView webView = (WebView) findViewById(R.id.webView2);
					      WebSettings settings= webView.getSettings();
					      settings.setDefaultFontSize(14);
					       webView.loadData(String.format(htmlText,null), "text/html", "utf-8");
					       

		    	a.id(img).progress(dialog).image(Constant.serverPathImage+s);
		    	a.id(preAppScreen).progress(dialog).image(Constant.serverPathImage+jobj.getString("screen"));
		    	
		    	 apk=jobj.getString("apk").toString();
	               System.out.println("apk name "+apk);
	               String[] parts = apk.split("\\.");
	               this.apkName = parts[0]; 
	               System.out.println("apk name s "+parts[0]);
	               thirdPartyUrl=jobj.getString("glink");
	               checkAppInstallOrNot(freeAppPackageName);
		    }
	
		}
		  } catch (JSONException e) {
		         
                e.printStackTrace();
            }

	 }
	
	
	
	 private void checkAppInstallOrNot(String freeAppPackageName) {
		 System.out.println("name ="+freeAppPackageName);
		
	   	  boolean installed  =   appInstalledOrNot(freeAppPackageName);  
	   	 System.out.println("b ="+installed);
	   	  if(installed) {
	   		downloadAppButton.setText("Open");
	   		  openApplication();
	   	  }
	         else {
	        	 downloadAppButton.setText("Free");
	       	  installApp();
	       	 
	       	  
	         }
		}
	// check app install or not
			 public boolean appInstalledOrNot(String freeAppPackageName) {
				
				 PackageManager pm = getPackageManager();
			        boolean app_installed = false;
			        try {
			            pm.getPackageInfo(freeAppPackageName, PackageManager.GET_ACTIVITIES);
			            app_installed = true;
			           // downloadAppButton.setText("open");
			        }
			        catch (PackageManager.NameNotFoundException e) {
			            app_installed = false;
			        }
			        return app_installed ;
			}

			 private void openApplication() {
					
					downloadAppButton.setOnClickListener(this);	  
				 }	
			 
			 private void installApp() {
					
				 downloadAppButton.setOnClickListener(this);
			}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_detail, menu);
		return true;
	}
	 @Override
	    public void onBackPressed() {
		 super.onBackPressed();
	        //setResult(Activity.RESULT_OK, intent);
		  overridePendingTransition(R.anim.slide_in_dialog, R.anim.slide_out_right_dialog);
	      finish();
	       
	    }

	@Override
	public void onClick(View view) {

		if(view==shareAppButton){
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
		    sharingIntent.setType("text/plain");
		    //System.out.println(" link ="+app_Name.getAppLinkUrl());
		    String shareBody = ""+appTitle+"\napplication is free for today.You can download from-\n"+shareLink+"";
		    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Todays Free App ");
		    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		    startActivity(Intent.createChooser(sharingIntent, "Share via"));
		}
		if(view==downloadAppButton && downloadAppButton.getText().equals("Open")){
			 //This intent will help you to launch if the package is already installed
			System.out.println("jpack"+freeAppPackageName);
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(freeAppPackageName);
            if(LaunchIntent==null){
           	// Toast.makeText(getApplicationContext(), "no",Toast.LENGTH_SHORT).show();
           	 downloadAppButton.setText("Buy");
           	 }else{
           	 startActivity(LaunchIntent);
            }
           
            System.out.println("App already installed on your phone");        
		}
	
			if(view==downloadAppButton && downloadAppButton.getText().equals("Free")){
				
				if(!thirdPartyUrl.equals("")){
					
					Intent intent = new Intent(Intent.ACTION_VIEW, 
						     Uri.parse(thirdPartyUrl));
						startActivity(intent);
						return;
				}
				
				DownloadConform(15);
				

			}

		
		
	}

	 private void DownloadConform(int i) {
	if(i==15){
		final Dialog dialog = new Dialog(AppDetail.this);
		 
		 // dialog.setTitle(" Feed back");
		  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	      dialog.setContentView(R.layout.dialog_conform_download);
	      dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
	      dialog.show();
	      
	      Button exitYes=(Button)dialog.findViewById(R.id.button_exitYes);
	      Button exitNo=(Button) dialog.findViewById(R.id.button_exitNo);
	      exitYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
			dialog.dismiss();
				 dwnload_file_path = Constant.fileFolder+apkName+".apk";
					showProgress(dwnload_file_path);
			       new Thread(new Runnable() {
			           public void run() {
			           	 downloadFile();
			           }

			   	    
			   	    void downloadFile(){
			   	    	
			   	    	try {
			   	    		
			   	    		
			   	    		URL url = new URL(dwnload_file_path);
			   	    		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			   	    		urlConnection.setRequestMethod("GET");
			   	    		urlConnection.setDoOutput(true);

			   	    		//connect
			   	    		urlConnection.connect();

			   	    		//set the path where we want to save the file    		
			   	    		final File SDCardRoot = Environment.getExternalStorageDirectory(); 
			   	    		//create a new file, to save the downloaded file 
			   	    		File file = new File(SDCardRoot,apkName+".apk");
			   	 
			   	    		FileOutputStream fileOutput = new FileOutputStream(file);

			   	    		//Stream used for reading the data from the internet
			   	    		InputStream inputStream = urlConnection.getInputStream();

			   	    		//this is the total size of the file which we are downloading
			   	    		totalSize = urlConnection.getContentLength();

			   	    		runOnUiThread(new Runnable() {
			   				    public void run() {
			   				    	pb.setMax(totalSize);
			   				    }			    
			   				});
			   	    		
			   	    		//create a buffer...
			   	    		byte[] buffer = new byte[1024];
			   	    		int bufferLength = 0;

			   	    		while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
			   	    			fileOutput.write(buffer, 0, bufferLength);
			   	    			downloadedSize += bufferLength;
			   	    			// update the progressbar //
			   	    			runOnUiThread(new Runnable() {
			   	    			    public void run() {
			   	    			    	pb.setProgress(downloadedSize);
			   	    			    	float per = ((float)downloadedSize/totalSize) * 100;
			   	    			    	cur_val.setText("Downloading " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );
			   	    			    	
			   	    			    }
			   	    			});
			   	    		}
			   	    		//close the output stream when complete //
			   	    		fileOutput.close();
			   	    		
			   	    		
			   	    		runOnUiThread(new Runnable() {
			   				    public void run() {
			   				    	dialog.dismiss(); // if you want close it..
			   				    	// this.checkUnknownSourceEnability();
			   				    	// this.initiateInstallation();
			   				    	installApk();
			   				    	
			   						
			   				    }
			   				    
			   				    
			   				    
			   				    private void installApk(){
			   				        Intent intent = new Intent(Intent.ACTION_VIEW);
			   				        // new File(Environment.getExternalStorageDirectory() +"Fashionist.apk")
			   				        Uri uri = Uri.fromFile(new File("/sdcard/"+apkName+".apk"));
			   				        intent.setDataAndType(uri, "application/vnd.android.package-archive");
			   				        startActivityForResult(intent, 10);
			   				        checkAppInstallOrNot(freeAppPackageName);
			   				        //Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();	
			   				    
			   				    }
			   			});    		
			   	    	
			   	    		
			   	    	}  
			   	    	catch (final MalformedURLException e) {
			   	    		showError("Error : MalformedURLException " + e);  		
			   	    		e.printStackTrace();
			   	    	} catch (final IOException e) {
			   	    		showError("Error : IOException " + e);  		
			   	    		e.printStackTrace();
			   	    	}
			   	    
			   	    	catch (final Exception e) {
			   	    		showError("Error : Please check your internet connection " + e);
			   	    	}
			   	    	
			   	    }


					void showError(final String err){
				    	runOnUiThread(new Runnable() {
						    public void run() {
						    	Toast.makeText(AppDetail.this, err, Toast.LENGTH_LONG).show();
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



	void showProgress(String file_path){
	    	dialog = new Dialog(AppDetail.this);
	    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	dialog.setContentView(R.layout.download_progress_dialog);
	    	
	    	dialog.setTitle("Download Progress");
             
	    	TextView text = (TextView) dialog.findViewById(R.id.tv1);
	    	text.setText(""+appNameStr);
	    	cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
	    	dialog.show();
	    	
	    	pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
	    	pb.setProgress(0);
	    	pb.setProgressDrawable(getResources().getDrawable(R.drawable.download_app_progress));  
	    }
	 
	   @Override
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   super.onActivityResult(requestCode, resultCode, data);
	       // requestCode == 1 means the result for package-installer activity
	       if (requestCode == 10) 
	       {
	           // resultCode == RESULT_CANCELED means user pressed `Done` button
	           if (resultCode == RESULT_CANCELED) {
	        	   downloadAppButton.setText("Open");
	        	   openApplication();
	               //Toast.makeText(this, "User pressed 'Done' button", Toast.LENGTH_SHORT);
	        	   System.out.println("user pressed open button ="+apkName+".apk");
	        	   File file = new File("/sdcard/"+apkName+".apk");
	        	   boolean deleted = file.delete();
	        	   this.topContentOne=apkName;
	        	   
	        	   if(isConnected()){
	   				  new MyAsyncTaskForDownload().execute();
	   				  //downloadAppButton.setBackgroundColor(Color.parseColor("#7cAA2d"));
	   				// checkAppInstallOrNot(freeAppPackageName);
	   				 
	   			   }
	   	           else{
	   	                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
	   	           }
	        	   
	        	   
	           } 
	        
	           
	       }
	      

	   }
	   
	   
	   public class MyAsyncTaskForDownload extends AsyncTask<Void, Void, Void>{
		    public void postData() {
		    	

		        // Create a new HttpClient and Post Header
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(Constant.serverPath+"/Download");

		        try {
		            // Add your data
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		            nameValuePairs.add(new BasicNameValuePair("name",appName.getText().toString()));
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
	   
	   public boolean isConnected(){
	        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
	            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	            if (networkInfo != null && networkInfo.isConnected()) 
	                return true;
	            else
	                return false;    
	    }
	}


	   



