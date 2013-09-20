package com.example.asteroidesapp.activities;

import com.example.asteroidesapp.Asteroids;
import com.example.asteroidesapp.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class ScoreActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);

		/*
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_activated_1,
		 * Asteroides.almacen.listaPuntuaciones(10)); setListAdapter(adapter);
		 */

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_score_element, R.id.tittle,
				Asteroids.scoresWharehouse.getLineOrderScored(10));
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score, menu);
		return true;
	}

}
