package com.esp.spycatch.bean;

public class CheckBoxItemBean{
	
	private String strTitle;
	private String strSubTitle;
	private boolean isCheckBox;
	public String getStrTitle() {
		return strTitle;
	}
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
	public String getStrSubTitle() {
		return strSubTitle;
	}
	public void setStrSubTitle(String strSubTitle) {
		this.strSubTitle = strSubTitle;
	}
	
	public void setIsCheckBox(boolean isCheckBox){
		this.isCheckBox = isCheckBox;
	}
	public boolean isCheckbox() {
		return this.isCheckBox;
	}
	 
	
}
