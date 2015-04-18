package com.luna.entity;

import java.io.Serializable;

public class Show implements Serializable{

	private String id;
	private String title;
	private String imageUrl;
	private String time;
	private String showDays;

	public String getShowDays() {
		return showDays;
	}

	public void setShowDays(String showDays) {
		this.showDays = showDays;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
