package com.jmbg.oldgloriescalendar;

import java.text.SimpleDateFormat;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.data.Partido;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PartidosAdapter extends BaseAdapter{
	
	private final Activity actividad;
	private final Vector<Partido> partidos;
	
	public PartidosAdapter(Activity actividad, Vector<Partido> partidos) {
		super();
		this.actividad = actividad;
		this.partidos = partidos;
	}
	@Override
	public int getCount() {
		return this.partidos.size();
	}

	@Override
	public Object getItem(int position) {
		return this.partidos.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.elemento_lista, null, true);
		
		TextView jornada = (TextView)view.findViewById(R.id.jornada);
		jornada.setText(this.partidos.elementAt(position).getJornada());
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String fechaPartido = df.format(this.partidos.elementAt(position).getFecha());
		TextView fecha = (TextView)view.findViewById(R.id.fecha);
		fecha.setText(fechaPartido);
		
		ImageView imagenLocal = (ImageView)view.findViewById(R.id.icono_local);
		if(this.partidos.elementAt(position).isLocal())
			imagenLocal.setImageResource(R.drawable.ic_local);
		else
			imagenLocal.setImageResource(R.drawable.ic_visitante);
		return view;
		
	}

}
