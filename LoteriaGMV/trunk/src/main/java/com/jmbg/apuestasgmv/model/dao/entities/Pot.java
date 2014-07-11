package com.jmbg.apuestasgmv.model.dao.entities;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pot extends AbstractEntity implements
		PotBaseColumns, Comparable<Pot>{
	
	public final static int POT_VALID = 1;
	public final static int POT_INVALID = 0;
	
	/** Fecha del bote */
	private String potDate;
	
	/** Cantidad del bote */
	private float potValue;
	
	/** Apostado */
	private float potBet;
	
	/** Ganancia */
	private float potWon;
	
	/** Si el bote es valido */
	private int potValid;

	public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FIELD_POT_DATE + " TEXT, " 
			+ FIELD_POT + " TEXT, " 
			+ FIELD_POT_BET + " TEXT, "
			+ FIELD_POT_WON + " TEXT, "
			+ FIELD_POT_VALID + " INTEGER" + "); "
			+ "CREATE INDEX IF NOT EXISTS IDX_" + TABLE_NAME + "_" + FIELD_POT_DATE + " ON TABLE_NAME (" + FIELD_POT_DATE + ");";

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			AbstractEntity.CURRENT_DATE);
	
	public Pot() {}

	public Pot(String potDate, float potValue, float potBet, float potWon, int potValid) {
		super();
		this.potDate = potDate;
		this.potValue = potValue;
		this.potValid = potValid;
		this.potBet = potBet;
		this.potWon = potWon;
	}
	
	public Pot(Date potDate, float potValue, float potBet, float potWon, int potValid) {
		this((String)null, potValue, potBet, potWon, potValid);
		this.setPotDate(potDate);
	}

	public String getTableName() {
		return PotBaseColumns.TABLE_NAME;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("id").append("=").append(this.id).append("]");
		sb.append("[").append("potDate").append("=").append(this.potDate).append("]");
		sb.append("[").append("potValue").append("=").append(this.potValue).append("]");
		sb.append("[").append("potBet").append("=").append(this.potBet).append("]");
		sb.append("[").append("potWon").append("=").append(this.potWon).append("]");
		sb.append("[").append("potValid").append("=").append(this.potValid).append("]");
		
		return sb.toString();
	}

	public String getPotDate() {
		return potDate;
	}
	
	public void setPotDate(String potDate) {
		this.potDate = potDate;
	}
	
	public Date getPotDateAsDate() {
		try {
			return sdf.parse(getPotDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setPotDate(Date messageDate) {
		this.setPotDate(sdf.format(messageDate));
	}
	
	public float getPotValue() {
		return potValue;
	}
	
	public void setPotValue(float potValue) {
		this.potValue = potValue;
	}
	
	public float getPotBet() {
		return potBet;
	}
	
	public void setPotBet(float potBet) {
		this.potBet = potBet;
	}
	
	public float getPotWon() {
		return potWon;
	}
	
	public void setPotWon(float potWon) {
		this.potWon = potWon;
	}
	
	public int getPotValid() {
		return potValid;
	}
	
	public void setPotValid(int potValid) {
		this.potValid = potValid;
	}

	public int compareTo(Pot another) {
		int compare = this.getPotDateAsDate().compareTo(another.getPotDateAsDate());
		return compare; 
	}

}
