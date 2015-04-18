package com.luna.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.luna.base.Prefs;
import com.luna.para.MainActivity;
import com.luna.para.R;

public class RegisterFragment extends Fragment {

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
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			mProgressBar.setVisibility(View.GONE);
			String cookies = CookieManager.getInstance().getCookie(url);
			Log.d(">>>>COOKIES", "All the cookies in a string:" + cookies);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			Log.d(">>>>>>DAW", error.toString());
			super.onReceivedSslError(view, handler, error);
		}

		@Override
		public void onReceivedHttpAuthRequest(WebView view,
				HttpAuthHandler handler, String host, String realm) {
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
			Log.d("HOST", host);
			Log.d("HOST", realm);
		}

		@Override
		public void onReceivedLoginRequest(WebView view, String realm,
				String account, String args) {
			super.onReceivedLoginRequest(view, realm, account, args);
			Log.d("REALM", realm);
			Log.d("ACCOUNT", account);
			Log.d("ARGS", args);
		}

	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.web_fragment, container,
				false);
		WebView webview = (WebView) rootView.findViewById(R.id.webView);
		webview.setWebViewClient(new MyWebViewClient());
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setUserAgentString("uo81yrDq2");
		webview.loadUrl(Prefs.getMyStringPrefs(ctx, Prefs.REGISTER_PAGE));
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.mProgressbar);
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.bringToFront();
		return rootView;
	}
}