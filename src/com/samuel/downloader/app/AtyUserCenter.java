package com.samuel.downloader.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.samuel.downloader.bean.UserInfo;
import com.samuel.downloader.utils.PreferencesUtils;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.ToastUtils;

public class AtyUserCenter extends Activity implements OnClickListener {

	TextView current_content_title_back,current_content_title;

	UserInfo userinfo;
	TextView app_user_account;
	TextView app_user_account_tv_name;
	TextView app_user_account_tv_notes;
	TextView app_user_account_tv_tel;
	Button app_user_account_btn_del;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_user_center);
		userinfo = (UserInfo) getIntent().getSerializableExtra(
				UserInfo.TAG.TAG_CLASSNAME);
		initView();
	}

	private void initView() {
		if (userinfo != null) {
			current_content_title = (TextView) this.findViewById(R.id.current_content_title);
			current_content_title.setText("个人中心");
			
			current_content_title_back = (TextView) this.findViewById(R.id.current_content_title_back);
			current_content_title_back.setVisibility(View.GONE);
			//			current_content_title_back.setOnClickListener(this);
			app_user_account = (TextView) this.findViewById(R.id.app_user_account);
			app_user_account.setText(extractString(R.string.app_user_account_hint,userinfo.getUserId()));
			app_user_account.setVisibility(View.GONE);
			app_user_account_tv_name = (TextView) this.findViewById(R.id.app_user_account_tv_name);
			app_user_account_tv_name.setText(extractString(R.string.app_user_account_name_hint,userinfo.getUserName()));
			app_user_account_tv_notes = (TextView) this.findViewById(R.id.app_user_account_tv_notes);
			app_user_account_tv_notes.setText(extractString(R.string.app_user_account_notes_hint,userinfo.getEmail()));
			app_user_account_tv_tel = (TextView) this.findViewById(R.id.app_user_account_tv_tel);
			app_user_account_tv_tel.setText(extractString(R.string.app_user_account_tel_hint,userinfo.getTel()));
			app_user_account_btn_del = (Button) this.findViewById(R.id.app_user_account_btn_del);
			app_user_account_btn_del.setTextColor(getResources().getColor(R.color.ems_white));
			app_user_account_btn_del.setOnClickListener(this);
		}
	}

	protected String extractString(int rsId, String value) {
		return String.format(getString(rsId), value);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.current_content_title_back:
			this.finish();
			break;
		case R.id.app_user_account_btn_del:
			// /删除帐户
			
			PreferencesUtils.remove(getApplicationContext(), SYSCS.UICS.ISREGISTERED_KEY);
			PreferencesUtils.remove(getApplicationContext(), UserInfo.TAG.TAG_CLASSNAME);
			ToastUtils.show(getApplicationContext(), "用户信息已清除");
			this.finish();
			
		default:
			break;
		}

	}

}
