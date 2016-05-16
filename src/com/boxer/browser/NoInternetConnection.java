package com.boxer.browser;

import com.boxer.browser.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class NoInternetConnection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //Hides the titlebar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.no_internet_connection);
	}

	
	 @Override
	    public void onBackPressed() {
		 NoInternetConnection.super.onBackPressed();
	        //setResult(Activity.RESULT_OK, intent);
		 // overridePendingTransition(R.anim.slide_in_dialog, R.anim.slide_out_right_dialog);
	      finish();
	       
	    }
}
