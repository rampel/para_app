package com.luna.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luna.base.Prefs;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class NavigationDrawerAdapter extends ArrayAdapter<String> {

	private String[] list;
	private Context ctx;
	private LayoutInflater mInflater;

	public NavigationDrawerAdapter(Context context, String[] strings) {
		super(context, 0);
		this.ctx = context;
		this.list = strings;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.navigation_drawer_row,
					null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
			holder.llSeparator = (LinearLayout) convertView
					.findViewById(R.id.llseperator);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(list[position]);
		switch (position) {
		case 0:
			holder.ivIcon.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.home_ico));
			holder.llSeparator.setVisibility(View.GONE);
			break;
		case 1:
			holder.ivIcon.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.home_ico));
			holder.llSeparator.setVisibility(View.GONE);
			break;
		case 2:
			holder.ivIcon.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.dj_ico));
			holder.llSeparator.setVisibility(View.GONE);
			break;
		case 3:
			holder.ivIcon.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.about_ico));
			holder.llSeparator.setVisibility(View.GONE);
			break;
		case 4:
			holder.ivIcon.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.logout_ico));
			holder.llSeparator.setVisibility(View.GONE);
			break;
		}
		return convertView;
	}

	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private Context ctx;
		private ViewHolder holder;
		Bitmap bitmap;
		int position;

		public LoadImage(Context ctx, int position, ViewHolder holder) {
			this.ctx = ctx;
			this.holder = holder;
			this.position = position;
		}

		@Override
		protected Void doInBackground(Void... params) {
			bitmap = MainActivity.getBitmapFromMemCache(Prefs.getMyStringPrefs(
					ctx, Prefs.USER_AVATAR));
			if (bitmap == null) {
				try {
					URL url = new URL(Prefs.getMyStringPrefs(ctx,
							Prefs.USER_AVATAR));
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
				MainActivity.addBitmapToMemoryCache(
						Prefs.getMyStringPrefs(ctx, Prefs.USER_AVATAR), bitmap);
				holder.ivIcon.setImageBitmap(bitmap);
			}
			super.onPostExecute(result);
		}

	}

	class ViewHolder {
		TextView tvTitle;
		ImageView ivIcon;
		LinearLayout llSeparator;
		int position;
	}

}