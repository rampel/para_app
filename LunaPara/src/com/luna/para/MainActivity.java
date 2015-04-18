/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.luna.para;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.luna.adapter.BaseActivity;
import com.luna.adapter.CategoryAdapter;
import com.luna.adapter.NavigationDrawerAdapter;
import com.luna.adapter.NetworkManager;
import com.luna.adapter.NewsAdapter;
import com.luna.base.AnimatedTabHostListener;
import com.luna.base.Conversion;
import com.luna.base.Prefs;
import com.luna.entity.Category;
import com.luna.entity.News;
import com.luna.fragment.AboutFragment;
import com.luna.fragment.ChatFragment;
import com.luna.fragment.DJFragment;
import com.luna.fragment.LoginFragment;
import com.luna.fragment.RegisterFragment;
import com.luna.fragment.ShowFragment;
import com.luna.para.R;
import com.luna.web.ClientManager;

public class MainActivity extends BaseActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	public Context ctx = this;
	public MediaPlayer mediaPlayer;
	public boolean isPrepared;
	public int currentPosition;
	private RelativeLayout rlStream;
	private TextView tvTime;
	private boolean isOnLoad;
	private static LruCache<String, Bitmap> mMemoryCache;
	private ImageView ivPlay;
	private ProgressBar mProgressBar;
	private WebView wbView;
	public NavigationDrawerAdapter adapter;
	private TextView tvTitlePlaying;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_orig);
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount() / 1024;
			}
		};

		mediaPlayer = new MediaPlayer();
		ivPlay = (ImageView) findViewById(R.id.ivPlay);
		tvTitlePlaying = (TextView) findViewById(R.id.tvTitle);
		tvTime = (TextView) findViewById(R.id.tvTime);
		mProgressBar = (ProgressBar) findViewById(R.id.mProgressbar);
		wbView = (WebView) findViewById(R.id.wbView);
		wbView.setWebViewClient(new MyWebViewClient());
		wbView.loadUrl("http://tinigpinoy.net/login");
		mProgressBar.setVisibility(View.INVISIBLE);

		if (isOnLoad) {
			mProgressBar.setVisibility(View.VISIBLE);
			ivPlay.setVisibility(View.INVISIBLE);
			rlStream.setClickable(false);
		}
		rlStream = (RelativeLayout) findViewById(R.id.rlStream);
		rlStream.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkManager.isNetworkOnline(ctx)) {
					new GetShowDetail().execute((Void) null);
					if (((MainActivity) ctx).mediaPlayer.isPlaying()) {
						ivPlay.setImageDrawable(ctx.getResources().getDrawable(
								R.drawable.play_ico));
						((MainActivity) ctx).mediaPlayer.pause();
					} else {
						if (isPrepared) {
							isPrepared = true;
							ivPlay.setImageDrawable(ctx.getResources()
									.getDrawable(R.drawable.pause_ico));
							((MainActivity) ctx).mediaPlayer.start();
							rlStream.setClickable(true);
						} else {
							rlStream.setClickable(false);
							mProgressBar.setVisibility(View.VISIBLE);
							ivPlay.setVisibility(View.INVISIBLE);
							try {
								isOnLoad = true;
								Uri myUri = Uri
										.parse("http://s6.voscast.com:8044/;stream.mp3");
								mediaPlayer.setDataSource(ctx, myUri);
								mediaPlayer
										.setAudioStreamType(AudioManager.STREAM_MUSIC);
								mediaPlayer.prepareAsync();
								mediaPlayer
										.setOnPreparedListener(new OnPreparedListener() {

											@Override
											public void onPrepared(
													MediaPlayer mp) {
												isOnLoad = false;
												mProgressBar
														.setVisibility(View.INVISIBLE);
												ivPlay.setVisibility(View.VISIBLE);
												isPrepared = true;
												ivPlay.setImageDrawable(ctx
														.getResources()
														.getDrawable(
																R.drawable.pause_ico));
												((MainActivity) ctx).mediaPlayer
														.start();
												rlStream.setClickable(true);
											}
										});
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						mHandler.sendMessageDelayed(mHandler.obtainMessage(),
								1000);

					}
				} else {
					toastLong("Please check your connection. Please try again later.");
				}
			}
		});

		isPrepared = false;
		initialSetup();
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#2063a7")));
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		adapter = new NavigationDrawerAdapter(ctx, ctx.getResources()
				.getStringArray(R.array.menu_array));
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.menu_icon, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(
						Html.fromHtml("<font color=\"white\">" + mTitle
								+ "</font>"));
				invalidateOptionsMenu(); // creates call to
			}

			public void onDrawerOpened(View drawerView) {
				mDrawerTitle = ctx.getResources().getString(R.string.app_name);
				getActionBar().setTitle(
						Html.fromHtml("<font color=\"white\">" + mDrawerTitle
								+ "</font>"));
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(1);
		}
	}

	private class GetShowDetail extends AsyncTask<Void, Void, Void> {
		HashMap<String, String> response;

		@Override
		protected Void doInBackground(Void... params) {
			response = ClientManager.getShowDetail(Prefs.getMyIntPref(ctx,
					Prefs.CURRENT_SHOWID) + "");
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
						tvTitlePlaying.setText(result.getString("title")
								.equals("") ? "Streaming" : result
								.getString("title"));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			super.onPostExecute(r);
		}
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
			String cookies = CookieManager.getInstance().getCookie(url);
			Log.d(">>>>COOKIES", "All the cookies in a string:" + cookies);
			new GetSetup().execute((Void) null);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public static Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	private void initialSetup() {
		Prefs.setMyIntPref(ctx, Prefs.CURRENT_SHOWID, 72);
		Prefs.setMyStringPref(ctx, Prefs.PLAYER_URL,
				"http://s6.voscast.com:8044/;stream.mp3");
		Prefs.setMyStringPref(ctx, Prefs.AUTO_PLAY, "1");
		Prefs.setMyStringPref(ctx, Prefs.VOLUME, "20");
		Prefs.setMyStringPref(ctx, Prefs.LOGIN_PAGE,
				"http://tinigpinoy.net/login");
		Prefs.setMyStringPref(ctx, Prefs.REGISTER_PAGE,
				"http://tinigpinoy.net/register");
		Prefs.setMyStringPref(ctx, Prefs.ABOUT_PAGE,
				"http://tinigpinoy.net/about");
		Prefs.setMyStringPref(ctx, Prefs.MYPOST_PAGE,
				"http://tinigpinoy.net/dashboard");
		Prefs.setMyStringPref(ctx, Prefs.CHAT_PAGE,
				"http://tinigpinoy.net/chat");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (currentPosition == 1) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main, menu);
		}

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				CookieSyncManager.createInstance(ctx);
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.removeAllCookie();
				clearCache(ctx, 100);
				wbView.clearCache(true);
				wbView.clearFormData();
				wbView.clearHistory();
				ctx.deleteDatabase("webview.db");
				ctx.deleteDatabase("webviewCache.db");
				Prefs.setMyStringPref(ctx, Prefs.USER_ID, "");
				Prefs.setMyStringPref(ctx, Prefs.USER_NAME, "");
				Prefs.setMyStringPref(ctx, Prefs.USER_AVATAR, "");
				selectItem(1);
				adapter.notifyDataSetChanged();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				selectItem(1);
				break;
			}
		}
	};

	public void selectItem(int position) {
		if (position != 0) {
			currentPosition = position;
			invalidateOptionsMenu();
			Fragment fragment = null;
			if (position == 6) {
				if (Prefs.getMyStringPrefs(ctx, Prefs.USER_ID).equals("")) {
					fragment = new LoginFragment();
					// update the main content by replacing fragments
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();

					// update selected item and title, then close the drawer
					mDrawerList.setItemChecked(position, true);
					setTitle(Html.fromHtml("<font color=\"white\">"
							+ ctx.getResources().getStringArray(
									R.array.menu_array)[position] + "</font>"));
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("Are you sure you want to logout?")
							.setPositiveButton("Yes", dialogClickListener)
							.setNegativeButton("No", dialogClickListener)
							.show();
				}
			} else {
				switch (position) {
				case 0:
					fragment = new LoginFragment();
					break;
				case 1:
					fragment = new LatestAndCategoriesFragment();
					break;
				case 2:
					fragment = new DJFragment();
					break;
				case 3:
					fragment = new ChatFragment();
					break;
				case 4:
					fragment = new ShowFragment();
					break;
				case 5:
					fragment = new RegisterFragment();
					break;
				case 6:
					fragment = new LoginFragment();
					break;
				case 7:
					fragment = new AboutFragment();
					break;
				}
				// update the main content by replacing fragments
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();

				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				setTitle(Html
						.fromHtml("<font color=\"white\">"
								+ ctx.getResources().getStringArray(
										R.array.menu_array)[position]
								+ "</font>"));
				mDrawerLayout.closeDrawer(mDrawerList);

			}

		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(
				Html.fromHtml("<font color=\"white\">" + mTitle + "</font>"));
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void updateAction() {
		// TODO Auto-generated method stub
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
						Prefs.setMyStringPref(ctx, Prefs.USER_ID, resultObject
								.getString("user_id").replace("\\", ""));
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
						adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			super.onPostExecute(result);
		}
	}

	@SuppressLint("ValidFragment")
	public class LatestAndCategoriesFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";
		Context ctx;

		private ListView lvNews;
		private ListView lvCategories;
		private int page = 1;
		private View footerView;
		private int preLast;
		private NewsAdapter adapter;
		private List<News> listOfNews;
		private SwipeRefreshLayout swipeLayout;

		@Override
		public void onAttach(Activity activity) {
			ctx = activity;
			super.onAttach(activity);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.latest_categories_fragment, container, false);
			swipeLayout = (SwipeRefreshLayout) rootView
					.findViewById(R.id.swipe_container);
			swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			footerView = ((LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.footer_view, null, true);
			listOfNews = new ArrayList<News>();
			adapter = new NewsAdapter(ctx, listOfNews);

			lvNews = (ListView) rootView.findViewById(R.id.lvListNews);
			lvNews.addFooterView(footerView);
			lvNews.setAdapter(adapter);
			lvNews.removeFooterView(footerView);
			swipeLayout.setRefreshing(true);
			lvNews.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(ctx, NewsDetailActivity.class);
					intent.putExtra("news", listOfNews.get(position));
					startActivity(intent);
				}
			});
			lvCategories = (ListView) rootView
					.findViewById(R.id.lvListCategories);
			final TabHost tabs = (TabHost) rootView.findViewById(R.id.tabHost);
			tabs.setup();

			tabs.addTab(tabs.newTabSpec("latest").setIndicator("Latest")
					.setContent(R.id.rlTab1));
			tabs.addTab(tabs.newTabSpec("categories")
					.setIndicator("Categories").setContent(R.id.rlTab2));
			tabs.setOnTabChangedListener(new AnimatedTabHostListener(tabs));

			new AsyncTask<Void, Void, Void>() {
				HashMap<String, String> response = null;
				List<Category> list;
				List<String> listOfCategories;

				@Override
				protected Void doInBackground(Void... params) {
					response = ClientManager.getCategories();
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					if (response != null) {
						if (response.get("status_code").contains("200")) {
							try {
								JSONObject json = new JSONObject(
										response.get("response"));
								JSONArray array = json.getJSONArray("result");
								listOfCategories = new ArrayList<String>();
								list = new ArrayList<Category>();
								for (int i = 0; i < array.length(); i++) {
									Category c = new Category();
									c.setId(Integer.valueOf(array
											.getJSONObject(i).getString(
													"category_ID")));
									c.setCategoryName(array.getJSONObject(i)
											.getString("category_name"));
									list.add(c);
									listOfCategories.add(c.getCategoryName());
								}
								final CategoryAdapter adapter2 = new CategoryAdapter(
										ctx, list);
								lvCategories.setAdapter(adapter2);
								lvCategories
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> parent,
													View view, int position,
													long id) {
												CategoryAdapter.idCheck = list
														.get(position).getId();
												adapter2.notifyDataSetChanged();
												lvNews.addFooterView(footerView);
												tabs.setCurrentTab(0);
												listOfNews.clear();
												page = 1;
												getNews(swipeLayout, adapter,
														lvNews, footerView,
														page, listOfNews, list
																.get(position)
																.getId());
											}
										});
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					super.onPostExecute(result);
				}

			}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((Void) null));
			getNews(swipeLayout, adapter, lvNews, footerView, page, listOfNews,
					-1);
			lvNews.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					final int lastItem = firstVisibleItem + visibleItemCount;
					if (lastItem == totalItemCount) {
						if (preLast != lastItem) {
							lvNews.addFooterView(footerView);
							preLast = lastItem + 1;
							page++;
							getNews(swipeLayout, adapter, lvNews, footerView,
									page, listOfNews, -1);
						}
					}

				}
			});
			swipeLayout.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					listOfNews.clear();
					page = 1;
					getNews(swipeLayout, adapter, lvNews, footerView, page,
							listOfNews, -1);
				}
			});

			return rootView;
		}

	}

	private void getNews(final SwipeRefreshLayout sl,
			final NewsAdapter adapter, final ListView lvNews,
			final View footerView, final int page, final List<News> listOfNews,
			final int categoryId) {
		new AsyncTask<Void, Void, Void>() {
			HashMap<String, String> response;

			@Override
			protected Void doInBackground(Void... params) {
				if (categoryId != -1) {
					response = ClientManager.getNewsByCategoriesId(categoryId);
				} else {
					response = ClientManager.getGetNewslist(page);
				}
				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Void result) {
				if (response != null) {
					if (response.get("status_code").contains("200")) {
						try {
							JSONObject json = new JSONObject(
									response.get("response"));
							JSONArray array = json.getJSONArray("result");
							for (int i = 0; i < array.length(); i++) {
								JSONObject newsObject = array.getJSONObject(i);
								News n = new News();
								n.setId(Integer.valueOf(newsObject
										.getString("ID")));
								n.setTitle(newsObject.getString("title"));
								n.setPost_date(newsObject
										.getString("post-date"));
								n.setAuthor_id(newsObject
										.getString("author_id"));
								n.setAuthor(newsObject.getString("author"));
								n.setCategory_id(newsObject
										.getString("category_id"));
								n.setCategory(newsObject.getString("category"));
								n.setHits(newsObject.getString("hits"));
								n.setThumbnail(newsObject
										.getString("thumbnail").replaceAll(
												"\\/", "/"));
								n.setCommentCount(Integer.valueOf(newsObject
										.getJSONObject("comment_count")
										.getString("0")));
								listOfNews.add(n);
							}
							adapter.notifyDataSetChanged();
							sl.setRefreshing(false);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					sl.setRefreshing(false);
					toastLong("No connection");
				}
				lvNews.removeFooterView(footerView);
				super.onPostExecute(result);
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((Void) null));
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			tvTime.setText(Conversion.convertMilliToTimeFormat(mediaPlayer
					.getCurrentPosition()));
			sendMessageDelayed(this.obtainMessage(), 1000);
		}
	};
}