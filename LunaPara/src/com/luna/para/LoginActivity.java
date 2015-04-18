package com.luna.para;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.luna.adapter.BaseActivity;
import com.luna.base.Prefs;
import com.luna.entity.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LoginActivity extends BaseActivity {

	private Button btnLogin;
	private EditText etUserName;
	private EditText etPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.login_activity);
		getActionBar().hide();
		etUserName = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etUserName.getText().toString().trim().isEmpty()
						&& !etPassword.getText().toString().trim().isEmpty()) {
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("Account");
					query.whereEqualTo("email", etUserName.getText().toString());
					query.whereEqualTo("password", etPassword.getText()
							.toString());
					showZapLoadingProgressDialog(ctx, "Loading...");
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> object,
								ParseException arg1) {
							dismissZapProgressDialog();
							if (arg1 == null) {
								if (object.size() == 0) {
									toast("Invalid Account");
								} else {
									ParseObject userObject = object.get(0);
									User user = new User();
									user.setContactName(userObject
											.getString("contactName"));
									user.setContactNumber(userObject
											.getString("contactNumber"));
									user.setContactRelationship(userObject
											.getString("relationship"));
									user.setEmailString(etUserName.getText()
											.toString().trim());
									user.setName(userObject.getString("name"));
									user.setPassword(userObject
											.getString("password"));
									user.setId_type(userObject
											.getString("type"));
									user.setId_name(userObject
											.getString("id_number"));
									User.setUser(ctx, user);
									Intent intent = new Intent(ctx,
											MainActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
											| Intent.FLAG_ACTIVITY_CLEAR_TASK
											| Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);

								}
							} else {
								toast("Invalid Account");
							}
						}
					});
				} else {
					toastLong("Please fill up all fields.");
				}

			}
		});
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub
	}
}
