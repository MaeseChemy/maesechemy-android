package com.jmbg.loteriasgmv.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.jmbg.loteriasgmv.dao.entities.Participant;

public class ParticipantDao extends AbstractDao<Participant> {

	public ParticipantDao(LotGMVDBAdapter lotGMVDBAdapter) {
		super(lotGMVDBAdapter, Participant.class);
	}

	@Override
	protected Participant cursorToEntity(Cursor cursor) {
		Participant entity = new Participant();

		entity.setId(cursor.getInt(cursor.getColumnIndex(Participant._ID)));
		entity.setParticipantName(cursor.getString(cursor.getColumnIndex(Participant.FIELD_PARTICIPANT_NAME)));
		entity.setParticipantFund(cursor.getFloat(cursor.getColumnIndex(Participant.FIELD_PARTICIPANT_FUND)));

		return entity;
	}

	@Override
	protected ContentValues entityToContentValues(Participant entity) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(Participant._ID, entity.getId());
		contentValues.put(Participant.FIELD_PARTICIPANT_NAME, entity.getParticipantName());
		contentValues.put(Participant.FIELD_PARTICIPANT_FUND, entity.getParticipantFund());

		return contentValues;
	}

}
