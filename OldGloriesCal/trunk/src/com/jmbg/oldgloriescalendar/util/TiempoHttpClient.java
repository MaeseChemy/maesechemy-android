package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class TiempoHttpClient {

	private static String IMG_URL = "http://openweathermap.org/img/w/";
	private static final String PETICION_TIEMPO = "http://api.openweathermap.org/data/2.5/forecast/daily?q="
			+ Constantes.DIRECCION_POLIDEPORTIVO.replace(" ", "") + "&mode=json&units=metric&cnt=10";

	public String getDatosDeTiempo() {
		HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(PETICION_TIEMPO);
		// Execute the request
		HttpResponse response;

		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			// A Simple JSON Response Read
			InputStream instream = entity.getContent();
			Log.d(Constantes.TAG, "---- instream --- " + instream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\r\n");
			}

			instream.close();
			return sb.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		} 
		return null;

	}

	public byte[] getImage(String code) {
		HttpURLConnection con = null;
		InputStream is = null;
		try {
			con = (HttpURLConnection) (new URL(IMG_URL + code))
					.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			// Let's read the response
			is = con.getInputStream();
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while (is.read(buffer) != -1)
				baos.write(buffer);

			return baos.toByteArray();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
			}
			try {
				con.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;

	}
}