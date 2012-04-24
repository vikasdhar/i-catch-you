package com.esp.spycatch.bean;

public class ImageBean {
	
	public int imageID;
	public String fileName;
	public int doEmail;
	public int doMMS;
	public int doFacebook;
	public long createdDate;
	public String thumbnail;
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getDoEmail() {
		return doEmail;
	}

	public void setDoEmail(int doEmail) {
		this.doEmail = doEmail;
	}

	public int getDoMMS() {
		return doMMS;
	}

	public void setDoMMS(int doMMS) {
		this.doMMS = doMMS;
	}

	public int getDoFacebook() {
		return doFacebook;
	}

	public void setDoFacebook(int doFacebook) {
		this.doFacebook = doFacebook;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	
	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}

	
	
	
}
