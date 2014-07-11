package com.jmbg.apuestasgmv.views;

import java.util.Collections;
import java.util.List;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.exception.ApuestasGMVException;
import com.jmbg.apuestasgmv.model.dao.BetDao;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.entities.Bet;
import com.jmbg.apuestasgmv.model.dao.entities.BetBaseColumns;
import com.jmbg.apuestasgmv.views.adapters.GalleryBetAdapter;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemClickListener;

public class BetActivity extends CustomActivity {

	private BetDao mBetDao;
	private LotGMVDBAdapter mLotGMVDBAdapter;

	private BetViewHolder mHolder;

	private GalleryBetAdapter adapterCurrentBets;
	private GalleryBetAdapter adapterHistoricBets;

	public class BetViewHolder {
		public Gallery currentBetsGallery;
		public Gallery historicBetsGallery;
	}

	/*
	 * ACTIVITY METHODS
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info("Create");
		setContentView(R.layout.bet_activity);

		initActionBar(getResources().getString(R.string.category_bets));
		enableGoHomeButton();
		changeActionBarBrandImage(getResources()
				.getDrawable(R.drawable.ic_bets));
		try {
			enableRefreshButton();
		} catch (ApuestasGMVException e) {
			e.printStackTrace();
		}

		initDaos();
		initViewHolder();
		
		// get intent data
		Intent i = getIntent();

		boolean forceUpdate;
		if (i.getExtras() != null)
			forceUpdate = i.getExtras().getBoolean("forceUpdate");
		else
			forceUpdate = false;

		if (forceUpdate)
			readWSData();
	}

	@Override
	public void onBackPressed() {
		finishActivity();
	}

	private void initDaos() {
		try {
			mLotGMVDBAdapter = new LotGMVDBAdapter(this);
			mBetDao = new BetDao(mLotGMVDBAdapter);
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
	protected TypeAppData getTypeDataActivity() {
		return TypeAppData.BET;
	}

	@Override
	protected void initViewHolder() {
		mHolder = new BetViewHolder();

		mHolder.currentBetsGallery = (Gallery) findViewById(R.id.bets_current_gallery);
		mHolder.historicBetsGallery = (Gallery) findViewById(R.id.bets_history_gallery);

		initHolderValues();
	}

	@Override
	protected void initHolderValues() {
		initBetValues();
	}

	private void initBetValues() {
		List<Bet> currentBets = mBetDao.findByField(
				BetBaseColumns.FIELD_BET_ACTIVE, 1);
		Collections.sort(currentBets, Collections.reverseOrder());

		List<Bet> historicBets = mBetDao.findByField(
				BetBaseColumns.FIELD_BET_ACTIVE, 0);
		Collections.sort(historicBets, Collections.reverseOrder());

		adapterCurrentBets = new GalleryBetAdapter(this,
				R.layout.gallery_bet_item_big, currentBets);
		mHolder.currentBetsGallery.setAdapter(adapterCurrentBets);

		adapterHistoricBets = new GalleryBetAdapter(this,
				R.layout.gallery_bet_item, historicBets);
		mHolder.historicBetsGallery.setAdapter(adapterHistoricBets);
		mHolder.historicBetsGallery
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Bet item = adapterHistoricBets.getItem(position);
						AlertImageDialog imageDialog = new AlertImageDialog(
								BetActivity.this);
						imageDialog.setTitle(item.getBetType() + " ["
								+ item.getBetDate() + "]");
						imageDialog.setImageBitmap(BitmapFactory
								.decodeByteArray(item.getBetImage(), 0,
										item.getBetImage().length));

						imageDialog.show();
					}
				});
	}
}