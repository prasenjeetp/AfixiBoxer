/*
 * Copyright 2014 A.C.R. Development
 */
package com.boxer.browser;

import com.boxer.browser.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;

public class LightningDownloadListener extends B_BrowserActivity implements DownloadListener {

	private Activity mActivity;

	LightningDownloadListener(Activity activity) {
		mActivity = activity;
	}

	@Override
	public void onDownloadStart(final String url, final String userAgent, final String contentDisposition,
			final String mimetype, long contentLength) {
		
		CommonUtilities.delete_app_download_tab = true;
		
		String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					DownloadHandler.onDownloadStart(mActivity, url, userAgent, contentDisposition, mimetype, false);
					((B_BrowserActivity) mActivity).delete_download_tab();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					((B_BrowserActivity) mActivity).delete_download_tab();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
		builder.setCancelable(false);
		builder.setTitle(fileName).setMessage(mActivity.getResources().getString(R.string.dialog_download))
				.setPositiveButton(mActivity.getResources().getString(R.string.action_download), dialogClickListener)
				.setNegativeButton(mActivity.getResources().getString(R.string.action_cancel), dialogClickListener)
				.setCancelable(false)
				.show();
	
		
		Log.i(B_Constants.TAG, "Downloading" + fileName);
		return;

	}
}
