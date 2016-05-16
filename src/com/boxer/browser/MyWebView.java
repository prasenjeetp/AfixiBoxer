package com.boxer.browser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class MyWebView extends Activity {
	WebView wv;
	Bundle bundle = new Bundle();
	String url;
	ProgressBar loadingProgressBar;
	ImageView refresh, next, back;

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

		Intent intent = getIntent();

		String url = intent.getStringExtra("webLink");
		Log.e("url found===", url);
		// startWebView("" + url);
		wv.getSettings().setJavaScriptEnabled(true);

		wv.loadUrl(url);

		WebSettings webSettings = wv.getSettings();

		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setJavaScriptEnabled(true);
		wv.setWebViewClient(new WebViewClient());
		wv.requestFocus(View.FOCUS_DOWN);
		wv.getSettings().setRenderPriority(RenderPriority.HIGH);
		wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		wv.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// do your handling codes here, which url is the requested url
				// probably you need to open that url rather than redirect:

				Log.e("over load url Calling", "" + url);
				// new LightningView(MyWebView.this, url).loadUrl(url);
				if (url.contains("market://details?id=")) {

					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					MyWebView.this.finish();

				} else {
					// new LightningView(MyWebView.this, url).loadUrl(url);

					MainActivity.mainUrl = url;
					Intent intent = new Intent(MyWebView.this,
							B_MainActivity.class);
					startActivity(intent);
					MyWebView.this.finish();

					// view.loadUrl(url);
				}

				return true; // then it is not handled by default action
			}
		});
		wv.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView view, int progress) {
				Log.e("progress..", "" + progress);
				if (progress < 100
						&& loadingProgressBar.getVisibility() == ProgressBar.GONE) {
					loadingProgressBar.setVisibility(ProgressBar.VISIBLE);

				}
				loadingProgressBar.setProgress(progress);
				if (progress == 100) {
					loadingProgressBar.setVisibility(ProgressBar.GONE);

				}
			}
		});

		// wv.getSettings().setSupportZoom(true);
		// wv.getSettings().setBuiltInZoomControls(true);
		// wv.requestFocus(View.FOCUS_DOWN);

		wv.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.e("Webview ", "Touched--");

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

		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				wv.reload();
				// TODO Auto-generated method stub

			}
		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				wv.goForward();
				// TODO Auto-generated method stub

			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				wv.goBack();
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("On Pause MyWebView ", " on Pause ");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("On onResume MyWebView ", "  onResume ");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	// // To handle "Back" key press event for WebView to go back to
	// previous screen.
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event)
	// {
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
	// wv.goBack();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

}
