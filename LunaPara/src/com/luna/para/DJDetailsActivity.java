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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luna.adapter.BaseActivity;
import com.luna.adapter.NewsAdapter;
import com.luna.adapter.ShowAdapter;
import com.luna.entity.Dj;
import com.luna.entity.News;
import com.luna.entity.Show;
import com.luna.para.R;
import com.luna.web.ClientManager;

public class DJDetailsActivity extends BaseActivity implements OnClickListener {

	private Dj dj;
	private ImageView ivBackground;
	private ImageView ivProfile;
	private TextView tvDjName;
	private TextView tvAboutMeHtml;
	private RelativeLayout rlShows;
	private RelativeLayout rlPost;
	private Context ctx = this;
	private List<News> listOfNews;
	private List<Show> listOfShows;
	private TextView tvShowsCount;
	private TextView tvPostCount;

	@Override
	public void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.dj_details_activity);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#2063a7")));
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		dj = (Dj) getIntent().getExtras().getSerializable("dj");
		getActionBar().setTitle(dj.getName());
		initComponents();
		new LoadImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				((Void) null));
		new GetDjList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				((Void) null));

	}

	private void initComponents() {
		ivBackground = (ImageView) findViewById(R.id.ivImage);
		ivProfile = (ImageView) findViewById(R.id.ivMainPicture);
		tvDjName = (TextView) findViewById(R.id.tvDjName);
		tvAboutMeHtml = (TextView) findViewById(R.id.tvHtmlContent);
		tvDjName.setText(dj.getName());
		rlPost = (RelativeLayout) findViewById(R.id.rlPost);
		rlShows = (RelativeLayout) findViewById(R.id.rlShows);
		tvShowsCount = (TextView) findViewById(R.id.tvShowNumber);
		tvPostCount = (TextView) findViewById(R.id.tvPostNumber);

		rlShows.setOnClickListener(this);
		rlPost.setOnClickListener(this);
	}

	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private Context ctx;
		Bitmap bitmap;
		int position;

		@Override
		protected Void doInBackground(Void... params) {
			bitmap = MainActivity.getBitmapFromMemCache(dj.getImageURL());
			if (bitmap == null) {
				try {
					URL url = new URL(dj.getImageURL());
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
				MainActivity.addBitmapToMemoryCache(dj.getImageURL(), bitmap);
				ivProfile.setImageBitmap(bitmap);
				Bitmap blur = fastblur(bitmap, 10);
				ivBackground.setImageBitmap(blur);
			}
			super.onPostExecute(result);
		}

	}

	private class GetDjList extends AsyncTask<Void, Void, Void> {
		HashMap<String, String> response;

		@Override
		protected Void doInBackground(Void... params) {
			response = ClientManager.getGetDJDetails(dj.getId());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (response != null) {
				if (response.get("status_code").contains("200")) {
					try {
						JSONObject json = new JSONObject(
								response.get("response"));
						JSONObject obj = json.getJSONObject("result");
						dj.setBio_data(obj.getString("user_bio"));
						tvAboutMeHtml.setText(Html.fromHtml(dj.getBio_data()));

						new AsyncTask<Void, Void, Void>() {

							HashMap<String, String> response;

							@Override
							protected Void doInBackground(Void... params) {
								response = ClientManager.getShowPerDj(dj
										.getId());
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								if (response != null) {
									if (response.get("status_code").contains(
											"200")) {
										try {
											JSONObject json = new JSONObject(
													response.get("response"));
											JSONArray array = json
													.getJSONArray("result");
											for (int i = 0; i < array.length(); i++) {
												JSONObject obj = array
														.getJSONObject(i);
												Show s = new Show();
												s.setId(obj.getString("ID"));
												s.setImageUrl(obj
														.getJSONArray(
																"thumbnail")
														.get(0).toString());
												s.setTitle(obj
														.getString("title"));
												s.setTime(obj
														.getString("timeslot"));
												s.setShowDays(obj
														.getString("show_days"));
												listOfShows.add(s);
											}
											tvShowsCount.setText(listOfShows
													.size() + "");
										} catch (JSONException e) {
											e.printStackTrace();
										}
									} else {
										Toast.makeText(
												DJDetailsActivity.this,
												"Failed. Please check connection.",
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(DJDetailsActivity.this,
											"Failed. Please check connection.",
											Toast.LENGTH_SHORT).show();
								}

								super.onPostExecute(result);
							}

							@Override
							protected void onPreExecute() {
								showZapLoadingProgressDialog(ctx,
										"Loading shows...");
								listOfShows = new ArrayList<Show>();
								super.onPreExecute();
							}

						}.execute((Void) null);

						new AsyncTask<Void, Void, Void>() {

							HashMap<String, String> response;

							@Override
							protected Void doInBackground(Void... params) {
								response = ClientManager.getNewsPerDj(dj
										.getId());
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								if (response != null) {
									if (response.get("status_code").contains(
											"200")) {
										try {
											JSONObject json = new JSONObject(
													response.get("response"));
											JSONArray array = json
													.getJSONArray("result");
											for (int i = 0; i < array.length(); i++) {
												JSONObject newsObject = array
														.getJSONObject(i);
												News n = new News();
												n.setId(Integer.valueOf(newsObject
														.getString("ID")));
												n.setTitle(newsObject
														.getString("title"));
												n.setPost_date(newsObject
														.getString("post-date"));
												n.setAuthor_id(newsObject
														.getString("author_id"));
												n.setAuthor(newsObject
														.getString("author"));
												n.setCategory_id(newsObject
														.getString("category_id"));
												n.setCategory(newsObject
														.getString("category"));
												n.setHits(newsObject
														.getString("hits"));
												n.setThumbnail(newsObject
														.getString("thumbnail")
														.replaceAll("\\/", "/"));
												n.setCommentCount(Integer
														.valueOf(newsObject
																.getJSONObject(
																		"comment_count")
																.getString("0")));
												listOfNews.add(n);
											}
											tvPostCount.setText(listOfNews
													.size() + "");
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								} else {
									toastLong("No connection");
								}
								dismissZapProgressDialog();
								super.onPostExecute(result);
							}

							@Override
							protected void onPreExecute() {
								showZapLoadingProgressDialog(ctx,
										"Loading shows...");
								listOfNews = new ArrayList<News>();
								super.onPreExecute();
							}

						}.execute((Void) null);
					} catch (JSONException e) {
						e.printStackTrace();
					}

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
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			showZapLoadingProgressDialog(DJDetailsActivity.this, "Loading...");
			super.onPreExecute();
		}
	}

	public Bitmap fastblur(Bitmap sentBitmap, int radius) {

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
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

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlShows:
			final Dialog dialog = new Dialog(this);
			dialog.setTitle("SHOWS");
			dialog.setContentView(R.layout.dialog_list);
			final ListView lvList = (ListView) dialog.findViewById(R.id.lvList);

			lvList.setAdapter(new ShowAdapter(ctx, listOfShows, true));
			lvList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(ctx, ShowDetailsActivity.class);
					intent.putExtra("show", listOfShows.get(arg2));
					startActivity(intent);
				}
			});
			if (listOfShows.size() != 0) {
				dialog.show();
			} else {
			}
			break;

		case R.id.rlPost:
			final Dialog postDialog = new Dialog(this);
			postDialog.setTitle("POSTS");
			postDialog.setContentView(R.layout.dialog_list);
			final ListView lvListPost = (ListView) postDialog
					.findViewById(R.id.lvList);
			lvListPost.setAdapter(new NewsAdapter(ctx, listOfNews));
			if (listOfNews.size() != 0) {
				postDialog.show();
			}

			break;

		default:
			break;
		}
	}
}
