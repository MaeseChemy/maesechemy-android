package com.jmbg.loteriasgmv.dao;

import com.jmbg.loteriasgmv.util.LogManager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractDBHelper extends SQLiteOpenHelper {

	private LogManager LOG = LogManager.getLogger(this.getClass());
	
	public AbstractDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}	
	
	protected abstract void createTables(SQLiteDatabase db);
	
	protected abstract void dropTables(SQLiteDatabase db);
	
	@Override
	public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
        	LOG.info("Creando base de datos...");
        	
        	//Se ejecuta la sentencia SQL de creación de las tablas
        	createTables(db);
        	
    		// Realizamos el commit
            db.setTransactionSuccessful();
        } catch (SQLException e) {
        	LOG.error("Error creating tables: " + e.getMessage());
        	LOG.debug("Error creating tables", e);
        } finally {
            db.endTransaction();
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {
        	LOG.info("Borrando tablas de base de datos...");
        	
        	// Eliminamos las tablas
        	dropTables(db);
			
        	LOG.info("Creando tablas de base de datos...");
        	
            //Se ejecuta la sentencia SQL de creación de las tablas
			createTables(db);
    		
    		// Realizamos el commit
            db.setTransactionSuccessful();
        } catch (SQLException e) {
        	LOG.error("Error creating tables: " + e.getMessage());
        	LOG.debug("Error creating tables", e);
        } finally {
            db.endTransaction();
        }			
	}	
}
