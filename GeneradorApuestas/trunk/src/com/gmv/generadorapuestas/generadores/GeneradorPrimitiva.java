package com.gmv.generadorapuestas.generadores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneradorPrimitiva {

	public static List<Integer> generarNumeros() {
		List<Integer> numeros = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			int number = (int) (Math.random() * 49 + 1);
			if (numeros.contains(number))
				i--;
			else
				numeros.add(number);
		}
		Collections.sort(numeros);
		return numeros;
	}

}
