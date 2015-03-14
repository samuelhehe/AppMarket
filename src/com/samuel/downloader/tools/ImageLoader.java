package com.samuel.downloader.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class ImageLoader {

	/**
	 * @param imageUrl  "http://mdmss.foxconn.com:8090/appmarket"
	 * @param cacheDir  "mnt/sdcard/picture/"
	 * @param fileName  "abcdefg.png"
	 * @param zoomW		300	
	 * @param zoomY     450
	 * @param roundPx   0.8f
	 * @return
	 * @throws Exception
	 */
	public  static Uri getImage2(String imageUrl, String cacheDir,
			String fileName, int zoomW, int zoomY, float roundPx)
			throws Exception {

		File localFile = new File(cacheDir, fileName);
//System.out.println("File cache : -------------->>>> "+ cacheDir);
//System.out.println("File name  : -------------->>>> "+ fileName);
		if (localFile.exists()) {
			return Uri.fromFile(localFile);
		} else {
			HttpURLConnection conn = (HttpURLConnection) new URL(imageUrl)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				if (null == bitmap) {
					return null;
				}
				Bitmap zoomedMap = BitmapCompressTools.zoomBitmap(bitmap,
						zoomW, zoomY);
				bitmap = null;
				String filePath = BitmapCompressTools.saveBitmap(
						zoomedMap, cacheDir, fileName);
				if (null != filePath && filePath.length() >= 4) {
					zoomedMap =null;
					return Uri.fromFile(new File(filePath));
				}
			}else{
				return null;
			}
		}
		return null;
	}

	/**
	 * @param imageUrl  "http://mdmss.foxconn.com:8090/appmarket"
	 * @param cacheDir  "mnt/sdcard/picture/"
	 * @param fileName  "abcdefg.png"
	 * @param zoomW		300	
	 * @param zoomY     450
	 * @param roundPx   0.8f
	 * @return
	 * @throws Exception
	 */
	public   static Uri getImage(String imageUrl, String cacheDir,
			String fileName, int zoomW, int zoomY, float roundPx)
			throws Exception {

		File localFile = new File(cacheDir, fileName);
		if (localFile.exists()) {
			return Uri.fromFile(localFile);
		} else {
			HttpURLConnection conn = (HttpURLConnection) new URL(imageUrl)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				if (null == bitmap) {
					return null;
				}
				String filePath = BitmapCompressTools.saveBitmap(
						bitmap, cacheDir, fileName);
				if (null != filePath && filePath.length() >= 1) {
					bitmap =null;
					return Uri.fromFile(new File(filePath));
				}

			}else{
				
				return null;
			}
		}
		return null;
	}
	
	
	

	/**
	 * @param imageUrl  "http://mdmss.foxconn.com:8090/appmarket"
	 * @param cacheDir  "mnt/sdcard/picture/"
	 * @param fileName  "abcdefg.png"
	 * @param zoomW		300	
	 * @param zoomY     450
	 * @param roundPx   0.8f
	 * @return
	 * @throws Exception
	 */
	public  static Uri getScreenshot(String imageUrl, String cacheDir,
			String fileName, int zoomW, int zoomY) throws Exception {

		File localFile = new File(cacheDir, fileName);
//System.out.println("File cache : -------------->>>> "+ cacheDir);
//System.out.println("File name  : -------------->>>> "+ fileName);
		if (localFile.exists()) {
			return Uri.fromFile(localFile);
		} else {
			HttpURLConnection conn = (HttpURLConnection) new URL(imageUrl)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				if (null == bitmap) {
					return null;
				}
				Bitmap zoomedMap = BitmapCompressTools.zoomBitmap(bitmap,
						zoomW, zoomY);
				bitmap = null;
				String filePath = BitmapCompressTools.saveBitmap(
						zoomedMap, cacheDir, fileName);
				if (null != filePath && filePath.length() >= 4) {
					zoomedMap =null;
					return Uri.fromFile(new File(filePath));
				}
			}else{
				return null;
			}
		}
		return null;
	}
	
	

	public static List<Drawable> getDrawablesFromUrls(List<String> imageUrls){
			List<Drawable> drawables = new ArrayList<Drawable>();
			for (String imgurl : imageUrls) {
				Drawable drawable = getDrawableFromUrl(imgurl);
				if(drawable!=null){
					drawables.add(drawable);
				}
			}
		return drawables;
	}
	
	/**
     * 通过url参数获得调用的图片资源
     * 
     * @param url
     * @return Drawable
     */
    public static Drawable getDrawableFromUrl(String url) {
        InputStream in = null;
        URLConnection con = null;
        try {
            // 打开连接
            con = new URL(url).openConnection();
            in = con.getInputStream();
            return Drawable.createFromStream(in, "image");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
	 * 
	 * @param bm
	 * 
	 * @param imgPath
	 * 
	 * 
	 * @return
	 */
	public static String saveToLocal(Bitmap bm  , String imgPath ) {
//		String path = Environment.getExternalStorageDirectory().getPath()+"/abc.jpg";
		try {
			FileOutputStream fos = new FileOutputStream(imgPath);
			bm.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return imgPath;
	}

  
}
