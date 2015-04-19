package com.luna.para;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.luna.adapter.BaseActivity;
import com.luna.entity.User;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

public class ReportActivity extends BaseActivity implements OnClickListener {
	private static final int GALLERY_REQUESTCODE = 0x200;
	private static final int TAKE_PHOTO_REQUESTCODE = 0x300;
	private static final int TAKE_VIDEO_REQUESTCODE = 0x400;

	private ImageView ivTakePhoto;
	private ImageView ivTakeVideo;
	private ImageView ivGallery;
	private Button btnSend;
	private EditText etDescription;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_report);
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		etDescription = (EditText) findViewById(R.id.etDescription);
		ivTakePhoto = (ImageView) findViewById(R.id.ivTakePhoto);
		ivTakePhoto.setOnClickListener(this);
		ivTakeVideo = (ImageView) findViewById(R.id.ivTakeVideo);
		ivTakeVideo.setOnClickListener(this);
		ivGallery = (ImageView) findViewById(R.id.ivGallery);
		ivGallery.setOnClickListener(this);
		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == TAKE_PHOTO_REQUESTCODE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageURI = data.getData();
				InputStream iStream = null;
				try {
					iStream = getContentResolver().openInputStream(
							selectedImageURI);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					byte[] inputData = getBytes(iStream);
					saveIncident(inputData, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (requestCode == TAKE_VIDEO_REQUESTCODE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageURI = data.getData();
				InputStream iStream = null;
				try {
					iStream = getContentResolver().openInputStream(
							selectedImageURI);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					byte[] inputData = getBytes(iStream);
					saveIncident(inputData, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (requestCode == GALLERY_REQUESTCODE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageURI = data.getData();
				InputStream iStream = null;
				try {
					iStream = getContentResolver().openInputStream(
							selectedImageURI);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					byte[] inputData = getBytes(iStream);
					saveIncident(inputData, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}

	private void saveIncident(byte[] data, boolean isImage) {
		showZapLoadingProgressDialog(ctx, "Saving File...");
		ParseFile file = null;
		if (isImage) {
			file = new ParseFile("image.jpg", data);
		} else {
			file = new ParseFile("video.mp4", data);
		}

		final ParseFile f = file;

		file.saveInBackground(new SaveCallback() {
			@Override
			public void done(com.parse.ParseException arg0) {
				ParseObject obj = new ParseObject("Report");
				obj.put("file", f);
				obj.put("description", etDescription.getText().toString() + " ");
				obj.put("user_identification_number", User.getUser(ctx)
						.getId_name());
				obj.put("user_identification_type", User.getUser(ctx)
						.getId_type());
				obj.saveInBackground(new SaveCallback() {

					@Override
					public void done(com.parse.ParseException arg0) {
						dismissZapProgressDialog();
						finish();
					}
				});

			}
		}, new ProgressCallback() {
			public void done(Integer percentDone) {
				// Update your progress spinner here. percentDone will be
				// between 0 and 100.
				showZapLoadingProgressDialog(ctx, "Saving File...("
						+ percentDone + "%)");
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivGallery:
			Intent photoPickerIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			photoPickerIntent.setType("image/* video/*");
			startActivityForResult(photoPickerIntent, GALLERY_REQUESTCODE);
			break;
		case R.id.ivTakePhoto:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, TAKE_PHOTO_REQUESTCODE);

			break;
		case R.id.ivTakeVideo:
			Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			if (takeVideoIntent.resolveActivity(ctx.getPackageManager()) != null) {
				startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUESTCODE);
			}
			break;

		default:
			break;
		}
	}
}
