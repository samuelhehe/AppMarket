package com.samuel.downloader.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.samuel.downloader.app.R;
import com.samuel.downloader.bean.RecommandBean;
import com.samuel.downloader.cache.image.ImageDownloader;

public class FocusAdapter extends BaseAdapter {

	private Context context;
	private List<RecommandBean> recommandList;
	private ImageDownloader imageDownloader;

	public FocusAdapter(Context context, List<RecommandBean> data) {
		this.context = context;
		this.recommandList = data;
		this.imageDownloader = new ImageDownloader(context);
	}

	@Override
	public int getCount() {
		return recommandList.size();
	}

	@Override
	public RecommandBean getItem(int position) {
		return recommandList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addObject(RecommandBean recBean) {
		recommandList.add(recBean);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.image_list,
				null);
		ImageView image = (ImageView) view.findViewById(R.id.imageList_img);
		RecommandBean map = recommandList.get(position);

//		if (SYSCS.ConValue.width != 0) {
//
//			layoutParams.height = (SYSCS.ConValue.width / 830) * 300;
//		} else {
//		}
//		if (SYSCS.ConValue.height != 0) {
//
//			layoutParams.width = SYSCS.ConValue.width;
//		} else {
//		}
		LayoutParams layoutParams = image.getLayoutParams();
		//设置样式 ：填充整个屏幕
		image.setScaleType(ImageView.ScaleType.FIT_XY); 
		layoutParams.height = 200;
		layoutParams.width = LayoutParams.FILL_PARENT;
		image.setLayoutParams(layoutParams);

		imageDownloader.download(map.getPicUrl(), image);

		// image.setBackgroundDrawable((Drawable)map.getPicUrl());

		return view;
	}
}
