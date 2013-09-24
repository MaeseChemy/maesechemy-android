package com.jmbg.oldgloriescalendar.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jmbg.oldgloriescalendar.data.Partido;

import android.content.Context;
import android.content.res.AssetManager;

public class LectorLiga {

	private Context context;

	public LectorLiga(Context context) {
		this.context = context;
	}

	public List<Partido> leerPartidos() {
		AssetManager am = this.context.getAssets();
		InputStream is;

		List<Partido> partidos = new ArrayList<Partido>();
		try {
			is = am.open("listadoliga.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String linea = "";

			while ((linea = br.readLine()) != null) {
				// Jornada 1|28/09/2013|18:30|OLD GLORIES RELOADED|METALICOS|La
				// Cantera FT2 M1
				System.out.println(linea);
				String campos[] = linea.split("\\|");
				Partido partido = new Partido();

				partido.setJornada(campos[0]);
				String fecha = campos[1] + " " + campos[2];
				Date date = new Date();
				try {
					date = new SimpleDateFormat("dd/MM/yyyy HH:mm")
							.parse(fecha);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				partido.setFecha(date);
				String local = campos[3];
				String visitante = campos[4];

				if (local.equals(Constantes.equipo)) {
					partido.setOponente(visitante);
					partido.setLocal(true);
				} else {
					partido.setOponente(local);
					partido.setLocal(false);
				}

				partido.setLugar(campos[5]);

				System.out.println(partido.toString());

				partidos.add(partido);
			}
			is.close();
			br.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return partidos;
	}
}
