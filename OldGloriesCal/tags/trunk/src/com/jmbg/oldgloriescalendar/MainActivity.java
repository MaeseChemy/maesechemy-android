package com.jmbg.oldgloriescalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.jmbg.oldgloriescalendar.equipo.EquiposActivity;
import com.jmbg.oldgloriescalendar.mapa.MapaActivity;
import com.jmbg.oldgloriescalendar.partido.PartidosActivity;
import com.jmbg.oldgloriescalendar.planitlla.PlantillaActivity;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Actividad principal de la aplicacion de calendario de partidos.
 * 
 * @author jmbg
 */
public class MainActivity extends Activity {

	private boolean notificaciones;
	private final int CREATE_ALARM = 1234567;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Constantes.TAG, "["+MainActivity.class.getName()+".onCreate] Creando actividad principal...");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Drawable background = getResources().getDrawable(
				R.drawable.ic_background);
		background.setAlpha(50);
		
		iniciarPreferencias();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onCreateOptionsMenu] Creando menus de la actividad principal principal...");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Opcion seleccionada en el menu");
		switch (item.getItemId()) {
		case R.id.main_config:
			Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Main Config");
			abrirPreferencias(null);
			return true;
		default:
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Finalizada la actividad "+ requestCode);
		if (requestCode == Constantes.BACK_PREF_MAIN) {
			iniciarPreferencias();
		}
	}

	public void abrirPartidos(View view) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".abrirPartidos] Accediendo a listado de partidos...");
		Intent iPartidos = new Intent(this, PartidosActivity.class);
		startActivity(iPartidos);
	}

	public void abrirEquipos(View view) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".abrirEquipos] Accediendo a listado de equipos...");
		Intent iEquipos = new Intent(this, EquiposActivity.class);
		startActivity(iEquipos);
	}

	public void mostrarComoLlegar(View view) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".mostrarComoLlegar] Accediendo al mapa...");
		Intent iMap = new Intent(this, MapaActivity.class);
		startActivity(iMap);
	}

	private void abrirPreferencias(View view) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".abrirPreferencias] Abriendo preferencias de la actividad principal...");
		Intent iPrefPart = new Intent(this, PreferenciasMainActivity.class);
		startActivityForResult(iPrefPart, Constantes.BACK_PREF_MAIN);
	}
	
	public void abrirPlantilla(View view) {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".abrirPlantilla] Accediendo a plantilla...");
		Intent iPlantilla = new Intent(this, PlantillaActivity.class);
		startActivity(iPlantilla);
	}
	
	/**
	 * Metodo de arrancado de preferencias. Se da valor a la variable notificaciones para saber si es necesario
	 * activar/desactivar el servicio de notificaciones que controlan las fechas de partidos para avisar al usuario.
	 */
	private void iniciarPreferencias() {
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Iniciando preferencias principales de la app...");
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		notificaciones = pref.getBoolean("notificaciones", false);
		Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Valor de notificaciones = "+notificaciones);
		Calendar cur_cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);
		String fechaStr = df.format(new Date());
		try {
			cur_cal.setTime(df.parse(fechaStr));
		} catch (ParseException e) {
			Log.e(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Error al parsear la fecha = "+fechaStr+". ["+e.getMessage()+"]");
		}
		
		Intent intent = new Intent(this, ServicioFechaPartido.class);
		PendingIntent pintent = PendingIntent.getService(this, CREATE_ALARM,
				intent, 0);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		if (notificaciones) {
			Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Activamos el servicio de notificaciones de la app...");
			// Arrancamos el servicio de verificacion de partidos
			alarm.setRepeating(AlarmManager.RTC_WAKEUP,
					cur_cal.getTimeInMillis(), Constantes.INTERVALO_REP_ALARMA_PARTIDO, pintent);
		} else {
			Log.d(Constantes.TAG, "["+MainActivity.class.getName()+".onOptionsItemSelected] Desactivamos el servicio de notificaciones de la app...");
			// Paramos el servicio
			alarm.cancel(pintent);
		}
	}

}
