package com.example.viewpager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.newstestpro.MainActivity;
import com.example.newstestpro.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 
 * 
 * @author Kevin
 * 
 */
public class BasePager {
	public Activity mActivity;
	public View mRootView;
	public FrameLayout fmContent;
	public TextView lable;
	public ImageButton menu;
	public BasePager(Activity activity) {
		mActivity = activity;
		initView();
	}

	public void initView() {
		mRootView = View.inflate(mActivity, R.layout.bpview, null);
		fmContent = (FrameLayout) mRootView.findViewById(R.id.base_pager_frame);
		lable = (TextView) mRootView.findViewById(R.id.id_biaoti);
		menu = (ImageButton) mRootView.findViewById(R.id.caidan);

	}

	public void initData() {

	}

	public void setSlidingMenuEnable(boolean enable) {
		MainActivity mainUi = (MainActivity) mActivity;

		SlidingMenu slidingMenu = mainUi.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
