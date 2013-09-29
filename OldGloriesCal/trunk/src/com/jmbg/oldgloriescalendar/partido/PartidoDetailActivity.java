package com.jmbg.oldgloriescalendar.partido;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.data.Partido;
import com.jmbg.oldgloriescalendar.data.Weather;
import com.jmbg.oldgloriescalendar.util.Constantes;
import com.jmbg.oldgloriescalendar.util.TiempoHttpClient;
import com.jmbg.oldgloriescalendar.util.WeatherJSON;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class PartidoDetailActivity extends Activity {

	private Weather tiempo;
	private Partido partido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partido_detail);

		Bundle extras = getIntent().getExtras();
		partido = (Partido) extras.getSerializable("partido");

		this.setTitle(partido.getJornada());

		TabHost tabHost = (TabHost) findViewById(R.id.partido_detail_tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Partido");
		spec1.setContent(R.id.tab_partido);
		spec1.setIndicator("Partido");

		TabSpec spec2 = tabHost.newTabSpec("Tiempo");
		spec2.setIndicator("Tiempo");
		spec2.setContent(R.id.tab_tiempo);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		TextView textoJornada = (TextView) findViewById(R.id.fecha_textView);
		TextView textoCampo = (TextView) findViewById(R.id.campo_textView);
		TextView textoRival = (TextView) findViewById(R.id.rival_textView);
		TextView textoLocal = (TextView) findViewById(R.id.local_textView);

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm",
				Locale.US);
		String fechaPartido = df.format(partido.getFecha());

		textoJornada.setText("Fecha: " + fechaPartido);
		textoCampo.setText("Campo: " + partido.getLugar());
		textoRival.setText("Rival: " + partido.getOponente());
		textoLocal.setText("Local: " + (partido.isLocal() ? "Si" : "No"));

		JSONTiempoTask task = new JSONTiempoTask();
		task.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partido_detail, menu);
		return true;
	}

	private class JSONTiempoTask extends AsyncTask<Void, Void, String> {
		private ProgressDialog progressDialog;
		private TiempoHttpClient tiempoConsulta;
		private WeatherJSON parserJSON;

		protected String doInBackground(Void... params) {
			tiempoConsulta = new TiempoHttpClient();
			String res = tiempoConsulta.getDatosDeTiempo();
			Log.e(Constantes.TAG, res);
			return res;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PartidoDetailActivity.this);
			progressDialog.setMessage("Obteniendo tiempo...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.hide();
			parserJSON = new WeatherJSON();

			Map<Date, Weather> listaTiempo = new HashMap<Date, Weather>();
			try {
				listaTiempo = parserJSON.weatherFromWeatherJSON(result);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			Calendar fechaPartido = Calendar.getInstance();
			SimpleDateFormat dfday = new SimpleDateFormat("dd/MM/yyyy",
					Locale.US);
			try {
				fechaPartido.setTime(dfday.parse(dfday.format(partido
						.getFecha())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tiempo = listaTiempo.get(fechaPartido.getTime());
			asignarTiempoPartido(tiempoConsulta);
		}
	}

	public void asignarTiempoPartido(TiempoHttpClient tiempoConsulta) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		TextView textoFecha = (TextView) findViewById(R.id.fecha_tiempo_textView);
		//TextView textoDesc = (TextView) findViewById(R.id.descripcion_tiempo_textView);
		ImageView imagen = (ImageView) findViewById(R.id.icon_fecha_imageview);
		TextView textoTempMin = (TextView) findViewById(R.id.tempMin_tiempo_textView);
		TextView textoTempMax = (TextView) findViewById(R.id.tempMax_tiempo_textView);
		
		if (tiempo == null) {
			String fechaPartido = df.format(partido.getFecha());
			textoFecha.setText(fechaPartido +" - (Sin prevision)");
			//textoDesc.setText("Descripcion: Sin prevision");
			textoTempMin.setText("Temp. Min: -");
			textoTempMax.setText("Temp. Max: -");
		} else {
			String fechaPartido = df.format(tiempo.getFechaTiempo());
			textoFecha.setText(fechaPartido);
			//textoDesc.setText("Descripcion: " + tiempo.getDescripcion());
			textoTempMin.setText("Minima: " + tiempo.getTempMin());
			textoTempMax.setText("Maxima: " + tiempo.getTempMax());
			setImagenTiempoByIcon(tiempo.getIcon().substring(0, 2), imagen);
		}
	}

	private void setImagenTiempoByIcon(String icon, ImageView imagen) {
		if (icon.equals("01")) {
			imagen.setImageResource(R.drawable.ic_01);
		} else if (icon.equals("02")) {
			imagen.setImageResource(R.drawable.ic_02);
		} else if (icon.equals("03")) {
			imagen.setImageResource(R.drawable.ic_03);
		} else if (icon.equals("04")) {
			imagen.setImageResource(R.drawable.ic_04);
		} else if (icon.equals("09")) {
			imagen.setImageResource(R.drawable.ic_09);
		} else if (icon.equals("10")) {
			imagen.setImageResource(R.drawable.ic_10);
		} else if (icon.equals("11")) {
			imagen.setImageResource(R.drawable.ic_11);
		} else if (icon.equals("13")) {
			imagen.setImageResource(R.drawable.ic_13);
		} else if (icon.equals("50")) {
			imagen.setImageResource(R.drawable.ic_50);
		} else {
			imagen.setImageResource(R.drawable.ic_01);
		}
	}

}
