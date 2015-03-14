package com.samuel.downloader.tools;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

public class OpenFileTool {

	public static Intent openFile(String filePath){

		File file = new File(filePath);
		if(!file.exists()) return null;
		String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase(); 
		if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
		end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
		return getAudioFileIntent(filePath);
		}else if(end.equals("3gp")||end.equals("mp4")){
		return getAudioFileIntent(filePath);
		}else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
		end.equals("jpeg")||end.equals("bmp")){
		return getImageFileIntent(filePath);
		}else if(end.equals("apk")){
		return getApkFileIntent(filePath);
		}else if(end.equals("ppt")){
		return getPptFileIntent(filePath);
		}else if(end.equals("xls")){
		return getExcelFileIntent(filePath);
		}else if(end.equals("doc")){
		return getWordFileIntent(filePath);
		}else if(end.equals("pdf")){
		return getPdfFileIntent(filePath);
		}else if(end.equals("chm")){
		return getChmFileIntent(filePath);
		}else if(end.equals("txt")){
		return getTextFileIntent(filePath,false);
		}else{
		return getAllIntent(filePath);
		}
		}
		//Android��ȡһ�����ڴ�APK�ļ���intent
		public static Intent getAllIntent( String param ) {

		Intent intent = new Intent();  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"*/*"); 
		return intent;
		}
		//Android��ȡһ�����ڴ�APK�ļ���intent
		public static Intent getApkFileIntent( String param ) {

		Intent intent = new Intent();  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/vnd.android.package-archive"); 
		return intent;
		}

		//Android��ȡһ�����ڴ�VIDEO�ļ���intent
		public static Intent getVideoFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "video/*");
		return intent;
		}

		//Android��ȡһ�����ڴ�AUDIO�ļ���intent
		public static Intent getAudioFileIntent( String param ){

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "audio/*");
		return intent;
		}

		//Android��ȡһ�����ڴ�Html�ļ���intent   
		public static Intent getHtmlFileIntent( String param ){

		Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
		}

		//Android��ȡһ�����ڴ�ͼƬ�ļ���intent
		public static Intent getImageFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "image/*");
		return intent;
		}

		//Android��ȡһ�����ڴ�PPT�ļ���intent   
		public static Intent getPptFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");   
		return intent;   
		}   

		//Android��ȡһ�����ڴ�Excel�ļ���intent   
		public static Intent getExcelFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-excel");   
		return intent;   
		}   

		//Android��ȡһ�����ڴ�Word�ļ���intent   
		public static Intent getWordFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/msword");   
		return intent;   
		}   

		//Android��ȡһ�����ڴ�CHM�ļ���intent   
		public static Intent getChmFileIntent( String param ){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/x-chm");   
		return intent;   
		}   

		//Android��ȡһ�����ڴ��ı��ļ���intent   
		public static Intent getTextFileIntent( String param, boolean paramBoolean){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		if (paramBoolean){   
		Uri uri1 = Uri.parse(param );   
		intent.setDataAndType(uri1, "text/plain");   
		}else{   
		Uri uri2 = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri2, "text/plain");   
		}   
		return intent;   
		}  
		//Android��ȡһ�����ڴ�PDF�ļ���intent   
		public static Intent getPdfFileIntent( String param ){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/pdf");   
		return intent;   
		}
}
