package com.samuel.downloader.bean;


public class InstalledAppInputParams {

	/**
	 * packagename String App的包名 是 versioncode int App的版本编号 是
	 * 
	 * 
	 */

	private String packageName;

	private int versionCode;

	@Override
	public String toString() {
		return "InstalledAppInputParams [packageName=" + packageName
				+ ", versionCode=" + versionCode + "]";
	}

	public InstalledAppInputParams(String packageName, int versionCode) {
		super();
		this.packageName = packageName;
		this.versionCode = versionCode;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	

}
