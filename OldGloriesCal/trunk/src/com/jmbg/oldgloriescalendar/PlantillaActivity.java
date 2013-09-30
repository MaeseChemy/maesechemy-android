package com.jmbg.oldgloriescalendar;
import java.util.List;
import java.util.Vector;


import com.jmbg.oldgloriescalendar.adapter.PlantillaAdapter;
import com.jmbg.oldgloriescalendar.data.Jugador;
import com.jmbg.oldgloriescalendar.util.LectorPlantilla;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.app.ListActivity;
import android.app.ProgressDialog;

public class PlantillaActivity extends ListActivity {

	private Vector<Jugador> plantilla;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plantilla);
		// Show the Up button in the action bar.
		
		LectorPlantillaTask task = new LectorPlantillaTask();
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plantilla, menu);
		return true;
	}
	
	private class LectorPlantillaTask extends AsyncTask<Void, Void, List<Jugador>> {
		private ProgressDialog progressDialog;
		private LectorPlantilla lectorPlantilla;

		protected List<Jugador> doInBackground(Void... params) {
			lectorPlantilla = new LectorPlantilla();
			List<Jugador> jugadores = lectorPlantilla.obtenerJugadores();
			return jugadores;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PlantillaActivity.this);
			progressDialog.setMessage("Obteniendo plantilla...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected void onPostExecute(List<Jugador> result) {
			super.onPostExecute(result);
	
			plantilla = new Vector<Jugador>();
			List<Jugador> jugadoresHTTP = result;
			for (Jugador jugador : jugadoresHTTP) {
				plantilla.add(jugador);
			}
			PlantillaAdapter adapterPlantilla = new PlantillaAdapter(PlantillaActivity.this, plantilla);
			setListAdapter(adapterPlantilla);
			
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}
}
