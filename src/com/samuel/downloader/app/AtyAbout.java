package com.samuel.downloader.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.TextView;

import com.samuel.downloader.utils.SYSCS;

public class AtyAbout extends Activity implements OnClickListener {

	protected TextView current_content_title_back,current_content_title;
	private WebView about_us;
	private int mDensity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about);
		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title.setText(R.string.sys_about_us);
		
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
		current_content_title_back.setOnClickListener(this);

		about_us = (WebView) this.findViewById(R.id.about_us);
		WebSettings mWebSettings = about_us.getSettings();
		mWebSettings.setBuiltInZoomControls(true); // 支持页面放大缩小按钮
		mWebSettings.setSupportZoom(true);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mDensity = metrics.densityDpi;

		if (mDensity == 240) { // 可以让不同的density的情况下，可以让页面进行适配
			mWebSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == 160) {
			mWebSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 120) {
			mWebSettings.setDefaultZoom(ZoomDensity.CLOSE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		about_us.loadUrl(SYSCS.CONFCS.ABOUT_US_URL);
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
