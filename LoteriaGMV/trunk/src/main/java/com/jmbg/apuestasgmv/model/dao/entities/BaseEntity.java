package com.jmbg.apuestasgmv.model.dao.entities;

import android.provider.BaseColumns;

public interface BaseEntity extends BaseColumns {

	/** SQLite format for CURRENT_TIME. */
	public static final String CURRENT_TIME = "HH:mm:ss";

	/** SQLite format for CURRENT_DATE */
	public static final String CURRENT_DATE = "dd/MM/yyyy";

	/** SQLite format for CURRENT_TIMESTAMP */
	public static final String CURRENT_TIMESTAMP = CURRENT_DATE + " "
			+ CURRENT_TIME;
	public static final String CURRENT_DAY = CURRENT_DATE;

	public String getTableName();

	public Integer getId();

	public void setId(Integer id);
}
