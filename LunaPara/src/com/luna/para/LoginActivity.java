package com.luna.para;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.luna.adapter.BaseActivity;

public class LoginActivity extends BaseActivity {

	private Button btnLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.login_activity);
		getActionBar().hide();
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ctx, MainActivity.class);
				startActivity(intent);
			}
		});
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub
	}
}
