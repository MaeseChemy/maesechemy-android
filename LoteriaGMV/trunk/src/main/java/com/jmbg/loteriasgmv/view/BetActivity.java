package com.jmbg.loteriasgmv.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.LotGMVDBAdapter;
import com.jmbg.loteriasgmv.dao.BetDao;
import com.jmbg.loteriasgmv.dao.entities.Bet;
import com.jmbg.loteriasgmv.util.LogManager;
import com.jmbg.loteriasgmv.view.PullToRefreshListView.OnRefreshListener;
import com.jmbg.loteriasgmv.view.adapter.BetAdapter;
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

public class BetActivity extends ListActivity {

	private LogManager LOG = LogManager.getLogger(this.getClass());

	// ASYNC TASKS
	private BetHistoryDBTask betHistoryDBTask;
	private BetHistoryWSTask betHistoryWSTask;

	// LIST
	private PullToRefreshListView lv;
	private List<Bet> betHistory;
	private BetAdapter adapter;

	// DAO
	private BetDao betDao;
	private LotGMVDBAdapter lotGMVDBAdapter;

	public ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet);

		try {
			lotGMVDBAdapter = new LotGMVDBAdapter(getBaseContext());
			betDao = new BetDao(lotGMVDBAdapter);

		} catch (NumberFormatException e) {
			LOG.error("Error with the package name: " + e.getMessage());
			LOG.debug("Error with the package name: " + e);
		} catch (NameNotFoundException e) {
			LOG.error("Error with the application version name: "
					+ e.getMessage());
			LOG.debug("Error with the application version name: " + e);
		}

		this.betHistory = new ArrayList<Bet>();
		this.adapter = new BetAdapter(this, R.layout.bet_item,
				betHistory);

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
				runGetBetWSTask();
			}
		});

		registerForContextMenu(lv);
		
		// Obtenemos los datos
		runGetBetDBTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.bet_main, menu);
		return true;
	}

	public void runGetBetDBTask() {
		if (betHistoryDBTask == null
				|| betHistoryDBTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			betHistoryDBTask = new BetHistoryDBTask();
			betHistoryDBTask.execute();
		}
	}

	public void runGetBetWSTask() {
		if (betHistoryWSTask == null
				|| betHistoryWSTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			betHistoryWSTask = new BetHistoryWSTask();
			betHistoryWSTask.execute();
		}
	}

	private class BetHistoryWSTask extends AsyncTask<Void, Integer, List<Bet>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(BetActivity.this);
			progress.setTitle(getResources().getString(R.string.app_name));
			progress.setMessage("Obteniendo betes del servidor");
			progress.show();
		}

		@Override
		protected List<Bet> doInBackground(Void... params) {
			LectorDatosWS lector = new LectorDatosWS(BetActivity.this);
			LOG.debug("Getting from server bet history...");
			List<Bet> betList = lector.readBet();
			Collections.sort(betList);
			return betList;
		}

		@Override
		protected void onPostExecute(List<Bet> result) {
			// Refresh DB
			betDao.removeAll();
			betDao.save(result);

			// Refresh list
			adapter.clear();
			for (Bet bet : result) {
				adapter.add(bet);
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

	private class BetHistoryDBTask extends AsyncTask<Void, Integer, List<Bet>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(BetActivity.this);
			progress.setTitle(getResources().getString(R.string.app_name));
			progress.setMessage("Obteniendo betes");
			progress.show();
		}

		@Override
		protected List<Bet> doInBackground(Void... params) {
			List<Bet> betList = new ArrayList<Bet>();

			try {
				LOG.debug("Running thread: " + Thread.currentThread().getName());
				betList = getBetHistoryList();
				LOG.debug("Bet history list: "
						+ (betList == null ? 0 : betList.size()));
				Collections.sort(betList);
				return betList;
			} catch (Exception e) {
				LOG.error("Error reading chat history: " + e.getMessage());
				LOG.debug("Error reading chat history", e);

				return betList;
			}

		}

		@Override
		protected void onPostExecute(List<Bet> result) {
			progress.dismiss();
			if (result.size() != 0) {
				adapter.clear();
				for (Bet bet : result) {
					adapter.add(bet);
				}
				adapter.notifyDataSetChanged();

				lv.postDelayed(new Runnable() {

					@Override
					public void run() {
						lv.onRefreshComplete();
					}
				}, 2000);
			} else {
				runGetBetWSTask();
			}
		}
	}

	private ArrayList<Bet> getBetHistoryList() {
		ArrayList<Bet> betHistoryList = new ArrayList<Bet>();

		try {
			betHistoryList = (ArrayList<Bet>) betDao.findAll();
			return betHistoryList;
		} catch (Exception e) {
			LOG.error("Error reading Bet History: " + e.getMessage());
			LOG.debug("Error reading Bet History", e);
		}

		return betHistoryList;
	}

}
