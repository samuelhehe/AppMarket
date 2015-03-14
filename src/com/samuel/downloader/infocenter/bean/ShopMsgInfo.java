package com.samuel.downloader.infocenter.bean;

public class ShopMsgInfo {

	public static class TableConst{
		
		public static String TABLE_NAME = "msginfo";
		
		public static String SHOP_NAME = "shopname";
		
		public static String MSG_THEME = "msgtheme";
		
		public static String RECEIVE_DATE = "receivedate";
		
		public static String MSG_CONTENT = "msgcontent";
	}
	
	private String shopName;
	
	private String msgTheme;
	
	private String receiveDate;
	
	private String msgContent;
	
	

	@Override
	public String toString() {
		return "ShopMsgInfo [shopName=" + shopName + ", msgTheme=" + msgTheme
				+ ", receiveDate=" + receiveDate + ", msgContent=" + msgContent
				+ "]";
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getMsgTheme() {
		return msgTheme;
	}

	public void setMsgTheme(String msgTheme) {
		this.msgTheme = msgTheme;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	
}
