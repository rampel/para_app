package com.luna.fragment;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.luna.base.Prefs;
import com.luna.entity.User;
import com.luna.para.MainActivity;
import com.luna.para.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ProfileFragment extends Fragment implements OnClickListener {

	private Context ctx;
	private EditText etName;
	private EditText etPassword;
	private EditText etEmailAddress;
	private Spinner spIdType;
	private EditText etIdNumber;
	private EditText etContactName;
	private EditText etContactNumber;
	private EditText etContactRelationship;
	private Button btnUpdateButton;

	@Override
	public void onAttach(Activity activity) {
		ctx = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_fragment, container,
				false);
		etName = (EditText) rootView.findViewById(R.id.etName);
		etPassword = (EditText) rootView.findViewById(R.id.etPassword);
		etEmailAddress = (EditText) rootView.findViewById(R.id.etEmailAddress);
		etIdNumber = (EditText) rootView.findViewById(R.id.etIdNumber);
		etContactName = (EditText) rootView.findViewById(R.id.etContactName);
		spIdType = (Spinner) rootView.findViewById(R.id.spType);
		etContactNumber = (EditText) rootView
				.findViewById(R.id.etContactNumber);
		etContactRelationship = (EditText) rootView
				.findViewById(R.id.etContactRelationship);
		User user = User.getUser(ctx);
		etName.setText(user.getName());
		etEmailAddress.setText(user.getEmailString());
		etPassword.setText(user.getPassword());
		etIdNumber.setText(user.getId_name());
		etContactName.setText(user.getContactName());
		etContactNumber.setText(user.getContactNumber());
		etContactRelationship.setText(user.getContactRelationship());
		btnUpdateButton = (Button) rootView.findViewById(R.id.btnUpdate);

		ArrayAdapter<String> dAdapter = new ArrayAdapter<String>(ctx,
				R.layout.text_view_spinner, getResources().getStringArray(
						R.array.id_type));
		dAdapter.setDropDownViewResource(R.layout.text_view_dropdown_spinner);
		spIdType.setAdapter(dAdapter);

		for (int i = 0; i < getResources().getStringArray(R.array.id_type).length; i++) {
			if (getResources().getStringArray(R.array.id_type)[i].equals(user
					.getId_type())) {
				spIdType.setSelection(i);
			}
		}

		btnUpdateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
				query.whereEqualTo("email",
						Prefs.getMyStringPrefs(ctx, Prefs.EMAIL_ADDRESS));
				((MainActivity) ctx).showZapLoadingProgressDialog(ctx,
						"Loading...");
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> object,
							ParseException arg1) {
						if (arg1 == null) {
							if (object.size() == 0) {
								((MainActivity) ctx).toast("Invalid Account");
							} else {
								final ParseObject userObject = object.get(0);
								userObject.put("name", etName.getText()
										.toString().trim());
								userObject.put("email", etEmailAddress
										.getText().toString().trim());
								userObject.put("password", etPassword.getText()
										.toString().trim());
								userObject.put("id_number", etIdNumber
										.getText().toString().trim());
								userObject.put("type", spIdType
										.getSelectedItem().toString().trim());
								userObject.put("contactName", etContactName
										.getText().toString().trim());
								userObject.put("contactNumber", etContactNumber
										.getText().toString().trim());
								userObject.put("relationship",
										etContactRelationship.getText()
												.toString().trim());
								userObject.saveInBackground(new SaveCallback() {

									@Override
									public void done(ParseException arg0) {
										User user = new User();
										user.setContactName(userObject
												.getString("contactName"));
										user.setContactNumber(userObject
												.getString("contactNumber"));
										user.setContactRelationship(userObject
												.getString("relationship"));
										user.setEmailString(userObject
												.getString("email"));
										user.setName(userObject
												.getString("name"));
										user.setPassword(userObject
												.getString("password"));
										user.setId_type(userObject
												.getString("type"));
										user.setId_name(userObject
												.getString("id_number"));
										User.setUser(ctx, user);
										((MainActivity) ctx)
												.dismissZapProgressDialog();
										((MainActivity) ctx)
												.toast("Successfully updated the profile");
									}
								});
							}
						} else {
							((MainActivity) ctx).toast("Invalid Account");
						}
					}
				});
			}
		});
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
