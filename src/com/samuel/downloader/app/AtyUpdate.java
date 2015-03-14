package com.samuel.downloader.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samuel.downloader.utils.AppMarketUtils;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.UpdateInfo;

public class AtyUpdate extends Activity {

	public AtyUpdate() {

	}

	UpdateInfo updateInfo;
	private TextView sys_updating_progress_status_tv;
	private TextView button1;
	private ProgressBar pb;
	private AlertDialog updateDialog;
	private PackageInfo packageinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.sys_common_updating_dialog);

		updateInfo = (UpdateInfo) getIntent().getSerializableExtra(
				UpdateInfo.TAG.UPDATEINFO);

		packageinfo = getIntent().getParcelableExtra("packageinfo");

		System.out.println("updateInfo -------------->>>" + updateInfo);
		createUpdateDialog();

	}

	protected void createUpdateDialog() {

		// startActivity(new Intent(this, AboutActivity.class));
		Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(
				R.layout.sys_common_updating_dialog, null);
		builder.setView(view);
		builder.setCancelable(true);
		sys_updating_progress_status_tv = (TextView) view
				.findViewById(R.id.sys_updating_progress_status_tv);
		button1 = (TextView) view.findViewById(R.id.button1);
		button1.setTag(AtyHome.UPDATEAPP);
		button1.setOnClickListener(new UpdateClickListener());
		TextView button2 = (TextView) view.findViewById(R.id.button2);
		button2.setOnClickListener(new UpdateClickListener());
		pb = (ProgressBar) view.findViewById(R.id.sys_updating_pb);
		
		sys_updating_progress_status_tv.setText("最新版本信息: "+ updateInfo.getVersionname()+",当前版本信息: "+packageinfo.versionName );
		pb.setVisibility(View.GONE);
		updateDialog = builder.create();
		updateDialog.show();

	}

	private final class UpdateAsynTask extends AsyncTask<String, Integer, File> {
		@Override
		protected void onPreExecute() {
			updateDialog.show();

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pb.setVisibility(View.VISIBLE);
			sys_updating_progress_status_tv.setText("更新程序包已下载:  " + values[0]
					+ "%");
			if (values[0] >= 100) {

				pb.setVisibility(View.GONE);
				sys_updating_progress_status_tv
						.setText("          安装包已下载完成,请点击按钮进行安装");
			}
		}

		@Override
		protected File doInBackground(String... params) {

			String downUrl = params[0].toString();
			String savePath = params[1].toString();
			String saveFileName = params[2].toString();

			System.out.println("downUrl ----->>>" + downUrl);
			System.out.println("savePath ----->>>" + savePath + saveFileName);
			System.out.println("saveFileName ----->>>" + saveFileName);

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
				file = new File(tmpFile, saveFileName);
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

			if (result != null && result.isFile() && result.exists()) {
				button1.setTag(INSTALLNEWAPP);
				button1.setClickable(true);
				button1.setText("安装");
				installNewApkPath = result;

				System.out.println("file path --->>>"
						+ installNewApkPath.getAbsolutePath());

			} else {
				sys_updating_progress_status_tv.setTextColor(getResources()
						.getColor(R.color.ems_red));
				sys_updating_progress_status_tv.setText("更新程序包下载失败,请稍候重试");
			}

		}

	}

	public static final int INSTALLNEWAPP = 1;
	public static final int UPDATEAPP = 2;
	public File installNewApkPath = null;

	class UpdateClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.button1:
				// ok
				switch ((Integer) button1.getTag()) {
				case AtyHome.INSTALLNEWAPP:
					if (installNewApkPath != null) {
						AppMarketUtils.install(getApplicationContext(),
								installNewApkPath.getAbsolutePath());
						updateDialog.dismiss();
						AtyUpdate.this.finish();
					}
					break;
				case AtyHome.UPDATEAPP:

					if (updateInfo != null) {
						sys_updating_progress_status_tv.setText("最新版本信息: "
								+ updateInfo.getVersionname());
						pb.setVisibility(View.GONE);

						UpdateAsynTask updateAsyncTask = new UpdateAsynTask();

						updateAsyncTask.execute(updateInfo.getUrl(),
								SYSCS.LCS.FILE_ENTITY_DOWNLOAD_DIR, "appmarket"
										+ updateInfo.getVersionname() + ".apk");
						button1.setClickable(false);
					} else {
						sys_updating_progress_status_tv
								.setText("服务器内部错误,请稍后重试!");
					}
					break;
				}
				break;
			case R.id.button2:
				// cancel
				updateDialog.dismiss();

				AtyUpdate.this.finish();

				break;
			}
		}
	}
}
