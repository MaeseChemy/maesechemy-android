package com.jmbg.loteriasgmv;

import java.util.ArrayList;
import java.util.List;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.util.ActivityElement;
import com.jmbg.loteriasgmv.util.BetActivityElement;
import com.jmbg.loteriasgmv.util.ParticipantsActivityElement;
import com.jmbg.loteriasgmv.util.PotActivityElement;
import com.jmbg.loteriasgmv.util.PrizeActivityElement;
import com.jmbg.loteriasgmv.view.adapter.GridAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private List<ActivityElement> mainGridElements;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.mainGridElements = new ArrayList<ActivityElement>();
		
		//Actividades principales
		ActivityElement bets = new BetActivityElement(this);
		ActivityElement awards = new PrizeActivityElement(this);
		ActivityElement pot = new PotActivityElement(this);
		ActivityElement participants = new ParticipantsActivityElement(this);

		this.mainGridElements.add(bets);
		this.mainGridElements.add(awards);
		this.mainGridElements.add(pot);
		this.mainGridElements.add(participants);
		
		GridAdapter adapeter = new GridAdapter(this, this.mainGridElements);
		GridView gridview = (GridView) findViewById(R.id.gridButtons);
		
		gridview.setAdapter(adapeter);
		
		//
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				ActivityElement activity = (ActivityElement)parent.getAdapter().getItem(position);
				
				if(activity != null){
					Intent launchIntent = activity.getIntent();
					if(launchIntent != null)
						startActivity(launchIntent);
					else
						Toast.makeText(MainActivity.this, "Lanzando categoria: " + activity.getActivityName(), Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
