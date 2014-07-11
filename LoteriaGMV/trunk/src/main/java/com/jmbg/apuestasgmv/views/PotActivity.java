package com.jmbg.apuestasgmv.views;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import com.jmbg.apuestasgmv.Constants;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.exception.ApuestasGMVException;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.PotDao;
import com.jmbg.apuestasgmv.model.dao.entities.Pot;
import com.jmbg.apuestasgmv.model.dao.entities.PotBaseColumns;
import com.jmbg.apuestasgmv.views.adapters.PotAdapter;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class PotActivity extends CustomActivity {

	private PotDao mPotDao;
	private LotGMVDBAdapter mLotGMVDBAdapter;

	private PotViewHolder mHolder;

	private PotAdapter mAdapter;

	public class PotViewHolder {
		public TextView currentPotValue;
		public TextView currentPotBaseMin;
		public TextView currentPotBetMin;
		public TextView currentPotRestMin;
		
		public ListView potLists;
	}

	/*
	 * ACTIVITY METHODS
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info("Create");
		setContentView(R.layout.pot_activity);

		initActionBar(getResources().getString(R.string.category_pot));
		enableGoHomeButton();
		changeActionBarBrandImage(getResources().getDrawable(R.drawable.ic_pot));
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
			mPotDao = new PotDao(mLotGMVDBAdapter);
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
	protected TypeAppData getTypeDataActivity(){
		return TypeAppData.POT;
	}
	
	@Override
	protected void initViewHolder() {
		mHolder = new PotViewHolder();

		mHolder.currentPotValue = (TextView) findViewById(R.id.pot_current_pot_value);
		mHolder.currentPotBaseMin = (TextView) findViewById(R.id.pot_current_pot_value_base_min);
		mHolder.currentPotBetMin = (TextView) findViewById(R.id.pot_current_pot_bet_min);
		mHolder.currentPotRestMin = (TextView) findViewById(R.id.pot_current_pot_rest_min);

		
		mHolder.potLists = (ListView) findViewById(R.id.pot_list);

		initHolderValues();
	}

	@Override
	protected void initHolderValues() {
		iniPotValues();
		iniPotHistoricValues();
	}

	private void iniPotValues() {
		List<Pot> currentPots = mPotDao.findByField(
				PotBaseColumns.FIELD_POT_VALID, 1);

		String potValue;
		String potBet;
		String potRest;

		boolean isEnoughtPot = false;
		if (currentPots.size() > 0) {
			DecimalFormat df = new DecimalFormat("#.##");
			Pot currentPot = currentPots.get(0);
			potValue = df.format(currentPot.getPotValue()) + " €";
			potBet = df.format(currentPot.getPotBet()) + " €";
			potRest = df.format(currentPot.getPotValue()
					- currentPot.getPotBet())
					+ " €";

			if (currentPot.getPotValue() - currentPot.getPotBet() >= Constants.MIN_POT_VALUE)
				isEnoughtPot = true;

		} else {
			potValue = "-";
			potBet = "-";
			potRest = "-";
		}

		int idColor = isEnoughtPot ? R.color.green : R.color.red;
		int color = getResources().getColor(idColor);
		mHolder.currentPotRestMin.setTextColor(color);

		mHolder.currentPotValue.setText(potRest);
		mHolder.currentPotBaseMin.setText(potValue);
		mHolder.currentPotBetMin.setText(potBet);
		mHolder.currentPotRestMin.setText(potRest);
	}

	private void iniPotHistoricValues() {
		List<Pot> pots = mPotDao.findByField(
				PotBaseColumns.FIELD_POT_VALID, 0);
		Collections.sort(pots);

		mAdapter = new PotAdapter(this, R.layout.list_pot_item, pots);

		mHolder.potLists.setAdapter(mAdapter);
	}
}