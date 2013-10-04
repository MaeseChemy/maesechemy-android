package com.jmbg.oldgloriescalendar.adapter;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.data.Equipo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EquiposAdapter extends BaseAdapter {

	private final Activity actividad;
	private final Vector<Equipo> equipos;

	public EquiposAdapter(Activity actividad, Vector<Equipo> equipos) {
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
		equipo.setText(this.equipos.elementAt(position).getNombre());

		// TextView subtexto = (TextView)view.findViewById(R.id.subtexto);
		// subtexto.setText(this.equipos.elementAt(position));

		String camiseta = this.equipos.elementAt(position).getCamiseta();
		ImageView imagenCamiseta = (ImageView) view
				.findViewById(R.id.icono_camiseta);

		if (camiseta.equals("Azul")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_azul);
		} else if (camiseta.equals("Negra")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_negra);
		} else if (camiseta.equals("Morada")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_morada);
		} else if (camiseta.equals("Blanca")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_blanca);
		} else if (camiseta.equals("Amarilla")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_amarilla);
		} else if (camiseta.equals("Verde")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_verde);
		} else if (camiseta.equals("Blanca-Azul")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_blanca_azul);
		} else if (camiseta.equals("Blanca-Verde")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_blanca_verde);
		} else if (camiseta.equals("Negro-Morado")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_negro_morado);
		} else if (camiseta.equals("Blanca-Roja")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_blanca_roja);
		} else if (camiseta.equals("Blanca-Negra")) {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_blanca_negra);
		} else {
			imagenCamiseta.setImageResource(R.drawable.ic_camiseta_roja);
		}

		return view;

	}

}
