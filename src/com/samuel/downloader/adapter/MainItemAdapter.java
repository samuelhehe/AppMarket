package com.samuel.downloader.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDBChen;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.samuel.downloader.app.AtyDownloadMgr;
import com.samuel.downloader.app.R;
import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.bean.DownLoaderContentInfo;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.cache.image.ImageDownloader;
import com.samuel.downloader.dao.DownLoadAppService;
import com.samuel.downloader.tools.ImageLoader;
import com.samuel.downloader.utils.AppMarketUtils;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.MD5;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.TextFormater;
import com.samuel.downloader.utils.TimeUtils;
import com.samuel.downloader.utils.ToastUtils;

public class MainItemAdapter extends BaseAdapter implements ContentValue {

	protected List<AppInfo> datas;
	protected LayoutInflater inflater;
	protected int itemres;
	private FinalDBChen db;
	protected static Context context;
	protected ImageDownloader imageDownloader;

	public MainItemAdapter(List<AppInfo> datas, int itemres, Context context,
			FinalDBChen db) {
		this.datas = datas;
		this.itemres = itemres;
		this.context = context;
		this.db = db;
		imageDownloader = new ImageDownloader(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MainItemAdapter(List<AppInfo> result, int simpleItem,
			Context applicationContext) {

		this.datas = result;
		this.itemres = simpleItem;
		this.context = applicationContext;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {

		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView appIcon = null;
		TextView appName = null;
		TextView companyName = null;
		TextView button_tv = null;
		/**
		 * lefticon img toptxt tv name bottomtxt tv neirong btn1 tv detail
		 */
		if (convertView == null) {
			convertView = inflater.inflate(itemres, null);
			appIcon = ( ImageView) convertView.findViewById(R.id.lefticon);
			appName = (TextView) convertView.findViewById(R.id.toptxt);
			companyName = (TextView) convertView.findViewById(R.id.bottomtxt);
			button_tv = (TextView) convertView.findViewById(R.id.btn1);
			convertView.setTag(new DataWrapper(appIcon, appName, companyName,
					button_tv));
		} else {
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			appIcon = dataWrapper.appIcon;
			appName = dataWrapper.appName;
			companyName = dataWrapper.versionName;
			button_tv = dataWrapper.button_tv;

		}
		AppInfo appInfo = datas.get(position);
		// appInfo.toString();

		String appNameTitle = appInfo.getAppName();
		if (appNameTitle.length() > 16) {
			appNameTitle = appNameTitle.substring(0, 16);
			appNameTitle = appNameTitle + "...";
		}
		appName.setText(appNameTitle);

		String appCompanyName = appInfo.getCompanyName();
		if (appCompanyName.length() > 10) {
			appCompanyName = appCompanyName.substring(0, 10);
			appCompanyName = appCompanyName + "...";
		}
		companyName.setText(appCompanyName);
		int flag = appInfo.getAppLocalStatus();
		switch (flag) {
		case CompareableLocalAppInfo.TAG.flag_download:
			button_tv.setText("下载");
			button_tv.setTextColor(context.getResources().getColor(
					R.color.ems_white));
			button_tv.setClickable(true);
			button_tv.setEnabled(true);
			break;
		case CompareableLocalAppInfo.TAG.flag_downloading:
			button_tv.setText("下载中");
			button_tv.setTextColor(context.getResources().getColor(
					R.color.ems_black));
			// button_tv.setClickable(false);
			button_tv.setEnabled(false);
			break;
		case CompareableLocalAppInfo.TAG.flag_installed:
			button_tv.setText("启动");
			button_tv.setTextColor(context.getResources().getColor(
					R.color.ems_white));
			button_tv.setEnabled(true);
			// button_tv.setClickable(false);
			break;
		case CompareableLocalAppInfo.TAG.flag_update:
			button_tv.setText("升级");
			button_tv.setTextColor(context.getResources().getColor(
					R.color.ems_white));
			button_tv.setEnabled(true);
			button_tv.setClickable(true);
			break;
		case CompareableLocalAppInfo.TAG.flag_isDownload:
			button_tv.setText("安装");
			button_tv.setTextColor(context.getResources().getColor(
					R.color.ems_white));
			button_tv.setClickable(true);
			button_tv.setEnabled(true);
			break;

		}
		button_tv.setOnClickListener(new DownloadClick(appInfo, button_tv));
		String imageUrl = getImageUrl(appInfo.getAppIcon());

		// AlphaAnimation alp = new AlphaAnimation(0.0f, 1.0f);
		// alp.setDuration(1000);
		// appIcon.setAnimation(alp);
		if (imageUrl != null) {
			appIcon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.app_icon_tbd));
			imageDownloader.download(imageUrl, appIcon);
		} else {
			appIcon.setImageDrawable(context.getResources().getDrawable(
					R.drawable.app_icon_tbd));
		}
		// asyncImageLoad(appIcon, appInfo.getAppIcon());
		return convertView;

	}

	private String getImageUrl(String imageurl) {

		if (imageurl == null || imageurl == "" || imageurl.length() <= 4) {
			return null;
		}
		return SYSCS.CONFCS.FILE_IMAGE_APPICON + imageurl;
	}

	public List<DownloadFileItem> getItems() {

		return getItems(datas);
	}

	public List<DownloadFileItem> getItems(List<AppInfo> appInfos) {

		List<DownloadFileItem> downloadFileItems = new ArrayList<DownloadFileItem>();
		for (AppInfo appInfo : appInfos) {
			downloadFileItems.add(appInfo.convetToDownloadFileItem());
		}
		return downloadFileItems;
	}

	class DownloadClick implements OnClickListener {

		// private DownloadFileItem downLoadFileItem;

		private AppInfo appInfo;

		private Button bt;
		private int position;
		private TextView btn;

		public DownloadClick(AppInfo appInfo, TextView btn) {
			this.btn = btn;
			this.appInfo = appInfo;
		}

		// public DownloadClick(DownloadFileItem downloadFileItem, TextView btn)
		// {
		// this.btn = btn;
		// this.downLoadFileItem = downloadFileItem;
		// }

		public void toDownload(DownloadFileItem downLoadFileItem) {

			System.out.println(downLoadFileItem.getFileName()
					+ ":DOWNLOAD_STATE_START" + ":toDownload");

			System.out.println("fileUrl --->>" + downLoadFileItem.getFileUrl());

			System.out.println("filePath ---->>"
					+ downLoadFileItem.getFilePath());

			System.out.println("appPackageName----->>"
					+ downLoadFileItem.getPackagename());

			SYSCS.setStartDownloadFileItem(downLoadFileItem);
			context.sendBroadcast(new Intent().setAction("download"));
			// btn.setClickable(false);
			// 将最初的数据保存到数据库.主要是为了 让数据库中的数据第一反应与所操作的状态一致,因为在后面的下载任务中.在线程中更改数据库状态
			// 如果在线程未开始且程序退出的情况.那么这个状态会不正确
			List<DownloadFileItem> ls = db.findItemsByWhereAndWhereValue(
					"fileName", downLoadFileItem.getFileName(),
					DownloadFileItem.class, TABNAME_DOWNLOADTASK, null);
			if (ls.size() == 0) {
				ToastUtils.show(context, "已加入下载管理器");
				// 说明没有此条数据
				btn.setText("下载中");
				db.insertObject(downLoadFileItem, TABNAME_DOWNLOADTASK);
				downLoadFileItem.setPosition(position);
				downLoadFileItem.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
				Intent appdlintent = new Intent();
				appdlintent.setClass(context, AtyDownloadMgr.class);
				context.startActivity(appdlintent);
			} else {

				ToastUtils.show(context, "任务已存在,跳转至下载管理器");
				downLoadFileItem.setPosition(position);
				downLoadFileItem.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
				btn.setText("下载中");
				// /跳转至下载管理器
				context.startActivity(new Intent(context, AtyDownloadMgr.class));

				// 更新这个状态
				db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "fileName=?",
						new String[] { downLoadFileItem.getFileName() },
						downLoadFileItem);
			}
		}

		@Override
		public void onClick(View v) {
			if (appInfo.getAppLocalStatus() == CompareableLocalAppInfo.TAG.flag_isDownload) {
				String filePath = appInfo.convetToDownloadFileItem()
						.getFilePath();
				if (filePath != null) {
					AppMarketUtils.install(context, filePath);
				}
			} else if (appInfo.getAppLocalStatus() == CompareableLocalAppInfo.TAG.flag_installed) {

				String packageName = appInfo.getPackageName();
				if (TextFormater.isEmpty(packageName)) {
					ToastUtils.show(context, "应用启动出错,请刷新后重试,或在主界面中启动");
				} else {
					ApplicationDetailInfo appDetailInfo = new ApplicationDetailInfo(
							context);
					appDetailInfo.launchApp(packageName);
				}
			} else {
				toDownload(appInfo.convetToDownloadFileItem());
			}

		}

	}

	/**
	 * no use
	 * 
	 * @author Administrator
	 * 
	 */
	protected final class ButtonClickListener implements OnClickListener {

		protected AppInfo appInfo;
		private String fileEntity;

		public ButtonClickListener(AppInfo appInfo) {
			this.appInfo = appInfo;
		}

		@Override
		public void onClick(View v) {

		}

		// / not use
		public int convertImpData(String appId) {
			appId = appId.replace("APP", "122");
			System.out.println("转换appId----------->>>" + appId);
			return Integer.valueOf(appId);
		}

		// /not use
		public void onClick2(View v) {
			switch (v.getId()) {
			case R.id.btn1:
				fileEntity = appInfo.getFileEntity().toString().trim();
				if (fileEntity == null || fileEntity == ""
						|| fileEntity.length() <= 4) {
					return;
				}
				String fileUrl = SYSCS.CONFCS.FILE_ENTITY_APP + fileEntity;

				System.out.println("appfileUrl----------->>>" + fileUrl);
				DownLoadAppService downloader = new DownLoadAppService(context);
				DownLoaderContentInfo dlci = new DownLoaderContentInfo();
				dlci.setAppName(appInfo.getAppName());
				dlci.setAppId(appInfo.getAppId());
				dlci.setDlStatus(DownloadManager.STATUS_PENDING);
				dlci.setIconName(appInfo.getAppIcon());
				dlci.setTaskdate(TimeUtils.getCurrentTimeInLong());
				dlci.setAppUrl(fileUrl);
				// // query exists download task
				if (downloader.addTaskInfoToDB(dlci, context)) {
					ToastUtils.show(context, "已加入下载队列中");
				} else {
					ToastUtils.show(context, "下载任务已存在");
				}
				break;
			default:
				break;
			}
		}
	}

	public class DataWrapper {

		ImageView appIcon = null;
		TextView appName = null;
		TextView versionName = null;
		TextView button_tv = null;

		public DataWrapper(ImageView appIcon, TextView appName,
				TextView versionName, TextView button_tv) {
			super();
			this.appIcon = appIcon;
			this.appName = appName;
			this.versionName = versionName;
			this.button_tv = button_tv;
		}
	}

	private void asyncImageLoad(final ImageView imageView, String imageurl) {
		// if (imageurl == null || imageurl == "" || imageurl.length() <= 4) {
		// return;
		// }
		// imageurl = SYSCS.CONFCS.FILE_IMAGE_APPICON + imageurl;
		// imageView.setTag(imageurl);
		// AsyncLoadImage asyncLoadImage = new AsyncLoadImage();
		//
		// Drawable drawable = asyncLoadImage.loadDrawable(imageurl, new
		// AsyncLoadImage.ImageCallback(){
		//
		// @Override
		// public void imageLoad(Drawable image,String imageUrl) {
		// if(imageUrl.equals(imageView.getTag())){
		// imageView.setImageDrawable(image);
		// }
		// else {
		// System.out.println(imageUrl+ "连接不到啊...");
		// }
		// }
		// });
		// if(drawable==null){
		// imageView.setImageResource(R.drawable.app_icon_tbd);
		// }else{
		// imageView.setImageDrawable(drawable);
		// }
		//

		// AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		// Uri cachedImage = asyncImageLoader.loadUri(imageurl,
		// imageView, new ImageCallback() {
		// @Override
		// public void imageLoaded(Uri drawable, ImageView iv,
		// String url) {
		// if(url.equals(iv.getTag())){
		//
		// imageView.setImageURI(drawable);
		// }
		// }
		// });
		// if (cachedImage == null) {
		// imageView.setImageResource(R.drawable.app_icon_tbd);
		// } else {
		// imageView.setImageURI(cachedImage);
		// }

		AsyncImageTask asyncImgTask = new AsyncImageTask(imageView);

		asyncImgTask.execute(imageurl);

	}

	private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {
		protected ImageView imageView;

		public AsyncImageTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Uri doInBackground(String... params) {

			String imageurl = params[0];
			if (imageurl == null || imageurl == "" || imageurl.length() <= 4) {
				return null;
			}
			imageurl = SYSCS.CONFCS.FILE_IMAGE_APPICON + imageurl;

			try {
				return ImageLoader.getImage(imageurl,
						SYSCS.LCS.IMG_ICONCACHEENTITYURL,
						getImageUniqueName(imageurl), 70, 70, 0.5f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Uri result) {
			if (result != null && imageView != null) {
				imageView.setImageURI(result);
			}
		}

		protected String getImageUniqueName(String imageurl) {

			return MD5.getMD5(imageurl) + ".png";
		}

	}

	protected String getAppUniqueName(String appUrl) {

		return MD5.getMD5(appUrl) + ".apk";
	}
}
