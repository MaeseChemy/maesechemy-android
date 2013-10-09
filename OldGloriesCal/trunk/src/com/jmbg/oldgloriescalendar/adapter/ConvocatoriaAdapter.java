package com.jmbg.oldgloriescalendar.adapter;

import java.util.Vector;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.data.Convocatoria;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConvocatoriaAdapter extends BaseAdapter {

	private final Activity actividad;
	private final Vector<Convocatoria> convocatoria;

	public ConvocatoriaAdapter(Activity actividad, Vector<Convocatoria> convocatoria) {
		super();
		this.actividad = actividad;
		this.convocatoria = convocatoria;
	}

	@Override
	public int getCount() {
		return this.convocatoria.size();
	}

	@Override
	public Object getItem(int position) {
		return this.convocatoria.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.actividad.getLayoutInflater();
		View view = inflater
				.inflate(R.layout.elemento_lista_convocatoria, null, true);

		TextView jugador = (TextView) view.findViewById(R.id.jugador);
		jugador.setText(this.convocatoria.elementAt(position).getJugador());

		TextView delegado = (TextView) view.findViewById(R.id.delegado);
		delegado.setText("Lleva delegado: " + ((this.convocatoria.elementAt(position).getDelegado())?"Si":"No"));
		
		TextView comentario = (TextView) view.findViewById(R.id.comentario);
		comentario.setText("Comentario: "+ this.convocatoria.elementAt(position).getComentario());
		
		return view;

	}

}
