package com.jmbg.oldgloriescalendar.data;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.util.LectorLiga;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LigaDBSQLite extends SQLiteOpenHelper {

	private Context context;

	private final static String DB_TABLA = "PARTIDOS";
	private final static String DB_CAMPO_JORNADA = "JORNADA";
	private final static String DB_CAMPO_FECHA = "FECHA";
	private final static String DB_CAMPO_OPONENTE = "OPONENTE";
	private final static String DB_CAMPO_CAMPO = "CAMPO";
	private final static String DB_CAMPO_LOCAL = "LOCAL";

	private final static String DB_TABLA_EQUIPO = "EQUIPOS";
	private final static String DB_CAMPO_EQUIPO = "EQUIPO";
	private final static String DB_CAMPO_CAMISETA = "CAMISETA";
	
	private final static String SENTENCIA_CREATE_TABLE = "CREATE TABLE "
			+ DB_TABLA + " (" + DB_CAMPO_JORNADA + " TEXT PRIMARY KEY, "
			+ DB_CAMPO_FECHA + " LONG," + "" + DB_CAMPO_OPONENTE + " TEXT,"
			+ "" + DB_CAMPO_CAMPO + " TEXT," + "" + DB_CAMPO_LOCAL
			+ " INTEGER)";

	private final static String SENTENCIA_CREATE_TABLE_EQUIPOS = "CREATE TABLE "
			+ DB_TABLA_EQUIPO + " (" + DB_CAMPO_EQUIPO + " TEXT PRIMARY KEY, "
			+ DB_CAMPO_CAMISETA + ")";
	
	private final static String SENTENCIA_DROP_TABLE = "DROP TABLE IF EXISTS "
			+ DB_TABLA;
	private final static String SENTENCIA_DROP_TABLE_EQUIPOS = "DROP TABLE IF EXISTS "
			+ DB_TABLA_EQUIPO;
	
	private final static int DB_VERSION = 2;

	public LigaDBSQLite(Context context, String name, CursorFactory factory) {
		super(context, name, factory, DB_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SENTENCIA_CREATE_TABLE);
		db.execSQL(SENTENCIA_CREATE_TABLE_EQUIPOS);
		this.cargarDB(db);
	}

	private void cargarDB(SQLiteDatabase db) {
		LectorLiga lector = new LectorLiga(context);
		List<Partido> partidosFichero = lector.obtenerPartidos();
		
		for (Partido partido : partidosFichero) {
			this.guardarPartido(partido, db);
		}
		
		List<Equipo> equiposFichero = lector.obtenerEquipos();
		for (Equipo equipo : equiposFichero) {
			this.guardarEquipo(equipo, db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(LigaDBSQLite.class.getName(),
	            "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
		db.execSQL(SENTENCIA_DROP_TABLE);
		db.execSQL(SENTENCIA_DROP_TABLE_EQUIPOS);
		// Se crea la nueva versión de la tabla
		onCreate(db);
	}

	public void guardarPartido(Partido partido, SQLiteDatabase db) {

		db.execSQL("INSERT INTO " + DB_TABLA + " VALUES('"
				+ partido.getJornada() + "', " + partido.getFechaLong() + ", '"
				+ partido.getOponente() + "', '" + partido.getLugar() + "', "
				+ (partido.isLocal() ? 1 : 0) + ")");
	}
	
	public void guardarEquipo(Equipo equipo, SQLiteDatabase db) {

		db.execSQL("INSERT INTO " + DB_TABLA_EQUIPO + " VALUES('"
				+ equipo.getNombre() + "', '" + equipo.getCamiseta()+"')");
	}

	public Vector<Partido> listaPartidos() {
		Vector<Partido> partidos = new Vector<Partido>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT " + DB_CAMPO_JORNADA + ", "
				+ DB_CAMPO_FECHA + ", " + DB_CAMPO_OPONENTE + ", "
				+ DB_CAMPO_CAMPO + ", " + DB_CAMPO_LOCAL + " FROM " + DB_TABLA,
				null);

		while (cursor.moveToNext()) {
			Partido partido = new Partido();

			partido.setJornada(cursor.getString(0));
			partido.setFechaLong(cursor.getLong(1));
			partido.setOponente(cursor.getString(2));
			partido.setLugar(cursor.getString(3));
			partido.setLocal(cursor.getInt(4) == 0 ? false : true);

			partidos.add(partido);
		}

		db.close();
		return partidos;
	}

	public Vector<Partido> listaPartidos(Calendar calendar) {
		Vector<Partido> partidos = new Vector<Partido>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery(
				"SELECT " + DB_CAMPO_JORNADA + ", " + DB_CAMPO_FECHA + ", "
						+ DB_CAMPO_OPONENTE + ", " + DB_CAMPO_CAMPO + ", "
						+ DB_CAMPO_LOCAL + " FROM " + DB_TABLA + " WHERE "
						+ DB_CAMPO_FECHA + " >= " + calendar.getTimeInMillis(),
				null);

		while (cursor.moveToNext()) {
			Partido partido = new Partido();

			partido.setJornada(cursor.getString(0));
			partido.setFechaLong(cursor.getLong(1));
			partido.setOponente(cursor.getString(2));
			partido.setLugar(cursor.getString(3));
			partido.setLocal(cursor.getInt(4) == 0 ? false : true);

			partidos.add(partido);
		}

		db.close();
		return partidos;
	}
	
	public Vector<Partido> listaPartidosEquipo(String oponente) {
		Vector<Partido> partidos = new Vector<Partido>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery(
				"SELECT " + DB_CAMPO_JORNADA + ", " + DB_CAMPO_FECHA + ", "
						+ DB_CAMPO_OPONENTE + ", " + DB_CAMPO_CAMPO + ", "
						+ DB_CAMPO_LOCAL + " FROM " + DB_TABLA + " WHERE "
						+ DB_CAMPO_OPONENTE + " = '" + oponente +"' ORDER BY "+DB_CAMPO_FECHA,
				null);

		while (cursor.moveToNext()) {
			Partido partido = new Partido();

			partido.setJornada(cursor.getString(0));
			partido.setFechaLong(cursor.getLong(1));
			partido.setOponente(cursor.getString(2));
			partido.setLugar(cursor.getString(3));
			partido.setLocal(cursor.getInt(4) == 0 ? false : true);

			partidos.add(partido);
		}

		db.close();
		return partidos;
	}
	
	public Vector<Equipo> listaEquipos() {
		Vector<Equipo> equipos = new Vector<Equipo>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT " + DB_CAMPO_EQUIPO + ", "+ DB_CAMPO_CAMISETA
				+ " FROM " + DB_TABLA_EQUIPO,
				null);

		while (cursor.moveToNext()) {
			Equipo equipo = new Equipo();
			equipo.setNombre(cursor.getString(0));
			equipo.setCamiseta(cursor.getString(1));			
			equipos.add(equipo);
		}

		db.close();
		return equipos;
	}
	
	public Vector<String> listaCampos() {
		Vector<String> campos = new Vector<String>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT DISTINCT(" + DB_CAMPO_CAMPO + ") "
				+ " FROM " + DB_TABLA,
				null);

		while (cursor.moveToNext()) {
			String campo = cursor.getString(0);

			campos.add(campo);
		}

		db.close();
		return campos;
	}
	

}
