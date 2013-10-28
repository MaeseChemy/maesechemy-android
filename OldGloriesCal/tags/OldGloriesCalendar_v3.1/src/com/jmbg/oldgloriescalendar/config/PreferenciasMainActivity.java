package com.jmbg.oldgloriescalendar.config;

import com.jmbg.oldgloriescalendar.R;
import com.jmbg.oldgloriescalendar.R.xml;
import com.jmbg.oldgloriescalendar.util.Constantes;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.KeyEvent;

public class PreferenciasMainActivity extends PreferenceActivity{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate (Bundle savedInstanceState){
		Log.i(Constantes.TAG, "["+PreferenciasMainActivity.class.getName()+".onCreate] Creando actividad de preferencias principales...");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias_main);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
}
