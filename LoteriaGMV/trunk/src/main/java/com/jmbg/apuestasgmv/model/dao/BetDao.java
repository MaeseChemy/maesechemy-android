package com.jmbg.apuestasgmv.model.dao;

import com.jmbg.apuestasgmv.model.dao.entities.Bet;

import android.content.ContentValues;
import android.database.Cursor;


public class BetDao extends AbstractDao<Bet> {

	public BetDao(LotGMVDBAdapter lotGMVDBAdapter) {
		super(lotGMVDBAdapter, Bet.class);
	}

	@Override
	protected Bet cursorToEntity(Cursor cursor) {
		Bet entity = new Bet();

		entity.setId(cursor.getInt(cursor.getColumnIndex(Bet._ID)));
		entity.setBetDate(cursor.getString(cursor
				.getColumnIndex(Bet.FIELD_BET_DATE)));
		entity.setBetType(cursor.getString(cursor
				.getColumnIndex(Bet.FIELD_BET_TYPE)));
		entity.setBetNumbers(cursor.getString(cursor
				.getColumnIndex(Bet.FIELD_BET_NUMBERS)));
		entity.setBetImage(cursor.getBlob(cursor
				.getColumnIndex(Bet.FIELD_BET_IMG)));
		entity.setBetActive(cursor.getInt(cursor
				.getColumnIndex(Bet.FIELD_BET_ACTIVE)));
		return entity;
	}

	@Override
	protected ContentValues entityToContentValues(Bet entity) {
		ContentValues contentValues = new ContentValues();

		contentValues.put(Bet._ID, entity.getId());
		contentValues.put(Bet.FIELD_BET_DATE, entity.getBetDate());
		contentValues.put(Bet.FIELD_BET_TYPE, entity.getBetType());
		contentValues.put(Bet.FIELD_BET_NUMBERS, entity.getBetNumbers());
		contentValues.put(Bet.FIELD_BET_IMG, entity.getBetImage());
		contentValues.put(Bet.FIELD_BET_ACTIVE, entity.getBetActive());
		return contentValues;
	}

}
