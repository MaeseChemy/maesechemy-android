package com.jmbg.loteriasgmv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.util.ActivityElement;
import com.jmbg.loteriasgmv.util.BetActivityElement;
import com.jmbg.loteriasgmv.util.Constantes;
import com.jmbg.loteriasgmv.util.LogManager;
import com.jmbg.loteriasgmv.util.ParticipantsActivityElement;
import com.jmbg.loteriasgmv.util.PotActivityElement;
import com.jmbg.loteriasgmv.util.PriceActivityElement;
import com.jmbg.loteriasgmv.view.adapter.GridAdapter;
import com.jmbg.loteriasgmv.ws.HTTPRegId;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private LogManager LOG = LogManager.getLogger(this.getClass());

	private List<ActivityElement> mainGridElements;

	private GoogleCloudMessaging gcm;
	private String regid;

	private int intentos;
	private final static int MAX_INTENTOS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.mainGridElements = new ArrayList<ActivityElement>();

		// Actividades principales
		ActivityElement bets = new BetActivityElement(this);
		ActivityElement awards = new PriceActivityElement(this);
		ActivityElement pot = new PotActivityElement(this);
		ActivityElement participants = new ParticipantsActivityElement(this);

		this.mainGridElements.add(bets);
		this.mainGridElements.add(awards);
		this.mainGridElements.add(pot);
		this.mainGridElements.add(participants);

		GridAdapter adapeter = new GridAdapter(this, this.mainGridElements);
		GridView gridview = (GridView) findViewById(R.id.gridButtons);

		gridview.setAdapter(adapeter);

		//
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				ActivityElement activity = (ActivityElement) parent
						.getAdapter().getItem(position);

				if (activity != null) {
					Intent launchIntent = activity.getIntent();
					if (launchIntent != null)
						startActivity(launchIntent);
					else
						Toast.makeText(
								MainActivity.this,
								"Lanzando categoria: "
										+ activity.getActivityName(),
								Toast.LENGTH_SHORT).show();
				}

			}

		});

		gcm = GoogleCloudMessaging.getInstance(this);
		regid = getRegistrationId(this);
		// Registramos el dispositivo
		discoverDevice();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void discoverDevice() {
		if (intentos <= MAX_INTENTOS) {
			if (regid == null || regid.equals("")) {
				Toast.makeText(this,
						"Activando notificaciones: Itento: " + intentos,
						Toast.LENGTH_LONG).show();
				RegisterGCM registerGCM = new RegisterGCM();
				registerGCM.execute(true);
			}else{
				try {
					String newRegid = gcm.register(Constantes.SENDER_ID);
					if(!regid.equals(newRegid)){
						Toast.makeText(this,
								"Cambio de Registration ID: Itento: " + intentos,
								Toast.LENGTH_LONG).show();
						RegisterGCM registerGCM = new RegisterGCM();
						registerGCM.execute(true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(this,
					"No se pudo realizar la accion, intentelo de nuevo...",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(boolean registration, String regid) {
		HTTPRegId regIdActions = new HTTPRegId();
		if (registration)
			regIdActions.doRegisterRegId(getDeviceImei(), regid);
		else {
			regIdActions.doUnRegisterRegId(getDeviceImei(), regid);
		}
	}

	private String getDeviceImei() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
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

	public class RegisterGCM extends AsyncTask<Boolean, Void, String> {
		private boolean discoverOk;

		@Override
		protected String doInBackground(Boolean... params) {
			boolean doRegister = params[0];
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
				}
				if (doRegister) {
					regid = gcm.register(Constantes.SENDER_ID);
					sendRegistrationIdToBackend(true, regid);
					LOG.debug("["
							+ RegisterGCM.class.getName()
							+ ".doInBackground] Device registered, registration ID="
							+ regid);
					msg = "Activacion de notificaciones correcto";
				} else {
					String oldRegid = regid;
					regid = "";
					sendRegistrationIdToBackend(false, oldRegid);
					gcm.unregister();
					Log.d(Constantes.TAG,
							"["
									+ RegisterGCM.class.getName()
									+ ".doInBackground] Device imregistered, registration ID="
									+ oldRegid);
					msg = "Desactivacion de notificaciones correcto";

				}

				storeRegistrationId(MainActivity.this, regid);
				discoverOk = true;
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				Log.e(Constantes.TAG, "[" + RegisterGCM.class.getName()
						+ ".doInBackground] Error =" + msg);
				discoverOk = false;
				// If there is an error, don't just keep trying to register.
				// Require the user to click a button again, or perform
				// exponential back-off.
			}
			return msg;
		}

		@Override
		protected void onPostExecute(String msg) {
			// mDisplay.append(msg + "\n");
			Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
			if (!discoverOk) {
				intentos++;
				discoverDevice();
			} else {
				intentos = 0;
			}
		}
	}

}
