package com.jmbg.oldgloriescalendar.data;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.jmbg.oldgloriescalendar.planitlla.PlantillaActivity;
import com.jmbg.oldgloriescalendar.util.LectorLiga;
import com.jmbg.oldgloriescalendar.util.LectorPlantilla;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

public class LigaDBSQLite extends SQLiteOpenHelper {

	private Context context;

	/* TABLA JORNADAS */
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

	/* TABLA EQUIPOS */
	private final static String DB_TABLA_EQUIPO = "EQUIPOS";
	private final static String DB_CAMPO_EQUIPO_EQUIPO = "EQUIPO";
	private final static String DB_CAMPO_EQUIPO_CAMISETA = "CAMISETA";

	private final static String SENTENCIA_CREATE_TABLE_EQUIPOS = "CREATE TABLE "
			+ DB_TABLA_EQUIPO
			+ " ("
			+ DB_CAMPO_EQUIPO_EQUIPO
			+ " TEXT PRIMARY KEY, " + DB_CAMPO_EQUIPO_CAMISETA + ")";
	private final static String SENTENCIA_DROP_TABLE_EQUIPOS = "DROP TABLE IF EXISTS "
			+ DB_TABLA_EQUIPO;

	/* TABLA PLANTILLA */
	private final static String DB_TABLA_PLANTILLA = "PLANTILLA";
	private final static String DB_CAMPO_PLANTILLA_DORSAL = "DORSAL";
	private final static String DB_CAMPO_PLANTILLA_NOMBRE = "NOMBRE";
	private final static String DB_CAMPO_PLANTILLA_GOLES = "GOLES";
	private final static String DB_CAMPO_PLANTILLA_TARAMA = "TARAMA";
	private final static String DB_CAMPO_PLANTILLA_TARROJ = "TARROJ";

	private final static String SENTENCIA_CREATE_TABLE_PLANTILLA = "CREATE TABLE "
			+ DB_TABLA_PLANTILLA
			+ " ("
			+ DB_CAMPO_PLANTILLA_DORSAL
			+ " INTEGER PRIMARY KEY, "
			+ DB_CAMPO_PLANTILLA_NOMBRE
			+ " TEXT,"
			+ ""
			+ DB_CAMPO_PLANTILLA_GOLES
			+ " INTEGER,"
			+ ""
			+ DB_CAMPO_PLANTILLA_TARAMA
			+ " INTEGER,"
			+ ""
			+ DB_CAMPO_PLANTILLA_TARROJ + " INTEGER)";
	private final static String SENTENCIA_DROP_TABLE_PLANTILLA = "DROP TABLE IF EXISTS "
			+ DB_TABLA_PLANTILLA;

	/* VERSION DATABASE */
	private final static int DB_VERSION = 3;

	public LigaDBSQLite(Context context, String name, CursorFactory factory) {
		super(context, name, factory, DB_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SENTENCIA_CREATE_TABLE);
		db.execSQL(SENTENCIA_CREATE_TABLE_EQUIPOS);
		db.execSQL(SENTENCIA_CREATE_TABLE_PLANTILLA);
		this.cargarDB(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LigaDBSQLite.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL(SENTENCIA_DROP_TABLE);
		db.execSQL(SENTENCIA_DROP_TABLE_EQUIPOS);
		db.execSQL(SENTENCIA_DROP_TABLE_PLANTILLA);
		// Se crea la nueva versi�n de la tabla
		onCreate(db);
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

		refrescarJugadores(db);
	}

	/* OPERACIONES PARA PARTIDOS */
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

		Cursor cursor = db.rawQuery("SELECT " + DB_CAMPO_JORNADA + ", "
				+ DB_CAMPO_FECHA + ", " + DB_CAMPO_OPONENTE + ", "
				+ DB_CAMPO_CAMPO + ", " + DB_CAMPO_LOCAL + " FROM " + DB_TABLA
				+ " WHERE " + DB_CAMPO_OPONENTE + " = '" + oponente
				+ "' ORDER BY " + DB_CAMPO_FECHA, null);

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

	/* OPERACIONES PARA EQUIPOS */
	public void guardarEquipo(Equipo equipo, SQLiteDatabase db) {
		db.execSQL("INSERT INTO " + DB_TABLA_EQUIPO + " VALUES('"
				+ equipo.getNombre() + "', '" + equipo.getCamiseta() + "')");
	}

	public Vector<Equipo> listaEquipos() {
		Vector<Equipo> equipos = new Vector<Equipo>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT " + DB_CAMPO_EQUIPO_EQUIPO + ", "
				+ DB_CAMPO_EQUIPO_CAMISETA + " FROM " + DB_TABLA_EQUIPO, null);

		while (cursor.moveToNext()) {
			Equipo equipo = new Equipo();
			equipo.setNombre(cursor.getString(0));
			equipo.setCamiseta(cursor.getString(1));
			equipos.add(equipo);
		}

		db.close();
		return equipos;
	}

	/* OPERACIONES PARA JUGADORES */
	public void actualizarPlantilla() {
		SQLiteDatabase db = getWritableDatabase();
		//db.execSQL(SENTENCIA_DROP_TABLE_PLANTILLA);
		//db.execSQL(SENTENCIA_CREATE_TABLE_PLANTILLA);
		refrescarJugadores(db);
	}
	
	private void refrescarJugadores(SQLiteDatabase db) {
		LectorPlantillaTask task = new LectorPlantillaTask();
		task.execute(db);
	}
	
	public void guardarJugador(Jugador jugador, SQLiteDatabase db) {
		db.execSQL("INSERT INTO " + DB_TABLA_PLANTILLA + " VALUES("
				+ jugador.getDorsal() + ", '" + jugador.getNombre() + "', "
				+ jugador.getGoles() + ", " + jugador.getTarjetasAmarillas()
				+ ", " + jugador.getTarjetasRojas() + ")");
	}
	
	public void actualizarDatosJugador(Jugador jugador, SQLiteDatabase db) {
		Log.w(LigaDBSQLite.class.getName(), "Upgrading jugador ["+jugador.toString()+"]");
		
		ContentValues content = new ContentValues();
		content.put(DB_CAMPO_PLANTILLA_NOMBRE, jugador.getNombre());
		content.put(DB_CAMPO_PLANTILLA_GOLES, jugador.getGoles());
		content.put(DB_CAMPO_PLANTILLA_TARAMA, jugador.getTarjetasAmarillas());
		content.put(DB_CAMPO_PLANTILLA_TARROJ, jugador.getTarjetasRojas());
		
		String whereClause = DB_CAMPO_PLANTILLA_DORSAL + "=="+ jugador.getDorsal();
		
		db.update(DB_TABLA_PLANTILLA, content, whereClause, null);

		Log.w(LigaDBSQLite.class.getName(), "Completo...");

	}

	public Vector<Jugador> listaJugadores() {
		Vector<Jugador> plantilla = new Vector<Jugador>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT " 
				+ DB_CAMPO_PLANTILLA_DORSAL + ", " 
				+ DB_CAMPO_PLANTILLA_NOMBRE	+ ", " 
				+ DB_CAMPO_PLANTILLA_GOLES + ", "
				+ DB_CAMPO_PLANTILLA_TARAMA + ", " 
				+ DB_CAMPO_PLANTILLA_TARROJ
				+ " FROM " + DB_TABLA_PLANTILLA, null);

		while (cursor.moveToNext()) {
			Jugador jugador = new Jugador();
			jugador.setDorsal(cursor.getInt(0));
			jugador.setNombre(cursor.getString(1));
			jugador.setGoles(cursor.getInt(2));
			jugador.setTarjetasAmarillas(cursor.getInt(3));
			jugador.setTarjetasRojas(cursor.getInt(4));
			plantilla.add(jugador);
		}

		db.close();
		return plantilla;
	}
	
	public void actualizarJugador(Jugador jugador, SQLiteDatabase db){
		if(!db.isOpen())
			db = getWritableDatabase();
		if(existeJugadorPlantilla(jugador, db)){
			this.actualizarDatosJugador(jugador, db);
		}else{
			this.guardarJugador(jugador, db);
		}
		db.close();
	}

	private boolean existeJugadorPlantilla(Jugador jugador, SQLiteDatabase db) {
		boolean exist = false;
		Cursor cursor = db.rawQuery(
				"SELECT " + DB_CAMPO_PLANTILLA_DORSAL + " FROM "
						+ DB_TABLA_PLANTILLA 
						+ " WHERE "
						+ DB_CAMPO_PLANTILLA_DORSAL + "==" + jugador.getDorsal(), null);
		
		while (cursor.moveToNext()) {
			exist = true;
		}
		return exist;
	}

	/* CLASES ASINCRONAS PARA CARGA DE DATOS REMOTOS */
	private class LectorPlantillaTask extends
			AsyncTask<SQLiteDatabase, Void, List<Jugador>> {
		private ProgressDialog progressDialog;
		private LectorPlantilla lectorPlantilla;
		private SQLiteDatabase db;

		protected List<Jugador> doInBackground(SQLiteDatabase... params) {
			db = params[0];
			lectorPlantilla = new LectorPlantilla();
			List<Jugador> jugadores = lectorPlantilla.obtenerJugadores();
			return jugadores;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Obteniendo plantilla...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected void onPostExecute(List<Jugador> result) {
			super.onPostExecute(result);

			// Cargamos los datos
			List<Jugador> jugadoresHTTP = result;
			for (Jugador jugador : jugadoresHTTP) {
				actualizarJugador(jugador,db);
			}
			
			if(context instanceof PlantillaActivity){
				((PlantillaActivity) context).refrescarJugadores();
			}
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}
}
