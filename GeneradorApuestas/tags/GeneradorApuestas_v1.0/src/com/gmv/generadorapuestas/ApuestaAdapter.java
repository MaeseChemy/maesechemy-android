package com.gmv.generadorapuestas;

import java.util.List;
import java.util.Vector;

import com.gmv.generadorapuestas.utils.Constantes;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ApuestaAdapter extends BaseAdapter {

	private final Activity actividad;
	private final Vector<Apuesta> apuestas;

	public ApuestaAdapter(Activity actividad, Vector<Apuesta> apuestas) {
		super();
		this.actividad = actividad;
		this.apuestas = apuestas;
	}

	@Override
	public int getCount() {
		return this.apuestas.size();
	}

	@Override
	public Object getItem(int position) {
		return this.apuestas.elementAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.actividad.getLayoutInflater();
		View view = inflater
				.inflate(R.layout.elemento_lista_apuesta, null, true);

		LinearLayout layoutApuestas = (LinearLayout) view.findViewById(R.id.apuestas_layout);
		layoutApuestas.setVisibility(View.VISIBLE);
		
		Apuesta apuesta = this.apuestas.elementAt(position);
		layoutApuestas.addView(this.generarApuestaIndividual(apuesta));
		
		return view;
	}

	
	private LinearLayout generarApuestaIndividual(Apuesta apuesta){
		
		int tipoApuesta = apuesta.getTipoApuesta();
		List<Integer> numeros = apuesta.getNumeros();
		List<Integer> estrellas = apuesta.getEstrellas();

		LinearLayout layoutApuesta = new LinearLayout(actividad);
		layoutApuesta.setGravity(Gravity.CENTER);
		layoutApuesta.setOrientation(LinearLayout.VERTICAL);
		layoutApuesta.setBackgroundResource(R.drawable.box_clas);
		
		LinearLayout layoutNumeros = new LinearLayout(actividad);
		layoutNumeros.setGravity(Gravity.CENTER);
		layoutNumeros.setOrientation(LinearLayout.HORIZONTAL);
		for(int numero : numeros){
			TextView numeroView = new TextView(actividad);
			numeroView.setText(Integer.toString(numero));
			RadioGroup numeroContent = new RadioGroup(actividad);
			numeroContent.setGravity(Gravity.CENTER);
			switch (tipoApuesta) {
				case Constantes.ID_EUROMILLONES:
					numeroContent.setBackgroundResource(R.drawable.bola_azul);
				break;
				case Constantes.ID_PRIMITIVA:
					numeroContent.setBackgroundResource(R.drawable.bola_gris);
				break;
			}
			numeroContent.addView(numeroView);
			
			layoutNumeros.addView(numeroContent);
		}
		
		LinearLayout layoutEstrellas = new LinearLayout(actividad);
		layoutEstrellas.setGravity(Gravity.CENTER);
		layoutEstrellas.setOrientation(LinearLayout.HORIZONTAL);
		for(int estrella : estrellas){
			TextView estrellaView = new TextView(actividad);
			estrellaView.setText(Integer.toString(estrella));
			RadioGroup estrellaContent = new RadioGroup(actividad);
			estrellaContent.setGravity(Gravity.CENTER);
			estrellaContent.setBackgroundResource(R.drawable.estrella_amarilla);
			estrellaContent.addView(estrellaView);
			
			layoutEstrellas.addView(estrellaContent);
		}
		
		layoutApuesta.addView(layoutNumeros);
		layoutApuesta.addView(layoutEstrellas);
		
		return layoutApuesta;
	}

}
