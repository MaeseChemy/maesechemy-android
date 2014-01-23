package com.jmbg.genapuestas.generadores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jmbg.genapuestas.restRandom.RandomRest;

public class GeneradorEuromillones {
	
	public static int MAX_NUM = 50;
	public static int MIN_NUM = 1;
	
	public static int MAX_EST = 11;
	public static int MIN_EST = 1;
	
	public static int NUMEROS_APUESTA = 5;
	public static int ESTRELLAS_APUESTA = 2;

	public static List<Integer> generarNumeros() {
		List<Integer> numeros = new ArrayList<Integer>();
		for (int i = 0; i < NUMEROS_APUESTA; i++) {
			int number = (int) (Math.random() * MAX_NUM + MIN_NUM);
			if (numeros.contains(number))
				i--;
			else
				numeros.add(number);
		}
		Collections.sort(numeros);
		return numeros;
	}

	public static List<Integer> generarEstrellas() {
		List<Integer> estrellas = new ArrayList<Integer>();
		for (int i = 0; i < ESTRELLAS_APUESTA; i++) {
			int star = (int) (Math.random() * MAX_EST + MIN_EST);
			if (estrellas.contains(star))
				i--;
			else
				estrellas.add(star);
		}
		Collections.sort(estrellas);
		return estrellas;
	}

	public static List<Integer> generarNumerosRest(){
		List<Integer> numeros;
		try {
			numeros = RandomRest.generarNumerosAleatorios(MIN_NUM, MAX_NUM, NUMEROS_APUESTA);
			Collections.sort(numeros);
			return numeros;
		} catch (Exception e) {
			return generarNumeros();
		}
	}
	
	public static List<Integer> generarEstrellasRest(){
		List<Integer> numeros;
		try {
			numeros = RandomRest.generarNumerosAleatorios(MIN_EST, MAX_EST, ESTRELLAS_APUESTA);
			Collections.sort(numeros);
			return numeros;
		} catch (Exception e) {
			return generarEstrellas();
		}
	}
	
	public static List<Integer> generarNumeros(List<Integer> numerosAleatorios) {
		List<Integer> numeros = new ArrayList<Integer>();
		for (int i = 0; i < NUMEROS_APUESTA; i++) {
			int indexNumber = (int) (Math.random() * numerosAleatorios.size());
			int number = (numerosAleatorios.get(indexNumber) % MAX_NUM )+MIN_NUM;
			if (numeros.contains(number))
				i--;
			else
				numeros.add(number);
		}
		Collections.sort(numeros);
		return numeros;
	}
	
	public static List<Integer> generarEstrellas(List<Integer> numerosAleatorios) {
		List<Integer> estrellas = new ArrayList<Integer>();
		
		for (int i = 0; i < ESTRELLAS_APUESTA; i++) {
			int indexStar = (int) (Math.random() * numerosAleatorios.size());
			int star = (numerosAleatorios.get(indexStar) % MAX_EST )+MIN_EST;
			if (estrellas.contains(star))
				i--;
			else
				estrellas.add(star);
		}
		Collections.sort(estrellas);
		return estrellas;
	}

}
