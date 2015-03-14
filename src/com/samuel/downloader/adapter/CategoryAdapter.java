package com.samuel.downloader.adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samuel.downloader.app.R;
import com.samuel.downloader.bean.CategoryInfo;
import com.samuel.downloader.cache.image.ImageDownloader;
import com.samuel.downloader.tools.ImageLoader;
import com.samuel.downloader.utils.MD5;
import com.samuel.downloader.utils.SYSCS;

public class CategoryAdapter extends BaseAdapter {

	protected Context context;
	private List<CategoryInfo> data;
	protected int listviewItem;
	protected ImageDownloader imageDownloader;
	protected LayoutInflater inflater;

	public CategoryAdapter(Context context, List<CategoryInfo> data,
			int listviewItem) {
		this.context = context;
		this.data = data;
		this.listviewItem = listviewItem;
		imageDownloader = new ImageDownloader(context);

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView item_name = null;
		TextView item_detail = null;
		// ImageView new_image_tag_view = null;
		ImageView cicon;

		if (convertView == null) {
			convertView = inflater.inflate(listviewItem, null);
			RelativeLayout item_body = (RelativeLayout) convertView
					.findViewById(R.id.item_body);

			// new_image_tag_view = (ImageView) item_body
			// .findViewById(R.id.new_image_tag_view);
			// if (position % 2 == 0) {
			// new_image_tag_view.setVisibility(View.VISIBLE);
			// }
			item_name = (TextView) item_body.findViewById(R.id.item_name);
			item_detail = (TextView) item_body.findViewById(R.id.item_detail);
			cicon = (ImageView) item_body.findViewById(R.id.icon);
			convertView.setTag(new DataWrapper(item_name, item_detail, cicon));

		} else {
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			item_name = dataWrapper.item_name;
			item_detail = dataWrapper.item_detail;
			cicon = dataWrapper.imageView;
		}
		CategoryInfo categoryInfo = data.get(position);
		// System.out.println("AppCategory:" + categoryInfo.getAppCategory());
		item_name.setVisibility(View.GONE);
		item_detail.setText(categoryInfo.getAppCategory());
		item_detail.setVisibility(View.VISIBLE);
		cicon.setImageDrawable(context.getResources().getDrawable(R.drawable.app_icon_tbd));
		imageDownloader.download(getImageUrl(categoryInfo.getCategoryIcon()), cicon);
		// asyncImageLoad(cicon, categoryInfo.getCategoryIcon());
		return convertView;
	}

	private String getImageUrl(String imageurl) {

		if (imageurl == null || imageurl == "" || imageurl.length() <= 4) {
			return null;
		}
		
		System.out.println("imageurl------>>>"+SYSCS.CONFCS.FILE_ENTITY_CATEGORY  + imageurl);
		return SYSCS.CONFCS.FILE_ENTITY_CATEGORY  + imageurl;
	}

	private  class DataWrapper {

		TextView item_name = null;
		TextView item_detail = null;
		ImageView imageView;

		public DataWrapper(TextView item_name, TextView item_detail,
				ImageView imageView) {
			super();
			this.item_name = item_name;
			this.imageView = imageView;
			this.item_detail = item_detail;
		}
	}

	private void asyncImageLoad(final ImageView imageView, String imageurl) {
		// if (imageurl == null || imageurl == "" || imageurl.length() <= 4) {
		// return;
		// }
		// imageurl = SYSCS.CONFCS.FILE_IMAGE_APPICON + imageurl;
		// imageView.setTag(imageurl);
		// AsyncLoadImage asyncLoadImage = new AsyncLoadImage();
		//
		// Drawable drawable = asyncLoadImage.loadDrawable(imageurl, new
		// AsyncLoadImage.ImageCallback(){
		//
		// @Override
		// public void imageLoad(Drawable image,String imageUrl) {
		// if(imageUrl.equals(imageView.getTag())){
		// imageView.setImageDrawable(image);
		// }
		// else {
		// System.out.println(imageUrl+ "连接不到啊...");
		// }
		// }
		// });
		// if(drawable==null){
		// imageView.setImageResource(R.drawable.app_icon_tbd);
		// }else{
		// imageView.setImageDrawable(drawable);
		// }
		//

		AsyncImageTask asyncImgTask = new AsyncImageTask(imageView);

		asyncImgTask.execute(imageurl);

	}

	/**
	 * AsyncImageTask
	 * 
	 * @author samuel
	 * 
	 */
	private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {
		private ImageView imageView;

		private String imageurl;

		public AsyncImageTask(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		protected Uri doInBackground(String... params) {

			imageurl = params[0];
			if (imageurl == null || imageurl == "" || imageurl.length() <= 4) {
				return null;
			}
			imageurl = SYSCS.CONFCS.FILE_ENTITY_CATEGORY + imageurl;

			imageView.setTag(imageurl);
			// System.out.println("categoryadapter imageurl----->>>" +
			// imageurl);
			try {
				return ImageLoader.getImage(imageurl,
						SYSCS.LCS.IMG_ICONCACHEENTITYURL,
						getImageUniqueName(imageurl), 70, 70, 0.5f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Uri result) {
			if (result != null && imageView != null) {
				if (imageurl.equalsIgnoreCase((String) imageView.getTag())) {

					imageView.setImageURI(result);
				} else {
					// Log.i("LoadImage------>>>", "网络异常图片加载失败");
				}
			}
		}

		protected String getImageUniqueName(String imageurl) {

			return MD5.getMD5(imageurl) + ".png";
		}
	}

}
