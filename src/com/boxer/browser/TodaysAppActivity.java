package com.boxer.browser;

import com.boxer.browser.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.SlidingDrawer;

public class TodaysAppActivity extends Activity implements ActionBar.OnNavigationListener  {
	// action bar
		public ActionBar actionBar;
		@SuppressWarnings("deprecation")
		SlidingDrawer slider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todays_app);
		
		 slider = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
		
		actionBar = getActionBar();

		// Hide the action bar title
		//actionBar.setDisplayShowTitleEnabled(false);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.todayapp_action_menu, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_location_found).getActionView();
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}


}
