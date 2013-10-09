package com.gmv.generadorapuestas.generadores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneradorEuromillones {


	public static List<Integer> generarNumeros() {
		List<Integer> numeros = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			int number = (int) (Math.random() * 50 + 1);
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
		for (int i = 0; i < 2; i++) {
			int star = (int) (Math.random() * 11 + 1);
			if (estrellas.contains(star))
				i--;
			else
				estrellas.add(star);
		}
		Collections.sort(estrellas);
		return estrellas;
	}

}
