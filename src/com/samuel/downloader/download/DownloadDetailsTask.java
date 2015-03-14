package com.samuel.downloader.download;

import java.io.File;

import net.tsz.afinal.FinalDBChen;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.downloader.app.R;
import com.samuel.downloader.bean.DownloadFileItem;
import com.samuel.downloader.utils.AppMarketUtils;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.SYSCS;


public class DownloadDetailsTask implements ContentValue {

	private Context mContext;
	private View view; // 条目的View
	private DownloadFileItem downLoadItem; // 下载任务
	private boolean comeDb;
	private FinalDBChen db;

	/**
	 * Title: Description:
	 */
	public DownloadDetailsTask(Context mContext, View view,
			DownloadFileItem downLoadMovieItem, boolean comeDb) {
		this.mContext = mContext;
		this.view = view;
		this.downLoadItem = downLoadMovieItem;
		this.comeDb = comeDb;
		ImageView del = (ImageView) view.findViewById(R.id.delete_movie);
		del.setOnClickListener(new DeleteClick());

		db = new FinalDBChen(mContext, mContext.getDatabasePath(DBNAME)
				.getAbsolutePath());
		gotoDownload(downLoadMovieItem, view);
	}

	/**
	 * @param downloadFileItem
	 * @param view
	 */
	public void gotoDownload(DownloadFileItem downloadFileItem, View view) {

		String url = downloadFileItem.getFileUrl();
		String path = downloadFileItem.getFilePath();

		System.out.println("downloadTask  url ------->>>" + url);
		System.out.println("downloadTask  path ------->>>" + path);

		if (comeDb) {
			// 如果数据来自数据库,将按钮背景设置为开始
			// 添加单机事件
			Button bt = (Button) view.findViewById(R.id.stop_download_bt);

			bt.setBackgroundResource(R.drawable.button_start);

			bt.setOnClickListener(new MyOnClick(DOWNLOAD_DB, downloadFileItem,bt));

		} else {
			// 直接下载
			downloadFileItem.setDownloadFile(new DownloadFile()
					.startDownloadFileByUrl(url, path, new DetailsCallBack(view,
							downloadFileItem)));
		}
	}

	public OnDeleteTaskListener getOnDeleteTaskListener() {
		return onDeleteTaskListener;
	}

	public void setOnDeleteTaskListener(
			OnDeleteTaskListener onDeleteTaskListener) {
		this.onDeleteTaskListener = onDeleteTaskListener;
	}

	public class MyOnClick implements OnClickListener {
		private int state;
		private DownloadFileItem downItem;
		private boolean clickState = false;
		private Button button;
		private TextView current_progress;

		/**
		 * Title: Description:
		 */
		public MyOnClick(int state, DownloadFileItem downItem, Button button) {
			this.state = state;
			this.downItem = downItem;
			this.button = button;
		}

		@Override
		public void onClick(View v) {
			String name = downItem.getFileName();

			switch (state) {
			case DOWNLOAD_STATE_SUCCESS:
				// 下载完成
				Toast.makeText(mContext, name + ":开始安装!", Toast.LENGTH_SHORT)
						.show();
				String filePath = downLoadItem.getFilePath();
				if (filePath != null) {
					AppMarketUtils.install(mContext, filePath);
				}
				break;
			case DOWNLOAD_STATE_FAIL:
				// 下载失败,与 开始可以执行同一个逻辑
				Toast.makeText(mContext, name + "将重新开始!", Toast.LENGTH_SHORT)
						.show();
				button.setVisibility(View.INVISIBLE);
				current_progress.setTextColor(Color.parseColor("#23b5bc"));
				current_progress.setText("等待中");
				gotoDownload(downLoadItem, view);
				break;
			case DOWNLOAD_STATE_START:
				if (clickState) {
					// 如果是第一次点击,默认状态下是 开始状态.点击之后暂停这个任务
					gotoDownload(downLoadItem, view);
					Toast.makeText(mContext, name + ":开始下载", Toast.LENGTH_SHORT).show();
					if (button != null) {
						// button.setBackgroundResource(R.drawable.button_stop);
						button.setVisibility(View.INVISIBLE);
						current_progress.setTextColor(Color.parseColor("#23b5bc"));
						current_progress.setText("等待中");
					}
					clickState = false;
				} else {
					downLoadItem.getDownloadFile().stopDownload();
					Toast.makeText(mContext, name + ":暂停", Toast.LENGTH_SHORT)
							.show();
					if (button != null) {
						button.setBackgroundResource(R.drawable.button_start);
					}
					clickState = true;
				}
				break;
			case DOWNLOAD_DB:
				// 点击之后开启下载任务
				String url = downLoadItem.getFileUrl();
				String path = downLoadItem.getFilePath();
				// ///下载文件开始
				downLoadItem.setDownloadFile(new DownloadFile()
						.startDownloadFileByUrl(url, path, new DetailsCallBack(
								view, downLoadItem)));
				break;
			}
		}

		public TextView getCurrent_progress() {
			return current_progress;
		}
		public void setCurrent_progress(TextView current_progress) {
			this.current_progress = current_progress;
		}

	}

	private OnDeleteTaskListener onDeleteTaskListener;

	public interface OnDeleteTaskListener {
		// 当点击删除时执行的回调
		public void onDelete(View taskView, DownloadFileItem down);
	}
	// 删除一个指定的View
	class DeleteClick implements OnClickListener {
		public void onClick(View v) {
			if (onDeleteTaskListener != null) {
				onDeleteTaskListener.onDelete(view, downLoadItem);
			}
		}
	}

	/**
	 * 回调函数更新UI
	 */
	class DetailsCallBack extends AjaxCallBack<File> {

//		private ProgressBar p;
//		private TextView kb;
//		private TextView totalSize;
//		private int cu;
		private Button stop_download_bt;
		private TextView current_progress;
//		private TextView movie_name_item;
		private View view;
		private DownloadFileItem downloadFileItem;

		/**
		 * Title: Description:
		 */
		public DetailsCallBack(View view, DownloadFileItem down) {
			this.downloadFileItem = down;
			this.view = view;

			if (view != null) {
//				p = (ProgressBar) view.findViewById(R.id.download_progressBar);
				// /进度条
//				kb = (TextView) view.findViewById(R.id.movie_file_size);
				// 文件大小
//				totalSize = (TextView) view.findViewById(R.id.totalsize);
				// 总大小
				stop_download_bt = (Button) view
						.findViewById(R.id.stop_download_bt);
				// stopbtn
				stop_download_bt.setBackgroundResource(R.drawable.button_stop);

				current_progress = (TextView) view
						.findViewById(R.id.current_progress);
				// 当前进度
//				movie_name_item = (TextView) view
//						.findViewById(R.id.movie_name_item);
				// movie name

				stop_download_bt.setVisibility(View.INVISIBLE);
				stop_download_bt.setBackgroundResource(R.drawable.button_stop);

//				movie_name_item.setText(down.getFileName());
				current_progress.setTextColor(Color.parseColor("#23b5bc"));
				current_progress.setText("等待中");

				MyOnClick mc = new MyOnClick(DOWNLOAD_STATE_START, down,
						stop_download_bt);
				mc.setCurrent_progress(current_progress);

				stop_download_bt.setOnClickListener(mc);
			} else {
				System.out.println("View为空");
			}
		}

		/**
		 * (非 Javadoc) Title: onStart Description:
		 * 
		 * @see net.tsz.afinal.http.AjaxCallBack#onStart()
		 */
		@Override
		public void onStart() {
			System.out.println(downloadFileItem.getFileName() + ":开始下载");
			// 将数据库中的下载状态改为下载中

			downloadFileItem.setDownloadState(DOWNLOAD_STATE_START);
			db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "fileName=?",
					new String[] { downloadFileItem.getFileName() },
					downloadFileItem);

			// 发送开始下载的广播
			Intent i = new Intent();
			i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_START);
			i.setAction(DOWNLOAD_TYPE);

			//
			// MyApplcation app = (MyApplcation)
			// mContext.getApplicationContext();
			// app.setDownloadSuccess(down);

			SYSCS.setSuccessDownloadFileItem(downloadFileItem);
			mContext.sendBroadcast(i);
			super.onStart();
		}

		@Override
		public void onLoading(long count, long current) {

//			int cus = 0;
//			if (current > cu) {
//				cus = (int) (current - cu);
//				cu = (int) current;// 得到上一秒的进度
//			}
//			String m = Formatter.formatFileSize(mContext, cus) + "/s";
			// 获得当前进度百分比
			int pc = (int) ((current * 100) / count);

			if (view != null) {

				String currentSize = Formatter
						.formatFileSize(mContext, current);
				current_progress.setText(pc + "%");
				downloadFileItem.setPercentage(pc + "%");// 设置百分比
				downloadFileItem.setProgressCount(count);// 设置总大小进度条中使用
				downloadFileItem.setCurrentProgress(current);
				String tsize = Formatter.formatFileSize(mContext, count);// 总大小
				downloadFileItem.setFileSize(tsize);// 设置总大小字符串
//				totalSize.setText(currentSize + "/" + tsize);
//				kb.setText(m);
//				if (kb.getVisibility() == View.INVISIBLE) {
//					kb.setVisibility(View.VISIBLE);
//				}
//				p.setMax((int) count);
//				p.setProgress((int) current);
				// 如果按钮被隐藏了.将其显示
				if (stop_download_bt.getVisibility() == View.INVISIBLE) {
					stop_download_bt.setVisibility(View.VISIBLE);
					stop_download_bt.setText("");
					stop_download_bt.setBackgroundResource(R.drawable.button_stop);
				}
				// 将数据保存到数据库中
				downloadFileItem.setDownloadState(DOWNLOAD_STATE_DOWNLOADING);
				db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "fileName=?",
						new String[] { downloadFileItem.getFileName() },
						downloadFileItem);
			}
		}

		/**
		 * (非 Javadoc) Title: onSuccess Description:
		 * 
		 * @param t
		 * @see net.tsz.afinal.http.AjaxCallBack#onSuccess(java.lang.Object)
		 */
		@Override
		public void onSuccess(File t) {
			System.out.println(downloadFileItem.getFileName() + "：下载完成");
			Toast.makeText(mContext, downloadFileItem.getFileName() + "：下载完成",
					Toast.LENGTH_SHORT).show();
			if (view != null) {
//				kb.setVisibility(View.INVISIBLE);
				current_progress.setText("下载完成");
				stop_download_bt.setBackgroundResource(R.drawable.button_finish);
				stop_download_bt.setOnClickListener(new MyOnClick(DOWNLOAD_STATE_SUCCESS, downloadFileItem,stop_download_bt));
			}

			// 更新数据库的状态为下载完成
			downloadFileItem.setDownloadState(DOWNLOAD_STATE_SUCCESS);
			db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "fileName=?",
					new String[] { downloadFileItem.getFileName() },
					downloadFileItem);

			// 发送下载完成的广播
			Intent i = new Intent();
			i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_SUCCESS);
			i.setAction(DOWNLOAD_TYPE);
			SYSCS.setSuccessDownloadFileItem(downloadFileItem);
			mContext.sendBroadcast(i);
			super.onSuccess(t);
		}

		
		
		@Override
		public void onFailure(Throwable t, String strMsg) {
			System.out.println("下载失败：" + strMsg);
			// 更新数据库的状态为下载失败
			if (TextUtils.isEmpty(downloadFileItem.getFileSize())) {
				// 下载失败的情况设置总大小为0.0B,如果因为网络原因导致.没有执行OnLoading方法
				downloadFileItem.setFileSize("0.0B");
			}
			downloadFileItem.setDownloadState(DOWNLOAD_STATE_FAIL);
			db.updateValuesByJavaBean(TABNAME_DOWNLOADTASK, "fileName=?",
					new String[] { downloadFileItem.getFileName() },
					downloadFileItem);
			Toast.makeText(mContext,downloadFileItem.getFileName()+ "：下载失败!可能是网络超时或内存卡空间不足", Toast.LENGTH_SHORT).show();
		}
	}
}
