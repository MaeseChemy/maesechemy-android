package com.jmbg.apuestasgmv.utils;

import com.jmbg.apuestasgmv.IntentResults;
import com.jmbg.apuestasgmv.R;
import com.jmbg.apuestasgmv.views.PriceActivity;

import android.content.Context;
import android.content.Intent;

public class PriceActivityElement extends ActivityElement {

	private final static int ID_DRAWABLE_RES = R.drawable.ic_prize;
	private final static int ID_NAME_ACTIVITY = R.string.category_prices_grid;

	private Context context;

	public PriceActivityElement(Context context) {
		super(ID_DRAWABLE_RES, context.getResources().getString(
				ID_NAME_ACTIVITY));
		this.context = context;
	}

	@Override
	public Intent getIntent() {
		Intent intent = new Intent(context, PriceActivity.class);
		return intent;
	}

	@Override
	public int getActivityCode() {
		return IntentResults.INIT_PRICE;
	}

}
