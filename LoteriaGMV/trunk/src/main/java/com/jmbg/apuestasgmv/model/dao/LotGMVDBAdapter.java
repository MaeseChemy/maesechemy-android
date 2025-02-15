package com.jmbg.apuestasgmv.model.dao;

import com.jmbg.apuestasgmv.model.dao.entities.Bet;
import com.jmbg.apuestasgmv.model.dao.entities.Participant;
import com.jmbg.apuestasgmv.model.dao.entities.Pot;
import com.jmbg.apuestasgmv.model.dao.entities.Price;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;


public class LotGMVDBAdapter extends AbstractDBAdapter {

	// private GNALogManager gnaLog = GNALogManager.getLogger(this.getClass());
	
	public LotGMVDBAdapter(Context context) throws NumberFormatException, NameNotFoundException {
		super(context, new LotGMVSQLiteHelper(context));
	}

	private static class LotGMVSQLiteHelper extends AbstractDBHelper {

		private static final String DATABASE_NAME = "lot_gmv.db";
		
		//private static final  int DATABASE_VERSION = 1;
		
		public LotGMVSQLiteHelper(Context context) throws NumberFormatException, NameNotFoundException {
			//super(context, DATABASE_NAME, null, DATABASE_VERSION);
			super(context, DATABASE_NAME, null, Integer.parseInt((context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName).replaceAll("[^0-9]+", "")));
		}
		
		/**
		 * Se ejecuta la sentencia SQL de creaci�n de las tablas
		 * @param db
		 */
		@Override
		protected void createTables(SQLiteDatabase db) {
	        //Se ejecuta la sentencia SQL de creaci�n de las tablas
			db.execSQL(Pot.SQL_CREATE);
			db.execSQL(Participant.SQL_CREATE);
			db.execSQL(Bet.SQL_CREATE);
			db.execSQL(Price.SQL_CREATE);
		}
		
		/**
		 * Elimina las tablas de la base de datos
		 * @param db
		 */
		@Override
		protected void dropTables(SQLiteDatabase db) {
	        // Eliminamos las tablas
			db.execSQL("DROP TABLE IF EXISTS " + Pot.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Participant.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Bet.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Price.TABLE_NAME);
		}
	}	
}
