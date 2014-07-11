package com.jmbg.loteriasgmv.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractDBAdapter implements BaseDBAdapter {

	protected Context context;
	
	protected SQLiteDatabase sqlLiteDatabase;
	
	protected AbstractDBHelper databaseHelper;
	
	public AbstractDBAdapter(Context context, AbstractDBHelper databaseHelper) {
		this.context = context;
		this.databaseHelper = databaseHelper;
	}
	
	/**
	 * Open the database. Please use AbstractDBAdapter.close() when finish.
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AbstractDBAdapter open() throws SQLException {
		sqlLiteDatabase = databaseHelper.getWritableDatabase();
		return this;
	}
	
	@Override
	public void close() {
		databaseHelper.close();
	}
	
	public SQLiteDatabase getSqlLiteDatabase() {
		return sqlLiteDatabase;
	}

	public void setSqlLiteDatabase(SQLiteDatabase sqlLiteDatabase) {
		this.sqlLiteDatabase = sqlLiteDatabase;
	}	
}
