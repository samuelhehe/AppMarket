package com.samuel.downloader.infocenter.app;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samuel.downloader.app.R;
import com.samuel.downloader.infocenter.adatper.CommonAdapter;
import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.infocenter.dao.MsgInfoDBHelper;

public class InfoList extends Activity {

	private static final int LOAD_ERROR = 0;

	private static final int LOAD_SUCCESS = 1;

	private static final int FLAG = 1;

	private ListView info_themes_lv;

	private TextView current_content_title,current_content_title_back;

	private List<ShopMsgInfo> datas = null;

	private MsgInfoDBHelper db;

	private String shopName = null;

	private int DELETE_FLAG = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_list);

		Intent intent = getIntent();
		shopName = intent.getStringExtra(ShopMsgInfo.TableConst.SHOP_NAME);

		initView();
	}

	private void initView() {
		current_content_title = (TextView) findViewById(R.id.current_content_title);
		current_content_title_back = (TextView) findViewById(R.id.current_content_title_back);
		
		info_themes_lv = (ListView) findViewById(R.id.info_themes_lv);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					db = new MsgInfoDBHelper(InfoList.this);

					datas = db.findShopListBySName(shopName);

					if (datas != null && datas.size() > 0) {
						handler.sendMessage(handler.obtainMessage(LOAD_SUCCESS,
								datas));
					} else {
						if (DELETE_FLAG == 0) {
							handler.sendMessage(handler.obtainMessage(
									LOAD_ERROR, datas));
						} else if (DELETE_FLAG == 1) {
							handler.sendMessage(handler.obtainMessage(
									LOAD_SUCCESS, datas));
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		current_content_title.setText(shopName);
		current_content_title_back.setOnClickListener(new SysBackTitleListener());
		info_themes_lv.setOnItemClickListener(new InfoDetailsItemListener());
		info_themes_lv
				.setOnItemLongClickListener(new InfoDetailsLongListener());
	}

	private final class InfoDetailsItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (datas != null) {
				ShopMsgInfo shopMsgInfo = datas.get(position);

				Intent intent = new Intent(InfoList.this, InfoDetails.class);
				Bundle bundle = new Bundle();

				bundle.putString(ShopMsgInfo.TableConst.SHOP_NAME,
						shopMsgInfo.getShopName());
				bundle.putString(ShopMsgInfo.TableConst.MSG_THEME,
						shopMsgInfo.getMsgTheme());
				bundle.putString(ShopMsgInfo.TableConst.RECEIVE_DATE,
						shopMsgInfo.getReceiveDate());
				bundle.putString(ShopMsgInfo.TableConst.MSG_CONTENT,
						shopMsgInfo.getMsgContent());
				intent.putExtras(bundle);

				startActivity(intent);

				// InfoList.this.finish();
			}
		}

	}

	private final class InfoDetailsLongListener implements
			OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			ShopMsgInfo shopMsgInfo = datas.get(position);
			deleteMsg(shopMsgInfo);
			return true;
		}

		public void deleteMsg(final ShopMsgInfo shopMsgInfo) {
			new AlertDialog.Builder(InfoList.this)
					.setTitle("删除")
					.setMessage("确定删除" + shopMsgInfo.getMsgTheme() + "信息?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									MsgInfoDBHelper db = new MsgInfoDBHelper(
											InfoList.this);
									long i = db.deleteByMsgTheme(shopMsgInfo);
									if (i != 0) {
										DELETE_FLAG = 1;
										initView();
										Toast.makeText(InfoList.this, "删除成功",
												Toast.LENGTH_SHORT).show();
									}
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}

	}

	private final class SysBackTitleListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Intent intent = new Intent(InfoList.this, ShopList.class);
			// startActivity(intent);
			InfoList.this.finish();
		}

	}

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUCCESS:

				info_themes_lv.setAdapter(new CommonAdapter(InfoList.this,
						datas, R.layout.info_list_item, FLAG));

				break;

			case LOAD_ERROR:

				Toast.makeText(InfoList.this, "暂无信息", Toast.LENGTH_LONG)
						.show();

				break;
			}
		};
	};

}
