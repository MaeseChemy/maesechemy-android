package com.jmbg.loteriasgmv.view;

import java.util.ArrayList;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.LotGMVDBAdapter;
import com.jmbg.loteriasgmv.dao.PotDao;
import com.jmbg.loteriasgmv.dao.entities.Pot;
import com.jmbg.loteriasgmv.util.LogManager;
import com.jmbg.loteriasgmv.view.PullToRefreshListView.OnRefreshListener;
import com.jmbg.loteriasgmv.view.adapter.PotAdapter;
import com.jmbg.loteriasgmv.ws.LectorDatosWS;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PotActivity extends ListActivity {

	private LogManager LOG = LogManager.getLogger(this.getClass());

	// ASYNC TASKS
	private PotHistoryDBTask potHistoryDBTask;
	private PotHistoryWSTask potHistoryWSTask;

	// LIST
	private PullToRefreshListView lv;
	private List<Pot> potHistory;
	private PotAdapter adapter;

	// DAO
	private PotDao potDao;
	private LotGMVDBAdapter lotGMVDBAdapter;

	public ProgressDialog progress;
	
	private Pot currentPot;
	private TextView currentPotTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pot);

		try {
			lotGMVDBAdapter = new LotGMVDBAdapter(getBaseContext());
			potDao = new PotDao(lotGMVDBAdapter);

		} catch (NumberFormatException e) {
			LOG.error("Error with the package name: " + e.getMessage());
			LOG.debug("Error with the package name: " + e);
		} catch (NameNotFoundException e) {
			LOG.error("Error with the application version name: "
					+ e.getMessage());
			LOG.debug("Error with the application version name: " + e);
		}

		this.potHistory = new ArrayList<Pot>();
		this.adapter = new PotAdapter(this, R.layout.pot_history_item,
				potHistory);

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
				runGetPotWSTask();
			}
		});

		registerForContextMenu(lv);

		this.currentPotTextView = (TextView) findViewById(R.id.currentPotValueText);
		
		// Obtenemos los datos
		runGetPotDBTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.pot_main, menu);
		return true;
	}

	public void runGetPotDBTask() {
		if (potHistoryDBTask == null
				|| potHistoryDBTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			potHistoryDBTask = new PotHistoryDBTask();
			potHistoryDBTask.execute();
		}
	}

	public void runGetPotWSTask() {
		if (potHistoryWSTask == null
				|| potHistoryWSTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			potHistoryWSTask = new PotHistoryWSTask();
			potHistoryWSTask.execute();
		}
	}

	private class PotHistoryWSTask extends AsyncTask<Void, Integer, List<Pot>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(PotActivity.this);
			progress.setTitle(getResources().getString(R.string.app_name));
			progress.setMessage("Obteniendo historico de botes del servidor");
			progress.show();
		}

		@Override
		protected List<Pot> doInBackground(Void... params) {
			LectorDatosWS lector = new LectorDatosWS(PotActivity.this);
			LOG.debug("Getting from server pot history...");

			return lector.readPotHistory();
		}

		@Override
		protected void onPostExecute(List<Pot> result) {
			// Refresh DB
			potDao.removeAll();
			potDao.save(result);

			// Refresh list
			adapter.clear();
			for (Pot pot : result) {
				if(pot.getPotValid() == Pot.POT_VALID)
					currentPot = pot;
				adapter.add(pot);
			}
			adapter.notifyDataSetChanged();

			lv.postDelayed(new Runnable() {

				@Override
				public void run() {
					lv.onRefreshComplete();
				}
			}, 2000);
			
			setCurrentPot();
			progress.dismiss();
		}
	}

	private class PotHistoryDBTask extends AsyncTask<Void, Integer, List<Pot>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(PotActivity.this);
			progress.setTitle(getResources().getString(R.string.app_name));
			progress.setMessage("Obteniendo historico de botes");
			progress.show();
		}

		@Override
		protected List<Pot> doInBackground(Void... params) {
			List<Pot> potHistoryList = new ArrayList<Pot>();

			try {
				LOG.debug("Running thread: " + Thread.currentThread().getName());
				potHistoryList = getPotHistoryList();
				LOG.debug("Pot history list: "
						+ (potHistoryList == null ? 0 : potHistoryList.size()));

				return potHistoryList;
			} catch (Exception e) {
				LOG.error("Error reading chat history: " + e.getMessage());
				LOG.debug("Error reading chat history", e);

				return potHistoryList;
			}

		}

		@Override
		protected void onPostExecute(List<Pot> result) {
			progress.dismiss();
			if (result.size() != 0) {
				adapter.clear();
				for (Pot pot : result) {
					if(pot.getPotValid() == Pot.POT_VALID)
						currentPot = pot;
					adapter.add(pot);
				}
				adapter.notifyDataSetChanged();

				lv.postDelayed(new Runnable() {

					@Override
					public void run() {
						lv.onRefreshComplete();
					}
				}, 2000);
				setCurrentPot();
			} else {
				runGetPotWSTask();
			}
		}
	}

	private ArrayList<Pot> getPotHistoryList() {
		ArrayList<Pot> potHistoryList = new ArrayList<Pot>();

		try {
			potHistoryList = (ArrayList<Pot>) potDao.findAll();
			return potHistoryList;
		} catch (Exception e) {
			LOG.error("Error reading Pot History: " + e.getMessage());
			LOG.debug("Error reading Pot History", e);
		}

		return potHistoryList;
	}
	
	private void setCurrentPot(){
		if(this.currentPot != null)
			this.currentPotTextView.setText("Bote Actual: "+Float.toString(this.currentPot.getPotValue() - this.currentPot.getPotBet()) + " €");
		else
			this.currentPotTextView.setText(getResources().getString(R.string.not_current_pot));
	}

}
