package com.jmbg.loteriasgmv.dao.entities;

import android.provider.BaseColumns;

public interface BaseEntity extends BaseColumns {

	/** SQLite format for CURRENT_TIME. */
	public static final String CURRENT_TIME = "HH:mm:ss";
	
	/** SQLite format for CURRENT_DATE */
	public static final String CURRENT_DATE = "yyyy-MM-dd";
	
	/** SQLite format for CURRENT_TIMESTAMP */
	public static final String CURRENT_TIMESTAMP = CURRENT_DATE + " " + CURRENT_TIME;
	
	public String getTableName();
	
	public Integer getId();
	
	public void setId(Integer id);
}
