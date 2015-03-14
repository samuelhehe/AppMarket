package com.samuel.downloader.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.CategoryInfo;
import com.samuel.downloader.bean.RecommandBean;
import com.samuel.downloader.bean.UserInfo;
import com.samuel.downloader.dao.PakageInfoService;
import com.samuel.downloader.utils.AppMarketUtils;
import com.samuel.downloader.utils.HttpClientUtil;
import com.samuel.downloader.utils.SYSCS;

public class HomeItemInfoHandler {

	/**
	 * 组合jsonobject from userId， imei
	 * 
	 * @param userID
	 * @param IMEI
	 * @return
	 */
	public static JSONObject getJSONObject(String userID, String IMEI) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("USER_ID", userID);
			obj.put("IMEI", IMEI);
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
		return obj;
	}

	/**
	 * 
	 * 
	 * @param currentpage
	 * @param pagesize
	 * @return
	 */
	public static List<AppInfo> getBoutiqueInfo(Context context,
			int currentpage, int pagesize) {

		String uri = SYSCS.CONFCS.BOUTIQUEAPPLIST;
		String reqparams = "?CurPage=" + currentpage + "&PageSize=" + pagesize;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);
			if (contentData != null) {

				return HomeItemInfoFilterHandler.getFilteredAppInfoList(
						context, parseAppInfo(contentData));

				// return parseAppInfo(contentData);
			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @param currentpage
	 * @param pagesize
	 * @return
	 */
	public static List<RecommandBean> getBoutiqueAndAd(int currentpage,
			int pagesize) {

		String uri = SYSCS.CONFCS.GETSUPERRECOMMANDAD;
		String reqparams = "?CurPage=" + currentpage + "&PageSize=" + pagesize;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);
			if (contentData != null) {
				return parseRecommandAndAd(contentData);
			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param currentpage
	 * @param pagesize
	 * @return
	 */
	public static List<AppInfo> getContentsInfo(Context context,
			int currentpage, int pagesize) {

		String uri = SYSCS.CONFCS.NEWESTAPPLIST;
		String reqparams = "?CurPage=" + currentpage + "&PageSize=" + pagesize;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);
			if (contentData != null) {
				// return parseAppInfo(contentData);
				return HomeItemInfoFilterHandler.getFilteredAppInfoList(
						context, parseAppInfo(contentData));

			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param currentpage
	 * @param pagesize
	 * @param categoryId
	 * @return
	 */
	public static List<AppInfo> getListByCategory(Context context,
			int currentpage, int pagesize, String categoryId) {
		String uri = SYSCS.CONFCS.APPLISTCATEGORY;
		String reqparams = "?CurPage=" + currentpage + "&PageSize=" + pagesize
				+ "&categoryId=" + categoryId;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);
			if (contentData != null) {
				// return parseAppInfo(contentData);
				return HomeItemInfoFilterHandler.getFilteredAppInfoList(
						context, parseAppInfo(contentData));

			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * @param currentpage
	 * @param pagesize
	 * @param categoryId
	 * @return 20140307
	 */

	public static List<AppInfo> getNeedUpdateAppList(Context context) {
		String uri = SYSCS.CONFCS.NEEDUPDATEAPPS;

		String installedAppInfoParams = PakageInfoService
				.getInstalledAppInputParamsJsonStr(PakageInfoService
						.getInstalledAppInputParams(context));
		/**
		 * ?param=[{"packageName":"com.nuomi","versionCode":10},{"packageName":
		 * "com.android.chrome","versionCode":10}]
		 * 
		 */

		installedAppInfoParams = AppMarketUtils
				.encodeingUrl(installedAppInfoParams);
		String reqparams = "?param=" + installedAppInfoParams;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);

			// System.out.println("contentData: ----------->>>>" + contentData);
			if (contentData != null) {
				// return parseAppInfo(contentData);
				return HomeItemInfoFilterHandler.getFilteredAppInfoList(
						context, parseAppInfo(contentData));

			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;

	}

	public static List<CategoryInfo> getCategoryInfo(int currentpage,
			int pagesize) {

		String uri = SYSCS.CONFCS.CATEGORYAPPLIST;
		String reqparams = "?CurPage=" + currentpage + "&PageSize=" + pagesize;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);
			if (contentData != null) {
				return parseCategory(contentData);
			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;

	}

	public static List<CategoryInfo> parseCategory(String jsonArray) {
		List<CategoryInfo> arrList = new ArrayList<CategoryInfo>();
		try {
			JSONArray arrays = new JSONArray(jsonArray);
			for (int i = 0; i < arrays.length(); i++) {
				JSONObject jsonObj = arrays.getJSONObject(i);
				CategoryInfo ci = new CategoryInfo();
				ci.setAppCategory(jsonObj
						.getString(CategoryInfo.TAG.TAG_APPCATEGORY));
				ci.setCategoryId(jsonObj
						.getString(CategoryInfo.TAG.TAG_CATEGORYID));
				ci.setCategoryIcon(jsonObj
						.getString(CategoryInfo.TAG.TAG_CATEGORYICON));
				ci.setCreateBy(jsonObj.getString(CategoryInfo.TAG.TAG_CREATEBY));
				arrList.add(ci);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrList;
	}

	public static UserInfo parseUserInfo(String jsonStr) {
		if (jsonStr != null && jsonStr != "" && jsonStr.length() > 4) {

			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				UserInfo userInfo = new UserInfo();
				userInfo.setUserName(jsonObj
						.getString(UserInfo.TAG.TAG_USERNAME));
				userInfo.setCreateDate(jsonObj
						.getString(UserInfo.TAG.TAG_CREATEDATE));
				userInfo.setModifyDate(jsonObj
						.getString(UserInfo.TAG.TAG_MODIFYDATE));
				userInfo.setEmail(jsonObj.getString(UserInfo.TAG.TAG_EMAIL));
				userInfo.setDepartment("GDSBG");
				userInfo.setTel(jsonObj.getString(UserInfo.TAG.TAG_TEL));
				userInfo.setUserId(jsonObj.getString(UserInfo.TAG.TAG_USERID));
				return userInfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static List<AppInfo> searchTask(Context context, int currentpage,
			int pagesize, String searchKey) {

		String uri = SYSCS.CONFCS.SEARCHAPPS;
		String reqparams = "?appName=" + searchKey + "&CurPage=" + currentpage
				+ "&PageSize=" + pagesize;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);
			if (contentData != null) {
				// return parseAppInfo(contentData);
				return HomeItemInfoFilterHandler.getFilteredAppInfoList(
						context, parseAppInfo(contentData));

			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		return null;

	}

	public static AppInfo getAppInfoByAppId(String appId) {

		String uri = SYSCS.CONFCS.DETAILSBYID;
		String reqparams = "?appId=" + appId;
		String contentData = null;
		try {
			contentData = HttpClientUtil.getRequest(uri, reqparams);
			if (contentData != null) {
				return parseAppInfoByOne(new JSONObject(contentData));
			} else {
				return null;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	// /**
	// * 解析json数组的第一种方式
	// *
	// * @param jsonArray
	// * @return
	// */
	// public static List<AppInfo> parseJson(List<AppInfo> arrList,
	// String jsonArray) {
	// try {
	// // 首先需要一个JsonReader对象，传入一个Reader参数
	// JsonReader reader = new JsonReader(new StringReader(jsonArray));
	// reader.beginArray();// 根据jsonArray可知道第一步要解析数组 即遇到了数组的"["
	// while (reader.hasNext()) {
	// arrList.add(readObject(reader));// 读取数组元素
	// }
	// reader.endArray();// 解析数组结束 遇到了数组的"]"
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return arrList;
	// }

	public static List<RecommandBean> parseRecommandAndAd(String jsonObj) {

		if (jsonObj != null && jsonObj != "" && jsonObj != "null"
				&& jsonObj.length() > 4) {
		} else {
			return null;
		}
		List<RecommandBean> recBeanLists = new ArrayList<RecommandBean>();
		recBeanLists.clear();
		try {
			JSONObject jsonObject = new JSONObject(jsonObj);

			JSONArray adArrays = jsonObject
					.getJSONArray(RecommandBean.TAG.adBeans);

			if (adArrays.length() >= 1) {

				for (int i = 0; i < adArrays.length(); i++) {

					if (null == adArrays.get(i) || adArrays.get(i) == "null"
							|| adArrays.get(i).equals("null")
							|| adArrays.get(i).equals(null)) {
						continue;
					}
					if (recBeanLists.size() < 5) {
						recBeanLists.add(parseRecBeanByOne(adArrays
								.getJSONObject(i)));

					} else {
						return recBeanLists;
					}
				}
			}

			// / add appInfoBeans to recBeanList ;
			JSONArray recArrays = jsonObject
					.getJSONArray(RecommandBean.TAG.appInfoBeans);
			if (recArrays.length() >= 1) {
				for (int i = 0; i < recArrays.length(); i++) {
					if (null == recArrays.get(i) || recArrays.get(i) == "null"
							|| recArrays.get(i).equals("null")
							|| recArrays.get(i).equals(null)) {
						continue;
					}
					AppInfo appInfo = parseAppInfoByOne(recArrays
							.getJSONObject(i));

					RecommandBean recBean = new RecommandBean();
					recBean.setIsad(false);
					recBean.setAdUrl("");
					recBean.setPicUrl(SYSCS.CONFCS.FILE_ENTITY_RECOMMAND
							+ appInfo.getRecommandImage());
					recBean.setAppId(appInfo.getAppId());

					recBean.appInfo = appInfo;
					if (recBeanLists.size() < 5) {
						recBeanLists.add(recBean);
					} else {
						return recBeanLists;
					}
				}
			}
			return recBeanLists;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return recBeanLists;

	}

	/**
	 * 解析json数组的第一种方式
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static List<AppInfo> parseAppInfo(String jsonArray) {
		if (null == jsonArray || jsonArray.length() <= 4 || jsonArray == "") {
			// System.out.println("jsonarry: " + jsonArray);
			return null;
		}
		List<AppInfo> arrList = new ArrayList<AppInfo>();
		try {
			JSONArray arrays = new JSONArray(jsonArray);

			for (int i = 0; i < arrays.length(); i++) {

				// System.out.println("arrays----->>>"+ arrays.get(i));
				if (null == arrays.get(i) || arrays.get(i) == "null"
						|| arrays.get(i).equals("null")
						|| arrays.get(i).equals(null)) {
					continue;
				}
				JSONObject jsonObj = arrays.getJSONObject(i);
				// if (null == jsonObj || jsonObj.length() <= 4) {
				// continue;
				// }
				AppInfo appInfo = parseAppInfoByOne(jsonObj);
				arrList.add(appInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return arrList;
	}

	public static AppInfo parseAppInfoByOne(JSONObject jsonObj)
			throws JSONException {

		AppInfo appInfo = new AppInfo();
		// id
		appInfo.setAppId(jsonObj.getString(AppInfo.TAG.TAG_APPID));
		// name
		appInfo.setAppName(jsonObj.getString(AppInfo.TAG.TAG_APPNAME));
		// companyname
		appInfo.setCompanyName(jsonObj.getString(AppInfo.TAG.TAG_COMPANYNAME));
		// fileurl
		appInfo.setFileEntity(jsonObj.getString(AppInfo.TAG.TAG_FILEENTITY));
		// appicon
		appInfo.setAppIcon(jsonObj.getString(AppInfo.TAG.TAG_APPICON));
		// apprank
		appInfo.setAppRank(jsonObj.getInt(AppInfo.TAG.TAG_APPRANK));
		// / appdesc
		appInfo.setAppDesc(jsonObj.getString(AppInfo.TAG.TAG_APPDESC));
		// / screenshot
		appInfo.setAppScreenShot(jsonObj
				.getString(AppInfo.TAG.TAG_APPSCREENSHOT));
		// / special
		appInfo.setAppSpecial(jsonObj.getString(AppInfo.TAG.TAG_APPSPECIAL));
		// file size
		appInfo.setFileSize(jsonObj.getString(AppInfo.TAG.TAG_FILESIZE));
		// createby
		appInfo.setCreateBy(jsonObj.getString(AppInfo.TAG.TAG_CREATEBY));
		// / createdate
		appInfo.setCreateDate(jsonObj.getString(AppInfo.TAG.TAG_CREATEDATE));

		// versionCode
		appInfo.setVersionCode(jsonObj.getInt(AppInfo.TAG.TAG_VERSIONCODE));
		// / versionname
		appInfo.setVersionName(jsonObj.getString(AppInfo.TAG.TAG_VERSIONNAME));
		// sysminversion
		appInfo.setSysMinVersion(jsonObj
				.getString(AppInfo.TAG.TAG_SYSMINVERSION));
		// /appPermission
		appInfo.setAppPermission(jsonObj
				.getString(AppInfo.TAG.TAG_APPPERMISSION));
		// // recommandimg
		appInfo.setRecommandImage(jsonObj
				.getString(AppInfo.TAG.TAG_RECOMANDIMAGE));
		// / packagename
		appInfo.setPackageName(jsonObj.getString(AppInfo.TAG.TAG_PACKAGENAME));

		// totalCount
		appInfo.setTotalCount(jsonObj.getInt(AppInfo.TAG.TAG_TOTALCOUNT));

		return appInfo;
	}

	public static RecommandBean parseRecBeanByOne(JSONObject jsonObj)
			throws JSONException {
		RecommandBean recBean = new RecommandBean();
		recBean.setIsad(true);
		recBean.setAdUrl(jsonObj.getString(RecommandBean.TAG.adUrl));
		recBean.setAppId("");
		recBean.setPicUrl(jsonObj.getString(RecommandBean.TAG.adPicture));
		recBean.appInfo = null;
		return recBean;

	}

	/**
	 * try { JSONObject jsonObj = new JSONObject(jsonStr);
	 * 
	 * // Getting JSON Array node contacts = jsonObj.getJSONArray(TAG_CONTACTS);
	 * 
	 * // looping through All Contacts for (int i = 0; i < contacts.length();
	 * i++) { JSONObject c = contacts.getJSONObject(i);
	 * 
	 * String id = c.getString(TAG_ID); String name = c.getString(TAG_NAME);
	 * String email = c.getString(TAG_EMAIL); String address =
	 * c.getString(TAG_ADDRESS); String gender = c.getString(TAG_GENDER);
	 * 
	 * // Phone node is JSON Object JSONObject phone =
	 * c.getJSONObject(TAG_PHONE); String mobile =
	 * phone.getString(TAG_PHONE_MOBILE); String home =
	 * phone.getString(TAG_PHONE_HOME); String office =
	 * phone.getString(TAG_PHONE_OFFICE);
	 * 
	 * // tmp hashmap for single contact HashMap<String, String> contact = new
	 * HashMap<String, String>();
	 * 
	 * // adding each child node to HashMap key => value contact.put(TAG_ID,
	 * id); contact.put(TAG_NAME, name); contact.put(TAG_EMAIL, email);
	 * contact.put(TAG_PHONE_MOBILE, mobile);
	 * 
	 * // adding contact to contact list contactList.add(contact); } } catch
	 * (JSONException e) { e.printStackTrace(); }
	 * 
	 */

	// /**
	// * 解析数组里面的json对象组成的元素
	// *
	// * [ { "appPermission": "定位", "appstatus": "1", "packageName":
	// * "com.rere.tt", "recomandImage": null, }]
	// *
	// * @param reader
	// * @return
	// * @throws Exception
	// */
	// private static AppInfo readObject(JsonReader reader) throws Exception {
	// AppInfo appInfo = new AppInfo();
	// reader.beginObject();// 开始解析对象，遇到了数组元素的'{'
	// while (reader.hasNext()) {// 开始解析对象里面的键值对
	// String key = reader.nextName();// 得到key
	// if (key.equals("appDesc")) {
	// appInfo.setAppDesc(reader.nextString());
	// } else if (key.equals("appIcon")) {
	// appInfo.setAppIcon(reader.nextString());
	// } else if (key.equals("fileSize")) {
	// DecimalFormat df = new DecimalFormat("0.00");
	// float d = (float) reader.nextInt() / (1024 * 1024);
	// String db = df.format(d) + "MB";
	// appInfo.setFileSize(db);
	// } else if (key.equals("appId")) {
	// appInfo.setAppId(reader.nextString());
	// } else if (key.equals("appName")) {
	// appInfo.setAppName(reader.nextString());
	// } else if (key.equals("companyName")) {
	// appInfo.setCompanyName(reader.nextString());
	// } else if (key.equals("versionName")) {
	// appInfo.setVersionName(reader.nextString());
	// } else if (key.equals("sysMinversion")) {
	// appInfo.setSysMinVersion(reader.nextString());
	// } else if (key.equals("appRank")) {
	// appInfo.setAppRank(reader.nextInt());
	// } else if (key.equals("appScreenshot")) {
	// appInfo.setAppScreenShot(reader.nextString());
	// } else if (key.equals("versionDesc")) {
	// appInfo.setVersionDesc(reader.nextString());
	// } else if (key.equals("modifyDate")) {
	// appInfo.setModifyDate(reader.nextString());
	// } else if (key.equals("fileEntity")) {
	// appInfo.setFileEntity(reader.nextString());
	// } else if (key.equals("createBy")) {
	// appInfo.setCreateBy(reader.nextString());
	// } else if (key.equals("appSpecial")) {
	// appInfo.setAppSpecial(reader.nextString());
	// } else if (key.equals("recomandImage")) {
	// reader.nextString();
	// continue;
	// } else {
	// reader.nextString();
	// continue;
	// }
	// }
	// reader.endObject();// 结束对象的解析 遇到了'}'
	// System.out.println("appInfo --->> " + appInfo);
	// return appInfo;
	// }
}
