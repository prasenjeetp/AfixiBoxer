package com.boxer.browser;

import java.net.MalformedURLException;
import java.net.URL;

import com.boxer.browser.R;import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainSearch extends TabActivity implements OnClickListener {
	
	int tab=1;
	private static TabHost tabHost;
	private EditText googleSearch;
	 // private final static String DEFAULT_URL = "http://www.google.com/";
	private int z = 0;
	ImageView addTab,removeTab,option;
	int position=0;
	private String SanTest=null;
	private String URL="";
	private Bundle webViewBundle;
    String url="";
    String activity="";
    static String tabName="";
    String tabCount="";
    private final static String DEFAULT_URL = "http://www.google.com/";
	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	   requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.main_search);
	  Bundle extras = getIntent().getExtras();
	  if (extras!= null) {
		   activity=extras.getString("activity");
			if(extras.getString("activity").equals("MainActivity")){
				this.URL = extras.getString("url");
				//showSearchResult(URL);
			}
		
			
		} 
	
	this.tabHost = getTabHost(); // The activity TabHost
	googleSearch = (EditText) findViewById(R.id.editText_searchText);
	
    //option = (ImageView) findViewById(R.id.imageView_option);
    //option.setOnClickListener(this);
	
	//this.addressBar.setText(DEFAULT_URL);
	//String url = "https://www.google.co.in/?gfe_rd=ctrl&ei=4j4DU_KsCOaJ8QfyhYHYCQ&gws_rd=cr#q="+googleSearch.getText().toString();
	    					
	
	addTab = (ImageView) findViewById(R.id.imageView_addTab);
	removeTab = (ImageView) findViewById(R.id.imageView_deleteTab);
	
	removeTab.setOnClickListener(this);
	
	addTab.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
	String s="";
	  addMethod();  // Method which adds the Tab Host
	}
	});
	Intent openBrowser = new Intent();
	openBrowser.setClass(MainSearch.this, BrowserActivity.class);
	openBrowser.putExtra("URL", URL);
	openBrowser.putExtra("activity",activity);
	SanTest=Intent.CATEGORY_LAUNCHER;
	Log.d("SanTest",SanTest);
	
	TabHost tabHost = getTabHost();  // The activity TabHost
    TabHost.TabSpec spec; 
    String urlshort=getHost(URL);
    View tabView = createTabView(this, urlshort);
    
 	 spec = tabHost.newTabSpec(""+tabCount).setIndicator(tabView)
               .setContent(openBrowser);
       tabHost.addTab(spec);
	//tabHost.addTab(tabHost.newTabSpec("1").setIndicator(getHost(URL)).setContent(openBrowser));
   	 position = tabHost.getCurrentTab();
    //Toast.makeText(getApplicationContext(), "c ="+position, Toast.LENGTH_LONG ).show();

	   tabHost = getTabHost();
      // tabHost.setOnTabChangedListener(this);
}
	
	public void addMethod() {

		/*System.out.println(" tab ");
		String act="MainSearch";
        Intent openBrowser = new Intent();
		openBrowser.setClass(this,BrowserActivity.class);
		openBrowser.putExtra("activity",act);
		tabCount = Integer.toString(tab=tab+1);
		int position = tabHost.getCurrentTab();
		tabHost.addTab(tabHost.newTabSpec(""+tabCount).setIndicator("New Tab").setContent(openBrowser));
		Log.d("z",Integer.toString(z));
		int count=tabHost.getCurrentTab();
		tabHost.setCurrentTab(count+1);
		++z;*/
		String act="MainSearch";
		Intent openBrowser = new Intent();
		openBrowser.setClass(this,BrowserActivity.class);
		openBrowser.putExtra("activity",act);
		tabCount = Integer.toString(tab=tab+1);
		 TabHost tabHost = getTabHost();  // The activity TabHost
	        TabHost.TabSpec spec; 
		 View tabView = createTabView(this, "New Tab");
		 spec = tabHost.newTabSpec(""+position).setIndicator(tabView).setContent(openBrowser);
	        tabHost.addTab(spec);
	        position = tabHost.getCurrentTab();
			tabHost.setCurrentTab(position+1);
			++z;
		 	
			//Toast.makeText(getApplicationContext(), "c ="+position, Toast.LENGTH_LONG ).show();
		
		}
	
	 private static View createTabView(Context context, String tabText) {
	        View view = LayoutInflater.from(context).inflate(R.layout.customtab, null, false);
	        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
	        tv.setText(tabText);
	        return view;
	    }

	/*public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
	    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dddddd"));
		}

		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#f2f2f2"));
	}*/
	public void addMethod1(BrowserActivity browserActivity,String s) {

		System.out.println(" tab "+s);
		
		
		//String url = "https://www.google.co.in/?gfe_rd=ctrl&ei=4j4DU_KsCOaJ8QfyhYHYCQ&gws_rd=cr#q="+googleSearch.getText().toString();
		
		//String webSiteURL = validateURL(googleSearch.getText().toString().trim());
		//String webSiteName = getHost(webSiteURL);
        String act="MainSearch";
        //System.out.println(" tab1 ");
		
		Intent openBrowser = new Intent();
		//System.out.println(" tab2 ");
		
		openBrowser.setClass(browserActivity,BrowserActivity.class);
		//System.out.println(" tab3 ");
		openBrowser.putExtra("activity",act);
		//System.out.println(" tab4 ");
		openBrowser.putExtra("URL", s);
		
		
		//System.out.println(" tab5 ");
		
		tabHost.addTab(tabHost.newTabSpec(""+tabCount).setIndicator(""+tabCount).setContent(openBrowser));
		//System.out.println(" tab6 ");
		Log.d("z",Integer.toString(z));
		//int count=tabHost.getCurrentTab();
		//tabHost.setCurrentTab(count+1);
		++z;
		
		}
	
	
	private void deleteMethod() {
	
		// Since we can't really delete a TAB
		// We hide it
	
		int position = tabHost.getCurrentTab();
		Log.d("Position",Integer.toString(position));
	
	
		// if (position != 0 ) {
		//
		// tabHost.getCurrentTabView().setVisibility(1);
		// tabHost.setCurrentTab(position-1);
		//
		// }
		// else if(position== z){
		// tabHost.getCurrentTabView().setVisibility(1);
		// tabHost.setCurrentTab(position+1);
		// }
		Log.d("Z val in delete()",Integer.toString(z));
		if(position >0)
		{
		tabHost.getCurrentTabView().setVisibility(View.GONE);
		tabHost.setCurrentTab(position+1);
		z-=1;
		if(z<0)
		z=0;
		}
		else if(position == 0)
		{
		tabHost.getCurrentTabView().setVisibility(View.GONE);
		tabHost.setCurrentTab(position+1);
		z=0;
		}
		else if(position == z)
		{
		tabHost.getCurrentTabView().setVisibility(View.GONE);
		tabHost.setCurrentTab(z-1);
		Log.d("Z value in final","lol");
		Log.d("Pos",Integer.toString(position));
		Log.d("z pos",Integer.toString(z));
	
	
		}
	
	
		}
	
	private String validateURL(String url) {
	
	StringBuffer urlB = new StringBuffer();
	
	// checks if addressBar has a valid URL
	// you can add stuff here in order to validate
	// this is just an example
	if (url.startsWith("http://")) {urlB.append(url);} else {urlB.append("http://");}
	
	try {
	URL urlTry = new URL(urlB.toString());
	
	return urlB.toString();
	
	} catch (MalformedURLException e) {
	
	return "http://www.google.com/";
	}
	}
		
	private String getHost(String url) {
	
	try {
	
	URL urlTry = new URL(url);
	
	return urlTry.getHost().replace("www.", "").replace(".com", "").replace(".org", "").replace(".net", "");
	
	} catch (MalformedURLException e) {
	
	return "";
	}
	//Inflates menu when "menu Key" is pressed
	}

	@Override
	public void onClick(View view) {

		
		if(view==removeTab){
			
			
			int count=tabHost.getCurrentTab();
			//int c=getTabHost().getTabWidget().getTabCount(); 
			 position = tabHost.getCurrentTab();
			//Toast.makeText(getApplicationContext(), ""+position,Toast.LENGTH_SHORT).show();
			if(position==0){
				tabHost.getCurrentTabView().setVisibility(View.VISIBLE);
				//Toast.makeText(getApplicationContext(), "c ="+position, Toast.LENGTH_LONG ).show();
				//tabHost.setCurrentTab(0);
			return;
			}if(position>0){
			
				System.out.println("p ="+count);
				tabHost.getCurrentTabView().setVisibility(View.GONE);
				tabHost.setCurrentTab(position-1);
				//Toast.makeText(getApplicationContext(), "c ="+position, Toast.LENGTH_LONG ).show();
				//Toast.makeText(getApplicationContext(), "Deleted",Toast.LENGTH_SHORT).show();
				
			}if(position==1){
			
				System.out.println("p ="+count);
				tabHost.getCurrentTabView().setVisibility(View.VISIBLE);
				tabHost.setCurrentTab(0);
				//Toast.makeText(getApplicationContext(), "c ="+position, Toast.LENGTH_LONG ).show();
				
				//Toast.makeText(getApplicationContext(), "Deleted",Toast.LENGTH_SHORT).show();
				
			}
			
			//tabHost.setCurrentTab(position+1);
		}
	}
	
	public void tabName(String url){
		int count=tabHost.getCurrentTab();
		 TextView tv = (TextView)tabHost.getTabWidget().getChildAt(count).findViewById(R.id.tabTitleText); 
		 String s= getHost(url);
		 tv.setText(s);
		 //String tabName=str;
		/*int count=tabHost.getCurrentTab();
		System.out.println("c ="+count);
		//String s=tabHost.getCurrentTabView().getTa
				 TextView tabContent = (TextView) tabHost.getTabContentView().getChildAt(count);
				 System.out.println("t ="+tabContent);		 
		  //TextView textView1 = (TextView) tabHost.getCurrentTab();
		  Toast.makeText(getApplicationContext(), ""+tabContent,Toast.LENGTH_SHORT).show();
		   // textView1.setText("This Tab is Selected");
		tabContent.setText("keshav");*/
		
		
		 //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab());
		
		
	}

	@Override
	public void onBackPressed() {
	  
		
		super.onBackPressed();
//		Toast.makeText(getApplicationContext(), "hello",Toast.LENGTH_SHORT).show();
	  
				Intent i=new Intent(MainSearch.this,MainActivity.class);
				  	    startActivity(i);
					     
	}
	       

	
	
	
	}
