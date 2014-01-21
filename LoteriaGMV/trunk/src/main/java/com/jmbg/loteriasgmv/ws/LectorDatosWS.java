package com.jmbg.loteriasgmv.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import com.jmbg.loteriasgmv.dao.entities.Pot;
import com.jmbg.loteriasgmv.util.LogManager;

import android.content.Context;

public class LectorDatosWS {

	private LogManager LOG = LogManager.getLogger(this.getClass());
	
	private Context context;
	
	private List<Pot> potHistory;
	private final static String URL_FILE_POT = "http://54.229.202.59/loteria_gmv/pot.txt";

	public LectorDatosWS(Context context) {
		this.context = context;
		this.potHistory = new ArrayList<Pot>();
	}

	public List<Pot> readPotHistory(){
		HttpURLConnection con = null;
		InputStream is = null;

		try {
			con = (HttpURLConnection) (new URL(URL_FILE_POT)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				Pot pot = generatePot(line);
				if (pot != null)
					this.potHistory.add(pot);
			}

			is.close();
			con.disconnect();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
			}
			try {
				con.disconnect();
			} catch (Throwable t) {
			}
		}
		return this.potHistory;
	}
	
	/**
	 * Genera un objeto Pot a partir de una linea;
	 * DATE|POT_VALUE|POT_VALID
	 * 
	 * @param linea
	 *            Datos del bote
	 * @return Objeto Pot con los datos del mismo o null en caso de que la
	 *         linea no sea valida.
	 */
	private Pot generatePot(String line) {
		LOG.debug("Creating Pot [" + line + "]");

		Pot pot = new Pot();
		String campos[] = line.split("\\|");

		String date = campos[0];
		String value = campos[1];
		String bet = campos[2];
		String won = campos[3];
		String valid = campos[4];

		pot.setPotDate(date);
		pot.setPotValue(Float.parseFloat(value));
		pot.setPotBet(Float.parseFloat(bet));
		pot.setPotWon(Float.parseFloat(won));
		pot.setPotValid(Integer.parseInt(valid));
		
		return pot;

	}
}
