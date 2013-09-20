package com.example.asteroidesapp;

import com.example.asteroidesapp.activities.AboutActivity;
import com.example.asteroidesapp.activities.ExitActivity;
import com.example.asteroidesapp.activities.ScoreActivity;
import com.example.asteroidesapp.config.Preferencies;
import com.example.asteroidesapp.score.ScoreRepository;
import com.example.asteroidesapp.score.ScoreRepositoryArray;
import com.example.asteroidesapp.utils.Constants;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Asteroids extends Activity {

	// Puntuaciones
	public static ScoreRepository scoresWharehouse = new ScoreRepositoryArray();

	// Configuracion de la APP
	private boolean music;
	private String fragments;
	private String graphicsType;
	private boolean multiplayer;
	private String maxPlayers;
	private String connectionType;

	// Botones del formulario
	private Button about;
	private Button config;
	private Button play;
	private Button scores;
	private Button exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(Constants.TAG, "[onCreateOptionsMenu]: Creando APP...");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Seteo de variables
		this.about = (Button) findViewById(R.id.btn_about);
		this.about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchAbout(null);
			}
		});

		this.config = (Button) findViewById(R.id.btn_config);
		this.config.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchPreferencies(v);
			}
		});

		this.exit = (Button) findViewById(R.id.btn_exit);
		this.exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initFinish();
			}
		});

		this.play = (Button) findViewById(R.id.btn_play);
		this.play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchGame(v);
			}
		});

		this.scores = (Button) findViewById(R.id.btn_scores);
		this.scores.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchScores(v);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.d(Constants.TAG,
				"[onCreateOptionsMenu]: Creando menu de opciones...");

		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(Constants.TAG,
				"[onKeyDown]: Capturado boton back, preguntamos si quiere salir...");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			initFinish();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			launchAbout(null);
			return true;
		case R.id.config:
			launchPreferencies(null);
			return true;
		case R.id.exit:
			initFinish();
			return true;
		default:
		}
		return false;
	}

	public void launchAbout(View view) {
		Intent iAbout = new Intent(this, AboutActivity.class);
		startActivity(iAbout);
	}

	public void launchPreferencies(View view) {
		Intent iPref = new Intent(this, Preferencies.class);
		startActivity(iPref);
	}

	public void launchGame(View view) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		music = pref.getBoolean("music", false);
		fragments = pref.getString("fragment", "3");
		String typeGraficsValue = pref.getString("grahics", "1");
		graphicsType = typeGraficsValue.equals("0") ? "Vectorial"
				: typeGraficsValue.equals("1") ? "bitmap" : "3D";

		multiplayer = pref.getBoolean("multiplayer", false);
		maxPlayers = pref.getString("fragment", "3");
		String connectionTypeValue = pref.getString("grahics", "1");
		connectionType = connectionTypeValue.equals("0") ? "Bluetooth"
				: connectionTypeValue.equals("1") ? "Wi-Fi" : "Internet";

		Log.d(Constants.TAG,
				"[launchGame]: Ejecutando juego con las siguientes preferencias:");
		Log.d(Constants.TAG, "[launchGame]: Musica      : " + music);
		Log.d(Constants.TAG, "[launchGame]: Fragmentos  : " + fragments);
		Log.d(Constants.TAG, "[launchGame]: Graficos    : " + graphicsType);
		Log.d(Constants.TAG, "[launchGame]: Multiplayer : " + multiplayer);
		Log.d(Constants.TAG, "[launchGame]: Max. Players: " + maxPlayers);
		Log.d(Constants.TAG, "[launchGame]: Tipo Connec.: " + connectionType);
	}

	private void initFinish() {
		Log.d(Constants.TAG,
				"[initFinish]: Preguntando al usuario si desea salir...");

		Intent iExit = new Intent(this, ExitActivity.class);
		startActivityForResult(iExit, Constants.EXIT_APP);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.EXIT_APP) {

		}
		if (resultCode == RESULT_OK) {
			Log.d(Constants.TAG,
					"[onActivityResult]: El usuario ha decidido salir. Cerramos!!");
			finish();
		} else {
			Log.d(Constants.TAG,
					"[onActivityResult]: El usuario ha decidido quedarse. Viva!!");
		}
	}

	private void launchScores(View view) {
		Log.d(Constants.TAG, "[launchScores]: Lanzando puntuaciones...");

		Intent iScores = new Intent(this, ScoreActivity.class);
		startActivity(iScores);
	}

}
