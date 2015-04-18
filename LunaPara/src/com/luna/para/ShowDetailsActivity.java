package com.luna.para;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.luna.adapter.BaseActivity;
import com.luna.base.RoundedImageView;
import com.luna.entity.Dj;
import com.luna.entity.Show;
import com.luna.para.R;
import com.luna.web.ClientManager;

public class ShowDetailsActivity extends BaseActivity {

	private ImageView ivShow;
	private HorizontalScrollView scrollView;
	private ImageView ivMonday;
	private ImageView ivTuesday;
	private ImageView ivWednesday;
	private ImageView ivThursday;
	private ImageView ivFriday;
	private ImageView ivSaturday;
	private ImageView ivSunday;
	private TextView tvTime;
	private TextView tvHtmlContent;
	private Show show;
	private List<String> listOfDj;
	private List<Dj> listOfDjEntity;
	private Context ctx = this;
	private LinearLayout llImageHolder;
	private WebView wbFacebook;

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.show_details_activity);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#2063a7")));
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		wbFacebook = (WebView) findViewById(R.id.wvFacebook);
		wbFacebook.setWebViewClient(new MyWebViewClient());
		wbFacebook.getSettings().setJavaScriptEnabled(true);
		String userAgent = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
		wbFacebook.getSettings().setUserAgentString(userAgent);
		show = (Show) getIntent().getExtras().getSerializable("show");
		getActionBar().setTitle(show.getTitle());
		ivShow = (ImageView) findViewById(R.id.ivImage);
		tvTime = (TextView) findViewById(R.id.tvTime);
		ivMonday = (ImageView) findViewById(R.id.ivMonday);
		ivTuesday = (ImageView) findViewById(R.id.ivTuesday);
		ivWednesday = (ImageView) findViewById(R.id.ivWednesday);
		ivThursday = (ImageView) findViewById(R.id.ivThursday);
		ivFriday = (ImageView) findViewById(R.id.ivFriday);
		ivSaturday = (ImageView) findViewById(R.id.ivSaturday);
		ivSunday = (ImageView) findViewById(R.id.ivSunday);
		tvHtmlContent = (TextView) findViewById(R.id.tvDetails);
		llImageHolder = (LinearLayout) findViewById(R.id.llImageHolder);
		listOfDj = new ArrayList<String>();
		listOfDjEntity = new ArrayList<Dj>();
		tvTime.setText(show.getTime());

		if (show.getShowDays().contains("1")) {
			ivMonday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.m_red));
		} else {
			ivMonday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.m_grey));
		}

		if (show.getShowDays().contains("2")) {
			ivTuesday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.t_red));
		} else {
			ivTuesday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.t_grey));
		}

		if (show.getShowDays().contains("3")) {
			ivWednesday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.w_red));
		} else {
			ivWednesday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.w_grey));
		}
		if (show.getShowDays().contains("4")) {
			ivThursday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.th_red));
		} else {
			ivThursday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.th_grey));
		}
		if (show.getShowDays().contains("5")) {
			ivFriday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.f_red));
		} else {
			ivFriday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.f_grey));
		}
		if (show.getShowDays().contains("6")) {
			ivSaturday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.sa_red));
		} else {
			ivSaturday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.sa_grey));
		}
		if (show.getShowDays().contains("0")) {
			ivSunday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.s_red));
		} else {
			ivSunday.setImageDrawable(this.getResources().getDrawable(
					R.drawable.s_grey));
		}
		showZapLoadingProgressDialog(ctx, "Loading show...");
		new LoadImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				(Void) null);
		new GetShowDetail().execute((Void) null);
	}

	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private Context ctx;
		Bitmap bitmap;
		int position;

		@Override
		protected Void doInBackground(Void... params) {
			bitmap = MainActivity.getBitmapFromMemCache(show.getImageUrl());
			if (bitmap == null) {
				try {
					URL url = new URL(show.getImageUrl());
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
				MainActivity.addBitmapToMemoryCache(show.getImageUrl(), bitmap);
				ivShow.setImageBitmap(bitmap);
			}
			super.onPostExecute(result);
		}

	}

	private class GetShowDetail extends AsyncTask<Void, Void, Void> {
		HashMap<String, String> response;

		@Override
		protected Void doInBackground(Void... params) {
			response = ClientManager.getShowDetail(show.getId());
			return null;
		}

		@Override
		protected void onPostExecute(Void r) {
			if (response != null) {
				if (response.get("status_code").contains("200")) {
					Log.d(">>>>RESPONSE", response.get("response"));
					try {
						JSONObject object = new JSONObject(
								response.get("response"));
						JSONObject result = object.getJSONObject("result");
						tvHtmlContent.setText(Html.fromHtml(result
								.getString("content")));
						String fbURL = result.getString("URL");
						wbFacebook
								.loadDataWithBaseURL(
										"http://www.facebook.com",
										"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html><head> <meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\"> <title>Facebook Comments Test Page</title> <style type=\"text/css\"> .fb-comments, span, .fb-comments iframe[style] {width: %dpx !important;}</style> </head> <body > <div id=\"fb-root\"></div> <script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; if (d.getElementById(id)) return; js = d.createElement(s); js.id = id; js.src = \"//connect.facebook.net/en_US/all.js#xfbml=1\";  fjs.parentNode.insertBefore(js, fjs); }(document, \"script\", \"facebook-jssdk\"));</script> <fb:comments href="
												+ fbURL
												+ " num_posts=\"10\" width=\"470\"></fb:comments> </body> </html>",
										"text/html", "UTF-8", null);
						JSONArray array = result.getJSONArray("dj_ids");
						for (int i = 0; i < array.length(); i++) {
							new GetDjList(array.getString(i))
									.executeOnExecutor(
											AsyncTask.THREAD_POOL_EXECUTOR,
											(Void) null);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			super.onPostExecute(r);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

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

	private class GetDjList extends AsyncTask<Void, Void, Void> {
		HashMap<String, String> response;

		String dj_id;
		Dj dj;

		public GetDjList(String dj_id) {
			this.dj_id = dj_id;
		}

		@Override
		protected Void doInBackground(Void... params) {
			response = ClientManager.getGetDJDetails(dj_id);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (response != null) {
				if (response.get("status_code").contains("200")) {
					new AsyncTask<Void, Void, Void>() {
						Bitmap bitmap;

						@Override
						protected Void doInBackground(Void... params) {
							try {
								JSONObject json = new JSONObject(
										response.get("response"));
								JSONObject obj = json.getJSONObject("result");
								dj = new Dj();
								dj.setId(dj_id);
								dj.setBio_data(obj.getString("user_bio"));
								dj.setImageURL(obj.getString("user_avatar"));
								dj.setName(obj.getString("user_name"));
								listOfDjEntity.add(dj);
								bitmap = MainActivity.getBitmapFromMemCache(dj
										.getImageURL());
								if (bitmap == null) {
									try {
										URL url = new URL(
												obj.getString("user_avatar"));
										HttpURLConnection connection = (HttpURLConnection) url
												.openConnection();
										connection.setDoInput(true);
										connection.connect();
										InputStream input = connection
												.getInputStream();
										bitmap = BitmapFactory
												.decodeStream(input);
									} catch (IOException e) {
										e.printStackTrace();
										return null;
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							RoundedImageView imageView = new RoundedImageView(
									ctx);
							imageView.setImageBitmap(bitmap);
							LayoutParams lp = new LayoutParams(80, 80);
							lp.setMargins(10, 10, 10, 10);
							imageView.setLayoutParams(lp);
							imageView.setScaleType(ScaleType.CENTER_CROP);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(ctx,
											DJDetailsActivity.class);
									intent.putExtra("dj", dj);
									startActivity(intent);
								}
							});
							llImageHolder.addView(imageView);
							super.onPostExecute(result);
						}
					}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
							(Void) null);

				} else {
					Toast.makeText(getBaseContext(),
							"Failed. Please check connection.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getBaseContext(),
						"Failed. Please check connection.", Toast.LENGTH_SHORT)
						.show();
			}
			dismissZapProgressDialog();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
