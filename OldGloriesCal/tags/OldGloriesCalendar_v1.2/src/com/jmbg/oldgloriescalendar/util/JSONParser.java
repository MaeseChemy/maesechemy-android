package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	InputStream is = null;
	JSONObject jObj = null;
	String json = "";

	// constructor
	public JSONParser() {
	}

	public String getJSONFromUrl(String url) {

		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(url);
		Log.e("url", url);

		// Execute the request
		HttpResponse response;
		try {

			// https://api.vkontakte.ru/method/audio.search?uid=163398985&q=akoncount=100&access_token=2a4db0e223f0f5ab23f0f5ab5f23da5680223f023f1f5a3c696b018be9b17b9

			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				Log.d(Constantes.TAG, "---- instream --- " + instream);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream, "iso-8859-1"), 8);

				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				json = sb.toString();
				Log.d(Constantes.TAG, "---- Result --- " + json);

				// now you have the string representation of the HTML request
				instream.close();

			} else {
				Log.d(Constantes.TAG, "---- Json Activity is --- null ");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		return json;

	}
}