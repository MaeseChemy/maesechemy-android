package com.jmbg.oldgloriescalendar.partido;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.json.JSONException;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.adapter.ConvocatoriaAdapter;
import com.jmbg.oldgloriescalendar.data.Convocatoria;
import com.jmbg.oldgloriescalendar.data.Partido;
import com.jmbg.oldgloriescalendar.data.SaveSharedPreference;
import com.jmbg.oldgloriescalendar.data.Weather;
import com.jmbg.oldgloriescalendar.login.LoginActivity;
import com.jmbg.oldgloriescalendar.util.Constantes;
import com.jmbg.oldgloriescalendar.util.LectorDatosLiga;
import com.jmbg.oldgloriescalendar.util.PeticionConvocatoria;
import com.jmbg.oldgloriescalendar.util.TiempoHttpClient;
import com.jmbg.oldgloriescalendar.util.WeatherJSON;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class PartidoDetailActivity extends ListActivity {

	private Weather tiempo;
	private Partido partido;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partido_detail);

		Bundle extras = getIntent().getExtras();
		partido = (Partido) extras.getSerializable("partido");
		
		username = SaveSharedPreference.getUserName(PartidoDetailActivity.this);
		if(username.length() == 0){
			Intent iLogin = new Intent(this, LoginActivity.class);
			startActivity(iLogin);
		}

		this.setTitle(partido.getJornada());

		TabHost tabHost = (TabHost) findViewById(R.id.partido_detail_tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Partido");
		spec1.setContent(R.id.tab_partido);
		spec1.setIndicator("Partido");

		TabSpec spec2 = tabHost.newTabSpec("Tiempo");
		spec2.setIndicator("Tiempo");
		spec2.setContent(R.id.tab_tiempo);

		TabSpec spec3 = tabHost.newTabSpec("Convocatoria");
		spec3.setIndicator("Convocatoria");
		spec3.setContent(R.id.tab_convocatoria);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);

		TextView textoJornada = (TextView) findViewById(R.id.fecha_textView);
		TextView textoCampo = (TextView) findViewById(R.id.campo_textView);
		TextView textoRival = (TextView) findViewById(R.id.rival_textView);
		TextView textoLocal = (TextView) findViewById(R.id.local_textView);
		RadioGroup formConvocatoria = (RadioGroup) findViewById(R.id.map_radio_group_convocatoria);
		
		if(username.equals(Constantes.INVITADO))
			formConvocatoria.setVisibility(View.GONE);

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm",
				Locale.US);
		SimpleDateFormat dfday = new SimpleDateFormat("dd/MM/yyyy",
				Locale.US);
		
		String fechaPartido = df.format(partido.getFecha());
		textoJornada.setText("Fecha: " + fechaPartido);
		textoCampo.setText("Campo: " + partido.getLugar());
		textoRival.setText("Rival: " + partido.getOponente());
		textoLocal.setText("Local: " + (partido.isLocal() ? "Si" : "No"));
		
		String fechaPartidoTiempo = dfday.format(partido.getFecha());
		TextView textoFecha = (TextView) findViewById(R.id.fecha_tiempo_textView);
		TextView textoTempMin = (TextView) findViewById(R.id.tempMin_tiempo_textView);
		TextView textoTempMax = (TextView) findViewById(R.id.tempMax_tiempo_textView);
		textoFecha.setText(fechaPartidoTiempo + " - (Sin prevision)");
		// textoDesc.setText("Descripcion: Sin prevision");
		textoTempMin.setText("Temp. Min: -");
		textoTempMax.setText("Temp. Max: -");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partido_detail, menu);
		return true;
	}
	
	public void refrescarTiempo(View view){
		obtenerPrevisionTiempo();
	}
	public void refrescarConvocados(View view){
		obtenerConvocados();
	}
	public void anadirConvocatoria(View view) {
		JSONAnadirConvocatoria task = new JSONAnadirConvocatoria();
		task.execute();
	}
	
	public void borrarConvocatoria(View view){
		JSONBorrarConvocatoria task = new JSONBorrarConvocatoria();
		task.execute();
	}

	public void rellenarConvocatoria(List<Convocatoria> convocatoria) {
		Vector<Convocatoria> listConvocados = new Vector<Convocatoria>();

		for (Convocatoria conv : convocatoria) {
			listConvocados.add(conv);
		}
		ConvocatoriaAdapter adapterConv = new ConvocatoriaAdapter(this,
				listConvocados);
		setListAdapter(adapterConv);
	}
	
	private void obtenerPrevisionTiempo(){
		JSONTiempoTask taskTiempo = new JSONTiempoTask();
		taskTiempo.execute();
	}
	private void obtenerConvocados(){
		JSONObtenerConvocatoria taskConv = new JSONObtenerConvocatoria();
		taskConv.execute();
	}		

	private class JSONTiempoTask extends AsyncTask<Void, Void, String> {
		private ProgressDialog progressDialog;
		private TiempoHttpClient tiempoConsulta;
		private WeatherJSON parserJSON;

		protected String doInBackground(Void... params) {
			tiempoConsulta = new TiempoHttpClient();
			String res = tiempoConsulta.getDatosDeTiempo();
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
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}

	public void asignarTiempoPartido(TiempoHttpClient tiempoConsulta) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		TextView textoFecha = (TextView) findViewById(R.id.fecha_tiempo_textView);
		ImageView imagen = (ImageView) findViewById(R.id.icon_fecha_imageview);
		TextView textoTempMin = (TextView) findViewById(R.id.tempMin_tiempo_textView);
		TextView textoTempMax = (TextView) findViewById(R.id.tempMax_tiempo_textView);

		if (tiempo != null) {
			String fechaPartido = df.format(tiempo.getFechaTiempo());
			textoFecha.setText(fechaPartido);
			// textoDesc.setText("Descripcion: " + tiempo.getDescripcion());
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

	private class JSONAnadirConvocatoria extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progressDialog;
		PeticionConvocatoria addConv;

		protected Boolean doInBackground(Void... params) {
			addConv = new PeticionConvocatoria();
			String jornada = partido.getJornada();
			EditText comentario = (EditText) findViewById(R.id.texto_comentario_partidoConvocatoria);
			String textoComentario = comentario.getText().toString();

			return addConv
					.addConvocatoria(jornada, username, textoComentario);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PartidoDetailActivity.this);
			progressDialog.setMessage("Realizando registro...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected void onPostExecute(Boolean result) {
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}
	
	private class JSONBorrarConvocatoria extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progressDialog;
		PeticionConvocatoria remConv;

		protected Boolean doInBackground(Void... params) {
			remConv = new PeticionConvocatoria();
			String jornada = partido.getJornada();

			return remConv
					.removeConvocatoria(jornada, username);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PartidoDetailActivity.this);
			progressDialog.setMessage("Borrando registro...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected void onPostExecute(Boolean result) {
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}

	private class JSONObtenerConvocatoria extends
			AsyncTask<Void, Void, List<Convocatoria>> {
		private ProgressDialog progressDialog;
		LectorDatosLiga lector;

		protected List<Convocatoria> doInBackground(Void... params) {
			lector = new LectorDatosLiga(PartidoDetailActivity.this);
			String jornada = partido.getJornada();

			return lector.leerConvocatoria(jornada);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PartidoDetailActivity.this);
			progressDialog.setMessage("Obteniendo convocados...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected void onPostExecute(List<Convocatoria> result) {
			rellenarConvocatoria(result);
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}
	

}
