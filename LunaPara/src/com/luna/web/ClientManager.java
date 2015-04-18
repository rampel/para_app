package com.luna.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import com.luna.base.Prefs;

import android.content.Context;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.webkit.CookieManager;

public class ClientManager {

	public static String android_id;
	public static String TAG = ">>>> CLIENT >>>>";
	public static String userName = "neil@yahoo.com";
	public static String password = "";
	public static Vibrator vib;
	public static Context ctx;
	public static DefaultHttpClient client;
	public static HttpGet httpGet;
	public static HttpPost httpPost;
	public static HttpPut httpPut;

	public static HashMap<String, String> getSetup(Context ctx) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = "http://tinigpinoy.net/?oJSON=89172938729183&type=setup";
		try {
			result = executeHttpGetResponseWithCookie(url, "", ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getGetNewslist(int page) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = "http://tinigpinoy.net/?oJSON=89172938729183&page=" + page;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getGetAllDj() {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = "http://tinigpinoy.net/?oJSON=89172938729183&type=alldjs";
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getDJShow(String id) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.DJ_SHOW + id;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getDJNews(String id) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.DJ_NEWS + id;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getShowDetail(String id) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.SHOW_DETAILS + id;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getGetDJDetails(String string) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.DJ_DETAILS + string;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getShowPerDj(String string) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.DJ_SHOW + string;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getNewsPerDj(String string) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.DJ_NEWS + string;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getAllShows() {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = "http://tinigpinoy.net/?oJSON=89172938729183&type=allshows";
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getNews(int newsId) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = "http://tinigpinoy.net/?oJSON=89172938729183&post_id="
				+ newsId + "&type=post";
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getCategories() {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.CATEGORIES;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static HashMap<String, String> getNewsByCategoriesId(int id) {
		HashMap<String, String> result = new HashMap<String, String>();
		String url = WebURL.NEWS_BY_CATEGORY + id;
		try {
			result = executeHttpGetResponse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, url);
		return result;
	}

	public static int executeHTTPPost(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		int statusCode = -1;

		try {
			httpPost = new HttpPost(url);
			httpPost.addHeader("Authorization", "Basic " + getCredentials());
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			httpPost.setEntity(formEntity);
			HttpResponse response = client.execute(httpPost);
			statusCode = response.getStatusLine().getStatusCode();

			Log.d("REQUEST RESPONSE:", response + "");
			Log.d("REQUEST CODE:", statusCode + "");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return statusCode;
	}

	public static String executeHTTPPostResponse(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		int statusCode = -1;
		String responseEntity = "";

		try {
			httpPost = new HttpPost(url);
			httpPost.addHeader("Authorization", "Basic " + getCredentials());
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			httpPost.setEntity(formEntity);
			HttpResponse response = client.execute(httpPost);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			responseEntity = convertStreamToString(entity.getContent());

			Log.d("REQUEST RESPONSE:", responseEntity + "");
			Log.d("REQUEST CODE:", statusCode + "");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return responseEntity;
	}

	/*
	 * public static String[] execHTTPPostResponse(String url,
	 * ArrayList<NameValuePair> postParameters) throws Exception {
	 * BufferedReader in = null; int statusCode = -1; String responseEntity =
	 * ""; String[] array = new String[2]; array[0] = "-1"; array[1] = ""; try {
	 * DefaultHttpClient client = new DefaultHttpClient(); HttpPost request =
	 * new HttpPost(url); request.addHeader("Authorization", "Basic " +
	 * getCredentials()); UrlEncodedFormEntity formEntity = new
	 * UrlEncodedFormEntity( postParameters); request.setEntity(formEntity);
	 * HttpResponse response = client.execute(request); statusCode =
	 * response.getStatusLine().getStatusCode(); HttpEntity entity =
	 * response.getEntity(); responseEntity =
	 * convertStreamToString(entity.getContent()); array[0] = statusCode + "";
	 * array[1] = responseEntity; Log.d("REQUEST RESPONSE:", responseEntity +
	 * ""); Log.d("REQUEST CODE:", statusCode + ""); } finally { if (in != null)
	 * { try { in.close(); } catch (IOException e) { e.printStackTrace(); } } }
	 * return array; }
	 */

	public static HashMap<String, String> execHTTPPostResponse(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {

		BufferedReader in = null;
		int statusCode = -1;
		String responseEntity = "";
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			httpPost = new HttpPost(url);
			httpPost.addHeader("Authorization", "Basic " + getCredentials());
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			httpPost.setEntity(formEntity);
			HttpResponse response = client.execute(httpPost);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			responseEntity = convertStreamToString(entity.getContent());
			map.put("status_code", statusCode + "");
			map.put("response", responseEntity);

			Log.d("REQUEST RESPONSE:", responseEntity + "");
			Log.d("REQUEST CODE:", statusCode + "");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public static HashMap<String, String> execHTTPPutResponse(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {

		BufferedReader in = null;
		int statusCode = -1;
		String responseEntity = "";
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			httpPut = new HttpPut(url);
			httpPut.addHeader("Authorization", "Basic " + getCredentials());
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			httpPut.setEntity(formEntity);
			HttpResponse response = client.execute(httpPut);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			responseEntity = convertStreamToString(entity.getContent());
			map.put("status_code", statusCode + "");
			map.put("response", responseEntity);
			Log.d("REQUEST RESPONSE:", responseEntity + "");
			Log.d("REQUEST CODE:", statusCode + "");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	private static String getCredentials() {
		String cred = null;

		try {
			cred = Base64.encodeToString(
					(userName + ":" + password).getBytes("UTF-8"),
					android.util.Base64.NO_WRAP);

			Log.d("Http", "BasicAuth: " + cred);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return cred;
	}

	public static String executeHttpGet(String url) throws Exception {
		httpGet = new HttpGet(url);

		httpGet.addHeader("Authorization", "Basic " + getCredentials());
		HttpResponse response;

		try {
			response = client.execute(httpGet);
			Log.i("LOG", "Status:[" + response.getStatusLine().toString() + "]");

			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				String result = convertStreamToString(inputStream);
				Log.i("Log", "Result of converstion: [" + result + "]");
				inputStream.close();
				return result;
			}
		} catch (ClientProtocolException e) {
			Log.e("REST", "There was a protocol based error", e);
		} catch (IOException e) {
			Log.e("REST", "There was an IO Stream related error", e);
		}
		return null;
	}

	public static HashMap<String, String> executeHttpGetResponse(String url)
			throws Exception {
		HttpGet httpGet = new HttpGet(url);
		HashMap<String, String> map = new HashMap<String, String>();
		HttpResponse response;
		client = new DefaultHttpClient();
		try {
			response = client.execute(httpGet);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				String result = convertStreamToString(inputStream);
				Log.i("Log", "Result of converstion: [" + result + "]");
				inputStream.close();
				map.put("response", result);
				map.put("status_code", response.getStatusLine().toString());
				return map;
			}
		} catch (ClientProtocolException e) {
			Log.e("REST", "There was a protocol based error", e);
		} catch (IOException e) {
			Log.e("REST", "There was an IO Stream related error", e);
		}
		return null;
	}

	public static HashMap<String, String> executeHttpGetResponseWithCookie(
			String url, String cookie, Context ctx) throws Exception {
		HttpGet httpGet = new HttpGet(url);
		HashMap<String, String> map = new HashMap<String, String>();
		HttpResponse response;
		client = new DefaultHttpClient();
		try {
			httpGet.addHeader(
					"Cookie",
					getCookieFromAppCookieManager("http://tinigpinoy.net/login"));
			response = client.execute(httpGet);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				String result = convertStreamToString(inputStream);
				Log.i("Log", "Result of converstion: [" + result + "]");
				inputStream.close();
				map.put("response", result);
				map.put("status_code", response.getStatusLine().toString());
				return map;
			}
		} catch (ClientProtocolException e) {
			Log.e("REST", "There was a protocol based error", e);
		} catch (IOException e) {
			Log.e("REST", "There was an IO Stream related error", e);
		}
		return null;
	}

	public static String getCookieFromAppCookieManager(String url)
			throws MalformedURLException {
		CookieManager cookieManager = CookieManager.getInstance();
		if (cookieManager == null)
			return null;
		String rawCookieHeader = null;
		URL parsedURL = new URL(url);

		// Extract Set-Cookie header value from Android app CookieManager for
		// this URL
		rawCookieHeader = cookieManager.getCookie(parsedURL.getHost());
		if (rawCookieHeader == null)
			return null;
		return rawCookieHeader;
	}

	public static int executeHttpGetStatus(String url) throws Exception {
		httpGet = new HttpGet(url);

		httpGet.addHeader("Authorization", "Basic " + getCredentials());
		HttpResponse response;

		try {
			response = client.execute(httpGet);
			Log.i("LOG", "Status:[" + response.getStatusLine().toString() + "]");

			return response.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			Log.e("REST", "There was a protocol based error", e);
		} catch (IOException e) {
			Log.e("REST", "There was an IO Stream related error", e);
		}
		return 0;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String getOrientation(Context context) {
		boolean isReverse = false;
		final int rotation = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getOrientation();
		switch (rotation) {

		case Surface.ROTATION_90:
			return "landscape";
		default:
			return "reverse landscape";
		}

	}

	public static void vibrate(Context context) {
		vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(150);

	}

}
