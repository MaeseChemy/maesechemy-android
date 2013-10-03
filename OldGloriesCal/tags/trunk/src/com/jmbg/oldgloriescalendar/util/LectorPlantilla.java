package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.jmbg.oldgloriescalendar.data.Jugador;

import android.util.Log;

public class LectorPlantilla {

	private final static String URL_JUGADORES = "http://54.229.202.59/futbol/plantilla.txt";
	private List<Jugador> plantilla;

	public LectorPlantilla() {
		this.plantilla = new ArrayList<Jugador>();
		lectorPlantilla();
	}

	private void lectorPlantilla() {
		HttpURLConnection con = null;
		InputStream is = null;
		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
		// "oliver.gmv.es", 80));
		try {
			con = (HttpURLConnection) (new URL(URL_JUGADORES)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			// Let's read the response
			StringBuffer buffer = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				Jugador jugador = generarJugador(line);
				if (jugador != null)
					this.plantilla.add(jugador);
			}

			System.out.println(buffer.toString());
			is.close();
			con.disconnect();
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
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private void lectorPlantillaOld() {
		HttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
		HttpGet httpget = new HttpGet(URL_JUGADORES);
		// Execute the request
		HttpResponse response;

		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			InputStream instream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				Jugador jugador = generarJugador(line);
				if (jugador != null)
					this.plantilla.add(jugador);
			}

			instream.close();
		} catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
		}
	}

	private Jugador generarJugador(String linea) {
		Log.d(Constantes.TAG, "[" + LectorLiga.class.getName()
				+ ".generarJugador] Generando jugador [" + linea + "]");

		Jugador jugador = new Jugador();
		String campos[] = linea.split("\\|");

		int dorsal = 0;
		String nombre= "";
		int goles = 0;
		int tarjetasAmarillas = 0;
		int tarjetasRojas = 0;
		try {
			nombre = campos[1];
			dorsal = Integer.parseInt(campos[0]);
			goles = Integer.parseInt(campos[2]);
			tarjetasAmarillas = Integer.parseInt(campos[3]);
			tarjetasRojas = Integer.parseInt(campos[4]);
		} catch (NumberFormatException eNFE) {
			eNFE.printStackTrace();
		}
		
		jugador.setDorsal(dorsal);
		jugador.setNombre(nombre);
		jugador.setGoles(goles);
		jugador.setTarjetasAmarillas(tarjetasAmarillas);
		jugador.setTarjetasRojas(tarjetasRojas);
		return jugador;
	}

	public List<Jugador> obtenerJugadores() {
		return this.plantilla;
	}
}
