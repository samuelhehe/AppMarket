package com.samuel.downloader.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.dao.PakageInfoService;
import com.samuel.downloader.utils.SYSCS;
import com.samuel.downloader.utils.TextFormater;


/**
 * 
 * @description 用于过滤从Server端返回的数据,
 * 与本地App的信息进行对比,设置状态
 * @author Administrator
 * @date  2014-06-28
 * @since 1.0.1
 * @version 1.0.1
 */
public class HomeItemInfoFilterHandler {

//	public static List<Map<String, Object>> getConvertedCategoryList(List<CategoryInfo> categoryInfos){
//		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
//		for(CategoryInfo categoryInfo: categoryInfos){
//			HashMap<String , Object> keyValuePair = new HashMap<String, Object>();
//			keyValuePair.put(CategoryImageListAdapter.FLAG_ITEMSICON, getImageUrl(categoryInfo.getCategoryIcon()));
//			keyValuePair.put(CategoryImageListAdapter.FLAG_ITEMSTITLE, categoryInfo.getAppCategory());
//			maps.add(keyValuePair);
//		}
//		return maps;
//	}
//	
	private static String getImageUrl(String imageurl) {
		if(TextFormater.isEmpty(imageurl)){
			return "";
		}
		return SYSCS.CONFCS.FILE_ENTITY_CATEGORY  + imageurl;
	}
	
	public static List<AppInfo> getFilteredAppInfoList(Context context,
			List<AppInfo> appInfos) {
		if(appInfos==null){
			return null;
		}
		List<CompareableLocalAppInfo> compareableLocalAppInfos = PakageInfoService
				.getCompareableLocalAppInfos(context);
		System.out.println("CompareableLocalAppInfo" + compareableLocalAppInfos.toString());
		List<AppInfo>  newAppInfos = new ArrayList<AppInfo>();
		for (AppInfo appInfo : appInfos) {
			for (CompareableLocalAppInfo compareableLocalAppInfo : compareableLocalAppInfos) {
				if(TextFormater.isEmpty(appInfo.getPackageName())){
					continue;
				}else{
					if (appInfo.getPackageName().equalsIgnoreCase(compareableLocalAppInfo.getPackageName())) {
						if (compareableLocalAppInfo.getVersionCode() < appInfo.getVersionCode()) {
							appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_update);
						} else if (compareableLocalAppInfo.getVersionCode() >= appInfo.getVersionCode()) {
							appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_installed);
						}else{/// 不合理,应该添加db中下载队列的Item状态
							appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_download);
						}
					} else {
						continue;
					}
					
				}
			}
			newAppInfos.add(appInfo);
		}
		return newAppInfos;
	}

	/**
	 * @param appInfo
	 * @param compareableLocalAppInfo
	 * @return
	 */
	public static AppInfo getFilteredAppInfo(AppInfo appInfo,
			CompareableLocalAppInfo compareableLocalAppInfo) {
		if (appInfo.getPackageName().equalsIgnoreCase(
				compareableLocalAppInfo.getPackageName())) {
			if (compareableLocalAppInfo.getVersionCode() < appInfo
					.getVersionCode()) {
				appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_update);
			} else if (compareableLocalAppInfo.getVersionCode() == appInfo.getVersionCode()) {
				appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_installed);
			}
		} else {
//			appInfo.setAppLocalStatus(CompareableLocalAppInfo.TAG.flag_download);
			return  appInfo;
		}
		return appInfo;
	}

}
