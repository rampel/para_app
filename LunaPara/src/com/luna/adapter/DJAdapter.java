package com.luna.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luna.adapter.NewsAdapter.ViewHolder;
import com.luna.entity.Dj;
import com.luna.entity.News;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class DJAdapter extends ArrayAdapter<Dj> {

	private Context ctx;
	private List<Dj> list;
	private LayoutInflater mInflater;

	public DJAdapter(Context context, List<Dj> list) {
		super(context, 0, list);
		this.ctx = context;
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.dj_row, null);
			holder.tvDjName = (TextView) convertView
					.findViewById(R.id.tvDjName);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvDjName.setText(list.get(position).getName());
		holder.position = position;
		new LoadImage(ctx, position, holder, list).executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR, ((Void) null));

		return convertView;
	}

	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private Context ctx;
		private ViewHolder holder;
		List<Dj> list;
		Bitmap bitmap;
		int position;

		public LoadImage(Context ctx, int position, ViewHolder holder,
				List<Dj> list) {
			this.ctx = ctx;
			this.holder = holder;
			this.list = list;
			this.position = position;
		}

		@Override
		protected Void doInBackground(Void... params) {
			bitmap = MainActivity.getBitmapFromMemCache(list.get(position)
					.getImageURL());
			if (bitmap == null) {
				try {
					URL url = new URL(list.get(position).getImageURL());
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					bitmap = BitmapFactory.decodeStream(input);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (bitmap != null && position == holder.position) {
				MainActivity.addBitmapToMemoryCache(list.get(position)
						.getImageURL(), bitmap);
				holder.ivImage.setImageBitmap(bitmap);
			}
			super.onPostExecute(result);
		}

	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		// TODO Auto-generated method stub
		int targetWidth = 50;
		int targetHeight = 50;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	class ViewHolder {
		TextView tvDjName;
		ImageView ivImage;
		int position;
	}

}
