package com.luna.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.zxing.client.android.CaptureActivity;
import com.luna.base.Prefs;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class AboutFragment extends Fragment implements OnClickListener {

	private ImageView btnRideATaxi;
	private ImageView btnReportAnIncident;
	private Context ctx;

	@Override
	public void onAttach(Activity activity) {
		ctx = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.main_fragment, container,
				false);
		init(rootView);
		return rootView;
	}

	private void init(View rootView) {
		btnRideATaxi = (ImageView) rootView.findViewById(R.id.btnRideATaxi);
		btnRideATaxi.setOnClickListener(this);
		btnReportAnIncident = (ImageView) rootView
				.findViewById(R.id.btnReportAnIncident);
		btnReportAnIncident.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRideATaxi:
			Intent intent = new Intent(ctx, CaptureActivity.class);
			startActivity(intent);
			break;

		case R.id.btnReportAnIncident:

			break;

		default:
			break;
		}

	}

}
