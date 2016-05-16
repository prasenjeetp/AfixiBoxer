package com.boxer.browser;


import java.util.List;

import com.boxer.bean.WrtualBean;


public interface FetchDataListener {
    public void onFetchComplete(List<WrtualBean> data);
    public void onFetchFailure(String msg);
	public void onSlidingDrawerDataFetchComplete(List<WrtualBean> freeAppInfoList);
}
