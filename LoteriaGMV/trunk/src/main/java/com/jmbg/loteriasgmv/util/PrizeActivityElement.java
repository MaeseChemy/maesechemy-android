package com.jmbg.loteriasgmv.util;

import com.jmbg.loteriasgmv.R;

import android.content.Context;
import android.content.Intent;

public class PrizeActivityElement extends ActivityElement {

	private final static int ID_DRAWABLE_RES = R.drawable.ic_prize;
	private final static int ID_NAME_ACTIVITY = R.string.category_prize;
	
	private Context context;
	
	public PrizeActivityElement(Context context){
		super(ID_DRAWABLE_RES, context.getResources().getString(ID_NAME_ACTIVITY));
		this.context = context;
	}
	@Override
	public Intent getIntent() {
		return null;
	}

}
