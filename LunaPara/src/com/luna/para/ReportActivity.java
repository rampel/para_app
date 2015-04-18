package com.luna.para;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.luna.adapter.BaseActivity;

public class ReportActivity extends BaseActivity implements OnClickListener {
	private static final int GALLERY_REQUESTCODE = 0x200;
	private static final int TAKE_PHOTO_REQUESTCODE = 0x300;
	private static final int TAKE_VIDEO_REQUESTCODE = 0x400;

	private ImageView ivTakePhoto;
	private ImageView ivTakeVideo;
	private ImageView ivGallery;
	private Button btnSend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_report);
		getActionBar().hide();
		super.onCreate(savedInstanceState);
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
			}
		}

		if (requestCode == TAKE_VIDEO_REQUESTCODE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageURI = data.getData();
			}
		}
		if (requestCode == GALLERY_REQUESTCODE) {
			if (resultCode == Activity.RESULT_OK) {

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
