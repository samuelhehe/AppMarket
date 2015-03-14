package com.samuel.downloader.download;

import java.io.File;
import java.io.Serializable;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

public class DownloadFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean isStop;
	private HttpHandler<File> mHttpHandler;

	public DownloadFile startDownloadFileByUrl(String url, String toPath,
			AjaxCallBack<File> downCallBack) {

		if (downCallBack == null) {
			throw new RuntimeException("AjaxCallBack对象不能为null");
		} else {
			FinalHttp finalHttp = new FinalHttp();
			// 支持断点续传
			setmHttpHandler(finalHttp.download(url, toPath, true, downCallBack));
			// mHttpHandler.
		}
		return this;
	}

	public DownloadFile startDownloadFileByUrlNoCatch(String url,
			String toPath, AjaxCallBack<File> downCallBack) {

		if (downCallBack == null) {
			throw new RuntimeException("AjaxCallBack对象不能为null");
		} else {
			FinalHttp down = new FinalHttp();
			// 支持断点续传
			setmHttpHandler(down.download(url, toPath, true, downCallBack));
			// mHttpHandler.
		}
		return this;
	}

	public void stopDownload() {
		if (getmHttpHandler() != null) {
			getmHttpHandler().stop();
			getmHttpHandler().cancel(true);
			if (!getmHttpHandler().isStop()) {
				getmHttpHandler().stop();
				getmHttpHandler().cancel(true);
			}
		}
	}

	public boolean isStop() {
		isStop = getmHttpHandler().isStop();
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public HttpHandler<File> getmHttpHandler() {
		return mHttpHandler;
	}

	public void setmHttpHandler(HttpHandler<File> mHttpHandler) {
		this.mHttpHandler = mHttpHandler;
	}
}
