package com.luna.para;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.zxing.client.android.CaptureActivity;
import com.luna.adapter.BaseActivity;

public class SplashScreen extends BaseActivity {

	private final int TIME_SPLASH = 2000;

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash_screen);
		getActionBar().hide();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(SplashScreen.this,
						LaunchActivity.class));
			}
		}, TIME_SPLASH);
		super.onCreate(savedInstanceState);
	}

}
