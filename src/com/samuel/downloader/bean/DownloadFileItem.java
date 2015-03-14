package com.samuel.downloader.bean;

import java.io.Serializable;

import com.samuel.downloader.download.DownloadFile;
import com.samuel.downloader.utils.SYSCS;


import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "downloadtask")
// 用于FinalDb指定的表名
public class DownloadFileItem implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 12L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private Long currentProgress = (long) 0;// 当前进度
	private DownloadFile downloadFile; // 下载控制器

	private Integer downloadState = 0; // 下载状态

	private String fileIcon; // 文件icon

	private String fileId; // 文件Id

	private String fileName; // 文件名

	private String filePath; // 文件存储路径

	private String fileSize;// 文件大小

	private String fileUrl;// 文件url

	private String id;// 文件Id

	
	/**
	 * App的本地下载状态:  下载. 下载中.已安装.升级
	 */
//	private int appLocalStatus;
	
	
	private String percentage = "..."; // 下载百分比的字符串

	
	
	private int position;// 标识了这个任务所在ListView中的位置 // 貌似还真不能删, 先留着

	private Long progressCount = (long) 0; // 文件总大小

	private Long uuid; // 下载任务的标识

	private String packagename ; 
	
	
	public Long getCurrentProgress() {
		return currentProgress;
	}

	public DownloadFile getDownloadFile() {
		return downloadFile;
	}

	public Integer getDownloadState() {
		return downloadState;
	}

	public String getFileIcon() {
		return fileIcon;
	}

	public String getFileId() {
		return fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		
//		System.out.println("filepath---->>>"+ SYSCS.LCS.FILE_ENTITY_DOWNLOAD_DIR + this.fileName);
		
		return SYSCS.LCS.FILE_ENTITY_DOWNLOAD_DIR + this.fileName+".apk";
	}

	public String getFileSize() {
		return fileSize;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public String getId() {
		return id;
	}

	public String getPercentage() {
		return percentage;
	}

	public int getPosition() {
		return position;
	}

	public Long getProgressCount() {
		return progressCount;
	}

	public Long getUuid() {
		return uuid;
	}

	public void setCurrentProgress(Long currentProgress) {
		this.currentProgress = currentProgress;
	}

	public void setDownloadFile(DownloadFile downloadFile) {
		this.downloadFile = downloadFile;
	}

	public void setDownloadState(Integer downloadState) {
		this.downloadState = downloadState;
	}

	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFilePath(String filePath) {

		// SYSCS.LCS.FILE_ENTITY_DOWNLOAD_DIR;

//		System.out.println("filepath---->>>"+ SYSCS.LCS.FILE_ENTITY_DOWNLOAD_DIR + this.fileName);

		this.filePath = SYSCS.LCS.FILE_ENTITY_DOWNLOAD_DIR + this.fileName+".apk";
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "DownloadFileItem [currentProgress=" + currentProgress
				+ ", downloadFile=" + downloadFile + ", downloadState="
				+ downloadState + ", fileIcon=" + fileIcon + ", fileId="
				+ fileId + ", fileName=" + fileName + ", filePath=" + filePath
				+ ", fileSize=" + fileSize + ", fileUrl=" + fileUrl + ", id="
				+ id + ", percentage=" + percentage + ", position=" + position
				+ ", progressCount=" + progressCount + ", uuid=" + uuid + "]";
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setProgressCount(Long progressCount) {
		this.progressCount = progressCount;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	
	public AppInfo converToAppInfo(){
		AppInfo appInfo = new AppInfo();
		appInfo.setAppIcon(this.fileIcon);
		appInfo.setDownloadStatus(this.downloadState);
		appInfo.setAppId(this.fileId);
		appInfo.setAppName(this.fileName);
		appInfo.setFileEntity(this.fileUrl);
		appInfo.setFileSize(this.fileSize);
		appInfo.setPackageName(packagename);
		return appInfo;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	

}
