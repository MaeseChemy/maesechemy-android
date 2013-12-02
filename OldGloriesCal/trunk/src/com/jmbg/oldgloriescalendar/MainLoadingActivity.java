package com.jmbg.oldgloriescalendar;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.dao.LigaDBSQLite;
import com.jmbg.oldgloriescalendar.dao.entities.Clasificacion;
import com.jmbg.oldgloriescalendar.dao.entities.Jugador;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.TextView;

public class MainLoadingActivity extends Activity {

	public enum Steps {
		INICIO, PARTIDOS, EQUIPOS, CLASIFICACION, PLANTILLA, FIN
	}

	private LigaDBSQLite liga;
	String textoCarga = "";
	public TextView elementLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_loading);

		Drawable background = getResources().getDrawable(
				R.drawable.ic_background);
		background.setAlpha(50);

		// Arrancamos la SQLite
		this.liga = new LigaDBSQLite(this, "DBCalendar", null, false);

		elementLoading = (TextView) findViewById(R.id.loadingStr);
		textoCarga = "Configurando base de datos...";
		setTextoCarga();
		// Iniciamos la carga

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				cargarDatos(Steps.INICIO);
			}
		}, 2000);

	}

	public void setTextoCarga(){
		elementLoading.setText(textoCarga);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main_loading, menu);
		return true;
	}

	private void cargarDatos(Steps inicio) {
		switch (inicio) {
		case INICIO:
			cargarDatos(Steps.PARTIDOS);
			break;
		case PARTIDOS:
			textoCarga = "Cargando partidos...";
			setTextoCarga();
			if (this.liga.listaPartidos().size() == 0) {
				this.liga.cargarPartidos();
			}
			cargarDatos(Steps.EQUIPOS);
			break;
		case EQUIPOS:
			textoCarga = "Cargando equipos...";
			setTextoCarga();
			if (this.liga.listaEquipos().size() == 0) {
				this.liga.cargarEquipos();
			}
			cargarDatos(Steps.CLASIFICACION);
			break;
		case CLASIFICACION:
			textoCarga = "Cargando Clasificacion...";
			setTextoCarga();
			if (this.liga.listaClasificacion().size() == 0) {
				LectorClasificacionTask task = new LectorClasificacionTask();
				task.execute();
			} else {
				cargarDatos(Steps.PLANTILLA);
			}
			break;
		case PLANTILLA:
			textoCarga = "Cargando Plantilla...";
			setTextoCarga();
			if (this.liga.listaJugadores().size() == 0) {
				LectorPlantillaTask task = new LectorPlantillaTask();
				task.execute();
			} else {
				cargarDatos(Steps.FIN);
			}
			break;
		case FIN:
			Intent mainIntent = new Intent(this, MainActivity.class);
			this.startActivity(mainIntent);
			this.finish();
			break;
		default:
			break;
		}
	}

	private class LectorClasificacionTask extends
			AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			liga.cargarClasificacion();
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			cargarDatos(Steps.PLANTILLA);
		}
	}

	private class LectorPlantillaTask extends
			AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			liga.cargarJugadores();
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			cargarDatos(Steps.FIN);
		}
	}

}
