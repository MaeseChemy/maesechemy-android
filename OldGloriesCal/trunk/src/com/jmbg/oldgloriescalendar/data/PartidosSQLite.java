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

public class PartidosSQLite extends SQLiteOpenHelper {

	private Context context;

	private final static String DB_TABLA = "PARTIDOS";
	private final static String DB_CAMPO_JORNADA = "JORNADA";
	private final static String DB_CAMPO_FECHA = "FECHA";
	private final static String DB_CAMPO_OPONENTE = "OPONENTE";
	private final static String DB_CAMPO_CAMPO = "CAMPO";
	private final static String DB_CAMPO_LOCAL = "LOCAL";

	private final static String SENTENCIA_CREATE_TABLE = "CREATE TABLE "
			+ DB_TABLA + " (" + DB_CAMPO_JORNADA + " TEXT PRIMARY KEY, "
			+ DB_CAMPO_FECHA + " LONG," + "" + DB_CAMPO_OPONENTE + " TEXT,"
			+ "" + DB_CAMPO_CAMPO + " TEXT," + "" + DB_CAMPO_LOCAL
			+ " INTEGER)";

	private final static String SENTENCIA_DROP_TABLE = "DROP TABLE IF EXISTS "
			+ DB_TABLA;

	public PartidosSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SENTENCIA_CREATE_TABLE);

		this.poblarBD(db);
	}

	private void poblarBD(SQLiteDatabase db) {
		LectorLiga lector = new LectorLiga(context);
		List<Partido> partidosFichero = lector.leerPartidos();

		for (Partido partido : partidosFichero) {
			this.guardarPartido(partido, db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Se elimina la versión anterior de la tabla
		db.execSQL(SENTENCIA_DROP_TABLE);

		// Se crea la nueva versión de la tabla
		onCreate(db);
	}

	public void guardarPartido(Partido partido, SQLiteDatabase db) {

		db.execSQL("INSERT INTO " + DB_TABLA + " VALUES('"
				+ partido.getJornada() + "', " + partido.getFechaLong() + ", '"
				+ partido.getOponente() + "', '" + partido.getLugar() + "', "
				+ (partido.isLocal() ? 1 : 0) + ")");
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
	
	public Vector<String> listaEquipos() {
		Vector<String> oponentes = new Vector<String>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT DISTINCT(" + DB_CAMPO_OPONENTE + ") "
				+ " FROM " + DB_TABLA,
				null);

		while (cursor.moveToNext()) {
			String oponente = cursor.getString(0);

			oponentes.add(oponente);
		}

		db.close();
		return oponentes;
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
