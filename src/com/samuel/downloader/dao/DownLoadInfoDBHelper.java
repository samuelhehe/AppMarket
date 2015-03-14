package com.samuel.downloader.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.samuel.downloader.bean.DownLoaderContentInfo;
import com.samuel.downloader.db.DBHelper;

/**
 * 用户数据服务类对下载任务信息进行增删改查操作
 * 
 */
public class DownLoadInfoDBHelper {

	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	private String[] columns = { DownLoaderContentInfo.TableConst.DQID,
			
			DownLoaderContentInfo.TableConst.APPID,
			DownLoaderContentInfo.TableConst.ICONNAME,
			DownLoaderContentInfo.TableConst.APPNAME,
			DownLoaderContentInfo.TableConst.APPURL,
			DownLoaderContentInfo.TableConst.DLSTATUS,
			DownLoaderContentInfo.TableConst.TASKDATE

	};

	public DownLoadInfoDBHelper(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 
	 * query exist task 
	 * 
	 * 
	 * @param taskUrl
	 * @return if true is task is exist , otherwise false 
	 */
	public boolean queryExistTask(String taskUrl) {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(DownLoaderContentInfo.TableConst.TABLE_NAME,
				columns, DownLoaderContentInfo.TableConst.APPURL + "=?",
				new String[] { taskUrl }, null, null, null);
		if (null != cursor) {
			if (cursor.getCount() > 0) {
				cursor.close();
				db.close();
				return true;
			} else {
				cursor.close();
				db.close();
				return false;
			}
		}
		return false;
	}

	/**
	 * add download info
	 * 
	 * @param user
	 * @return rowId;
	 */
	public long insertDownLoadInfo(DownLoaderContentInfo downLoaderContentInfo) {

		db = dbHelper.getWritableDatabase();

		values = new ContentValues(5);

		values.put(DownLoaderContentInfo.TableConst.APPID,
				String.valueOf(downLoaderContentInfo.getAppId()));
		values.put(DownLoaderContentInfo.TableConst.DQID,
				String.valueOf(downLoaderContentInfo.getDqId()));
		values.put(DownLoaderContentInfo.TableConst.APPNAME,
				downLoaderContentInfo.getAppName());
		values.put(DownLoaderContentInfo.TableConst.APPURL,
				downLoaderContentInfo.getAppUrl());
		values.put(DownLoaderContentInfo.TableConst.DLSTATUS,
				downLoaderContentInfo.getDlStatus());
		values.put(DownLoaderContentInfo.TableConst.ICONNAME,
				downLoaderContentInfo.getIconName());
		values.put(DownLoaderContentInfo.TableConst.TASKDATE,
				String.valueOf(downLoaderContentInfo.getTaskdate()));
		long rowId = db.insert(DownLoaderContentInfo.TableConst.TABLE_NAME,
				null, values);

		db.close();

		return rowId;
	}

	/**
	 * updateCurrentValue
	 * 
	 * @param downLoaderContentInfo
	 * @param flag
	 * 
	 */
	public int updateDLStatus(DownLoaderContentInfo downLoaderContentInfo) {
		db = this.dbHelper.getWritableDatabase();
		values = new ContentValues();
		values.put(DownLoaderContentInfo.TableConst.DLSTATUS,
				downLoaderContentInfo.getDlStatus());
		int count = db
				.update(DownLoaderContentInfo.TableConst.TABLE_NAME, values,
						DownLoaderContentInfo.TableConst.DQID + " = ?",
						new String[] { String.valueOf(downLoaderContentInfo
								.getDqId()) });
		db.close();
		return count;
	}

	/**
	 * updateAppIconName
	 * 
	 * @param downLoaderContentInfo
	 * @param flag
	 * 
	 */
	public int updateAppIcon(DownLoaderContentInfo downLoaderContentInfo) {
		db = this.dbHelper.getWritableDatabase();
		values = new ContentValues();
		values.put(DownLoaderContentInfo.TableConst.ICONNAME,
				downLoaderContentInfo.getIconName());
		int count = db
				.update(DownLoaderContentInfo.TableConst.TABLE_NAME, values,
						DownLoaderContentInfo.TableConst.DQID + " = ?",
						new String[] { String.valueOf(downLoaderContentInfo
								.getDqId()) });
		db.close();
		return count;
	}

	/**
	 * 根据用户ID获取用户对象
	 * 
	 * @param userId
	 * @return
	 */
	public DownLoaderContentInfo getDownLoadInfoByDQId(long dqId) {

		db = dbHelper.getReadableDatabase();
		DownLoaderContentInfo downLoaderContentInfo = null;
		Cursor cursor = db.query(DownLoaderContentInfo.TableConst.TABLE_NAME,
				columns, DownLoaderContentInfo.TableConst.DQID + "=?",
				new String[] { String.valueOf(dqId) }, null, null, null);

		if (null != cursor) {
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				downLoaderContentInfo = new DownLoaderContentInfo();

				long dqid = Long
						.valueOf(cursor.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.DQID)));
				String appName = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.APPNAME));
				String appId = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.APPID));
				String appUrl = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.APPURL));
				int currentValue = cursor
						.getInt(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.DLSTATUS));
				String iconName = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.ICONNAME));
				long taskDate = Long
						.valueOf(cursor.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.TASKDATE)));

				downLoaderContentInfo.setAppUrl(appUrl);
				downLoaderContentInfo.setAppName(appName);
				downLoaderContentInfo.setAppId(appId);
				downLoaderContentInfo.setDqId(dqid);
				downLoaderContentInfo.setDlStatus(currentValue);
				downLoaderContentInfo.setIconName(iconName);
				downLoaderContentInfo.setTaskdate(taskDate);
			}
		}
		cursor.close();
		db.close();
		return downLoaderContentInfo;
	}

	/**
	 * query all download task
	 * 
	 * @return
	 */
	public List<DownLoaderContentInfo> findAllDownLoadInfos() {

		db = this.dbHelper.getReadableDatabase();

		List<DownLoaderContentInfo> users = null;

		Cursor cursor = db.query(DownLoaderContentInfo.TableConst.TABLE_NAME,
				columns, null, null, null, null, null);

		if (null != cursor && cursor.getCount() > 0) {

			users = new ArrayList<DownLoaderContentInfo>(cursor.getCount());

			DownLoaderContentInfo downLoaderContentInfo = null;
			while (cursor.moveToNext()) {
				downLoaderContentInfo = new DownLoaderContentInfo();
				long dqid = Long
						.valueOf(cursor.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.DQID)));
				String appId = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.APPID));
				String appName = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.APPNAME));
				String appUrl = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.APPURL));
				int currentValue = cursor
						.getInt(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.DLSTATUS));
				String iconName = cursor
						.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.ICONNAME));
				long taskDate = Long
						.valueOf(cursor.getString(cursor
								.getColumnIndex(DownLoaderContentInfo.TableConst.TASKDATE)));
				downLoaderContentInfo.setAppUrl(appUrl);
				
				downLoaderContentInfo.setAppId(appId);
				downLoaderContentInfo.setAppName(appName);
				downLoaderContentInfo.setDqId(dqid);
				downLoaderContentInfo.setDlStatus(currentValue);
				downLoaderContentInfo.setIconName(iconName);
				downLoaderContentInfo.setTaskdate(taskDate);

				users.add(downLoaderContentInfo);
			}
		}
		cursor.close();
		db.close();
		return users;
	}

	/**
	 * 删除 downloadinfo 表的记录
	 * 
	 * @param dqid
	 * @return
	 */
	public int delTaskById(long dqid) {
		db = this.dbHelper.getWritableDatabase();
		int count = db.delete(DownLoaderContentInfo.TableConst.TABLE_NAME,
				DownLoaderContentInfo.TableConst.DQID + " = ? ",
				new String[] { String.valueOf(dqid) });
		return count;
	}

}
