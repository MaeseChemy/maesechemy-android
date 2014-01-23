package com.jmbg.genapuestas;

import java.util.ArrayList;
import java.util.List;

import com.jmbg.genapuestas.generadores.GeneradorEuromillones;
import com.jmbg.genapuestas.generadores.GeneradorPrimitiva;
import com.jmbg.genapuestas.utils.Constantes;

public class Apuesta {
	
	private int tipoApuesta;
	
	//listas Euromillones
	private List<Integer> numerosEuromillones;
	private List<Integer> estrellasEuromillones;

	//lista Primitiva
	private List<Integer> numerosPrimitiva;

	public Apuesta(int tipoApuesta) {
		this.tipoApuesta = tipoApuesta;
		this.numerosEuromillones = new ArrayList<Integer>();
		this.estrellasEuromillones = new ArrayList<Integer>();
		
		this.numerosPrimitiva = new ArrayList<Integer>();
		
		switch(this.tipoApuesta){
		case Constantes.ID_EUROMILLONES:
			this.numerosEuromillones = GeneradorEuromillones.generarNumeros();
			this.estrellasEuromillones = GeneradorEuromillones.generarEstrellas();
		case Constantes.ID_PRIMITIVA:
			this.numerosPrimitiva = GeneradorPrimitiva.generarNumeros();
		}

	}

	public Apuesta(int tipoApuesta, List<Integer> numerosAleatorios) {
		this.tipoApuesta = tipoApuesta;
		this.numerosEuromillones = new ArrayList<Integer>();
		this.estrellasEuromillones = new ArrayList<Integer>();
		
		this.numerosPrimitiva = new ArrayList<Integer>();
		
		switch(this.tipoApuesta){
		case Constantes.ID_EUROMILLONES:
			this.numerosEuromillones = GeneradorEuromillones.generarNumeros(numerosAleatorios);
			this.estrellasEuromillones = GeneradorEuromillones.generarEstrellas(numerosAleatorios);
		case Constantes.ID_PRIMITIVA:
			this.numerosPrimitiva = GeneradorPrimitiva.generarNumeros(numerosAleatorios);
		}
	}
	
	public Apuesta(int tipoApuesta, List<Integer> numeros, List<Integer> estrellas) {
		this.tipoApuesta = tipoApuesta;
		this.numerosEuromillones = new ArrayList<Integer>();
		this.estrellasEuromillones = new ArrayList<Integer>();
		
		this.numerosPrimitiva = new ArrayList<Integer>();
		
		switch(this.tipoApuesta){
		case Constantes.ID_EUROMILLONES:
			this.numerosEuromillones = numeros;
			this.estrellasEuromillones = estrellas;
		case Constantes.ID_PRIMITIVA:
			this.numerosPrimitiva = numeros;
		}
	}
	
	public List<Integer> getEstrellas() {
		switch(this.tipoApuesta){
			case Constantes.ID_EUROMILLONES:
				return this.estrellasEuromillones;
		}
		return new ArrayList<Integer>();
	}

	public List<Integer> getNumeros() {
		switch(this.tipoApuesta){
		case Constantes.ID_EUROMILLONES:
			return this.numerosEuromillones;
		case Constantes.ID_PRIMITIVA:
			return this.numerosPrimitiva;
		}
		return new ArrayList<Integer>();
	}
	
	public int getTipoApuesta() {
		return tipoApuesta;
	}

}
