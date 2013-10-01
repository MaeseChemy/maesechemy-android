package com.jmbg.oldgloriescalendar.adapter;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.data.Jugador;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
		
		Jugador jugadorAdapter = this.plantilla.elementAt(position);
		TextView jugador = (TextView) view.findViewById(R.id.nombre_jugador);
		jugador.setText(jugadorAdapter.getNombre());
		TextView goles = (TextView) view.findViewById(R.id.goles_jugador);
		goles.setText(""+jugadorAdapter.getGoles());
		TextView tarAma = (TextView) view.findViewById(R.id.tarjAma_jugador);
		tarAma.setText(""+jugadorAdapter.getTarjetasAmarillas());
		TextView tarRoj = (TextView) view.findViewById(R.id.tarjRoj_jugador);
		tarRoj.setText(""+jugadorAdapter.getTarjetasRojas()+"");

		ImageView dorsal = (ImageView) view.findViewById(R.id.icono_dorsal);
		
		int valorDorsal = jugadorAdapter.getDorsal();
		switch (valorDorsal) {
		case 1:
			dorsal.setImageResource(R.drawable.ic_dorsal1);
			break;
		case 3:
			dorsal.setImageResource(R.drawable.ic_dorsal3);
			break;
		case 4:
			dorsal.setImageResource(R.drawable.ic_dorsal4);
			break;
		case 5:
			dorsal.setImageResource(R.drawable.ic_dorsal5);
			break;
		case 7:
			dorsal.setImageResource(R.drawable.ic_dorsal7);
			break;
		case 8:
			dorsal.setImageResource(R.drawable.ic_dorsal8);
			break;
		case 9:
			dorsal.setImageResource(R.drawable.ic_dorsal9);
			break;
		case 10:
			dorsal.setImageResource(R.drawable.ic_dorsal10);
			break;
		case 11:
			dorsal.setImageResource(R.drawable.ic_dorsal11);
			break;
		case 14:
			dorsal.setImageResource(R.drawable.ic_dorsal14);
			break;
		case 16:
			dorsal.setImageResource(R.drawable.ic_dorsal16);
			break;
		case 23:
			dorsal.setImageResource(R.drawable.ic_dorsal23);
			break;
		default:
			break;
		}
		return view;

	}

}
