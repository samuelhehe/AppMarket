package com.samuel.downloader.utils;

import java.io.Serializable;

// 实体类 存放 解析出来服务器信息的

public class UpdateInfo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int versioncode;; // 版本
	private String url; // 新版本存放 url 路径
	private String description; // 更新说明,如新增什么功能特性等
	private String versionname ;
	
	
	public static class  TAG{
		
		public static final String UPDATEINFO = "UPDATEINFO";
		
	}
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "UpdateInfo [versioncode=" + versioncode + ", url=" + url
				+ ", description=" + description + ", versionname="
				+ versionname + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVersioncode() {
		return versioncode;
	}

	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

}