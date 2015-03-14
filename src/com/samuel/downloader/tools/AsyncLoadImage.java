package com.samuel.downloader.tools;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.Drawable;
import android.os.Handler;


public class AsyncLoadImage {
	//缓存
	private Map<String,SoftReference<Drawable>> imageCache = new HashMap<String,SoftReference<Drawable>>();

	private ExecutorService executorService = Executors.newFixedThreadPool(20);//总共�?0个线程循环使�?
	//Hanlder
	private Handler mHandler = new Handler();
	public interface ImageCallback {
		void imageLoad(Drawable image,String imageUrl);
	}
	/**
	 * 
	 * @param imageUrl 图片的地
	 * @param imageCallback 回调接口
	 * @return 返回内存中缓存的图像 第一次返回null
	 */
	public Drawable loadDrawable(final String imageUrl,final ImageCallback imageCallback){
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if(softReference.get()!=null){ 
				return softReference.get();  
			}
		}
		 
		executorService.submit(new Runnable(){
			@Override
			public void run() {
				try {
					final Drawable drawable = getDrawableFormUrl(imageUrl); 
//					final Drawable drawable = ImageLoader.getDrawableFromUrl(imageUrl); 
					
					imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));//将加载的图片放入到内存中
					mHandler.post(new Runnable(){
						@Override
						public void run() {
							imageCallback.imageLoad(drawable,imageUrl); 
						}
					});
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
			
		});
		return null ;
	}
	/**
	 * 从网络上获取数据
	 */
	public Drawable getDrawableFormUrl(String imageUrl){
		Drawable drawable = null ;
		try {
			drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return drawable ;
	}

}
