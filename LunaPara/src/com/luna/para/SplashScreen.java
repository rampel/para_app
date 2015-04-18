package com.luna.para;

import android.os.Bundle;
import android.os.Handler;

import com.luna.adapter.BaseActivity;
import com.luna.para.R;

public class SplashScreen extends BaseActivity {

	private final int TIME_SPLASH = 2000;

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.splash_screen);
		getActionBar().hide();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
			}
		}, TIME_SPLASH);

		super.onCreate(savedInstanceState);
	}

}
