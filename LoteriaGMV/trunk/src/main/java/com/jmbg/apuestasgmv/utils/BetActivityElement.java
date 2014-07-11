package com.jmbg.apuestasgmv.utils;

import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.views.BetActivity;

import android.content.Context;
import android.content.Intent;

public class BetActivityElement extends ActivityElement {

	private final static int ID_DRAWABLE_RES = R.drawable.ic_bets;
	private final static int ID_NAME_ACTIVITY = R.string.category_bets_grid;
	
	private Context context;
	
	public BetActivityElement(Context context){
		super(ID_DRAWABLE_RES, context.getResources().getString(ID_NAME_ACTIVITY));
		this.context = context;
	}
	@Override
	public Intent getIntent() {
		Intent intent = new Intent(context, BetActivity.class);
		return intent;
	}

}
