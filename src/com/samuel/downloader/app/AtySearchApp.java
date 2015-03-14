package com.samuel.downloader.app;

import java.util.List;

import net.tsz.afinal.FinalDBChen;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.samuel.downloader.adapter.MainItemAdapter;
import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.utils.ToastUtils;


public class AtySearchApp extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnFocusChangeListener, OnKeyListener {

	protected TextView current_content_title, current_content_title_back;

	protected ImageView search_close_btn;

	protected EditText search_src_text;

	protected ListView sys_search_result_lv;

	private FinalDBChen db;

	protected static List<AppInfo> datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sys_search_activity);
		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title_back = (TextView) this.findViewById(R.id.current_content_title_back);
		current_content_title.setText("应用搜索");
		current_content_title.setTextColor(getResources().getColor(
				R.color.webapp_black));
		current_content_title_back.setOnClickListener(this);

		search_close_btn = (ImageView) this.findViewById(R.id.search_close_btn);

		search_close_btn.setOnClickListener(this);
		search_src_text = (EditText) this.findViewById(R.id.search_src_text);
		search_src_text.setOnKeyListener(this);
		search_src_text.setOnFocusChangeListener(this);

		sys_search_result_lv = (ListView) this
				.findViewById(R.id.sys_search_result_lv);
		sys_search_result_lv.setOnItemClickListener(this);

		// 创建一个数据库
		new FinalDBChen(getApplicationContext(), DBNAME,
				new DownloadFileItem(), TABNAME_DOWNLOADTASK, null);
		db = new FinalDBChen(getApplicationContext(), getApplicationContext()
				.getDatabasePath(DBNAME).getAbsolutePath());

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (datas != null) {
			Intent intent = new Intent(getApplicationContext(),
					AtyAppDetails.class);
			intent.setFlags(R.layout.app_themes_boutiques);
			intent.putExtra(AppInfo.TAG.TAG_CLASSNAME, datas.get(position));
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.current_content_title_back:
			this.finish();
			break;
		case R.id.search_close_btn:
			search_src_text.setText("");
			search_src_text.setFocusableInTouchMode(true);
			search_close_btn.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	protected boolean search_task = true;

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		switch (v.getId()) {
		case R.id.search_src_text:
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				if (search_task) {
					String sk = search_src_text.getText().toString().trim();
					if (sk != null && sk != "" & sk.length() >= 1) {
						SearchTask st = new SearchTask();
						st.execute(sk);
						search_task = false;
					} else {
						ToastUtils.show(getApplicationContext(), "搜索关键字不能为空");
						search_task = true;
					}
				}

			}
			break;
		}
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		switch (v.getId()) {
		case R.id.search_src_text:
			if (hasFocus) {
				search_src_text.setCursorVisible(true);
				search_close_btn.setVisibility(View.VISIBLE);
			} else {
				search_src_text.setCursorVisible(false);
				search_close_btn.setVisibility(View.INVISIBLE);
			}
			break;
		default:
			break;
		}

	}

	protected MainItemAdapter adapter;

	protected class SearchTask extends AsyncTask<String, Void, List<AppInfo>> {

		@Override
		protected List<AppInfo> doInBackground(String... params) {
			String searchKey = params[0].toString().trim();
			if (searchKey != null && searchKey != "" && searchKey.length() >= 1) {
				System.out.println("searchKey -------->>> " + searchKey);
				return HomeItemInfoHandler.searchTask(AtySearchApp.this, 0, 20, searchKey);
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<AppInfo> result) {
			super.onPostExecute(result);

			if (result != null) {
				datas = result;
				adapter = new MainItemAdapter(result, R.layout.simple_item,
						AtySearchApp.this, db);
				if (sys_search_result_lv != null) {
//					adapter.notifyDataSetChanged();
					sys_search_result_lv.setAdapter(adapter);
					sys_search_result_lv.setVisibility(View.VISIBLE);

				}
				search_task = true;
			} else {
				sys_search_result_lv.setVisibility(View.GONE);
				ToastUtils.show(getApplicationContext(), "获取应用列表为空,请更换关键字后重试");
				search_task = true;
			}
		}

	}

}
