package com.jmbg.apuestasgmv.model.dao.entities;
import android.provider.BaseColumns;

public interface PriceBaseColumns extends BaseColumns {

	//TYPE|FECHA_SORTEO|NUMEROS|FECHA_SIG_SORTEO|BOTE_SIG_SORTEO
	public static final String TABLE_NAME = "price";
	
	public static final String FIELD_PRICE_TYPE = "price_type";
	public static final String FIELD_PRICE_DATE = "price_date";
	public static final String FIELD_PRICE_NUMBERS = "price_numbers";
	public static final String FIELD_PRICE_NEXT_DATE = "price_next_date";
	public static final String FIELD_PRICE_NEXT_POT ="price_next_pot";
	
}