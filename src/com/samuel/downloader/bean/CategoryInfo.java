package com.samuel.downloader.bean;

public class CategoryInfo {

	/**
	 "appCategory": "閱讀", "categoryId": "2", "createBy": "H2601977"
	 */

	/**
	 * 分类
	 */
	private String appCategory;

	/**
	 * 分类图标
	 */
	private String categoryIcon;

	/**
	 * 分类Id 
	 */
	private String categoryId;

	/**
	 * 分类创建者
	 */
	private String createBy;
	
	public static class TAG{
		public static final String TAG_APPCATEGORY = "appCategory";
		
		public static final String TAG_CATEGORYICON = "categoryIcon";
		
		public static final String TAG_CATEGORYID = "categoryId";
		
		public static final String TAG_CREATEBY = "createBy";
	}
	
	public CategoryInfo(String appCategory, String categoryId, String createBy,
			String categoryIcon) {
		this.appCategory = appCategory;
		this.categoryId = categoryId;
		this.createBy = createBy;
		this.categoryIcon = categoryIcon;
	}

	public CategoryInfo() {
	}

	public String getAppCategory() {
		return appCategory;
	}

	public String getCategoryIcon() {
		return categoryIcon;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}

	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String toString() {
		return "CategoryInfo [appCategory=" + appCategory + ", categoryId="
				+ categoryId + ", createBy=" + createBy + ", categoryIcon="
				+ categoryIcon + "]";
	}

}
