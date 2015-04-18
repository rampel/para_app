package com.luna.base;

import com.parse.Parse;

import android.app.Application;

public class LunaApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "8Pyt4wajnJtAiUtMz3Ro8z8XNDZsVMxyallrDUNN",
				"hdkpfhk3PDWeIqkDgWzhfapznNStl2lDfh3oqrWE");
		super.onCreate();
	}

}
