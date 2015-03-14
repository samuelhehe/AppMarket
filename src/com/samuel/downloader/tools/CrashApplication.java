package com.samuel.downloader.tools;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.samuel.downloader.app.AtyUpdate;
import com.samuel.downloader.app.R;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.UpdateInfo;

public class CrashApplication extends Application {

	private static final int SHOWREQID = 250;

	private NotificationManager mNotificationManager;

	private Notification mNotification;

	private static CrashApplication mApplication;

	public synchronized static CrashApplication getInstance() {
		return mApplication;
	}

	public void onCreate() {
		super.onCreate();
		mApplication = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}

	public void showNotification(UpdateInfo updateInfo, PackageInfo  packageInfo) {

		// Notification
		mNotification = new Notification();
//		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		mNotification.icon = R.drawable.ic_launcher;
		mNotification.defaults = Notification.DEFAULT_LIGHTS;
		// mNotification.contentView = Notification.notification;

		// mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		try {
			mNotification.sound = Uri
					.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
							+ "://"
							+ ApplicationDetailInfo.getPackageInfo(this).packageName
							+ "/raw/office");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		 mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotification.when = System.currentTimeMillis();
//		mNotification.tickerText = "AppMarket客户端有可用更新";
		// Intent intent = new Intent(this, MainActivity.class);

		Intent intent = new Intent(this, AtyUpdate.class);
		intent.putExtra(UpdateInfo.TAG.UPDATEINFO, updateInfo);
		intent.putExtra("packageinfo", packageInfo);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, SHOWREQID,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(this, "升级提醒", "富士康应用客户端有可用更新",
				contentIntent);
		// 指定内容意图
		// mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(SHOWREQID, mNotification);
	}

}
