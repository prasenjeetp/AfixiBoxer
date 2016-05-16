package com.boxer.browser;

import com.boxer.browser.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class License extends Activity {
	WebView wv;
	private ProgressDialog progDailog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.license);
		wv = (WebView) findViewById(R.id.webview);

		progDailog = ProgressDialog.show(License.this, "Loading",
				"Please wait...", true);
		progDailog.setCancelable(false);

		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				progDailog.show();
				view.loadUrl(url);

				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				progDailog.dismiss();
			}
		});

		wv.loadUrl("http://bryanmaxim.com/boxer/eula.html");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.license, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// setResult(Activity.RESULT_OK, intent);
		overridePendingTransition(R.anim.slide_in_dialog,
				R.anim.slide_out_right_dialog);
		finish();

	}
}
