package com.jmbg.apuestasgmv.views;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.exception.ApuestasGMVException;
import com.jmbg.apuestasgmv.control.ws.LectorDatosWS;
import com.jmbg.apuestasgmv.model.dao.BetDao;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.ParticipantDao;
import com.jmbg.apuestasgmv.model.dao.PotDao;
import com.jmbg.apuestasgmv.model.dao.PriceDao;
import com.jmbg.apuestasgmv.utils.LogManager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class CustomActivity extends Activity {
	protected LogManager logger = LogManager.getLogger(this.getClass());
	private RelativeLayout customActionBar;

	public enum TypeAppData {
		HOME, PARTICIPANT, BET, PRICE, POT
	};

	private enum TypeActionBarButton {
		CONFIG, REFRESH
	};

	/*
	 * ACTIVITY METHODS
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		logger.info("Destroy");
	}

	@Override
	protected void onResume() {
		super.onResume();
		logger.info("Resume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		logger.info("Pause");
	}

	protected void initActionBar(String barText) {
		customActionBar = (RelativeLayout) findViewById(R.id.app_header);
		setActionBarText(barText);
	}

	protected void changeActionBarBrandImage(Drawable brandImage) {
		ImageView imageBarBrand = (ImageView) findViewById(R.id.action_bar_brand);
		imageBarBrand.setImageDrawable(brandImage);
	}

	private void setActionBarText(String barText) {
		TextView actionBarText = (TextView) customActionBar
				.findViewById(R.id.action_bar_name_window);
		actionBarText.setText(barText);
	}

	protected void enableConfigButton() throws ApuestasGMVException {
		ImageButton configBtn = getActionBarButton(TypeActionBarButton.CONFIG);
		if (configBtn == null)
			throw new ApuestasGMVException(
					"Error getting config button from actionBar");
		configBtn.setVisibility(View.VISIBLE);
		configBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(CustomActivity.this, "Configuración",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void enableRefreshButton() throws ApuestasGMVException {
		ImageButton configBtn = getActionBarButton(TypeActionBarButton.REFRESH);
		if (configBtn == null)
			throw new ApuestasGMVException(
					"Error getting refresh button from actionBar");
		configBtn.setVisibility(View.VISIBLE);
		configBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				readWSData();
			}
		});
	}

	protected RelativeLayout getCustomActionBar() throws ApuestasGMVException {
		if (customActionBar != null) {
			return customActionBar;
		} else {
			throw new ApuestasGMVException("ActionBar not initialized");
		}
	}

	private ImageButton getActionBarButton(TypeActionBarButton type)
			throws ApuestasGMVException {
		ImageButton btn = null;
		if (customActionBar != null) {
			switch (type) {
			case CONFIG:
				btn = (ImageButton) customActionBar
						.findViewById(R.id.action_bar_button_settings);
				break;
			case REFRESH:
				btn = (ImageButton) customActionBar
						.findViewById(R.id.action_bar_button_refresh);
				break;
			}
		} else {
			throw new ApuestasGMVException("ActionBar not initialized");
		}
		return btn;
	}

	protected void enableGoHomeButton() {
		LinearLayout layoutName = (LinearLayout) customActionBar
				.findViewById(R.id.action_bar_layout_name);
		((ImageView) layoutName.findViewById(R.id.ic_back_action))
				.setVisibility(View.VISIBLE);
		layoutName.setClickable(true);
		layoutName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finishActivity();
			}
		});

	}

	protected void finishActivity() {
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	protected void readWSData() {
		DataWSTask readWSData = new DataWSTask(this, getTypeDataActivity());
		readWSData.execute();
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

		private AlertProgressDialog mProgressDialog;

		public DataWSTask(Context context, TypeAppData typeData) {
			mTypeData = typeData;
			mProgressDialog = new AlertProgressDialog(CustomActivity.this);
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
		protected void onPreExecute() {
			String title;
			String message = "Obteniendo datos del servidor...";
			switch (mTypeData) {
			case BET:
				title = "Apuestas";
				break;
			case POT:
				title = "Bote";
				break;
			case PARTICIPANT:
				title = "Participantes";
				break;
			case PRICE:
				title = "Premios";
				break;
			default:
				title = "-";
				break;
			}
			mProgressDialog.setTitle(title);
			mProgressDialog.setMessage(message);

			mProgressDialog.show();

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			logger.debug("Getting data from server...");

			switch (mTypeData) {
			case BET:
				mBetDao.removeAll();
				mBetDao.save(mLector.readBet());
				break;
			case POT:
				mPotDao.removeAll();
				mPotDao.save(mLector.readPotHistory());
				break;
			case PARTICIPANT:
				mParticipantDao.removeAll();
				mParticipantDao.save(mLector.readParticipant());
				break;
			case PRICE:
				mPriceDao.removeAll();
				mPriceDao.save(mLector.readPrice());
				break;
			case HOME:
				mBetDao.removeAll();
				mBetDao.save(mLector.readBet());
				mPotDao.removeAll();
				mPotDao.save(mLector.readPotHistory());
				mParticipantDao.removeAll();
				mParticipantDao.save(mLector.readParticipant());
				mPriceDao.removeAll();
				mPriceDao.save(mLector.readPrice());
			default:
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean mResult) {
			mProgressDialog.dismiss();
			if (mResult)
				initHolderValues();
		}
	}

	// METODOS ABSTRACTOS
	protected abstract void initViewHolder();

	protected abstract void initHolderValues();

	protected abstract TypeAppData getTypeDataActivity();

}