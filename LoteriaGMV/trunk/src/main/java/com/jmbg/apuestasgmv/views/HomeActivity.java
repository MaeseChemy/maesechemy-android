package com.jmbg.apuestasgmv.views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jmbg.apuestasgmv.Constants;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.exception.ApuestasGMVException;
import com.jmbg.apuestasgmv.model.dao.BetDao;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.PotDao;
import com.jmbg.apuestasgmv.model.dao.entities.Bet;
import com.jmbg.apuestasgmv.model.dao.entities.BetBaseColumns;
import com.jmbg.apuestasgmv.model.dao.entities.Pot;
import com.jmbg.apuestasgmv.model.dao.entities.PotBaseColumns;
import com.jmbg.apuestasgmv.utils.ActivityElement;
import com.jmbg.apuestasgmv.utils.BetActivityElement;
import com.jmbg.apuestasgmv.utils.ParticipantsActivityElement;
import com.jmbg.apuestasgmv.utils.PotActivityElement;
import com.jmbg.apuestasgmv.utils.PriceActivityElement;
import com.jmbg.apuestasgmv.views.adapters.GalleryBetAdapter;
import com.jmbg.apuestasgmv.views.adapters.GridAdapter;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HomeActivity extends CustomActivity {

	private List<ActivityElement> mainGridElements;

	private PotDao mPotDao;
	private BetDao mBetDao;
	private LotGMVDBAdapter mLotGMVDBAdapter;

	private GalleryBetAdapter adapterCurrentBets;

	private HomeViewHolder mHolder;

	public class HomeViewHolder {
		public GridView activitiesGrid;

		public TextView currentPotValue;
		public TextView currentPotBaseMin;
		public TextView currentPotBetMin;
		public TextView currentPotRestMin;

		public Gallery currentBetsGallery;
	}

	/*
	 * ACTIVITY METHODS
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info("Create");
		setContentView(R.layout.home_activity);

		initActionBar(getResources().getString(R.string.category_home));
		try {
			enableConfigButton();
		} catch (ApuestasGMVException e) {
			e.printStackTrace();
		}
		
		initDaos();
		initViewHolder();
	}

	private void initDaos() {
		try {
			mLotGMVDBAdapter = new LotGMVDBAdapter(this);
			mPotDao = new PotDao(mLotGMVDBAdapter);
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
	protected TypeAppData getTypeDataActivity(){
		return TypeAppData.HOME;
	}
	
	@Override
	protected void initViewHolder() {
		mHolder = new HomeViewHolder();

		mHolder.activitiesGrid = (GridView) findViewById(R.id.home_grid_buttons);

		mHolder.currentPotValue = (TextView) findViewById(R.id.home_current_pot_value);
		mHolder.currentPotBaseMin = (TextView) findViewById(R.id.home_current_pot_value_base_min);
		mHolder.currentPotBetMin = (TextView) findViewById(R.id.home_current_pot_bet_min);
		mHolder.currentPotRestMin = (TextView) findViewById(R.id.home_current_pot_rest_min);

		mHolder.currentBetsGallery = (Gallery) findViewById(R.id.home_bets_gallery);

		initHolderValues();
	}

	@Override
	protected void initHolderValues() {
		initActivitiesButtons();
		initPotValues();
		initBetValues();
	}

	private void initActivitiesButtons() {
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

		mHolder.activitiesGrid.setAdapter(adapeter);

		mHolder.activitiesGrid
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						ActivityElement activity = (ActivityElement) parent
								.getAdapter().getItem(position);

						if (activity != null) {
							Intent launchIntent = activity.getIntent();
							if (launchIntent != null) {
								startActivity(launchIntent);
								overridePendingTransition(R.anim.push_left_in,
										R.anim.push_left_out);
							}
						}

					}

				});
	}

	private void initPotValues() {
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

	private void initBetValues() {
		List<Bet> currentBets = mBetDao.findByField(
				BetBaseColumns.FIELD_BET_ACTIVE, 1);
		Collections.sort(currentBets, Collections.reverseOrder());

		adapterCurrentBets = new GalleryBetAdapter(this,
				R.layout.gallery_bet_item, currentBets);
		mHolder.currentBetsGallery.setAdapter(adapterCurrentBets);
		mHolder.currentBetsGallery
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Bet item = adapterCurrentBets.getItem(position);
						AlertImageDialog imageDialog = new AlertImageDialog(
								HomeActivity.this);
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