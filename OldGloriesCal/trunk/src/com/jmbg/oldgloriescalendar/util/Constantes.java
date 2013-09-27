package com.jmbg.oldgloriescalendar.util;

import com.google.android.gms.maps.model.LatLng;

public class Constantes {
	public static final String TAG = "OGC";
	public static final String EQUIPO = "OLD GLORIES RELOADED";
	public static final String FICHERO_PARTIDOS = "listadoliga.txt";
	
	/*CONSTANTES PARA EL MAPA*/
	public static final LatLng CANTERA = new LatLng(40.339661, -3.762539);
	public static final String NOMBRE_POLIDEPORTIVO = "Polideportivo: La Cantera";
	public static final String DIRECCION_POLIDEPORTIVO = "Calle Arquitectura 6, Leganés, Madrid";
	public static final String PETICION_TIEMPO ="http://api.openweathermap.org/data/2.5/forecast/daily?q="+DIRECCION_POLIDEPORTIVO+"&mode=xml&units=metric&cnt=10";
	
	/*CONSTANTES PARA FILTRADO DE PARTIDOS*/
	public static final int LOCAL_TODO = 0;
	public static final int LOCAL_LOCAL = 1;
	public static final int LOCAL_VISITANTE = 2;
	public static final String TEXTO_LOCAL_TODO = "Todo";
	public static final String TEXTO_LOCAL_LOCAL = "Local";
	public static final String TEXTO_LOCAL_VISITANTE = "Visitante";

	public static final int HORA_TODAS= 0;
	public static final int HORA_CUATRO = 1;
	public static final int HORA_CINCO = 2;
	public static final int HORA_SEIS = 3;
	public static final int HORA_OCHO = 4;
	public static final String TEXTO_HORA_TODAS = "Todas";
	public static final String TEXTO_HORA_CUATRO = "16:00";
	public static final String TEXTO_HORA_CINCO = "17:15";
	public static final String TEXTO_HORA_SEIS = "18:30";
	public static final String TEXTO_HORA_OCHO = "20:00";

	public static final int CAMPO_TODOS= 0;
	public static final int CAMPO_FT2_M1 = 1;
	public static final int CAMPO_FT2_M2 = 2;
	public static final int CAMPO_F7_1 = 3;
	public static final int CAMPO_F7_2 = 4;
	public static final String TEXTO_CAMPO_TODOS = "Todos";
	public static final String TEXTO_CAMPO_FT2_M1 = "La Cantera FT2 M1";
	public static final String TEXTO_CAMPO_FT2_M2 = "La Cantera FT2 M2";
	public static final String TEXTO_CAMPO_F7_1 = "La Cantera F7 1";
	public static final String TEXTO_CAMPO_F7_2 = "La Cantera F7 2";
	
	
	/*CONSTANTES DE RESPUESTAS DE INTENTS*/
	public static final int BACK_PREF_MAIN = 0;
	public static final int BACK_PREF = 1;
	public static final int BACK_PREF_MAP = 2;

	/*CONSTANTES TIPO URI*/
	public static final int MAP_URI_GENERIC = 0;
	public static final int MAP_URI_GOOGLE_MAPS = 1;
	
	/*CONSTANTES DEL SERVICIO*/
	public static final double DIAS_AVISO_MAX = 2;
	public static final long INTERVALO_REP_ALARMA_PARTIDO = 60 * 60 * 1000;
	
}
