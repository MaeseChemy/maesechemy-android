package com.jmbg.loteriasgmv.util;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.view.PotActivity;

import android.content.Context;
import android.content.Intent;

public class PotActivityElement extends ActivityElement {

	private final static int ID_DRAWABLE_RES = R.drawable.ic_pot;
	private final static int ID_NAME_ACTIVITY = R.string.category_pot;
	
	private Context context;
	
	public PotActivityElement(Context context){
		super(ID_DRAWABLE_RES, context.getResources().getString(ID_NAME_ACTIVITY));
		this.context = context;
	}
	@Override
	public Intent getIntent() {
		Intent intent = new Intent(context, PotActivity.class);
		return intent;
	}

}
