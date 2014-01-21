package com.jmbg.loteriasgmv.dao;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;

import com.jmbg.loteriasgmv.dao.entities.Pot;

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
		 * Se ejecuta la sentencia SQL de creación de las tablas
		 * @param db
		 */
		@Override
		protected void createTables(SQLiteDatabase db) {
	        //Se ejecuta la sentencia SQL de creación de las tablas
			db.execSQL(Pot.SQL_CREATE);
		}
		
		/**
		 * Elimina las tablas de la base de datos
		 * @param db
		 */
		@Override
		protected void dropTables(SQLiteDatabase db) {
	        // Eliminamos las tablas
			db.execSQL("DROP TABLE IF EXISTS " + Pot.TABLE_NAME);
		}
	}	
}
