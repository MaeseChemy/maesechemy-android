package com.jmbg.oldgloriescalendar.dao.entities;

public class Convocatoria {

	private String jornada;
	private String jugador;
	private String comentario;
	private boolean delegado;

	public Convocatoria() {
		super();
	}

	public Convocatoria(String jornada, String jugador, String comentario,
			boolean delegado) {
		super();
		this.jornada = jornada;
		this.jugador = jugador;
		this.comentario = comentario;
		this.delegado = delegado;
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

	public boolean getDelegado() {
		return delegado;
	}

	public void setDelegado(Boolean delegado) {
		this.delegado = delegado;
	}

}
