package com.samuel.downloader.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AppMarketUtils {

	/**
	 * replaceSpace in the url
	 * 
	 * @param url
	 * @return
	 */
	public static String replaceSpace(String url) {
		return url.replaceAll(" ", "%20");
	}

	public static String encodeingUrl(String url) {
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return replaceSpace(url);

	}

	/**
	 *  get file name form url 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromURL(String url) {
		int length = url.length();
		int lastIndex = 0;
		if (url.contains("/")) {
			lastIndex = url.lastIndexOf("/");
		} else if (url.contains("\"")) {
			lastIndex = url.lastIndexOf("\"");
		}
		return url.substring(lastIndex, length - lastIndex);
	}
	


	static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");

	public static final int MB_2_BYTE = 1024 * 1024;
	public static final int KB_2_BYTE = 1024;

	/**
	 * @param size
	 * @return
	 */
	public static CharSequence getAppSize(long size) {
	
		size*=1024;
		//just to suitable for  kb from db 
		if (size <= 0) {
			return "0M";
		}

		if (size >= MB_2_BYTE) {
			return new StringBuilder(16).append(
					DOUBLE_DECIMAL_FORMAT.format((double) size / MB_2_BYTE))
					.append("M");
		} else if (size >= KB_2_BYTE) {
			return new StringBuilder(16).append(
					DOUBLE_DECIMAL_FORMAT.format((double) size / KB_2_BYTE))
					.append("K");
		} else {
			return size + "B";
		}
	}

	/**
	 * getNotiPercent
	 * 
	 * @param progress
	 * @param max
	 * @return
	 */
	public static Integer getNotiPercent(long progress, long max) {
		int rate = 0;
		if (progress <= 0 || max <= 0) {
			rate = 0;
		} else if (progress > max) {
			rate = 100;
		} else {
			rate = (int) ((double) progress / max * 100);
		}
		return Integer.valueOf(new StringBuilder(16).append(rate).toString());
	}

	/**
	 * install app
	 * 
	 * @param context
	 * @param filePath
	 * @return whether apk exist
	 */
	public static boolean install(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
			i.setDataAndType(Uri.parse("file://" + filePath),
					"application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return true;
		}
		return false;
	}

}
