package com.jmbg.loteriasgmv.util;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.dao.entities.Bet;

import android.content.Context;
import android.content.Intent;

public class BetActivityElement extends ActivityElement {

	private final static int ID_DRAWABLE_RES = R.drawable.ic_bets;
	private final static int ID_NAME_ACTIVITY = R.string.category_bets;
	
	private Context context;
	
	public BetActivityElement(Context context){
		super(ID_DRAWABLE_RES, context.getResources().getString(ID_NAME_ACTIVITY));
		this.context = context;
	}
	@Override
	public Intent getIntent() {
		Intent intent = new Intent(context, Bet.class);
		return intent;
	}

}
