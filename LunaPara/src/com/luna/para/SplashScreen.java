package com.luna.para;

<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> 6397fbe450a24ec0ed022a85a1511b9d0fcbaa43
import android.os.Bundle;
import android.os.Handler;

import com.luna.adapter.BaseActivity;
import com.luna.para.R;
<<<<<<< HEAD
import com.parse.Parse;
=======
>>>>>>> 6397fbe450a24ec0ed022a85a1511b9d0fcbaa43

public class SplashScreen extends BaseActivity {

	private final int TIME_SPLASH = 2000;

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
<<<<<<< HEAD
		setContentView(R.layout.splash_screen);
		getActionBar().hide();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
=======
		// TODO Auto-generated method stub
		setContentView(R.layout.splash_screen);
		getActionBar().hide();
>>>>>>> 6397fbe450a24ec0ed022a85a1511b9d0fcbaa43
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
<<<<<<< HEAD
				startActivity(new Intent(SplashScreen.this,
						LaunchActivity.class));
			}
		}, TIME_SPLASH);
=======
			}
		}, TIME_SPLASH);

>>>>>>> 6397fbe450a24ec0ed022a85a1511b9d0fcbaa43
		super.onCreate(savedInstanceState);
	}

}
