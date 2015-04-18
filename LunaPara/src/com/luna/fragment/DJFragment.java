package com.luna.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.luna.adapter.DJAdapter;
import com.luna.entity.Dj;
import com.luna.para.DJDetailsActivity;
import com.luna.para.MainActivity;
import com.luna.para.R;
import com.luna.web.ClientManager;

public class DJFragment extends Fragment implements OnRefreshListener {

	private Context ctx;
	private SwipeRefreshLayout swipeLayout;
	private ListView lvList;
	private List<Dj> list;

	@Override
	public void onAttach(Activity activity) {
		ctx = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.fragment_dj, container, false);
		lvList = (ListView) rootView.findViewById(R.id.lvList);
		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setRefreshing(true);
		new GetDjList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				((Void) null));

		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent intent = new Intent(ctx, DJDetailsActivity.class);
				intent.putExtra("dj", list.get(pos));
				startActivity(intent);
			}
		});

		return rootView;
	}

	private class GetDjList extends AsyncTask<Void, Void, Void> {
		HashMap<String, String> response;

		@Override
		protected Void doInBackground(Void... params) {
			response = ClientManager.getGetAllDj();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (response != null) {
				if (response.get("status_code").contains("200")) {
					try {
						JSONObject json = new JSONObject(
								response.get("response"));
						JSONArray array = json.getJSONArray("result");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							Dj dj = new Dj();
							dj.setId(obj.getString("user_id"));
							dj.setName(obj.getString("user_name"));
							dj.setImageURL(obj.getString("user_avatar"));
							list.add(dj);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					lvList.setAdapter(new DJAdapter(ctx, list));
				} else {
					Toast.makeText(ctx, "Failed. Please check connection.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ctx, "Failed. Please check connection.",
						Toast.LENGTH_SHORT).show();
			}
			swipeLayout.setRefreshing(false);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			list = new ArrayList<Dj>();
			super.onPreExecute();
		}

	}

	@Override
	public void onRefresh() {
		new GetDjList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				((Void) null));
	}
}
