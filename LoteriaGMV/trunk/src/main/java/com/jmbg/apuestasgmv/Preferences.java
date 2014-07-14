package com.jmbg.apuestasgmv;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jmbg.apuestasgmv.control.ws.HTTPRegId;
import com.jmbg.apuestasgmv.utils.DeviceUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Preferences {

	private static Preferences instance;
	public static final float INITIAL_POSITION = 0;
	private SharedPreferences sharedPreferences;
	private PreferenceChangeListener mPreferenceListener = null;

	private Context context;

	private int intentos;
	private final static int MAX_INTENTOS = 3;

	protected Preferences() {
		this.context = ApuestasAppApplication.getInstance()
				.getApplicationContext();

		PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
		this.sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		mPreferenceListener = new PreferenceChangeListener();
		this.sharedPreferences
				.registerOnSharedPreferenceChangeListener(mPreferenceListener);
	}

	public static Preferences getInstance() {
		if (instance == null) {
			instance = new Preferences();
		}

		return instance;
	}

	// APP Preferences
	public boolean getAllowInitLoadWSData() {
		return sharedPreferences.getBoolean(
				context.getResources().getString(
						R.string.pref_init_load_ws_all_data), false);
	}

	public boolean getEnableNotifications() {
		return sharedPreferences.getBoolean(
				context.getResources().getString(R.string.pref_notifications),
				false);
	}

	public String getRegistrationID() {
		return sharedPreferences
				.getString(
						context.getResources().getString(
								R.string.pref_registration_id), "");
	}

	public void setRegistrationID(String registrationID) {
		sharedPreferences
				.edit()
				.putString(
						context.getResources().getString(
								R.string.pref_registration_id), registrationID)
				.commit();

	}

	// Handle preferences changes
	private class PreferenceChangeListener implements
			OnSharedPreferenceChangeListener {

		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {
			intentos = 0;
			if (key.equals(context.getResources().getString(
					R.string.pref_notifications)))
				updateNotifications();
		}
	}

	private void updateNotifications() {
		if (intentos <= MAX_INTENTOS) {
			RegisterGCM registerGCM = new RegisterGCM();
			registerGCM.execute(getEnableNotifications());
		} else {
			Toast.makeText(context,
					"No se pudo realizar la accion, intentelo de nuevo...",
					Toast.LENGTH_LONG).show();
		}
	}

	public class RegisterGCM extends AsyncTask<Boolean, Void, String> {
		private boolean discoverOk;
		private GoogleCloudMessaging gcm;
		private String regId;

		@Override
		protected String doInBackground(Boolean... params) {
			boolean doRegister = params[0];
			String msg = "";
			regId = getRegistrationID();
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}
				if (doRegister) {
					regId = gcm.register(Constants.SENDER_ID);
					boolean register = sendRegistrationIdToBackend(true, regId);
					if (register) {
						msg = "Activacion de notificaciones correcto";
						discoverOk = true;
					} else {
						msg = "Activacion de notificaciones incorrecto";
						regId = "";
						discoverOk = false;
					}

				} else {
					String oldRegid = regId;
					boolean unregister = sendRegistrationIdToBackend(false,
							oldRegid);
					if (unregister) {
						gcm.unregister();
						msg = "Desactivacion de notificaciones correcto";
						discoverOk = true;
						regId = "";
					} else {
						msg = "Desactivacion de notificaciones correcto";
						discoverOk = true;
					}

				}

				discoverOk = true;
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				discoverOk = false;
			}
			return msg;
		}

		@Override
		protected void onPostExecute(String msg) {
			if (!discoverOk) {
				updateNotifications();
				intentos++;
			} else {
				setRegistrationID(regId);
				Toast.makeText(context,
						"Actualizacion de notificaciones correcta...",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private boolean sendRegistrationIdToBackend(boolean registration,
			String regid) {
		HTTPRegId regIdActions = new HTTPRegId();
		if (registration)
			return regIdActions.doRegisterRegId(
					DeviceUtils.getDeviceImei(context), regid);
		else {
			return regIdActions.doUnRegisterRegId(
					DeviceUtils.getDeviceImei(context), regid);
		}
	}
}
