package com.samuel.downloader.app;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.samuel.downloader.adapter.CategoryAdapter;
import com.samuel.downloader.bean.CategoryInfo;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.utils.ToastUtils;


///// back up 
public class FrgCategory2 extends Fragment  implements OnClickListener{

	protected static final int LOAD_COMPLETE = 1;

	protected static final int LOAD_FAIL = 0;

	private ListView installedApp_lv = null;

	private List<CategoryInfo> data = null;
	
	protected LinearLayout loading_error_retry_layout;

	protected View loading_pb;

	protected View sys_retry;

	protected TextView not_network_retry_tv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View parentView = inflater.inflate(R.layout.app_themes_category,
				container, false);
		installedApp_lv = (ListView) parentView
				.findViewById(R.id.app_themes_category_lv);
		installedApp_lv.setOnItemClickListener(new ItemClickListener());
		
		loading_error_retry_layout = (LinearLayout) parentView
				.findViewById(R.id.loading_error_retry_layout);

		loading_pb = loading_error_retry_layout.findViewById(R.id.loading_pb);
		loading_pb.setVisibility(View.VISIBLE);

		sys_retry = loading_error_retry_layout.findViewById(R.id.sys_retry);
		sys_retry.setVisibility(View.GONE);

		not_network_retry_tv = (TextView) sys_retry
				.findViewById(R.id.not_network_retry_tv);
		not_network_retry_tv.setOnClickListener(this);
		installedApp_lv.setVisibility(View.GONE);

		
		
		
		return parentView;
	}

	protected Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOAD_COMPLETE:
				if (installedApp_lv != null) {
					data = (List<CategoryInfo>) msg.obj;
					installedApp_lv.setAdapter(new CategoryAdapter(getActivity(),
							data, R.layout.app_themes_category_item));
					loading_error_retry_layout.setVisibility(View.GONE);
					installedApp_lv.setVisibility(View.VISIBLE);
				}
				break;
			case LOAD_FAIL:
				loading_error_retry_layout.setVisibility(View.VISIBLE);
				installedApp_lv.setVisibility(View.GONE);
				loading_pb.setVisibility(View.GONE);
				sys_retry.setVisibility(View.VISIBLE);
				
				ToastUtils.show(getActivity(), "获取应用列表失败,请稍候重试");
				break;
			}

		};

	};

	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					data = HomeItemInfoHandler.getCategoryInfo(0, 15);
					
					if (data != null && data.size() > 0) {
						handler.sendMessage(handler.obtainMessage(
								LOAD_COMPLETE, data));
					} else {
						handler.sendMessage(handler.obtainMessage(LOAD_FAIL));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.not_network_retry_tv:
			sys_retry.setVisibility(View.GONE);
			loading_pb.setVisibility(View.VISIBLE);
			loadData();
			break;
		default:
			break;
		}
	}
	private final class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (data != null) {
				String categoryId = data.get(position).getCategoryId();
				String appcategory = data.get(position).getAppCategory();
				Intent intent = new Intent(getActivity(), AtyCategory.class);
				Bundle bundle = new Bundle();
				bundle.putString(CategoryInfo.TAG.TAG_CATEGORYID, categoryId);
				bundle.putString(CategoryInfo.TAG.TAG_APPCATEGORY, appcategory);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}

	}

}
