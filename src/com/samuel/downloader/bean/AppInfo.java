package com.samuel.downloader.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.samuel.downloader.utils.SYSCS;



/**
 * 
 * {
        "appDesc": "字体管家，千万手机用户首选的换字体软件！一款精美小巧的手机字体美化软件，汇集海量精美字体。软件安全稳定，操作简单，换换字体，换换心情！【主要功能】 1.支持大部分手机一键更换字体(部分手机需root)，操作简单方便。 2.完美支持三星，小米手机，更换字体无需重启。 3.首次运行，自动备份默认字体，轻松恢复出厂字体，安全稳定。 4.上百款精选字体，不同风格任你挑选。 5. 为用户提供当下最热门应用。 6. 适配Go桌面，QQ阅读，iReader等多种应用。不同应用，不同字体。",
        "appIcon": "20140530080752-1565225499.jpg",
        "appId": "APP0000586",
        "appName": "字体管家",
        "appPermission": "访问网络,获取网络状态,获取WiFi状态,获取错略位置,改变配置,连续广播,获取精确位置",
        "appRank": 3,
        "appScreenshot": "",
        "appSpecial": "0",
        "appstatus": "",
        "category": null,
        "companyName": "字体管家科技 ",
        "createBy": "USER000023",
        "createDate": "2014-05-30T08:09:51",
        "fileEntity": "20140530080820-863590525.apk",
        "fileSize": 3998,
        "isRecomand": "0",
        "modifyDate": "2014-05-30T08:09:51",
        "packageName": "com.xinmei365.font",
        "recomandImage": "recomandImage",
        "sysMinversion": "2.2",
        "totalCount": 2,
        "versionCode": 64,
        "versionDesc": "1、修复字体替换不全或替换后没有效果的问题 2、解决字体安装失败的问题 3、修复自定义字体功能无法使用的问题 4、解决字体替换后手机无法开机的问题 ",
        "versionName": "3.6.5"
    }
 * 
 * @author Administrator
 *
 */
public class AppInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class TAG {

		public static final String TAG_UPDATES = "needupdateapps";
		public static final String TAG_CLASSNAME = "AppInfo";
		public static final String TAG_APPDESC = "appDesc";
		public static final String TAG_APPICON = "appIcon";
		public static final String TAG_APPID = "appId";
		public static final String TAG_APPNAME = "appName";
		public static final String TAG_APPPERMISSION = "appPermission";
		public static final String TAG_APPRANK = "appRank";
		public static final String TAG_APPSCREENSHOT = "appScreenshot";
		public static final String TAG_APPSPECIAL = "appSpecial";
		public static final String TAG_APPSTATUS = "appstatus";
		public static final String TAG_COMPANYNAME = "companyName";
		public static final String TAG_CREATEBY = "createBy";
		public static final String TAG_CREATEDATE = "createDate";
		public static final String TAG_FILEENTITY = "fileEntity";
		public static final String TAG_FILESIZE = "fileSize";
		public static final String TAG_ISRECOMAND = "isRecomand";
		public static final String TAG_MODIFYDATE = "modifyDate";
		public static final String TAG_PACKAGENAME = "packageName";
		public static final String TAG_RECOMANDIMAGE = "recomandImage";
		public static final String TAG_SYSMINVERSION = "sysMinversion";
		public static final String TAG_TOTALCOUNT = "totalCount";
		public static final String TAG_VERSIONNAME = "versionName";
		public static final String TAG_VERSIONDESC = "versionDesc";
		public static final String TAG_VERSIONCODE = "versionCode";

	}

	/**
	 * 
	 * "appDesc": "web",
	 */
	private String appDesc;

	/**
	 * "appIcon": "QQ.png",
	 * 
	 */
	private String appIcon;

	/**
	 * "appId": "APP0000030",
	 */
	private String appId;

	/**
	 * "appName": "QQ",
	 * 
	 */
	private String appName;

	/**
	 * "appPermission": "定位",
	 * 
	 */
	private String appPermission;

	/**
	 * 
	 * "appRank": 0, 下载量
	 * 
	 */
	private int appRank;

	/**
	 * "appScreenshot": "QQ1.jpg,QQ2.jpg,QQ3.jpg,QQ4.jpg",
	 * 
	 */
	private String appScreenShot;

	/**
	 * 
	 * "appSpecial": "0", 0:非特殊應用1:特殊應用
	 * 
	 */
	private String appSpecial;

	
	/**
	 * App的本地下载状态:  下载. 下载中.已安装.升级
	 */
	private int appLocalStatus;
	
	/**
	 * download status
	 */
	private Integer downloadStatus;

	public AppInfo(String appDesc, String appIcon, String appId,
			String appName, String appPermission, int appRank,
			String appScreenShot, String appSpecial, int appLocalStatus,
			Integer downloadStatus, String appstatus, String companyName,
			String createBy, String createDate, String fileEntity,
			String fileSize, String isRecommand, String modifyDate,
			String packageName, String recommandImage, String sysMinVersion, int totalCount,
			int versionCode, String versionDesc, String versionName) {
		super();
		this.appDesc = appDesc;
		this.appIcon = appIcon;
		this.appId = appId;
		this.appName = appName;
		this.appPermission = appPermission;
		this.appRank = appRank;
		this.appScreenShot = appScreenShot;
		this.appSpecial = appSpecial;
		this.setAppLocalStatus(appLocalStatus);
		this.downloadStatus = downloadStatus;
		this.appstatus = appstatus;
		this.companyName = companyName;
		this.createBy = createBy;
		this.createDate = createDate;
		this.fileEntity = fileEntity;
		this.fileSize = fileSize;
		this.isRecommand = isRecommand;
		this.modifyDate = modifyDate;
		this.packageName = packageName;
		this.recommandImage = recommandImage;
		this.sysMinVersion = sysMinVersion;
		this.totalCount = totalCount;
		this.versionCode = versionCode;
		this.versionDesc = versionDesc;
		this.versionName = versionName;
	}

	/**
	 * "appstatus": "1", 0:審核中 1:審核通過/已經上架2:審核未通過3:APP下架
	 */
	private String appstatus;

	/**
	 * "companyName": "騰訊", 事业群名称
	 * 
	 */
	private String companyName;

	/**
	 * "createBy": "ceshi002",
	 * 
	 */
	private String createBy;

	/**
	 * "createDate": "2014-02-21T00:00:00",
	 * 
	 */
	private String createDate;

	/**
	 * "fileEntity": "web.apk",
	 * http://mdmss.foxconn.com/selfservice/20140220155323web.apk
	 */
	private String fileEntity;

	/**
	 * "fileSize": 20826,
	 * 
	 */
	private String fileSize;

	/**
	 * 
	 * "isRecomand": "2", 0:不推薦1:推薦2:特別推薦
	 * 
	 */
	private String isRecommand;

	/**
	 * 
	 * "modifyDate": "2014-02-04T00:00:00",
	 */
	private String modifyDate;

	/**
	 * "packageName": "com.rere.tt",
	 * 
	 */
	private String packageName;

	/**
	 * "recomandImage": null,
	 * 
	 * 
	 */
	private String recommandImage;

	/**
	 * "sysMinversion": "Android2.3",
	 * 
	 */
	private String sysMinVersion;
	
	/**
	 * "totalCount": 2,
	 */
	private int totalCount;

	/**
	 * "versionCode": 3,
	 * 
	 */
	private int versionCode;

	/**
	 * "versionDesc": "该版本新增了隔空传物的功能,请谨慎使用",
	 * 
	 */
	private String versionDesc;

	/**
	 * 
	 * "versionName": "5.2.1"
	 */
	private String versionName;

	public AppInfo(int appRank, String appScreenShot, String createBy,
			String appSpecial, String isRecommand, String appPermission,
			String companyName, String fileEntity, String fileSize,
			String sysMinVersion, int versionCode, String versionDesc,
			String versionName, String packageName, String modifyDate,
			String createDate, String appstatus, String appIcon,
			String appDesc, String appId, String appName) {
		super();

		this.appRank = appRank;
		this.appScreenShot = appScreenShot;
		this.createBy = createBy;
		this.appSpecial = appSpecial;
		this.isRecommand = isRecommand;
		this.appPermission = appPermission;
		this.companyName = companyName;
		this.fileEntity = fileEntity;
		this.fileSize = fileSize;
		this.sysMinVersion = sysMinVersion;
		this.versionCode = versionCode;
		this.versionDesc = versionDesc;
		this.versionName = versionName;
		this.packageName = packageName;
		this.modifyDate = modifyDate;
		this.createDate = createDate;
		this.appstatus = appstatus;
		this.appIcon = appIcon;
		this.appDesc = appDesc;
		this.appId = appId;
		this.appName = appName;
	}


	public AppInfo(String recommandImage, int appRank, String appScreenShot,
			String createBy, String appSpecial, String isRecommand,
			String appPermission, String companyName, String fileEntity,
			String fileSize, String sysMinVersion, int versionCode,
			String versionDesc, String versionName, String packageName,
			String modifyDate, String createDate, String appstatus,
			String appIcon, String appDesc, String appId, String appName) {
		super();
		this.recommandImage = recommandImage;
		this.appRank = appRank;
		this.appScreenShot = appScreenShot;
		this.createBy = createBy;
		this.appSpecial = appSpecial;
		this.isRecommand = isRecommand;
		this.appPermission = appPermission;
		this.companyName = companyName;
		this.fileEntity = fileEntity;
		this.fileSize = fileSize;
		this.sysMinVersion = sysMinVersion;
		this.versionCode = versionCode;
		this.versionDesc = versionDesc;
		this.versionName = versionName;
		this.packageName = packageName;
		this.modifyDate = modifyDate;
		this.createDate = createDate;
		this.appstatus = appstatus;
		this.appIcon = appIcon;
		this.appDesc = appDesc;
		this.appId = appId;
		this.appName = appName;
	}

	public AppInfo() {
	}

	public String getAppDesc() {
		return appDesc;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppName() {
		return appName;
	}

	public String getAppPermission() {
		return appPermission;
	}

	public int getAppRank() {
		return appRank;
	}

	public String getAppScreenShot() {
		return appScreenShot;
	}

	public String getAppSpecial() {
		return appSpecial;
	}

	public String getAppstatus() {
		return appstatus;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCreateBy() {
		return createBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public String getFileEntity() {

		try {
			return java.net.URLEncoder.encode(fileEntity, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileEntity;
	}

	public String getFileSize() {
		return fileSize;
	}

	public String getIsRecommand() {
		return isRecommand;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getRecommandImage() {
		return recommandImage;
	}

	public String getSysMinVersion() {
		return sysMinVersion;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setAppPermission(String appPermission) {
		this.appPermission = appPermission;
	}

	public void setAppRank(int appRank) {
		this.appRank = appRank;
	}

	public void setAppScreenShot(String appScreenShot) {
		this.appScreenShot = appScreenShot;
	}

	public void setAppSpecial(String appSpecial) {
		this.appSpecial = appSpecial;
	}

	public void setAppstatus(String appstatus) {
		this.appstatus = appstatus;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setFileEntity(String fileEntity) {

		try {
			this.fileEntity = java.net.URLEncoder.encode(fileEntity, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public void setIsRecommand(String isRecommand) {
		this.isRecommand = isRecommand;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setRecommandImage(String recommandImage) {
		this.recommandImage = recommandImage;
	}

	public void setSysMinVersion(String sysMinVersion) {
		this.sysMinVersion = sysMinVersion;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	@Override
	public String toString() {
		return "AppInfo [appDesc=" + appDesc + ", appIcon=" + appIcon
				+ ", appId=" + appId + ", appName=" + appName
				+ ", appPermission=" + appPermission + ", appRank=" + appRank
				+ ", appScreenShot=" + appScreenShot + ", appSpecial="
				+ appSpecial + ", appLocalStatus=" + appLocalStatus
				+ ", downloadStatus=" + downloadStatus + ", appstatus="
				+ appstatus + ", companyName=" + companyName + ", createBy="
				+ createBy + ", createDate=" + createDate + ", fileEntity="
				+ fileEntity + ", fileSize=" + fileSize + ", isRecommand="
				+ isRecommand + ", modifyDate=" + modifyDate + ", packageName="
				+ packageName + ", recommandImage=" + recommandImage
				+ ", sysMinVersion=" + sysMinVersion + ", totalCount="
				+ totalCount + ", versionCode=" + versionCode
				+ ", versionDesc=" + versionDesc + ", versionName="
				+ versionName + "]";
	}

	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public Integer getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(Integer downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	public DownloadFileItem convetToDownloadFileItem() {
		DownloadFileItem item = new DownloadFileItem();
		item.setDownloadState(this.getDownloadStatus());
		item.setFileIcon(this.getAppIcon());
		item.setFileId(this.getAppId());
		item.setFileName(this.getAppName());
		item.setFilePath("Hello");// 已经写死
		item.setPackagename(this.getPackageName());
		
		String fileUrl = SYSCS.CONFCS.FILE_ENTITY_APP + this.getFileEntity();
//		System.out.println("fileUrl----->>"+ fileUrl);
		item.setFileUrl(fileUrl);
		item.setFileSize(this.getFileSize());
		return item;
	}


	public int getAppLocalStatus() {
		return appLocalStatus;
	}


	public void setAppLocalStatus(int appLocalStatus) {
		this.appLocalStatus = appLocalStatus;
	}

}
