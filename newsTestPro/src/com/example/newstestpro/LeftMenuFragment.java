package com.example.newstestpro;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.domain.NewsData;
import com.example.domain.NewsData.NewsMenuData;
import com.example.viewpager.NewsPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LeftMenuFragment extends BaseFragment {
	private ListView lvList;
	private ArrayList<NewsMenuData> mMenuList;

	private int mCurrentPos;
	private MenuAdapter mAdapter;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.left_menu_frag, null);
		lvList = (ListView) view.findViewById(R.id.arryList);
		return view;
	}

	@Override
	public void initData() {
		lvList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPos = position;
				mAdapter.notifyDataSetChanged();
				setCurrentMenuDetailPager(position);
			}

			private void setCurrentMenuDetailPager(int position) {
				MainActivity mainUi = (MainActivity) mActivity;
				ContentFragment contentFragment = mainUi.getContentFragment();
				NewsPager newsPage= contentFragment.getNewsCenterPager();
			
				newsPage.setCurrentMenuDetailPager(position);
			}
		});
	}

	public void setMenuData(NewsData data) {
		mMenuList = data.data;
		mAdapter = new MenuAdapter();
		lvList.setAdapter(mAdapter);
	}

	class MenuAdapter extends BaseAdapter {

		public int getCount() {
			return mMenuList.size();
		}

		public NewsMenuData getItem(int position) {
			return mMenuList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.liftmenu_item, null);
			TextView tvTitle = (TextView) view.findViewById(R.id.id_leftmenu);
			NewsMenuData newsMenuData = getItem(position);
			tvTitle.setText(newsMenuData.title);

			if (mCurrentPos == position) {
				tvTitle.setEnabled(true);
			} else {
				tvTitle.setEnabled(false);
			}

			return view;
		}

	}

}
