package com.luna.fragment;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.luna.base.Prefs;
import com.luna.para.MainActivity;
import com.luna.para.R;
import com.luna.web.ClientManager;

public class LoginFragment extends Fragment {

	private Context ctx;
	private ProgressBar mProgressBar;
	private String cookies;

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
			view.clearCache(true);
			view.clearHistory();
			view.clearFormData();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			String cookies = CookieManager.getInstance().getCookie(url);
			mProgressBar.setVisibility(View.GONE);
			Log.d(">>>>COOKIES", "All the cookies in a string:" + cookies);
			new GetSetup().execute((Void) null);
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

	private class GetSetup extends AsyncTask<Void, Void, Void> {
		HashMap<String, String> map;

		@Override
		protected Void doInBackground(Void... params) {
			map = ClientManager.getSetup(ctx);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (map != null) {
				if (map.get("status_code").contains("200")) {
					try {
						Log.d("RESPONSE", map.get("response"));
						JSONObject jo = new JSONObject(map.get("response"));
						JSONObject resultObject = jo.getJSONObject("result");
						Prefs.setMyIntPref(ctx, Prefs.CURRENT_SHOWID, Integer
								.valueOf(resultObject
										.getString("currentshow_id")));
						Prefs.setMyStringPref(ctx, Prefs.AUTO_PLAY,
								resultObject.getString("autoplay"));
						Prefs.setMyStringPref(ctx, Prefs.VOLUME,
								resultObject.getString("volume"));
						Prefs.setMyStringPref(
								ctx,
								Prefs.LOGIN_PAGE,
								resultObject.getString("login_page").replace(
										"\\", ""));
						Prefs.setMyStringPref(ctx, Prefs.REGISTER_PAGE,
								resultObject.getString("register_page")
										.replace("\\", ""));
						Prefs.setMyStringPref(
								ctx,
								Prefs.ABOUT_PAGE,
								resultObject.getString("about_page").replace(
										"\\", ""));
						Prefs.setMyStringPref(
								ctx,
								Prefs.MYPOST_PAGE,
								resultObject.getString("myposts_page").replace(
										"\\", ""));
						Prefs.setMyStringPref(
								ctx,
								Prefs.CHAT_PAGE,
								resultObject.getString("chat_page").replace(
										"\\", ""));
						Prefs.setMyStringPref(ctx, Prefs.USER_ID,
								resultObject.getString("user_id"));
						Prefs.setMyStringPref(
								ctx,
								Prefs.USER_NAME,
								resultObject.getString("user_name").replace(
										"\\", ""));
						Prefs.setMyStringPref(
								ctx,
								Prefs.USER_AVATAR,
								resultObject.getString("user_avatar")
										.replace("\\", "").split("src='")[1]
										.split(";r=G'")[0]);
						((MainActivity) ctx).adapter.notifyDataSetChanged();

						if (!Prefs.getMyStringPrefs(ctx, Prefs.USER_ID).equals(
								"")) {
							((MainActivity) ctx).selectItem(1);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			super.onPostExecute(result);
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
		String userAgent = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
		webview.getSettings().setUserAgentString(userAgent);
		webview.loadUrl(Prefs.getMyStringPrefs(ctx, Prefs.LOGIN_PAGE));
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.mProgressbar);
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.bringToFront();
		return rootView;
	}
}
