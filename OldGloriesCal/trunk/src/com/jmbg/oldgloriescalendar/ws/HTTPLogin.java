package com.jmbg.oldgloriescalendar.ws;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPLogin {
	private String URL_ACCESS = "http://54.229.202.59/futbol/scripts/access.php";

	public boolean doLogin(String username, String password) {

		HttpURLConnection con = null;
		InputStream is = null;

		try {

			con = (HttpURLConnection) (new URL(URL_ACCESS).openConnection());

			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("usuario", username));
			params.add(new BasicNameValuePair("password", password));

			OutputStream os = con.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, "UTF-8"));
			writer.write(getQuery(params));
			writer.flush();
			writer.close();
			os.close();

			con.connect();

			// Let's read the response
			StringBuffer buffer = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() != 0)
					buffer.append(line + "\r\n");
			}

			is.close();
			con.disconnect();

			int logstatus;
			JSONArray jArray = new JSONArray(buffer.toString());
			if (jArray != null && jArray.length() > 0) {

				JSONObject json_data;
				try {
					json_data = jArray.getJSONObject(0);
					logstatus = json_data.getInt("logstatus");

					if (logstatus == 0) {
						return false;
					} else {
						return true;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				return false;
			}
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
		return false;
	}

	private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}
