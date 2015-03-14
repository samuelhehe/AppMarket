/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samuel.downloader.infocenter.fcm;

import org.fcm.client.Constants;
import org.fcm.client.LogUtil;

import com.google.gson.Gson;
import com.samuel.downloader.infocenter.bean.JSONMsgInfo;
import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.infocenter.dao.MsgInfoDBHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class NotificationReceiver extends BroadcastReceiver {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationReceiver.class);

	public NotificationReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
		String action = intent.getAction();
		Log.d(LOGTAG, "action=" + action);
		if (FCMContants.senderkey.equals(action)) {

			String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			String packetId = intent.getStringExtra(Constants.PACKET_ID);

			Notifier notifier = new Notifier(context);
			// notifier.notify(notificationMessage, packetId);

//			JSONMsgInfo jsonInfo = new Gson().fromJson(notificationMessage,
//					JSONMsgInfo.class);
			ShopMsgInfo shopMsgInfo  = FCMmsgParser.parseFCMMsg(notificationMessage);
			if(shopMsgInfo!=null){
				System.out.println("NotificationReceiver: msgInfo--" + shopMsgInfo);
				MsgInfoDBHelper db = new MsgInfoDBHelper(context);
				db.insertMsgInfo(shopMsgInfo);
				notifier.notify(shopMsgInfo.getShopName(),shopMsgInfo.getMsgTheme(), packetId);
			}
		}
	}

}
