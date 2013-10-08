package com.jmbg.oldgloriescalendar.partido;

import com.jmbg.oldgloriescalendar.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

public class PreferenciasPartidoActivity extends PreferenceActivity{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias_partidos);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
}
