package com.jmbg.oldgloriescalendar.ws.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jmbg.oldgloriescalendar.dao.entities.Convocatoria;

public class ConvocatoriaJSON {
	public final static String CONVOCATORIA_JUGADOR = "jugador";
	public final static String CONVOCATORIA_COMENTARIO = "comentario";
	public final static String CONVOCATORIA_DELEGADO = "delegado";
	public final static String WEATHER_CONDITIONS = "weather";


	public List<Convocatoria> convocatoriaFromJSON(String jornada, String json)
			throws JSONException {
		List<Convocatoria> listaConvocatoria = new ArrayList<Convocatoria>();
		JSONArray arrayList = new JSONArray(json);

		for (int i = 0; i < arrayList.length(); i++) {
			JSONObject objeto = arrayList.getJSONObject(i);
			String jugador = objeto.getString(CONVOCATORIA_JUGADOR);
			String comentario = objeto.getString(CONVOCATORIA_COMENTARIO);
			boolean delegado = (((objeto.getString(CONVOCATORIA_DELEGADO)).equals("0"))?false:true);
			Convocatoria convocatoria = new Convocatoria();
			convocatoria.setJornada(jornada);
			convocatoria.setJugador(jugador);
			convocatoria.setComentario(comentario);
			convocatoria.setDelegado(delegado);
			
			listaConvocatoria.add(convocatoria);
		}
		return listaConvocatoria;
	}
}
