package com.boxer.component;

import java.util.ArrayList;

import com.boxer.browser.MainActivity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class AppImageAdapter extends PagerAdapter {
	private MainActivity activity;
	private String[] data;
	private static LayoutInflater inflater = null;

	private ArrayList<Bitmap> imageBitMapList = new ArrayList<Bitmap>();

	public AppImageAdapter(MainActivity a, ArrayList<Bitmap> appScreenbitmapList) {
		activity = a;
		// data=appScreenbitmapList.toArray(new
		// String[appScreenbitmapList.size()]);
		this.imageBitMapList = appScreenbitmapList;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return imageBitMapList.size();
	}

	public Object instantiateItem(View collection, int position) {
		System.out.println("size in adap =" + imageBitMapList.size());
		ImageView imageView = new ImageView(activity);
		imageView.setImageBitmap(imageBitMapList.get(position));
		LayoutParams imageParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		imageView.setLayoutParams(imageParams);
		((ViewPager) collection).addView(imageView, 0);
		return imageView;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	public Parcelable saveState() {
		return null;
	}

}
