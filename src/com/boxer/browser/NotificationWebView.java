package com.boxer.browser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class NotificationWebView extends Activity {
	WebView wv;
	Bundle bundle = new Bundle();
	String url;
	ProgressBar loadingProgressBar;
	ImageView refresh, next, back;
	Activity activity;
	private ProgressDialog progDailog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_web_view);
		wv = (WebView) findViewById(R.id.webView3);
		loadingProgressBar = (ProgressBar) findViewById(R.id.progressbar1);
		refresh = (ImageView) findViewById(R.id.imageView_RefreshIcon);
		next = (ImageView) findViewById(R.id.imageView_ForWardIcon);
		back = (ImageView) findViewById(R.id.imageView_BackwardIcon);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NotificationWebView.this, MainActivity.class);
				startActivity(i);
				NotificationWebView.this.finish();
				
			}
		});
		Bundle bundle = this.getIntent().getExtras();

		if (bundle != null) {
			// ObtainBundleData in the object
			url = bundle.getString("webLink");
			// Do something here if data received
		} else {
			Toast.makeText(activity, "Can not open Url", 3).show();
		}
		Log.e("url found===", url);
		activity = this;

		progDailog = ProgressDialog.show(activity, "Loading", "Please wait...",
				true);
		progDailog.setCancelable(false);

		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				progDailog.show();
				if (url.contains("market://details?id=")) {

					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					activity.finish();

				} else {
					// new LightningView(activity, url).loadUrl(url);

					MainActivity.mainUrl = url;
					Intent intent = new Intent(activity,
							B_MainActivity.class);
					startActivity(intent);
					activity.finish();

					// view.loadUrl(url);
				}
				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				progDailog.dismiss();
			}
		});
		if (url.startsWith("market")) {
			activity.startActivityForResult((new Intent(Intent.ACTION_VIEW, Uri
					.parse(url))),0);
			 

		} else {
			wv.loadUrl(url);
			url = null;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		this.finish();
	}

}
