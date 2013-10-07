package com.jmbg.oldgloriescalendar.data;

import java.io.Serializable;

public class Equipo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String camiseta;
	private Clasificacion clasificacion;

	public Equipo() {
		super();
	}

	public Equipo(String nombre, String camiseta, Clasificacion clasificacion) {
		super();
		this.nombre = nombre;
		this.camiseta = camiseta;
		this.clasificacion = clasificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCamiseta() {
		return camiseta;
	}

	public void setCamiseta(String camiseta) {
		this.camiseta = camiseta;
	}

	public Clasificacion getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(Clasificacion clasificacion) {
		this.clasificacion = clasificacion;
	}
	@Override
	public String toString() {
		return "Equipo [clasificacion="+clasificacion.toString()+ ", nombre=" + nombre + ", camiseta=" + camiseta+"]";
	}

}
