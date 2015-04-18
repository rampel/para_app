package com.luna.para;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SMSActivity {

	public static Dialog dialog;
	public static final int START = 0;
	public static final int STOP = 1;

	public static void sendSMS(final Context ctx, int parameter,
			final String phoneNumber, final String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(ctx, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(ctx, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		ctx.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(ctx, "SMS sent", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ctx, GetOffActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					ctx.startActivity(intent);
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					showDialogBox(ctx, "SENDING FAILED! RETRY?", phoneNumber,
							message);
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					showDialogBox(ctx, "SENDING FAILED! RETRY?", phoneNumber,
							message);
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					showDialogBox(ctx, "SENDING FAILED! RETRY?", phoneNumber,
							message);
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					showDialogBox(ctx, "SENDING FAILED! RETRY?", phoneNumber,
							message);
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		ctx.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(ctx, "SMS delivered", Toast.LENGTH_SHORT)
							.show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(ctx, "SMS not delivered", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

	public static void showDialogBox(final Context ctx, final String prompt,
			final String pN, final String mesage) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		dialog = new Dialog(ctx);
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
				sendSMS(ctx,0, pN, mesage);

			}
		});
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ctx, MainActivity.class);
				ctx.startActivity(intent);
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setCancelable(false);
	}
}
