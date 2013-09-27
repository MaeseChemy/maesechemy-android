package com.jmbg.oldgloriescalendar.adapter;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EquiposAdapter extends BaseAdapter {

	private final Activity actividad;
	private final Vector<String> equipos;

	public EquiposAdapter(Activity actividad, Vector<String> equipos) {
		super();
		this.actividad = actividad;
		this.equipos = equipos;
	}

	@Override
	public int getCount() {
		return this.equipos.size();
	}

	@Override
	public Object getItem(int position) {
		return this.equipos.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.actividad.getLayoutInflater();
		View view = inflater
				.inflate(R.layout.elemento_lista_equipo, null, true);

		TextView equipo = (TextView) view.findViewById(R.id.equipo);
		equipo.setText(this.equipos.elementAt(position));

		// TextView subtexto = (TextView)view.findViewById(R.id.subtexto);
		// subtexto.setText(this.equipos.elementAt(position));

		int round = (int) (Math.round(Math.random() * 5));
		ImageView imagenCamiseta = (ImageView) view
				.findViewById(R.id.icono_camiseta);
		switch (round) {
		case 0:
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_amarilla);
			break;
		case 1:
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_azul);
			break;
		case 2:
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_blanca);
			break;
		case 3:
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_negra);
			break;
		case 4:
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_roja);
			break;
		case 5:
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_verde);
			break;
		default:
			break;
		}

		return view;

	}

}
