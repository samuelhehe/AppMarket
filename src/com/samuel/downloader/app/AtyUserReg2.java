package com.samuel.downloader.app;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samuel.downloader.bean.UserInfo;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.HttpClientUtil;
import com.samuel.downloader.utils.SYSCS;

public class AtyUserReg2 extends Activity implements OnClickListener {

	protected TextView current_content_title_back,current_content_title;

	protected EditText edittext_tel;

	protected RelativeLayout app_user_reg2_phoneno_layout;

	protected EditText edittext_phone_no;

	protected EditText edittext_email;

	protected Button reg_next;

	protected String userName;

	protected String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_user_reg2_activity);

		Intent intent = getIntent();
		userName = intent.getStringExtra("username");
		password = intent.getStringExtra("password");

		initView();

	}

	private String movePhone;

	private void initView() {
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
//		 current_content_title_back.setOnClickListener(this);
		current_content_title_back.setVisibility(View.GONE);
		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
//		 current_content_title_back.setOnClickListener(this);
		current_content_title.setText("用户注册");

		edittext_tel = (EditText) this.findViewById(R.id.edittext_tel);
		edittext_email = (EditText) this.findViewById(R.id.edittext_email);

		reg_next = (Button) this.findViewById(R.id.reg_next);
		reg_next.setOnClickListener(this);

		movePhone = getPhoneNo();
		if (movePhone == null || movePhone == "" || movePhone.length() < 1) {
			app_user_reg2_phoneno_layout = (RelativeLayout) this
					.findViewById(R.id.app_user_reg2_phoneno_layout);
			app_user_reg2_phoneno_layout.setVisibility(View.VISIBLE);
			edittext_phone_no = (EditText) this
					.findViewById(R.id.edittext_phone_no);
		}

	}

	private String getPhoneNo() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String num = tm.getLine1Number();
		// String imei =tm.getDeviceId();
		System.out.println("num------->>>" + num);

		if (num == null || num == "" || num == "null") {
			return "";
		}
		return num;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.current_content_title_back:
			this.finish();
			break;

		case R.id.reg_next:

			String phoneno = null;
			String nickname = "tom";
			if (movePhone == null || movePhone == "" || movePhone.length() < 1) {

				phoneno = edittext_phone_no.getText().toString().trim();
			} else {

				phoneno = movePhone;
			}
			String telphone = edittext_tel.getText().toString().trim();
			String email = edittext_email.getText().toString().trim();
			if (telphone == null || telphone.length() < 9
					|| telphone.contains(" ")) {

				edittext_tel.setError("分机格式不正确,请重新输入");
				edittext_tel.setText("");

			} else if (phoneno == null || phoneno.length() < 10
					|| phoneno.contains(" ")) {

				edittext_phone_no.setError("手机号码格式不正确,请重新输入");
				edittext_phone_no.setText("");
			} else if (email == null || email.length() <= 4
					|| !checkEmail(email)) {

				edittext_email.setError("email格式不正确,请重新输入");
				edittext_email.setText("");
			} else {
				/**
				 * ?username=" + username + "&password=" + password +
				 * "&nickname=" + getNickname() + "&telphone=" + getTelphone() +
				 * "&movephone=" + getMovephone() + "&email=" + email);
				 * 
				 */
				RegTask regTask = new RegTask();
				regTask.execute(userName, password, nickname, telphone,
						phoneno, email);
			}

			// startActivity(new Intent(this, UserRegActivity3.class));
			break;

		default:
			break;
		}
	}

	public static boolean checkEmail(String email) {
		// 验证邮箱的正则表达式
		String format = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
		if (email.matches(format)) {
			return true;
			// 邮箱名合法，返回true
		} else {
			return false;
			// 邮箱名不合法，返回false
		}
	}

	protected final class RegTask extends AsyncTask<String, Void, String> {

		/**
		 * userName,password,nickname, telphone,phoneno, email
		 * 
		 */
		private String nickname;
		private String telphone;
		private String movephone;
		private String email;
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
			// String androidSdk = android.os.Build.VERSION.SDK;
			String releaseVersion = android.os.Build.VERSION.RELEASE;
			String mac = wifiMgr.getConnectionInfo().getMacAddress();
			String combinDeviceInfo = null;
			try {
				String versionName = ApplicationDetailInfo
						.getPackageInfo(getApplicationContext()).versionName;
				combinDeviceInfo = "appVersion=" + versionName
						+ "&deviceType=Android" + devicetype.replace(" ", "")
						+ "&osVersion=" + releaseVersion + "&udId="
						+ mac.replace(":", "");
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return combinDeviceInfo;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			nickname = params[2];
			telphone = params[3];
			movephone = params[4];
			email = params[5];

			try {

				/**
				 * username=dd3&password=123&nickname=1&email=0&telphone=2&
				 * movephone=789
				 */
				String loginResult = HttpClientUtil.getRequest(
						SYSCS.CONFCS.USERREG, "?username=" + username
								+ "&password=" + password + "&nickname="
								+ nickname + "&telphone=" + telphone
								+ "&movephone=" + movephone + "&email=" + email
								+ "&" + combinDeviceInfo());
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

			if (loginResult.contains("1")) {

				Intent intent = new Intent(AtyUserReg2.this,
						AtyUserReg3.class);
				intent.putExtra("success", Boolean.TRUE);
				intent.putExtra(UserInfo.TAG.TAG_USERNAME, userName);
				intent.putExtra(UserInfo.TAG.TAG_PASSWORD, password);
				startActivity(intent);
				AtyUserReg2.this.finish();

			} else if (loginResult.contains("0")) {
				Intent intent = new Intent(AtyUserReg2.this,
						AtyUserReg3.class);
				intent.putExtra("success", Boolean.FALSE);
				startActivity(intent);
				AtyUserReg2.this.finish();
			}
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getTelphone() {
			return telphone;
		}

		public void setTelphone(String telphone) {
			this.telphone = telphone;
		}

		public String getMovephone() {
			return movephone;
		}

		public void setMovephone(String movephone) {
			this.movephone = movephone;
		}
	}

}
