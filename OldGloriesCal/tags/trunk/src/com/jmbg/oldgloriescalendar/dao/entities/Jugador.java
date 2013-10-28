package com.jmbg.oldgloriescalendar.dao.entities;

import java.io.Serializable;

public class Jugador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int dorsal;
	private String nombre;
	private int goles;
	private int tarjetasAmarillas;
	private int tarjetasRojas;

	public Jugador() {
		super();
	}

	public Jugador(int dorsal, String nombre, int goles, int tarjetasAmarillas,
			int tarjetasRojas) {
		super();
		this.dorsal = dorsal;
		this.nombre = nombre;
		this.goles = goles;
		this.tarjetasAmarillas = tarjetasAmarillas;
		this.tarjetasRojas = tarjetasRojas;
	}

	public void setDorsal(int dorsal) {
		this.dorsal = dorsal;
	}

	public int getDorsal() {
		return dorsal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getGoles() {
		return goles;
	}

	public void setGoles(int goles) {
		this.goles = goles;
	}

	public int getTarjetasAmarillas() {
		return tarjetasAmarillas;
	}

	public void setTarjetasAmarillas(int tarjetasAmarillas) {
		this.tarjetasAmarillas = tarjetasAmarillas;
	}

	public int getTarjetasRojas() {
		return tarjetasRojas;
	}

	public void setTarjetasRojas(int tarjetasRojas) {
		this.tarjetasRojas = tarjetasRojas;
	}

	@Override
	public String toString() {
		return "Jugador [dorsal=" + dorsal + ", nombre=" + nombre + ", goles="
				+ goles + ", tarjetasAmarillas=" + tarjetasAmarillas
				+ ", tarjetasRojas=" + tarjetasRojas + "]";
	}

}
