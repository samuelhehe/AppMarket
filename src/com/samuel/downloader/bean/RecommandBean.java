package com.samuel.downloader.bean;

import java.io.Serializable;

public class RecommandBean  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class TAG {

		public static final String RecommandBean= "RecommandBean";
		
		public static final String adBeans = "adBeans";

		public static final String appInfoBeans = "appInfoBeans";

		public static final String adPicture = "adPicture";

		public static final String adUrl = "adUrl";

	}
	
	public AppInfo appInfo ; 

	private boolean isad;

	public RecommandBean(boolean isad, String adUrl, String appId, String picUrl) {
		super();
		this.isad = isad;
		this.adUrl = adUrl;
		this.appId = appId;
		this.picUrl = picUrl;
	}

	public RecommandBean() {
		// TODO Auto-generated constructor stub
	}

	private String adUrl;

	private String appId;

	private String picUrl;

	public String getAdUrl() {
		return adUrl;
	}

	public String getAppId() {
		return appId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Override
	public String toString() {
		return "RecommandBean [isad=" + isad + ", adUrl=" + adUrl + ", appId="
				+ appId + ", picUrl=" + picUrl + "]";
	}

	public boolean isIsad() {
		return isad;
	}

	public void setIsad(boolean isad) {
		this.isad = isad;
	}

}
