package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

import com.jmbg.oldgloriescalendar.data.Clasificacion;

import android.util.Log;

public class LectorClasificacion {

	private final static String URL_CLASIFICACION = "http://54.229.202.59/futbol/clasificacion.txt";
	private List<Clasificacion> clasificacion;

	public LectorClasificacion() {
		this.clasificacion = new ArrayList<Clasificacion>();
		lectorClasificacion();
	}

	private void lectorClasificacion() {
		HttpURLConnection con = null;
		InputStream is = null;
		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
		// "oliver.gmv.es", 80));
		try {
			con = (HttpURLConnection) (new URL(URL_CLASIFICACION)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				Clasificacion clas = generarClasificacion(line);
				if (clas != null)
					this.clasificacion.add(clas);
			}
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
	
	private Clasificacion generarClasificacion(String linea) {
		Log.d(Constantes.TAG, "[" + LectorLiga.class.getName()
				+ ".generarClasificacion] Generando linea clasificacion [" + linea + "]");

		Clasificacion clas = new Clasificacion();
		String campos[] = linea.split("\\|");

		int posicion = 0;
		String nombre= "";
		int partidoJugados = 0;
		int partidosGanados = 0;
		int partidosPerdidos = 0;
		int partidosEmpatados = 0;
		int golesFavor = 0;
		int golesContra = 0;
		int puntos = 0;

		
		try {
			nombre = campos[1];
			posicion = Integer.parseInt(campos[0]);
			partidoJugados = Integer.parseInt(campos[2]);
			partidosGanados = Integer.parseInt(campos[3]);
			partidosPerdidos = Integer.parseInt(campos[4]);
			partidosEmpatados = Integer.parseInt(campos[5]);
			golesFavor = Integer.parseInt(campos[6]);
			golesContra = Integer.parseInt(campos[7]);
			puntos = Integer.parseInt(campos[8]);
		} catch (NumberFormatException eNFE) {
			eNFE.printStackTrace();
		}
		
		clas.setNombre(nombre);
		clas.setPosicion(posicion);
		clas.setPartidoJugados(partidoJugados);
		clas.setPartidosGanados(partidosGanados);
		clas.setPartidosPerdidos(partidosPerdidos);
		clas.setPartidosEmpatados(partidosEmpatados);
		clas.setGolesFavor(golesFavor);
		clas.setGolesContra(golesContra);
		clas.setPuntos(puntos);
		
		return clas;
	}

	public List<Clasificacion> obtenerClasificacion() {
		return this.clasificacion;
	}
}
