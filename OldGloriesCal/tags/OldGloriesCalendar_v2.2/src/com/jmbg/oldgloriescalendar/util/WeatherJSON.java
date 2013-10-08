package com.jmbg.oldgloriescalendar.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jmbg.oldgloriescalendar.data.Weather;

public class WeatherJSON {
	public final static String WEATHER_LIST = "list";
	public final static String WEATHER_DATE = "dt";
	public final static String WEATHER_CONDITIONS = "weather";
	public final static String WEATHER_ID = "id";
	public final static String WEATHER_MAIN = "main";
	public final static String WEATHER_DESC = "description";
	public final static String WEATHER_ICON = "icon";
	public final static String WEATHER_TEMP = "temp";
	public final static String WEATHER_TEMP_MIN = "min";
	public final static String WEATHER_TEMP_MAX = "max";

	public Map<Date, Weather> weatherFromWeatherJSON(String json)
			throws JSONException {
		Map<Date, Weather> listaTiempo = new HashMap<Date, Weather>();
		JSONObject jsonObjectMain = new JSONObject(json);

		JSONArray arrayList = jsonObjectMain.getJSONArray(WEATHER_LIST);

		for (int i = 0; i < arrayList.length(); i++) {
			Weather weather = new Weather();
			JSONObject jsonObjectList = arrayList.getJSONObject(i);
			weather.setFechaTiempo(new Date(jsonObjectList
					.getLong(WEATHER_DATE) * 1000));

			JSONArray arrayListWeather = jsonObjectList
					.getJSONArray(WEATHER_CONDITIONS);
			JSONObject JSONObjectWeather = arrayListWeather.getJSONObject(0);

			weather.setId(JSONObjectWeather.getInt(WEATHER_ID));
			weather.setMain(JSONObjectWeather.getString(WEATHER_MAIN));

			weather.setDescripcion(JSONObjectWeather.getString(WEATHER_DESC));
			weather.setIcon(JSONObjectWeather.getString(WEATHER_ICON));
			weather.setTemperaturas(jsonObjectList.getJSONObject(WEATHER_TEMP));
			weather.setTempMax(weather.getTemperaturas().getDouble(
					WEATHER_TEMP_MAX));
			weather.setTempMin(weather.getTemperaturas().getDouble(
					WEATHER_TEMP_MIN));

			listaTiempo.put(weather.getFechaTiempo(), weather);

		}
		return listaTiempo;
	}

}