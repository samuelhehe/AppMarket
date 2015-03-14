package com.samuel.downloader.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.samuel.downloader.adapter.FocusAdapter;
import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.bean.RecommandBean;
import com.samuel.downloader.bean.UserInfo;
import com.samuel.downloader.receiver.NetWorkStatusReceiver;
import com.samuel.downloader.receiver.NetWorkStatusReceiver.NetworkStatusCallback;
import com.samuel.downloader.service.HomeItemInfoHandler;
import com.samuel.downloader.tools.Tools;
import com.samuel.downloader.utils.AppMarketUtils;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.ContentValue;
import com.samuel.downloader.utils.Exit;
import com.samuel.downloader.utils.HttpClientUtil;
import com.samuel.downloader.utils.ParseXmlUtils;
import com.samuel.downloader.utils.PreferencesUtils;
import com.samuel.downloader.utils.ReceiverValue;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.ToastUtils;
import com.samuel.downloader.utils.UpdateInfo;

public class AtyHome2 extends FragmentActivity implements OnClickListener,
		NetworkStatusCallback {

	public static final int INSTALLNEWAPP = 1;
	public static final int UPDATEAPP = 2;
	public static final int LOAD_AD_FINISH = 3;
	public static final int LOAD_AD_FAIL = -1;
	protected static final int AUTO_SCROLL_ADBAR = 0;

	protected static int refreshCount = 0;
	protected static int downloadState = 0;
	protected static String packageName = null;

	protected Exit exit = new Exit();
	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;

	private ViewPager mViewPage;

	protected TabsAdapter mTabsAdapter;

	private final Class[] fragments = { FrgBoutiques.class, FrgNewest.class,
			FrgCategory.class, FrgAppMgr.class };
	private View sys_no_network = null;

	private int current_Pos = 0;// 当前图片位置
	private int count_Pic = 0; // 总共的图片大小
	private boolean isalive = true;
	public BroadcastReceiver nsBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_tabs_layout2);
		sys_no_network = this.findViewById(R.id.sys_no_network);
		sys_no_network.setOnClickListener(this);

		setNetworkStatusBar();

		initView();

		nsBroadcastReceiver = new NetWorkStatusReceiver(this);
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(nsBroadcastReceiver, myIntentFilter);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(R.string.app_name);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.main_content_bg_top));

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	Runnable scrollBarRunner = new Runnable() {

		@Override
		public void run() {

			while (isalive) {
				current_Pos = current_Pos % count_Pic; // 图片区间[0,count_Pic)
				// msg.arg1 = cur_index
				Message msg = mhandler.obtainMessage(AUTO_SCROLL_ADBAR,
						current_Pos, 0);
				mhandler.sendMessage(msg);
				// 更新时间间隔为 2s
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				current_Pos++; // 放置在Thread.sleep(2000)
								// ；防止mhandler处理消息的同步性，导致current_Pos>=count_Pic
			}

		}
	};
	private Gallery gallery;
	private ImageView focusPointImage;
	private FocusAdapter adapter;

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		System.out.println("AtyHome ------------>>>>"
				+ fragment.getClass().getSimpleName() + "attached ");

	}

	@Override
	public void onRefreshNetworkStatus() {
		setNetworkStatusBar();

	}

	private void setNetworkStatusBar() {
		if (sys_no_network != null) {
			if (Tools.isNetworkAvailable(getApplicationContext())) {
				sys_no_network.setVisibility(View.GONE);
			} else {
				sys_no_network.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initView() {

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager());
		mViewPage = (ViewPager) findViewById(R.id.pager);

		mViewPage.setOffscreenPageLimit(4);
		LinearLayout hotapplay = (LinearLayout) this.findViewById(R.id.hot_app);

		LinearLayout rank_app = (LinearLayout) this.findViewById(R.id.rank_app);
		LinearLayout rank_game = (LinearLayout) this
				.findViewById(R.id.rank_game);
		LinearLayout app_topics = (LinearLayout) this
				.findViewById(R.id.app_topics);

		LinearLayout linearLayouts[] = { hotapplay, rank_app, rank_game,
				app_topics };

		for (int i = 0; i < linearLayouts.length; i++) {
			linearLayouts[i].setOnClickListener(this);
		}
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPage, linearLayouts);
		// 得到fragment的个数
		int count = fragments.length;
		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(i + "");
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragments[i], null);
			mTabsAdapter.addTab(mTabHost.newTabSpec(i + "")
					.setIndicator(i + ""), fragments[i], null);
		}
		initAdBanner();
	}

	View adbarView;

	private void initAdBanner() {

		adbarView = this.findViewById(R.id.home_recommand_adbar);
		adbarView.setVisibility(View.GONE);

		gallery = (Gallery) adbarView.findViewById(R.id.focusGallery);
		focusPointImage = (ImageView) adbarView
				.findViewById(R.id.focusPointImage);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				RecommandBean map = adapter.getItem(position);
				current_Pos = position;
				switch (position) {
				case 0:
					focusPointImage
							.setBackgroundResource(R.drawable.focus_point_1);
					break;
				case 1:
					focusPointImage
							.setBackgroundResource(R.drawable.focus_point_2);
					break;
				case 2:
					focusPointImage
							.setBackgroundResource(R.drawable.focus_point_3);
					break;
				case 3:
					focusPointImage
							.setBackgroundResource(R.drawable.focus_point_4);
					break;
				case 4:
					focusPointImage
							.setBackgroundResource(R.drawable.focus_point_5);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (data != null) {

					RecommandBean recBean = data.get(position);
					System.out.println(recBean);
					if (recBean.isIsad()) {

						startActivity(new Intent(AtyHome2.this, AtyAd.class)
								.putExtra(RecommandBean.TAG.RecommandBean,
										recBean));
					} else {
						startActivity(new Intent(AtyHome2.this,
								AtyAppDetails.class).putExtra(
								AppInfo.TAG.TAG_CLASSNAME, recBean.appInfo));
					}
					// ToastUtils.show(HomeTabPagerTest.this, "" + recBean);

				}
			}
		});

		LoadAdBar loadAdBar = new LoadAdBar();
		loadAdBar.execute("");

	}

	List<RecommandBean> data;
	// 通过handler来更新主界面
	// gallery.setSelection(positon),选中第position的图片，然后调用OnItemSelectedListener监听改变图像
	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_AD_FINISH:

				if ((data = (List<RecommandBean>) msg.obj) != null) {

					adapter = new FocusAdapter(AtyHome2.this, data);

					gallery.setAdapter(adapter);

					count_Pic = adapter.getCount();
					// / 启动自动滚动
					new Thread(scrollBarRunner).start();
					adbarView.setVisibility(View.VISIBLE);
				}
				break;
			case AUTO_SCROLL_ADBAR:
				gallery.setSelection(msg.arg1);
				// 直接更改图片 ，不触发Gallery.OnItemSelectedListener监听
				// imgSwitcher.setBackgroundResource(imgAdapter.getResId(msg.arg1));
				break;
			case LOAD_AD_FAIL:
				adbarView.setVisibility(View.GONE);
				break;
			}

		}
	};

	protected class LoadAdBar extends
			AsyncTask<String, Void, List<RecommandBean>> {

		@Override
		protected List<RecommandBean> doInBackground(String... params) {

			return HomeItemInfoHandler.getBoutiqueAndAd(0, 12);
		}

		@Override
		protected void onPostExecute(List<RecommandBean> result) {
			super.onPostExecute(result);

			if (result != null && result.size() > 0) {

				Message msg = mhandler.obtainMessage();

				msg.what = LOAD_AD_FINISH;
				msg.obj = result;
				mhandler.sendMessage(msg);
			} else {

				Message msg = mhandler.obtainMessage(LOAD_AD_FAIL);
				mhandler.sendMessage(msg);
			}
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.hot_app:
			mTabHost.setCurrentTab(0);
			v.setPressed(true);
			if (refreshCount != 0) {
				sendBroadcastToFragment(ReceiverValue.FrgBoutiques.ACTION);
			}
			break;
		case R.id.rank_app:
			mTabHost.setCurrentTab(1);
			v.setPressed(true);
			if (refreshCount != 0) {
				sendBroadcastToFragment(ReceiverValue.FrgNewest.ACTION);
			}
			break;
		case R.id.rank_game:
			mTabHost.setCurrentTab(2);
			v.setPressed(true);
			break;
		case R.id.app_topics:
			v.setPressed(true);
			mTabHost.setCurrentTab(3);
			break;
		case R.id.sys_no_network:
			// 设置完毕会返回当前应用
			AtyHome2.this.startActivityForResult(
			// new Intent(
			// android.provider.Settings.ACTION_WIRELESS_SETTINGS)
					new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				setNetworkStatusBar();
			}
		}
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		private LinearLayout[] linearlayouts;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager, LinearLayout[] linearLayouts) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;

			this.linearlayouts = linearLayouts;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
			linearlayouts[position].setPressed(true);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			widget.setStripEnabled(true);
			linearlayouts[position].setPressed(true);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume---AtyHome");
		setNetworkStatusBar();
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ContentValue.DOWNLOAD_TYPE);
		registerReceiver(mBtChaBroadcastReceiver, myIntentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isalive = false;
		// File cacheDir = new File(SYSCS.LCS.IMG_ICONCACHEENTITYURL);
		// if (cacheDir.exists()) {
		// /**
		// * del cache
		// */
		// for (File file : cacheDir.listFiles()) {
		// file.delete();
		// }
		// cacheDir.delete();
		// }

		if (nsBroadcastReceiver != null) {
			unregisterReceiver(nsBroadcastReceiver);
		}
		if (mBtChaBroadcastReceiver != null) {
			unregisterReceiver(mBtChaBroadcastReceiver);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// getSupportFragmentManager()
		// .putFragment(outState, MessagesFragment.class.getName(),
		// mMessagesFragment);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.main, menu);
		return true;
	}

	protected AlertDialog updateDialog;
	protected TextView sys_updating_progress_status_tv;
	protected ProgressBar pb;

	protected CheckUpdateVersion checkUpdateVersion;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// case R.id.refresh:
		//
		// startActivity(new Intent(this, AboutActivity.class));
		// return true;
		case R.id.user_center:

			boolean isReged = PreferencesUtils.getBoolean(
					getApplicationContext(), SYSCS.PS.DEF_PNAME, false);

			Intent intentLanuch = null;
			if (isReged) {
				intentLanuch = new Intent(this, AtyUserCenter.class);
				String jsonArray = PreferencesUtils
						.getString(getApplicationContext(),
								UserInfo.TAG.TAG_CLASSNAME, "");
				if (jsonArray == "" || jsonArray.isEmpty()) {
					intentLanuch = new Intent(this, AtyUserLogin.class);
				}
				UserInfo userInfo = HomeItemInfoHandler
						.parseUserInfo(jsonArray);
				intentLanuch.putExtra(UserInfo.TAG.TAG_CLASSNAME, userInfo);
			} else {
				intentLanuch = new Intent(this, AtyUserLogin.class);
			}
			if (intentLanuch != null) {
				startActivity(intentLanuch);
			}
			return true;
		case R.id.check_update:

			// startActivity(new Intent(this, AboutActivity.class));
			Builder builder = new AlertDialog.Builder(AtyHome2.this);
			View view = getLayoutInflater().inflate(
					R.layout.sys_common_updating_dialog, null);
			builder.setView(view);
			builder.setCancelable(true);
			sys_updating_progress_status_tv = (TextView) view
					.findViewById(R.id.sys_updating_progress_status_tv);
			button1 = (TextView) view.findViewById(R.id.button1);
			button1.setTag(AtyHome2.UPDATEAPP);
			button1.setOnClickListener(new UpdateClickListener());
			TextView button2 = (TextView) view.findViewById(R.id.button2);
			button2.setOnClickListener(new UpdateClickListener());
			pb = (ProgressBar) view.findViewById(R.id.sys_updating_pb);

			updateDialog = builder.create();
			updateDialog.show();
			checkUpdateVersion = new CheckUpdateVersion();
			checkUpdateVersion.execute();

			return true;
		case R.id.about:
			startActivity(new Intent(this, AtyAbout.class));
			return true;
		case R.id.sys_app_search:
			startActivity(new Intent(this, AtySearchApp.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	TextView button1 = null;

	UpdateInfo updateInfo = null;

	File installNewApkPath = null;

	class UpdateClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.button1:
				// ok
				switch ((Integer) button1.getTag()) {
				case AtyHome2.INSTALLNEWAPP:
					if (installNewApkPath != null) {
						AppMarketUtils.install(getApplicationContext(),
								installNewApkPath.getAbsolutePath());
						updateDialog.dismiss();
					}
					break;
				case AtyHome2.UPDATEAPP:

					if (updateInfo != null) {
						sys_updating_progress_status_tv.setText("正在连接文件服务器...");
						pb.setVisibility(View.GONE);

						UpdateAsynTask updateAsyncTask = new UpdateAsynTask();

						updateAsyncTask.execute(updateInfo.getUrl(),SYSCS.LCS.FILE_ENTITY_DOWNLOAD_DIR, "appmarket"
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
				checkUpdateVersion.cancel(true);

				break;
			}
		}
	}

	class CheckUpdateVersion extends AsyncTask<Void, Integer, UpdateInfo> {

		protected void onPreExecute() {
			super.onPreExecute();
			button1.setClickable(false);

		}

		@Override
		protected void onCancelled(UpdateInfo result) {
			super.onCancelled(result);
			sys_updating_progress_status_tv.setText("检查更新已取消!");
		}

		protected void onPostExecute(UpdateInfo result) {
			super.onPostExecute(result);
			try {
				pb.setVisibility(View.GONE);
				if (result != null) {
					updateInfo = result;

					PackageInfo packageInfo = ApplicationDetailInfo
							.getPackageInfo(getApplicationContext());
					int localversionCode = packageInfo.versionCode;
					String localversionName = packageInfo.versionName;
					if (result.getVersioncode() > localversionCode) {
						sys_updating_progress_status_tv.setText("最新应用版本为:"
								+ result.getVersionname() + " 当前版本为:"
								+ localversionName);
						button1.setTag(UPDATEAPP);
						button1.setClickable(true);

					} else if (result.getVersioncode() <= localversionCode) {
						sys_updating_progress_status_tv.setText("应用已是最新版本");
						button1.setClickable(false);
					}
				} else {
					sys_updating_progress_status_tv
							.setText("网络异常,版本检查失败,稍后请重试");
					sys_updating_progress_status_tv.setTextColor(getResources()
							.getColor(R.color.ems_red));
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected UpdateInfo doInBackground(Void... params) {
			try {
				InputStream updateInfo = HttpClientUtil
						.getRequestXML(SYSCS.CONFCS.TEST_SYS_REQUEST_CHECK_UPATE_URL);
				if (updateInfo != null) {
					return ParseXmlUtils.getUpdataInfo(updateInfo);
				} else {
					return null;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
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

	private void pressAgainExit() {
		if (exit.isExit()) {
			finish();
		} else {
			ToastUtils.show(getApplicationContext(), "再按一次退出富友", 1000);
			exit.doExitInOneSecond();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			pressAgainExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private BroadcastReceiver mBtChaBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshCount = 2;
			// String downloadFileItem = intent.getStringExtra("DOWNLOADFILE");
			// System.out.println("AtyHomeReceive=========" + downloadFileItem);
			downloadState = intent.getIntExtra(ContentValue.DOWNLOAD_TYPE, 0);
			if (downloadState == CompareableLocalAppInfo.TAG.flag_installed
					|| downloadState == CompareableLocalAppInfo.TAG.flag_remove) {
				packageName = intent.getStringExtra(ReceiverValue.PACKAGENAME);
			}
			int tabNum = mTabHost.getCurrentTab();
			if (tabNum == 0) {
				sendBroadcastToFragment(ReceiverValue.FrgBoutiques.ACTION);
			} else if (tabNum == 1) {
				sendBroadcastToFragment(ReceiverValue.FrgNewest.ACTION);
			}
			System.out.println("DOWNLOAD_TYPE==========" + downloadState);
		}
	};

	private void sendBroadcastToFragment(String action) {
		Intent i = new Intent();
		i.putExtra(ContentValue.DOWNLOAD_TYPE, downloadState);
		i.putExtra(ReceiverValue.PACKAGENAME, packageName);
		i.setAction(action);
		sendBroadcast(i);
		refreshCount--;
	}

}
