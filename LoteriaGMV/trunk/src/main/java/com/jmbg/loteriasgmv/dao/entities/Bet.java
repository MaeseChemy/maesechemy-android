package com.jmbg.loteriasgmv.dao.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bet extends AbstractEntity implements BetBaseColumns,
		Comparable<Bet> {

	/** Fecha del bote */
	private String betDate;

	/** Cantidad del bote */
	private String betType;

	/** **/
	private byte[] betImage;

	public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIELD_BET_DATE
			+ " TEXT, " + FIELD_BET_TYPE + " TEXT, " + FIELD_BET_IMG + " BLOB"
			+ "); " + "CREATE INDEX IF NOT EXISTS IDX_" + TABLE_NAME + "_"
			+ FIELD_BET_DATE + " ON TABLE_NAME (" + FIELD_BET_DATE + ");";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			AbstractEntity.CURRENT_TIMESTAMP);

	public Bet() {
	}

	public Bet(String betDate, String betType, byte[] betImage) {
		super();
		this.betDate = betDate;
		this.betType = betType;
		this.betImage = betImage;
	}

	@Override
	public String getTableName() {
		return ParticipantBaseColumns.TABLE_NAME;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("id").append("=").append(this.id).append("]");
		sb.append("[").append("betDate").append("=").append(this.betDate)
				.append("]");
		sb.append("[").append("betType").append("=").append(this.betType)
				.append("]");
		sb.append("[").append("betImage").append("=").append(this.betImage)
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

	@Override
	public int compareTo(Bet another) {
		int compare = this.getBetDate().compareTo(another.getBetDate());
		return compare;
	}

}
