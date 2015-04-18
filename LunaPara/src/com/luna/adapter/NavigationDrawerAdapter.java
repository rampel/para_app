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
					R.drawable.about_ico));
			holder.llSeparator.setVisibility(View.GONE);
			break;
		case 3:
			holder.ivIcon.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.logout_ico));
			holder.llSeparator.setVisibility(View.GONE);
			break;
		case 4:

			break;
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;
		ImageView ivIcon;
		LinearLayout llSeparator;
		int position;
	}

}