package com.samuel.downloader.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.samuel.downloader.app.R;
import com.samuel.downloader.cache.image.ImageDownloader;

public class ScreenShotAdapter extends BaseAdapter {

	private List<String> shoturls;
	private Context context;
	private ImageDownloader imageDownloader;

	public ScreenShotAdapter(Context context, List<String> shoturls) {
		this.context = context;
		this.shoturls = shoturls;
		imageDownloader = new ImageDownloader(context);
	}

	@Override
	public int getCount() {
		return shoturls.size();
	}

	@Override
	public String getItem(int location) {
		return shoturls.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addObject(String url) {
		shoturls.add(url);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int location, View convertView, ViewGroup arg2) {
		View view = LayoutInflater.from(context).inflate(R.layout.movie, null);

		ImageView image = (ImageView) view.findViewById(R.id.screen_shot_image);
		LayoutParams layoutParams = image.getLayoutParams();
		//设置样式 ：填充整个屏幕
		image.setScaleType(ImageView.ScaleType.FIT_XY); 
		layoutParams.height = 400;
//		layoutParams.width = LayoutParams.WRAP_CONTENT;
		
		layoutParams.width = 280;
		
		String imageUrl = getItem(location);
		System.out.println("ScreenAdapter imageUrl ------------>>" + imageUrl);
		if (imageUrl != null && imageUrl != "" && imageUrl != "null"
				&& imageUrl.length() > 4) {
			
			imageDownloader.download(getItem(location), image);
		} else {
			
			
			image.setImageDrawable(context.getResources().getDrawable(
					R.drawable.place_holder_screen));
		}
		return view;
	}
}
