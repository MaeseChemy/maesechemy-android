package com.jmbg.loteriasgmv.dao.entities;
import android.provider.BaseColumns;

public interface ParticipantBaseColumns extends BaseColumns {

	public static final String TABLE_NAME = "participants";
	
	public static final String FIELD_PARTICIPANT_NAME = "part_name";
	public static final String FIELD_PARTICIPANT_FUND = "part_fund";
	public static final String FIELD_PARTICIPANT_IMG_URL = "part_img_url";
	
}