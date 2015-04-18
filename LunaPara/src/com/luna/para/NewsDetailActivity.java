package com.luna.para;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.luna.adapter.BaseActivity;
import com.luna.base.Prefs;
import com.luna.entity.News;
import com.luna.para.R;
import com.luna.web.ClientManager;

public class NewsDetailActivity extends BaseActivity {

	private ImageView ivImage;
	private TextView tvCategory;
	private TextView tvTitle;
	private TextView tvAuthorName;
	private ImageView ivAuthorPic;
	private TextView tvPostDate;
	private News news;
	private WebView wbFacebook;
	private LinearLayout llContent;

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		news = (News) getIntent().getExtras().getSerializable("news");
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#2063a7")));
		getActionBar().setTitle(news.getCategory());
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		initComponents();
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	private void initComponents() {
		wbFacebook = (WebView) findViewById(R.id.wbFacebook);
		wbFacebook.setWebViewClient(new MyWebViewClient());
		wbFacebook.getSettings().setJavaScriptEnabled(true);
		wbFacebook.getSettings().setDomStorageEnabled(true);
		String userAgent = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
		wbFacebook.getSettings().setUserAgentString(userAgent);
		llContent = (LinearLayout) findViewById(R.id.llContent);
		ivImage = (ImageView) findViewById(R.id.ivImage);
		tvCategory = (TextView) findViewById(R.id.tvCategoryName);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvAuthorName = (TextView) findViewById(R.id.tvAuthor);
		ivAuthorPic = (ImageView) findViewById(R.id.ivAuthorImage);
		tvPostDate = (TextView) findViewById(R.id.tvPostDate);
		tvCategory.setText(news.getCategory() + "");
		tvTitle.setText(news.getTitle());
		tvAuthorName.setText(news.getAuthor() + "");
		tvPostDate.setText(news.getPost_date() + " | Hits: "
				+ news.getHeartCount() + " | " + news.getCommentCount()
				+ " comments");
		new AsyncTask<Void, Void, Void>() {
			HashMap<String, String> response;

			@Override
			protected Void doInBackground(Void... params) {
				response = ClientManager.getNews(news.getId());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (response != null) {
					if (response.get("status_code").contains("200")) {
						try {
							JSONObject json = new JSONObject(
									response.get("response"));
							JSONObject resultObj = json.getJSONObject("result");
							// Html.fromHtml(resultObj
							// .getString("content"));
							String content = resultObj.getString("content");
							String fbURL = resultObj.getString("URL");
							wbFacebook
									.loadDataWithBaseURL(
											"http://www.facebook.com",
											"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html><head> <meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\"> <title>Facebook Comments Test Page</title> <style type=\"text/css\"> .fb-comments, span, .fb-comments iframe[style] {width: %dpx !important;}</style> </head> <body > <div id=\"fb-root\"></div> <script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; if (d.getElementById(id)) return; js = d.createElement(s); js.id = id; js.src = \"//connect.facebook.net/en_US/all.js#xfbml=1\";  fjs.parentNode.insertBefore(js, fjs); }(document, \"script\", \"facebook-jssdk\"));</script> <fb:comments href="
													+ fbURL
													+ " num_posts=\"10\" width=\"470\"></fb:comments> </body> </html>",
											"text/html", "UTF-8", null);
							try {
								String[] contentSplitted = content
										.split("[link display=");
								for (String s : contentSplitted) {
									TextView valueTV = new TextView(
											NewsDetailActivity.this);
									valueTV.setText(Html.fromHtml(s));
									valueTV.setId(5);
									valueTV.setLayoutParams(new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									llContent.addView(valueTV);
								}
							} catch (Exception e) {
								TextView valueTV = new TextView(
										NewsDetailActivity.this);
								valueTV.setText(Html.fromHtml(content));
								valueTV.setId(5);
								valueTV.setLayoutParams(new LayoutParams(
										LayoutParams.FILL_PARENT,
										LayoutParams.WRAP_CONTENT));
								llContent.addView(valueTV);
								e.printStackTrace();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				super.onPostExecute(result);
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);

		new AsyncTask<Void, Void, Void>() {
			Bitmap bitmap;

			@Override
			protected Void doInBackground(Void... params) {

				bitmap = MainActivity
						.getBitmapFromMemCache(news.getThumbnail());
				if (bitmap == null) {
					try {
						URL url = new URL(news.getThumbnail());
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
				if (bitmap != null) {
					ivImage.setImageBitmap(bitmap);
				}
				super.onPostExecute(result);
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);

	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; goto parent activity.
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
