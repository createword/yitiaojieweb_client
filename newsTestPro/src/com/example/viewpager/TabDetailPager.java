package com.example.viewpager;

import java.util.ArrayList;

import com.example.activity.NewsDetailActivity;
import com.example.domain.NewsData.NewsTabData;
import com.example.domain.TabData;
import com.example.domain.TabData.TabNewsData;
import com.example.domain.TabData.TopNewsData;
import com.example.menudetail.BaseMenuDetailPager;
import com.example.newstestpro.R;
import com.example.utils.CacheUtils;
import com.example.utils.IpUtils;
import com.example.utils.PrefUtils;
import com.example.viewpager.RefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签详情页
 * 
 * @author Kevin
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener {
	private ArrayList<TopNewsData> mTopNewsList;// 头条新闻数据集合
	NewsTabData mTabData;
	private TextView tvText;
	private String mUrl;
	private ViewPager vp;
	private RefreshListView newsList;
	private TabData mTabDetailData;
	private TextView titleText;
	private CirclePageIndicator id_indicator;
	private ArrayList<TabNewsData> mNewsList;
	private String mMoreUrl;
	private NewsAdapter mNewsAdapter;
	private Handler myHandler;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
		mUrl = IpUtils.SERVER_URL + mTabData.url;
	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.tab_dial_pager, null);
		View headView = View.inflate(mActivity, R.layout.news_head_pic, null);
		vp = (ViewPager) headView.findViewById(R.id.vp_news_pager);
		vp.setOnPageChangeListener(this);
		newsList = (RefreshListView) view.findViewById(R.id.list_news);

		// 将头条新闻以头布局的形式加给listview
		newsList.addHeaderView(headView);
		titleText = (TextView) headView.findViewById(R.id.titleText);
		id_indicator = (CirclePageIndicator) headView
				.findViewById(R.id.id_indicator);
		newsList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				System.out.println(position);// 因为添加了两个headView 所以第一列显示2
				String ids = PrefUtils.getString(mActivity, "read_ids", "");
				String readId = mNewsList.get(position).id;
				if (!ids.contains(readId)) {
					ids = ids + readId + ",";
					PrefUtils.setString(mActivity, "read_ids", ids);
				}

				// mNewsAdapter.notifyDataSetChanged();
				changeReadState(view);
				Intent intent = new Intent();
				intent.setClass(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", mNewsList.get(position).url);
				mActivity.startActivity(intent);
			}

			private void changeReadState(View view) {
				// TODO Auto-generated method stub

				TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
				tvTitle.setTextColor(Color.GRAY);

			}
		});
		newsList.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				// TODO Auto-generated method stub
				getDataFromServer();
			}

			public void onLoadMore() {

				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "亲,已经加载到底了", Toast.LENGTH_SHORT)
							.show();
					newsList.onRefreshComplete(false);// 鏀惰捣鍔犺浇鏇村鐨勫竷灞�
				}

			}

			private void getMoreDataFromServer() {

				HttpUtils utils = new HttpUtils();
				utils.send(HttpMethod.GET, mMoreUrl,
						new RequestCallBack<String>() {

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								String result = (String) responseInfo.result;

								getGson(result, true);

								newsList.onRefreshComplete(true);
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								Toast.makeText(mActivity, msg,
										Toast.LENGTH_SHORT).show();
								error.printStackTrace();
								newsList.onRefreshComplete(false);
							}
						});

			}
		});
		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			getGson(cache, false);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils hutils = new HttpUtils();
		hutils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String mresult = responseInfo.result;
				System.out.println("页面数据打印" + mresult);
				getGson(mresult, false);
				newsList.onRefreshComplete(true);
				CacheUtils.setCache(mUrl, mresult, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void getGson(String mresult, boolean ismore) {
		// TODO Auto-generated method stub
		Gson mGson = new Gson();
		mTabDetailData = mGson.fromJson(mresult, TabData.class);
		String more = mTabDetailData.data.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = IpUtils.SERVER_URL + more;
		} else {
			mMoreUrl = null;
		}
		if (!ismore) {
			vp.setAdapter(new veiwPagerAdapter());
			mTopNewsList = mTabDetailData.data.topnews;
			mNewsList = mTabDetailData.data.news;
			titleText.setText(mTopNewsList.get(0).title);
			id_indicator.setViewPager(vp);
			id_indicator.setSnap(true);
			id_indicator.setOnPageChangeListener(this);

			id_indicator.onPageSelected(0);// 让指示器重新定位到第一个点
			System.out.println(mTabDetailData + "页面标签详情解析");

			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				newsList.setAdapter(mNewsAdapter);
			}
			if (myHandler == null) {
				myHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = vp.getCurrentItem();

						if (currentItem < mTopNewsList.size() - 1) {
							currentItem++;
						} else {
							currentItem = 0;
						}

						vp.setCurrentItem(currentItem);
						myHandler.sendEmptyMessageDelayed(0, 3000);
					};
				};
				myHandler.sendEmptyMessageDelayed(0, 3000);
			}
		} else {
			ArrayList<TabNewsData> news = mTabDetailData.data.news;
			mNewsList.addAll(news);
			mNewsAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 
	 * @author WINTER
	 * 
	 */
	class veiwPagerAdapter extends PagerAdapter {
		BitmapUtils mBitmap;

		public veiwPagerAdapter() {
			mBitmap = new BitmapUtils(mActivity);
			mBitmap.configDefaultLoadingImage(R.drawable.ic_launcher);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image = new ImageView(mActivity);
			image.setScaleType(ScaleType.FIT_XY);// 基于控件大小填充图片

			mBitmap.display(image,
					mTabDetailData.data.topnews.get(position).topimage);// 传递imagView对象和图片地址

			container.addView(image);

			System.out.println("instantiateItem....." + position);
			return image;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}

	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void onPageSelected(int arg0) {

		TopNewsData topNewsData = mTopNewsList.get(arg0);
		titleText.setText(topNewsData.title);
	}

	class NewsAdapter extends BaseAdapter {

		private BitmapUtils utils;

		public NewsAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.ic_launcher);
		}

		public int getCount() {
			return mNewsList.size();
		}

		public TabNewsData getItem(int position) {
			return mNewsList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_news_item,
						null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TabNewsData item = getItem(position);

			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.pubdate);

			utils.display(holder.ivPic, item.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivPic;
	}
}
