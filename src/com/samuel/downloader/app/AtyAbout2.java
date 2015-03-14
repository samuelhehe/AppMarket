package com.samuel.downloader.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AtyAbout2  extends  Activity implements OnClickListener{

	protected TextView current_content_title_back ,current_content_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about);
		current_content_title = (TextView) this.findViewById(R.id.current_content_title);
		current_content_title.setText(R.string.sys_about_us);
		  current_content_title_back = (TextView) this.findViewById(R.id.current_content_title_back);
		  current_content_title_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.current_content_title_back:
			
			this.finish();
			break;

		default:
			break;
		}
		
	}
	
	
}
