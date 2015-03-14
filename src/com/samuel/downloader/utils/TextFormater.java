package com.samuel.downloader.utils;

import java.text.DecimalFormat;

public class TextFormater {
	public static String getDataSize(long size) {
		DecimalFormat formater = new DecimalFormat("####.00");
		if (size < 1024) {
			return size + "bytes";
		} else if (size < 1024 * 1024) {
			float kbsize = size / 1024f;
			return formater.format(kbsize) + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			float mbsize = size / 1024f / 1024f;
			return formater.format(mbsize) + "MB";
		} else {
			return "size: error";
		}
	}

	public static String getKBDataSize(long size) {
		return getDataSize(size * 1024);
	}

	public static <T> T nonNull(T argument) {
		if (argument == null) {
			throw new IllegalArgumentException("argument cannot be null");
		}

		return argument;
	}

	public static boolean isEmpty(Object str) {
		
		if (str instanceof String) {
			str = (String) str;
			if (str != null && str != "null" && str != "") {
				return false;
			} else {
				return true;
			}
		}else if(str==null){
			return true;
		}
		return false;
	}
}
