package com.jmbg.loteriasgmv.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.LotGMVDBAdapter;
import com.jmbg.loteriasgmv.dao.ParticipantDao;
import com.jmbg.loteriasgmv.dao.entities.Participant;
import com.jmbg.loteriasgmv.util.LogManager;
import com.jmbg.loteriasgmv.view.PullToRefreshListView.OnRefreshListener;
import com.jmbg.loteriasgmv.view.adapter.ParticipantAdapter;
import com.jmbg.loteriasgmv.ws.LectorDatosWS;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class ParticipantActivity extends ListActivity {

	private LogManager LOG = LogManager.getLogger(this.getClass());

	// ASYNC TASKS
	private ParticipantHistoryDBTask participantHistoryDBTask;
	private ParticipantHistoryWSTask participantHistoryWSTask;

	// LIST
	private PullToRefreshListView lv;
	private List<Participant> participantHistory;
	private ParticipantAdapter adapter;

	// DAO
	private ParticipantDao participantDao;
	private LotGMVDBAdapter lotGMVDBAdapter;

	public ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_participant);

		try {
			lotGMVDBAdapter = new LotGMVDBAdapter(getBaseContext());
			participantDao = new ParticipantDao(lotGMVDBAdapter);

		} catch (NumberFormatException e) {
			LOG.error("Error with the package name: " + e.getMessage());
			LOG.debug("Error with the package name: " + e);
		} catch (NameNotFoundException e) {
			LOG.error("Error with the application version name: "
					+ e.getMessage());
			LOG.debug("Error with the application version name: " + e);
		}

		Button buttonRefresh = (Button) findViewById(R.id.refreshButton);
		buttonRefresh.setVisibility(View.VISIBLE);
		buttonRefresh.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		       runGetParticipantWSTask();
		    }
		});
		
		this.participantHistory = new ArrayList<Participant>();
		this.adapter = new ParticipantAdapter(this, R.layout.participant_item,
				participantHistory);

		lv = (PullToRefreshListView) getListView();
		lv.setShowLastUpdatedText(true);
		lv.setAdapter(this.adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				LOG.debug("Clicado el objeto de la lista: " + position);
			}
		});
		lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				runGetParticipantWSTask();
			}
		});

		registerForContextMenu(lv);
		
		// get intent data
		Intent i = getIntent();

		boolean forceUpdate;
		if (i.getExtras() != null)
			forceUpdate = i.getExtras().getBoolean("forceUpdate");
		else
			forceUpdate = false;
		
		if(forceUpdate)
			runGetParticipantWSTask();
		else
			runGetParticipantDBTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.participant_main, menu);
		return true;
	}

	public void runGetParticipantDBTask() {
		if (participantHistoryDBTask == null
				|| participantHistoryDBTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			participantHistoryDBTask = new ParticipantHistoryDBTask();
			participantHistoryDBTask.execute();
		}
	}

	public void runGetParticipantWSTask() {
		if (participantHistoryWSTask == null
				|| participantHistoryWSTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			participantHistoryWSTask = new ParticipantHistoryWSTask();
			participantHistoryWSTask.execute();
		}
	}

	private class ParticipantHistoryWSTask extends AsyncTask<Void, Integer, List<Participant>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(ParticipantActivity.this);
			progress.setTitle(getResources().getString(R.string.category_participants));
			progress.setMessage("Obteniendo participantes del servidor");
			progress.show();
		}

		@Override
		protected List<Participant> doInBackground(Void... params) {
			LectorDatosWS lector = new LectorDatosWS(ParticipantActivity.this);
			LOG.debug("Getting from server participant history...");
			List<Participant> participantHistory = lector.readParticipant();
			Collections.sort(participantHistory);
			return participantHistory;
		}

		@Override
		protected void onPostExecute(List<Participant> result) {
			// Refresh DB
			participantDao.removeAll();
			participantDao.save(result);

			// Refresh list
			adapter.clear();
			for (Participant participant : result) {
				adapter.add(participant);
			}
			adapter.notifyDataSetChanged();

			lv.postDelayed(new Runnable() {

				@Override
				public void run() {
					lv.onRefreshComplete();
				}
			}, 2000);
			
			progress.dismiss();
		}
	}

	private class ParticipantHistoryDBTask extends AsyncTask<Void, Integer, List<Participant>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(ParticipantActivity.this);
			progress.setTitle(getResources().getString(R.string.category_participants));
			progress.setMessage("Obteniendo participantes");
			progress.show();
		}

		@Override
		protected List<Participant> doInBackground(Void... params) {
			participantHistory = new ArrayList<Participant>();

			try {
				LOG.debug("Running thread: " + Thread.currentThread().getName());
				participantHistory = getParticipantHistoryList();
				LOG.debug("Participant history list: "
						+ (participantHistory == null ? 0 : participantHistory.size()));
				Collections.sort(participantHistory);
				return participantHistory;
			} catch (Exception e) {
				LOG.error("Error reading chat history: " + e.getMessage());
				LOG.debug("Error reading chat history", e);

				return participantHistory;
			}

		}

		@Override
		protected void onPostExecute(List<Participant> result) {
			progress.dismiss();
			if (result.size() != 0) {
				adapter.clear();
				for (Participant participant : result) {
					adapter.add(participant);
				}
				adapter.notifyDataSetChanged();

				lv.postDelayed(new Runnable() {

					@Override
					public void run() {
						lv.onRefreshComplete();
					}
				}, 2000);
			} else {
				runGetParticipantWSTask();
			}
		}
	}

	private ArrayList<Participant> getParticipantHistoryList() {
		ArrayList<Participant> participantHistoryList = new ArrayList<Participant>();

		try {
			participantHistoryList = (ArrayList<Participant>) participantDao.findAll();
			return participantHistoryList;
		} catch (Exception e) {
			LOG.error("Error reading Participant History: " + e.getMessage());
			LOG.debug("Error reading Participant History", e);
		}

		return participantHistoryList;
	}

}
