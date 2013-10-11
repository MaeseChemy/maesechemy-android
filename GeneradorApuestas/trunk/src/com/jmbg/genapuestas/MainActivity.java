package com.jmbg.genapuestas;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jmbg.genapuestas.utils.Constantes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity implements SensorEventListener {

	private ListView listaEuromillones;
	private ListView listaPrimitiva;

	private Spinner spinnerEuromillones;
	private Spinner spinnerPrimitiva;

	private CheckBox checkAletoriedadEuromillones;
	private CheckBox checkAletoriedadPrimitiva;

	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private List<Integer> numerosAleatorios;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Drawable background = getResources()
				.getDrawable(R.drawable.fondo_bolas);
		background.setAlpha(50);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager	.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
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
		this.listaPrimitiva = (ListView) findViewById(R.id.list_primitiva);
		this.checkAletoriedadEuromillones = (CheckBox) findViewById(R.id.check_aletoriedad_euromillones);
		this.checkAletoriedadPrimitiva = (CheckBox) findViewById(R.id.check_aletoriedad_primitiva);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.numeros_array,
				android.R.layout.simple_spinner_item);
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

	public void generarApuestaEuromillones(View view) {
		this.numerosAleatorios = new ArrayList<Integer>();
		if(this.checkAletoriedadEuromillones.isChecked()){
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
			GenNumAlTask task = new GenNumAlTask(Constantes.ID_EUROMILLONES);
			task.execute();
		}else{
			generarApuestaEuromillonesFinish(false);
		}
	
	}
	public void generarApuestaEuromillonesFinish(boolean move) {

			mSensorManager.unregisterListener(this);
		
		int numApuest = Integer.parseInt(this.spinnerEuromillones
				.getSelectedItem().toString());

		Vector<Apuesta> apuestas = new Vector<Apuesta>();
		for (int i = 0; i < numApuest; i++) {
			Apuesta apuesta;
			if(move)
				apuesta = new Apuesta(Constantes.ID_EUROMILLONES, numerosAleatorios);
			else
				apuesta = new Apuesta(Constantes.ID_EUROMILLONES);
			apuestas.add(apuesta);
		}
		this.listaEuromillones.setVisibility(View.VISIBLE);
		ApuestaAdapter adapterAp = new ApuestaAdapter(MainActivity.this,
				apuestas);
		this.listaEuromillones.setAdapter(adapterAp);

	}

	public void generarApuestaPrimitiva(View view) {
		this.numerosAleatorios = new ArrayList<Integer>();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		if(this.checkAletoriedadPrimitiva.isChecked()){
			GenNumAlTask task = new GenNumAlTask(Constantes.ID_PRIMITIVA);
			task.execute();
		}else{
			generarApuestaPrimitivaFinish(false);
		}
	}
	public void generarApuestaPrimitivaFinish(boolean move) {
		mSensorManager.unregisterListener(this);
		int numApuest = Integer.parseInt(this.spinnerPrimitiva
				.getSelectedItem().toString());

		Vector<Apuesta> apuestas = new Vector<Apuesta>();
		for (int i = 0; i < numApuest; i++) {
			Apuesta apuesta;
			if(move)
				apuesta = new Apuesta(Constantes.ID_PRIMITIVA, numerosAleatorios);
			else
				apuesta = new Apuesta(Constantes.ID_PRIMITIVA);

			apuestas.add(apuesta);
		}
		this.listaPrimitiva.setVisibility(View.VISIBLE);
		ApuestaAdapter adapterAp = new ApuestaAdapter(MainActivity.this,
				apuestas);
		this.listaPrimitiva.setAdapter(adapterAp);

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];

			//int result = Math.abs((int) ((x * y) - z));
			int result = Math.abs(functionZ(x, y, z));
			
			if (!numerosAleatorios.contains(result)) {
				Log.d("Generador","Numeros generado: "+ result);
				numerosAleatorios.add(result);
			}
			break;
		}

	}
	
	private int functionZ(float a, float b, float c){
		int x = (int) ((((b - a)*c)/2) + (((b + a))/2));
		return x;
	}

	public class GenNumAlTask extends AsyncTask<Void, Integer, Void> {
		private ProgressDialog progressDialog;
		private int idApuesta;
		
		public GenNumAlTask(int idApuesta) {
			this.idApuesta = idApuesta;
		}

		@Override
		protected Void doInBackground(Void... params) {
			while (numerosAleatorios.size() < Constantes.NUMEROS_ALEATORIOS) {
				publishProgress((numerosAleatorios.size() * 100)
						/ Constantes.NUMEROS_ALEATORIOS);
				if (isCancelled()) break;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog
					.setMessage("Mueva el movil para generar numeros aleatorios");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}

		protected void onPostExecute(Void result) {
			progressDialog.hide();
			progressDialog.dismiss();
			switch (this.idApuesta) {
			case Constantes.ID_EUROMILLONES:
				generarApuestaEuromillonesFinish(true);
				break;
			case Constantes.ID_PRIMITIVA:
				generarApuestaPrimitivaFinish(true);
				break;
			}
		}
		

	}

}
