package com.samuel.downloader.app;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samuel.downloader.bean.UserInfo;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.HttpClientUtil;
import com.samuel.downloader.utils.PreferencesUtils;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.ToastUtils;

public class AtyUserReg3 extends Activity implements OnClickListener {

	protected TextView current_content_title,current_content_title_back;

	protected TextView app_user_reg_result;

	protected ImageView app_user_reg_result_icon;

	protected Button app_user_register_btn;

	protected Boolean success = false;

	private String username;

	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_user_reg3_activity);
		success = getIntent().getBooleanExtra("success", Boolean.FALSE);
		username= getIntent().getStringExtra(UserInfo.TAG.TAG_USERNAME);
		password= getIntent().getStringExtra(UserInfo.TAG.TAG_PASSWORD);
		
		initView(success);
	}

	private void initView(Boolean success) {
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
//		current_content_title_back.setOnClickListener(this);
		current_content_title_back.setVisibility(View.GONE);

		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title.setText("用户注册");

		if (success) {
			app_user_reg_result = (TextView) this
					.findViewById(R.id.app_user_reg_result);
			app_user_reg_result_icon = (ImageView) this
					.findViewById(R.id.app_user_reg_result_icon);
			app_user_register_btn = (Button) this
					.findViewById(R.id.app_user_register_btn);
			app_user_register_btn.setOnClickListener(this);
		} else {

			app_user_reg_result = (TextView) this
					.findViewById(R.id.app_user_reg_result);
			app_user_reg_result.setText("服务器繁忙,请返回重试!");
			app_user_reg_result_icon = (ImageView) this
					.findViewById(R.id.app_user_reg_result_icon);
			app_user_reg_result_icon
					.setImageResource(R.drawable.app_reg_error_icon);
			app_user_register_btn = (Button) this
					.findViewById(R.id.app_user_register_btn);
			app_user_register_btn.setText("重新注册");
			app_user_register_btn.setOnClickListener(this);

		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.current_content_title_back:
			this.finish();
			break;
		case R.id.app_user_register_btn:
			if (success) {
				
				
//				Intent intent = new Intent(this, UserLoginActivity.class);
//				startActivity(intent);
				/**
				 * 正在登录跳转中...
				 */
				LoginTask loginTask= new LoginTask();
				
				loginTask.execute(username,password);
				this.finish();
			} else {
				startActivity(new Intent(this, AtyUserReg1.class));
				this.finish();
			}
			break;
		default:
			break;
		}
	}

	
	private Dialog createDialog(int progressBarVisiblity, String content) {

		View digView = View.inflate(this, R.layout.user_center_login_dialog,
				null);
		ProgressBar refresh_list_footer_progressbar = (ProgressBar) digView
				.findViewById(R.id.refresh_list_footer_progressbar);
		refresh_list_footer_progressbar.setVisibility(progressBarVisiblity);

		TextView user_loging_tv = (TextView) digView
				.findViewById(R.id.user_loging_tv);
		user_loging_tv.setText(content);
		Dialog dialog = new Dialog(this, R.style.login_dialog);

//		AlertDialog.Builder builder = new Builder(UserLoginActivity.this);

		dialog.setContentView(digView);
		dialog.setCancelable(false);

		return dialog;
	}
	
	
	
	protected final class LoginTask extends AsyncTask<String, Void, String> {

		Dialog logindialog = null;
		private WifiManager wifiMgr;

		private String combinDeviceInfo() {

			/**
			 * http://mdmss.foxconn.com/appservice/user_userLogin.action?
			 * username=ceshi002&password=123 &
			 * 
			 * appVersion=2.3.4 &deviceType=ios &osVersion=2.1.3 &udId=234543
			 * 
			 */

			/**
			 * appVersion=2.3.4 &deviceType=ios&osVersion=2.1.3 &udId=234543
			 */

			wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

			String devicetype = android.os.Build.MODEL;

			// System.out.println("MODEL: " + android.os.Build.MODEL);

			String androidSdk = android.os.Build.VERSION.SDK;

			// System.out.println("SDK: " + android.os.Build.VERSION.SDK);

			String releaseVersion = android.os.Build.VERSION.RELEASE;

			// System.out.println("RELEASE: " +
			// android.os.Build.VERSION.RELEASE);

			String mac = wifiMgr.getConnectionInfo().getMacAddress();
			
			String combinDeviceInfo = null;
			try {
				String versionName = ApplicationDetailInfo
						.getPackageInfo(getApplicationContext()).versionName;
				 combinDeviceInfo = "appVersion=" + versionName
						+ "&deviceType=Android" + devicetype.replace(" ", "") + "&osVersion="
						+ releaseVersion + "&udId=" + mac.replace(":", "");
				 
				System.out.println("deviceInfo :"+ combinDeviceInfo);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return combinDeviceInfo;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (logindialog == null) {
				logindialog = createDialog(View.VISIBLE, "正在登录跳转中...");
			}
			logindialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			try {
				String loginResult = HttpClientUtil.getRequest(
						SYSCS.CONFCS.USERLOGIN, "?username=" + username
								+ "&password=" + password+ "&"+combinDeviceInfo());
				if (loginResult != null && loginResult.length() > 0) {
					return loginResult;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String loginResult) {
			super.onPostExecute(loginResult);

			if (loginResult != null && loginResult != ""
					&& loginResult.length() > 4) {
				UserInfo userInfo = HomeItemInfoHandler
						.parseUserInfo(loginResult);
				System.out.println("loginresult------>>>>"+loginResult);
				Intent intent = new Intent(AtyUserReg3.this,
						AtyUserCenter.class);
				intent.putExtra(UserInfo.TAG.TAG_CLASSNAME, userInfo);
				PreferencesUtils.putString(getApplicationContext(),
						UserInfo.TAG.TAG_CLASSNAME, loginResult);
				PreferencesUtils.putBoolean(getApplicationContext(),
						SYSCS.PS.DEF_PNAME, true);
				logindialog.dismiss();
				startActivity(intent);
				AtyUserReg3.this.finish();

			}else{
				
				ToastUtils.show(getApplicationContext(), "服务器内部错误,请稍候重试!");
				AtyUserReg3.this.finish();
			}
		}
	}
	
}
