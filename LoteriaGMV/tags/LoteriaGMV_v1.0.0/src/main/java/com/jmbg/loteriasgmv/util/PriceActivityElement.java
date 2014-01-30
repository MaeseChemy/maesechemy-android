package com.jmbg.loteriasgmv.util;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.view.PotActivity;
import com.jmbg.loteriasgmv.view.PriceActivity;

import android.content.Context;
import android.content.Intent;

public class PriceActivityElement extends ActivityElement {

	private final static int ID_DRAWABLE_RES = R.drawable.ic_prize;
	private final static int ID_NAME_ACTIVITY = R.string.category_prize;
	
	private Context context;
	
	public PriceActivityElement(Context context){
		super(ID_DRAWABLE_RES, context.getResources().getString(ID_NAME_ACTIVITY));
		this.context = context;
	}
	@Override
	public Intent getIntent() {
		Intent intent = new Intent(context, PriceActivity.class);
		return intent;
	}

}
