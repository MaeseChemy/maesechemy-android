package com.jmbg.apuestasgmv;

import com.jmbg.apuestasgmv.utils.LogManager;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * Útil para mantener estado global y mantener información útil para varias
 * Activity
 */
public class ApuestasAppApplication extends Application {

	private static ApuestasAppApplication instance;

	public ApuestasAppApplication() {
		instance = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		LogManager.setTag(getAppName());
		if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
			LogManager.setLevel(Log.DEBUG);
		}
	}

	public static ApuestasAppApplication getInstance() {
		return instance;
	}

	public String getAppName() {
		return getInstance().getString(R.string.app_name);
	}

}
