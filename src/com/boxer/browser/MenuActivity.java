package com.boxer.browser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boxer.util.Constant;
import com.boxer.util.HashGenerator;

public class MenuActivity extends Activity {
	RadioButton rbtn;
	RelativeLayout rl;
	String appName = "";
	String email = "";
	String feedback = "";
	String AID = "";
	// edit text for feed back
	EditText editTextUserEmail, editTextFeedback;
	Typeface tfSemiBoldItalic;
	Typeface tfRegular;
	Typeface tfPlain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_menu);
		tfSemiBoldItalic = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebBold);
		tfRegular = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebItalic);
		tfPlain = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebRegular);
		AID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		rl = (RelativeLayout) findViewById(R.id.IncludedLayOut);
		handleFooter();
		handleMenu();
		handleLayoutClicks();

	}

	void handleFooter() {
		// final ImageView ivBack = (ImageView)
		// findViewById(R.id.imageView_BackwardIcon);
		// final ImageView ivNext = (ImageView)
		// findViewById(R.id.imageView_ForWardIcon);
		final ImageView ivTab = (ImageView) findViewById(R.id.ivTabIcon);
		final ImageView ivSetting = (ImageView) findViewById(R.id.settingIcon);
		final ImageView ivHome = (ImageView) findViewById(R.id.imageView_Home);
		// ivBack.setVisibility(View.INVISIBLE);
		// ivNext.setVisibility(View.INVISIBLE);
		ivSetting.setSelected(true);
		// ivNext.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// ivTab.setSelected(false);
		// ivSetting.setSelected(false);
		// ivHome.setSelected(false);
		//
		//
		// }
		// });
		// ivBack.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// ivTab.setSelected(false);
		// ivSetting.setSelected(false);
		// ivHome.setSelected(false);
		//
		//
		// }
		// });
		ivSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);

				MenuActivity.this.finish();

			}
		});
		ivTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);

				MenuActivity.this.finish();

			}
		});
		ivHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				MenuActivity.this.finish();
			}
		});
	} 

	void handleMenu() {

		TextView tvDefaultEngine = (TextView) findViewById(R.id.tvDefaultEngine);
		TextView tvHelp = (TextView) findViewById(R.id.tvHelp);
		TextView tvPrivacyAndPolicy = (TextView) findViewById(R.id.tvPrivacyAndPolicy);
		TextView tvTermsAndConditions = (TextView) findViewById(R.id.tvTermsAndConditions);
		TextView tvAbout = (TextView) findViewById(R.id.tvAbout);
		TextView tvFeedback = (TextView) findViewById(R.id.tvFeedback);
		TextView tvSettings = (TextView) findViewById(R.id.tvSettings);
		TextView tvRateUS = (TextView) findViewById(R.id.tvRateUS);

		TextView tvDefaultEngineSub = (TextView) findViewById(R.id.tvDefaultEngineSub);
		TextView tvHelpSub = (TextView) findViewById(R.id.tvHelpSub);
		TextView tvPrivacyAndPolicySub = (TextView) findViewById(R.id.tvPrivacyAndPolicySub);
		TextView tvTermsAndConditionsSub = (TextView) findViewById(R.id.tvTermsAndConditionsSub);
		TextView tvAboutSub = (TextView) findViewById(R.id.tvAboutSub);
		TextView tvFeedbackSub = (TextView) findViewById(R.id.tvFeedbackSub);
		TextView tvSettingsSub = (TextView) findViewById(R.id.tvSettingsSub);
		TextView tvRateUSsub = (TextView) findViewById(R.id.tvRateUSSub);

		LinearLayout llDefaultEngone = (LinearLayout) findViewById(R.id.llDefaultEngine);
		LinearLayout llAbout = (LinearLayout) findViewById(R.id.llAbout);
		LinearLayout llPnP = (LinearLayout) findViewById(R.id.llPnP);
		LinearLayout llFeedback = (LinearLayout) findViewById(R.id.llFeedback);
		LinearLayout llTnC = (LinearLayout) findViewById(R.id.llTnC);
		LinearLayout llSetting = (LinearLayout) findViewById(R.id.llSetting);
		LinearLayout llHelp = (LinearLayout) findViewById(R.id.llHelp);
		LinearLayout llRateUs = (LinearLayout) findViewById(R.id.llRateUS);

		tvRateUS.setTypeface(tfSemiBoldItalic);
		tvRateUSsub.setTypeface(tfRegular);

		tvAbout.setTypeface(tfSemiBoldItalic);
		tvAboutSub.setTypeface(tfRegular);
		tvDefaultEngine.setTypeface(tfSemiBoldItalic);
		tvDefaultEngineSub.setTypeface(tfRegular);
		tvFeedback.setTypeface(tfSemiBoldItalic);
		tvFeedbackSub.setTypeface(tfRegular);
		tvHelp.setTypeface(tfSemiBoldItalic);
		tvHelpSub.setTypeface(tfRegular);
		tvPrivacyAndPolicy.setTypeface(tfSemiBoldItalic);
		tvPrivacyAndPolicySub.setTypeface(tfRegular);
		tvSettings.setTypeface(tfSemiBoldItalic);
		tvSettingsSub.setTypeface(tfRegular);
		tvTermsAndConditions.setTypeface(tfSemiBoldItalic);
		tvTermsAndConditionsSub.setTypeface(tfRegular);

		llSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				MenuActivity.this.finish();
				startActivity(new Intent(MenuActivity.this,
						B_SettingsActivity.class));

			}
		});
		llDefaultEngone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Dialog dialogSearch = new Dialog(MenuActivity.this);

				dialogSearch.setTitle("Search engines");
				dialogSearch.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogSearch.setContentView(R.layout.dialog_searchengine);
				DisplayMetrics metrics = getApplicationContext().getResources()
						.getDisplayMetrics();

				int width = metrics.widthPixels;
				int height = metrics.heightPixels;
				dialogSearch.getWindow().setLayout((7 * width) / 9,
						(4 * height) / 7);
				dialogSearch.show();

				SharedPreferences sharedPreferences1 = PreferenceManager
						.getDefaultSharedPreferences(MenuActivity.this);
				MainActivity.engine = sharedPreferences1.getString(
						"defaultengine", Constant.defaultEngine);
				Log.e("shared pref", MainActivity.engine);

				TextView textView1 = (TextView) dialogSearch
						.findViewById(R.id.textView1);
				textView1.setTypeface(tfSemiBoldItalic);
				final RadioGroup rg = (RadioGroup) dialogSearch
						.findViewById(R.id.radiogrp);

				RadioButton rbGoogle = (RadioButton) dialogSearch
						.findViewById(R.id.radiogoogle);
				rbGoogle.setTypeface(tfPlain);
				RadioButton rbask = (RadioButton) dialogSearch
						.findViewById(R.id.radioask);
				rbask.setTypeface(tfPlain);
				RadioButton rbbing = (RadioButton) dialogSearch
						.findViewById(R.id.radioBing);
				rbbing.setTypeface(tfPlain);
				RadioButton rbyahoo = (RadioButton) dialogSearch
						.findViewById(R.id.radioyahoo);
				rbyahoo.setTypeface(tfPlain);

				if (MainActivity.engine.equalsIgnoreCase("google")) {
					// Toggle status of checkbox selection
					rbGoogle.setChecked(true);
				} else if (MainActivity.engine.equalsIgnoreCase("bing")) {
					rbbing.setChecked(true);

				} else if (MainActivity.engine.equalsIgnoreCase("yahoo")) {
					rbyahoo.setChecked(true);

				}

				else if (MainActivity.engine.equalsIgnoreCase("ask")) {
					rbask.setChecked(true);

				}

				Button cancelbtn = (Button) dialogSearch
						.findViewById(R.id.btncancel);
				cancelbtn.setTypeface(tfSemiBoldItalic);
				cancelbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogSearch.dismiss();

					}
				});

				Button okbtn = (Button) dialogSearch.findViewById(R.id.btnok);
				okbtn.setTypeface(tfSemiBoldItalic);
				okbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						rbtn = (RadioButton) dialogSearch.findViewById(rg
								.getCheckedRadioButtonId());
						String valueradio = rbtn.getText().toString();
						String lower = valueradio.toLowerCase();

						// System.out.println(lower);

						SharedPreferences sharedPreferences = PreferenceManager
								.getDefaultSharedPreferences(MenuActivity.this);
						Editor editor = sharedPreferences.edit();
						editor.putString("defaultengine", lower);
						if (MainActivity.engine.equalsIgnoreCase("google")) {
							// Toggle status of checkbox selection
							editor.putInt(PreferenceConstants.SEARCH, 1);
						} else if (MainActivity.engine.equalsIgnoreCase("bing")) {
							editor.putInt(PreferenceConstants.SEARCH, 3);
						} else if (MainActivity.engine
								.equalsIgnoreCase("yahoo")) {
							editor.putInt(PreferenceConstants.SEARCH, 4);
						}

						else if (MainActivity.engine.equalsIgnoreCase("ask")) {
							editor.putInt(PreferenceConstants.SEARCH, 2);
						}
						editor.commit();

						dialogSearch.dismiss();
						rl.setVisibility(View.INVISIBLE);
						MenuActivity.this.finish();
					}
				});
				//
			}

		});

		llTnC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, License.class);
				startActivity(intent);
				MenuActivity.this.finish();
			}
		});
		llPnP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, PrivecyAndPolicy.class);
				startActivity(intent);
				MenuActivity.this.finish();
			}
		});
		llHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, Help.class);
				startActivity(intent);
				MenuActivity.this.finish();
			}
		});

		llRateUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				Uri uri = Uri.parse("market://details?id=" + MenuActivity.this.getPackageName());
			    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
			    // To count with Play market backstack, After pressing back button, 
			    // to taken back to our application, we need to add following flags to intent. 
			    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
			                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
			                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			    try {
			        startActivity(goToMarket);
			    } catch (ActivityNotFoundException e) {
			        startActivity(new Intent(Intent.ACTION_VIEW,
			                Uri.parse("http://play.google.com/store/apps/details?id=" + MenuActivity.this.getPackageName())));
			    }
				MenuActivity.this.finish();
			}
		});

		llFeedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFeedBack();

			}
		});
		llAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, About.class);
				startActivity(intent);
				MenuActivity.this.finish();

			}
		});

	}

	void handleLayoutClicks() {
		LinearLayout llUpish = (LinearLayout) findViewById(R.id.llUpish);
		LinearLayout llSide = (LinearLayout) findViewById(R.id.llSide);
		LinearLayout llMain = (LinearLayout) findViewById(R.id.llMain);
		llUpish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				MenuActivity.this.finish();
			}
		});
		llSide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				MenuActivity.this.finish();
			}
		});
		llMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.INVISIBLE);
				MenuActivity.this.finish();
			}
		});

	}

	void showFeedBack() {

		final Dialog dialog = new Dialog(MenuActivity.this);

		dialog.setTitle(" Feed back");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_feedback);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.show();
		TextView tvHeader = (TextView) dialog
				.findViewById(R.id.textView_Feedback);
		tvHeader.setTypeface(tfSemiBoldItalic);

		EditText editTextAppName = (EditText) dialog
				.findViewById(R.id.editText_AppName);

		editTextAppName.setTypeface(tfRegular);
		appName = editTextAppName.getText().toString();
		editTextUserEmail = (EditText) dialog
				.findViewById(R.id.editText_UserEmail);
		editTextUserEmail.setTypeface(tfRegular);

		email = editTextUserEmail.getText().toString();
		editTextFeedback = (EditText) dialog
				.findViewById(R.id.editText_writeFeedback);
		editTextFeedback.setTypeface(tfRegular);
		feedback = editTextFeedback.getText().toString();
		final Button submitFeedBack = (Button) dialog
				.findViewById(R.id.button_submitFeedBack);
		submitFeedBack.setTypeface(tfSemiBoldItalic);
		submitFeedBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean validationResult = formValidator();
				if (validationResult == false) {
					return;
				}
				boolean check = isNetworkAvailable();
				if (check != true) {
					Intent intent = new Intent(MenuActivity.this,
							NoInternetConnection.class);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_right);
					finish();
					return;
				} else {
					rl.setVisibility(View.INVISIBLE);
					new FeedbackAsyncTask().execute();
					Toast.makeText(getApplicationContext(), "Feedback Sent",
							Toast.LENGTH_SHORT).show();
					editTextFeedback.setText("");
					editTextUserEmail.setText("");
					dialog.dismiss();
				}

			}

			private boolean formValidator() {
				String strFeedback = editTextFeedback.getText().toString()
						.trim();
				String strEmail = editTextUserEmail.getText().toString().trim();

				boolean b3 = isEmpty(strFeedback, editTextFeedback,
						"You can't leave this empty.");
				boolean b2 = isValid(strEmail, editTextUserEmail,
						"Enter valid email.");
				boolean b1 = isEmpty(strEmail, editTextUserEmail,
						"You can't leave this empty.");

				if (b1 && b2 && b3) {
					return true;
				}

				return false;
			}

			private boolean isValid(String strEmail,
					EditText editTextUserEmail, String errorMsg) {

				String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
				int ecolor = Color.RED;
				String estring = errorMsg;

				ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
				SpannableStringBuilder msg = new SpannableStringBuilder(estring);
				msg.setSpan(fgcspan, 0, estring.length(), 0);
				CharSequence inputStr = strEmail;
				Pattern pattern = Pattern.compile(expression,
						Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(inputStr);
				if (matcher.matches()) {
					return true;
				} else {
					editTextUserEmail.setError(msg);
					return false;
				}
			}

			private boolean isEmpty(CharSequence charSequence,
					EditText feedback, String errorMsg) {
				if (charSequence.equals("")) {
					int ecolor = Color.RED;
					String estring = errorMsg;
					ForegroundColorSpan fgcspan = new ForegroundColorSpan(
							ecolor);
					SpannableStringBuilder msg = new SpannableStringBuilder(
							estring);
					msg.setSpan(fgcspan, 0, estring.length(), 0);
					feedback.setError(msg);
					return false;
				} else {
					return true;
				}

			}
		});
	}

	class FeedbackAsyncTask extends AsyncTask<Void, Void, Void> {
		public void postData() {
			// Toast.makeText(getApplicationContext(), ""+email+" "+feedback,
			// Toast.LENGTH_SHORT).show();

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost httppost = new
			// HttpPost(Constant.serverPath+"/feedback.php");
			HttpPost httppost = new HttpPost(Constant.serverPath
					+ "/feedbackdb.php");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("app_name", ""));
				nameValuePairs.add(new BasicNameValuePair("email",
						editTextUserEmail.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("feed",
						editTextFeedback.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("AID", HashGenerator
						.encrypt(AID, Constant.EncriptionKey)));
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

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
