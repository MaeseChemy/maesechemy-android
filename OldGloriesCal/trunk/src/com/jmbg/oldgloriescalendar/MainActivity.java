package com.jmbg.oldgloriescalendar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jmbg.oldgloriescalendar.config.PreferenciasMainActivity;
import com.jmbg.oldgloriescalendar.dao.entities.SaveSharedPreference;
import com.jmbg.oldgloriescalendar.util.Constantes;
import com.jmbg.oldgloriescalendar.view.ClasificacionActivity;
import com.jmbg.oldgloriescalendar.view.EquiposActivity;
import com.jmbg.oldgloriescalendar.view.LoginActivity;
import com.jmbg.oldgloriescalendar.view.MapaActivity;
import com.jmbg.oldgloriescalendar.view.PartidosActivity;
import com.jmbg.oldgloriescalendar.view.PlantillaActivity;
import com.jmbg.oldgloriescalendar.ws.HTTPRegId;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Actividad principal de la aplicacion de calendario de partidos.
 * 
 * @author jmbg
 */
public class MainActivity extends Activity {

	private String username;

	private boolean notificaciones;

	private GoogleCloudMessaging gcm;
	private String regid;

	static final String EXTRA_MESSAGE = "notif_message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".onCreate] Creando actividad principal...");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		username = SaveSharedPreference.getUserName(MainActivity.this);
		if (username.length() == 0) {
			Intent iLogin = new Intent(this, LoginActivity.class);
			startActivity(iLogin);
		}

		TextView nombreUser = (TextView) findViewById(R.id.nombreUsuario_main);
		nombreUser.setText("Bienvenido " + username);
		Drawable background = getResources().getDrawable(
				R.drawable.ic_background);
		background.setAlpha(50);

		iniciarPreferencias();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(Constantes.TAG,
				"["
						+ MainActivity.class.getName()
						+ ".onCreateOptionsMenu] Creando menus de la actividad principal principal...");

		if (!username.equals(Constantes.INVITADO))
			getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".onOptionsItemSelected] Opcion seleccionada en el menu");
		switch (item.getItemId()) {
		case R.id.main_config:
			Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
					+ ".onOptionsItemSelected] Main Config");
			abrirPreferencias(null);
			return true;
		case R.id.main_logout:
			Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
					+ ".onOptionsItemSelected] Saliendo...");
			SaveSharedPreference.setUserName(this, "");
			Intent iLogin = new Intent(this, LoginActivity.class);
			startActivity(iLogin);
		default:
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".onOptionsItemSelected] Finalizada la actividad "
				+ requestCode);
		if (requestCode == Constantes.BACK_PREF_MAIN) {
			iniciarPreferencias();
		}
	}

	public void abrirPartidos(View view) {
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".abrirPartidos] Accediendo a listado de partidos...");
		Intent iPartidos = new Intent(this, PartidosActivity.class);
		startActivity(iPartidos);
	}

	public void abrirEquipos(View view) {
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".abrirEquipos] Accediendo a listado de equipos...");
		Intent iEquipos = new Intent(this, EquiposActivity.class);
		startActivity(iEquipos);
	}

	public void mostrarComoLlegar(View view) {
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".mostrarComoLlegar] Accediendo al mapa...");
		Intent iMap = new Intent(this, MapaActivity.class);
		startActivity(iMap);
	}

	private void abrirPreferencias(View view) {
		Log.d(Constantes.TAG,
				"["
						+ MainActivity.class.getName()
						+ ".abrirPreferencias] Abriendo preferencias de la actividad principal...");
		Intent iPrefPart = new Intent(this, PreferenciasMainActivity.class);
		startActivityForResult(iPrefPart, Constantes.BACK_PREF_MAIN);
	}

	public void abrirPlantilla(View view) {
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".abrirPlantilla] Accediendo a plantilla...");
		Intent iPlantilla = new Intent(this, PlantillaActivity.class);
		startActivity(iPlantilla);
	}

	public void abrirClasificacion(View view) {
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".abrirClasificacion] Accediendo a clasificacion...");
		Intent iClasificacion = new Intent(this, ClasificacionActivity.class);
		startActivity(iClasificacion);
	}

	/**
	 * Metodo de arrancado de preferencias. Se da valor a la variable
	 * notificaciones para saber si es necesario activar/desactivar el servicio
	 * de notificaciones que controlan las fechas de partidos para avisar al
	 * usuario.
	 */
	private void iniciarPreferencias() {
		Log.d(Constantes.TAG,
				"["
						+ MainActivity.class.getName()
						+ ".onOptionsItemSelected] Iniciando preferencias principales de la app...");
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		notificaciones = pref.getBoolean("notificaciones", false);
		Log.d(Constantes.TAG, "[" + MainActivity.class.getName()
				+ ".onOptionsItemSelected] Valor de notificaciones = "
				+ notificaciones);
		Calendar cur_cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);
		String fechaStr = df.format(new Date());
		try {
			cur_cal.setTime(df.parse(fechaStr));
		} catch (ParseException e) {
			Log.e(Constantes.TAG, "[" + MainActivity.class.getName()
					+ ".onOptionsItemSelected] Error al parsear la fecha = "
					+ fechaStr + ". [" + e.getMessage() + "]");
		}

		gcm = GoogleCloudMessaging.getInstance(this);
		regid = getRegistrationId(this);
		if (notificaciones) {
			if (regid == null || regid.equals("")) {
				RegisterGCM registerGCM = new RegisterGCM();
				registerGCM.execute(true);
			}

		}else{
			if (regid != null && !regid.equals("")) {
				RegisterGCM registerGCM = new RegisterGCM();
				registerGCM.execute(false);
			}
		}

	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(Constantes.PROPERTY_REG_ID, "");
		if (registrationId == null || registrationId.equals("")) {
			Log.i(Constantes.TAG, "Registration not found.");
			return "";
		}
		return registrationId;
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constantes.PROPERTY_REG_ID, regId);
		editor.commit();
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(boolean registration, String regid) {
		HTTPRegId regIdActions = new HTTPRegId();
		if(registration)
			regIdActions.doRegisterRegId(this.username, regid);
		else{
			regIdActions.doUnRegisterRegId(this.username, regid);
		}
	}

	public class RegisterGCM extends AsyncTask<Boolean, Void, String> {
		@Override
		protected String doInBackground(Boolean... params) {
			boolean doRegister = params[0];
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
				}
				if(doRegister){
					regid = gcm.register(Constantes.SENDER_ID);
					sendRegistrationIdToBackend(true, regid);
					msg = "Device registered, registration ID=" + regid;
				}else{
					String oldRegid = regid;
					regid = "";
					sendRegistrationIdToBackend(false, oldRegid);
					msg = "Device registered, registration ID=" + regid;
					gcm.unregister();
				}
				
				storeRegistrationId(MainActivity.this, regid);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				// If there is an error, don't just keep trying to register.
				// Require the user to click a button again, or perform
				// exponential back-off.
			}
			return msg;
		}

        @Override
        protected void onPostExecute(String msg) {
           // mDisplay.append(msg + "\n");
        }
	}
}
