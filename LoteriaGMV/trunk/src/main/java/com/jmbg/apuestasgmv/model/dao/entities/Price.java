package com.jmbg.apuestasgmv.model.dao.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

import com.jmbg.apuestasgmv.model.dao.entities.Ball.TypeBall;
import com.jmbg.apuestasgmv.model.dao.entities.Ticket.TypeTicket;

public class Price extends AbstractEntity implements PriceBaseColumns,
		Comparable<Price> {

	// TYPE|FECHA_SORTEO|NUMEROS|FECHA_SIG_SORTEO|BOTE_SIG_SORTEO

	private String priceType;

	/** Fecha del bote */
	private String priceDate;

	/** Cantidad del bote */
	private String priceNumbers;

	/** Apostado */
	private String priceNextDate;

	/** Ganancia */
	private int priceNextPot;

	public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIELD_PRICE_TYPE
			+ " TEXT, " + FIELD_PRICE_DATE + " TEXT, " + FIELD_PRICE_NUMBERS
			+ " TEXT, " + FIELD_PRICE_NEXT_DATE + " TEXT, "
			+ FIELD_PRICE_NEXT_POT + " INTEGER" + "); "
			+ "CREATE INDEX IF NOT EXISTS IDX_" + TABLE_NAME + "_"
			+ FIELD_PRICE_DATE + " ON TABLE_NAME (" + FIELD_PRICE_DATE + ");";

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			AbstractEntity.CURRENT_DATE);

	public Price() {
	}

	public Price(String priceType, String priceDate, String priceNumbers,
			String priceNextDate, int priceNextPot) {
		super();
		this.priceType = priceType;
		this.priceDate = priceDate;
		this.priceNumbers = priceNumbers;
		this.priceNextDate = priceNextDate;
		this.priceNextPot = priceNextPot;
	}

	public String getTableName() {
		return Price.TABLE_NAME;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("id").append("=").append(this.id).append("]");
		sb.append("[").append("priceType").append("=").append(this.priceType)
				.append("]");
		sb.append("[").append("priceDate").append("=").append(this.priceDate)
				.append("]");
		sb.append("[").append("priceNumbers").append("=")
				.append(this.priceNumbers).append("]");
		sb.append("[").append("priceNextDate").append("=")
				.append(this.priceNextDate).append("]");
		sb.append("[").append("priceNextPot").append("=")
				.append(this.priceNextPot).append("]");

		return sb.toString();
	}

	public String getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(String priceDate) {
		this.priceDate = priceDate;
	}

	public Date getPriceDateAsDate() {
		try {
			return sdf.parse(getPriceDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setPriceDate(Date messageDate) {
		this.setPriceDate(sdf.format(messageDate));
	}

	public String getPriceNextDate() {
		return priceNextDate;
	}

	public void setPriceNextDate(String priceNextDate) {
		this.priceNextDate = priceNextDate;
	}

	public Date getPriceNextDateAsDate() {
		try {
			return sdf.parse(getPriceDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setPriceNextDate(Date messageDate) {
		this.setPriceDate(sdf.format(messageDate));
	}

	public String getPriceNumbers() {
		return priceNumbers;
	}

	public String getPriceNumbersFormat() {
		String campos[] = getPriceNumbers().split("-");
		StringBuffer sb = new StringBuffer();

		String numeros = "";
		String estrellas = " - ";
		for (String campo : campos) {
			if (campo.contains("N")) {
				numeros += campo.replace("N", "") + " ";
			} else if (campo.contains("E")) {
				estrellas += campo.replace("E", "") + " ";
			}
		}
		sb.append(numeros);
		if (getPriceType().equals("Euromillones"))
			sb.append(estrellas);
		return sb.toString();
	}

	public void setPriceNumbers(String priceNumbers) {
		this.priceNumbers = priceNumbers;
	}

	public int getPriceNextPot() {
		return priceNextPot;
	}

	public void setPriceNextPot(int priceNextPot) {
		this.priceNextPot = priceNextPot;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public int compareTo(Price another) {
		int compare = this.getPriceDateAsDate().compareTo(
				another.getPriceDateAsDate());
		return compare;
	}

	public Ticket getTicketPrice() {
		Ticket ticket = new Ticket();
		String campos[] = getPriceNumbers().split("-");

		List<Ball> balls = new ArrayList<Ball>();
		for (String campo : campos) {
			Ball ball = new Ball();
			String number = "-1";
			if (campo.contains("N")) {
				ball.setTypeBall(TypeBall.NUMBER);
				number = campo.replace("N", "");
			} else if (campo.contains("E")) {
				ball.setTypeBall(TypeBall.STAR);
				number = campo.replace("E", "");
			}
			ball.setNumber(Integer.parseInt(number));
			balls.add(ball);
		}

		ticket.setNumbers(balls);
		ticket.setDate(getPriceDate());

		if (this.getPriceType().equals("Euromillones")) {
			ticket.setType(TypeTicket.EUROMILLONES);
		} else if (this.getPriceType().equals("Primitiva")) {
			ticket.setType(TypeTicket.PRIMITIVA);
		} else {
			ticket.setType(TypeTicket.OTHER);
		}
		return ticket;
	}

}
