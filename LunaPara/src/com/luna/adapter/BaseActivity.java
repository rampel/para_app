package com.luna.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity {

	protected DecimalFormat twoDecimal = new DecimalFormat("0.00");
	public Context ctx = this;

	public static Dialog dialogOk;
	private final String TAG = this.getClass().getSimpleName();

	protected abstract void updateAction();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
	}

	protected ProgressDialog zapProgressDialog;

	public void showZapLoadingProgressDialog(Context context, String message) {
		Log.d(TAG, "showing loading progress dialog");
		if (zapProgressDialog == null) {
			zapProgressDialog = new ProgressDialog(context);
			zapProgressDialog.setMessage(message);
			zapProgressDialog.setCancelable(true);
			zapProgressDialog.setCanceledOnTouchOutside(true);
			zapProgressDialog.show();
		} else {
			zapProgressDialog.setMessage(message);
			zapProgressDialog.setCancelable(true);
			zapProgressDialog.setCanceledOnTouchOutside(true);
			zapProgressDialog.show();
		}
	}

	public void dismissZapProgressDialog() {
		Log.d(TAG, "dismissing progress dialog");
		if (zapProgressDialog != null) {
			zapProgressDialog.dismiss();
		}
	}

	public void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void toastLong(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	static int clearCacheFolder(final File dir, final int numDays) {

		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {

					// first delete subdirectories recursively
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}

					// then delete the files and subdirectories in this dir
					// only empty directories can be deleted, so subdirs have
					// been done first
					if (child.lastModified() < new Date().getTime() - numDays
							* DateUtils.DAY_IN_MILLIS) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return deletedFiles;
	}

	/*
	 * Delete the files older than numDays days from the application cache 0
	 * means all files.
	 */
	public static void clearCache(final Context context, final int numDays) {
		int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
	}

}
