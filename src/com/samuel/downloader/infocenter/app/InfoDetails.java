package com.samuel.downloader.infocenter.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.samuel.downloader.app.R;
import com.samuel.downloader.infocenter.bean.ShopMsgInfo;
import com.samuel.downloader.utils.TimeUtils;

public class  InfoDetails  extends Activity{

	private TextView current_content_title,  current_content_title_back, msg_theme, receive_date, msg_content;
	
	private String shopName, msgTheme, msgContent, receiveDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_details);
		
		Intent intent = getIntent();
		shopName = intent.getStringExtra(ShopMsgInfo.TableConst.SHOP_NAME);
		msgTheme = intent.getStringExtra(ShopMsgInfo.TableConst.MSG_THEME);
		msgContent = intent.getStringExtra(ShopMsgInfo.TableConst.MSG_CONTENT);
		receiveDate = intent.getStringExtra(ShopMsgInfo.TableConst.RECEIVE_DATE);
		
		initView();
	}
	
	private void initView(){
		current_content_title = (TextView) findViewById(R.id.current_content_title);
		current_content_title_back = (TextView) findViewById(R.id.current_content_title_back);
		msg_theme = (TextView) findViewById(R.id.shop_name);
		receive_date = (TextView) findViewById(R.id.receive_date);
		msg_content = (TextView) findViewById(R.id.theme_content);
		
		current_content_title.setText(shopName);
		msg_theme.setText(msgTheme);
		receive_date.setText(TimeUtils.getTime(Long.valueOf(receiveDate)));
		msg_content.setText(msgContent);
		current_content_title_back.setOnClickListener(new SysBackTitleListener());
	}
	
	private final class SysBackTitleListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Intent intent = new Intent(InfoDetails.this, InfoList.class);
//			startActivity(intent);
			InfoDetails.this.finish();
		}
    	
    }
	
}
