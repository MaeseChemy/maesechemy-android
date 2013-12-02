package com.jmbg.oldgloriescalendar.dao.entities;

public class Clasificacion {
	private int posicion;
	private String nombre;
	private int partidoJugados;
	private int partidosGanados;
	private int partidosPerdidos;
	private int partidosEmpatados;
	private int golesFavor;
	private int golesContra;
	private int puntos;

	public Clasificacion() {
		super();
	}

	public Clasificacion(int posicion, String nombre, int partidoJugados,
			int partidosGanados, int partidosPerdidos, int partidosEmpatados,
			int golesFavor, int golesContra, int puntos) {
		super();
		this.posicion = posicion;
		this.nombre = nombre;
		this.partidoJugados = partidoJugados;
		this.partidosGanados = partidosGanados;
		this.partidosPerdidos = partidosPerdidos;
		this.partidosEmpatados = partidosEmpatados;
		this.golesFavor = golesFavor;
		this.golesContra = golesContra;
		this.puntos = puntos;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPartidoJugados() {
		return partidoJugados;
	}

	public void setPartidoJugados(int partidoJugados) {
		this.partidoJugados = partidoJugados;
	}

	public int getPartidosGanados() {
		return partidosGanados;
	}

	public void setPartidosGanados(int partidosGanados) {
		this.partidosGanados = partidosGanados;
	}

	public int getPartidosPerdidos() {
		return partidosPerdidos;
	}

	public void setPartidosPerdidos(int partidosPerdidos) {
		this.partidosPerdidos = partidosPerdidos;
	}

	public int getPartidosEmpatados() {
		return partidosEmpatados;
	}

	public void setPartidosEmpatados(int partidosEmpatados) {
		this.partidosEmpatados = partidosEmpatados;
	}

	public int getGolesFavor() {
		return golesFavor;
	}

	public void setGolesFavor(int golesFavor) {
		this.golesFavor = golesFavor;
	}

	public int getGolesContra() {
		return golesContra;
	}

	public void setGolesContra(int golesContra) {
		this.golesContra = golesContra;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	@Override
	public String toString() {
		return "Clasificacion [posicion=" + posicion + ", nombre=" + nombre
				+ ", partidoJugados=" + partidoJugados + ", partidosGanados="
				+ partidosGanados + ", partidosPerdidos=" + partidosPerdidos
				+ ", partidosEmpatados=" + partidosEmpatados + ", golesFavor="
				+ golesFavor + ", golesContra=" + golesContra + ", puntos="
				+ puntos + "]";
	}

	public String getDatosClasificacion() {
		String datos = "";
		datos += "Posicion: "+getPosicion()+"\n";
		datos += "Puntos: "+getPuntos()+"\n";
		datos += "Partidos: J:"+getPartidoJugados()+ " G:"+getPartidosGanados() + " P:"+getPartidosPerdidos() + " E:"+getPartidosEmpatados()+"\n";
		datos += "Goles: F:"+getGolesFavor()+ " F:"+getGolesContra()+"\n";
		return datos;
	}
	
	

}
