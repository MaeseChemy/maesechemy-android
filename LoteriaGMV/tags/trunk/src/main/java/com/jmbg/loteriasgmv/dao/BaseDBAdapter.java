package com.jmbg.loteriasgmv.dao;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public interface BaseDBAdapter {

	public AbstractDBAdapter open() throws SQLException;
	
	public void close();
	
	public SQLiteDatabase getSqlLiteDatabase();
	
	public void setSqlLiteDatabase(SQLiteDatabase sqlLiteDatabase);
}