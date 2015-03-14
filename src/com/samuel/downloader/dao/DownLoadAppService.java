package com.samuel.downloader.dao;

import java.util.List;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.samuel.downloader.bean.DownLoaderContentInfo;
import com.samuel.downloader.utils.SYSCS;

public class DownLoadAppService {

	private static final String TAG = "DownLoadInfoService";
	protected long downloadReference;

	protected Context context;
	protected DownloadManager downloadManager;

	public DownLoadAppService() {
		super();
	}

	public DownLoadAppService(Context context) {
		downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		this.context = context;
	}

	/**
	 * get all task infos
	 * 
	 * @param context
	 * @return
	 * 
	 */
	public static List<DownLoaderContentInfo> getAllTaskInfos(Context context) {
		DownLoadInfoDBHelper dldbhelper = new DownLoadInfoDBHelper(context);
		return dldbhelper.findAllDownLoadInfos();
	}

	/**
	 * 
	 * query exist task
	 * 
	 * @param taskurl
	 * @param context
	 * @return true is could add task , false is can't add task
	 */
	private static boolean addTaskInfoToDB(String taskurl, Context context) {

		DownLoadInfoDBHelper dbhelper = new DownLoadInfoDBHelper(context);

		if (dbhelper.queryExistTask(taskurl)) {
			// 不能添加
			return false;
		} else {
			// 能够添加
			return true;
		}
	}

	/**
	 * 
	 * 
	 * @param contentInfo
	 * @param context
	 * @return true is addtask info success or false
	 */
	public boolean addTaskInfoToDB(DownLoaderContentInfo contentInfo,
			Context context) {

		DownLoadInfoDBHelper dbhelper = new DownLoadInfoDBHelper(context);

		if (addTaskInfoToDB(contentInfo.getAppUrl(), context)) {
			// 添加成功
			long downloadId = addDownloadTask(contentInfo.getAppUrl(),
					contentInfo.getAppName());
			System.out.println("downloadId ----->>>" + downloadId);
			contentInfo.setDqId(downloadId);
			dbhelper.insertDownLoadInfo(contentInfo);
			return true;
		} else {
			// 添加失败
			return false;
		}

		// dbhelper.insertDownLoadInfo(new
		// DownLoaderContentInfo("appName",20,downloadReference,"adfadfadfadsf.png",TimeUtils.getCurrentTimeInLong()));

	}

	public long addDownloadTask(String url, String fileName) {

		Uri download_uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(
				download_uri);
		request.setDestinationInExternalPublicDir(
				SYSCS.CONFCS.DOWNLOAD_FOLDER_NAME, fileName);
		// request.setTitle(getString(R.string.download_notification_title));
		request.setDescription(fileName+" 正在下载中...");
		/**
		 * 表示下载进行中和下载完成的通知栏是否显示。默认只显示下载中通知。
		 * VISIBILITY_VISIBLE_NOTIFY_COMPLETED表示下载完成后显示通知栏提示。
		 * VISIBILITY_HIDDEN表示不显示任何通知栏提示，
		 * 这个需要在AndroidMainfest中添加权限android.permission
		 * .DOWNLOAD_WITHOUT_NOTIFICATION.
		 * 
		 */
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(false);
		request.setTitle("AppMarket下载管理器");
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
				| DownloadManager.Request.NETWORK_MOBILE);
		// request.allowScanningByMediaScanner();
		// request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		// request.setShowRunningNotification(false);
		// request.setMimeType("application/cn.trinea.download.file");

		return downloadReference = downloadManager.enqueue(request);

	}
}
