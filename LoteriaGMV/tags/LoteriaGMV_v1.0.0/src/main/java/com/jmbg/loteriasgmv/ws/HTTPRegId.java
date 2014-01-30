package com.jmbg.loteriasgmv.ws;

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

public class HTTPRegId {							  
	private final static String URL_REGISTER_REGID = "http://54.229.202.59/loteria_gmv/scripts/register.php";
	private final static String URL_UNREGISTER_REGID = "http://54.229.202.59/loteria_gmv/scripts/unregister.php";

	public boolean doRegisterRegId(String imei, String regid) {

		HttpURLConnection con = null;
		InputStream is = null;

		try {

			con = (HttpURLConnection) (new URL(URL_REGISTER_REGID).openConnection());

			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("imei", imei));
			params.add(new BasicNameValuePair("regid", regid));

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
					logstatus = json_data.getInt("registerstatus");

					if (logstatus == 1) {
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
	
	public boolean doUnRegisterRegId(String imei, String regid) {

		HttpURLConnection con = null;
		InputStream is = null;

		try {

			con = (HttpURLConnection) (new URL(URL_UNREGISTER_REGID).openConnection());

			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("device_imei", imei));
			params.add(new BasicNameValuePair("registration_id", regid));

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
					logstatus = json_data.getInt("unregisterstatus");

					if (logstatus == 1) {
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
