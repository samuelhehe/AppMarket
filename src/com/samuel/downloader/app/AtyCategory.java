package com.samuel.downloader.app;

import java.io.Serializable;
import java.util.List;

import net.tsz.afinal.FinalDBChen;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samuel.downloader.adapter.MainItemAdapter;
import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.CategoryInfo;
import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.ReceiverValue;
import com.samuel.downloader.view.XListView.IXListViewListener;

/**
 * 
 * @author vivi 20140307
 */
public class AtyCategory extends BaseActivity implements OnClickListener,
		IXListViewListener {

	protected static final int LOAD_COMPLETE = 1;

	protected static final int LOAD_FAIL = 0;

	protected static final int LOAD_MORE = 2;

	private com.samuel.downloader.view.XListView app_themes_boutiques_lv;

	private TextView current_content_title, current_content_title_back;

	private List<AppInfo> datas = null;

	private String categoryId, appcategory;

	private FinalDBChen db;

	protected MainItemAdapter homeItemAdapter;

	private LinearLayout loading_error_retry_layout;

	private View loading_pb;

	private View sys_retry;

	private TextView not_network_retry_tv;

	protected static final int REFRESH = 0;

	protected static final int LOADMORE = 1;

	private static int page_size = 15;

	private static int current_page = 1;

	private static int first_page = 1;

	private static int total_page = 1;

	private static int current_totalsize = page_size;

	private static String packageName = null;

	private static int downloadState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_list_category);
		// 创建一个数据库
		new FinalDBChen(getApplicationContext(), DBNAME,
				new DownloadFileItem(), TABNAME_DOWNLOADTASK, null);
		db = new FinalDBChen(getApplicationContext(), getApplicationContext()
				.getDatabasePath(DBNAME).getAbsolutePath());
		initView2();
		loadData(first_page, page_size, REFRESH);
	}

	protected void initView2() {
		app_themes_boutiques_lv = (com.samuel.downloader.view.XListView) this
				.findViewById(R.id.app_themes_boutiques_lv);
		app_themes_boutiques_lv.setPullLoadEnable(true);
		app_themes_boutiques_lv.setXListViewListener(this);

		// app_themes_boutiques_lv.setOnItemClickListener(new
		// ItemClickListener());
		app_themes_boutiques_lv.setVisibility(View.GONE);
		app_themes_boutiques_lv
				.setOnItemClickListener(new AppDetailsItemListener());

		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);

		loading_error_retry_layout = (LinearLayout) this
				.findViewById(R.id.loading_error_retry_layout);

		loading_pb = loading_error_retry_layout.findViewById(R.id.loading_pb);
		loading_pb.setVisibility(View.VISIBLE);

		sys_retry = loading_error_retry_layout.findViewById(R.id.sys_retry);
		sys_retry.setVisibility(View.GONE);

		not_network_retry_tv = (TextView) sys_retry
				.findViewById(R.id.not_network_retry_tv);
		not_network_retry_tv.setOnClickListener(this);

		Intent intent = getIntent();
		categoryId = intent.getStringExtra(CategoryInfo.TAG.TAG_CATEGORYID);
		appcategory = intent.getStringExtra(CategoryInfo.TAG.TAG_APPCATEGORY);

		current_content_title.setText(appcategory);
		current_content_title_back.setOnClickListener(this);

	}

	public void updateState() {
		// / database ds
		List<DownloadFileItem> ds = db.findItemsByWhereAndWhereValue(null,
				null, DownloadFileItem.class, TABNAME_DOWNLOADTASK, null);

		if (ds.size() != 0) {
			for (DownloadFileItem downloadMovieItem : ds) {
				System.out.println(ds);
				// 拿到数据库中下载的条目,与当前列表进行匹配.然后改变其下载状态
				// List<DownloadFileItem> items =
				// homeItemAdapter.getItems();
				for (AppInfo appInfo : datas) {
					if (downloadMovieItem.getFileId()
							.equals(appInfo.getAppId())) {
						// 如果找到了这条记录.将这条记录的 状态更新
						if (downloadMovieItem.getDownloadState().equals(
								ContentValue.DOWNLOAD_STATE_SUCCESS)) {
							appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_isDownload);
						} else if (downloadMovieItem.getDownloadState().equals(
								ContentValue.DOWNLOAD_STATE_DOWNLOADING)) {
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

	private void loadData(final int current_page, final int pageSize,
			final int type) {
		//
		// // / database ds
		// List<DownloadFileItem> ds = db.findItemsByWhereAndWhereValue(null,
		// null, DownloadFileItem.class, TABNAME_DOWNLOADTASK, null);
		// if (ds.size() != 0) {
		// if (adapter != null) {
		// for (DownloadFileItem downloadMovieItem : ds) {
		// // 拿到数据库中下载的条目,与当前列表进行匹配.然后改变其下载状态
		// List<DownloadFileItem> items = adapter.getItems();
		// for (DownloadFileItem item : items) {
		// if (downloadMovieItem.getFileId().equals(
		// item.getFileId())) {
		// // 如果找到了这条记录.将这条记录的 状态更新
		// item.setDownloadState(downloadMovieItem
		// .getDownloadState());
		// }
		// }
		// }
		// adapter.notifyDataSetChanged();// 更新这个列表
		// }
		//
		// }

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (type == REFRESH) {
						if (datas != null) {
							datas.clear();
							datas.addAll(HomeItemInfoHandler.getListByCategory(
									AtyCategory.this, current_page, pageSize,
									categoryId));

						} else {
							datas = HomeItemInfoHandler.getListByCategory(
									AtyCategory.this, current_page, pageSize,
									categoryId);
						}
						if (datas != null && datas.size() > 0) {
							updateState();
							total_page = datas.get(first_page).getTotalCount();
							mHandler.sendMessage(mHandler.obtainMessage(
									LOAD_COMPLETE, datas));
						} else {
							mHandler.sendMessage(mHandler
									.obtainMessage(LOAD_FAIL));
						}
					} else if (type == LOADMORE) {
						if (datas != null) {
							datas.addAll(HomeItemInfoHandler.getListByCategory(
									AtyCategory.this, current_page, pageSize,
									categoryId));
						} else {
							datas = HomeItemInfoHandler.getListByCategory(
									AtyCategory.this, current_page, pageSize,
									categoryId);
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

					// datas = HomeItemInfoHandler.getListByCategory(
					// AtyCategory.this, 0, 20, categoryId);
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

	protected Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOAD_COMPLETE:
				datas = (List<AppInfo>) msg.obj;
				if (app_themes_boutiques_lv != null) {

					homeItemAdapter = new MainItemAdapter(datas,
							R.layout.simple_item, AtyCategory.this, db);
					app_themes_boutiques_lv.setAdapter(homeItemAdapter);

					loading_error_retry_layout.setVisibility(View.GONE);
					app_themes_boutiques_lv.setVisibility(View.VISIBLE);
					app_themes_boutiques_lv.setPullLoadEnable(true);
				}
				break;
			case LOAD_FAIL:
				loading_error_retry_layout.setVisibility(View.VISIBLE);
				app_themes_boutiques_lv.setVisibility(View.GONE);
				loading_pb.setVisibility(View.GONE);
				sys_retry.setVisibility(View.VISIBLE);

				break;

			case LOAD_MORE:
				datas = (List<AppInfo>) msg.obj;
				homeItemAdapter.notifyDataSetChanged();
				break;
			}
		};
	};

	protected final class AppDetailsItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (datas != null && position > 0) {
				if (datas.size() >= position) {
					position = position - 1;
				}
				Intent intent = new Intent(AtyCategory.this,
						AtyAppDetails.class);
				intent.setFlags(R.layout.app_list_category);
				intent.putExtra(AppInfo.TAG.TAG_CLASSNAME,
						(Serializable) datas.get(position));
				startActivity(intent);
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.not_network_retry_tv:
			sys_retry.setVisibility(View.GONE);
			loading_pb.setVisibility(View.VISIBLE);
			loadData(current_page, current_totalsize, REFRESH);

			break;
		case R.id.current_content_title_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	/** 停止刷新， */
	private void onLoad() {
		app_themes_boutiques_lv.stopRefresh();
		app_themes_boutiques_lv.stopLoadMore();
		app_themes_boutiques_lv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				app_themes_boutiques_lv.setPullLoadEnable(false);
				loadData(first_page, page_size, REFRESH);
				current_page = first_page;
				app_themes_boutiques_lv.setAdapter(homeItemAdapter);
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
				if (current_page < total_page) {
					loadData(current_page, page_size, LOADMORE);
					current_page++;
					current_totalsize += current_page * page_size;
					homeItemAdapter.notifyDataSetChanged();
					onLoad();
				} else {
					app_themes_boutiques_lv.setPullLoadEnable(false);
				}
			}
		}, 2000);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentFileter = new IntentFilter();
		intentFileter.addAction(ContentValue.DOWNLOAD_TYPE);
		registerReceiver(mBtChaBroadcastReceiver, intentFileter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBtChaBroadcastReceiver != null) {
			unregisterReceiver(mBtChaBroadcastReceiver);
		}
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
