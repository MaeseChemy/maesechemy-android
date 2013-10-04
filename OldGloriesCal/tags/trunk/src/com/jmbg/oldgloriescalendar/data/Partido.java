package com.jmbg.oldgloriescalendar.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Partido implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String jornada;
	private String oponente;
	private String lugar;
	private boolean local;
	private Date fecha;

	public Partido() {
		super();
	}

	public Partido(String jornada, Date fecha, String lugar, String oponente,
			boolean local) {
		super();
		this.jornada = jornada;
		this.fecha = fecha;
		this.lugar = lugar;
		this.oponente = oponente;
		this.local = local;
	}

	public String getOponente() {
		return oponente;
	}

	public void setOponente(String oponente) {
		this.oponente = oponente;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Date getFecha() {
		return fecha;
	}
	
	public Long getFechaLong(){
		Calendar myCal = new GregorianCalendar();
		myCal.setTime(this.fecha);
		
		return myCal.getTimeInMillis();
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void setFechaLong(Long fecha){
		Calendar myCal = new GregorianCalendar();
		myCal.setTimeInMillis(fecha);
		
		this.fecha = myCal.getTime();
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public String getJornada() {
		return jornada;
	}

	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	@Override
	public String toString() {
		return "Partido [jornada="+jornada+", oponente=" + oponente + ", lugar=" + lugar
				+ ", fecha=" + fecha + ", local=" + local;
	}



}
