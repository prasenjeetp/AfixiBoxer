package com.boxer.browser;

import com.boxer.browser.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
	private WebView webView;
	String url="";
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);
		
		
		Bundle extras = getIntent().getExtras();
		   if (extras != null) 
		   {
		         url = extras.getString("webLink");
		   }
		
		webView = (WebView) findViewById(R.id.webView_OpenNotiUrl);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

}
