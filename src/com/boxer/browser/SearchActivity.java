package com.boxer.browser;



import com.boxer.browser.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class SearchActivity extends Activity {

	WebView webView;
	ProgressBar loadingProgressBar,loadingTitle;
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		
		webView=(WebView)findViewById(R.id.webviewSearch);
		 loadingProgressBar=(ProgressBar)findViewById(R.id.progressH); 
			Intent intent = getIntent();
			String url = intent.getStringExtra("url");
		
			webView.getSettings().setJavaScriptEnabled(true);
			 WebSettings webSettings = webView.getSettings();
	         webSettings.setDatabaseEnabled(true);
	         webSettings.setDomStorageEnabled(true);
	         webSettings.setAppCacheEnabled(true);
	         webView.getSettings().setJavaScriptEnabled(true);
	         webView.loadUrl(url);
	         webView.getSettings().setRenderPriority(RenderPriority.HIGH);
	         webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	         webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);  
	         
	         webView.setDownloadListener(new DownloadListener() {
	 			
	 			@Override
	 			public void onDownloadStart(String url, String userAgent,
	 					String contentDisposition, String mimetype, long contentLength) {
	 				
	 				
	 				 Intent intent = new Intent(Intent.ACTION_VIEW);
	 		            intent.setData(Uri.parse(url));
	 		            startActivity(intent);

	 				
	 			}
	 		});
	 		  
	         webView.setOnTouchListener(new View.OnTouchListener() {
	 		       
	 		        public boolean onTouch(View v, MotionEvent event) {
	 		            switch (event.getAction()) {
	 		                case MotionEvent.ACTION_DOWN:
	 		                case MotionEvent.ACTION_UP:
	 		                    if (!v.hasFocus()) {
	 		                        v.requestFocus();
	 		                    }
	 		                    break;
	 		            }
	 		            return false;
	 		        }
	 		    });
	 		 

	         
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
		    
		    
	         webView.setWebViewClient(new WebViewClient());
	 	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
