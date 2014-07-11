package com.jmbg.loteriasgmv.view;

import com.jmbg.loteriasgmv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class PriceDialog extends Activity {

	private String priceDate;
	private String priceType;
	private String priceNumbers;
	
	private String betData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_price);
		
		 // get intent data
        Intent i = getIntent();
        // Selected image id
        this.priceDate = i.getExtras().getString("priceDate");
        this.priceType = i.getExtras().getString("priceType");
        this.priceNumbers = i.getExtras().getString("priceNumbers");
        this.betData = i.getExtras().getString("betData");
		
		// set the custom dialog components - text, image and button
		TextView type = (TextView) findViewById(R.id.dialog_text_bet_type);
		type.setText(priceType);
		TextView date = (TextView) findViewById(R.id.dialog_text_bet_date);
		date.setText(priceDate);
		TextView numbers = (TextView) findViewById(R.id.dialog_text_bet_numbers);
		numbers.setText(priceNumbers);
		TextView bet = (TextView) findViewById(R.id.dialog_text_bet);
		bet.setText(betData);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.price, menu);
		return true;
	}
}
