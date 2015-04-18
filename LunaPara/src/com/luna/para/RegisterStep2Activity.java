package com.luna.para;

import android.R.anim;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.luna.adapter.BaseActivity;
import com.luna.entity.User;

public class RegisterStep2Activity extends BaseActivity implements
		OnClickListener {

	private Button btnNext;
	private Spinner spType;
	private EditText etIdNumber;
	private User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.register_activity2);
		user = (User) getIntent().getExtras().getSerializable("user");
		getActionBar().hide();
		etIdNumber = (EditText) findViewById(R.id.etIdentification);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		spType = (Spinner) findViewById(R.id.spType);
		spType.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.id_type)));
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
			if (!etIdNumber.getText().toString().isEmpty()) {
				user.setId_type(spType.getSelectedItem().toString());
				user.setId_name(etIdNumber.getText().toString());
				Intent intent = new Intent(ctx, RegisterStep3Activity.class);
				intent.putExtra("user", user);
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
