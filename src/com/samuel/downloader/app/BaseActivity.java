package com.samuel.downloader.app;

import java.util.Map;

import com.samuel.downloader.utils.ContentValue;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

public class BaseActivity extends Activity implements ContentValue {

	private SharedPreferences sp;
	private Context mContext;
	private MyApplcation myApp;
	private Editor edit;

	public ProgressDialog showProgressDialog(Context mContext, String title,
			String msg, boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setCancelable(cancelable);
		dialog.show();
		return dialog;

	}

	public void showMyDialog(Context mContext, String title, String msg,
			String positiveButtonStr, String negativeButtonStr,
			OnClickListener negativeButtonStrListener,
			OnClickListener positiveButtonStrListener, int resourceId) {
		Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setPositiveButton(positiveButtonStr, positiveButtonStrListener);
		dialog.setNegativeButton(negativeButtonStr, negativeButtonStrListener);
		if (resourceId != 0) {
			dialog.setIcon(resourceId);
		}
		dialog.show();
	}


	public void initView() {
		MyApplcation app = (MyApplcation) getApplication();
		setSp(getSharedPreferences("config", MODE_PRIVATE));
		this.edit = getSp().edit();
		setMyApp(app);
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public SharedPreferences getSp() {
		return sp;
	}

	public void setSp(SharedPreferences sp) {
		this.sp = sp;
	}

	public MyApplcation getMyApp() {
		return myApp;
	}

	public void setMyApp(MyApplcation myApp) {
		this.myApp = myApp;
	}

	public Editor getEdit() {
		return edit;
	}

	public void setEdit(Editor edit) {
		this.edit = edit;
	}

}
