package com.samuel.downloader.app;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDBChen;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.samuel.downloader.adapter.MainItemAdapter;
import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.bean.InstalledAppInfo;
import com.samuel.downloader.dao.PakageInfoService;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.ToastUtils;


public class AtyAppMgr extends BaseActivity implements OnClickListener {

	protected static final int LOAD_COMPLETE = 1;

	protected static final int LOAD_FAIL = 0;

	protected static final String TAG = "AppMgrActivity";

	protected TextView current_content_title_back , current_content_title;

	protected TextView appItem_title = null;

	protected ListView installedApp_lv = null;

	protected View loadingView = null;

	protected List<InstalledAppInfo> datas = null;

	protected ArrayList<AppInfo> needupdateapps = null;

	private FinalDBChen db;

	protected static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_mgr);
		context = AtyAppMgr.this;
		// 创建一个数据库
		new FinalDBChen(this, DBNAME, new DownloadFileItem(),
				TABNAME_DOWNLOADTASK, null);
		db = new FinalDBChen(this, this.getDatabasePath(DBNAME).getAbsolutePath());
		selectInitView(savedInstanceState);

	}

	protected void selectInitView(Bundle savedInstanceState) {

		/**
		 * current_content_title back
		 * 
		 * app_mgr_info_layout visible
		 * 
		 * app_mgr_item_title tv
		 * 
		 * app_mgr_upgrade_info_listview lv
		 * 
		 * app_mgr_all_new_layout
		 * 
		 */

		current_content_title_back = (TextView) this.findViewById(R.id.current_content_title_back);
		current_content_title_back.setOnClickListener(this);
		current_content_title = (TextView) this.findViewById(R.id.current_content_title);
		appItem_title = (TextView) this.findViewById(R.id.app_mgr_item_title);

		loadingView = this.findViewById(R.id.app_mgr_loading_layout);

		// / 应用列表
		View layoutView = this.findViewById(R.id.app_mgr_info_layout);

		// / 全部最新
		View allNewView = this.findViewById(R.id.app_mgr_all_new_layout);

		
		switch (this.getIntent().getFlags()) {
		case R.id.app_mgr_perview_update:

			current_content_title.setText(R.string.sys_common_app_update);
			appItem_title.setText(R.string.app_mgr_info_hint);

			needupdateapps = (ArrayList<AppInfo>) this.getIntent().getSerializableExtra(AppInfo.TAG.TAG_UPDATES);

			if (needupdateapps != null) {
				if (needupdateapps.size() >= 1) {
					layoutView.setVisibility(View.VISIBLE);
					allNewView.setVisibility(View.GONE);
					// // 可升级列表
					installedApp_lv = (ListView) layoutView
							.findViewById(R.id.app_mgr_upgrade_info_listview);
					installedApp_lv.setAdapter(new MainItemAdapter(needupdateapps, R.layout.simple_item,AtyAppMgr.this,db));
				}
			} else {
				layoutView.setVisibility(View.GONE);
				allNewView.setVisibility(View.VISIBLE);

			}

			// 应用更新

			break;
		case R.id.app_mgr_perview_appmgr:
			current_content_title.setText(R.string.sys_common_app_mgr);
			appItem_title.setText(R.string.app_mgr_installed_info_hint);
			installedApp_lv = (ListView) layoutView
					.findViewById(R.id.app_mgr_upgrade_info_listview);
			loadingView.setVisibility(View.GONE);
			installedApp_lv.setOnItemClickListener(new InstalledItemListener());
			loadData();

			// 应用管理
			break;
		}

	}

	private final class InstalledItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (datas != null) {

				String packageName = datas.get(position).getPackageName();

				ApplicationDetailInfo appDetailInfo = new ApplicationDetailInfo(
						context);
				appDetailInfo.showInstalledAppDetails(packageName);
			}
		}

	}

	protected Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOAD_COMPLETE:

				datas = (List<InstalledAppInfo>) msg.obj;
				installedApp_lv.setAdapter(new AppMgrAdapter(datas,
						R.layout.app_themes_smaller_downloader_item,
						AtyAppMgr.this));
				loadingView.setVisibility(View.GONE);
				break;

			case LOAD_FAIL:

				ToastUtils.show(AtyAppMgr.this, "获取应用列表失败,请稍候重试");
				break;

			}

		};

	};

	/**
	 * 
	 * load data the listView required
	 */
	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					List<InstalledAppInfo> datas = PakageInfoService
							.getAppInfos(getApplicationContext());

					// String inputParams =
					// JsonUtils.getInstalledAppInputParamsJsonStr(PakageInfoService.getInstalledAppInputParams(getApplicationContext()));

					// Log.i(TAG, inputParams);
					if (datas != null && datas.size() > 0) {
						
						
						handler.sendMessage(handler.obtainMessage(
								LOAD_COMPLETE, datas));
//						for (Iterator<InstalledAppInfo> iterator = datas.iterator(); iterator
//								.hasNext();) {
//							InstalledAppInfo installedAppInfo = iterator.next();
//							System.out.println(installedAppInfo.getPackageName());
//							
//						}
					
					} else {
						handler.sendMessage(handler.obtainMessage(LOAD_FAIL));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private  final class AppMgrAdapter extends BaseAdapter {

		protected List<InstalledAppInfo> datas;
		protected LayoutInflater inflater;
		protected int itemres;

		public AppMgrAdapter(List<InstalledAppInfo> datas, int itemres,
				Context context) {
			this.datas = datas;
			this.itemres = itemres;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView appIcon = null;
			TextView appName = null;
			TextView versionName = null;
			TextView button_tv = null;
			TextView launchapp = null;
			final InstalledAppInfo contentInfo = datas.get(position);
			/**
			 * lefticon img toptxt tv name bottomtxt tv neirong btn1 tv detail
			 */
			if (convertView == null) {
				convertView = inflater.inflate(itemres, null);
				appIcon = (ImageView) convertView.findViewById(R.id.lefticon);
				appName = (TextView) convertView.findViewById(R.id.toptxt);
				versionName = (TextView) convertView.findViewById(R.id.bottomtxt);
				button_tv = (TextView) convertView.findViewById(R.id.btn1);
				
//				button_tv.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						ApplicationDetailInfo appDetailInfo = new ApplicationDetailInfo(
//								context);
//						appDetailInfo.showInstalledAppDetails(contentInfo
//								.getPackageName());
//					}
//				});
				launchapp = (TextView) convertView.findViewById(R.id.launchapp);
				launchapp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ApplicationDetailInfo appDetailInfo = new ApplicationDetailInfo(
								context);
						appDetailInfo.launchApp(contentInfo.getPackageName());
					}
				});
				convertView.setTag(new DataWrapper(appIcon, appName,
						versionName, button_tv, launchapp));
			} else {
				DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
				appIcon = dataWrapper.appIcon;
				appName = dataWrapper.appName;
				versionName = dataWrapper.versionName;
				button_tv = dataWrapper.button_tv;
				launchapp = dataWrapper.launchapp;

			}

			appName.setText(contentInfo.getAppName());
			versionName.setText(contentInfo.getVersionName());
			appIcon.setImageDrawable(contentInfo.getDrawable());
			button_tv.setText("详情");
			launchapp.setText("启动");
			launchapp.setVisibility(View.GONE);
			return convertView;
		}

		private  class DataWrapper {

			ImageView appIcon = null;
			TextView appName = null;
			TextView versionName = null;

			TextView button_tv = null;
			TextView launchapp = null;

			public DataWrapper(ImageView appIcon, TextView appName,
					TextView versionName, TextView button_tv, TextView launchapp) {
				super();
				this.appIcon = appIcon;
				this.appName = appName;
				this.versionName = versionName;
				this.button_tv = button_tv;
				this.launchapp = launchapp;
			}

		}

	}

	@Override
	public void onClick(View v) {
		this.finish();

	}
}
