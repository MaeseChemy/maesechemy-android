package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.jmbg.oldgloriescalendar.data.Clasificacion;
import com.jmbg.oldgloriescalendar.data.Convocatoria;
import com.jmbg.oldgloriescalendar.data.Equipo;
import com.jmbg.oldgloriescalendar.data.Jugador;
import com.jmbg.oldgloriescalendar.data.Partido;

public class LectorDatosLiga {

	private Context context;

	//Lista de partidos generados
	private List<Partido> partidos;
	
	//Lista de equipos generados
	private List<Equipo> equipos;

	//URL donde esta el fichero con la plantilla
	private final static String URL_JUGADORES = "http://54.229.202.59/futbol/plantilla.txt";
	//Lista con la plantilla generada
	private List<Jugador> plantilla;

	
	//URL donde se encuentra el fichero con la clasificacion
	private final static String URL_CLASIFICACION = "http://54.229.202.59/futbol/clasificacion.txt";
	//Lista  con la clasificacion generada
	private List<Clasificacion> clasificacion;
	
	//PHP que devuelve un JSON con la clasificacion
	private final static String URL_CONVOCATORIA = "http://54.229.202.59/futbol/scripts/getconv.php";
	//Lista de convocados
	private List<Convocatoria> convocatoriaJornada;
	
	public LectorDatosLiga(Context context) {
		this.context = context;
		this.partidos = new ArrayList<Partido>();
		this.equipos = new ArrayList<Equipo>();
		this.clasificacion = new ArrayList<Clasificacion>();
		this.plantilla = new ArrayList<Jugador>();
		this.convocatoriaJornada = new ArrayList<Convocatoria>();
	}

	/*LIGA*/
	/*----*/
	public List<Partido> leerPartidos() {
		AssetManager am = this.context.getAssets();
		InputStream is;

		Log.i(Constantes.TAG,
				"["
						+ LectorDatosLiga.class.getName()
						+ ".leerPartidos] Iniciando lectura de ficheros para generar los partidos de la liga... ["
						+ Constantes.FICHERO_PARTIDOS + "]");
		try {
			is = am.open(Constantes.FICHERO_PARTIDOS);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String linea = "";

			while ((linea = br.readLine()) != null) {
				Partido partido = generarPartido(linea);
				if (partido != null)
					this.partidos.add(partido);
			}
			is.close();
			br.close();
		} catch (FileNotFoundException exFNF) {
			Log.e(Constantes.TAG, "[" + LectorDatosLiga.class.getName()
					+ ".leerPartidos] Fichero [" + Constantes.FICHERO_PARTIDOS
					+ "] no localizado [" + exFNF.getMessage() + "]");
		} catch (IOException exIO) {
			Log.e(Constantes.TAG,
					"[" + LectorDatosLiga.class.getName()
							+ ".leerPartidos] Error I/O al leer el fichero ["
							+ exIO.getMessage() + "]");
		}
		
		return this.partidos;

	}
	
	/**
	 * Genera un objeto Partido a partir de una linea;
	 * JORNADA|FECHA|HORA|EQUIPO_LOCAL|EQUIPO_VISITANTE|CAMPO
	 * 
	 * @param linea
	 *            Datos del partido
	 * @return Objeto Partido con los datos del mismo o null en caso de que la
	 *         linea no sea de un partido del equipo principal.
	 */
	private Partido generarPartido(String linea) {
		Log.d(Constantes.TAG, "[" + LectorDatosLiga.class.getName()
				+ ".generarPartido] Generando partido [" + linea + "]");

		Partido partido = new Partido();
		String campos[] = linea.split("\\|");

		String fecha = campos[1] + " " + campos[2];
		String local = campos[3];
		String visitante = campos[4];

		if (local.equals(Constantes.EQUIPO)
				|| visitante.equals(Constantes.EQUIPO)) {
			Log.d(Constantes.TAG, "[" + LectorDatosLiga.class.getName()
					+ ".generarPartido] Partido relevante...");
			partido.setJornada(campos[0]);

			Date date = new Date();
			try {
				date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
						.parse(fecha);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			partido.setFecha(date);

			if (local.equals(Constantes.EQUIPO)) {
				partido.setOponente(visitante);
				partido.setLocal(true);
			} else if (visitante.equals(Constantes.EQUIPO)) {
				partido.setOponente(local);
				partido.setLocal(false);
			}

			partido.setLugar(campos[5]);
			return partido;
		} else {
			Log.w(Constantes.TAG,
					"["
							+ LectorDatosLiga.class.getName()
							+ ".generarPartido] Partido no relevante. Ninguno de los equipos es ["
							+ Constantes.EQUIPO + "]. Se devuelve null.");
			return null;
		}

	}

	
	/*EQUIPOS*/
	/*-------*/
	public List<Equipo> leerEquipos() {
		AssetManager am = this.context.getAssets();
		InputStream is;

		Log.i(Constantes.TAG,
				"["
						+ LectorDatosLiga.class.getName()
						+ ".leerEquipos] Iniciando lectura de ficheros para generar los equipos de la liga... ["
						+ Constantes.FICHERO_EQUIPOS + "]");
		try {
			is = am.open(Constantes.FICHERO_EQUIPOS);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String linea = "";

			while ((linea = br.readLine()) != null) {
				Log.e(Constantes.TAG,linea);
				Equipo equipo = generarEquipo(linea);
				if (equipo != null)
					this.equipos.add(equipo);
			}
			is.close();
			br.close();
		} catch (FileNotFoundException exFNF) {
			Log.e(Constantes.TAG, "[" + LectorDatosLiga.class.getName()
					+ ".leerEquipos] Fichero [" + Constantes.FICHERO_EQUIPOS
					+ "] no localizado [" + exFNF.getMessage() + "]");
		} catch (IOException exIO) {
			Log.e(Constantes.TAG,
					"[" + LectorDatosLiga.class.getName()
							+ ".leerEquipos] Error I/O al leer el fichero ["
							+ exIO.getMessage() + "]");
		}
		return this.equipos;
	}

	/**
	 * Genera un objeto Partido a partir de una linea; EQUIPO|CAMISETA
	 * 
	 * @param linea
	 *            Datos del partido
	 * @return Objeto Partido con los datos del mismo o null en caso de que la
	 *         linea no sea de un partido del equipo principal.
	 */
	private Equipo generarEquipo(String linea) {
		Log.d(Constantes.TAG, "[" + LectorDatosLiga.class.getName()
				+ ".generaEquipo] Generando equipo [" + linea + "]");

		Equipo equipo = new Equipo();
		String campos[] = linea.split("\\|");

		String nombre = campos[0];
		String camiseta = campos[1];

		equipo.setNombre(nombre);
		equipo.setCamiseta(camiseta);

		return equipo;
	}
	
	/*PLANTILLA*/
	public List<Jugador> leerPlantilla() {
		HttpURLConnection con = null;
		InputStream is = null;

		try {
			con = (HttpURLConnection) (new URL(URL_JUGADORES)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				Jugador jugador = generarJugador(line);
				if (jugador != null)
					this.plantilla.add(jugador);
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
		return this.plantilla;
	}

	private Jugador generarJugador(String linea) {
		Log.d(Constantes.TAG, "[" + LectorDatosLiga.class.getName()
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

	
	/*CLASIFICACION*/
	/*-------------*/
	public List<Clasificacion> leerClasificacion() {
		HttpURLConnection con = null;
		InputStream is = null;

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
		return this.clasificacion;
	}
	
	private Clasificacion generarClasificacion(String linea) {
		Log.d(Constantes.TAG, "[" + LectorDatosLiga.class.getName()
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
	
	
	/*CONVOCATORIA*/
	/*------------*/
	
	public List<Convocatoria> leerConvocatoria(String jornada) {
		HttpURLConnection con = null;
		InputStream is = null;
		// Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
		// "oliver.gmv.es", 80));
		try {
			// con = (HttpURLConnection) (new
			// URL(URL_ACCESS)).openConnection(proxy);
			con = (HttpURLConnection) (new URL(URL_CONVOCATORIA).openConnection());

			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("jornada", jornada));

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
			
			ConvocatoriaJSON lectorJConvocatoria = new ConvocatoriaJSON();
			this.convocatoriaJornada = lectorJConvocatoria.convocatoriaFromJSON(jornada, buffer.toString());
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
		return this.convocatoriaJornada;
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
