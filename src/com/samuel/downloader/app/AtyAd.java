package com.samuel.downloader.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.samuel.downloader.bean.RecommandBean;

public class AtyAd extends Activity implements OnClickListener {

	protected TextView current_content_title_back,current_content_title;
	private WebView about_us;
	private int mDensity;

	private RecommandBean recBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about);

		this.recBean = (RecommandBean) getIntent().getSerializableExtra(
				RecommandBean.TAG.RecommandBean);

		current_content_title = (TextView) this
				.findViewById(R.id.current_content_title);
		current_content_title.setText("应用推广");
		
		current_content_title_back = (TextView) this
				.findViewById(R.id.current_content_title_back);
		current_content_title_back.setOnClickListener(this);

		about_us = (WebView) this.findViewById(R.id.about_us);
		WebSettings mWebSettings = about_us.getSettings();
		
		about_us.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
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
		if(recBean.getAdUrl()!=null&&recBean.getAdUrl()!=""&&recBean.getAdUrl()!="null"){
			about_us.loadUrl(recBean.getAdUrl());
		}else{
			about_us.loadUrl("http://app.foxconn.com/appmarket_user");
			about_us.setContentDescription("网页被外星人带走了， Sorry ^0^");
		}
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
