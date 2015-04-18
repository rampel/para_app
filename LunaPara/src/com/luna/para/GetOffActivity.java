package com.luna.para;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.luna.adapter.BaseActivity;

public class GetOffActivity extends BaseActivity {
	private ImageView ivStop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_get_off);
		super.onCreate(savedInstanceState);
		ivStop = (ImageView) findViewById(R.id.ivStop);
		ivStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialogBox("Did you get off safely?");
			}
		});
		// TODO PREFERENCE THIS
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

		btnRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
