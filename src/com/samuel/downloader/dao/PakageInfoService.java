package com.samuel.downloader.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.samuel.downloader.app.R;
import com.samuel.downloader.bean.CompareableLocalAppInfo;
import com.samuel.downloader.bean.InstalledAppInfo;
import com.samuel.downloader.bean.InstalledAppInputParams;

/**
 * 
 * PakageInfo Service
 * 
 * @author samuel
 * @1.0.1
 * @date 2014/2/25
 */
public class PakageInfoService {

	public static List<InstalledAppInfo> getAppInfos(Context context) {
		List<InstalledAppInfo> appInfos;
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> pakageinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		appInfos = new ArrayList<InstalledAppInfo>();
		for (PackageInfo packageInfo : pakageinfos) {
			if (!filterApp(packageInfo.applicationInfo)) {
				continue;
			}
			
//			Signature[] signatures = packageInfo.signatures;
//			if(signatures!=null&&signatures.length>=1){
//				String string = signatures[0].toString();
//				System.out.println("appName---->>>"+  string);
//			}
//			
			InstalledAppInfo appInfo = new InstalledAppInfo();
			appInfo.setIsUserApp(filterApp(packageInfo.applicationInfo));
			context.getString(R.string.app_name);
			context.getResources().getString(R.string.app_name);
			appInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
			appInfo.setVersionCode(packageInfo.versionCode); 
			appInfo.setVersionName(packageInfo.versionName);
			appInfo.setDrawable(packageInfo.applicationInfo.loadIcon(pm));
			appInfo.setPackageName(packageInfo.packageName);
			appInfos.add(appInfo);
			appInfo = null;
		}
		return appInfos;
	}

	
	public static List<CompareableLocalAppInfo> getCompareableLocalAppInfos(Context context){
		
		List<CompareableLocalAppInfo> appInfos;
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> pakageinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		appInfos = new ArrayList<CompareableLocalAppInfo>();
		for (PackageInfo packageInfo : pakageinfos) {
			if (!filterApp(packageInfo.applicationInfo)) {
				continue;
			}
			CompareableLocalAppInfo appInfo = new CompareableLocalAppInfo();
			appInfo.setIsUserApp(filterApp(packageInfo.applicationInfo));
			context.getString(R.string.app_name);
			context.getResources().getString(R.string.app_name);
			appInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
			appInfo.setVersionCode(packageInfo.versionCode);
			appInfo.setVersionName(packageInfo.versionName);
			appInfo.setPackageName(packageInfo.packageName);
			appInfos.add(appInfo);
			appInfo = null;
		}
		return appInfos;
	}
	
	
	/**
	 * 
	 * 
	 * @return
	 */
	public static List<InstalledAppInputParams> getInstalledAppInputParams(
			Context context) {

		List<InstalledAppInfo> installedAppInfos = getAppInfos(context);

		List<InstalledAppInputParams> inputParams = new ArrayList<InstalledAppInputParams>();
		for (InstalledAppInfo appInfo : installedAppInfos) {

			InstalledAppInputParams inputParam = new InstalledAppInputParams(
					appInfo.getPackageName(), appInfo.getVersionCode());
			// System.out.println("inputparam "+ inputParam.toString());
			inputParams.add(inputParam);
		}
		return inputParams;
	}

	/**
	 * 
	 * 
	 * @param inputInstalledAppInputParams
	 * @return
	 */
	public static String getInstalledAppInputParamsJsonStr(
			List<InstalledAppInputParams> inputInstalledAppInputParams) {
		// JSONObject objs = new JSONObject();
		JSONArray jsonArrays = new JSONArray();
		try {
			for (InstalledAppInputParams installedAppInputParam : inputInstalledAppInputParams) {
				JSONObject obj = new JSONObject();
				obj.put("versionCode", installedAppInputParam.getVersionCode());
				obj.put("packageName", installedAppInputParam.getPackageName());
				jsonArrays.put(obj);
			}
			// objs.put("applist", jsonArrays);
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
		// return objs.toString();
		return jsonArrays.toString();
	}

	/**
	 * 三方应用程序的过滤器
	 * 
	 * @param info
	 * @return true 三方应用 false 系统应用
	 */
	public static boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			// 代表的是系统的应用,但是被用户升级了. 用户应用
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			// 代表的用户的应用
			return true;
		}
		return false;
	}

}