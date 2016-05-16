package com.boxer.browser;


import com.boxer.browser.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class BrowserActivity extends Activity implements OnClickListener {


	WebView webView;

	private String URL="";
	EditText googleSearch,addressSearch ;
	Button go;
	private Bundle webViewBundle;
    String url="";
    String getUrl="";
ProgressBar loadingProgressBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	 //InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	   //  imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(),InputMethodManager.RESULT_HIDDEN);
	
	setContentView(R.layout.activity_browser);
	webView = (WebView) findViewById(R.id.webview);
	
	//googleSearch = (EditText)findViewById(R.id.editText_searchText);
	addressSearch = (EditText)findViewById(R.id.editText_searchAddress);
	addressSearch.requestFocus();
		loadingProgressBar=(ProgressBar)findViewById(R.id.progressH); 
	go=(Button)findViewById(R.id.button_go);
	go.setOnClickListener(this);
    Bundle extras = getIntent().getExtras();
	Log.d("I m before If","1");
	if (extras!= null) {
		if(extras.getString("activity").equals("MainActivity")){
			this.URL = extras.getString("URL");
			//showSearchResult(URL);
			getWebView1(URL);
			addressSearch.setText(URL);
		}
		else if (extras.getString("activity").equals("MainSearch")){
			getWebView();
	    } 
	}
	

	

	 webView.setDownloadListener(new DownloadListener() {
	        public void onDownloadStart(String url, String userAgent,
	                String contentDisposition, String mimetype,
	                long contentLength) {
	          Intent i = new Intent(Intent.ACTION_VIEW);
	          i.setData(Uri.parse(url));
	          startActivity(i);
	        }
	    });
	 webView.setWebChromeClient(new WebChromeClient(){
	       
          public void onProgressChanged(WebView view, int progress) 
             {
             if(progress < 100 && loadingProgressBar.getVisibility() == ProgressBar.GONE){
          	   loadingProgressBar.setVisibility(ProgressBar.VISIBLE);
                
             }
             loadingProgressBar.setProgress(progress);
             if(progress == 100) {
          	   loadingProgressBar.setVisibility(ProgressBar.GONE);
                 
             }
          }
      });
	 
	// InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	  //  imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
	 addressSearch.setOnEditorActionListener(new OnEditorActionListener() {
		 
		 
		 
			@SuppressLint("SetJavaScriptEnabled")
			@SuppressWarnings("deprecation")
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				String url = "http://"+addressSearch.getText().toString();
				//webView.setWebViewClient(new HelloWebViewClient());
				 WebSettings webSettings = webView.getSettings();
				 webSettings.setDatabaseEnabled(true);
			        webSettings.setDomStorageEnabled(true);
			         webSettings.setAppCacheEnabled(true);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setPluginState(PluginState.ON);
				//webView.setWebViewClient(new WebViewClient());
				 //webView.setWebViewClient(new WebViewClient());
				
				
		        webView.getSettings().setBuiltInZoomControls(true);
		        webView.getSettings().setSupportZoom(true);
		        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
		        webView.getSettings().setAllowFileAccess(true); 
		        webView.getSettings().setDomStorageEnabled(true);
		       
		       
				webView.loadUrl(url);
				webView.getSettings().setRenderPriority(RenderPriority.HIGH);
				webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
				
				
				 MainSearch m=new MainSearch();
				 m.tabName(url);
				
				// TODO Auto-generated method stub
				return false;
			}
			
		});




	
	/*  googleSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@SuppressLint("SetJavaScriptEnabled")
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {	
		try {
			  if(actionId==EditorInfo.IME_ACTION_GO){
				  InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				  url = "https://www.google.co.in/?gfe_rd=ctrl&ei=4j4DU_KsCOaJ8QfyhYHYCQ&gws_rd=cr#q="+googleSearch.getText().toString();
				 
				  String s=googleSearch.getText().toString();
				
				 //WebSettings webSettings = ((WebView) webView).getSettings();
				   
					webView.getSettings().setLoadWithOverviewMode(true);
					webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
					webView.getSettings().setJavaScriptEnabled(true);
					webView.setWebViewClient(new WebViewClient());
			        webView.getSettings().setBuiltInZoomControls(true);
			        webView.getSettings().setSupportZoom(true);
			        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
			        webView.getSettings().setAllowFileAccess(true); 
			        webView.getSettings().setDomStorageEnabled(true);
			        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
			        
			        
			         if (webViewBundle == null) {
			        	 try{
			    		    webView.loadUrl(url);
			    		    //MainSearch m=new MainSearch();
							//m.tabName();
			        	 }catch (Exception e){
			     			e.printStackTrace();
			     			}
			    		} else {
			    		    webView.restoreState(webViewBundle);
			    		}
			         						    	    }
			
			  }
 		 catch (Exception e) {
 			// TODO: handle exception
 		}
     	
	
		// TODO Auto-generated method stub
		return false;
	}


});	*/
	  
	/*  webView.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
	       Toast.makeText(getApplicationContext(), ""+getUrl,Toast.LENGTH_SHORT).show();
			openDialog(1);//open dialog
			return false;
		}

	
	});*/
	 
	 webView.setWebViewClient(new InsideWebViewClient());
	}
	
	private class InsideWebViewClient extends WebViewClient {
		@Override
		// Force links to be opened inside WebView and not in Default Browser
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			Toast.makeText(getApplicationContext(), ""+url, Toast.LENGTH_LONG).show();
			System.out.println("url ="+url);
			return false;

		}

	}
	

	@SuppressLint("SetJavaScriptEnabled")
	private void getWebView1(String s) {
	
	
			//Log.d("GetWebView Invoked", URL);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				setProgress(progress * 100);
			}
		});
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
        webView.getSettings().setAllowFileAccess(true); 
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
        webView.loadUrl(s);
			

	}
	


	private void showSearchResult(String URL) {
		
		
		WebSettings webSettings = webView.getSettings();
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);
        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);  
		 

		
	}


	public void getWebView() {
	

               addressSearch.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				String url = "http://"+addressSearch.getText().toString();
				
			
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.setWebViewClient(new WebViewClient());
		        webView.getSettings().setBuiltInZoomControls(true);
		        webView.getSettings().setSupportZoom(true);
		        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
		        webView.getSettings().setAllowFileAccess(true); 
		        webView.getSettings().setDomStorageEnabled(true);
		        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
				webView.loadUrl(url);
				// TODO Auto-generated method stub
				return false;
			}
		});
		
	
	}
	
	
	private class HelloWebViewClient extends WebViewClient {

		private String getUrl;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			this.getUrl=url;
			view.loadUrl(url);
			Toast.makeText(getApplicationContext(), "s"+view.getUrl(),Toast.LENGTH_SHORT).show();
			
		return true;
		}
		}

		public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
		webView.goBack();
		return true;
		}
		return super.onKeyDown(keyCode, event);
		}
		
		private void openDialog(int i) {
		
			
			if(i==1){
				
				final Dialog dialog = new Dialog(BrowserActivity.this);
				 
				  dialog.setTitle(" Settings");
				  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			      dialog.setContentView(R.layout.linkclickdialog);
			      dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			      dialog.show();
			      
			      
			      TextView textViewNewTab=(TextView)dialog.findViewById(R.id.textView_openNewTab);
			      textViewNewTab.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String s =webView.getUrl().toString();
						System.out.println("url ="+s);
						Toast.makeText(getApplicationContext(), "s"+s,Toast.LENGTH_SHORT).show();
						 MainSearch m=new MainSearch();
						  m.addMethod1(BrowserActivity.this,s);
						dialog.dismiss();
					}
				});
			      

		}
		}


		@Override
		public void onClick(View view) {
			
			if(view==go){
				
				String url = "http://"+addressSearch.getText().toString();
				 InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				webView.getSettings().setJavaScriptEnabled(true);
				
		        webView.getSettings().setBuiltInZoomControls(true);
		        webView.getSettings().setSupportZoom(true);
		        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
		        webView.getSettings().setAllowFileAccess(true); 
		        webView.getSettings().setDomStorageEnabled(true);
		        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
				webView.loadUrl(url);
				webView.setWebViewClient(new WebViewClient());
				 MainSearch m=new MainSearch();
				 m.tabName(url);
				
			}
		}
		
		
		 private class MyBrowser extends WebViewClient {
		      @Override
		      public boolean shouldOverrideUrlLoading(WebView view, String url) {
		         view.loadUrl(url);
		         System.out.println("url ="+view.getUrl());
		         Toast.makeText(getApplicationContext(), view.getUrl(),Toast.LENGTH_LONG).show();
		         return false;
		      }
		   }
		
		
}
