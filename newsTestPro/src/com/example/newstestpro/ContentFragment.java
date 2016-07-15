package com.example.newstestpro;

import java.util.ArrayList;

import com.example.viewpager.BasePager;
import com.example.viewpager.HomePager;
import com.example.viewpager.NewsPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * zhm
 * 
 * @author WINTER
 * 
 */
public class ContentFragment extends BaseFragment implements OnClickListener {
	private TextView t1, t2, t3, t4;
	private ViewPager mViewPager;
	private ArrayList<BasePager> mPagerList;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.contentinfo, null);
		mViewPager = (ViewPager) view.findViewById(R.id.noScrollViewPager1);
		t1 = (TextView) view.findViewById(R.id.radio_h1);
		t2 = (TextView) view.findViewById(R.id.radio_h2);
		t3 = (TextView) view.findViewById(R.id.radio_h3);
		t4 = (TextView) view.findViewById(R.id.radio_h4);
		return view;
	}

	@Override
	public void initData() {
		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);
		t4.setOnClickListener(this);
		mPagerList = new ArrayList<BasePager>();
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsPager(mActivity));
		mViewPager.setAdapter(new ContentAdapter());
		mPagerList.get(0).initData();
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				mPagerList.get(arg0).initData();

			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio_h1:
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.radio_h2:
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.radio_h3:
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.radio_h4:
			mViewPager.setCurrentItem(3, false);
			break;
		default:
			break;
		}
	}
	public NewsPager getNewsCenterPager() {
		return (NewsPager) mPagerList.get(1);
	}
}
