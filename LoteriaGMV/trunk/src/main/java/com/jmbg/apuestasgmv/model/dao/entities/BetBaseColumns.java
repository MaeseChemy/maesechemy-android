package com.jmbg.apuestasgmv.model.dao.entities;
import android.provider.BaseColumns;

public interface BetBaseColumns extends BaseColumns {

	public static final String TABLE_NAME = "bets";
	
	public static final String FIELD_BET_DATE = "bet_date";
	public static final String FIELD_BET_TYPE = "bet_type";
	public static final String FIELD_BET_NUMBERS = "bet_numbers";
	public static final String FIELD_BET_IMG = "bet_img";
	public static final String FIELD_BET_ACTIVE = "bet_valid";
	
}