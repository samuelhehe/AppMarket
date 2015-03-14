package com.samuel.downloader.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.DownLoaderContentInfo;
import com.samuel.downloader.dao.DownLoadAppService;
import com.samuel.downloader.infocenter.app.ShopList;
import com.samuel.downloader.service.HomeItemInfoHandler;


public class FrgAppMgr extends Fragment implements OnClickListener {

	public static final Integer TYPE_UPDATE = 0;

	public static final Integer TYPE_DOWNLOAD = 1;

	protected static ArrayList<AppInfo> needupdateapps = null;

	TextView appdel_detail_tv = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.app_mgr_perview, container,
				false);

		/**
		 * app_mgr_perview_update
		 * 
		 * app_mgr_perview_appmgr
		 * 
		 * app_mgr_perview_downloadmgr
		 */

		initUpdateView(rootView);

		initappmgrView(rootView);

		initappDownloadMgrView(rootView);

		initmsgmgrView(rootView);

		/**
		 * item_body RelativeLayout icon 图标 new_image_tag_view 尝鲜 item_name 管理
		 * item_detail 详情
		 */
		return rootView;
	}

	private void initmsgmgrView(View rootView) {

		View appdlView = rootView.findViewById(R.id.app_mgr_perview_msgmgr);

		ImageView appdl_img = (ImageView) appdlView.findViewById(R.id.icon);
		appdl_img.setImageDrawable(getResources().getDrawable(
				R.drawable.app_preview_icon_msg));
		TextView appdl_tv = (TextView) appdlView.findViewById(R.id.item_name);
		appdl_tv.setText("消息中心");

		appdel_detail_tv = (TextView) appdlView.findViewById(R.id.item_detail);
		appdel_detail_tv.setVisibility(View.GONE);
		appdlView.setOnClickListener(this);

	}

	// // load how many app need to update
	protected void asyLoadData(TextView textView, Context context) {
		AsyncLoadEmergencyData haha = new AsyncLoadEmergencyData(textView,
				context);
		haha.execute();
	}

	protected static final class AsyncLoadEmergencyData extends
			AsyncTask<Void, Void, List<AppInfo>> {
		private TextView textView;
		private Context context;
		public AsyncLoadEmergencyData(TextView textView, Context context) {
			this.textView = textView;
			this.context = context;
		}
		@Override
		protected List<AppInfo> doInBackground(Void... params) {
			List<AppInfo> appInfos = HomeItemInfoHandler
					.getNeedUpdateAppList(context);
			return appInfos;
		}

		@Override
		protected void onPostExecute(List<AppInfo> result) {
			super.onPostExecute(result);
			if (result != null && result.size() >= 1) {
				for (Iterator<AppInfo> iterator = result.iterator(); iterator
						.hasNext();) {
					AppInfo appInfo = iterator.next();
					System.out.println("appInfo----->>>" + appInfo.toString());
				}
				needupdateapps = (ArrayList<AppInfo>) result;
				for (Iterator<AppInfo> iterator = needupdateapps.iterator(); iterator
						.hasNext();) {
					AppInfo appInfo = iterator.next();
					System.out.println("update---->>>" + appInfo.toString());
				}
				textView.setVisibility(View.VISIBLE);
				textView.setText(String.format("您有%1$d项应用需要更新", needupdateapps.size()));
			} else {
				textView.setText("您的应用都是最新的^0^");
			}
			textView.setVisibility(View.GONE);
		}
	}

	/**
	 * initappdownloadmgrView
	 * 
	 * @param rootView
	 */
	private void initappDownloadMgrView(View rootView) {
		View appdlView = rootView
				.findViewById(R.id.app_mgr_perview_downloadmgr);
		ImageView appdl_img = (ImageView) appdlView.findViewById(R.id.icon);
		appdl_img.setImageDrawable(getResources().getDrawable(
				R.drawable.button_download));
		TextView appdl_tv = (TextView) appdlView.findViewById(R.id.item_name);
		appdl_tv.setText("下载管理");
		appdel_detail_tv = (TextView) appdlView.findViewById(R.id.item_detail);
		// appdel_detail_tv.setText("当前没有下载任务");
		// LoadDownloadTask ldlts = new LoadDownloadTask(appdel_detail_tv,
		// getActivity());
		// ldlts.execute();
		appdel_detail_tv.setVisibility(View.GONE);
		appdlView.setOnClickListener(this);

	}

	private void initUpdateView(View rootView) {
		View updateView = rootView.findViewById(R.id.app_mgr_perview_update);

		ImageView update_img = (ImageView) updateView.findViewById(R.id.icon);
		update_img.setImageDrawable(getResources().getDrawable(
				R.drawable.button_update));
		TextView update_tv = (TextView) updateView.findViewById(R.id.item_name);
		update_tv.setText("应用更新");
		TextView update_detail_tv = (TextView) updateView
				.findViewById(R.id.item_detail);
		asyLoadData(update_detail_tv, getActivity());
		update_detail_tv.setText("您的应用都是最新的^0^");
		update_detail_tv.setVisibility(View.GONE);

		updateView.setOnClickListener(this);
	}

	private void initappmgrView(View rootView) {
		View appmgrView = rootView.findViewById(R.id.app_mgr_perview_appmgr);

		ImageView appmgr_img = (ImageView) appmgrView.findViewById(R.id.icon);
		appmgr_img.setImageDrawable(getResources().getDrawable(
				R.drawable.button_installed));
		TextView appmgr_tv = (TextView) appmgrView.findViewById(R.id.item_name);
		appmgr_tv.setText("应用管理");
		// appmgr_tv.setVisibility(View.VISIBLE);

		appmgrView.setOnClickListener(this);
	}

	/**
	 * 
	 * load data the listView required
	 */

	static class LoadDownloadTask extends
			AsyncTask<Void, Void, List<DownLoaderContentInfo>> {
		private TextView appdel_detail_tv;
		private Context context;

		public LoadDownloadTask(TextView textView, Context context) {
			this.appdel_detail_tv = textView;
			this.context = context;
		}

		@Override
		protected List<DownLoaderContentInfo> doInBackground(Void... params) {
			return DownLoadAppService.getAllTaskInfos(context);
		}

		@Override
		protected void onPostExecute(List<DownLoaderContentInfo> datas) {
			super.onPostExecute(datas);
			if (datas != null && datas.size() >= 1) {
				if (datas.size() >= 1) {
					appdel_detail_tv.setText(String.format("当前有%d项任务正在下载",
							datas.size()));
				} else if (datas.size() == 0) {
					appdel_detail_tv.setText("当前没有下载任务");
				}
			} else {
				appdel_detail_tv.setText("当前没有下载任务");
			}
			appdel_detail_tv.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.app_mgr_perview_update:

			Intent updateintent = new Intent();
			updateintent.setClass(getActivity(), AtyAppMgr.class);
			updateintent.setFlags(R.id.app_mgr_perview_update);
			if (needupdateapps != null && needupdateapps.size() >= 1) {
				updateintent.putExtra(AppInfo.TAG.TAG_UPDATES, needupdateapps);
			}
			startActivity(updateintent);
			break;
		case R.id.app_mgr_perview_appmgr:
			Intent appmgrintent = new Intent();
			appmgrintent.setClass(getActivity(), AtyAppMgr.class);
			appmgrintent.setFlags(R.id.app_mgr_perview_appmgr);
			startActivity(appmgrintent);

			break;
		case R.id.app_mgr_perview_downloadmgr:
			Intent appdlintent = new Intent();
			// appdlintent.setClass(getActivity(), DownLoadMgrActivity.class);
			// appdlintent.setClass(getActivity(), DownLoadingActivity.class);
			appdlintent.setClass(getActivity(), AtyDownloadMgr.class);

			startActivity(appdlintent);
			break;
		case R.id.app_mgr_perview_msgmgr:

			Intent appmsgmgr = new Intent();
			appmsgmgr.setClass(getActivity(), ShopList.class);
			startActivity(appmsgmgr);
			break;

		}

	}

}
