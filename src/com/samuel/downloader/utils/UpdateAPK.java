package com.samuel.downloader.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

/**
 * 
 * @author samuel
 * @date 2013/12/11
 * @version 1.0.1
 */
public class UpdateAPK {

	private UpdateInfo updateInfo = null;
	private String localVersion = null;
	private Context context;

	private Handler handler;
	private String downUrl = null;
	private static final String savePath = "/sdcard/updatedemo/";

	private static final String saveFileName = savePath + String.valueOf(new Date().getTime())+".apk";

	public UpdateAPK(Context context, Handler handler) {
		this.context = context;
		this.setHandler(handler);
	}

	public void updateAndDownLoad(UpdateInfo info, String local) {
		updateInfo = new UpdateInfo();
		updateInfo = info;
		downUrl = info.getUrl();
		localVersion = local;

		System.out.println(" --->>> " + downUrl);
		System.out.println(" --->>> " + localVersion);
		downLoadVersion();
	}

	/**
	 * download version
	 * 
	 */
	private void downLoadVersion() {
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:").append(localVersion).append(",\n新版本:")
				.append(updateInfo.getVersioncode()).append(",\n版本特性:")
				.append(updateInfo.getDescription()).append("\n確定更新");

		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("软件版本更新").setMessage(sb.toString()) // 设置提示内容
				.setPositiveButton("下载", // 按钮名称
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								UpdateAsynTask updateAsynTask = new UpdateAsynTask();
								updateAsynTask.execute(updateInfo.getUrl(),
										savePath, saveFileName);
							}
						});
		// builder.setNegativeButton("暂不更新",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// // 点击"取消按钮之后退出程序"
		// dialog.dismiss();
		//
		// }
		// });

		builder.setCancelable(false).create().show();

	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private final class UpdateAsynTask extends AsyncTask<String, Integer, File> {
		private ProgressDialog pBar;

		@Override
		protected void onPreExecute() {
			pBar = new ProgressDialog(context);
			pBar.setTitle("正在下载...");
			pBar.setMessage("请稍候...");
			pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pBar.setCancelable(false);
			pBar.show();
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			pBar.setProgress(values[0]);
			pBar.setMessage("等一下就好...");

		}

		@Override
		protected File doInBackground(String... params) {

			String downUrl = params[0].toString();
			String savePath = params[1].toString();
			String saveFileName = params[2].toString();
			File file = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(downUrl);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				long length = entity.getContentLength();
				// 声明路径 如没有就创建
				File tmpFile = new File(savePath);
				if (!tmpFile.exists()) {
					tmpFile.mkdirs();
				}
				file = new File(saveFileName);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int currCount = 0;
				int percent = 0;
				double count = 0;
				while (count <= 100) {
					if (is != null) {
						int numRead = is.read(buf);
						if (numRead <= 0) {
							break;
						} else {
							fos.write(buf, 0, numRead);
							currCount += numRead;
							percent = (int) (currCount / length) * 100;
							publishProgress(percent);
						}
					} else {
						break;
					}
				}
				fos.flush();
				fos.close();
				is.close();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			return file;
		}

		@Override
		protected void onPostExecute(File result) {
			super.onPostExecute(result);
			if (result != null) {
				Toast.makeText(UpdateAPK.this.context, "新版本已下載成功，請安裝",
						Toast.LENGTH_LONG).show();
				pBar.setMessage("新版本已下載成功");
				pBar.dismiss();
				// 安装
				installVersion(result);
			} else {
				Toast.makeText(UpdateAPK.this.context, "新版本已下載失敗，請稍後再試",
						Toast.LENGTH_SHORT).show();
//				handler.sendEmptyMessage(LoadActivity.VERSIONUPDATE);

			}

		}

		/**
		 * 
		 * 
		 * @param file
		 */
		private void installVersion(File file) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}

}