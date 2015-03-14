package com.samuel.downloader.app;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samuel.downloader.adapter.CategoryAdapter;
import com.samuel.downloader.bean.CategoryInfo;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.view.XListView.IXListViewListener;

public class FrgCategory extends Fragment implements OnClickListener,
		IXListViewListener {

	protected static final int LOAD_COMPLETE = 1;

	protected static final int LOAD_FAIL = 0;

	private com.samuel.downloader.view.XListView installedApp_lv = null;

	private List<CategoryInfo> data = null;

	protected LinearLayout loading_error_retry_layout;

	protected View loading_pb;

	protected View sys_retry;

	protected TextView not_network_retry_tv;
	
	protected static final int REFRESH = 0;

	protected static final int LOADMORE = 1;
	private static int page_size = 15;

	private static int current_page = 0;

	private static int first_page = 0;

	private static int CURRENT_TOTALSIZE = page_size;

	protected CategoryAdapter categoryAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadData(current_page, page_size, REFRESH);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("onResume-----FrgCategory");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("onPause------FrgCategory");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.app_themes_category,
				container, false);
		installedApp_lv = (com.samuel.downloader.view.XListView) parentView
				.findViewById(R.id.app_themes_category_lv);
		installedApp_lv.setPullLoadEnable(false);
		installedApp_lv.setXListViewListener(this);

		installedApp_lv.setOnItemClickListener(new ItemClickListener());

		loading_error_retry_layout = (LinearLayout) parentView
				.findViewById(R.id.loading_error_retry_layout);

		loading_pb = loading_error_retry_layout.findViewById(R.id.loading_pb);
		loading_pb.setVisibility(View.VISIBLE);

		sys_retry = loading_error_retry_layout.findViewById(R.id.sys_retry);
		sys_retry.setVisibility(View.GONE);

		not_network_retry_tv = (TextView) sys_retry
				.findViewById(R.id.not_network_retry_tv);
		not_network_retry_tv.setOnClickListener(this);
		installedApp_lv.setVisibility(View.GONE);

		return parentView;
	}

	protected Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOAD_COMPLETE:
				if (installedApp_lv != null) {
					data = (List<CategoryInfo>) msg.obj;
					categoryAdapter = new CategoryAdapter(getActivity(), data,
							R.layout.app_themes_category_item);
					installedApp_lv.setAdapter(categoryAdapter);
					loading_error_retry_layout.setVisibility(View.GONE);
					installedApp_lv.setVisibility(View.VISIBLE);
				}
				break;
			case LOAD_FAIL:
				loading_error_retry_layout.setVisibility(View.VISIBLE);
				installedApp_lv.setVisibility(View.GONE);
				loading_pb.setVisibility(View.GONE);
				sys_retry.setVisibility(View.VISIBLE);

//				ToastUtils.show(getActivity(), "获取应用列表失败,请稍候重试");
				break;
			}

		};

	};

	private void loadData(final int current_page, final int pageSize,
			final int type) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (type == REFRESH) {
						if (data != null) {
							data.clear();
							data.addAll(HomeItemInfoHandler.getCategoryInfo(
									current_page, pageSize));
						} else {
							data = HomeItemInfoHandler.getCategoryInfo(
									current_page, pageSize);
						}
					} else if (type == LOADMORE) {
						if (data != null) {
							data.addAll(HomeItemInfoHandler.getCategoryInfo(
									current_page, pageSize));
						} else {
							data = HomeItemInfoHandler.getCategoryInfo(
									current_page, pageSize);
						}
					}
					if (data != null && data.size() > 0) {
						mHandler.sendMessage(mHandler.obtainMessage(
								LOAD_COMPLETE, data));
					} else {
						mHandler.sendMessage(mHandler.obtainMessage(LOAD_FAIL));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.not_network_retry_tv:
			sys_retry.setVisibility(View.GONE);
			loading_pb.setVisibility(View.VISIBLE);
			loadData(current_page, CURRENT_TOTALSIZE, REFRESH); // /load has
																// load
			// completed total items
			break;
		default:
			break;
		}
	}

	private final class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (data != null&&position>0) {
				if (data.size() >= position) {
					position = position - 1;
				}
				String categoryId = data.get(position).getCategoryId();
				String appcategory = data.get(position).getAppCategory();
				Intent intent = new Intent(getActivity(), AtyCategory.class);
				Bundle bundle = new Bundle();
				bundle.putString(CategoryInfo.TAG.TAG_CATEGORYID, categoryId);
				bundle.putString(CategoryInfo.TAG.TAG_APPCATEGORY, appcategory);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}

	}

	/** 停止刷新， */
	private void onLoad() {
		installedApp_lv.stopRefresh();
		installedApp_lv.stopLoadMore();
		installedApp_lv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadData(first_page, CURRENT_TOTALSIZE, REFRESH);
				installedApp_lv.setAdapter(categoryAdapter);
				onLoad();
			}
		}, 2000);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				loadData(current_page, page_size, LOADMORE);
				current_page++;
				CURRENT_TOTALSIZE += current_page * page_size;
				categoryAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

}
