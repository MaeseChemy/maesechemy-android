package com.jmbg.loteriasgmv.dao.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jmbg.loteriasgmv.util.Ball;
import com.jmbg.loteriasgmv.util.Ticket;
import com.jmbg.loteriasgmv.util.Ball.TypeBall;
import com.jmbg.loteriasgmv.util.Ticket.TypeTicket;

public class Bet extends AbstractEntity implements BetBaseColumns,
		Comparable<Bet> {

	public final static int BET_ACTIVE = 1;
	public final static int BET_INACTIVE = 0;

	/** Fecha del bote */
	private String betDate;

	/** Cantidad del bote */
	private String betType;

	private String betNumbers;
	/** **/
	private byte[] betImage;

	/** **/
	private int betActive;

	public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIELD_BET_DATE
			+ " TEXT, " + FIELD_BET_TYPE + " TEXT, " + FIELD_BET_NUMBERS
			+ " TEXT, " + FIELD_BET_IMG + " BLOB, " + FIELD_BET_ACTIVE
			+ " INTEGER" + "); " + "CREATE INDEX IF NOT EXISTS IDX_"
			+ TABLE_NAME + "_" + FIELD_BET_DATE + " ON TABLE_NAME ("
			+ FIELD_BET_DATE + ");";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			AbstractEntity.CURRENT_DATE);

	public Bet() {
	}

	public Bet(String betDate, String betType, String betNumbers,
			byte[] betImage, int betActive) {
		super();
		this.betDate = betDate;
		this.betType = betType;
		this.betNumbers = betNumbers;
		this.betImage = betImage;
		this.betActive = betActive;
	}

	@Override
	public String getTableName() {
		return BetBaseColumns.TABLE_NAME;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("id").append("=").append(this.id).append("]");
		sb.append("[").append("betDate").append("=").append(this.betDate)
				.append("]");
		sb.append("[").append("betType").append("=").append(this.betType)
				.append("]");
		sb.append("[").append("betNumbers").append("=").append(this.betNumbers)
				.append("]");
		sb.append("[").append("betImage").append("=").append(this.betImage)
				.append("]");
		sb.append("[").append("betActive").append("=").append(this.betActive)
				.append("]");
		return sb.toString();
	}

	public String getBetDate() {
		return betDate;
	}

	public void setBetDate(String betDate) {
		this.betDate = betDate;
	}

	public Date getBetDateAsDate() {
		try {
			return sdf.parse(getBetDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setBetDate(Date messageDate) {
		this.setBetDate(sdf.format(messageDate));
	}

	public byte[] getBetImage() {
		return betImage;
	}

	public void setBetImage(byte[] betImage) {
		this.betImage = betImage;
	}

	public String getBetType() {
		return betType;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	public String getBetNumbers() {
		return betNumbers;
	}

	public void setBetNumbers(String betNumbers) {
		this.betNumbers = betNumbers;
	}

	public int getBetActive() {
		return betActive;
	}

	public void setBetActive(int betActive) {
		this.betActive = betActive;
	}

	@Override
	public int compareTo(Bet another) {
		int compare = this.getBetDateAsDate().compareTo(
				another.getBetDateAsDate());
		return compare;
	}

	public List<Ticket> getTickets() {
		List<Ticket> listTickets = new ArrayList<Ticket>();
		String listBets[] = getBetNumbers().split("_");
		for (String sigleBet : listBets) {
			listTickets.add(generateTicket(sigleBet));
		}
		return listTickets;
	}

	private Ticket generateTicket(String numbers) {
		Ticket ticket = new Ticket();
		String campos[] = numbers.split("-");

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
		ticket.setDate(getBetDate());

		if (this.getBetType() == "Euromillones") {
			ticket.setType(TypeTicket.EUROMILLONES);
		} else if (this.getBetType() == "Primitiva") {
			ticket.setType(TypeTicket.PRIMITIVA);
		} else {
			ticket.setType(TypeTicket.OTHER);
		}
		return ticket;
	}

}
