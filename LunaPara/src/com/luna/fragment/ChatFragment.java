package com.luna.fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.luna.para.MainActivity;
import com.luna.para.R;

public class ChatFragment extends Fragment {

	private Context ctx;

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
			((MainActivity) ctx).dismissZapProgressDialog();
			super.onPageFinished(view, url);
		}

	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.web_fragment, container,
				false);
		((MainActivity) ctx).showZapLoadingProgressDialog(ctx,
				"Loading page...");
		WebView mWebView = (WebView) rootView.findViewById(R.id.webView);
		mWebView.setWebViewClient(new MyWebViewClient());
		WebSettings ws = mWebView.getSettings();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			try {
				Method m1 = WebSettings.class.getMethod("setDomStorageEnabled",
						new Class[] { Boolean.TYPE });
				m1.invoke(ws, Boolean.TRUE);

				Method m2 = WebSettings.class.getMethod("setDatabaseEnabled",
						new Class[] { Boolean.TYPE });
				m2.invoke(ws, Boolean.TRUE);

				Method m3 = WebSettings.class.getMethod("setDatabasePath",
						new Class[] { String.class });
				m3.invoke(ws, "/data/data/" + ctx.getPackageName()
						+ "/databases/");

				Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize",
						new Class[] { Long.TYPE });
				m4.invoke(ws, 1024 * 1024 * 8);

				Method m5 = WebSettings.class.getMethod("setAppCachePath",
						new Class[] { String.class });
				m5.invoke(ws, "/data/data/" + ctx.getPackageName() + "/cache/");

				Method m6 = WebSettings.class.getMethod("setAppCacheEnabled",
						new Class[] { Boolean.TYPE });
				m6.invoke(ws, Boolean.TRUE);

			} catch (NoSuchMethodException e) {
				Log.d("wew", "wew1");
			} catch (InvocationTargetException e) {
				Log.d("wew", "wew2");
			} catch (IllegalAccessException e) {
				Log.d("wew", "wew13");
			}
		}
		ws.setJavaScriptEnabled(true);
		ws.setAllowFileAccess(true);
		String userAgent = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
		ws.setUserAgentString(userAgent);

		mWebView.loadUrl("http://tinigpinoy.chatango.com/?js");

		return rootView;
	}

}