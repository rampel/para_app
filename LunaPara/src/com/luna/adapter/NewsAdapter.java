package com.luna.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luna.entity.News;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class NewsAdapter extends ArrayAdapter<News> {

	private Context ctx;
	private LayoutInflater mInflater;
	private List<News> list;

	public NewsAdapter(Context context, List<News> objects) {
		super(context, 0, objects);
		this.ctx = context;
		this.list = objects;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.news_adapter, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvAuthor = (TextView) convertView
					.findViewById(R.id.tvAuthor);
			holder.tvCommentCount = (TextView) convertView
					.findViewById(R.id.tvCommentCount);
			holder.tvHeartCount = (TextView) convertView
					.findViewById(R.id.tvHeartCount);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(list.get(position).getTitle());
		holder.tvAuthor.setText("news by " + list.get(position).getAuthor());
		holder.tvCommentCount
				.setText(list.get(position).getCommentCount() + "");
		holder.tvHeartCount.setText(list.get(position).getHeartCount() + "");
		holder.position = position;
		holder.ivImage.setImageDrawable(ctx.getResources().getDrawable(
				R.drawable.placeholder));
		new LoadImage(ctx, position, holder, list).executeOnExecutor( 
				AsyncTask.THREAD_POOL_EXECUTOR, ((Void) null));
		return convertView;
	}

	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private Context ctx;
		private ViewHolder holder;
		List<News> list;
		Bitmap bitmap;
		int position;

		public LoadImage(Context ctx, int position, ViewHolder holder,
				List<News> list) {
			this.ctx = ctx;
			this.holder = holder;
			this.list = list;
			this.position = position;
		}

		@Override
		protected Void doInBackground(Void... params) {
			bitmap = MainActivity.getBitmapFromMemCache(list.get(position)
					.getThumbnail());
			if (bitmap == null) {
				try {
					URL url = new URL(list.get(position).getThumbnail());
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
						.getThumbnail(), bitmap);
				holder.ivImage.setImageBitmap(bitmap);
			}
			super.onPostExecute(result);
		}

	}

	class ViewHolder {
		TextView tvTitle;
		ImageView ivImage;
		TextView tvAuthor;
		TextView tvCommentCount;
		TextView tvHeartCount;
		int position;
	}

}
