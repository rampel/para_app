package com.luna.para;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.luna.adapter.BaseActivity;
import com.luna.entity.User;

public class RegisterStep1Activity extends BaseActivity implements
		OnClickListener {

	private Button btnNext;
	private EditText etEmail;
	private EditText etPassword;
	private EditText etName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.register_activity1);
		getActionBar().hide();
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etName = (EditText) findViewById(R.id.etName);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			if (!etEmail.getText().toString().isEmpty()
					&& !etPassword.getText().toString().isEmpty()
					&& !etName.getText().toString().isEmpty()) {
				User mUser = new User();
				mUser.setEmailString(etEmail.getText().toString());
				mUser.setPassword(etPassword.getText().toString());
				mUser.setName(etName.getText().toString());

				Intent intent = new Intent(this, RegisterStep2Activity.class);
				intent.putExtra("user", mUser);
				startActivity(intent);
			} else {
				toastLong("Please fill up all fields");
			}
			break;

		default:
			break;
		}

	}
}
