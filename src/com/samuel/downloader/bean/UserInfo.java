package com.samuel.downloader.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * "username":"常耀耀", "department": " GDSBG-SIDC-SIPD-SMI ", "email":
	 * "yao-yao.chang@mail.foxconn.com", "tel": "579-25540", "userauth ":"1",
	 * "createdata":"2014/01/03", "modifydate":"2014/01/03"
	 * 
	 * 
	 * { "crate_date": "2014-02-21T00:00:00", "modify_date":
	 * "2014-02-21T00:00:00", "nickNmae": "小赵", "telExtension": "579-25547",
	 * "userEmail": "yao.chang@mail.conm", "userPhoneno": "12354678965",
	 * "user_authority": "2", "user_id": "USER000022", "user_name": "ceshi001",
	 * "user_pw": "123" }
	 */

	private String userId;

	private String userName;

	private String department;

	private String email;

	private String createDate;

	private String modifyDate;

	private String tel;

	public static class TAG {

		public static final String TAG_CLASSNAME = "USERINFO";

		public static final String TAG_USERNAME = "user_name";

		public static final String TAG_PASSWORD = "password";

		public static final String TAG_USERID = "user_id";

		public static final String TAG_DEPARTMENT = "DEPARTMENT";

		public static final String TAG_EMAIL = "userEmail";

		public static final String TAG_TEL = "telExtension";

		public static final String TAG_CREATEDATE = "crate_date";

		public static final String TAG_MODIFYDATE = "modify_date";

	}

	public static class TableConst {

		public static final String USERNAME = "USERNAME";

		public static final String DEPARTMENT = "DEPARTMENT";

		public static final String EMAIL = "EMAIL";

		public static final String CREATEDATE = "CREATEDATE";

		public static final String MODIFYDATE = "MODIFYDATE";

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
