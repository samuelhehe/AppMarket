package com.samuel.downloader.infocenter.adatper;

import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samuel.downloader.app.R;
import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.utils.TextFormater;

public class CommonAdapter extends BaseAdapter {

	private static final int SHOPLIST = 0;

	private static final int INFOLIST = 1;

	protected Context context;

	private List<ShopMsgInfo> datas;

	protected int listViewItem, flag;

	protected LayoutInflater inflater;

	public CommonAdapter(Context context, List<ShopMsgInfo> datas,
			int listViewItem, int flag) {

		this.context = context;
		this.datas = datas;
		this.listViewItem = listViewItem;
		this.flag = flag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView shopName = null;
		TextView themeContent = null;
		TextView receiveDate = null;

		if (convertView == null) {

			convertView = inflater.inflate(listViewItem, null);
			shopName = (TextView) convertView.findViewById(R.id.shop_name);
			shopName.setEllipsize(TruncateAt.END);
			themeContent = (TextView) convertView
					.findViewById(R.id.theme_content);
			receiveDate = (TextView) convertView
					.findViewById(R.id.receive_date);
			convertView.setTag(new DataWrapper(shopName, themeContent,
					receiveDate));
		} else {

			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			shopName = dataWrapper.shopName;
			themeContent = dataWrapper.themeContent;
			receiveDate = dataWrapper.receiveData;
		}

		ShopMsgInfo shopMsgInfo = datas.get(position);
		
		if(TextFormater.isEmpty(shopMsgInfo.getReceiveDate())){
			receiveDate.setText(com.samuel.downloader.utils.TimeUtils.getTime(System.currentTimeMillis()));
		}else{
			receiveDate.setText(com.samuel.downloader.utils.TimeUtils.getTime(Long.parseLong(shopMsgInfo.getReceiveDate())));
		}
//		receiveDate.setText(shopMsgInfo.getReceiveDate());
		if (flag == SHOPLIST) {
			shopName.setText(shopMsgInfo.getShopName());
			String theme = shopMsgInfo.getMsgTheme();
			if (theme.length() > 26) {
				theme = theme.substring(0, 25) + "...";
			}
			themeContent.setText(theme);
		} else if (flag == INFOLIST) {
			shopName.setText(shopMsgInfo.getMsgTheme());
			String content = shopMsgInfo.getMsgContent();
			if (content.length() > 34) {
				content = content.substring(0, 34) + " ...";
			}
			themeContent.setText(content);
		}
		
		return convertView;
	}

	private static class DataWrapper {
		TextView shopName = null;
		TextView themeContent = null;
		TextView receiveData = null;

		public DataWrapper(TextView shopName, TextView themeContent,
				TextView receiveData) {
			super();
			this.shopName = shopName;
			this.themeContent = themeContent;
			this.receiveData = receiveData;
		}
	}

}
