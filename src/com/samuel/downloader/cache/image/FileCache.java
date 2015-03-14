package com.samuel.downloader.cache.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;


public class FileCache {
	
	@SuppressWarnings("unused")
	private static final String LOG_TAG = FileCache.class.getSimpleName();
	
	private File cacheDir;
	
	/**
	 * @param context
	 */
	public FileCache(Context context) {
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "fuImageCache");
		} else {
			cacheDir = context.getCacheDir();
		}
		if(!cacheDir.exists()) {
			cacheDir.mkdir();
		}
	}
	
	/**
	 * @param url
	 * @param bitmap
	 */
	public void addToFileCache(String url, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(getFromFileCache(url));
			copyStream(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public File getFromFileCache(String url) {
		String fileName = urlToFileName(url);
		File file = new File(cacheDir, fileName);
		return file;
	}
	
	/**
	 * 清空文件缓存
	 */
	public void clearCache() {
		File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
	}
	
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	private String urlToFileName(String url) {
		return String.valueOf(url.hashCode());
	}
	
	private void copyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


}
