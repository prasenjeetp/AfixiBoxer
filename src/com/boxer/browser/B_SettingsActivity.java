/*
 * Copyright 2014 A.C.R. Development
 */
package com.boxer.browser;

import info.guardianproject.onionkit.ui.OrbotHelper;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.boxer.util.Constant;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class B_SettingsActivity extends Activity {

	private static int API = android.os.Build.VERSION.SDK_INT;

	private SharedPreferences.Editor mEditPrefs;

	private int mAgentChoice;
	RadioButton rbtn;
	private String mHomepage;

	private TextView mAgentTextView;

	private TextView mDownloadTextView;

	private int mEasterEggCounter;

	private String mDownloadLocation;

	private TextView mHomepageText;

	private SharedPreferences mPreferences;

	private TextView mSearchText;

	private Context mContext;

	private Activity mActivity;
	Typeface tfSemiBold, tfRegular;
	Typeface tfPlain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b_settings);
		mContext = this;
		mActivity = this;
		tfSemiBold = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebBold);
		tfRegular = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebItalic);
		tfPlain = Typeface.createFromAsset(this.getAssets(),
				Constant.FontTitilliumWebRegular);
		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}

	@SuppressLint("NewApi")
	public void init() {
		// mPreferences storage
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		mPreferences = getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
		if (mPreferences.getBoolean(PreferenceConstants.HIDE_STATUS_BAR, false)) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		mEditPrefs = mPreferences.edit();

		// initialize UI
		RelativeLayout layoutLocation = (RelativeLayout) findViewById(R.id.layoutLocation);
		RelativeLayout layoutFullScreen = (RelativeLayout) findViewById(R.id.layoutFullScreen);
		RelativeLayout layoutFlash = (RelativeLayout) findViewById(R.id.layoutFlash);
		RelativeLayout layoutBlockAds = (RelativeLayout) findViewById(R.id.layoutAdBlock);
		RelativeLayout layoutOrbot = (RelativeLayout) findViewById(R.id.layoutUseOrbot);
		layoutLocation.setVisibility(View.GONE);
		layoutFullScreen.setVisibility(View.GONE);
		layoutBlockAds.setVisibility(View.GONE);
		layoutOrbot.setVisibility(View.GONE);

		mSearchText = (TextView) findViewById(R.id.searchText);

		switch (mPreferences.getInt(PreferenceConstants.SEARCH, 1)) {
		case 0:
			mSearchText.setText(getResources().getString(R.string.custom_url));
			break;
		case 1:
			mSearchText.setText("Google");
			break;
		case 2:
			mSearchText.setText("ask");
			break;
		case 3:
			mSearchText.setText("Bing");
			break;
		case 4:
			mSearchText.setText("Yahoo");
			break;
		case 5:
			mSearchText.setText("StartPage");
			break;
		case 6:
			mSearchText.setText("StartPage (Mobile)");
			break;
		case 7:
			mSearchText.setText("DuckDuckGo");
			break;
		case 8:
			mSearchText.setText("DuckDuckGo Lite");
			break;
		case 9:
			mSearchText.setText("Baidu");
			break;
		case 10:
			mSearchText.setText("Yandex");
		}

		mAgentTextView = (TextView) findViewById(R.id.agentText);
		// mHomepageText = (TextView) findViewById(R.id.homepageText);
		mDownloadTextView = (TextView) findViewById(R.id.downloadText);
		if (API >= 19) {
			mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
			mEditPrefs.commit();
		}
		boolean locationBool = mPreferences.getBoolean(
				PreferenceConstants.LOCATION, false);
		int flashNum = mPreferences.getInt(
				PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
		boolean fullScreenBool = mPreferences.getBoolean(
				PreferenceConstants.FULL_SCREEN, false);
		mAgentChoice = mPreferences.getInt(PreferenceConstants.USER_AGENT, 1);
		mHomepage = mPreferences.getString(PreferenceConstants.HOMEPAGE,
				B_Constants.HOMEPAGE);
		mDownloadLocation = mPreferences.getString(
				PreferenceConstants.DOWNLOAD_DIRECTORY,
				Environment.DIRECTORY_DOWNLOADS);

		mDownloadTextView.setText(B_Constants.EXTERNAL_STORAGE + '/'
				+ mDownloadLocation);

		String code = "HOLO";

		try {
			PackageInfo p = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			code = p.versionName;
		} catch (NameNotFoundException e) {
			// TODO add logging
			e.printStackTrace();
		}

		if (mHomepage.contains("about:home")) {
			/*
			 * mHomepageText.setText(getResources().getString(
			 * R.string.action_homepage));
			 */
		} else if (mHomepage.contains("about:blank")) {
			/*
			 * mHomepageText.setText(getResources().getString(
			 * R.string.action_blank))
			 */;
		} else if (mHomepage.contains("about:bookmarks")) {
			/*
			 * mHomepageText.setText(getResources().getString(
			 * R.string.action_bookmarks));
			 */
		} else {
			// mHomepageText.setText(mHomepage);
		}

		switch (mAgentChoice) {
		case 1:
			mAgentTextView.setText(getResources().getString(
					R.string.agent_default));
			break;
		case 2:
			mAgentTextView.setText(getResources().getString(
					R.string.agent_desktop));
			break;
		case 3:
			mAgentTextView.setText(getResources().getString(
					R.string.agent_mobile));
			break;
		case 4:
			mAgentTextView.setText(getResources().getString(
					R.string.agent_custom));
		}
		RelativeLayout r1, r2, r3, r4, r5, licenses;
		r1 = (RelativeLayout) findViewById(R.id.setR1);
		r2 = (RelativeLayout) findViewById(R.id.setR2);
		r3 = (RelativeLayout) findViewById(R.id.setR3);
		r4 = (RelativeLayout) findViewById(R.id.setR4);
		r5 = (RelativeLayout) findViewById(R.id.setR5);

		Switch location = new Switch(this);
		Switch fullScreen = new Switch(this);
		Switch flash = new Switch(this);
		Switch adblock = new Switch(this);
		Switch orbot = new Switch(this);

		r1.addView(location);
		r2.addView(fullScreen);
		r3.addView(flash);
		r4.addView(adblock);
		r5.addView(orbot);
		location.setChecked(locationBool);
		fullScreen.setChecked(fullScreenBool);
		if (flashNum > 0) {
			flash.setChecked(true);
		} else {
			flash.setChecked(false);
		}
		adblock.setChecked(mPreferences.getBoolean(
				PreferenceConstants.BLOCK_ADS, false));
		orbot.setChecked(mPreferences.getBoolean(PreferenceConstants.USE_PROXY,
				false));

		initSwitch(location, fullScreen, flash, adblock, orbot);
		clickListenerForSwitches(layoutLocation, layoutFullScreen, layoutFlash,
				layoutBlockAds, layoutOrbot, location, fullScreen, flash,
				adblock, orbot);

		RelativeLayout agent = (RelativeLayout) findViewById(R.id.layoutUserAgent);
		RelativeLayout download = (RelativeLayout) findViewById(R.id.layoutDownload);
		TextView tvDownLoad = (TextView) findViewById(R.id.textView6);
		TextView tvDownLoadSmall = (TextView) findViewById(R.id.downloadText);
		tvDownLoad.setTypeface(tfSemiBold);
		tvDownLoadSmall.setTypeface(tfRegular);

		TextView tvAdvanceSetting = (TextView) findViewById(R.id.textView8);
		tvAdvanceSetting.setTypeface(tfSemiBold);
		// RelativeLayout homepage = (RelativeLayout)
		// findViewById(R.id.layoutHomepage);
		RelativeLayout advanced = (RelativeLayout) findViewById(R.id.layoutAdvanced);
		// RelativeLayout source = (RelativeLayout)
		// findViewById(R.id.layoutSource);

		agent(agent);
		download(download);
		// homepage(homepage);
		advanced(advanced);
		// source(source);
		search();
		easterEgg();
	}

	public void search() {
		RelativeLayout search = (RelativeLayout) findViewById(R.id.layoutSearch);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder picker = new AlertDialog.Builder(mActivity);
				picker.setTitle(getResources().getString(
						R.string.title_search_engine));
				final CharSequence[] chars = {

				"Google", "Ask", "Bing", "Yahoo", };

				int n = mPreferences.getInt(PreferenceConstants.SEARCH, 1);

				picker.setSingleChoiceItems(chars, n,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mEditPrefs.putInt(PreferenceConstants.SEARCH,
										which + 1);
								mEditPrefs.putString("defaultengine",
										String.valueOf(chars[which]));
								mEditPrefs.commit();
								switch (which) {
								case 0:
									searchUrlPicker();
									break;
								case 1:
									mSearchText.setText("Google");
									break;
								case 2:
									mSearchText.setText("Android Search");
									break;
								case 3:
									mSearchText.setText("Bing");
									break;
								case 4:
									mSearchText.setText("Yahoo");
									break;
								case 5:
									mSearchText.setText("StartPage");
									break;
								case 6:
									mSearchText.setText("StartPage (Mobile)");
									break;
								case 7:
									mSearchText.setText("DuckDuckGo");
									break;
								case 8:
									mSearchText.setText("DuckDuckGo Lite");
									break;
								case 9:
									mSearchText.setText("Baidu");
									break;
								case 10:
									mSearchText.setText("Yandex");
								}
							}
						});
				picker.setNeutralButton(
						getResources().getString(R.string.action_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				picker.show();
			}

		});
	}

	public void searchUrlPicker() {
		final AlertDialog.Builder urlPicker = new AlertDialog.Builder(this);

		urlPicker.setTitle(getResources().getString(R.string.custom_url));
		final EditText getSearchUrl = new EditText(this);

		String mSearchUrl = mPreferences.getString(
				PreferenceConstants.SEARCH_URL, B_Constants.GOOGLE_SEARCH);
		getSearchUrl.setText(mSearchUrl);
		urlPicker.setView(getSearchUrl);
		urlPicker.setPositiveButton(getResources()
				.getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getSearchUrl.getText().toString();
						mEditPrefs.putString(PreferenceConstants.SEARCH_URL,
								text);
						mEditPrefs.commit();
						mSearchText.setText(getResources().getString(
								R.string.custom_url)
								+ ": " + text);
					}
				});
		urlPicker.show();
	}

	public void clickListenerForSwitches(RelativeLayout one,
			RelativeLayout two, RelativeLayout three,
			RelativeLayout layoutBlockAds, RelativeLayout layoutOrbot,
			final Switch loc, final Switch full, final Switch flash,
			final Switch adblock, final Switch orbot) {
		layoutBlockAds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adblock.setChecked(!adblock.isChecked());
			}

		});
		one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loc.setChecked(!loc.isChecked());
			}

		});
		two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				full.setChecked(!full.isChecked());
			}

		});
		three.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (API < 19) {
					flash.setChecked(!flash.isChecked());
				} else {
					B_Utils.createInformativeDialog(mContext, getResources()
							.getString(R.string.title_warning), getResources()
							.getString(R.string.dialog_adobe_dead));
				}
			}

		});
		layoutOrbot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (orbot.isEnabled()) {
					orbot.setChecked(!orbot.isChecked());
				} else {
					B_Utils.showToast(mContext,
							getResources().getString(R.string.install_orbot));
				}
			}

		});
	}

	public void easterEgg() {
		// RelativeLayout easter = (RelativeLayout)
		// findViewById(R.id.layoutVersion);
		// easter.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {
		// mEasterEggCounter++;
		// if (mEasterEggCounter == 10) {
		//
		// startActivity(new Intent(
		// Intent.ACTION_VIEW,
		// Uri.parse("http://imgs.xkcd.com/comics/compiling.png"),
		// mContext, B_MainActivity.class));
		// finish();
		// mEasterEggCounter = 0;
		// }
		// }
		//
		// });
	}

	public void initSwitch(Switch location, Switch fullscreen, Switch flash,
			Switch adblock, Switch orbot) {
		adblock.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.BLOCK_ADS, isChecked);
				mEditPrefs.commit();
			}

		});
		location.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.LOCATION, isChecked);
				mEditPrefs.commit();

			}

		});
		flash.setEnabled(API < 19);
		flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					getFlashChoice();
				} else {
					mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT,
							0);
					mEditPrefs.commit();
				}

				boolean flashInstalled = false;
				try {
					PackageManager pm = getPackageManager();
					ApplicationInfo ai = pm.getApplicationInfo(
							"com.adobe.flashplayer", 0);
					if (ai != null) {
						flashInstalled = true;
					}
				} catch (NameNotFoundException e) {
					flashInstalled = false;
				}
				if (!flashInstalled && isChecked) {
					B_Utils.createInformativeDialog(
							B_SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_not_installed));
					buttonView.setChecked(false);
					mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT,
							0);
					mEditPrefs.commit();

				} else if ((API >= 17) && isChecked) {
					B_Utils.createInformativeDialog(
							B_SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_unsupported));
				}
			}

		});
		fullscreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.FULL_SCREEN,
						isChecked);
				mEditPrefs.commit();

			}

		});
		OrbotHelper oh = new OrbotHelper(this);
		if (!oh.isOrbotInstalled()) {
			orbot.setEnabled(false);
		}

		orbot.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.USE_PROXY, isChecked);
				mEditPrefs.commit();

			}

		});
	}

	private void getFlashChoice() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(mContext.getResources()
				.getString(R.string.title_flash));
		builder.setMessage(getResources().getString(R.string.flash))
				.setCancelable(true)
				.setPositiveButton(
						getResources().getString(R.string.action_manual),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mEditPrefs
										.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT,
												1);
								mEditPrefs.commit();
							}
						})
				.setNegativeButton(
						getResources().getString(R.string.action_auto),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mEditPrefs
										.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT,
												2);
								mEditPrefs.commit();
							}
						}).setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						mEditPrefs.putInt(
								PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
						mEditPrefs.commit();
					}

				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void initCheckBox(CheckBox location, CheckBox fullscreen,
			CheckBox flash) {
		location.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.LOCATION, isChecked);
				mEditPrefs.commit();

			}

		});
		flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int n = 0;
				if (isChecked) {
					n = 1;
				}
				mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, n);
				mEditPrefs.commit();
				boolean flashInstalled = false;
				try {
					PackageManager pm = getPackageManager();
					ApplicationInfo ai = pm.getApplicationInfo(
							"com.adobe.flashplayer", 0);
					if (ai != null) {
						flashInstalled = true;
					}
				} catch (NameNotFoundException e) {
					flashInstalled = false;
				}
				if (!flashInstalled && isChecked) {
					B_Utils.createInformativeDialog(
							B_SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_not_installed));
					buttonView.setChecked(false);
					mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT,
							0);
					mEditPrefs.commit();

				} else if ((API > 17) && isChecked) {
					B_Utils.createInformativeDialog(
							B_SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_unsupported));
				}
			}

		});
		fullscreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.FULL_SCREEN,
						isChecked);
				mEditPrefs.commit();

			}

		});
	}

	public void agent(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder agentPicker = new AlertDialog.Builder(
						mActivity);
				agentPicker.setTitle(getResources().getString(
						R.string.title_user_agent));
				mAgentChoice = mPreferences.getInt(
						PreferenceConstants.USER_AGENT, 1);
				agentPicker.setSingleChoiceItems(R.array.user_agent,
						mAgentChoice - 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mEditPrefs.putInt(
										PreferenceConstants.USER_AGENT,
										which + 1);
								mEditPrefs.commit();
								switch (which + 1) {
								case 1:
									mAgentTextView.setText(getResources()
											.getString(R.string.agent_default));
									break;
								case 2:
									mAgentTextView.setText(getResources()
											.getString(R.string.agent_desktop));
									break;
								case 3:
									mAgentTextView.setText(getResources()
											.getString(R.string.agent_mobile));
									break;
								case 4:
									mAgentTextView.setText(getResources()
											.getString(R.string.agent_custom));
									agentPicker();
									break;
								}
							}
						});
				agentPicker.setNeutralButton(
						getResources().getString(R.string.action_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}

						});
				agentPicker
						.setOnCancelListener(new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								// TODO Auto-generated method stub
								Log.i("Cancelled", "");
							}
						});
				agentPicker.show();

			}

		});
	}

	public void agentPicker() {
		final AlertDialog.Builder agentStringPicker = new AlertDialog.Builder(
				mActivity);

		agentStringPicker.setTitle(getResources().getString(
				R.string.title_user_agent));
		final EditText getAgent = new EditText(this);
		agentStringPicker.setView(getAgent);
		agentStringPicker.setPositiveButton(
				getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getAgent.getText().toString();
						mEditPrefs.putString(
								PreferenceConstants.USER_AGENT_STRING, text);
						mEditPrefs.commit();
						mAgentTextView.setText(getResources().getString(
								R.string.agent_custom));
					}
				});
		agentStringPicker.show();
	}

	public void download(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mDownloadLocation = mPreferences.getString(
						PreferenceConstants.DOWNLOAD_DIRECTORY,
						Environment.DIRECTORY_DOWNLOADS);
				int n;
				if (mDownloadLocation.contains(Environment.DIRECTORY_DOWNLOADS)) {
					n = 1;
				} else {
					n = 2;
				}

				// TODO Auto-generated method stub

				final Dialog dialogSearch = new Dialog(B_SettingsActivity.this);

				dialogSearch.setTitle("Search engines");
				dialogSearch.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogSearch.setContentView(R.layout.dialog_download);
				DisplayMetrics metrics = getApplicationContext().getResources()
						.getDisplayMetrics();

				int width = metrics.widthPixels;
				int height = metrics.heightPixels;
				dialogSearch.getWindow().setLayout((7 * width) / 9,
						(4 * height) / 7);
				dialogSearch.show();

				TextView textView1 = (TextView) dialogSearch
						.findViewById(R.id.textView1);
				textView1.setTypeface(tfSemiBold);
				final RadioGroup rg = (RadioGroup) dialogSearch
						.findViewById(R.id.radiogrp);

				RadioButton rbGoogle = (RadioButton) dialogSearch
						.findViewById(R.id.radioDefault);
				rbGoogle.setTypeface(tfPlain);
				RadioButton rbask = (RadioButton) dialogSearch
						.findViewById(R.id.radioCustom);
				rbask.setTypeface(tfPlain);

				if (n == 1) {
					// Toggle status of checkbox selection
					rbGoogle.setChecked(true);
				}

				else if (MainActivity.engine.equalsIgnoreCase("ask")) {
					rbask.setChecked(true);

				}

				Button cancelbtn = (Button) dialogSearch
						.findViewById(R.id.btncancel);
				cancelbtn.setTypeface(tfSemiBold);
				cancelbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogSearch.dismiss();

					}
				});

				Button okbtn = (Button) dialogSearch.findViewById(R.id.btnok);
				okbtn.setTypeface(tfSemiBold);
				okbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						rbtn = (RadioButton) dialogSearch.findViewById(rg
								.getCheckedRadioButtonId());
						String valueradio = rbtn.getText().toString();
						String lower = valueradio.toLowerCase();

						// System.out.println(lower);
						int pos = 0;
						if (lower == "Default") {
							pos = 1;
						} else {
							pos = 2;
						}

						switch (pos) {
						case 1:
							mEditPrefs.putString(
									PreferenceConstants.DOWNLOAD_DIRECTORY,
									Environment.DIRECTORY_DOWNLOADS);
							mEditPrefs.commit();
							mDownloadTextView
									.setText(B_Constants.EXTERNAL_STORAGE + '/'
											+ Environment.DIRECTORY_DOWNLOADS);
							break;
						case 2:
							downPicker();

							break;
						default:
							break;
						}

						dialogSearch.dismiss();

					}
				});
				//

			}

		});
	}

	public void homePicker() {
		final AlertDialog.Builder homePicker = new AlertDialog.Builder(
				mActivity);
		homePicker.setTitle(getResources().getString(
				R.string.title_custom_homepage));
		final EditText getHome = new EditText(this);
		mHomepage = mPreferences.getString(PreferenceConstants.HOMEPAGE,
				B_Constants.HOMEPAGE);
		if (!mHomepage.startsWith("about:")) {
			getHome.setText(mHomepage);
		} else {
			getHome.setText("http://www.google.com");
		}
		homePicker.setView(getHome);
		homePicker.setPositiveButton(
				getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getHome.getText().toString();
						mEditPrefs
								.putString(PreferenceConstants.HOMEPAGE, text);
						mEditPrefs.commit();
						mHomepageText.setText(text);
					}
				});
		homePicker.show();
	}

	@SuppressWarnings("deprecation")
	public void downPicker() {
		// final EditText editViewMassage ;
		// mDownloadLocation = mPreferences.getString(
		// PreferenceConstants.DOWNLOAD_DIRECTORY,
		// Environment.DIRECTORY_DOWNLOADS);
		// final Dialog dialogClear = new Dialog(
		// B_SettingsActivity.this);
		//
		// dialogClear.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialogClear.setContentView(R.layout.dialog_downloadpath);
		// TextView textView1 = (TextView) dialogClear
		// .findViewById(R.id.textViewTitel);
		// textView1.setTypeface(tfSemiBold);
		// textView1.setText(getResources().getString(
		// R.string.title_download_location));
		// TextView textViewMassage = (TextView) dialogClear
		// .findViewById(R.id.textView);
		// textViewMassage.setTypeface(tfPlain);
		// // textViewMassage. ( );
		// editViewMassage= (EditText) dialogClear
		// .findViewById(R.id.editView);
		// editViewMassage.setTypeface(tfPlain);
		// editViewMassage.setText(mDownloadLocation);
		// Button btnCnacle = (Button) dialogClear
		// .findViewById(R.id.btncancel);
		// btnCnacle.setText(getResources().getString(R.string.action_no));
		// btnCnacle.setTypeface(tfSemiBold);
		// btnCnacle.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// dialogClear.dismiss();
		// }
		// });
		//
		// Button btnOK = (Button) dialogClear.findViewById(R.id.btnok);
		// btnOK.setText(getResources().getString(R.string.action_yes));
		// btnOK.setTypeface(tfSemiBold);
		// btnOK.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// String text = editViewMassage.getText().toString();
		// mEditPrefs.putString(
		// PreferenceConstants.DOWNLOAD_DIRECTORY, text);
		// mEditPrefs.commit();
		// mDownloadTextView.setText(B_Constants.EXTERNAL_STORAGE
		// + '/' + text);
		// dialogClear.dismiss();
		// }
		// });
		// dialogClear.show();

		final AlertDialog.Builder downLocationPicker = new AlertDialog.Builder(
				mActivity);
		LinearLayout layout = new LinearLayout(this);
		downLocationPicker.setTitle(getResources().getString(
				R.string.title_download_location));
		final EditText getDownload = new EditText(this);
		getDownload.setBackgroundResource(0);
		mDownloadLocation = mPreferences.getString(
				PreferenceConstants.DOWNLOAD_DIRECTORY,
				Environment.DIRECTORY_DOWNLOADS);
		int padding = B_Utils.convertToDensityPixels(this, 10);

		LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		getDownload.setLayoutParams(lparams);
		getDownload.setTextColor(Color.DKGRAY);
		getDownload.setText(mDownloadLocation);
		getDownload.setPadding(0, padding, padding, padding);

		TextView v = new TextView(this);
		v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		v.setTextColor(Color.DKGRAY);
		v.setText(B_Constants.EXTERNAL_STORAGE + '/');
		v.setPadding(padding, padding, 0, padding);
		layout.addView(v);
		layout.addView(getDownload);
		if (API < 16) {
			layout.setBackgroundDrawable(getResources().getDrawable(
					android.R.drawable.edit_text));
		} else {
			layout.setBackground(getResources().getDrawable(
					android.R.drawable.edit_text));
		}
		downLocationPicker.setView(layout);
		downLocationPicker.setPositiveButton(
				getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getDownload.getText().toString();
						mEditPrefs.putString(
								PreferenceConstants.DOWNLOAD_DIRECTORY, text);
						mEditPrefs.commit();
						mDownloadTextView.setText(B_Constants.EXTERNAL_STORAGE
								+ '/' + text);
					}
				});
		downLocationPicker.show();
	}

	public void homepage(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder picker = new AlertDialog.Builder(mActivity);
				picker.setTitle(getResources().getString(R.string.home));
				mHomepage = mPreferences.getString(
						PreferenceConstants.HOMEPAGE, B_Constants.HOMEPAGE);
				int n;
				if (mHomepage.contains("about:home")) {
					n = 1;
				} else if (mHomepage.contains("about:blank")) {
					n = 2;
				} else if (mHomepage.contains("about:bookmarks")) {
					n = 3;
				} else {
					n = 4;
				}

				picker.setSingleChoiceItems(R.array.homepage, n - 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								switch (which + 1) {
								case 1:
									mEditPrefs.putString(
											PreferenceConstants.HOMEPAGE,
											"about:home");
									mEditPrefs.commit();
									mHomepageText
											.setText(getResources().getString(
													R.string.action_homepage));
									break;
								case 2:
									mEditPrefs.putString(
											PreferenceConstants.HOMEPAGE,
											"about:blank");
									mEditPrefs.commit();
									mHomepageText.setText(getResources()
											.getString(R.string.action_blank));
									break;
								case 3:
									mEditPrefs.putString(
											PreferenceConstants.HOMEPAGE,
											"about:bookmarks");
									mEditPrefs.commit();
									mHomepageText.setText(getResources()
											.getString(
													R.string.action_bookmarks));

									break;
								case 4:
									homePicker();

									break;
								}
							}
						});
				picker.setNeutralButton(
						getResources().getString(R.string.action_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				picker.show();
			}

		});
	}

	public void advanced(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext,
						AdvancedSettingsActivity.class));
			}

		});
	}

	public void source(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://twitter.com/ACRDevelopment"), mContext,
						B_MainActivity.class));
				finish();
			}

		});
	}
}
