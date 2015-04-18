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
		// TODO Auto-generated method stub
		setContentView(R.layout.splash_screen);
		getActionBar().hide();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this,
						CaptureActivity.class);
				startActivity(intent);
			}
		}, TIME_SPLASH);

		super.onCreate(savedInstanceState);
	}

}
