package com.example.viewpager;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.newstestpro.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新的ListView
 * 
 * @author Kevin
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener ,android.widget.AdapterView.OnItemClickListener{

	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新

	private View mHeaderView;
	private int startY = -1;// 滑动起点的y坐标
	private int mHeaderViewHeight;
	private int mCurrrentState;

	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	RotateAnimation rAnimationup, rAnimationdown;
	OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_top_listview,
				null);
		this.addHeaderView(mHeaderView);

		tvTitle = (TextView) mHeaderView.findViewById(R.id.ls_tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.ls_tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏头布局

		initAnim();
		tvTime.setText("当前时间:" + getCurrentTime());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getRawY();
			}

			if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
				break;
			}

			int endY = (int) ev.getRawY();
			int dy = endY - startY;// 移动便宜量

			if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
				int padding = dy - mHeaderViewHeight;// 计算padding
				mHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding

				if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
					mCurrrentState = STATE_RELEASE_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
					mCurrrentState = STATE_PULL_REFRESH;
					refreshState();
				}

				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 重置

			if (mCurrrentState == STATE_RELEASE_REFRESH) {
				mCurrrentState = STATE_REFRESHING;// 正在刷新
				mHeaderView.setPadding(0, 0, 0, 0);// 显示
				refreshState();
			} else if (mCurrrentState == STATE_PULL_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 刷新下拉控件的布局
	 */
	private void refreshState() {
		switch (mCurrrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(rAnimationup);
			break;
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(rAnimationdown);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
			ivArrow.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);
			if (mListener != null) {
				mListener.onRefresh();
			}

			break;

		default:
			break;
		}
	}

	private void initAnim() {// 控制箭头动画
		// 向上箭头
		rAnimationup = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rAnimationup.setDuration(200);
		rAnimationup.setFillAfter(true);
		// 向下箭头
		rAnimationdown = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rAnimationdown.setDuration(200);
		rAnimationdown.setFillAfter(true);
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		mListener = refreshListener;
	}

	// 加载数据
	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();// 加载下一页

	}

	public void onRefreshComplete(boolean success) {
		if (isLoadingMore) {// 加载更多
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadingMore = false;
		} else {
			mCurrrentState = STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);

			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
			if (success) {
				tvTime.setText("当前时间:" + getCurrentTime());
			}
		}

	}

	public void initFooterView() {
		mFooterView = View.inflate(getContext(),
				R.layout.refresh_listview_footer, null);
		this.addFooterView(mFooterView);
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		this.setOnScrollListener(this);
	}

	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());

	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	private boolean isLoadingMore;

	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {

			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {
				mFooterView.setPadding(0, 0, 0, 0);
				System.out.println("到底了................");
				setSelection(getCount() - 1);
				isLoadingMore = true;
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}
	}
	OnItemClickListener mItemClickListener;
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated method stub
		super.setOnItemClickListener(this);
		mItemClickListener=listener;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);
		}
		
	}

}
