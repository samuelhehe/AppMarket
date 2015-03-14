package com.samuel.downloader.app;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;

import com.fcm.libs.DeviceControl;
import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.infocenter.dao.MsgInfoDBHelper;
import com.samuel.downloader.infocenter.fcm.FCMContants;
import com.samuel.downloader.receiver.DeviceAdminSampleReceiver;
import com.samuel.downloader.tools.CrashApplication;
import com.samuel.downloader.tools.Tools;
import com.samuel.downloader.utils.ApplicationDetailInfo;
import com.samuel.downloader.utils.HttpClientUtil;
import com.samuel.downloader.utils.ParseXmlUtils;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.UpdateInfo;

public class AtyWelcome extends Activity {

	private Handler mhandler;
	public DevicePolicyManager mDPM;
	public ComponentName mDeviceAdmin;
	public static final int REQUEST_CODE_ENABLE_ADMIN = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdmin = new ComponentName(this, DeviceAdminSampleReceiver.class);
		setContentView(R.layout.splash);
		// saveInfo();
		// ServiceManager serviceManager = new ServiceManager(AtyWelcome.this);
		// serviceManager.setNotificationIcon(R.drawable.icon);
		// serviceManager.startService();
		
		

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
		executor.execute(updateDeviceInforunnable);
		executor.execute(checkUpdateRunnable);
		executor.execute(registerFcm);
		mhandler = new Handler();
		// if (isActiveAdmin()) {
		// if (Tools.checkNetwork(this)) {
		mhandler.postDelayed(gotoMainAct, 3000);
		// }
		// }
		// initDisplay();
		createShotCut();
	}
	public void createShotCut() {

		if (!hasInstallShortcut()) {
			addShortcut();
		}
	}

	public void createShortCut(Activity app, String componetName,
			String appName, int icon) {

		SharedPreferences sp = app.getSharedPreferences("CreateShortcut", 0);
		// 这种创建方法可以在程序卸载的时候，快捷方式自动 删除！
		ComponentName comp = new ComponentName(app.getApplicationContext(),
				componetName);
		Intent shortcutIntent = new Intent(
				new Intent(Intent.ACTION_MAIN).setComponent(comp));

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				app, R.drawable.ic_launcher);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		// 不创建重复快捷方式
		intent.putExtra("duplicate", false);
		// 添加快捷方式
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		app.sendBroadcast(intent);
		sp.edit().putString("create", "yes").commit();

	}
	
	/**
	 * 判断是否已安装快捷方式
	 */
	private boolean hasInstallShortcut() {
		boolean hasInstall = false;
		// Android2.2以前的Launcher查询
		// final String AUTHORITY1 = "com.android.launcher.settings";
		// Android2.2以后的Launcher查询
		final String AUTHORITY2 = "com.android.launcher2.settings";
		// Uri CONTENT_URI1 = Uri.parse("content://" + AUTHORITY1
		// + "/favorites?notify=true");
		Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY2
				+ "/favorites?notify=true");
		Cursor cursor = getContentResolver().query(CONTENT_URI2,
				new String[] { "_id", "title", "iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);
		// 如果Android2.2以前的不能查到，就采用Android2.2以后的Launcher查询
		if (cursor == null) {
			cursor = getContentResolver().query(CONTENT_URI2,
					new String[] { "_id", "title", "iconResource" }, "title=?",
					new String[] { getString(R.string.app_name) }, null);
		}
		// 如果查到有快捷方式，则不创建;如果没有，则引导用户创建快捷方式
		if (cursor != null && cursor.getCount() > 0) {
			hasInstall = true;
		}
		return hasInstall;
	}

	/**
	 * create shortcut in home screen
	 */
	private void addShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		shortcut.putExtra("duplicate", false);

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 这里必须为Intent设置一个action，可以任意(但安装和卸载时该参数必须一致)
		Intent respondIntent = new Intent(this, this.getClass());
		respondIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, respondIntent);
		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		sendBroadcast(shortcut);
	}

	
	private void initDisplay() {
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		SYSCS.ConValue.width = dm.widthPixels;
		SYSCS.ConValue.height = dm.heightPixels;
	}

	Runnable gotoMainAct = new Runnable() {
		@Override
		public void run() {

			startActivity(new Intent(AtyWelcome.this, AtyHome.class));
			finish();
		}
	};

	private ShopMsgInfo shopMsgInfo;
	private MsgInfoDBHelper db;
	private WifiManager wifiMgr;

	private void saveInfo() {
		shopMsgInfo = new ShopMsgInfo();
		db = new MsgInfoDBHelper(AtyWelcome.this);
		shopMsgInfo.setShopName("第一炉面包房");
		shopMsgInfo.setMsgTheme("第一家面包房正式开业");
		shopMsgInfo.setReceiveDate(String.valueOf(System.currentTimeMillis()));
		shopMsgInfo
				.setMsgContent("港区第一家面包房正式开业啦。开业期间(3/15-3/20)凭此信息到店消费即可领取精美礼品一份"
						+ "\n"
						+ "开业期间，积分双倍计算港区第一家面包房正式开业啦。开业期间(3/15-3/20)凭此信息到店消费即可领取精美礼品一份"
						+ "\n"
						+ "开业期间，积分双倍计算港区第一家面包房正式开业啦。开业期间(3/15-3/20)凭此信息到店消费即可领取精美礼品一份"
						+ "\n" + "最终解释权归《第一炉面包房》所有。");
		try {
			long l = db.insertMsgInfo(shopMsgInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Runnable updateDeviceInforunnable = new Runnable() {

		@Override
		public void run() {
			try {
				String loginResult = HttpClientUtil.getRequest(
						SYSCS.CONFCS.UPDATEDEVICEINFO, "?" + combinDeviceInfo()
								+ "&username=''");
				if (loginResult != null && loginResult != ""
						&& loginResult != "null") {
					if (loginResult.contains("1")) {
						System.out.println("updatedeviceInfo ------>>>success");
					} else {
						System.out.println("updatedeviceInfo ------>>>fail");
					}
				} else {
					System.out.println("updatedeviceInfo ------>>>fail");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		private String combinDeviceInfo() {

			/**
			 * http://mdmss.foxconn.com/appservice/user_userLogin.action?
			 * username=ceshi002&password=123 &
			 * appVersion=2.3.4 &deviceType=ios &osVersion=2.1.3 &udId=234543
			 */
			wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			String devicetype = android.os.Build.MODEL;
			// String androidSdk = android.os.Build.VERSION.SDK;
			String releaseVersion = android.os.Build.VERSION.RELEASE;
			String mac = wifiMgr.getConnectionInfo().getMacAddress();
			String combinDeviceInfo = null;
			try {
				String versionName = ApplicationDetailInfo
						.getPackageInfo(getApplicationContext()).versionName;
				combinDeviceInfo = "appVersion=" + versionName
						+ "&deviceType=Android" + devicetype.replace(" ", "")
						+ "&osVersion=" + releaseVersion 
						+ "&udId="+ mac.replace(":", "");
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return combinDeviceInfo;
		}
	};

	Runnable registerFcm = new Runnable() {
		/**
		 * FCM注册，返回值fcmId f1a13fa6c64d6257dc2f87c994eb18c0 ：senderKey 6da59b5c4ea57ea5eb69d1f60e8cfb4f
		 */
		protected void registerFcm() {
			DeviceControl deviceControl = new DeviceControl(
					FCMContants.senderkey, getApplicationContext());
			String fcm_id = deviceControl.deviceRegister();
			Log.d("AtyWelcome", "fcmId为：" + fcm_id);
		}

		@Override
		public void run() {
			try {
				registerFcm();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	Runnable checkUpdateRunnable = new Runnable() {

		@Override
		public void run() {

			try {
				InputStream updateInfoStr = HttpClientUtil
						.getRequestXML(SYSCS.CONFCS.TEST_SYS_REQUEST_CHECK_UPATE_URL);
				if (updateInfoStr != null) {
					UpdateInfo updateInfo = ParseXmlUtils
							.getUpdataInfo(updateInfoStr);

					System.out.println("updateinfo----------->>>" + updateInfo);
					PackageInfo packageInfo = ApplicationDetailInfo
							.getPackageInfo(getApplicationContext());
					int localversionCode = packageInfo.versionCode;
					// String localversionName = packageInfo.versionName;
					if (updateInfo.getVersioncode() > localversionCode) {
						CrashApplication.getInstance().showNotification(
								updateInfo, packageInfo);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_CODE_ENABLE_ADMIN:
			if (resultCode == RESULT_OK) {
				if (Tools.isNetworkAvailable(this)) {
					mhandler.postDelayed(gotoMainAct, 3000);
				}
			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
			break;
		}

	};

	/**
	 * Helper to determine if we are an active admin
	 */
	private boolean isActiveAdmin() {
		return mDPM.isAdminActive(mDeviceAdmin);
	}

	@Override
	protected void onResume() {
		// if (!isActiveAdmin()) {
		// Intent intent = new Intent(
		// DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		// intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
		// mDeviceAdmin);
		// intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
		// getString(R.string.add_admin_extra_app_text));
		// startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
		// }
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
