package com.samuel.downloader.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RecommandAdapter  extends BaseAdapter{

	private Context context;
	private List<String> imgs;

	public RecommandAdapter(Context context , List<String> imgs ){
		this.context= context ;
		this.imgs= imgs ;
	}
	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		return null;
	}

}
