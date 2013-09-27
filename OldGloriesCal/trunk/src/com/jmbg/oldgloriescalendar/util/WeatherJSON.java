package com.jmbg.oldgloriescalendar.util;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherJSON {
	public final static String WEATHER_DATE = "dt";
	public final static String WEATHER_MAIN = "main";
	public final static String WEATHER_WIND = "wind";
	public final static String WEATHER_CONDITIONS = "weather";

	// TODO: Precipiations are not included in this version

	public static String weatherFromWeatherJSON(JSONObject json)
			throws JSONException {
		//Weather weather = new Weather();

//		weather.setCity(CityJSON.cityFromWeatherJSON(json));
//		weather.setDate(new Date(json.getInt(WEATHER_DATE)));
//		weather.setMain(MainJSON.mainFromWeatherJSON(json
//				.getJSONObject(WEATHER_MAIN)));
//		weather.setWind(WindJSON.windFromWeatherJSON(json
//				.getJSONObject(WEATHER_WIND)));
//
//		ArrayList<WeatherCondition> wcs = new ArrayList<WeatherCondition>();
//		JSONArray array = json.getJSONArray(WEATHER_CONDITIONS);
//		for (int i = 0; i < array.length(); i++) {
//			JSONObject jsonWC = array.getJSONObject(i);
//			wcs.add(WeatherConditionJSON
//					.weatherConditionFromWeatherJSON(jsonWC));
//		}
//		weather.setConditions(wcs);

//		return weather;
		return "";
	}

}