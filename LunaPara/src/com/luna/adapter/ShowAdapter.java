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

import com.luna.entity.Show;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class ShowAdapter extends ArrayAdapter<Show> {

	private Context ctx;
	private List<Show> list;
	private LayoutInflater mInflater;
	private boolean isDialog;

	public ShowAdapter(Context context, List<Show> list, boolean isDialog) {
		super(context, 0, list);
		this.isDialog = isDialog;
		this.ctx = context;
		this.list = list;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			if (isDialog) {
				convertView = mInflater.inflate(R.layout.show_row_dialog, null);
			} else {
				convertView = mInflater.inflate(R.layout.show_row, null);
			}
			holder.tvShow = (TextView) convertView.findViewById(R.id.tvShow);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTimes);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

			holder.ivMonday = (ImageView) convertView
					.findViewById(R.id.ivMonday);
			holder.ivTuesday = (ImageView) convertView
					.findViewById(R.id.ivTuesday);
			holder.ivWednesday = (ImageView) convertView
					.findViewById(R.id.ivWednesday);
			holder.ivThursday = (ImageView) convertView
					.findViewById(R.id.ivThursday);
			holder.ivFriday = (ImageView) convertView
					.findViewById(R.id.ivFriday);
			holder.ivSaturday = (ImageView) convertView
					.findViewById(R.id.ivSaturday);
			holder.ivSunday = (ImageView) convertView
					.findViewById(R.id.ivSunday);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).getShowDays().contains("1")) {
			holder.ivMonday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.m_red));
		} else {
			holder.ivMonday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.m_grey));
		}

		if (list.get(position).getShowDays().contains("2")) {
			holder.ivTuesday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.t_red));
		} else {
			holder.ivTuesday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.t_grey));
		}

		if (list.get(position).getShowDays().contains("3")) {
			holder.ivWednesday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.w_red));
		} else {
			holder.ivWednesday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.w_grey));
		}
		if (list.get(position).getShowDays().contains("4")) {
			holder.ivThursday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.th_red));
		} else {
			holder.ivThursday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.th_grey));
		}
		if (list.get(position).getShowDays().contains("5")) {
			holder.ivFriday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.f_red));
		} else {
			holder.ivFriday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.f_grey));
		}
		if (list.get(position).getShowDays().contains("6")) {
			holder.ivSaturday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.sa_red));
		} else {
			holder.ivSaturday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.sa_grey));
		}
		if (list.get(position).getShowDays().contains("0")) {
			holder.ivSunday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.s_red));
		} else {
			holder.ivSunday.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.s_grey));
		}

		holder.tvShow.setText(list.get(position).getTitle());
		holder.tvTime.setText(list.get(position).getTime() + "");
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
		List<Show> list;
		Bitmap bitmap;
		int position;

		public LoadImage(Context ctx, int position, ViewHolder holder,
				List<Show> list) {
			this.ctx = ctx;
			this.holder = holder;
			this.list = list;
			this.position = position;
		}

		@Override
		protected Void doInBackground(Void... params) {
			bitmap = MainActivity.getBitmapFromMemCache(list.get(position)
					.getImageUrl());
			if (bitmap == null) {
				try {
					URL url = new URL(list.get(position).getImageUrl());
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
						.getImageUrl(), bitmap);
				holder.ivImage.setImageBitmap(bitmap);
			}
			super.onPostExecute(result);
		}

	}

	class ViewHolder {
		TextView tvShow;
		ImageView ivImage;
		TextView tvTime;
		int position;

		ImageView ivMonday;
		ImageView ivTuesday;
		ImageView ivWednesday;
		ImageView ivThursday;
		ImageView ivFriday;
		ImageView ivSaturday;
		ImageView ivSunday;
	}
}