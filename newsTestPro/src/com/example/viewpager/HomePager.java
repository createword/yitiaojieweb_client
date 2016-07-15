package com.example.viewpager;

import java.util.ArrayList;

import com.example.domain.NewsData;
import com.example.domain.NewsData.NewsMenuData;
import com.example.menudetail.BaseMenuDetailPager;
import com.example.menudetail.InteractMenuDetailPager;
import com.example.menudetail.NewsMenuDetailPager;
import com.example.menudetail.PhotoMenuDetailPager;
import com.example.menudetail.TopicMenuDetailPager;
import com.example.newstestpro.LeftMenuFragment;
import com.example.newstestpro.MainActivity;
import com.example.newstestpro.R;
import com.example.utils.IpUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomePager extends BasePager {
	private View view;
	NewsData newsData;
	private ArrayList<BaseMenuDetailPager> mPagers;
	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		view = LayoutInflater.from(mActivity).inflate(R.layout.homepager, null);

		fmContent.addView(view);
	
	}

	public void getDataForServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, IpUtils.MainIpServer
				+ "/zhbj/categories.json", new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result = responseInfo.result;
				System.out.println(result);
	
				asyJson(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub

			}

		});
	}

	protected void asyJson(String result) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		 newsData = gson.fromJson(result, NewsData.class);
		System.out.println(newsData);
		MainActivity activity = (MainActivity) mActivity;
		LeftMenuFragment liftMenuFragment = activity.getLeftFragment();
		liftMenuFragment.setMenuData(newsData);

		mPagers=new ArrayList<BaseMenuDetailPager>();
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,
				newsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity,menu));
		mPagers.add(new InteractMenuDetailPager(mActivity));
		setCurrentMenuDetailPager(0);
	}

	public void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailPager pager = mPagers.get(position);
		fmContent.removeAllViews();
		fmContent.addView(pager.mRootView);
		NewsMenuData menuData = newsData.data.get(position);
		lable.setText(menuData.title);

		pager.initData();
	}

	

}
