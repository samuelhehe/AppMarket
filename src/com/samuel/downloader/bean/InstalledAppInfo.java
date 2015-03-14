package com.samuel.downloader.bean;

import android.graphics.drawable.Drawable;

public class InstalledAppInfo {

	
	
	
	/**
	 * 
	 * 
	  AppId	String	應用ID	
	  appIcon	String	应用图标	
AppName	String	應用名稱	
VersionName	String	App版本名稱	
AppVersion	Int	App版本編號	
SysVersion	String	Android最低版本要求	
VersionDesc	String	版本描述	
ModifyTime	String	更新時間	
FileEntity	String	AppUrl	



packagename	String	App的包名	是	
versioncode	int	App的版本编号	是	



	 * 
	 * 
	 */
	
	
	private String appName;
	private String versionName;
	private Drawable drawable;
	private Boolean isUserApp;
	private String packageName;
	
	private int versionCode ;
	

	public InstalledAppInfo(String appName, String versionName, Drawable drawable,
			Boolean isUserApp, String packageName, int versionCode) {
		super();
		this.appName = appName;
		this.versionName = versionName;
		this.drawable = drawable;
		this.isUserApp = isUserApp;
		this.packageName = packageName;
		this.versionCode = versionCode;
	}

	public InstalledAppInfo() {
		// TODO Auto-generated constructor stub
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

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
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

}
