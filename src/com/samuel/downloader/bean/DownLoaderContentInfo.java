package com.samuel.downloader.bean;

public class DownLoaderContentInfo {

	@Override
	public String toString() {
		return "DownLoaderContentInfo [appId=" + appId + ", appUrl=" + appUrl
				+ ", appName=" + appName + ", dlStatus=" + dlStatus + ", dqId="
				+ dqId + ", iconName=" + iconName + ", taskdate=" + taskdate
				+ "]";
	}

	public DownLoaderContentInfo(String appId, String appUrl, String appName,
			int dlStatus, long dqId, String iconName, long taskdate) {
		super();
		this.appId = appId;
		this.appUrl = appUrl;
		this.appName = appName;
		this.dlStatus = dlStatus;
		this.dqId = dqId;
		this.iconName = iconName;
		this.taskdate = taskdate;
	}

	public static class TableConst {

		/**
		 * appId;
		 */
		public static String APPID = "appId";

		/**
		 * appName
		 * 
		 */
		public static String APPNAME = "appname";

		/**
		 * dlstatus
		 * 
		 */
		public static String DLSTATUS = "dlstatus";

		/**
		 * download queue id
		 * 
		 */
		public static String DQID = "dqid";

		/**
		 * appName ref iconName
		 * 
		 */
		public static String ICONNAME = "iconname";

		public static String TABLE_NAME = "downloadinfo";

		/**
		 * task date add queue
		 * 
		 */
		public static String TASKDATE = "taskdate";

		/**
		 * 
		 * AppUrl
		 */
		public static String APPURL = "APPURL";

	}

	/**
	 * appId ;
	 * 
	 */
	private String appId;
	/**
	 * 
	 * AppUrl
	 */
	private String appUrl;

	/**
	 * appName
	 * 
	 */
	private String appName;

	/**
	 * appStatus
	 * 
	 */
	private int dlStatus;


	

	/**
	 * download queue id
	 * 
	 */
	private long dqId;

	/**
	 * appName ref iconName
	 * 
	 */
	private String iconName;

	/**
	 * task date add queue
	 * 
	 */
	private long taskdate;

	public DownLoaderContentInfo() {

	}

	public String getAppName() {
		return appName;
	}



	public long getDqId() {
		return dqId;
	}

	public String getIconName() {
		return iconName;
	}

	public long getTaskdate() {
		return taskdate;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setDqId(long dqId) {
		this.dqId = dqId;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public void setTaskdate(long taskdate) {
		this.taskdate = taskdate;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getDlStatus() {
		return dlStatus;
	}

	public void setDlStatus(int dlStatus) {
		this.dlStatus = dlStatus;
	}

	

}
