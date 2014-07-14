package com.jmbg.apuestasgmv.views;

import java.util.ArrayList;
import java.util.List;

import com.jmbg.apuestasgmv.Constants.TypeAppData;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.control.exception.ApuestasGMVException;
import com.jmbg.apuestasgmv.model.dao.BetDao;
import com.jmbg.apuestasgmv.model.dao.LotGMVDBAdapter;
import com.jmbg.apuestasgmv.model.dao.PriceDao;
import com.jmbg.apuestasgmv.model.dao.entities.Ball;
import com.jmbg.apuestasgmv.model.dao.entities.Bet;
import com.jmbg.apuestasgmv.model.dao.entities.Price;
import com.jmbg.apuestasgmv.model.dao.entities.Ticket;
import com.jmbg.apuestasgmv.views.adapters.PriceAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class PriceActivity extends CustomActivity {

	private PriceDao mPriceDao;
	private BetDao mBetDao;
	private LotGMVDBAdapter mLotGMVDBAdapter;

	private PriceViewHolder mHolder;

	private PriceAdapter mAdapter;

	public class PriceViewHolder {
		public ListView priceList;
	}

	/*
	 * ACTIVITY METHODS
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info("Create");
		setContentView(R.layout.price_activity);

		initActionBar(getResources().getString(R.string.category_prices));
		enableGoHomeButton();
		changeActionBarBrandImage(getResources().getDrawable(
				R.drawable.ic_prize));
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
			mPriceDao = new PriceDao(mLotGMVDBAdapter);
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
		return TypeAppData.PRICE;
	}

	@Override
	protected void initViewHolder() {
		mHolder = new PriceViewHolder();

		mHolder.priceList = (ListView) findViewById(R.id.price_list);
		initHolderValues();
	}

	@Override
	protected void initHolderValues() {
		iniParticipantValues();
	}

	private void iniParticipantValues() {
		List<Price> prices = mPriceDao.findAll();

		mAdapter = new PriceAdapter(this, R.layout.list_price_item, prices,
				this);

		mHolder.priceList.setAdapter(mAdapter);
	}

	public void searchPriceResults(Price item) {
		List<Bet> relatedBets = getBetsOnDate(item);

		LinearLayout layoutBets = new LinearLayout(this);
		layoutBets.setGravity(Gravity.CENTER);
		layoutBets.setOrientation(LinearLayout.VERTICAL);

		// Agregamos el ticket Ganador
		TextView text = new TextView(this);
		text.setText("Apuesta Ganadora [" + item.getPriceDate() + "]");
		text.setTextAppearance(getApplicationContext(), R.style.text_subheader);
		layoutBets.addView(text);
		layoutBets.addView(layoutAddNumbers(null, item.getTicketPrice()));

		View separator = new View(this);
		LinearLayout.LayoutParams viewLp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 2);
		separator.setLayoutParams(viewLp);
		separator.setBackgroundColor(getResources().getColor(R.color.purple));
		layoutBets.addView(separator);

		text = new TextView(this);
		text.setText("Apuestas");
		text.setTextAppearance(getApplicationContext(), R.style.text_subheader);
		layoutBets.addView(text);

		for (Bet bet : relatedBets) {
			LinearLayout layoutSingleBet = new LinearLayout(this);
			layoutSingleBet.setGravity(Gravity.CENTER);
			layoutSingleBet.setOrientation(LinearLayout.VERTICAL);

			List<Ticket> ticketsBet = bet.getTickets();
			for (Ticket ticket : ticketsBet) {
				layoutSingleBet.addView(generateTicketLayout(item, ticket));
			}
			layoutBets.addView(layoutSingleBet);
		}

		AlertTextDialog alertDialog = new AlertTextDialog(this);
		alertDialog.setTitle("Aciertos");
		alertDialog.setMessage("");
		alertDialog.setLayoutMessage(layoutBets);
		alertDialog.show();
	}

	private View generateTicketLayout(Price price, Ticket ticket) {

		LinearLayout layoutTicket = new LinearLayout(this);
		layoutTicket.setGravity(Gravity.CENTER);
		layoutTicket.setOrientation(LinearLayout.VERTICAL);

		// Agregamos los numeros de nuestros tickets
		layoutTicket.addView(layoutAddNumbers(null, ticket));
		layoutTicket.addView(layoutAddNumbers(price, ticket));

		return layoutTicket;
	}

	private LinearLayout layoutAddNumbers(Price price, Ticket ticket) {
		LayoutInflater vi = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout layoutNumeros = new LinearLayout(this);
		layoutNumeros.setGravity(Gravity.CENTER);
		layoutNumeros.setOrientation(LinearLayout.HORIZONTAL);

		List<Ball> ticketBalls = (price == null) ? ticket.getNumbers() : ticket
				.hitNumber(price.getTicketPrice());

		if (price != null) {
			TextView text = new TextView(this);
			text.setText("- Aciertos: "
					+ ((ticketBalls.size() == 0) ? "-" : ""));
			text.setTextAppearance(getApplicationContext(), R.style.text_parraf);
			layoutNumeros.addView(text);
		}

		for (Ball ball : ticketBalls) {
			View numberLayout = vi.inflate(R.layout.number_format_layout, null);

			TextView numeroView = (TextView) numberLayout
					.findViewById(R.id.number_text);
			numeroView.setText(Integer.toString(ball.getNumber()));

			ImageView numberImage = (ImageView) numberLayout
					.findViewById(R.id.number_image);
			switch (ball.getTypeBall()) {
			case STAR:
				numberImage.setImageResource((R.drawable.ic_estrella_amarilla));
				break;
			case NUMBER:
				switch (ticket.getType()) {
				case EUROMILLONES:
					numberImage.setImageResource(R.drawable.ic_bola_azul);
					break;
				case OTHER:
					numberImage.setImageResource(R.drawable.ic_bola_gris);
					break;
				case PRIMITIVA:
					numberImage.setImageResource(R.drawable.ic_bola_gris);
					break;
				}
				break;

			}
			layoutNumeros.addView(numberLayout);
		}
		return layoutNumeros;
	}

	private List<Bet> getBetsOnDate(Price price) {
		List<Bet> betList = new ArrayList<Bet>();

		try {
			for (Bet bet : (ArrayList<Bet>) mBetDao.findByField(
					Bet.FIELD_BET_DATE, price.getPriceDate())) {
				if (bet.getBetType().equals(price.getPriceType())) {
					betList.add(bet);
				}
			}
			return betList;
		} catch (Exception e) {
			logger.error("Error reading Price : " + e.getMessage());
			logger.debug("Error reading Price ", e);
		}

		return betList;
	}
}