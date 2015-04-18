package com.luna.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
=======
import android.webkit.WebView;
import android.webkit.WebViewClient;
>>>>>>> 6397fbe450a24ec0ed022a85a1511b9d0fcbaa43
import android.widget.ProgressBar;

import com.luna.base.Prefs;
import com.luna.para.MainActivity;
import com.luna.para.R;

<<<<<<< HEAD
public class AboutFragment extends Fragment implements OnClickListener {

	private Button btnRideATaxi;
	private Button btnReportAnIncident;
	private Context ctx;
=======
public class AboutFragment extends Fragment {

	private Context ctx;
	private ProgressBar mProgressBar;
>>>>>>> 6397fbe450a24ec0ed022a85a1511b9d0fcbaa43

	@Override
	public void onAttach(Activity activity) {
		ctx = activity;
		super.onAttach(activity);
	}

<<<<<<< HEAD
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.main_fragment, container,
				false);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRideATaxi:

			break;

		case R.id.btnReportAnIncident:

			break;

		default:
			break;
		}

	}

=======
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mProgressBar.setVisibility(View.GONE);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.web_fragment, container,
				false);
		
		WebView webview = (WebView) rootView.findViewById(R.id.webView);
		webview.setWebViewClient(new MyWebViewClient());
		webview.getSettings().setUserAgentString("uo81yrDq2");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(Prefs.getMyStringPrefs(ctx, Prefs.ABOUT_PAGE));
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.mProgressbar);
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.bringToFront();
		return rootView;
	}
>>>>>>> 6397fbe450a24ec0ed022a85a1511b9d0fcbaa43
}
