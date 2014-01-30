package com.jmbg.loteriasgmv.util;

import com.jmbg.loteriasgmv.R;
import com.jmbg.loteriasgmv.view.ParticipantActivity;

import android.content.Context;
import android.content.Intent;

public class ParticipantsActivityElement extends ActivityElement {

	private final static int ID_DRAWABLE_RES = R.drawable.ic_participants;
	private final static int ID_NAME_ACTIVITY = R.string.category_participants;
	
	private Context context;
	
	public ParticipantsActivityElement(Context context){
		super(ID_DRAWABLE_RES, context.getResources().getString(ID_NAME_ACTIVITY));
		this.context = context;
	}
	@Override
	public Intent getIntent() {
		Intent intent = new Intent(context, ParticipantActivity.class);
		return intent;
	}

}
