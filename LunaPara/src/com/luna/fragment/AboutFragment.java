package com.luna.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.luna.base.Prefs;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class AboutFragment extends Fragment {

	private Context ctx;
	private ProgressBar mProgressBar;

	@Override
	public void onAttach(Activity activity) {
		ctx = activity;
		super.onAttach(activity);
	}

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
}
