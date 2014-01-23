package com.jmbg.genapuestas.restRandom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RandomRest {

	private static final String URL_REST = "http://www.random.org/integers/";
	
	public static List<Integer> generarNumerosAleatorios(int min, int max, int cantidad)  throws MalformedURLException,
	IOException {
		List<Integer> numbers = new ArrayList<Integer>();
		//http://www.random.org/sequences/?min=1&max=50&col=50&format=plain&rnd=new
		String url_petition = URL_REST + "?min="+min+"&max="+max+"&num="+cantidad+"&col=1&unique=on&format=plain&rnd=new";
		
		URL url;
		url = new URL(url_petition);
		
		// Opening and sending data
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
		conn.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.flush();

		// Receiving and parsing data
		InputStream is = conn.getInputStream();
		String returnData = convertStreamToString(is);
		String arrayNumberString[]  = returnData.split("\\n");
		for(String number : arrayNumberString){
			System.out.print(number + " ");
			numbers.add(Integer.parseInt(number));
		}
		System.out.println("\nGenerados: "+numbers.size());
		return numbers;
	}
	
	private static String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

}
