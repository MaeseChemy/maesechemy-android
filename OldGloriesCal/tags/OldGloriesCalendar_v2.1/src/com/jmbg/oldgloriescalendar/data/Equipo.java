package com.jmbg.oldgloriescalendar.data;

import java.io.Serializable;

public class Equipo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String camiseta;

	public Equipo() {
		super();
	}

	public Equipo(String nombre, String camiseta) {
		super();
		this.nombre = nombre;
		this.camiseta = camiseta;

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

	@Override
	public String toString() {
		return "Equipo [nombre=" + nombre + ", camiseta=" + camiseta;
	}

}
