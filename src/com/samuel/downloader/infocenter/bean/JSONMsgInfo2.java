package com.samuel.downloader.infocenter.bean;

public class JSONMsgInfo2 {
	
	/**
	 *{"msgTitle":"测试","msgType":"message",
	 *"user":{
	 *	"userId":"USER0000000000000055","user_name_chi":"常耀","user_name_eng":"shy",
	 *	"providerName":"移動應用整合部","userType":"2","userPw":"ceebbce4c86d5195e5d064d58d6c4af3",
	 *  "userEmail":"yao.chang@mail.foxconn.com","telNum":"12354622110","phoneNum":"18695851046",
	 *  "isDel":"N","apiKey":"b53728145b8eccbe0774f8adbac7a525","note":"我是个好人",
	 *  "createDate":"Jun 23, 2014 4:20:28 PM","modifyDate":"Jun 23, 2014 4:20:28 PM"
	 *     },
	 *"msg":"消息内容"}
	 *  
	 */
	public static class TAG{
		
		/**
		 * msg
		 */
		public static final String msgTitle = "msgTitle";
		public static final String msgType = "msgType";
		public static final String msgContent = "msg";
		/**
		 * msg_type
		 */
		public static final String msg_type_message = "message";
		public static final String msg_type_notifcation = "notifcation";
		
		/**
		 * userinfo 
		 */
		
		public static final String user_user = "user";  
		public static final String user_userEmail = "userEmail";  
		public static final String user_telNum = "telNum"; 
		public static final String user_phoneNum = "phoneNum";
		public static final String user_user_name_chi = "user_name_chi";
		public static final String user_providerName = "providerName";
		public static final String user_note = "note";
		
		
		
	}
	
	

	private String message;
	
	private String time;
	
	private String title;
	
	private String providerName;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
	
}
