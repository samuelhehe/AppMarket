package com.samuel.downloader.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalDBChen;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.tools.ImageLoader;
import com.samuel.downloader.utils.AppMarketUtils;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.MD5;
import com.samuel.downloader.utils.ReceiverValue;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.TextFormater;
import com.samuel.downloader.utils.TimeUtils;
import com.samuel.downloader.utils.ToastUtils;
import com.samuel.downloader.view.ScreenShotAdapter;
import com.samuel.downloader.view.ScreenShotLayout;

public class AtyAppDetails extends BaseActivity implements OnClickListener {

	public static final int IMG_LOAD_COMPLETE = 0;

	private static String packageName = null;
	private static String appName = null;
	private static int downloadState = 0;

	protected AppInfo appInfo;
	protected TextView current_content_title_back, current_content_title;
	protected Button btn_app_details_download;
	RelativeLayout download_content_status;
	LinearLayout download_content;

	ImageView app_icon;
	TextView tv_app_name;
	TextView tv_app_group;
	TextView app_details_tv_introduct_content;

	Button app_ratings;
	Button app_permission_details;

	// / tablelayout start
	TableLayout app_details_infos;
	// TextView app_details_infos_tv_category_content ;
	TextView app_details_infos_tv_version_content;
	TextView app_details_infos_tv_downloads_content;
	TextView app_details_infos_tv_entitysize_content;
	TextView app_details_infos_tv_updatetime_content;
	TextView app_details_infos_tv_sysminversion_content;
	TextView app_details_infos_tv_devolper_content;

	// / tablelayout end
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case IMG_LOAD_COMPLETE:
				// Map<String, Object> map = (Map<String, Object>) msg.obj;
				// adapter.addObject(map);
				break;
			case DATA_LOAD_FINISH:

				final AppInfo detailsAppInfo = (AppInfo) msg.obj;

				if (detailsAppInfo != null) {
					System.out.println("detailsAppInfo--->>>"
							+ detailsAppInfo.toString());
					adapter = new ScreenShotAdapter(AtyAppDetails.this,
							getImgUrls(detailsAppInfo.getAppScreenShot()));
					movieLayout.setAdapter(adapter);
				}
				break;
			default:
				break;
			}
		}
	};
	protected static final int DATA_LOAD_FINISH = 1;
	protected static final int DATA_LOAD_FAIL = 2;

	protected FinalDBChen db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_details);

		// 创建一个数据库
		new FinalDBChen(this, DBNAME, new DownloadFileItem(),
				TABNAME_DOWNLOADTASK, null);
		db = new FinalDBChen(this, this.getDatabasePath(DBNAME)
				.getAbsolutePath());

		appInfo = (AppInfo) getIntent().getSerializableExtra(
				AppInfo.TAG.TAG_CLASSNAME);
		initView2();
		loadData();

	}

	private void initView2() {
		if (appInfo == null) {
			ToastUtils.show(getApplicationContext(), "应用信息加载失败");
			return;
		}
		download_content = (LinearLayout) this
				.findViewById(R.id.download_content);
		download_content_status = (RelativeLayout) this
				.findViewById(R.id.download_content_status);
		app_details_infos = (TableLayout) this
				.findViewById(R.id.app_details_infos);

		app_icon = (ImageView) this.findViewById(R.id.app_icon);
		tv_app_name = (TextView) this.findViewById(R.id.tv_app_name);
		tv_app_name.setText(appInfo.getAppName());

		tv_app_group = (TextView) this.findViewById(R.id.tv_app_group);
		tv_app_group.setText(appInfo.getCompanyName());

		initTableLayout();

		app_ratings = (Button) this.findViewById(R.id.app_ratings);

		app_permission_details = (Button) this
				.findViewById(R.id.app_permission_details);
		app_permission_details.setOnClickListener(this);

		app_ratings.setText(appInfo.getAppRank() + "人评分");

		app_ratings.setOnClickListener(this);

		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title.setText(appInfo.getAppName());
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);

		current_content_title_back.setOnClickListener(this);
		movieLayout = (ScreenShotLayout) findViewById(R.id.movieLayout);
		asyncImageLoad(app_icon, appInfo.getAppIcon());

		btn_app_details_download = (Button) download_content
				.findViewById(R.id.btn_app_details_download);
		btn_app_details_download.setOnClickListener(new DownloadClick(appInfo,
				btn_app_details_download));

		int flag = appInfo.getAppLocalStatus();
		switch (flag) {
		case CompareableLocalAppInfo.TAG.flag_download:
			if (!TextFormater.isEmpty(appInfo.getFileSize())) {
				btn_app_details_download.setText("下载("
						+ AppMarketUtils.getAppSize(Long.valueOf(appInfo
								.getFileSize())) + ")");
			} else {
				btn_app_details_download.setText("下载");
			}
			btn_app_details_download.setTextColor(getResources().getColor(
					R.color.ems_white));
			btn_app_details_download.setClickable(true);
			break;
		case CompareableLocalAppInfo.TAG.flag_downloading:
			btn_app_details_download.setText("下载中");
			btn_app_details_download.setTextColor(getResources().getColor(
					R.color.ems_white));
			btn_app_details_download.setClickable(false);
			break;
		case CompareableLocalAppInfo.TAG.flag_installed:
			btn_app_details_download.setText("启动");
			btn_app_details_download.setTextColor(getResources().getColor(
					R.color.ems_white));
			btn_app_details_download.setClickable(true);
			break;
		case CompareableLocalAppInfo.TAG.flag_update:
			btn_app_details_download.setText("升级");
			btn_app_details_download.setTextColor(getResources().getColor(
					R.color.ems_white));
			btn_app_details_download.setClickable(true);
			break;
		case CompareableLocalAppInfo.TAG.flag_isDownload:
			btn_app_details_download.setText("安装");
			btn_app_details_download.setTextColor(getResources().getColor(
					R.color.ems_white));
			btn_app_details_download.setClickable(true);
			break;
		}

	}

	/**
	 * 
	 * DETAILSBYID
	 * 
	 * @param imageView
	 * @param imageurl
	 *            load data the listView required
	 */
	private void loadData() {
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
		// }

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					AppInfo detailAppInfo = HomeItemInfoHandler
							.getAppInfoByAppId(appInfo.getAppId());
					if (detailAppInfo != null) {
						handler.sendMessage(handler.obtainMessage(
								DATA_LOAD_FINISH, detailAppInfo));
					} else {
						handler.sendMessage(handler
								.obtainMessage(DATA_LOAD_FAIL));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void asyncImageLoad(ImageView imageView, String imageurl) {

		AsyncImageTask asyncImgTask = new AsyncImageTask(imageView);

		asyncImgTask.execute(imageurl);

	}

	class DownloadClick implements OnClickListener {

		private AppInfo appInfo;
		private DownloadFileItem downLoadFileItem;
		private Button bt;
		// private int position;
		private TextView btn;

		public DownloadClick(DownloadFileItem downloadFileItem, TextView btn) {
			this.btn = btn;
			this.downLoadFileItem = downloadFileItem;
		}

		// /**
		// * Title: Description:
		// */
		// public DownloadClick(DownloadFileItem downLoadFileItem, Button bt) {
		// this.bt = bt;
		// this.downLoadFileItem = downLoadFileItem;
		// // this.position = position;
		// }
		public DownloadClick(AppInfo appInfo, Button bt) {
			this.bt = bt;
			this.appInfo = appInfo;
		}

		public void toDownload(DownloadFileItem downLoadFileItem) {

			System.out.println(downLoadFileItem.getFileName()
					+ ":DOWNLOAD_STATE_START" + ":toDownload");

			// downLoadFileItem.setPosition(position);
			downLoadFileItem.setDownloadState(DOWNLOAD_STATE_START);

			System.out.println("fileUrl --->>" + downLoadFileItem.getFileUrl());

			System.out.println("filePath ---->>"
					+ downLoadFileItem.getFilePath());

			System.out.println("appPackageName----->>"
					+ downLoadFileItem.getPackagename());

			SYSCS.setStartDownloadFileItem(downLoadFileItem);
			sendBroadcast(new Intent().setAction("download"));
			// btn.setClickable(false);
			// btn.setText("下载");
			// 将最初的数据保存到数据库.主要是为了 让数据库中的数据第一反应与所操作的状态一致,因为在后面的下载任务中.在线程中更改数据库状态
			// 如果在线程未开始且程序退出的情况.那么这个状态会不正确
			List<DownloadFileItem> ls = db.findItemsByWhereAndWhereValue(
					"fileName", downLoadFileItem.getFileName(),
					DownloadFileItem.class, TABNAME_DOWNLOADTASK, null);
			if (ls.size() == 0) {
				ToastUtils.show(getApplicationContext(), "已加入下载管理器");
				// 说明没有此条数据
				db.insertObject(downLoadFileItem, TABNAME_DOWNLOADTASK);
				ToastUtils.show(getApplicationContext(), "任务已存在,跳转至下载管理器");

				// /跳转至下载管理器
				startActivity(new Intent(getApplicationContext(),
						AtyDownloadMgr.class));
			} else {
				ToastUtils.show(getApplicationContext(), "任务已存在,跳转至下载管理器");

				// /跳转至下载管理器
				startActivity(new Intent(getApplicationContext(),
						AtyDownloadMgr.class));
				// 更新这个状态
				db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "fileName=?",
						new String[] { downLoadFileItem.getFileName() },
						downLoadFileItem);
			}
		}

		@Override
		public void onClick(View v) {
			if (appInfo.getAppLocalStatus() == CompareableLocalAppInfo.TAG.flag_isDownload) {
				String filePath = appInfo.convetToDownloadFileItem()
						.getFilePath();
				if (filePath != null) {
					AppMarketUtils.install(AtyAppDetails.this, filePath);
				}
			} else if (appInfo.getAppLocalStatus() == CompareableLocalAppInfo.TAG.flag_installed) {

				String packageName = appInfo.getPackageName();
				if (TextFormater.isEmpty(packageName)) {
					ToastUtils.show(AtyAppDetails.this,
							"应用启动出错,请刷新后重试,或在主界面中启动");
				} else {
					ApplicationDetailInfo appDetailInfo = new ApplicationDetailInfo(
							AtyAppDetails.this);
					appDetailInfo.launchApp(packageName);
				}
			} else {
				toDownload(appInfo.convetToDownloadFileItem());

			}
		}

	}

	/**
	 * AsyncImageTask
	 * 
	 * @author samuel
	 * 
	 */
	private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {
		private ImageView imageView;

		public AsyncImageTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Uri doInBackground(String... params) {

			String imageurl = params[0];
			if (imageurl == null || imageurl == "" || imageurl.length() <= 4) {
				return null;
			}
			imageurl = SYSCS.CONFCS.FILE_IMAGE_APPICON + imageurl;
			// System.out.println("---imageurl --------->>> " + imageurl);

			try {
				return ImageLoader.getImage(imageurl,
						SYSCS.LCS.IMG_ICONCACHEENTITYURL,
						getImageUniqueName(imageurl), 70, 70, 0.5f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Uri result) {
			if (result != null && imageView != null) {
				imageView.setImageURI(result);
			}
		}

		protected String getImageUniqueName(String imageurl) {

			return MD5.getMD5(imageurl) + ".png";
		}
	}

	private ScreenShotLayout movieLayout;
	private ScreenShotAdapter adapter;

	private List<String> getImgUrls(String combinUrl) {

		if (combinUrl != null && combinUrl != "" && combinUrl != "null"
				&& combinUrl.length() >= 8) {
			combinUrl = combinUrl.substring(0, combinUrl.length() - 1);

			String[] strs = combinUrl.split(",");
			for (int i = 0; i < strs.length; i++) {
				strs[i] = SYSCS.CONFCS.FILE_IMAGE_SHOT + strs[i];
				System.out.println("shot img --->>" + strs[i]);
			}
			return Arrays.asList(strs);
		} else {
			return null;
		}

	}

	/**
	 * 加载截图
	 */
	private void loadScrollShot(AppInfo detailsAppInfo) {

		// int[] pic = { R.drawable.pic01, R.drawable.pic02, R.drawable.pic03,
		// R.drawable.pic04, R.drawable.pic05 };
		List<String> shotUrls = getImgUrls(detailsAppInfo.getAppScreenShot());
		Map<String, Object> map = new HashMap<String, Object>();
		if (shotUrls != null && !shotUrls.isEmpty() && shotUrls.size() > 0) {
			List<Drawable> drawables = ImageLoader
					.getDrawablesFromUrls(shotUrls);
			if (drawables != null && drawables.size() > 0) {
				for (Drawable drawable : drawables) {
					map.put("image", drawable);
					map.put("text", "欢迎使用");
				}
			} else {
				for (int i = 0; i < 4; i++) {
					map.put("image",
							getResources().getDrawable(
									R.drawable.place_holder_screen));
					map.put("text", "截图加载失败");
				}
			}
		} else {
			for (int i = 0; i < 4; i++) {
				map.put("image",
						getResources().getDrawable(
								R.drawable.place_holder_screen));
				map.put("text", "截图加载失败");
			}
		}
		// Message msg = Message.obtain();
		// msg.what = IMG_LOAD_COMPLETE;
		// msg.obj = map;
		// handler.sendMessage(msg);
	}

	@SuppressWarnings("deprecation")
	private void initTableLayout() {
		// / tablelayout start
		// app_details_infos_tv_category_content = (TextView)
		// this.findViewById(R.id.app_details_infos_tv_category_content);

		app_details_tv_introduct_content = (TextView) this
				.findViewById(R.id.app_details_tv_introduct_content);
		app_details_tv_introduct_content.setText(appInfo.getAppDesc());

		app_details_infos_tv_version_content = (TextView) this
				.findViewById(R.id.app_details_infos_tv_version_content);
		app_details_infos_tv_version_content.setText(appInfo.getVersionName());

		app_details_infos_tv_downloads_content = (TextView) this
				.findViewById(R.id.app_details_infos_tv_downloads_content);
		app_details_infos_tv_downloads_content.setText(appInfo.getAppRank()
				+ "次");

		app_details_infos_tv_entitysize_content = (TextView) this
				.findViewById(R.id.app_details_infos_tv_entitysize_content);

		// app_details_infos_tv_entitysize_content.setText("50M");

		// System.out.println("filesize-------->>>>" + appInfo.getFileSize());

		if (appInfo.getFileSize() != null && appInfo.getFileSize() != ""
				&& appInfo.getFileSize() != "null") {

			app_details_infos_tv_entitysize_content.setText(AppMarketUtils
					.getAppSize(Long.valueOf(appInfo.getFileSize())));
		}

		app_details_infos_tv_updatetime_content = (TextView) this
				.findViewById(R.id.app_details_infos_tv_updatetime_content);

		String dateStr = appInfo.getCreateDate();
		String updateTimeStr;
		if (dateStr != null && dateStr.length() > 0) {

			// updateTimeStr = TimeUtils.getTime(new Date(dateStr.toLowerCase())
			// .getTime());
			updateTimeStr = dateStr;
		} else {
			updateTimeStr = TimeUtils.getCurrentTimeInString();
		}

		app_details_infos_tv_updatetime_content.setText(updateTimeStr);

		app_details_infos_tv_sysminversion_content = (TextView) this
				.findViewById(R.id.app_details_infos_tv_sysminversion_content);
		app_details_infos_tv_sysminversion_content.setText(appInfo
				.getSysMinVersion());

		app_details_infos_tv_devolper_content = (TextView) this
				.findViewById(R.id.app_details_infos_tv_devolper_content);
		if (appInfo.getCompanyName() != null
				&& appInfo.getCompanyName().length() > 0) {
			app_details_infos_tv_devolper_content.setText(appInfo
					.getCompanyName());
		} else {
			app_details_infos_tv_devolper_content.setText("官方提供");
		}
		// / tablelayout end
	}

	private void initHidenView(int currentProgress) {
		TextView app_details_download_current_percent = (TextView) download_content_status
				.findViewById(R.id.app_details_download_current_percent);
		app_details_download_current_percent.setText("下载中:" + currentProgress
				+ "%");
		ImageView btn_download_content_cancel = (ImageView) download_content_status
				.findViewById(R.id.iv_download_content_cancel);
		btn_download_content_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// / 下载
		case R.id.btn_app_details_download:

			// download_content.setVisibility(View.GONE);
			// download_content_status.setVisibility(View.VISIBLE);
			// initHidenView(currentProgress);

			break;
		// / 返回
		case R.id.current_content_title_back:
			this.finish();
			break;
		// / 取消
		case R.id.iv_download_content_cancel:
			download_content.setVisibility(View.VISIBLE);
			download_content_status.setVisibility(View.GONE);
			break;
		// / 评分
		case R.id.app_ratings:
			ToastUtils.show(AtyAppDetails.this, "规划中...");
			break;
		// / 权限详情
		case R.id.app_permission_details:
			if (appInfo.getAppPermission() == null
					|| appInfo.getAppPermission() == ""
					|| appInfo.getAppPermission().length() <= 4) {
				// System.out.println(appInfo.getAppPermission());
				ToastUtils.show(getApplicationContext(), "权限信息加载失败");
				return;
			}
			String appPermissions = appInfo.getAppPermission();
			Intent intent = new Intent(getApplicationContext(),
					AtyAppDetailPermission.class);
			intent.putExtra(AppInfo.TAG.TAG_APPPERMISSION, appPermissions);
			startActivity(intent);
			break;
		}

	}

	private void refreshBtn() {
		if (packageName != null && packageName.equals(appInfo.getPackageName())) {
			if (downloadState == CompareableLocalAppInfo.TAG.flag_installed) {
				btn_app_details_download.setText("启动");
				appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_installed);
				btn_app_details_download.setClickable(true);
			} else {
				btn_app_details_download.setText("下载");
				appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_download);
				btn_app_details_download.setClickable(true);
			}
		}else if(appName != null && appName.equals(appInfo.getAppName())){
			if(downloadState == ContentValue.DOWNLOAD_STATE_START){
				btn_app_details_download.setText("下载中");
				appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_downloading);
				btn_app_details_download.setClickable(false);
			}else if(downloadState == ContentValue.DOWNLOAD_STATE_SUCCESS){
				btn_app_details_download.setText("安装");
				appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_isDownload);
				btn_app_details_download.setClickable(true);
			}else{
				btn_app_details_download.setText("下载");
				appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_download);
				btn_app_details_download.setClickable(true);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ContentValue.DOWNLOAD_TYPE);
		registerReceiver(mBtChaBroadcastReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBtChaBroadcastReceiver);
	}

	private BroadcastReceiver mBtChaBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			downloadState = intent.getIntExtra(ContentValue.DOWNLOAD_TYPE, 0);
			if (downloadState == CompareableLocalAppInfo.TAG.flag_installed
					|| downloadState == CompareableLocalAppInfo.TAG.flag_remove) {
				packageName = intent.getStringExtra(ReceiverValue.PACKAGENAME);
			} else {
				appName = intent
						.getStringExtra(ContentValue.DOWNLOAD_ITEM_NAME);
			}
			refreshBtn();
		}
	};

}
