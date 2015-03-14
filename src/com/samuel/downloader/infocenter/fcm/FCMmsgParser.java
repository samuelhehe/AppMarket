package com.samuel.downloader.infocenter.fcm;

import org.json.JSONException;
import org.json.JSONObject;

import com.samuel.downloader.infocenter.bean.JSONMsgInfo2;
import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.utils.TextFormater;

public class FCMmsgParser {
	
	public static ShopMsgInfo parseFCMMsg(String msgContent){
		if(TextFormater.isEmpty(msgContent)){
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(msgContent);
			ShopMsgInfo shopMsgInfo = new ShopMsgInfo();
			JSONObject jsonObject2 = jsonObject.getJSONObject(JSONMsgInfo2.TAG.user_user);
			shopMsgInfo.setShopName(jsonObject2.getString(JSONMsgInfo2.TAG.user_providerName));
			shopMsgInfo.setMsgTheme(jsonObject.getString(JSONMsgInfo2.TAG.msgTitle));
			shopMsgInfo.setReceiveDate(String.valueOf(System.currentTimeMillis()));
			shopMsgInfo.setMsgContent(jsonObject.getString(JSONMsgInfo2.TAG.msgContent));
			return shopMsgInfo; 
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
