package com.google.zxing.client.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Configuration {
	public static  void TCP(String name) {
		Socket socket = null;

		try {
			socket = new Socket("192.168.0.130", 9101);

			if (socket.isConnected()) {
				byte b[] = new byte[6 + name.length()];
				b[0] = 'G';
				b[1] = 'U';
				b[2] = 'E';
				b[3] = 'S';
				b[4] = 'T';
				b[5] = (byte) name.length();
				char nameChars[] = name.toCharArray();
				for (int i = 0; i < name.length(); ++i) {
					b[6 + i] = (byte) nameChars[i];
				}
				try {

					OutputStream out = socket.getOutputStream();
					out.write(b);
					out.flush();
					

				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
	}

	public  static void sendData(ArrayList<NameValuePair> data) {

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.0.130/dtrscanner/guest.php");
			httppost.setEntity(new UrlEncodedFormEntity(data));
			HttpResponse response = httpclient.execute(httppost);
		} catch (Exception e) {
		}
	}
	
	
	public  static String sendNoId(String usecode, String usepass) {
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					6);
			nameValuePairs.add(new BasicNameValuePair("uid", usecode));
			nameValuePairs.add(new BasicNameValuePair("upass", usepass));
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.0.130/dtrscanner/NoId.php");
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			/*String res = EntityUtils.getContentCharSet(response.getEntity()).toString();
			String response = Entity.getC
			InputStream is = response.getEntity().getContent();
			*/
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
	}
	
	
	public  static String GuestValidation(String str) {
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					6);
			nameValuePairs.add(new BasicNameValuePair("uid", str));
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.0.130/dtrscanner/GuestValidate.php");
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
	}

	public static void sendGuestData(String givname, String companyname,
			String contactpersons, String guestpurpose, String photo) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				6);
		nameValuePairs.add(new BasicNameValuePair("fname", givname));
		nameValuePairs.add(new BasicNameValuePair("Cname", companyname));
		nameValuePairs.add(new BasicNameValuePair("CP", contactpersons));
		nameValuePairs.add(new BasicNameValuePair("GP", guestpurpose));
		nameValuePairs.add(new BasicNameValuePair("image", photo));
		sendData(nameValuePairs);
	}
	
	
	
	
}
