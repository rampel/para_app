package com.luna.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luna.entity.Show;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context ctx;
	private List<String> _listDataHeader; // header titles
	private HashMap<String, List<Show>> _listDataChild;
	private LayoutInflater mInflater;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<Show>> listChildData) {
		this.ctx = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.show_row, null);
			holder.tvShow = (TextView) convertView.findViewById(R.id.tvShow);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTimes);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvShow.setText(_listDataChild
				.get(_listDataHeader.get(groupPosition)).get(childPosition)
				.getTitle());
		holder.tvTime.setText(_listDataChild
				.get(_listDataHeader.get(groupPosition)).get(childPosition)
				.getTime()
				+ "");
		holder.position = childPosition;
		holder.ivImage.setImageDrawable(ctx.getResources().getDrawable(
				R.drawable.appicon));
		new LoadImage(ctx, childPosition, holder,
				_listDataChild.get(groupPosition)).executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR, ((Void) null));
		return convertView;
	}

	class ViewHolder {
		TextView tvShow;
		ImageView ivImage;
		TextView tvTime;
		int position;
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

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
		Log.d("COUNT CHILDRENNN", count + " <<<<<");
		Log.d("COUNT CHILDRENNN", count + " <<<<<");
		return count;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
