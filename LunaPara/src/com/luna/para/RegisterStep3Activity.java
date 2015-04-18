package com.luna.para;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.parse.SaveCallback;

public class RegisterStep3Activity extends BaseActivity implements
		OnClickListener {

	private Button btnNext;
	private EditText etName;
	private EditText etContactNumber;
	private EditText etRelationship;
	private User mUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.register_activity3);
		getActionBar().hide();
		mUser = (User) getIntent().getExtras().getSerializable("user");
		etName = (EditText) findViewById(R.id.etName);
		etContactNumber = (EditText) findViewById(R.id.etContactNumber);
		etRelationship = (EditText) findViewById(R.id.etRelationship);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
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
			if (!etName.getText().toString().trim().isEmpty()
					&& !etContactNumber.getText().toString().trim().isEmpty()
					&& !etRelationship.getText().toString().trim().isEmpty()) {
				mUser.setContactName(etName.getText().toString());
				mUser.setContactNumber(etContactNumber.getText().toString());
				mUser.setContactRelationship(etRelationship.getText()
						.toString());
				showZapLoadingProgressDialog(ctx, "Loading... Please Wait...");

				ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
				query.whereEqualTo("email", mUser.getEmailString());
				showZapLoadingProgressDialog(ctx, "Loading...");
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> object,
							ParseException arg1) {
						dismissZapProgressDialog();
						if (arg1 == null) {
							if (object.size() != 0) {
								toast("Account Already Exist.");
							} else {
								final ParseObject testObject = new ParseObject(
										"Account");
								testObject.put("name", mUser.getName());
								testObject.put("email", mUser.getEmailString());
								testObject.put("password", mUser.getPassword());
								testObject.put("type", mUser.getId_type());
								testObject.put("id_number", mUser.getId_name());
								testObject.put("contactName",
										mUser.getContactName());
								testObject.put("contactNumber",
										mUser.getContactNumber());
								testObject.put("relationship",
										mUser.getContactRelationship());
								testObject.saveInBackground(new SaveCallback() {

									@Override
									public void done(ParseException arg0) {
										dismissZapProgressDialog();
										if (arg0 == null) {
											Prefs.setMyStringPref(ctx,
													Prefs.EMAIL_ADDRESS,
													mUser.getEmailString());
											Intent intent = new Intent(ctx,
													MainActivity.class);
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
													| Intent.FLAG_ACTIVITY_CLEAR_TASK
													| Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(intent);

											ParseObject userObject = testObject;
											User user = new User();
											user.setContactName(userObject
													.getString("contactName"));
											user.setContactNumber(userObject
													.getString("contactNumber"));
											user.setContactRelationship(userObject
													.getString("relationship"));
											user.setEmailString(mUser
													.getEmailString().trim());
											user.setName(userObject
													.getString("name"));
											user.setPassword(userObject
													.getString("password"));
											user.setId_type(userObject
													.getString("type"));
											user.setId_name(userObject
													.getString("id_number"));
											User.setUser(ctx, user);
										} else {
											toastLong("registration failed. please try again later.");
										}
									}
								});
							}
						} else {
							toast("Account Already Exist.");
						}
					}
				});

			} else {
				toastLong("Please fill up all fields");
			}
			break;

		default:
			break;
		}

	}
}
