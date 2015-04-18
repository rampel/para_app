package com.luna.entity;

import java.io.Serializable;

public class Dj implements Serializable {

	private String id;
	private String name;
	private String imageURL;
	private String bio_data;

	public String getBio_data() {
		return bio_data;
	}

	public void setBio_data(String bio_data) {
		this.bio_data = bio_data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

}
