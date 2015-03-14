package com.samuel.downloader.infocenter.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.infocenter.db.DBHelper;

public class MsgInfoDBHelper {

	private DBHelper dbHelper = null;

	private SQLiteDatabase db = null;

	private ContentValues values = null;

	private String[] columns = { ShopMsgInfo.TableConst.SHOP_NAME,
			ShopMsgInfo.TableConst.MSG_THEME,
			ShopMsgInfo.TableConst.RECEIVE_DATE,
			ShopMsgInfo.TableConst.MSG_CONTENT };

	public MsgInfoDBHelper(Context context) {

		dbHelper = new DBHelper(context);

	}

	/**
	 * insertMsgInfo
	 * 
	 * @param shopMsgInfo
	 * @return
	 */
	public long insertMsgInfo(ShopMsgInfo shopMsgInfo) {

		db = dbHelper.getReadableDatabase();

		values = new ContentValues(4);

		values.put(ShopMsgInfo.TableConst.SHOP_NAME,
				String.valueOf(shopMsgInfo.getShopName()));
		values.put(ShopMsgInfo.TableConst.MSG_THEME,
				String.valueOf(shopMsgInfo.getMsgTheme()));
		values.put(ShopMsgInfo.TableConst.RECEIVE_DATE,
				String.valueOf(shopMsgInfo.getReceiveDate()));
		values.put(ShopMsgInfo.TableConst.MSG_CONTENT,
				String.valueOf(shopMsgInfo.getMsgContent()));

		long rowId = db.insert(ShopMsgInfo.TableConst.TABLE_NAME, null, values);

		db.close();

		return rowId;
	}

	/**
	 * findShopList
	 * 
	 * @return
	 */
	public List<ShopMsgInfo> findShopList() {

		List<ShopMsgInfo> msgInfos = new ArrayList<ShopMsgInfo>();

		List<String> shopName = new ArrayList<String>();

		db = dbHelper.getReadableDatabase();

		Cursor cursor = db.query(ShopMsgInfo.TableConst.TABLE_NAME, columns,
				null, null, null, null, null);

		if (cursor != null) {
			if (cursor.getCount() > 0) {
				if (cursor.moveToLast()) {

					do {
						int flag = 0;
						ShopMsgInfo shopMsgInfo = new ShopMsgInfo();
						String nextName = cursor
								.getString(cursor
										.getColumnIndex(ShopMsgInfo.TableConst.SHOP_NAME));

						if (shopName.size() != 0) {
							for (int i = 0; i < shopName.size(); i++) {
								if (nextName.equals(shopName.get(i))) {
									flag = 1;
									break;
								}
							}
							if(flag == 0){
								shopName.add(nextName);
								shopMsgInfo.setShopName(nextName);
								shopMsgInfo
										.setMsgTheme(cursor.getString(cursor
												.getColumnIndex(ShopMsgInfo.TableConst.MSG_THEME)));
								shopMsgInfo
										.setReceiveDate(cursor.getString(cursor
												.getColumnIndex(ShopMsgInfo.TableConst.RECEIVE_DATE)));
								shopMsgInfo
										.setMsgContent(cursor.getString(cursor
												.getColumnIndex(ShopMsgInfo.TableConst.MSG_CONTENT)));
								msgInfos.add(shopMsgInfo);
							}
						}else{
							shopName.add(nextName);
							shopMsgInfo.setShopName(nextName);
							shopMsgInfo
									.setMsgTheme(cursor.getString(cursor
											.getColumnIndex(ShopMsgInfo.TableConst.MSG_THEME)));
							shopMsgInfo
									.setReceiveDate(cursor.getString(cursor
											.getColumnIndex(ShopMsgInfo.TableConst.RECEIVE_DATE)));
							shopMsgInfo
									.setMsgContent(cursor.getString(cursor
											.getColumnIndex(ShopMsgInfo.TableConst.MSG_CONTENT)));
							msgInfos.add(shopMsgInfo);
						}

					} while (cursor.moveToPrevious());

				}
			}
			cursor.close();
			db.close();
		}

		return msgInfos;
	}

	/**
	 * findShopListBySName
	 * 
	 * @param shopName
	 * @return
	 */
	public List<ShopMsgInfo> findShopListBySName(String shopName) {

		List<ShopMsgInfo> msgInfos = new ArrayList<ShopMsgInfo>();

		db = dbHelper.getReadableDatabase();

		Cursor cursor = db.query(ShopMsgInfo.TableConst.TABLE_NAME, columns,
				ShopMsgInfo.TableConst.SHOP_NAME + " =? ",
				new String[] { shopName }, null, null, null);

		if (cursor != null) {
			if (cursor.getCount() > 0) {
				if (cursor.moveToLast()) {
					do {

						ShopMsgInfo shopMsgInfo = new ShopMsgInfo();
						shopMsgInfo.setShopName(shopName);
						shopMsgInfo
								.setMsgTheme(cursor.getString(cursor
										.getColumnIndex(ShopMsgInfo.TableConst.MSG_THEME)));
						shopMsgInfo
								.setReceiveDate(cursor.getString(cursor
										.getColumnIndex(ShopMsgInfo.TableConst.RECEIVE_DATE)));
						shopMsgInfo
								.setMsgContent(cursor.getString(cursor
										.getColumnIndex(ShopMsgInfo.TableConst.MSG_CONTENT)));
						msgInfos.add(shopMsgInfo);

					} while (cursor.moveToPrevious());
				}
			}
			cursor.close();
			db.close();
		}

		return msgInfos;
	}

	/**
	 * 
	 * @param shopName
	 * @return
	 */
	public long deleteByShopName(String shopName) {
		db = dbHelper.getReadableDatabase();
		long i = db.delete(ShopMsgInfo.TableConst.TABLE_NAME,
				ShopMsgInfo.TableConst.SHOP_NAME + " =? ",
				new String[] { shopName });
		return i;
	}

	/**
	 * 
	 * @param shopMsgInfo
	 * @return
	 */
	public long deleteByMsgTheme(ShopMsgInfo shopMsgInfo) {
		db = dbHelper.getReadableDatabase();
		long i = db
				.delete(ShopMsgInfo.TableConst.TABLE_NAME,
						ShopMsgInfo.TableConst.SHOP_NAME + " =? and "
								+ ShopMsgInfo.TableConst.MSG_THEME
								+ " = ? and "
								+ ShopMsgInfo.TableConst.RECEIVE_DATE + " =? ",
						new String[] { shopMsgInfo.getShopName(),
								shopMsgInfo.getMsgTheme(),
								shopMsgInfo.getReceiveDate() });
		return i;
	}

}
