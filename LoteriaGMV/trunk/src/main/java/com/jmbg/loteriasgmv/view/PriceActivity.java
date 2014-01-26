package com.jmbg.loteriasgmv.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.BetDao;
import com.jmbg.loteriasgmv.dao.LotGMVDBAdapter;
import com.jmbg.loteriasgmv.dao.PriceDao;
import com.jmbg.loteriasgmv.dao.entities.Bet;
import com.jmbg.loteriasgmv.dao.entities.Price;
import com.jmbg.loteriasgmv.util.Ball;
import com.jmbg.loteriasgmv.util.LogManager;
import com.jmbg.loteriasgmv.util.Ticket;
import com.jmbg.loteriasgmv.view.PullToRefreshListView.OnRefreshListener;
import com.jmbg.loteriasgmv.view.adapter.PriceAdapter;
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

public class PriceActivity extends ListActivity {

	private LogManager LOG = LogManager.getLogger(this.getClass());

	// ASYNC TASKS
	private PriceDBTask priceDBTask;
	private PriceWSTask priceWSTask;
	private BetDBTask betDBTask;

	// LIST
	private PullToRefreshListView lv;
	private List<Price> prices;
	private List<Bet> relateBets;
	private PriceAdapter adapter;

	// DAO
	private PriceDao priceDao;
	private BetDao betDao;
	private LotGMVDBAdapter lotGMVDBAdapter;

	public ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price);

		this.prices = new ArrayList<Price>();
		this.relateBets = new ArrayList<Bet>();
		try {
			lotGMVDBAdapter = new LotGMVDBAdapter(getBaseContext());
			priceDao = new PriceDao(lotGMVDBAdapter);
			betDao = new BetDao(lotGMVDBAdapter);
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
				runGetPriceWSTask();
			}
		});

		this.adapter = new PriceAdapter(this, R.layout.price_item,
				new ArrayList<Price>());

		this.prices = new ArrayList<Price>();
		this.adapter = new PriceAdapter(this, R.layout.price_item, prices);

		lv = (PullToRefreshListView) getListView();
		lv.setShowLastUpdatedText(true);
		lv.setAdapter(this.adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				Price price = adapter.getItem(position);
				runGetBetDBTask(price);
			}
		});
		lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				runGetPriceWSTask();
			}
		});

		registerForContextMenu(lv);

		runGetPriceDBTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.price, menu);
		return true;
	}

	public void runGetBetDBTask(Price price) {
		if (betDBTask == null
				|| betDBTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			betDBTask = new BetDBTask(price);
			betDBTask.execute();
		}
	}

	public void runGetPriceDBTask() {
		if (priceDBTask == null
				|| priceDBTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			priceDBTask = new PriceDBTask();
			priceDBTask.execute();
		}
	}

	public void runGetPriceWSTask() {
		if (priceWSTask == null
				|| priceWSTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			LOG.debug("Creating new chatGetTask...");
			priceWSTask = new PriceWSTask();
			priceWSTask.execute();
		}
	}

	private class PriceWSTask extends AsyncTask<Void, Integer, List<Price>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(PriceActivity.this);
			progress.setTitle(getResources().getString(
					R.string.title_activity_prices));
			progress.setMessage("Obteniendo apuestas del servidor");
			progress.show();
		}

		@Override
		protected List<Price> doInBackground(Void... params) {
			LectorDatosWS lector = new LectorDatosWS(PriceActivity.this);
			LOG.debug("Getting from server price history...");
			prices = lector.readPrice();
			Collections.sort(prices);
			return prices;
		}

		@Override
		protected void onPostExecute(List<Price> result) {
			// Refresh DB
			priceDao.removeAll();
			priceDao.save(result);
			
			adapter.clear();
			for (Price price : result) {
				adapter.add(price);
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

	private class BetDBTask extends AsyncTask<Void, Integer, List<Bet>> {

		private Price price;

		public BetDBTask(Price price) {
			this.price = price;
		}

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(PriceActivity.this);
			progress.setTitle(getResources().getString(
					R.string.title_activity_prices));
			progress.setMessage("Obteniendo apuestas");
			progress.show();
		}

		@Override
		protected List<Bet> doInBackground(Void... params) {
			relateBets = new ArrayList<Bet>();

			try {
				LOG.debug("Running thread: " + Thread.currentThread().getName());
				relateBets = getBetsOnDate(price);
				LOG.debug("Price history list: "
						+ (prices == null ? 0 : prices.size()));
				Collections.sort(relateBets);
				return relateBets;
			} catch (Exception e) {
				LOG.error("Error reading chat history: " + e.getMessage());
				LOG.debug("Error reading chat history", e);

				return relateBets;
			}

		}

		@Override
		protected void onPostExecute(List<Bet> result) {
			progress.dismiss();
			Ticket ticketPrice = price.getTicketPrice();
			StringBuffer sb = new StringBuffer();
			for (Bet bet : result) {
				List<Ticket> ticketsBet = bet.getTickets();
				for (Ticket ticket : ticketsBet) {
					List<Ball> hits = ticket.hitNumber(ticketPrice);
					if (hits.size() == 0) {
						sb.append("Apuesta\n  [" + ticket.toString() + "]\n  Aciertos: Ningun acierto...\n");
					} else {
						sb.append("Apuesta\n  [" + ticket.toString() + "]\n  Aciertos: ");
						for (Ball ball : hits) {
							sb.append(ball.toString() + " ");
						}
						sb.append("\n");
					}
				}
			}
            Intent i = new Intent(getApplicationContext(), PriceDialog.class);
            i.putExtra("priceDate", price.getPriceDate());
            i.putExtra("priceType", price.getPriceType());
            i.putExtra("priceNumbers", ticketPrice.toString());
            
            i.putExtra("betData", sb.toString());
            startActivity(i);
		}
	}

	private class PriceDBTask extends AsyncTask<Void, Integer, List<Price>> {

		@Override
		protected void onPreExecute() {
			LOG.debug("<-- onPreExecute");
			progress = new ProgressDialog(PriceActivity.this);
			progress.setTitle(getResources().getString(
					R.string.title_activity_prices));
			progress.setMessage("Obteniendo apuestas");
			progress.show();
		}

		@Override
		protected List<Price> doInBackground(Void... params) {
			prices = new ArrayList<Price>();

			try {
				LOG.debug("Running thread: " + Thread.currentThread().getName());
				prices = getPriceList();
				LOG.debug("Price history list: "
						+ (prices == null ? 0 : prices.size()));
				Collections.sort(prices);
				return prices;
			} catch (Exception e) {
				LOG.error("Error reading chat history: " + e.getMessage());
				LOG.debug("Error reading chat history", e);

				return prices;
			}

		}

		@Override
		protected void onPostExecute(List<Price> result) {
			progress.dismiss();
			if (result.size() != 0) {
				adapter.clear();
				for (Price price : result) {
					adapter.add(price);
				}
				adapter.notifyDataSetChanged();

				lv.postDelayed(new Runnable() {

					@Override
					public void run() {
						lv.onRefreshComplete();
					}
				}, 2000);
			} else {
				runGetPriceWSTask();
			}
		}
	}

	private ArrayList<Price> getPriceList() {
		ArrayList<Price> priceList = new ArrayList<Price>();

		try {
			priceList = (ArrayList<Price>) priceDao.findAll();
			return priceList;
		} catch (Exception e) {
			LOG.error("Error reading Price : " + e.getMessage());
			LOG.debug("Error reading Price ", e);
		}

		return priceList;
	}

	private List<Bet> getBetsOnDate(Price price) {
		ArrayList<Bet> betList = new ArrayList<Bet>();

		try {
			for (Bet bet : (ArrayList<Bet>) betDao.findByField(
					Bet.FIELD_BET_DATE, price.getPriceDate())) {
				if (bet.getBetType().equals(price.getPriceType())) {
					betList.add(bet);
				}
			}
			return betList;
		} catch (Exception e) {
			LOG.error("Error reading Price : " + e.getMessage());
			LOG.debug("Error reading Price ", e);
		}

		return betList;
	}

}
