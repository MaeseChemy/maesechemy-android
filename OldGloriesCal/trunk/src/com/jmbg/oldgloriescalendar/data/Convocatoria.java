package com.jmbg.oldgloriescalendar.data;

public class Convocatoria {

	private String jornada;
	private String jugador;
	private String comentario;

	public Convocatoria() {
		super();
	}

	public Convocatoria(String jornada, String jugador, String comentario) {
		super();
		this.jornada = jornada;
		this.jugador = jugador;
		this.comentario = comentario;
	}

	public String getJornada() {
		return jornada;
	}

	public void setJornada(String jornada) {
		this.jornada = jornada;
	}

	public String getJugador() {
		return jugador;
	}

	public void setJugador(String jugador) {
		this.jugador = jugador;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

}
