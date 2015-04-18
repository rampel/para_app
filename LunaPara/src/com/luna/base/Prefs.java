package com.luna.base;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

	public static String EMAIL_ADDRESS = "EMAIL_ADDRESS";
	public static String PASSWORD = "PASSWORD";
	public static String NAME = "NAME";
	public static String ID_TYPE = "ID_TYPE";
	public static String ID_NAME = "ID_NAME";
	public static String CONTACTNUMBER = "CONTACTNUMBER";
	public static String CONTACTNAME = "CONTACTNAME";
	public static String CONTACTRELATIONSHIP = "CONTACTRELATIONSHIP";
	public static String DATA_ARRAY_STATUS = "DATA_ARRAY_STATUS";

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

	public static void removeAllPref(Context context) {
		getPrefs(context).edit().clear().commit();
	}

}
