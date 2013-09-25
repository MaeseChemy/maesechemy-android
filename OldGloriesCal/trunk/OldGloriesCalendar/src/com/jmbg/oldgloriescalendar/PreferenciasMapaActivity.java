package com.jmbg.oldgloriescalendar;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

public class PreferenciasMapaActivity extends PreferenceActivity{
	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias_mapa);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
}
