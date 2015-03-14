package com.samuel.downloader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作帮助类
 */
public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		// /第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
		super(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.VERSION);
	}

	// 用于初次使用软件时生成数据库表
	// 当调用SQLiteOpenHelper的getWritableDatabase()或者getReadableDatabase()方法获取用于操作数据库的SQLiteDatabase实例的时候，
	//
	public void onCreate(SQLiteDatabase db) {

//		db.beginTransaction();
		db.execSQL(DBInfo.Table.DOWNLOAD_INFO_CREATE);
//		db.execSQL(DBInfo.Table.INSTALLEDAPP_INFO_CREATE);
//		db.endTransaction();
//		db.setTransactionSuccessful();

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//		db.beginTransaction();
		db.execSQL(DBInfo.Table.DOWNLOAD_INFO_DROP);
//		db.execSQL(DBInfo.Table.INSTALLEDAPP_INFO_DROP);
//		db.endTransaction();
//		db.setTransactionSuccessful();
		onCreate(db);

	}

}
