package com.luna.entity;

import java.io.Serializable;

import android.content.Context;

import com.luna.base.Prefs;

public class User implements Serializable {

	private String name;
	private String password;
	private String emailString;
	private String id_type;
	private String id_name;
	private String contactNumber;
	private String contactName;
	private String contactRelationship;

	public String getId_name() {
		return id_name;
	}

	public void setId_name(String id_name) {
		this.id_name = id_name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactRelationship() {
		return contactRelationship;
	}

	public void setContactRelationship(String contactRelationship) {
		this.contactRelationship = contactRelationship;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailString() {
		return emailString;
	}

	public void setEmailString(String emailString) {
		this.emailString = emailString;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public static void setUser(Context ctx, User user) {
		Prefs.setMyStringPref(ctx, Prefs.EMAIL_ADDRESS, user.getEmailString());
		Prefs.setMyStringPref(ctx, Prefs.NAME, user.getName());
		Prefs.setMyStringPref(ctx, Prefs.PASSWORD, user.getPassword());
		Prefs.setMyStringPref(ctx, Prefs.ID_TYPE, user.getId_type());
		Prefs.setMyStringPref(ctx, Prefs.ID_NAME, user.getId_name());
		Prefs.setMyStringPref(ctx, Prefs.CONTACTNAME, user.getContactName());
		Prefs.setMyStringPref(ctx, Prefs.CONTACTNUMBER, user.getContactNumber());
		Prefs.setMyStringPref(ctx, Prefs.CONTACTRELATIONSHIP,
				user.getContactRelationship());
	}

	public static User getUser(Context ctx) {
		User user = new User();
		user.setEmailString(Prefs.getMyStringPrefs(ctx, Prefs.EMAIL_ADDRESS));
		user.setPassword(Prefs.getMyStringPrefs(ctx, Prefs.PASSWORD));
		user.setContactNumber(Prefs.getMyStringPrefs(ctx, Prefs.CONTACTNUMBER));
		user.setContactName(Prefs.getMyStringPrefs(ctx, Prefs.CONTACTNAME));
		user.setContactRelationship(Prefs.getMyStringPrefs(ctx,
				Prefs.CONTACTRELATIONSHIP));
		user.setId_type(Prefs.getMyStringPrefs(ctx, Prefs.ID_TYPE));
		user.setId_name(Prefs.getMyStringPrefs(ctx, Prefs.ID_NAME));
		user.setName(Prefs.getMyStringPrefs(ctx, Prefs.NAME));
		return user;
	}

}
