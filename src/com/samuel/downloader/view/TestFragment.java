package com.samuel.downloader.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.samuel.downloader.app.AtyAd;
import com.samuel.downloader.app.AtyAppDetails;
import com.samuel.downloader.bean.AppInfo;
import com.samuel.downloader.bean.RecommandBean;
import com.samuel.downloader.cache.image.ImageDownloader;

public final class TestFragment extends Fragment implements OnClickListener {
	private static final String KEY_CONTENT = "TestFragment:Content";

	public static TestFragment newInstance(Integer content) {
		TestFragment fragment = new TestFragment();
		// fragment.mContent = content;
		return fragment;
	}

	public static TestFragment newInstance(RecommandBean recommandBean) {
		TestFragment fragment = new TestFragment();

		fragment.recommandBean = recommandBean;
		return fragment;
	}

	private RecommandBean recommandBean;
	private ImageDownloader imageDownloader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		imageDownloader = new ImageDownloader(getActivity());

	}

	// private int mContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageView image = new ImageView(getActivity());
		// image.setBackgroundDrawable(getResources().getDrawable(mContent));
		imageDownloader.download(recommandBean.getPicUrl(), image);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		image.setScaleType(ScaleType.FIT_XY);
		image.setOnClickListener(this);

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		layout.setGravity(Gravity.CENTER);
		layout.addView(image);
		return layout;
	}

	@Override
	public void onClick(View v) {

		Log.i("TAG", "This page was clicked: " + recommandBean.getPicUrl());
		System.out.println(recommandBean);
		if (recommandBean.isIsad()) {

			startActivity(new Intent(getActivity(), AtyAd.class).putExtra(
					RecommandBean.TAG.RecommandBean, recommandBean));
		} else {
			startActivity(new Intent(getActivity(), AtyAppDetails.class)
					.putExtra(AppInfo.TAG.TAG_CLASSNAME, recommandBean.appInfo));
		}
	}

}
