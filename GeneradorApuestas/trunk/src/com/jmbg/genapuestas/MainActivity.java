package com.jmbg.genapuestas;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jmbg.genapuestas.generadores.GeneradorEuromillones;
import com.jmbg.genapuestas.generadores.GeneradorPrimitiva;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity implements SensorEventListener {

	private ListView listaEuromillones;
	private ListView listaPrimitiva;

	private Spinner spinnerEuromillones;
	private Spinner spinnerPrimitiva;

	private Spinner spinnerTipoAleatoriedadEuromillones;
	private Spinner spinnerTipoAleatoriedadPrimitiva;

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	private List<Integer> numerosAleatorios;

	private enum TipoAleatoriedad {
		RANDOM_JAVA, RANDOM_RANDOM_ORG, RANDOM_ACELEROMETRO
	};

	private TipoAleatoriedad aleatoriedadEuromillones;
	private TipoAleatoriedad aleatoriedadPrimitiva;

	private Vector<Apuesta> apuestas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Drawable background = getResources()
				.getDrawable(R.drawable.fondo_bolas);
		background.setAlpha(50);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		aleatoriedadEuromillones = TipoAleatoriedad.RANDOM_JAVA;
		aleatoriedadPrimitiva = TipoAleatoriedad.RANDOM_JAVA;

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

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.numeros_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		this.spinnerEuromillones = (Spinner) findViewById(R.id.numero_apuestas_euromillones_spinner);
		this.spinnerEuromillones.setAdapter(adapter);

		this.spinnerPrimitiva = (Spinner) findViewById(R.id.numero_apuestas_primitiva_spinner);
		this.spinnerPrimitiva.setAdapter(adapter);

		ArrayAdapter<CharSequence> adapterAleatoriedad = ArrayAdapter
				.createFromResource(this, R.array.tipo_aleatoriedad,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		this.spinnerTipoAleatoriedadEuromillones = (Spinner) findViewById(R.id.tipo_aleatoriedad_euromillones_spinner);
		this.spinnerTipoAleatoriedadEuromillones
				.setAdapter(adapterAleatoriedad);

		this.spinnerTipoAleatoriedadPrimitiva = (Spinner) findViewById(R.id.tipo_aleatoriedad_primitiva_spinner);
		this.spinnerTipoAleatoriedadPrimitiva.setAdapter(adapterAleatoriedad);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private TipoAleatoriedad verificarTipoAleatoriedadEuromillones() {
		String tipoApuesta = this.spinnerTipoAleatoriedadEuromillones
				.getSelectedItem().toString();
		if (tipoApuesta.equals("Normal")) {
			return TipoAleatoriedad.RANDOM_JAVA;
		} else if (tipoApuesta.equals("Random.org")) {
			return TipoAleatoriedad.RANDOM_RANDOM_ORG;
		} else if (tipoApuesta.equals("Acelerometro")) {
			return TipoAleatoriedad.RANDOM_ACELEROMETRO;
		} else {
			return TipoAleatoriedad.RANDOM_JAVA;
		}
	}

	private TipoAleatoriedad verificarTipoAleatoriedadPrimitiva() {
		String tipoApuesta = this.spinnerTipoAleatoriedadPrimitiva
				.getSelectedItem().toString();
		if (tipoApuesta.equals("Normal")) {
			return TipoAleatoriedad.RANDOM_JAVA;
		} else if (tipoApuesta.equals("Random.org")) {
			return TipoAleatoriedad.RANDOM_RANDOM_ORG;
		} else if (tipoApuesta.equals("Acelerometro")) {
			return TipoAleatoriedad.RANDOM_ACELEROMETRO;
		} else {
			return TipoAleatoriedad.RANDOM_JAVA;
		}
	}

	public void generarApuestaEuromillones(View view) {
		this.numerosAleatorios = new ArrayList<Integer>();
		aleatoriedadEuromillones = verificarTipoAleatoriedadEuromillones();
		switch (aleatoriedadEuromillones) {
		case RANDOM_ACELEROMETRO:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_GAME);
			GenNumAlTask task = new GenNumAlTask(Constantes.ID_EUROMILLONES);
			task.execute();
			break;
		case RANDOM_JAVA:
			generarApuestaEuromillonesFinish(TipoAleatoriedad.RANDOM_JAVA);
			break;
		case RANDOM_RANDOM_ORG:
			generarApuestaEuromillonesFinish(TipoAleatoriedad.RANDOM_RANDOM_ORG);
			break;
		default:
			break;

		}
	}

	public void generarApuestaEuromillonesFinish(TipoAleatoriedad aleatoriedad) {
		mSensorManager.unregisterListener(this);
		int numApuest = Integer.parseInt(this.spinnerEuromillones
				.getSelectedItem().toString());

		apuestas = new Vector<Apuesta>();
		for (int i = 0; i < numApuest; i++) {
			Apuesta apuesta;
			switch (aleatoriedad) {
			case RANDOM_ACELEROMETRO:
				apuesta = new Apuesta(Constantes.ID_EUROMILLONES,
						numerosAleatorios);
				break;
			case RANDOM_JAVA:
				apuesta = new Apuesta(Constantes.ID_EUROMILLONES);
				break;
			case RANDOM_RANDOM_ORG:
				GenNumAlRandomTask taskRan = new GenNumAlRandomTask(
						Constantes.ID_EUROMILLONES, numApuest);
				taskRan.execute();
				apuesta = null;
				return;
			default:
				apuesta = new Apuesta(Constantes.ID_EUROMILLONES);
				break;
			}
			if (apuesta != null)
				apuestas.add(apuesta);
		}
		this.listaEuromillones.setVisibility(View.VISIBLE);
		ApuestaAdapter adapterAp = new ApuestaAdapter(MainActivity.this,
				apuestas);
		this.listaEuromillones.setAdapter(adapterAp);

	}

	public void generarApuestaPrimitiva(View view) {
		this.numerosAleatorios = new ArrayList<Integer>();
		aleatoriedadPrimitiva = verificarTipoAleatoriedadPrimitiva();
		switch (aleatoriedadPrimitiva) {
		case RANDOM_ACELEROMETRO:
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_GAME);
			GenNumAlTask task = new GenNumAlTask(Constantes.ID_PRIMITIVA);
			task.execute();
			break;
		case RANDOM_JAVA:
			generarApuestaPrimitivaFinish(TipoAleatoriedad.RANDOM_JAVA);
			break;
		case RANDOM_RANDOM_ORG:
			generarApuestaPrimitivaFinish(TipoAleatoriedad.RANDOM_RANDOM_ORG);
			break;
		default:
			break;
		}
	}

	public void generarApuestaPrimitivaFinish(TipoAleatoriedad aleatoriedad) {
		mSensorManager.unregisterListener(this);
		int numApuest = Integer.parseInt(this.spinnerPrimitiva
				.getSelectedItem().toString());
		GenNumAlRandomTask taskRan = null;
		apuestas = new Vector<Apuesta>();
		for (int i = 0; i < numApuest; i++) {
			Apuesta apuesta;
			switch (aleatoriedad) {
			case RANDOM_ACELEROMETRO:
				apuesta = new Apuesta(Constantes.ID_PRIMITIVA,
						numerosAleatorios);
				break;
			case RANDOM_JAVA:
				apuesta = new Apuesta(Constantes.ID_PRIMITIVA);
				break;
			case RANDOM_RANDOM_ORG:
				taskRan = new GenNumAlRandomTask(Constantes.ID_PRIMITIVA,
						numApuest);
				taskRan.execute();

				apuesta = null;
				return;
			default:
				apuesta = new Apuesta(Constantes.ID_PRIMITIVA);
				break;
			}
			if (apuesta != null)
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
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];

			// int result = Math.abs((int) ((x * y) - z));
			int result = Math.abs(functionZ(x, y, z));

			if (!numerosAleatorios.contains(result)) {
				Log.d("Generador", "Numeros generado: " + result);
				numerosAleatorios.add(result);
			}
			break;
		}

	}

	private int functionZ(float a, float b, float c) {
		int x = (int) ((((b - a) * c) / 2) + (((b + a)) / 2));
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
				if (isCancelled())
					break;
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
				generarApuestaEuromillonesFinish(TipoAleatoriedad.RANDOM_ACELEROMETRO);
				break;
			case Constantes.ID_PRIMITIVA:
				generarApuestaPrimitivaFinish(TipoAleatoriedad.RANDOM_ACELEROMETRO);
				break;
			}
		}
	}

	public class GenNumAlRandomTask extends AsyncTask<Void, Integer, Void> {
		private ProgressDialog progressDialog;
		private int idApuesta;
		private int totApuestas;

		private List<List<Integer>> numeros;
		private List<List<Integer>> estrellas;

		public GenNumAlRandomTask(int idApuesta, int totApuestas) {
			this.idApuesta = idApuesta;
			this.totApuestas = totApuestas;

			this.numeros = new ArrayList<List<Integer>>();
			this.estrellas = new ArrayList<List<Integer>>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			switch (this.idApuesta) {
			case Constantes.ID_EUROMILLONES:
				for (int i = 0; i < this.totApuestas; i++) {
					numeros.add(GeneradorEuromillones.generarNumerosRest());
					estrellas.add(GeneradorEuromillones.generarEstrellasRest());
					publishProgress(((i+1) * 100) / this.totApuestas);
				}

				break;
			case Constantes.ID_PRIMITIVA:
				for (int i = 0; i < this.totApuestas; i++) {
					numeros.add(GeneradorPrimitiva.generarNumerosRest());
					publishProgress(((i+1) * 100) / this.totApuestas);
				}
				break;
			}
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("Solicitando numeros a random.org");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		protected void onPostExecute(Void result) {
			progressDialog.hide();
			progressDialog.dismiss();
			switch (this.idApuesta) {
			case Constantes.ID_EUROMILLONES:
				for (int i = 0; i < this.totApuestas; i++) {
					apuestas.add(new Apuesta(Constantes.ID_EUROMILLONES,
							numeros.get(i), estrellas.get(i)));
				}
				listaEuromillones.setVisibility(View.VISIBLE);
				ApuestaAdapter adapterEur = new ApuestaAdapter(
						MainActivity.this, apuestas);
				listaEuromillones.setAdapter(adapterEur);
				break;
			case Constantes.ID_PRIMITIVA:
				for (int i = 0; i < this.totApuestas; i++) {
					apuestas.add(new Apuesta(Constantes.ID_EUROMILLONES,
							numeros.get(i), new ArrayList<Integer>()));
				}
				listaPrimitiva.setVisibility(View.VISIBLE);
				ApuestaAdapter adapterPri = new ApuestaAdapter(
						MainActivity.this, apuestas);
				listaPrimitiva.setAdapter(adapterPri);

				break;
			}
		}
	}

}
