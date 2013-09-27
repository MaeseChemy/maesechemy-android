package com.jmbg.oldgloriescalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.jmbg.oldgloriescalendar.equipo.EquiposActivity;
import com.jmbg.oldgloriescalendar.mapa.MapaActivity;
import com.jmbg.oldgloriescalendar.partido.PartidosActivity;
import com.jmbg.oldgloriescalendar.service.ServicioFechaPartido;
import com.jmbg.oldgloriescalendar.util.Constantes;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	private boolean notificaciones;
	private final int CREATE_ALARM = 1234567;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Drawable background = getResources().getDrawable(
				R.drawable.ic_background);

		// setting the opacity (alpha)
		background.setAlpha(50);

		iniciarPreferencias();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void abrirPartidos(View view) {
		Intent iPartidos = new Intent(this, PartidosActivity.class);
		startActivity(iPartidos);
	}

	public void abrirEquipos(View view) {
		Intent iEquipos = new Intent(this, EquiposActivity.class);
		startActivity(iEquipos);
	}

	public void mostrarComoLlegar(View view) {
		Intent iMap = new Intent(this, MapaActivity.class);
		startActivity(iMap);
	}

	private void abrirPreferencias(View view) {
		Intent iPrefPart = new Intent(this, PreferenciasMainActivity.class);
		startActivityForResult(iPrefPart, Constantes.BACK_PREF_MAIN);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_config:
			abrirPreferencias(null);
			return true;
		default:
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constantes.BACK_PREF_MAIN) {
			iniciarPreferencias();
		}
	}

	private void iniciarPreferencias() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		notificaciones = pref.getBoolean("notificaciones", false);
		Calendar cur_cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);
		String fechaStr = df.format(new Date());
		try {
			cur_cal.setTime(df.parse(fechaStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent intent = new Intent(this, ServicioFechaPartido.class);
		PendingIntent pintent = PendingIntent.getService(this, CREATE_ALARM,
				intent, 0);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		if (notificaciones) {
			// Arrancamos el servicio de verificacion de partidos
			alarm.setRepeating(AlarmManager.RTC_WAKEUP,
					cur_cal.getTimeInMillis(), 1 * 60 * 1000, pintent);
		} else {
			// Paramos el servicio
			alarm.cancel(pintent);
		}
	}

}
