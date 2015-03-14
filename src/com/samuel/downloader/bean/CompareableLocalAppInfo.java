package com.samuel.downloader.bean;

public class CompareableLocalAppInfo {

	/**
	 * 
	 * 
	 AppId String 應用ID appIcon String 应用图标 AppName String 應用名稱 VersionName
	 * String App版本名稱 AppVersion Int App版本編號 SysVersion String Android最低版本要求
	 * VersionDesc String 版本描述 ModifyTime String 更新時間 FileEntity String AppUrl
	 * 
	 * packagename String App的包名 是 versioncode int App的版本编号 是
	 * 
	 * 
	 * 
	 */

	public static class TAG {

		/*
		 * 下载. 下载中.已安装.升级.
		 */
		/**
		 * download , start download
		 */
		public static final int flag_download = 0;

		/**
		 * downloading
		 */
		public static final int flag_downloading = 1;

		/**
		 * installed
		 */
		public static final int flag_installed = 2;
		/**
		 * update
		 */
		public static final int flag_update = 3;

		/**
		 * isDownload
		 */
		public static final int flag_isDownload = 4;
		
		/**
		 * remove
		 */
		public static final int flag_remove = 5;
		
	}

	private String appName;

	private int appLocalStatus;

	@Override
	public String toString() {
		return "CompareableLocalAppInfo [appName=" + appName
				+ ", appLocalStatus=" + getAppLocalStatus() + ", versionName="
				+ versionName + ", isUserApp=" + isUserApp + ", packageName="
				+ packageName + ", versionCode=" + versionCode + "]";
	}

	public CompareableLocalAppInfo(String appName, int appLocalStatus,
			String versionName, Boolean isUserApp, String packageName,
			int versionCode) {
		super();
		this.appName = appName;
		this.appLocalStatus = appLocalStatus;
		this.versionName = versionName;
		this.isUserApp = isUserApp;
		this.packageName = packageName;
		this.versionCode = versionCode;
	}

	private String versionName;
	private Boolean isUserApp;
	private String packageName;

	private int versionCode;

	public CompareableLocalAppInfo() {
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public Boolean getIsUserApp() {
		return isUserApp;
	}

	public void setIsUserApp(Boolean isUserApp) {
		this.isUserApp = isUserApp;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getAppLocalStatus() {
		return appLocalStatus;
	}

	public void setAppLocalStatus(int appLocalStatus) {
		this.appLocalStatus = appLocalStatus;
	}

}
