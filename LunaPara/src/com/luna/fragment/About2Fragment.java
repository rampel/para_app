package com.luna.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import com.luna.para.R;

public class About2Fragment extends Fragment implements OnClickListener {

	private Context ctx;

	@Override
	public void onAttach(Activity activity) {
		ctx = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.about2fragment, container,
				false);
		init(rootView);
		return rootView;
	}

	private void init(View rootView) {
	}

	@Override
	public void onClick(View v) {

	}

}
