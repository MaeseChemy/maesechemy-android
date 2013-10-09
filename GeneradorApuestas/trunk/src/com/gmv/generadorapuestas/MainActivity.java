package com.gmv.generadorapuestas;


import java.util.Vector;

import com.gmv.generadorapuestas.utils.Constantes;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity  {
	
	private ListView listaEuromillones;
	private ListView listaPrimitiva;

	private Spinner spinnerEuromillones;
	private Spinner spinnerPrimitiva;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Drawable background = getResources().getDrawable(
				R.drawable.fondo_bolas);
		background.setAlpha(50);
		
		
		TabHost tabHost = (TabHost) findViewById(R.id.apuestas_tabHost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Euromillones");
		spec1.setIndicator("Euromillones");
		spec1.setContent(R.id.tab_euromillones);

		TabSpec spec2 = tabHost.newTabSpec("Primitiva");
		spec2.setIndicator("Primitiva");
		spec2.setContent(R.id.tab_primitiva);
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		
		
		this.listaEuromillones = (ListView) findViewById(R.id.list_euromillones);
		this.listaPrimitiva  = (ListView) findViewById(R.id.list_primitiva);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.numeros_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		this.spinnerEuromillones = (Spinner) findViewById(R.id.numero_apuestas_euromillones_spinner);
		this.spinnerEuromillones.setAdapter(adapter);
		
		this.spinnerPrimitiva = (Spinner) findViewById(R.id.numero_apuestas_primitiva_spinner);
		this.spinnerPrimitiva.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void generarApuestaEuromillones(View view){
		int numApuest = Integer.parseInt(this.spinnerEuromillones.getSelectedItem().toString());
		
		Vector<Apuesta> apuestas = new Vector<Apuesta>();
		for(int i = 0; i < numApuest; i++){
			Apuesta apuesta = new Apuesta(Constantes.ID_EUROMILLONES);
			apuestas.add(apuesta);
		}
		this.listaEuromillones.setVisibility(View.VISIBLE);
		ApuestaAdapter adapterAp = new ApuestaAdapter(MainActivity.this, apuestas);
		this.listaEuromillones.setAdapter(adapterAp);
		
	}
	
	public void generarApuestaPrimitiva(View view){
		int numApuest = Integer.parseInt(this.spinnerPrimitiva.getSelectedItem().toString());
		
		Vector<Apuesta> apuestas = new Vector<Apuesta>();
		for(int i = 0; i < numApuest; i++){
			Apuesta apuesta = new Apuesta(Constantes.ID_PRIMITIVA);
			apuestas.add(apuesta);
		}
		this.listaPrimitiva.setVisibility(View.VISIBLE);
		ApuestaAdapter adapterAp = new ApuestaAdapter(MainActivity.this, apuestas);
		this.listaPrimitiva.setAdapter(adapterAp);
		
	}
	
}
