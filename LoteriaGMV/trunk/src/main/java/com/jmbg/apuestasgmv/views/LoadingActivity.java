package com.jmbg.apuestasgmv.views;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jmbg.apuestasgmv.Constants;
import com.jmbg.apuestasgmv.Constants.TypeAppData;
import com.jmbg.apuestasgmv.IntentResults;
import com.jmbg.apuestasgmv.Preferences;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.ws.HTTPRegId;
import com.jmbg.apuestasgmv.control.ws.LectorDatosWS;
import com.jmbg.apuestasgmv.model.dao.BetDao;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.ParticipantDao;
import com.jmbg.apuestasgmv.model.dao.PotDao;
import com.jmbg.apuestasgmv.model.dao.PriceDao;
import com.jmbg.apuestasgmv.utils.DeviceUtils;
import com.jmbg.apuestasgmv.utils.LogManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingActivity extends Activity {

	private LogManager logger = LogManager.getLogger(this.getClass());
	private Preferences prefrerences;
	private int loadingProcess;
	private final static int DISCOVER_GCM = 0;
	private final static int GET_POTS = 1;
	private final static int GET_BETS = 2;
	private final static int GET_PARTICIPANT = 3;
	private final static int GET_PRICES = 4;
	private final static int START_APP = 5;

	private GoogleCloudMessaging gcm;
	private int intentos;
	private final static int MAX_INTENTOS = 3;
	private String regid;

	private LoadingHolder holder;

	private class LoadingHolder {
		public TextView loadingText;
	}

	/*
	 * ACTIVITY METHODS
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info("Create app");
		setContentView(R.layout.loading_activity);

		prefrerences = Preferences.getInstance();

		initActivityHolder();

		loadingProcess = -1;
		nextStep();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		logger.info("Destroy app");
	}

	@Override
	protected void onResume() {
		super.onResume();
		logger.info("Resume app");
	}

	@Override
	protected void onPause() {
		super.onPause();
		logger.info("Pause app");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		logger.debug("onActivityResult [requestCode=" + requestCode + "]"
				+ "[resultCode=" + resultCode + "]");

		switch (resultCode) {
		case IntentResults.CLOSE_APP:
			finish();
			return;
		}
	}

	private void initActivityHolder() {
		holder = new LoadingHolder();
		holder.loadingText = (TextView) findViewById(R.id.splash_text_loading);
	}

	private void setLoadingText(String text) {
		holder.loadingText.setText(text);
	}

	/*
	 * PROGRESS METHODS
	 */
	private void nextStep() {
		loadingProcess++;
		makeLoadingProcess();
	}

	private void makeLoadingProcess() {
		switch (loadingProcess) {
		case DISCOVER_GCM:
			discoverDevice();
			break;
		case GET_POTS:
			getPots();
			break;
		case GET_PRICES:
			getPrices();
			break;
		case GET_BETS:
			getBets();
			break;
		case GET_PARTICIPANT:
			getParticipants();
			break;
		case START_APP:
			startMainActivity();
			break;
		}
	}

	private void discoverDevice() {
		if (intentos <= MAX_INTENTOS) {
			String text = prefrerences.getEnableNotifications() ? "Activando notificaciones"
					: "Desactivando notificaciones";
			setLoadingText(text);
			RegisterGCM registerGCM = new RegisterGCM();
			registerGCM.execute(prefrerences.getEnableNotifications());
		} else {
			Toast.makeText(this,
					"No se pudo realizar la accion, intentelo de nuevo...",
					Toast.LENGTH_LONG).show();
			nextStep();
		}
	}

	private void getBets() {
		String text = prefrerences.getAllowInitLoadWSData() ? "Obteniedo apuestas"
				: "Verificando apuestas";
		setLoadingText(text);
		DataWSTask task = new DataWSTask(this, TypeAppData.BET,
				prefrerences.getAllowInitLoadWSData());
		task.execute();
	}

	private void getPots() {
		String text = prefrerences.getAllowInitLoadWSData() ? "Obteniedo Bote"
				: "Verificando bote";
		setLoadingText(text);
		DataWSTask task = new DataWSTask(this, TypeAppData.POT,
				prefrerences.getAllowInitLoadWSData());
		task.execute();
	}

	private void getParticipants() {
		String text = prefrerences.getAllowInitLoadWSData() ? "Obteniedo participantes"
				: "Verificando participantes";
		setLoadingText(text);
		DataWSTask task = new DataWSTask(this, TypeAppData.PARTICIPANT,
				prefrerences.getAllowInitLoadWSData());
		task.execute();
	}

	private void getPrices() {
		String text = prefrerences.getAllowInitLoadWSData() ? "Obteniedo premios"
				: "Verificando premios";
		setLoadingText(text);
		DataWSTask task = new DataWSTask(this, TypeAppData.PRICE,
				prefrerences.getAllowInitLoadWSData());
		task.execute();
	}

	private void startMainActivity() {
		Intent intent = new Intent();
		intent.setClass(LoadingActivity.this, HomeActivity.class);
		startActivityForResult(intent, IntentResults.DO_START_APP);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private boolean sendRegistrationIdToBackend(boolean registration,
			String regid) {
		HTTPRegId regIdActions = new HTTPRegId();
		if (registration)
			return regIdActions.doRegisterRegId(
					DeviceUtils.getDeviceImei(this), regid);
		else {
			return regIdActions.doUnRegisterRegId(
					DeviceUtils.getDeviceImei(this), regid);
		}
	}

	/*
	 * ASYNCTASKS
	 */
	public class RegisterGCM extends AsyncTask<Boolean, Void, String> {
		private boolean discoverOk;

		@Override
		protected String doInBackground(Boolean... params) {
			boolean doRegister = params[0];
			String msg = "";
			regid = prefrerences.getRegistrationID();
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging
							.getInstance(LoadingActivity.this);
				}
				if (doRegister) {
					regid = gcm.register(Constants.SENDER_ID);
					if (!regid.equals(prefrerences.getRegistrationID())) {
						boolean register = sendRegistrationIdToBackend(true,
								regid);
						if (register) {
							logger.debug("["
									+ RegisterGCM.class.getName()
									+ ".doInBackground] Device registered, registration ID="
									+ regid);
							msg = "Activacion de notificaciones correcto";
							discoverOk = true;
						} else {
							logger.debug("["
									+ RegisterGCM.class.getName()
									+ ".doInBackground] Device not registered, setting registration ID=null");
							msg = "Activacion de notificaciones incorrecto";
							regid = "";
							discoverOk = false;
						}
					}else{
						msg = "Activacion de notificaciones correcto";
						discoverOk = true;
					}

				} else {
					String oldRegid = regid;
					boolean unregister = sendRegistrationIdToBackend(false,
							oldRegid);
					if (unregister) {
						gcm.unregister();
						logger.debug("["
								+ RegisterGCM.class.getName()
								+ ".doInBackground] Device unregistered, registration ID="
								+ oldRegid);
						msg = "Desactivacion de notificaciones correcto";
						discoverOk = true;
						regid = "";
					} else {
						logger.debug("[" + RegisterGCM.class.getName()
								+ ".doInBackground] Device not unregistered");
						msg = "Desactivacion de notificaciones correcto";
						discoverOk = true;
					}

				}

				discoverOk = true;
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				logger.error("[" + RegisterGCM.class.getName()
						+ ".doInBackground] Error =" + msg);
				discoverOk = false;
			}
			return msg;
		}

		@Override
		protected void onPostExecute(String msg) {
			if (!discoverOk) {
				intentos++;
				discoverDevice();
			} else {
				prefrerences.setRegistrationID(regid);
				intentos = 0;
				nextStep();
			}
		}
	}

	private class DataWSTask extends AsyncTask<Void, Integer, Boolean> {

		private LogManager logger = LogManager.getLogger(this.getClass());

		private ParticipantDao mParticipantDao;
		private BetDao mBetDao;
		private PriceDao mPriceDao;
		private PotDao mPotDao;
		private LotGMVDBAdapter mLotGMVDBAdapter;
		private LectorDatosWS mLector;

		private TypeAppData mTypeData;

		private boolean mForceUpdate;

		public DataWSTask(Context context, TypeAppData typeData,
				boolean forceUpdate) {
			mTypeData = typeData;
			mForceUpdate = forceUpdate;
			try {
				mLotGMVDBAdapter = new LotGMVDBAdapter(context);
				mParticipantDao = new ParticipantDao(mLotGMVDBAdapter);
				mBetDao = new BetDao(mLotGMVDBAdapter);
				mPriceDao = new PriceDao(mLotGMVDBAdapter);
				mPotDao = new PotDao(mLotGMVDBAdapter);
				mLector = new LectorDatosWS(context);
			} catch (NumberFormatException e) {
				logger.error("Error with the package name: " + e.getMessage());
				logger.debug("Error with the package name: " + e);
			} catch (NameNotFoundException e) {
				logger.error("Error with the application version name: "
						+ e.getMessage());
				logger.debug("Error with the application version name: " + e);
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			logger.debug("Getting data from server...");
			List<?> dbResult;
			switch (mTypeData) {
			case BET:
				dbResult = mBetDao.findAll();
				if (mForceUpdate || dbResult.size() == 0) {
					mBetDao.removeAll();
					mBetDao.save(mLector.readBet());
				}
				break;
			case POT:
				dbResult = mPotDao.findAll();
				if (mForceUpdate || dbResult.size() == 0) {
					mPotDao.removeAll();
					mPotDao.save(mLector.readPotHistory());
				}
				break;
			case PARTICIPANT:
				dbResult = mParticipantDao.findAll();
				if (mForceUpdate || dbResult.size() == 0) {
					mParticipantDao.removeAll();
					mParticipantDao.save(mLector.readParticipant());
				}
				break;
			case PRICE:
				dbResult = mPriceDao.findAll();
				if (mForceUpdate || dbResult.size() == 0) {
					mPriceDao.removeAll();
					mPriceDao.save(mLector.readPrice());
				}
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean mResult) {
			nextStep();
		}
	}

}
