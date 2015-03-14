package com.samuel.downloader.receiver;

import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.ReceiverValue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		// 接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序。
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString().substring(8);

			// System.out.println("新安装了---------------" + packageName);

			// Intent newIntent = new Intent();
			// newIntent.setClassName(packageName, "packageName.MainActivity");
			// newIntent.setAction("android.intent.action.MAIN");
			// newIntent.addCategory("android.intent.category.LAUNCHER");
			// newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(newIntent);
			Intent newIntent = new Intent();
			newIntent.setAction(ContentValue.DOWNLOAD_TYPE);
			newIntent.putExtra(ReceiverValue.PACKAGENAME, packageName);
			newIntent.putExtra(ContentValue.DOWNLOAD_TYPE,
					CompareableLocalAppInfo.TAG.flag_installed);
			context.sendBroadcast(newIntent);

		}
		// 接收广播：设备上删除了一个应用程序包。
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString().substring(8);
			Intent newIntent = new Intent();
			newIntent.setAction(ContentValue.DOWNLOAD_TYPE);
			newIntent.putExtra(ContentValue.DOWNLOAD_TYPE, 5);
			newIntent.putExtra(ReceiverValue.PACKAGENAME, packageName);
			context.sendBroadcast(newIntent);
			// System.out.println("卸載了---------------" + packageName);
			// DatabaseHelper dbhelper = new DatabaseHelper();
			// dbhelper.executeSql("delete from users");
		}
	}
}
