package com.example.asteroidesapp.activities;

import com.example.asteroidesapp.Asteroids;
import com.example.asteroidesapp.R;
import com.example.asteroidesapp.score.Score;
import com.example.asteroidesapp.score.ScoreAdapter;
import com.example.asteroidesapp.utils.Constants;

import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class ScoreActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		/*
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_score_element, R.id.tittle,
				Asteroids.scoresWharehouse.getLineOrderScored(10));
		setListAdapter(adapter);
		*/
		
		ScoreAdapter adapterScore = new ScoreAdapter(this, Asteroids.scoresWharehouse.getOrderScored(10));
		setListAdapter(adapterScore);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id){
		super.onListItemClick(listView, view, position, id);
		
		Score s = (Score)getListAdapter().getItem(position);
		
		Log.d(Constants.TAG, "[onListItemClick]: Usuario    : " + s.getUsername());
		Log.d(Constants.TAG, "[onListItemClick]: Puntuacion : " + Integer.toString(s.getScore()));
	}
}
