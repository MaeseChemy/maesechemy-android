package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.jmbg.oldgloriescalendar.data.Equipo;
import com.jmbg.oldgloriescalendar.data.Partido;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class LectorLiga {

	private Context context;

	private List<Partido> partidos;
	private List<Equipo> equipos;

	public LectorLiga(Context context) {
		this.context = context;
		this.partidos = new ArrayList<Partido>();
		this.equipos = new ArrayList<Equipo>();
		leerPartidos();
		leerEquipos();
	}

	private void leerPartidos() {
		AssetManager am = this.context.getAssets();
		InputStream is;

		Log.i(Constantes.TAG,
				"["
						+ LectorLiga.class.getName()
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
			Log.e(Constantes.TAG, "[" + LectorLiga.class.getName()
					+ ".leerPartidos] Fichero [" + Constantes.FICHERO_PARTIDOS
					+ "] no localizado [" + exFNF.getMessage() + "]");
		} catch (IOException exIO) {
			Log.e(Constantes.TAG,
					"[" + LectorLiga.class.getName()
							+ ".leerPartidos] Error I/O al leer el fichero ["
							+ exIO.getMessage() + "]");
		}
	}

	private void leerEquipos() {
		AssetManager am = this.context.getAssets();
		InputStream is;

		Log.i(Constantes.TAG,
				"["
						+ LectorLiga.class.getName()
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
			Log.e(Constantes.TAG, "[" + LectorLiga.class.getName()
					+ ".leerEquipos] Fichero [" + Constantes.FICHERO_EQUIPOS
					+ "] no localizado [" + exFNF.getMessage() + "]");
		} catch (IOException exIO) {
			Log.e(Constantes.TAG,
					"[" + LectorLiga.class.getName()
							+ ".leerEquipos] Error I/O al leer el fichero ["
							+ exIO.getMessage() + "]");
		}
	}

	public List<Partido> obtenerPartidos() {
		return this.partidos;
	}

	public List<Equipo> obtenerEquipos() {
		return this.equipos;
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
		Log.d(Constantes.TAG, "[" + LectorLiga.class.getName()
				+ ".generarPartido] Generando partido [" + linea + "]");

		Partido partido = new Partido();
		String campos[] = linea.split("\\|");

		String fecha = campos[1] + " " + campos[2];
		String local = campos[3];
		String visitante = campos[4];

		if (local.equals(Constantes.EQUIPO)
				|| visitante.equals(Constantes.EQUIPO)) {
			Log.d(Constantes.TAG, "[" + LectorLiga.class.getName()
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
							+ LectorLiga.class.getName()
							+ ".generarPartido] Partido no relevante. Ninguno de los equipos es ["
							+ Constantes.EQUIPO + "]. Se devuelve null.");
			return null;
		}

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
		Log.d(Constantes.TAG, "[" + LectorLiga.class.getName()
				+ ".generaEquipo] Generando equipo [" + linea + "]");

		Equipo equipo = new Equipo();
		String campos[] = linea.split("\\|");

		String nombre = campos[0];
		String camiseta = campos[1];

		equipo.setNombre(nombre);
		equipo.setCamiseta(camiseta);

		return equipo;
	}
}
