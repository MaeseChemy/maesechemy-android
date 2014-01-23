package com.jmbg.genapuestas.generadores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jmbg.genapuestas.restRandom.RandomRest;

public class GeneradorPrimitiva {

	public static int MAX_NUM = 49;
	public static int MIN_NUM = 1;
	
	public static int NUMEROS_APUESTA = 6;
	
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

}
