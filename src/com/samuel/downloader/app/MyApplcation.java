package com.samuel.downloader.app;

import java.util.ArrayList;
import java.util.List;

import com.samuel.downloader.bean.DownloadFileItem;

import android.app.Application;
import android.support.v4.view.ViewPager;

public class MyApplcation extends Application {

	private DownloadFileItem startDownloadFileItem; // 需要下载的任务
	
	private DownloadFileItem downloadSuccess;
	private ViewPager pager;
	private List<DownloadFileItem> downloadItems = new ArrayList<DownloadFileItem>(); // 下载队列

	public List<DownloadFileItem> getDownloadItems() {
		return downloadItems;
	}

	public void setDownloadItems(List<DownloadFileItem> downloadItems) {
		this.downloadItems = downloadItems;
	}

	public DownloadFileItem getStartDownloadMovieItem() {
		return startDownloadFileItem;
	}

	public void setStartDownloadMovieItem(
			DownloadFileItem startDownloadMovieItem) {
		this.startDownloadFileItem = startDownloadMovieItem;
	}

	public DownloadFileItem getDownloadSuccess() {
		return downloadSuccess;
	}

	public void setDownloadSuccess(DownloadFileItem downloadSuccess) {
		this.downloadSuccess = downloadSuccess;
	}

	public ViewPager getPager() {
		return pager;
	}

	public void setPager(ViewPager pager) {
		this.pager = pager;
	}

}
