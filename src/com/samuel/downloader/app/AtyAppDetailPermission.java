package com.samuel.downloader.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.samuel.downloader.bean.AppInfo;

public class AtyAppDetailPermission extends Activity implements
		OnClickListener, OnItemClickListener {

	protected ListView app_details_permission_lv;

	protected TextView current_content_title_back,current_content_title;

	protected List<Map<String, String>> appPermission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_details_permission);
		initData();
		initView();
	}

	private void initData() {
		appPermission = new ArrayList<Map<String, String>>();
		String appPer = getIntent().getStringExtra(
				AppInfo.TAG.TAG_APPPERMISSION).trim();
		// System.out.println(appPer);
		if (appPer != null && appPer != "" && appPer != "null") {
			if (appPer.contains(",")) {

				for (String per : appPer.split(",")) {
					Map<String, String> map = new HashMap<String, String>();
					System.out.println(per);
					map.put("app_details_tv_permision_title", per);
					appPermission.add(map);
				}
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("app_details_tv_permision_title", appPer);
				System.out.println(appPer);
				appPermission.add(map);
			}
		}
	}

	private void initView() {
		app_details_permission_lv = (ListView) this
				.findViewById(R.id.app_details_permission_lv);
		app_details_permission_lv.setAdapter(new SimpleAdapter(
				getApplicationContext(), appPermission,
				R.layout.app_details_permission_item,
				new String[] { "app_details_tv_permision_title" },
				new int[] { R.id.app_details_tv_permision_title }));
		app_details_permission_lv.setOnItemClickListener(this);
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
		current_content_title_back.setOnClickListener(this);

		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title.setText("权限详情");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.current_content_title_back:
			this.finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

}
