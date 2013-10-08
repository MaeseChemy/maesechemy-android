package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class TiempoHttpClient {

	private static final String PETICION_TIEMPO = "http://api.openweathermap.org/data/2.5/forecast/daily?q="
			+ Constantes.DIRECCION_POLIDEPORTIVO.replace(" ", "")
			+ "&mode=json&units=metric&cnt=10";

	
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

}