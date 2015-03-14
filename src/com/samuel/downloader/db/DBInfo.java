package com.samuel.downloader.db;

import com.samuel.downloader.bean.DownLoaderContentInfo;

public class DBInfo {
    public static class DB {

        /**
         * 数据库名称
         */
        public static final String DB_NAME = "appmarket.db";

        /**
         * 数据库版本
         */
        public static final int VERSION = 8;
    }

    public static class Table {
        /**
         * DOWNLOAD_INFO_TB_NAME 表名
         */
        public static final String DOWNLOAD_INFO_TB_NAME = DownLoaderContentInfo.TableConst.TABLE_NAME;
        
        
        /**
         * 
         * INSTALLEDAPP_INFO_TB_NAME 
         */
//        public static final String INSTALLEDAPP_INFO_TB_NAME = InstalledAppInfo.TableConst.TABLE_NAME;
        
        
        
        /**
         * 建表 DOWNLOAD_INFO_TB_NAME 语句
         */
        public static final String DOWNLOAD_INFO_CREATE = "CREATE TABLE IF NOT EXISTS  "
                + DOWNLOAD_INFO_TB_NAME
                + " ( _id INTEGER PRIMARY KEY autoincrement,"
                + DownLoaderContentInfo.TableConst.DQID+ " TEXT , " 
                + DownLoaderContentInfo.TableConst.APPNAME+ " TEXT , " 
                + DownLoaderContentInfo.TableConst.APPID+ " TEXT , " 
                + DownLoaderContentInfo.TableConst.DLSTATUS+ " INTEGER , " 
                + DownLoaderContentInfo.TableConst.ICONNAME+ " TEXT , " 
                + DownLoaderContentInfo.TableConst.APPURL+ " TEXT , " 
                + DownLoaderContentInfo.TableConst.TASKDATE+ " TEXT )"; 
//        /**
//         * 建表 INSTALLEDAPP_INFO_TB_NAME 语句
//         */
//        public static final String INSTALLEDAPP_INFO_CREATE = "CREATE TABLE IF NOT EXISTS  "
//        		+ INSTALLEDAPP_INFO_TB_NAME
//        		+ " ( _id INTEGER PRIMARY KEY autoincrement,"
//        		+ InstalledAppInfo.TableConst.APPNAME+ " TEXT , " 
//        		+ InstalledAppInfo.TableConst.PACKAGENAME+ " TEXT , " 
//        		+ InstalledAppInfo.TableConst.APPVERSIONCODE+ " INTEGER , " 
//        		+ InstalledAppInfo.TableConst.APPVERSIONNAME+ " TEXT , " 
//        		+ InstalledAppInfo.TableConst.INSTALLEDDATE+ " TEXT )"; 
        /**
         * 删除表DOWNLOAD_INFO_TB_NAME 空间语句
         */
        public static final String DOWNLOAD_INFO_DROP = "DROP TABLE "+ DOWNLOAD_INFO_TB_NAME;
        /**
         * 删除表INSTALLEDAPP_INFO_TB_NAME 空间语句
         */
//        public static final String INSTALLEDAPP_INFO_DROP = "DROP TABLE "+ INSTALLEDAPP_INFO_TB_NAME;
    }
    
}
