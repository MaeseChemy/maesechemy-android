package com.jmbg.apuestasgmv.model.dao;

import com.jmbg.apuestasgmv.model.dao.entities.Price;

import android.content.ContentValues;
import android.database.Cursor;

public class PriceDao extends AbstractDao<Price> {

	public PriceDao(LotGMVDBAdapter lotGMVDBAdapter) {
		super(lotGMVDBAdapter, Price.class);
	}

	@Override
	protected Price cursorToEntity(Cursor cursor) {
		Price entity = new Price();

		entity.setId(cursor.getInt(cursor.getColumnIndex(Price._ID)));
		entity.setPriceType(cursor.getString(cursor
				.getColumnIndex(Price.FIELD_PRICE_TYPE)));
		entity.setPriceDate(cursor.getString(cursor
				.getColumnIndex(Price.FIELD_PRICE_DATE)));
		entity.setPriceNumbers(cursor.getString(cursor
				.getColumnIndex(Price.FIELD_PRICE_NUMBERS)));
		entity.setPriceNextDate(cursor.getString(cursor
				.getColumnIndex(Price.FIELD_PRICE_NEXT_DATE)));
		entity.setPriceNextPot(cursor.getInt(cursor
				.getColumnIndex(Price.FIELD_PRICE_NEXT_POT)));

		return entity;
	}

	@Override
	protected ContentValues entityToContentValues(Price entity) {
		ContentValues contentValues = new ContentValues();

		contentValues.put(Price._ID, entity.getId());
		contentValues.put(Price.FIELD_PRICE_TYPE, entity.getPriceType());
		contentValues.put(Price.FIELD_PRICE_DATE, entity.getPriceDate());
		contentValues.put(Price.FIELD_PRICE_NUMBERS, entity.getPriceNumbers());
		contentValues.put(Price.FIELD_PRICE_NEXT_DATE,
				entity.getPriceNextDate());
		contentValues.put(Price.FIELD_PRICE_NEXT_POT, entity.getPriceNextPot());

		return contentValues;
	}

}
