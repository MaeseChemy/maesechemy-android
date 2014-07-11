package com.jmbg.apuestasgmv.model.dao;

import com.jmbg.apuestasgmv.model.dao.entities.Pot;

import android.content.ContentValues;
import android.database.Cursor;


public class PotDao extends AbstractDao<Pot> {

	public PotDao(LotGMVDBAdapter lotGMVDBAdapter) {
		super(lotGMVDBAdapter, Pot.class);
	}

	@Override
	protected Pot cursorToEntity(Cursor cursor) {
		Pot entity = new Pot();

		entity.setId(cursor.getInt(cursor.getColumnIndex(Pot._ID)));
		entity.setPotDate(cursor.getString(cursor.getColumnIndex(Pot.FIELD_POT_DATE)));
		entity.setPotValue(cursor.getFloat(cursor.getColumnIndex(Pot.FIELD_POT)));
		entity.setPotBet(cursor.getFloat(cursor.getColumnIndex(Pot.FIELD_POT_BET)));
		entity.setPotWon(cursor.getFloat(cursor.getColumnIndex(Pot.FIELD_POT_WON)));
		entity.setPotValid(cursor.getInt(cursor.getColumnIndex(Pot.FIELD_POT_VALID)));

		return entity;
	}

	@Override
	protected ContentValues entityToContentValues(Pot entity) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(Pot._ID, entity.getId());
		contentValues.put(Pot.FIELD_POT_DATE, entity.getPotDate());
		contentValues.put(Pot.FIELD_POT, entity.getPotValue());
		contentValues.put(Pot.FIELD_POT_BET, entity.getPotBet());
		contentValues.put(Pot.FIELD_POT_WON, entity.getPotWon());
		contentValues.put(Pot.FIELD_POT_VALID, entity.getPotValid());

		return contentValues;
	}

}
