package com.luna.base;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

	public static String CURRENT_SHOWID = "CURRENT_SHOWID";
	public static String PLAYER_URL = "PLAYER_URL";
	public static String AUTO_PLAY = "AUTO_PLAY";
	public static String VOLUME = "VOLUME";
	public static String LOGIN_PAGE = "LOGIN_PAGE";
	public static String REGISTER_PAGE = "REGISTER_PAGE";
	public static String ABOUT_PAGE = "ABOUT_PAGE";
	public static String MYPOST_PAGE = "MYPOST_PAGE";
	public static String CHAT_PAGE = "CHAT_PAGE";
	public static String USER_ID = "USER_ID";
	public static String USER_NAME = "USER_NAME";
	public static String USER_AVATAR = "USER_AVATAR";

	public static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences("myprefs", 0);
	}

	public static String getMyStringPrefs(Context context, String key) {
		return getPrefs(context).getString(key, "");
	}

	public static int getMyIntPref(Context context, String key) {
		return getPrefs(context).getInt(key, 0);
	}

	public static float getMyFloatPref(Context context, String key) {
		return getPrefs(context).getFloat(key, 0f);
	}

	public static boolean getMyBooleanPref(Context context, String key) {
		return getPrefs(context).getBoolean(key, true);
	}

	public static void setMyBooleanPref(Context context, String key,
			boolean value) {
		getPrefs(context).edit().putBoolean(key, value).commit();
	}

	public static void setMyStringPref(Context context, String key, String value) {
		getPrefs(context).edit().putString(key, value).commit();
	}

	public static void setMyIntPref(Context context, String key, int value) {
		getPrefs(context).edit().putInt(key, value).commit();
	}

	public static void setMyFloatPref(Context context, String key, Float value) {
		getPrefs(context).edit().putFloat(key, value).commit();
	}

	public static void removePref(Context context, String key) {
		getPrefs(context).edit().remove(key);
	}

}
