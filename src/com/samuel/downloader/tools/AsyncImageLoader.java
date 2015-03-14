package com.samuel.downloader.tools;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.samuel.downloader.utils.MD5;
import com.samuel.downloader.utils.SYSCS;

public class AsyncImageLoader {

	/**
	 * 图片数据缓存 key = url value = 图片Uri对象k
	 */
	private static HashMap<String, SoftReference<Uri>> imageCache;

	public AsyncImageLoader() {
		if (imageCache == null) {
			imageCache = new HashMap<String, SoftReference<Uri>>();
		}
	}

	/**
	 * 资源下载并缓存
	 * 
	 * @param url
	 *            需要下载的资源
	 * @param imageView
	 *            需要设置图片的组件
	 * @param callback
	 *            回调（在回调中进行参数2 图片资源的设置）
	 * @return Uri
	 */
	public Uri loadUri(final String url, final ImageView imageView,
			final ImageCallback callback) {
		// 判断一个HashMap中的key有没有包含url
		if (imageCache.containsKey(url)) {
			// 获得缓存中的Uri对象数据
			SoftReference<Uri> softReference = imageCache.get(url);
			Uri drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				callback.imageLoaded((Uri) msg.obj, imageView, url);
			}
		};
		new Thread() {
			public void run() {

				Uri drawable;
				try {
					drawable = ImageLoader.getImage(url,SYSCS.LCS.IMG_ICONCACHEENTITYURL,getImageUniqueName(url), 70, 70, 0.5f);
//					drawable = ImageLoader.getDrawableFromUrl(url);
					imageCache.put(url, new SoftReference<Uri>(drawable));
					Message msg = handler.obtainMessage(0, drawable);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			protected String getImageUniqueName(String imageurl) {

				return MD5.getMD5(imageurl) + ".png";
			}

		}.start();
		return null;
	}

	/**
	 * 回调接口
	 * 
	 * @author hanfei.li
	 */
	public interface ImageCallback {
		/**
		 * 实现ImgaeView组件设置内容操作
		 * 
		 * @param drawable
		 * @param iv
		 * @param url
		 */
		public void imageLoaded(Uri drawable, ImageView iv, String url);
	}
}
