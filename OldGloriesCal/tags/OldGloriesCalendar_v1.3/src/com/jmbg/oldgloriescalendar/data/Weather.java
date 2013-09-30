package com.jmbg.oldgloriescalendar.data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

public class Weather {

	private Date fechaTiempo;
	private int id;
	private String descripcion;
	private String icon;
	private String main;
	private JSONObject temperaturas;
	private double tempMax;
	private double tempMin;

	private SimpleDateFormat dfday;

	public Weather() {
		super();
		dfday = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
	}

	public Date getFechaTiempo() {
		return fechaTiempo;
	}

	public void setFechaTiempo(Date fechaTiempo) {
		String fechaDia = dfday.format(fechaTiempo);
		
		try {
			this.fechaTiempo = dfday.parse(fechaDia);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getFechaTiempoFormat() {
		return dfday.format(fechaTiempo);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public JSONObject getTemperaturas() {
		return temperaturas;
	}

	public void setTemperaturas(JSONObject temperaturas) {
		this.temperaturas = temperaturas;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public String toString(){
		return this.getFechaTiempoFormat()+":\n"
				+"- Descripccion:"+this.getDescripcion()+"\n"
				+"- Temp Maxima :"+this.getTempMax()+"\n"
				+"- Temp Minima :"+this.getTempMin()+"\n"
				+"- Icon        :"+this.getIcon()+"\n"
				+"- Id          :"+this.getId()+"\n";
	}
}
