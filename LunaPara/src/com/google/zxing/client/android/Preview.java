package com.google.zxing.client.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class Preview extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "Preview";
	static Bitmap finalphoto;
	SurfaceHolder mHolder;
	public Camera camera;

	@SuppressWarnings("deprecation")
	Preview(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open(findFirstFrontFacingCamera());
		try {
			camera.setPreviewDisplay(holder);
			camera.setPreviewCallback(new PreviewCallback() {

				public void onPreviewFrame(byte[] data, Camera camera) {
					Camera.Parameters params = camera.getParameters();
					int w = params.getPreviewSize().width;
					int h = params.getPreviewSize().height;
					int format = params.getPreviewFormat();
					YuvImage image = new YuvImage(data, format, w, h, null);

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Rect area = new Rect(0, 0, w, h);
					image.compressToJpeg(area, 50, out);
					Bitmap bm = BitmapFactory.decodeByteArray(
							out.toByteArray(), 0, out.size());
					finalphoto = bm;
					
					
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String Photo(){
		String picture = "";
		Bitmap bitmapOrg = finalphoto;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] ba = bao.toByteArray();
		picture = Base64.encodeToString(ba, 0);
		return picture;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

        if(camera!=null){
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }

    }

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Camera.Parameters parameters = camera.getParameters();
		// parameters.setPreviewSize(w, h);
		camera.setParameters(parameters);
		camera.startPreview();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Paint p = new Paint(Color.RED);
		Log.d(TAG, "draw");
		canvas.drawText("PREVIEW", canvas.getWidth() / 2,
				canvas.getHeight() / 2, p);
	}

	private int findFirstFrontFacingCamera() {
		int cameraId = -1;
		// search for the first front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}
}