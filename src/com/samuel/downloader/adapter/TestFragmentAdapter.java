package com.samuel.downloader.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.samuel.downloader.bean.RecommandBean;
import com.samuel.downloader.view.TestFragment;

public class TestFragmentAdapter extends FragmentPagerAdapter {
//	protected static final Integer[] CONTENT = new Integer[] { R.drawable.back,
//			R.drawable.background, R.drawable.back, R.drawable.background };

//	private int mCount = CONTENT.length;
	private int mCount ;
	
	

	private List<RecommandBean> recommandLists;

	public TestFragmentAdapter(FragmentManager fm,List<RecommandBean> recommandLists) {
		super(fm);
		this.setRecommandLists(recommandLists);
		mCount = (getRecommandLists() != null) ? getRecommandLists().size() : 0;
	}

	@Override
	public Fragment getItem(int position) {
		return TestFragment.newInstance(recommandLists.get(position%(mCount)));
	}

	@Override
	public int getCount() {
		return mCount;
	}

	// @Override
	// public CharSequence getPageTitle(int position) {
	// return TestFragmentAdapter.CONTENT[position % CONTENT.length];
	// }

//	/**
//	 * have to make final so we can see it inside of onClick()
//	 */
//	public Object instantiateItem(View collection, final int pos) {
//		LayoutInflater inflater = (LayoutInflater) collection.getContext()
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View page = inflater.inflate(R.layout.indicatorpager, null);
//
//		page.getContext();
//
//		page.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				// this will log the page number that was click
//				Log.i("TAG", "This page was clicked: " + pos);
//			}
//		});
//		((ViewPager) collection).addView(page, 0);
//		return page;
//	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

	public List<RecommandBean> getRecommandLists() {
		return recommandLists;
	}

	public void setRecommandLists(List<RecommandBean> recommandLists) {
		this.recommandLists = recommandLists;
	}
}