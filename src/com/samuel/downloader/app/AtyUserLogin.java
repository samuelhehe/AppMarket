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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

public class AtyUserLogin extends Activity implements OnClickListener {

	protected TextView current_content_title_back,current_content_title;

	protected AutoCompleteTextView edittext_account;

	protected ImageView button_account_clear;
	protected ImageView button_pwd_clear;

	protected EditText edittext_password;

	protected Button button_login;

	protected Button button_register;

	protected TextView forget_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_user_login_activity);

		initView();
		// showDialog();
	}

	private void initView() {
		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title.setText("个人中心");

		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
		current_content_title_back.setVisibility(View.GONE);
		
		edittext_account = (AutoCompleteTextView) this
				.findViewById(R.id.edittext_account);

		button_account_clear = (ImageView) this
				.findViewById(R.id.button_account_clear);
		button_account_clear.setOnClickListener(this);

		button_pwd_clear = (ImageView) this.findViewById(R.id.button_pwd_clear);
		button_pwd_clear.setOnClickListener(this);

		button_login = (Button) this.findViewById(R.id.button_login);
		button_login.setOnClickListener(this);

		button_register = (Button) this.findViewById(R.id.button_register);
		button_register.setOnClickListener(this);

		forget_pwd = (TextView) this.findViewById(R.id.forget_pwd);
//		forget_pwd.setOnClickListener(this);

		edittext_password = (EditText) this
				.findViewById(R.id.edittext_password);
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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.current_content_title_back:
			// / sysback

			this.finish();
			break;
		case R.id.button_account_clear:
			// /account

			edittext_account.setText("");
			break;
		case R.id.button_pwd_clear:
			// /password

			edittext_password.setText("");
			break;
		case R.id.button_login:
			// /login

			String username = edittext_account.getText().toString().trim();
			String password = edittext_password.getText().toString().trim();

			if (TextUtils.isEmpty(username)) {
				edittext_account.setError("用户名不能为空");
				edittext_account.setText("");
				return;
			}
			if (TextUtils.isEmpty(password)) {
				edittext_password.setError("密码不能为空");
				edittext_password.setText("");
				return;
			}
			LoginTask loginTask = new LoginTask();

			loginTask.execute(username, password);

			break;
		case R.id.button_register:
			// /register

			// ToastUtils.show(getApplicationContext(), "自己想办法去");

			startActivity(new Intent(this, AtyUserReg1.class));
			break;
		case R.id.forget_pwd:
			// / forgetpassword
			ToastUtils.show(getApplicationContext(), "开玩笑,你怎么能把密码给丢了?");

			break;

		default:
			break;
		}
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

//			String androidSdk = android.os.Build.VERSION.SDK;

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
				logindialog = createDialog(View.VISIBLE, "登录中...");
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
				System.out.println("loginResult---->>>>"+ loginResult);
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
				UserInfo userInfo = HomeItemInfoHandler.parseUserInfo(loginResult);
				System.out.println("loginresult------>>>>"+loginResult);
				Intent intent = new Intent(AtyUserLogin.this, AtyUserCenter.class);
				intent.putExtra(UserInfo.TAG.TAG_CLASSNAME, userInfo);
				PreferencesUtils.putString(getApplicationContext(),
						UserInfo.TAG.TAG_CLASSNAME, loginResult);
				PreferencesUtils.putBoolean(getApplicationContext(),
						SYSCS.PS.DEF_PNAME, true);
				logindialog.dismiss();
				startActivity(intent);
				AtyUserLogin.this.finish();

			} else {
				logindialog.dismiss();
				// /用户名或密码错误
				// Dialog errordialog = createDialog(View.GONE, "用户名或密码错误");
				// errordialog.show();
				ToastUtils.show(AtyUserLogin.this, "用户名或密码错误");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				edittext_password.setText("");

			}
		}
	}

}
