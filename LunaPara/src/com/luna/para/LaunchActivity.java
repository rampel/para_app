package com.luna.para;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.luna.adapter.BaseActivity;

public class LaunchActivity extends BaseActivity implements OnClickListener {

	private Button btnLogin;
	private Button btnSignup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.launch_activity);
		init();
		getActionBar().hide();
		super.onCreate(savedInstanceState);
	}

	private void init() {
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignup = (Button) findViewById(R.id.btnSignup);
		btnLogin.setOnClickListener(this);
		btnSignup.setOnClickListener(this);
	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			startActivity(new Intent(this, LoginActivity.class));
			break;

		case R.id.btnSignup:
			startActivity(new Intent(this, RegisterStep1Activity.class));
			break;

		default:
			break;
		}

	}

}
