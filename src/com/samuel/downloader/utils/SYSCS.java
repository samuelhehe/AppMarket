package com.samuel.downloader.utils;

import java.io.File;

import android.app.DownloadManager;
import android.os.Environment;

import com.samuel.downloader.app.MyApplcation;
import com.samuel.downloader.bean.DownloadFileItem;

//// 系統配置文件
////1. http://10.207.248.21/  是測試服務器host  http://app.foxconn.com/ 是正式服務器host
////2. 修改的地方包括 1> appservice  test_host的地址  2> 文件服務器 file_host地址
////3. abouthost URL 一直使用的是正式服務器地址,不用更改.

public class SYSCS extends MyApplcation {

	public static final class CONFCS {

		 public static final String TEST_HOST = "http://app.foxconn.com/";
		 //20140701

//		public static final String TEST_HOST = "http://10.207.248.21/";
//
//		public static final String FILE_HOST = "http://10.207.248.21/";
		 public static final String FILE_HOST = "http://app.foxconn.com/";
		 //20140701

		/**
		 * icon/
		 */
		public static final String FILE_IMAGE_APPICON = FILE_HOST + "icon/";

		/**
		 * 
		 * shot/
		 */
		public static final String FILE_IMAGE_SHOT = FILE_HOST + "shot/";

		/**
		 * app/
		 */
		public static final String FILE_ENTITY_APP = FILE_HOST + "app/";

		/**
		 * rec/
		 */
		public static final String FILE_ENTITY_RECOMMAND = FILE_HOST + "rec/";

		/**
		 * ad/
		 */
		public static final String FILE_ENTITY_AD = FILE_HOST + "ad/";

		/**
		 * icon/fenlei/
		 */
		public static final String FILE_ENTITY_CATEGORY = FILE_IMAGE_APPICON
				+ "fenlei/";

		/**
		 * http://localhost:9090/appservice/
		 */
		public static final String TEST_SYS_REQUEST_BASE_URL = SYSCS.CONFCS.TEST_HOST
				+ "appservice/";

		public static final String TEST_SYS_REQUEST_CHECK_UPATE_URL = TEST_SYS_REQUEST_BASE_URL
				+ "version.xml";

		/**
		 * app_getBoutiqueAppList.action CurPage=0&PageSize=10
		 */
		public static final String BOUTIQUEAPPLIST = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getBoutiqueAppList.action";

		/**
		 * app_getNewAppList.action?CurPage=0&PageSize=10
		 * 
		 */
		public static final String NEWESTAPPLIST = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getNewAppList.action";

		/**
		 * app_getAppCategoryList.action CurPage=0&PageSize=10
		 * 
		 */
		public static final String CATEGORYAPPLIST = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getAppCategoryList.action";

		/**
		 * CurPage=0&PageSize=10&categoryId=1
		 * 
		 */
		public static final String APPLISTCATEGORY = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getAppListByCategory.action";

		/**
		 * app_getAppDetailById.action appId=APP0000049
		 */
		public static final String DETAILSBYID = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getAppDetailById.action";

		/**
		 * user_userLogin.action username=ceshi001&password=123
		 */
		public static final String USERLOGIN = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "user_userLogin.action";

		/**
		 * 
		 * appservice/app_updateAppInfo.action?param=[{"packageName":"com.nuomi"
		 * ,"versionCode":10},{"packageName":"com.android.chrome","versionCode":
		 * 10}]
		 */
		public static final String NEEDUPDATEAPPS = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_updateAppInfo.action";

		/**
		 * appName=飞&CurPage=0&PageSize=10
		 * 
		 */
		public static final String SEARCHAPPS = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getSearchAppList.action";

		/**
		 * 
		 * username= dd3&password=123&nickname=1&email=0&telphone=2&movephone=789 "1", "0"
		 */
		public static final String USERREG = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "user_userRegister.action";

		/**
		 * username=dd3
		 * "true", "false"
		 */
		public static final String CHECKUSERNAME = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "user_checkUser.action";
		/**
		 * appVersion=2.3.5&deviceType=android&osVersion=2.1.3&udId=78910
		 * &username=zhangsan "1"
		 * 
		 */
		public static final String UPDATEDEVICEINFO = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "user_updateUserInfo.action";

		/**
		 * 
		 appId= APP0000182
		 * 
		 */
		public static final String ADDDOWNLOADCOUNTS = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getRank.action";

		/**
		 * 
		appId=APP0000388
		 */
		public static final String GETRANK = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getRank.action";

		/**
		 * 
		 * PageSize=12&CurPage=0
		 * 
		 */
		public static final String GETSUPERRECOMMANDAD = SYSCS.CONFCS.TEST_SYS_REQUEST_BASE_URL
				+ "app_getBoutiqueAndAds.action";

		public static final String NETWORKSTATUS = "ns";

		public static final String DOWNLOAD_FOLDER_NAME = "AppMarket";

		public static final String abouthost = "http://app.foxconn.com/";

		public static final String TEST_ABOUT_US_BASE_URL = abouthost
				+ "appmarket_user/";

		public static final String ABOUT_US_URL = TEST_ABOUT_US_BASE_URL
				+ "about.jsp";

	}

	public static class UICS {

		public static final String ISREGISTERED_KEY = "ISREGED";

		public static final String USERID_KEY = "USERID";

		public static final String USERNAME = "USERNAME";

		public static final String DEPARTMENT = "DEPARTMENT";

		public static final String EMAIL = "EMAIL";

		// public static final String EMAIL = "EMAIL";

		public static final String CREATEDATE = "CREATEDATE";

		public static final String MODIFYDATE = "MODIFYDATE";
	}

	/**
	 * LocaledConstants
	 */
	public static final class LCS {
		public static final String scale1 = "407*720";
		public static final String scale2 = "300*531";
		public static final String def_scale = "300*450";
		/**
		 * directory downloads dir
		 */
		public static final String FileEntity_host = Environment
				.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_DOWNLOADS).getPath();

		public static final String FileEntityDir = "AppMarket";

		/**
		 * cache dir cache icon dir cache img shotcache dir
		 */
		// public static final String FILE_ENTITY_CACHE = Environment
		// .getDownloadCacheDirectory().getPath()+ File.separator;

		public static final String FILE_ENTITY_CACHE = Environment
				.getExternalStorageDirectory().getPath()
				+ File.separator
				+ FileEntityDir + File.separator;

		/**
		 * download dir
		 * 
		 */
		public static final String FILE_ENTITY_DOWNLOAD_DIR = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + FileEntityDir + File.separator;

		public static final String IMG_CACHEENTITYURL = SYSCS.LCS.FILE_ENTITY_CACHE
				+ ".filecache/";

		public static final String IMG_ICONCACHEENTITYURL = SYSCS.LCS.IMG_CACHEENTITYURL
				+ ".iconcache/";

		public static final String IMG_SHOTCACHEENTITYURL = SYSCS.LCS.IMG_CACHEENTITYURL
				+ ".shotcache/";
	}

	/**
	 * 
	 * 緩存思路一. 可以考慮使用 ps 進行緩存操作以支持後續開發, 分三個部分進行緩存工作 緩存思路二.
	 * 可以考慮使用sqlite,進行緩存操作.建一張表將Server端數據同步下來
	 * ,僅僅同步必要的字段.更新可以對比client端與Server端的DB時間進行更新. 需要進一步更改後台系統架構
	 * 
	 * @author Administrator
	 * 
	 */
	public static final class PS {

		public static final String DEF_PNAME = "appmarket_sys";

		// public static final String BOUTIQUES_PNAME = "appmarket_boutiques";
		//
		// public static final String NEWSTLIST_PNAME = "appmarket_newstlist";
		//
		// public static final String CATEGORY_PNAME = "appmarket_category";
		//
		// public static final String PREVIEW_PNAME = "appmarket_preview";

	}

	public static final class DLS {

		public static String getPauseReason(int reasonId) {

			/**
			 * {@link DownloadManager#PAUSED_WAITING_TO_RETRY}<br/>
			 * {@link DownloadManager#PAUSED_WAITING_FOR_NETWORK}<br/>
			 * {@link DownloadManager#PAUSED_QUEUED_FOR_WIFI}<br/>
			 * {@link DownloadManager#PAUSED_UNKNOWN}</li>
			 * 
			 */

			switch (reasonId) {
			case DownloadManager.PAUSED_WAITING_TO_RETRY:
				// 正在等待重试
				return "正在等待重试";
			case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
				return "等待可用网络";
			case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
				return "等待中";
			case DownloadManager.PAUSED_UNKNOWN:
			default:
				// 未知原因
				return "未知原因";
			}
		}

		public static String getErrorReason(int reasonId) {

			switch (reasonId) {
			case DownloadManager.ERROR_CANNOT_RESUME:
				return "服务器端异常无法继续";
			case DownloadManager.ERROR_DEVICE_NOT_FOUND:
				return "SDCard没有挂载";
			case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
				return "文件已存在";
			case DownloadManager.ERROR_HTTP_DATA_ERROR:
				return "网络数据异常";
			case DownloadManager.ERROR_INSUFFICIENT_SPACE:
				return "SDCard没有存储空间";
			case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
				return "下载管理器无法处理错误";
			case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
			case DownloadManager.ERROR_FILE_ERROR:
			case DownloadManager.ERROR_UNKNOWN:
			default:
				return "未知错误";
			}
		}

	}

	public static final class FileClassify {

		public static final String SOFTWARE = "software";
		public static final String GAME = "game";
		public static final String AMUSEMENT = "amusement";

	}

	// public static boolean start_flag = true;

	public static DownloadFileItem startDownloadFileItem; // 需要下载的任务

	private static DownloadFileItem successDownloadFileItem; // 下载完成的任务

	public DownloadFileItem getStartDownloadFileItem() {
		return startDownloadFileItem;
	}

	public static void setStartDownloadFileItem(
			DownloadFileItem startDownloadFileItem) {
		SYSCS.startDownloadFileItem = startDownloadFileItem;
	}

	public static DownloadFileItem getSuccessDownloadFileItem() {
		return successDownloadFileItem;
	}

	public static void setSuccessDownloadFileItem(
			DownloadFileItem successDownloadFileItem) {
		SYSCS.successDownloadFileItem = successDownloadFileItem;
	}

	public static final class ConValue {

		public static int width;

		public static int height;

	}
}
