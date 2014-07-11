package com.jmbg.apuestasgmv.utils;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceUtils {
	public static String getDeviceImei(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (imei == null || imei.length() == 0) {
			imei = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
		}
		return imei;
	}
}
