package com.jmbg.apuestasgmv.model.dao.entities;
import android.provider.BaseColumns;

public interface PotBaseColumns extends BaseColumns {

	public static final String TABLE_NAME = "pot_historic";
	
	public static final String FIELD_POT_DATE = "pot_date";
	public static final String FIELD_POT = "pot_value";
	public static final String FIELD_POT_BET = "pot_bet";
	public static final String FIELD_POT_WON = "pot_won";
	public static final String FIELD_POT_VALID ="pot_valid";
	
}