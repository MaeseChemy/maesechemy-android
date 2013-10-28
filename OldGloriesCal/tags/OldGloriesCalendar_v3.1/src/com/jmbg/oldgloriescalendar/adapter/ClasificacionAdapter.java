package com.jmbg.oldgloriescalendar.adapter;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.dao.entities.Clasificacion;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClasificacionAdapter extends BaseAdapter {

	private final Activity actividad;
	private final Vector<Clasificacion> clasificacion;

	public ClasificacionAdapter(Activity actividad, Vector<Clasificacion> plantilla) {
		super();
		this.actividad = actividad;
		this.clasificacion = plantilla;
	}

	@Override
	public int getCount() {
		return this.clasificacion.size();
	}

	@Override
	public Object getItem(int position) {
		return this.clasificacion.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.actividad.getLayoutInflater();
		View view = inflater
				.inflate(R.layout.elemento_lista_clasificacion, null, true);
		int max = this.clasificacion.size();
		
		if (position == 0){
			view.setBackgroundColor(Color.argb(200, 0, 255, 255));
		}else if (position > 0 && position <= 3){
			view.setBackgroundColor(Color.argb(200, 0, 255, 0));
		}else if (position > 3 && position <=5){
			view.setBackgroundColor(Color.argb(200, 255, 255, 0));
		}else if (position >= (max-2)){
			view.setBackgroundColor(Color.argb(200, 255, 0, 0));
		}

		
		Clasificacion clasAdapter = this.clasificacion.elementAt(position);
		
		TextView textoPosicion = (TextView) view.findViewById(R.id.texto_posicion_clasificacion);
		textoPosicion.setText(""+clasAdapter.getPosicion());
		TextView textoNombre = (TextView) view.findViewById(R.id.texto_nombre_equipo_clasificacion);
		textoNombre.setText(" "+clasAdapter.getNombre());
		TextView textoParJug = (TextView) view.findViewById(R.id.texto_par_jug_clasificacion);
		textoParJug.setText(" J:"+clasAdapter.getPartidoJugados());
		TextView textoParGan = (TextView) view.findViewById(R.id.texto_par_gan_clasificacion);
		textoParGan.setText(" G:"+clasAdapter.getPartidosGanados());
		TextView textoParPer = (TextView) view.findViewById(R.id.texto_par_per_clasificacion);
		textoParPer.setText(" P:"+clasAdapter.getPartidosPerdidos());
		TextView textoParEmp = (TextView) view.findViewById(R.id.texto_par_emp_clasificacion);
		textoParEmp.setText(" E:"+clasAdapter.getPartidosEmpatados());
		TextView textoGolFav = (TextView) view.findViewById(R.id.texto_goles_favor_clasificacion);
		textoGolFav.setText(" F:"+clasAdapter.getGolesFavor());
		TextView textoGolCon = (TextView) view.findViewById(R.id.texto_goles_contra_clasificacion);
		textoGolCon.setText(" C:"+clasAdapter.getGolesContra());
		TextView textoPuntos = (TextView) view.findViewById(R.id.texto_puntos_clasificacion);
		textoPuntos.setText(" "+clasAdapter.getPuntos());

		return view;

	}

}
