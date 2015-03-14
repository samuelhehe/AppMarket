package com.samuel.downloader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.samuel.downloader.utils.PreferencesUtils;
import com.samuel.downloader.utils.SYSCS;

public class NetWorkStatusReceiver extends BroadcastReceiver {


	public NetWorkStatusReceiver(NetworkStatusCallback networkStatusCallback){
		this.networkStatusCallback = networkStatusCallback;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean cns = checkNetWorkInfo(context);
		if (cns) {
			PreferencesUtils.putBoolean(context, SYSCS.CONFCS.NETWORKSTATUS, true);
			networkStatusCallback.onRefreshNetworkStatus();
		} else {
			PreferencesUtils.putBoolean(context, SYSCS.CONFCS.NETWORKSTATUS, false);
			networkStatusCallback.onRefreshNetworkStatus();
		}
	}

	
	private  NetworkStatusCallback networkStatusCallback  ; 
	
	
	public interface NetworkStatusCallback {
		
		void onRefreshNetworkStatus();
		
	}
	public boolean checkNetWorkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager
					.getAllNetworkInfo();
			for (int i = 0; i < networkInfos.length; i++) {
				State state = networkInfos[i].getState();
				if (NetworkInfo.State.CONNECTED == state) {
					return true;
				}
			}
		}
		return false;
	}
	public NetworkStatusCallback getNetworkStatusCallback() {
		return networkStatusCallback;
	}
	public void setNetworkStatusCallback(NetworkStatusCallback networkStatusCallback) {
		this.networkStatusCallback = networkStatusCallback;
	}

}
