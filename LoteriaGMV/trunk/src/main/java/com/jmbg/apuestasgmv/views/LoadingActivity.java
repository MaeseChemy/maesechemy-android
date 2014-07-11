package com.jmbg.apuestasgmv.views;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jmbg.apuestasgmv.Constants;
import com.jmbg.apuestasgmv.IntentResults;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.ws.HTTPRegId;
import com.jmbg.apuestasgmv.control.ws.LectorDatosWS;
import com.jmbg.apuestasgmv.model.dao.BetDao;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.ParticipantDao;
import com.jmbg.apuestasgmv.model.dao.PotDao;
import com.jmbg.apuestasgmv.model.dao.PriceDao;
import com.jmbg.apuestasgmv.model.dao.entities.Bet;
import com.jmbg.apuestasgmv.model.dao.entities.Participant;
import com.jmbg.apuestasgmv.model.dao.entities.Pot;
import com.jmbg.apuestasgmv.model.dao.entities.Price;
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
			setLoadingText("Activando notificaciones");
			RegisterGCM registerGCM = new RegisterGCM();
			registerGCM.execute(true);
		} else {
			Toast.makeText(this,
					"No se pudo realizar la accion, intentelo de nuevo...",
					Toast.LENGTH_LONG).show();
			nextStep();
		}
	}

	private void getBets() {
		setLoadingText("Obteniendo apuestas");
		BetWSTask task = new BetWSTask(this);
		task.execute();
	}

	private void getPots() {
		setLoadingText("Obteniendo bote");
		PotWSTask task = new PotWSTask(this);
		task.execute();
	}

	private void getParticipants() {
		setLoadingText("Obteniendo partipantes");
		ParticipantWSTask task = new ParticipantWSTask(this);
		task.execute();
	}

	private void getPrices() {
		setLoadingText("Obteniendo premios");
		PricesWSTask task = new PricesWSTask(this);
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
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging
							.getInstance(LoadingActivity.this);
				}
				if (doRegister) {
					regid = gcm.register(Constants.SENDER_ID);
					boolean register = sendRegistrationIdToBackend(true, regid);
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
				intentos = 0;
				nextStep();
			}
		}
	}

	public class PotWSTask extends AsyncTask<Void, Integer, List<Pot>> {

		private LogManager logger = LogManager.getLogger(this.getClass());

		private PotDao mDao;
		private LotGMVDBAdapter mLotGMVDBAdapter;
		private LectorDatosWS mLector;

		private List<Pot> mResult;

		public PotWSTask(Context context) {
			try {
				mLotGMVDBAdapter = new LotGMVDBAdapter(context);
				mDao = new PotDao(mLotGMVDBAdapter);
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
		protected List<Pot> doInBackground(Void... params) {
			logger.debug("Getting from server bet history...");

			mResult = mDao.findAll();
			if (mResult == null || mResult.size() == 0) {
				mResult = mLector.readPotHistory();
			}

			Collections.sort(mResult);
			return mResult;
		}

		@Override
		protected void onPostExecute(List<Pot> mResult) {
			if (mResult != null) {
				mDao.removeAll();
				mDao.save(mResult);
			}
			nextStep();
		}
	}

	public class BetWSTask extends AsyncTask<Void, Integer, List<Bet>> {

		private LogManager logger = LogManager.getLogger(this.getClass());

		private BetDao mDao;
		private LotGMVDBAdapter mLotGMVDBAdapter;
		private LectorDatosWS mLector;

		private List<Bet> mResult;

		public BetWSTask(Context context) {
			try {
				mLotGMVDBAdapter = new LotGMVDBAdapter(context);
				mDao = new BetDao(mLotGMVDBAdapter);
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
		protected List<Bet> doInBackground(Void... params) {
			logger.debug("Getting from server bet history...");

			mResult = mDao.findAll();
			if (mResult == null || mResult.size() == 0) {
				mResult = mLector.readBet();
			}

			Collections.sort(mResult);
			return mResult;
		}

		@Override
		protected void onPostExecute(List<Bet> mResult) {
			if (mResult != null) {
				mDao.removeAll();
				mDao.save(mResult);
			}
			nextStep();
		}
	}

	public class ParticipantWSTask extends
			AsyncTask<Void, Integer, List<Participant>> {

		private LogManager logger = LogManager.getLogger(this.getClass());

		private ParticipantDao mDao;
		private LotGMVDBAdapter mLotGMVDBAdapter;
		private LectorDatosWS mLector;

		private List<Participant> mResult;

		public ParticipantWSTask(Context context) {
			try {
				mLotGMVDBAdapter = new LotGMVDBAdapter(context);
				mDao = new ParticipantDao(mLotGMVDBAdapter);
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
		protected List<Participant> doInBackground(Void... params) {
			logger.debug("Getting from server bet history...");

			mResult = mDao.findAll();
			if (mResult == null || mResult.size() == 0) {
				mResult = mLector.readParticipant();
			}

			Collections.sort(mResult);
			return mResult;
		}

		@Override
		protected void onPostExecute(List<Participant> mResult) {
			if (mResult != null) {
				mDao.removeAll();
				mDao.save(mResult);
			}
			nextStep();
		}
	}

	public class PricesWSTask extends AsyncTask<Void, Integer, List<Price>> {

		private LogManager logger = LogManager.getLogger(this.getClass());

		private PriceDao mDao;
		private LotGMVDBAdapter mLotGMVDBAdapter;
		private LectorDatosWS mLector;

		private List<Price> mResult;

		public PricesWSTask(Context context) {
			try {
				mLotGMVDBAdapter = new LotGMVDBAdapter(context);
				mDao = new PriceDao(mLotGMVDBAdapter);
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
		protected List<Price> doInBackground(Void... params) {
			logger.debug("Getting from server bet history...");

			mResult = mDao.findAll();
			if (mResult == null || mResult.size() == 0) {
				mResult = mLector.readPrice();
			}

			Collections.sort(mResult);
			return mResult;
		}

		@Override
		protected void onPostExecute(List<Price> mResult) {
			if (mResult != null) {
				mDao.removeAll();
				mDao.save(mResult);
			}
			nextStep();
		}
	}

}
