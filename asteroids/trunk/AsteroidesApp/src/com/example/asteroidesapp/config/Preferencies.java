package com.example.asteroidesapp.config;

import com.example.asteroidesapp.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferencies extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

}
