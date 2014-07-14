package com.jmbg.apuestasgmv.views;

import java.util.List;

import com.jmbg.apuestasgmv.Constants.TypeAppData;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.exception.ApuestasGMVException;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.ParticipantDao;
import com.jmbg.apuestasgmv.model.dao.entities.Participant;
import com.jmbg.apuestasgmv.views.adapters.ParticipantAdapter;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.ListView;

public class ParticipantActivity extends CustomActivity {

	private ParticipantDao mParticipantDao;
	private LotGMVDBAdapter mLotGMVDBAdapter;

	private ParticipantViewHolder mHolder;

	private ParticipantAdapter mAdapter;

	public class ParticipantViewHolder {
		public ListView participantsList;
	}

	/*
	 * ACTIVITY METHODS
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info("Create");
		setContentView(R.layout.participants_activity);

		initActionBar(getResources().getString(R.string.category_participants));
		enableGoHomeButton();
		changeActionBarBrandImage(getResources().getDrawable(
				R.drawable.ic_participants));
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
			mParticipantDao = new ParticipantDao(mLotGMVDBAdapter);
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
		return TypeAppData.PARTICIPANT;
	}
	
	@Override
	protected void initViewHolder() {
		mHolder = new ParticipantViewHolder();

		mHolder.participantsList = (ListView) findViewById(R.id.participant_list);

		initHolderValues();
	}

	@Override
	protected void initHolderValues() {
		iniParticipantValues();
	}

	private void iniParticipantValues() {
		List<Participant> participants = mParticipantDao.findAll();

		mAdapter = new ParticipantAdapter(this, R.layout.list_part_item,
				participants);

		mHolder.participantsList.setAdapter(mAdapter);
	}
}