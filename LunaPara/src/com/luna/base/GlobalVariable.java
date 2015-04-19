package com.luna.base;

import android.content.Context;

public class GlobalVariable {

	public static String getHeader(Context ctx) {
		String user = Prefs.getMyStringPrefs(ctx, Prefs.NAME);
		return "PARA! ALERT \n " + user
				+ " take a cab with this following details \n\n";
	}

	public static String getNumber(Context ctx) {
		return Prefs.getMyStringPrefs(ctx, Prefs.CONTACTNUMBER);
	}

}
