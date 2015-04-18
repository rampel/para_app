package com.luna.adapter;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.luna.entity.Category;
import com.luna.para.R;

public class CategoryAdapter extends ArrayAdapter<Category> {

	private Context ctx;
	private LayoutInflater mInflater;
	private List<Category> list;
	public static int idCheck;

	public CategoryAdapter(Context context, List<Category> objects) {
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
			convertView = mInflater.inflate(R.layout.category_row, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cbCheck);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(list.get(position).getCategoryName());
		if (idCheck == list.get(position).getId()) {
			holder.cbCheck.setChecked(true);
		} else {
			holder.cbCheck.setChecked(false);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;
		CheckBox cbCheck;
	}

}
