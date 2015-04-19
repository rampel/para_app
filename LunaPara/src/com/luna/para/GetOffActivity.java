package com.luna.para;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.luna.adapter.BaseActivity;
import com.luna.base.Prefs;
import com.luna.entity.User;
import com.parse.ParseObject;

public class GetOffActivity extends BaseActivity {
	private ImageView ivStop;
	String[] dataArray;

	TextView tvDriverName;
	TextView tvPlateNumber;
	TextView tvMakeTextView;
	TextView tvModelTextView;
	TextView tvTaxiName;
	Button btnReport;
	User mUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_get_off);
		super.onCreate(savedInstanceState);
		btnReport = (Button) findViewById(R.id.btnReportAbuse);
		tvTaxiName = (TextView) findViewById(R.id.tvTaxiName);
		tvDriverName = (TextView) findViewById(R.id.tvDriverName);
		tvMakeTextView = (TextView) findViewById(R.id.tvModelMake);
		tvPlateNumber = (TextView) findViewById(R.id.tvPlateNumber);
		tvModelTextView = (TextView) findViewById(R.id.tvModelName);
		ivStop = (ImageView) findViewById(R.id.ivStop);
		ivStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					dataArray = Prefs.getMyStringPrefs(ctx,
							Prefs.DATA_ARRAY_STATUS).split("<~>");
					sendReportToPnp(dataArray);
				} catch (Exception e) {
					e.printStackTrace();
				}
				showDialogBox("Did you get off safely?");
			}
		});
		try {
			dataArray = Prefs.getMyStringPrefs(ctx, Prefs.DATA_ARRAY_STATUS)
					.split("<~>");
			tvDriverName.setText("Driver Name: " + dataArray[5]);
			tvMakeTextView.setText("Make: " + dataArray[1]);
			tvPlateNumber.setText("Plate Number: " + dataArray[4]);
			tvModelTextView.setText("Model: " + dataArray[2]);
			tvTaxiName.setText("Taxi Name: " + dataArray[6]);

			mUser = User.getUser(ctx);

			btnReport.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SMSActivity.sendSMS(
							ctx,
							9,
							" PARA ALERT!!! \n\n Looks like "
									+ mUser.getName()
									+ " is in danger! "
									+ "Time:"
									+ getCurrentDate()
									+ "\n\n"
									+ "Plate No:"
									+ dataArray[4]
									+ "\n\n"
									+ (dataArray[6].length() > 0 ? "Taxi Name: "
											+ dataArray[6]
											: ""));
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			btnReport.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SMSActivity.sendSMS(ctx, 9,
							" PARA ALERT!!! \n\n Looks like " + mUser.getName()
									+ " is in danger! " + "Time:"
									+ getCurrentDate());
				}
			});
			e.printStackTrace();
		}
		// TODO PREFERENCE THIS
	}

	public String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy HH:ss:mm");
		return sdf.format(cal.getTime());
	}

	private void sendReportToPnp(String[] dataArray) {
		ParseObject object = new ParseObject("PNP_database");
		object.put("vin_number", dataArray[0]);
		object.put("vehicle_make", dataArray[1]);
		object.put("vehicle_model", dataArray[2]);
		object.put("vehicle_year", dataArray[3]);
		object.put("plate_number", dataArray[4]);
		object.put("driver_name", dataArray[5]);
		object.put("taxi_name", dataArray[6]);
		object.put("operator_contact_number", dataArray[7]);
		object.put("operator_rating", dataArray[8]);
		User user = User.getUser(this);
		object.put("reporter_name", user.getName());
		object.put("reporter_email", user.getEmailString());
		object.put("reporter_identification", user.getId_name());
		object.put("reporter_identification_type", user.getId_type());
		object.put("status", "OUT");
		object.put("latitude", "14.5412181");
		object.put("longitude", "121.019488700000010000");
		object.saveInBackground();
	}

	private void showDialogBox(String prompt) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_prompt);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
		Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
		TextView tvPrompt = (TextView) dialog.findViewById(R.id.tvPrompt);
		tvPrompt.setText(prompt);

		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showRating();
				dialog.dismiss();
			}
		});
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reportToPolice("Do you want to sent a report to the police?");
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	private void sendTaxiRating(String rating) {
		ParseObject object = new ParseObject("Taxi_Rating");
		object.put("taxi_plate_number", dataArray[4]);
		object.put("rating", rating);
		object.saveInBackground();
	}

	private void showRating() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_rating);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		Button btnRate = (Button) dialog.findViewById(R.id.btnRate);
		Button btnSkip = (Button) dialog.findViewById(R.id.btnSkip);
		final RatingBar ratingBar = (RatingBar) dialog
				.findViewById(R.id.ratingBar);

		btnRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendTaxiRating(ratingBar.getRating() + "");
				Prefs.setMyBooleanPref(ctx, Prefs.ACTIVE, false);
				Intent intent = new Intent(GetOffActivity.this,
						MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				dialog.dismiss();
			}
		});
		btnSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Prefs.setMyBooleanPref(ctx, Prefs.ACTIVE, false);
				Intent intent = new Intent(GetOffActivity.this,
						MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	private void reportToPolice(String prompt) {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_prompt);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
		Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
		TextView tvPrompt = (TextView) dialog.findViewById(R.id.tvPrompt);
		tvPrompt.setText(prompt);

		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub

	}

}
