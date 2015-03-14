package com.samuel.downloader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ScreenShotLayout extends LinearLayout {

	private ScreenShotAdapter adapter;
	private Context context;

	public ScreenShotLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	public void setAdapter(ScreenShotAdapter adapter) {
		this.adapter = adapter;
		for (int i = 0; i < adapter.getCount(); i++) {
			// final Map<String,Object> map=adapter.getItem(i);
			View view = adapter.getView(i, null, null);
//			if (i == adapter.getCount() - 1) {
//				view.setPadding(10, 10, 10, 10);
//			} else {
//				view.setPadding(10, 10, 0, 10);
//			}
			// view.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// // Toast.makeText(context, map.get("text").toString(),
			// Toast.LENGTH_SHORT).show();
			// }
			// });
			LayoutParams params  =  new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
			params.weight = 1.0f;
			params.leftMargin=2;
			params.rightMargin= 2;
			params.topMargin= 2;
			params.bottomMargin = 2;
			this.setOrientation(HORIZONTAL);
			this.addView(view,params);
		}
	}
}
