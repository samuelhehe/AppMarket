package com.samuel.downloader.app;

import java.io.File;
import java.util.List;

import net.tsz.afinal.FinalDBChen;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.dao.PakageInfoService;
import com.samuel.downloader.download.DownloadFile;
import com.samuel.downloader.download.DownloadTask;
import com.samuel.downloader.download.DownloadTask.OnDeleteTaskListener;
import com.samuel.downloader.tools.ImageLoader;
import com.samuel.downloader.utils.AppMarketUtils;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.MD5;
import com.samuel.downloader.utils.ReceiverValue;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.ToastUtils;

public class AtyDownloadMgr extends BaseActivity implements ContentValue {

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			// 获得需要下载的对象
			DownloadFileItem downLoadFileItem = SYSCS.startDownloadFileItem;
			View currentTaskView = getLayoutInflater().inflate(
					R.layout.list_download_item, null);
			downLoadFileItem.setDownloadState(DOWNLOAD_STATE_START);
			allDownloadTaskLayout.addView(currentTaskView);
			// 开启下载任务
			new DownloadTask(context, currentTaskView, downLoadFileItem, false)
					.setOnDeleteTaskListener(new DownloadMgrDeleteTaskListener(
							allDownloadTaskLayout));

		}
	};

	private BroadcastReceiver appChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getIntExtra(ContentValue.DOWNLOAD_TYPE, 0) == CompareableLocalAppInfo.TAG.flag_installed) {
				for (DownloadFileItem downloadMovieItem : ds) {
					if (intent.getStringExtra(ReceiverValue.PACKAGENAME)
							.equalsIgnoreCase(
									downloadMovieItem.getPackagename())) {
						// 删除本地文件
						File df = new File(downloadMovieItem.getFilePath());
						if (df.exists()) {
							// 如果文件存在
							df.delete();
						}

						// 删除数据库中的内容
						new FinalDBChen(getApplicationContext(), DBNAME)
								.deleteItem(TABNAME_DOWNLOADTASK, "fileName=?",
										new String[] { downloadMovieItem
												.getFileName() });
					}
				}
				initValue();
			}
		}

	};

	private LinearLayout allDownloadTaskLayout;
	private TextView current_content_title, current_content_title_back;
	private FinalDBChen db;
	private List<DownloadFileItem> ds;

	class LoadImageThread extends Thread {

		private ImageView imageView;
		private String imageurl;
		private Uri imageUri;

		public LoadImageThread(ImageView icon, String imageUrl) {
			this.imageView = icon;
			this.imageurl = imageUrl;
		}

		protected String getImageUniqueName(String imageurl) {
			return MD5.getMD5(imageurl) + ".png";
		}

		@Override
		public void run() {
			super.run();
			try {
				imageurl = SYSCS.CONFCS.FILE_IMAGE_APPICON + imageurl;
				imageUri = ImageLoader.getImage(imageurl,
						SYSCS.LCS.IMG_ICONCACHEENTITYURL,
						getImageUniqueName(imageurl), 70, 70, 0.5f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (imageUri != null) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						imageView.setImageURI(imageUri);
					}
				});
			}

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_manager_activity);

		allDownloadTaskLayout = (LinearLayout) findViewById(R.id.download_listview_lin);
		// 注册广播
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("download");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
		// 注册安装AppReceiver
		IntentFilter appIntentFilter = new IntentFilter();
		appIntentFilter.addAction(ContentValue.DOWNLOAD_TYPE);
		registerReceiver(appChangeReceiver, appIntentFilter);

		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
		current_content_title.setText("下载管理");
		current_content_title.setTextColor(getResources().getColor(
				R.color.webapp_black));
		current_content_title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AtyDownloadMgr.this.finish();
			}
		});

		initValue();

	}

	private void initValue() {
		// 遍历数据库得到已有的数据
		db = new FinalDBChen(getApplicationContext(), getDatabasePath(DBNAME)
				.getAbsolutePath());
		// /查询所有已存在数据
		ds = db.findItemsByWhereAndWhereValue(null, null,
				DownloadFileItem.class, TABNAME_DOWNLOADTASK, null);
		List<CompareableLocalAppInfo> compareableLocalAppInfos = PakageInfoService
				.getCompareableLocalAppInfos(AtyDownloadMgr.this);

		System.out.println("数据库中已经存在的数据;" + ds.size());
		if (ds.size() != 0) {
			// 如果数据库中有数据
			// 直接初始化
			for (DownloadFileItem downloadMovieItem : ds) {
				// // //遍历所有下载对象
				// boolean isDel = false;
				// for (CompareableLocalAppInfo compareableLocalAppInfo :
				// compareableLocalAppInfos) {
				// if (TextFormater
				// .isEmpty(downloadMovieItem.getPackagename())) {
				// continue;
				// } else {
				// if (downloadMovieItem.getPackagename()
				// .equalsIgnoreCase(
				// compareableLocalAppInfo
				// .getPackageName())) {
				// // 删除本地文件
				// File df = new File(downloadMovieItem.getFilePath());
				// if (df.exists()) {
				// // 如果文件存在
				// df.delete();
				// }
				//
				// // 删除数据库中的内容
				// new FinalDBChen(getApplicationContext(), DBNAME)
				// .deleteItem(TABNAME_DOWNLOADTASK,
				// "fileName=?",
				// new String[] { downloadMovieItem
				// .getFileName() });
				// isDel = true;
				// continue;
				// }
				// }
				// }
				// if(isDel == true){
				// continue;
				// }
				View view = getLayoutInflater().inflate(
						R.layout.list_download_item, null);
				allDownloadTaskLayout.addView(view);

				ImageView headImg = (ImageView) view
						.findViewById(R.id.movie_headimage);

				String imageurl = downloadMovieItem.getFileIcon();

				if (imageurl == null || imageurl == ""
						|| imageurl.length() <= 4) {
					headImg.setImageDrawable(getResources().getDrawable(
							R.drawable.app_icon_tbd));
				} else {
					LoadImageThread loadImageThread = new LoadImageThread(
							headImg, imageurl);
					loadImageThread.start();
				}

				ProgressBar progressBar = (ProgressBar) view
						.findViewById(R.id.download_progressBar);// 得到进度条
				TextView t = (TextView) view.findViewById(R.id.movie_name_item);
				t.setText(downloadMovieItem.getFileName());// 设置名字
				// 设置当前进度百分比
				String stsize = downloadMovieItem.getFileSize();// 设置当前进度,和总大小
				if (stsize.contains("B") || stsize.contains("K")
						|| stsize.contains("M") || stsize.contains("G")) {

				} else {
					stsize = AppMarketUtils.getAppSize(Long.parseLong(stsize))
							.toString();
				}
				// /获取总大小的合理格式
				// System.out.println("stsize is ---->>>" + stsize);
				TextView currentprogress = (TextView) view
						.findViewById(R.id.current_progress);
				TextView tsize = (TextView) view.findViewById(R.id.totalsize);
				if (downloadMovieItem.getPercentage().contains("100")) {
					currentprogress.setText("下载完成");

				} else {
					currentprogress.setText(downloadMovieItem.getPercentage());
				}

				TextView movie_file_size = (TextView) view
						.findViewById(R.id.movie_file_size);
				movie_file_size.setText(stsize);
				long currentSize = downloadMovieItem.getCurrentProgress();

				String ts = Formatter.formatFileSize(getApplicationContext(),
						currentSize);
				if (stsize.contains("null")) {
					stsize = "0.00B";// 如果是因为某种特定的情况未开始下载.将总大小设置为0.0
				}

				tsize.setText(ts + "/" + stsize);
				Button bt = (Button) view.findViewById(R.id.stop_download_bt);
				// 设置当前进度
				long count = downloadMovieItem.getProgressCount();

				progressBar.setMax((int) count);
				progressBar.setProgress((int) currentSize);
				if (bt.getVisibility() == View.INVISIBLE) {
					// 如果是隐藏状态

					if (downloadMovieItem.getDownloadState() == DOWNLOAD_STATE_DOWNLOADING) {
						bt.setBackgroundResource(R.drawable.button_pause);
					} else if (downloadMovieItem.getDownloadState() == DOWNLOAD_STATE_SUSPEND) {
						bt.setBackgroundResource(R.drawable.button_start);
					}
					bt.setVisibility(View.VISIBLE);
				}

				// if(downloadMovieItem.getPercentage().contains("100")){
				// bt.setBackgroundResource(R.drawable.button_finish);
				// }

				new DownloadTask(getApplicationContext(), view,
						downloadMovieItem, true)
						.setOnDeleteTaskListener(new DownloadMgrDeleteTaskListener(
								allDownloadTaskLayout));
			}

		} else {
			ToastUtils.show(getApplicationContext(), "暂时没有下载内容");
			allDownloadTaskLayout.removeAllViews();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// System.out.println("DMgrActivity------->>>解除receiver");
		unregisterReceiver(mBroadcastReceiver);
		unregisterReceiver(appChangeReceiver);
	}

	/**
	 * 删除任务可以删除view 和 db中相应的任务
	 */
	class DownloadMgrDeleteTaskListener implements OnDeleteTaskListener {

		private LinearLayout lin;

		/**
		 * Title: Description:
		 */
		public DownloadMgrDeleteTaskListener(LinearLayout lin) {
			this.lin = lin;
		}

		@Override
		public void onDelete(final View taskView,
				final DownloadFileItem downloadFileItem) {
			// // remove View
			lin.removeView(taskView);
			// 停止这个任务
			DownloadFile downloadFile = downloadFileItem.getDownloadFile();
			if (downloadFile != null) {
				downloadFile.stopDownload();
			}
			// 删除本地文件
			File df = new File(downloadFileItem.getFilePath());
			if (df.exists()) {
				// 如果文件存在
				df.delete();
			}

			// 删除数据库中的内容
			new FinalDBChen(getApplicationContext(), DBNAME).deleteItem(
					TABNAME_DOWNLOADTASK, "fileName=?",
					new String[] { downloadFileItem.getFileName() });

			ToastUtils.show(getApplicationContext(),
					"删除完毕 " + downloadFileItem.getFileName());
			sendOnDelBroadCast(downloadFileItem);
		}

		private void sendOnDelBroadCast(final DownloadFileItem downloadFileItem) {
			// 发送一个删除文件的广播.让主页的下载按钮变为可下载
			Intent intent = new Intent();
			intent.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_DELETE);
			intent.setAction(DOWNLOAD_TYPE);
			SYSCS.setSuccessDownloadFileItem(downloadFileItem);
			sendBroadcast(intent);
		}
	}

}
