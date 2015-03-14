package com.samuel.downloader.app;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.samuel.downloader.utils.HttpClientUtil;
import com.samuel.downloader.utils.SYSCS;

public class AtyUserReg1 extends Activity implements OnClickListener {

	protected TextView current_content_title_back,current_content_title;

	protected AutoCompleteTextView edittext_account;

	protected EditText edittext_password;

	protected Button reg_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_user_reg1_activity);
		initView();

	}

	private void initView() {
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
//		current_content_title_back.setOnClickListener(this);
		current_content_title_back.setVisibility(View.GONE);
		
		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
//		current_content_title_back.setOnClickListener(this);
		current_content_title.setText("用户注册");

		edittext_account = (AutoCompleteTextView) this
				.findViewById(R.id.edittext_account);
		edittext_password = (EditText) this
				.findViewById(R.id.edittext_password);

		reg_next = (Button) this.findViewById(R.id.reg_next);
		reg_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.current_content_title_back:
			this.finish();
			break;

		case R.id.reg_next:
			String userName = edittext_account.getText().toString().trim();

			String password = edittext_password.getText().toString().trim();

			if (userName == null || userName.length() < 6 || userName == ""
					|| userName.contains(" ") || userName.contains("-")
					|| userName.contains("+") || userName.contains("*")
					|| userName.contains("/") || userName.contains(".")) {
				edittext_account.setError("用户名长度不得少于6位", getResources()
						.getDrawable(R.drawable.app_download_warn));
				edittext_account.setText("");
			} else if (password == null || password.length() < 6
					|| password == "" || password.contains(" ")) {
				edittext_password.setError("密码长度不符合安全标准,请重新输入", getResources()
						.getDrawable(R.drawable.app_download_warn));
				edittext_password.setText("");
			} else {

				CheckUserNameTask checkUserNameTask = new CheckUserNameTask();
				checkUserNameTask.execute(userName, password);
			}

			break;

		default:
			break;
		}
	}

	protected final class CheckUserNameTask extends
			AsyncTask<String, Void, String> {

		protected String username;

		protected String password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String username = params[0];
			String password = params[1];
			this.username = username;
			this.password = password;

			try {
				String loginResult = HttpClientUtil.getRequest(
						SYSCS.CONFCS.CHECKUSERNAME, "?username=" + username);
				return loginResult;
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

			if (loginResult != null && loginResult != "") {

				System.out.println("loginresult---->>>" + loginResult);
				if (loginResult.contains("true")) {

					Intent intent = new Intent(AtyUserReg1.this,
							AtyUserReg2.class);
					intent.putExtra("username", username);
					intent.putExtra("password", password);
					startActivity(intent);
					AtyUserReg1.this.finish();

				} else if (loginResult.contains("false")) {

					edittext_account.setError(
							"您输入的用户名已被使用,请重新输入",
							getResources().getDrawable(
									R.drawable.app_download_error));
					edittext_account.setText("");
				}
			}
		}
	}

}
