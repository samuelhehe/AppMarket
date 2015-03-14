package com.samuel.downloader.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.samuel.downloader.app.R;


public class DeviceAdminSampleReceiver extends DeviceAdminReceiver {

	private static final String TAG = "DeviceAdminSample";
	public static int ERR_TIME = 0;
	public static int ERR_TIME_TOTAL = 0;
	

    void showToast(Context context, String msg) {    	
        String status = context.getString(R.string.admin_receiver_status, msg);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        //showToast(context, context.getString(R.string.admin_receiver_status_enabled));
    	showToast(context, "裝置管理員已和設備綁定!");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.admin_receiver_status_disable_warning);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_disabled));
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_pw_changed));
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
    	ERR_TIME_TOTAL += 1;
    	if(ERR_TIME_TOTAL >= ERR_TIME){
    		showToast(context, "密碼輸入錯誤已達" + ERR_TIME + "次，清除系統資料!");
    	}else{
    		showToast(context, "密碼輸入錯誤" + ERR_TIME_TOTAL + "次，連續錯誤" + ERR_TIME + "次將清除系統資料!");
    	}
    	//showToast(context, context.getString(R.string.admin_receiver_status_pw_failed));
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        //showToast(context, context.getString(R.string.admin_receiver_status_pw_succeeded));
    	showToast(context, "密碼輸入成功!");
        ERR_TIME_TOTAL = 0;
    }

    @Override
    public void onPasswordExpiring(Context context, Intent intent) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        long expr = dpm.getPasswordExpiration(
                new ComponentName(context, DeviceAdminSampleReceiver.class));
        long delta = expr - System.currentTimeMillis();
        boolean expired = delta < 0L;
        String message = context.getString(expired ?
                R.string.expiration_status_past : R.string.expiration_status_future);
        showToast(context, message);
        Log.v(TAG, message);
    }
    }