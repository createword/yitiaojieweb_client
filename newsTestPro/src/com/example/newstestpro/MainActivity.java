package com.example.newstestpro;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.Window;
import android.widget.FrameLayout;

public class MainActivity extends SlidingFragmentActivity {
	private FrameLayout frameLayout;
	private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	private static final String FRAGMENT_CONTENT = "fragment_content";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu_main);
		intView();
		intDate();
	}

	public void intView() {

		SlidingMenu sMenu = getSlidingMenu();
		sMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sMenu.setBehindOffset(300);

	}

	public void intDate() {
		initFragment();
	}

	public void initFragment() {

		FragmentManager fManager = getSupportFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
		fTransaction.replace(R.id.parant_frame, new ContentFragment(),
				FRAGMENT_CONTENT);
		fTransaction.replace(R.id.left_menu_content, new LeftMenuFragment(),
				FRAGMENT_LEFT_MENU);

		fTransaction.commit();

	}

	public LeftMenuFragment getLeftFragment() {
		FragmentManager manager = getSupportFragmentManager();
		LeftMenuFragment liftMenuFragment = (LeftMenuFragment) manager
				.findFragmentByTag(FRAGMENT_LEFT_MENU);
		return liftMenuFragment;

	}

	public ContentFragment getContentFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		ContentFragment contentFragment = (ContentFragment) fragmentManager
				.findFragmentByTag(FRAGMENT_CONTENT);

		return contentFragment;

	}
}
