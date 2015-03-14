package com.samuel.downloader.infocenter.db;

import com.samuel.downloader.infocenter.bean.ShopMsgInfo;

public class DBInfo {

	public static class DB {

		public static final String DB_NAME = "fcm.db";
		public static final int VERSION = 1;
	}

	public static class Table {

		public static final String MSG_INFO_TB_NAME = ShopMsgInfo.TableConst.TABLE_NAME;

		public static final String MSG_INFO_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ MSG_INFO_TB_NAME
				+ " ( _id INTEGER PRIMARY KEY autoincrement, "
				+ ShopMsgInfo.TableConst.SHOP_NAME + " TEXT , "
				+ ShopMsgInfo.TableConst.MSG_THEME + " TEXT , "
				+ ShopMsgInfo.TableConst.RECEIVE_DATE + " TEXT , "
				+ ShopMsgInfo.TableConst.MSG_CONTENT + " TEXT )";
		
        public static final String MSG_INFO_DROP = "DROP TABLE "+ MSG_INFO_TB_NAME;

	}
}
