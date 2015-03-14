package com.samuel.downloader.app;

import java.util.List;

import net.tsz.afinal.FinalDBChen;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.samuel.downloader.adapter.MainItemAdapter;
import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.ReceiverValue;
import com.samuel.downloader.view.XListView.IXListViewListener;

public class FrgBoutiques extends Fragment implements OnClickListener,
		IXListViewListener, ContentValue {

	protected static final int LOAD_FAIL = 0;
	protected static final int LOAD_COMPLETE = 1;
	protected static final int LOAD_MORE = 2;

	protected static final int REFRESH = 0;
	protected static final int LOADMORE = 1;

	protected com.samuel.downloader.view.XListView installedApp_lv = null;

	protected List<AppInfo> datas = null;

	protected LinearLayout loading_error_retry_layout;

	protected View loading_pb;

	protected View sys_retry;

	protected TextView not_network_retry_tv;

	protected MainItemAdapter homeItemAdapter = null;

	protected FinalDBChen db;

	protected static int page_size = 15;

	protected static int current_page = 1;

	protected static int first_page = 1;

	protected static int total_page = 1;

	protected static int CURRENT_TOTALSIZE = page_size;

	protected static String packageName = null;

	protected static int downloadState = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 创建一个数据库
		new FinalDBChen(getActivity(), DBNAME, new DownloadFileItem(),
				TABNAME_DOWNLOADTASK, null);
		db = new FinalDBChen(getActivity(), getActivity().getDatabasePath(
				DBNAME).getAbsolutePath());
		loadData(first_page, page_size, REFRESH);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parentView = initView(inflater, container);

		return parentView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("onResume----FrgBoutiques");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ReceiverValue.FrgBoutiques.ACTION);
		getActivity().registerReceiver(mBtChaBroadcastReceiver, intentFilter);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("onStop----FrgBoutiques");
		// if (mBtChaBroadcastReceiver != null) {
		// getActivity().unregisterReceiver(mBtChaBroadcastReceiver);
		// }
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("onDestroy----FrgBoutiques");
		if (mBtChaBroadcastReceiver != null) {
			getActivity().unregisterReceiver(mBtChaBroadcastReceiver);
		}
	}

	private View initView(LayoutInflater inflater, ViewGroup container) {
		View parentView = inflater.inflate(R.layout.app_themes_boutiques,
				container, false);
		installedApp_lv = (com.samuel.downloader.view.XListView) parentView
				.findViewById(R.id.app_themes_boutiques_lv);
		installedApp_lv.setPullLoadEnable(true);
		installedApp_lv.setXListViewListener(this);

		installedApp_lv.setOnItemClickListener(new AppDetailsItemListener());

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

	private final class AppDetailsItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (datas != null && position > 0) {
				if (datas.size() >= position) {
					position = position - 1;
				}
				// String appId = datas.get(position).getAppId();
				Intent intent = new Intent(getActivity(), AtyAppDetails.class);
				intent.setFlags(R.layout.app_themes_boutiques);
				intent.putExtra(AppInfo.TAG.TAG_CLASSNAME, datas.get(position));
				startActivity(intent);
			}
		}
	}

	private void reload() {
		sys_retry.setVisibility(View.GONE);
		loading_pb.setVisibility(View.VISIBLE);

		loadData(current_page, CURRENT_TOTALSIZE, REFRESH); // /load has
		// load completed total items
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.not_network_retry_tv:
			reload();
			break;

		default:
			break;
		}
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_COMPLETE:
				datas = (List<AppInfo>) msg.obj;
				if (installedApp_lv != null) {
					homeItemAdapter = new MainItemAdapter(datas,
							R.layout.simple_item, getActivity(), db);
					installedApp_lv.setAdapter(homeItemAdapter);
					loading_error_retry_layout.setVisibility(View.GONE);
					installedApp_lv.setVisibility(View.VISIBLE);
					installedApp_lv.setPullLoadEnable(true);
				}
				break;
			case LOAD_FAIL:
				if (loading_error_retry_layout != null)
					loading_error_retry_layout.setVisibility(View.VISIBLE);
				installedApp_lv.setVisibility(View.GONE);
				loading_pb.setVisibility(View.GONE);
				sys_retry.setVisibility(View.VISIBLE);

				// ToastUtils.show(getActivity(), "获取应用列表失败,请稍候重试");
				break;

			case LOAD_MORE:
				datas = (List<AppInfo>) msg.obj;
				homeItemAdapter.notifyDataSetChanged();
				break;
			}

		};

	};

	public void updateState() {
		// / database ds
		List<DownloadFileItem> ds = db.findItemsByWhereAndWhereValue(null,
				null, DownloadFileItem.class, TABNAME_DOWNLOADTASK, null);
		System.out.println("ds==============" + ds);
		if (ds.size() != 0) {
			for (DownloadFileItem downloadMovieItem : ds) {
				System.out.println(ds);
				// 拿到数据库中下载的条目,与当前列表进行匹配.然后改变其下载状态
				// List<DownloadFileItem> items =
				// homeItemAdapter.getItems();
				for (AppInfo appInfo : datas) {
					if (downloadMovieItem.getFileId()
							.equals(appInfo.getAppId())
							&& appInfo.getAppLocalStatus() != CompareableLocalAppInfo.TAG.flag_installed) {
						// 如果找到了这条记录.将这条记录的 状态更新
						if (downloadMovieItem.getDownloadState().equals(
								ContentValue.DOWNLOAD_STATE_SUCCESS)) {
							appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_isDownload);
						} else if (downloadMovieItem.getDownloadState().equals(
								ContentValue.DOWNLOAD_STATE_START)) {
							appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_downloading);
						}
					} else if (appInfo.getAppLocalStatus() == CompareableLocalAppInfo.TAG.flag_downloading) {
						appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_download);
					}
				}

			}
		} else {
			for (AppInfo appInfo : datas) {
				if (appInfo.getAppLocalStatus() == CompareableLocalAppInfo.TAG.flag_downloading) {
					appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_download);
				}
			}
		}
	}

	public void refreshList() {
		if (packageName != null) {
			// datas = getFilteredAppInfoList(getActivity(), datas);
			for (AppInfo appInfo : datas) {
				if (appInfo.getPackageName().equals(packageName)) {
					if (downloadState == CompareableLocalAppInfo.TAG.flag_installed)
						appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_installed);
					else 
						updateState();
				}
			}
		} else {
			updateState();
		}
		homeItemAdapter.notifyDataSetChanged();// 更新这个列表
	}

	/**
	 * 
	 * load data the listView required
	 * 
	 * @param type
	 * @param page_size
	 * @param current_page
	 */
	private void loadData(final int current_page, final int page_size,
			final int type) {

		// updateState();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (type == REFRESH) {
						if (datas != null) {
							datas.clear();
							datas = HomeItemInfoHandler.getBoutiqueInfo(
									getActivity(), current_page, page_size);
						} else {
							datas = HomeItemInfoHandler.getBoutiqueInfo(
									getActivity(), current_page, page_size);
						}
						if (datas != null && datas.size() > 0) {
							updateState();
							total_page = datas.get(first_page).getTotalCount();
							System.out.println("datas" + datas);
							mHandler.sendMessage(mHandler.obtainMessage(
									LOAD_COMPLETE, datas));
						} else {
							mHandler.sendMessage(mHandler
									.obtainMessage(LOAD_FAIL));
						}
					} else if (type == LOADMORE) {
						if (datas != null) {
							datas.addAll(HomeItemInfoHandler.getBoutiqueInfo(
									getActivity(), current_page, page_size));
						} else {
							datas = HomeItemInfoHandler.getBoutiqueInfo(
									getActivity(), current_page, page_size);
						}
						if (datas != null && datas.size() > 0) {
							updateState();
							mHandler.sendMessage(mHandler.obtainMessage(
									LOAD_MORE, datas));
						} else {
							mHandler.sendMessage(mHandler
									.obtainMessage(LOAD_FAIL));
						}
					}
					// if (datas != null && datas.size() > 0) {
					// mHandler.sendMessage(mHandler.obtainMessage(
					// LOAD_COMPLETE, datas));
					// } else {
					// mHandler.sendMessage(mHandler.obtainMessage(LOAD_FAIL));
					// }
					//
					// // List<AppInfo> datas =
					// // HomeItemInfoHandler.getBoutiqueInfo(
					// // getActivity(), 0, 20);
					// if (datas != null && datas.size() > 0) {
					// mHandler.sendMessage(mHandler.obtainMessage(
					// LOAD_COMPLETE, datas));
					// } else {
					// mHandler.sendMessage(mHandler.obtainMessage(LOAD_FAIL));
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
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
				installedApp_lv.setPullLoadEnable(false);
				loadData(first_page, page_size, REFRESH);
				current_page = first_page;
				installedApp_lv.setAdapter(homeItemAdapter);
				onLoad();
			}
		}, 3000);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (current_page < total_page) {
					current_page++;
					loadData(current_page, page_size, LOADMORE);
					CURRENT_TOTALSIZE += current_page * page_size;
					// homeItemAdapter.notifyDataSetChanged();
					onLoad();
				} else {
					installedApp_lv.setPullLoadEnable(false);
				}
			}
		}, 3000);
	}

	private BroadcastReceiver mBtChaBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			downloadState = intent.getIntExtra(ContentValue.DOWNLOAD_TYPE, 0);
			packageName = intent.getStringExtra(ReceiverValue.PACKAGENAME);
			refreshList();
		}
	};
}
