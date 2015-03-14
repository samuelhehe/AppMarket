package com.samuel.downloader.infocenter.app;

import java.util.List;

import org.fcm.client.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fcm.libs.NotificationReplay;
import com.samuel.downloader.app.R;
import com.samuel.downloader.infocenter.adatper.CommonAdapter;
import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.infocenter.dao.MsgInfoDBHelper;

public class ShopList extends Activity {

	private static final String LOGTAG = "ShopList";
	
	private static final int LOAD_ERROR = 0;

	private static final int LOAD_SUCCESS = 1;

	private static final int FLAG = 0;

	private ListView shop_themes_lv;

	private List<ShopMsgInfo> datas = null;

	private MsgInfoDBHelper db;

	private boolean isChoice = false;

	private TextView current_content_title, current_content_title_back;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPrefs = this.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPrefs.getString(
                Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
        sharedPrefs.getString(
                Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");

        Intent intent = getIntent();
        String notificationTitle = intent
                .getStringExtra(Constants.NOTIFICATION_TITLE);
        String notificationMessage = intent
                .getStringExtra(Constants.NOTIFICATION_MESSAGE);
        String packetId = intent
				.getStringExtra(Constants.PACKET_ID);
        
        Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
        Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
        
    	//发送回执消息
		NotificationReplay notificationReplay=new NotificationReplay(this.getApplicationContext());
		notificationReplay.replayNotification();
		
		setContentView(R.layout.shop_list);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initView();
	}

	private void initView() {
		current_content_title_back = (TextView) findViewById(R.id.current_content_title_back);
		current_content_title_back.setOnClickListener(new SysBackTitleListener());
		current_content_title = (TextView) findViewById(R.id.current_content_title);
		current_content_title.setText("消息中心");
		
		
		shop_themes_lv = (ListView) findViewById(R.id.shop_themes_lv);
		shop_themes_lv.setOnItemClickListener(new InfoListItemListener());
//		shop_themes_lv
//				.setOnItemLongClickListener(new InfoListItemLongListener());

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					db = new MsgInfoDBHelper(ShopList.this);
					datas = db.findShopList();
					if (datas != null && datas.size() > 0) {
						handler.sendMessage(handler.obtainMessage(LOAD_SUCCESS,
								datas));
					} else {
						handler.sendMessage(handler.obtainMessage(LOAD_ERROR,
								datas));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	
	private final class SysBackTitleListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}

	}

	private final class InfoListItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (isChoice) {
				// CommonAdapter.isCheck(position);
//				v = (LinearLayout) findViewById(R.layout.info_list_item);
//				layout = (LinearLayout) v.findViewById(R.id.LinearLayout1);
//				select = (CheckBox) layout.findViewById(R.id.select);
//				selectId = new ArrayList<ShopMsgInfo>();
//				if (select.isChecked()) {
//					select.setChecked(false);
//					selectId.remove(datas.get(position));
//				} else {
//					select.setChecked(true);
//					selectId.add(datas.get(position));
//				}
//				Toast.makeText(ShopList.this, "��ѡ����" + selectId.size() + "��",
//						Toast.LENGTH_SHORT).show();
//				Toast.makeText(ShopList.this, "longClick", Toast.LENGTH_LONG).show();

			} else {
				if (datas != null) {
					Intent intent = new Intent(ShopList.this, InfoList.class);
					Bundle bundle = new Bundle();
					bundle.putString(ShopMsgInfo.TableConst.SHOP_NAME, datas
							.get(position).getShopName());
					intent.putExtras(bundle);
					startActivity(intent);
					// ShopList.this.finish();
				}
			}
		}
	}

	private final class InfoListItemLongListener implements
			OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			isChoice = true;
			shop_themes_lv.setAdapter(new CommonAdapter(ShopList.this, datas,
					R.layout.info_list_item, FLAG));
			return false;
		}

	}

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUCCESS:

				shop_themes_lv.setAdapter(new CommonAdapter(ShopList.this,
						datas, R.layout.info_list_item, FLAG));

				break;

			case LOAD_ERROR:

				Toast.makeText(ShopList.this, "暂无消息 ...", Toast.LENGTH_LONG)
						.show();
				break;
			}
		};
	};
}
