package com.jmbg.oldgloriescalendar.adapter;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.data.Jugador;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlantillaAdapter extends BaseAdapter {

	private final Activity actividad;
	private final Vector<Jugador> plantilla;

	public PlantillaAdapter(Activity actividad, Vector<Jugador> plantilla) {
		super();
		this.actividad = actividad;
		this.plantilla = plantilla;
	}

	@Override
	public int getCount() {
		return this.plantilla.size();
	}

	@Override
	public Object getItem(int position) {
		return this.plantilla.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.actividad.getLayoutInflater();
		View view = inflater
				.inflate(R.layout.elemento_lista_plantilla, null, true);

		TextView jugador = (TextView) view.findViewById(R.id.nombre_jugador);
		jugador.setText(this.plantilla.elementAt(position).getNombre());
		TextView goles = (TextView) view.findViewById(R.id.goles_jugador);
		goles.setText(" "+this.plantilla.elementAt(position).getGoles());
		TextView tarAma = (TextView) view.findViewById(R.id.tarjAma_jugador);
		tarAma.setText(" "+this.plantilla.elementAt(position).getTarjetasAmarillas());
		TextView tarRoj = (TextView) view.findViewById(R.id.tarjRoj_jugador);
		tarRoj.setText(" "+this.plantilla.elementAt(position).getTarjetasRojas());


		return view;

	}

}
