package com.jmbg.loteriasgmv.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.LotGMVDBAdapter;
import com.jmbg.loteriasgmv.dao.BetDao;
import com.jmbg.loteriasgmv.dao.entities.Bet;
import com.jmbg.loteriasgmv.util.LogManager;
import com.jmbg.loteriasgmv.view.adapter.BetAdapter;
import com.jmbg.loteriasgmv.ws.LectorDatosWS;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class BetActivity extends Activity {

	private LogManager LOG = LogManager.getLogger(this.getClass());

	// ASYNC TASKS
	private BetDBTask betDBTask;
	private BetWSTask betWSTask;

	// LIST
	private List<Bet> bets;
	private BetAdapter adapterAllBets;
	private BetAdapter adapterValidBets;

	// DAO
	private BetDao betDao;
	private LotGMVDBAdapter lotGMVDBAdapter;

	public ProgressDialog progress;
	
	private GridView gridviewActiveBets;
	private GridView gridviewAllBets;
	private CheckBox checkSeeAllBets;
	private TextView txtNameMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet);
		
		this.bets = new ArrayList<Bet>();
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
		
		checkSeeAllBets = (CheckBox) findViewById(R.id.bet_checkbox_all);
		txtNameMenu = (TextView) findViewById(R.id.nameBetMenu);
		
		Button buttonRefresh = (Button) findViewById(R.id.refreshButton);
		buttonRefresh.setVisibility(View.VISIBLE);
		buttonRefresh.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		       runGetBetWSTask();
		    }
		});
		
		this.adapterValidBets = new BetAdapter(this, R.layout.bet_item,
				new ArrayList<Bet>()); 
		gridviewActiveBets = (GridView) findViewById(R.id.gridActiveBets);
		gridviewActiveBets.setAdapter(this.adapterValidBets);	
		gridviewActiveBets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Bet selectBet = adapterValidBets.getItem(position);
				
                // Sending image id to BetFullImageActivity
                Intent i = new Intent(getApplicationContext(), BetFullImageActivity.class);
                i.putExtra("bet_image", selectBet.getBetImage());
                startActivity(i);
			}
			
		});
		
		this.adapterAllBets = new BetAdapter(this, R.layout.bet_item,
				new ArrayList<Bet>());
		gridviewAllBets = (GridView) findViewById(R.id.gridAllBets);
		gridviewAllBets.setAdapter(this.adapterAllBets);	
		gridviewAllBets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Bet selectBet = adapterAllBets.getItem(position);
				
                // Sending image id to BetFullImageActivity
                Intent i = new Intent(getApplicationContext(), BetFullImageActivity.class);
                i.putExtra("bet_image", selectBet.getBetImage());
                startActivity(i);
			}
			
		});
		
		runGetBetDBTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.bet, menu);
		return true;
	}
	
	public void changeGrid(View view){
		if(checkSeeAllBets.isChecked()){
			txtNameMenu.setText("Historico de Apuestas");
			gridviewAllBets.setVisibility(View.VISIBLE);
			gridviewActiveBets.setVisibility(View.GONE);
		}else{
			txtNameMenu.setText("Apuestas Activas");
			gridviewAllBets.setVisibility(View.GONE);
			gridviewActiveBets.setVisibility(View.VISIBLE);
		}
		
	}
	public void runGetBetDBTask() {
		if (betDBTask == null
				|| betDBTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			betDBTask = new BetDBTask();
			betDBTask.execute();
		}
	}

	public void runGetBetWSTask() {
		if (betWSTask == null
				|| betWSTask.getStatus().equals(
						AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			betWSTask = new BetWSTask();
			betWSTask.execute();
		}
	}

	private class BetWSTask extends AsyncTask<Void, Integer, List<Bet>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(BetActivity.this);
			progress.setTitle(getResources().getString(R.string.title_activity_bets));
			progress.setMessage("Obteniendo apuestas del servidor");
			progress.show();
		}

		@Override
		protected List<Bet> doInBackground(Void... params) {
			LectorDatosWS lector = new LectorDatosWS(BetActivity.this);
			LOG.debug("Getting from server bet history...");
			bets = lector.readBet();
			Collections.sort(bets);
			return bets;
		}

		@Override
		protected void onPostExecute(List<Bet> result) {
			// Refresh DB
			betDao.removeAll();
			betDao.save(result);

			// Refresh list
			adapterAllBets.clear();
			adapterValidBets.clear();
			for (Bet bet : result) {
				adapterAllBets.add(bet);
				if(bet.getBetActive() == Bet.BET_ACTIVE)
					adapterValidBets.add(bet);
			}
			adapterAllBets.notifyDataSetChanged();
			adapterValidBets.notifyDataSetChanged();
			progress.dismiss();
		}
	}

	private class BetDBTask extends AsyncTask<Void, Integer, List<Bet>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(BetActivity.this);
			progress.setTitle(getResources().getString(R.string.title_activity_bets));
			progress.setMessage("Obteniendo bets");
			progress.show();
		}

		@Override
		protected List<Bet> doInBackground(Void... params) {
			bets = new ArrayList<Bet>();

			try {
				LOG.debug("Running thread: " + Thread.currentThread().getName());
				bets = getBetList();
				LOG.debug("Bet history list: "
						+ (bets == null ? 0 : bets.size()));
				Collections.sort(bets);
				return bets;
			} catch (Exception e) {
				LOG.error("Error reading chat history: " + e.getMessage());
				LOG.debug("Error reading chat history", e);

				return bets;
			}

		}

		@Override
		protected void onPostExecute(List<Bet> result) {
			progress.dismiss();
			if (result.size() != 0) {
				adapterAllBets.clear();
				adapterValidBets.clear();
				for (Bet bet : result) {
					adapterAllBets.add(bet);
					if(bet.getBetActive() == Bet.BET_ACTIVE)
						adapterValidBets.add(bet);
				}
				adapterAllBets.notifyDataSetChanged();
				adapterValidBets.notifyDataSetChanged();

			} else {
				runGetBetWSTask();
			}
		}
	}

	private ArrayList<Bet> getBetList() {
		ArrayList<Bet> betList = new ArrayList<Bet>();

		try {
			betList = (ArrayList<Bet>) betDao.findAll();
			return betList;
		} catch (Exception e) {
			LOG.error("Error reading Bet : " + e.getMessage());
			LOG.debug("Error reading Bet ", e);
		}

		return betList;
	}

}
