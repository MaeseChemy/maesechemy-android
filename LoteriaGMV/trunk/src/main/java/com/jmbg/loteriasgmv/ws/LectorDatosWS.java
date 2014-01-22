package com.jmbg.loteriasgmv.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import com.jmbg.loteriasgmv.dao.entities.AbstractEntity;
import com.jmbg.loteriasgmv.dao.entities.Participant;
import com.jmbg.loteriasgmv.dao.entities.Pot;
import com.jmbg.loteriasgmv.util.LogManager;

import android.content.Context;

public class LectorDatosWS {

	private LogManager LOG = LogManager.getLogger(this.getClass());
	
	private Context context;
	
	private List<Pot> potHistory;
	private final static String URL_FILE_POT = "http://54.229.202.59/loteria_gmv/pot.txt";
	
	private List<Participant> participants;
	private final static String URL_FILE_PARTICIPANT = "http://54.229.202.59/loteria_gmv/participants.txt";
	
	public enum TypeFile {
		POT_FILE,
		PARTICIPANT_FILE
	}


	public LectorDatosWS(Context context) {
		this.context = context;
		this.potHistory = new ArrayList<Pot>();
		this.participants = new ArrayList<Participant>();
	}

	
	public List<Pot> readPotHistory(){
		this.potHistory.clear();
		readFile(TypeFile.POT_FILE);
		return this.potHistory;
	}
	
	public List<Participant> readParticipant(){
		this.participants.clear();
		readFile(TypeFile.PARTICIPANT_FILE);
		return this.participants;
	}
	
	private void readFile(TypeFile typeFile){
		HttpURLConnection con = null;
		InputStream is = null;

		try {
			String fileURL = this.getFileURL(typeFile);
			if(fileURL.length() != 0){
				con = (HttpURLConnection) (new URL(fileURL)).openConnection();
				con.setRequestMethod("GET");
				con.setDoInput(true);
				con.setDoOutput(true);
				con.connect();
	
				is = con.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				int numLine = 0;
				while ((line = br.readLine()) != null) {
					//Descartamos la cabecera
					if(numLine != 0)
						this.generateRegister(line, typeFile);
					numLine++;
				}
	
				is.close();
				con.disconnect();
			}
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
	}
	
	private void generateRegister(String line, TypeFile typeFile){
		switch(typeFile){
		case PARTICIPANT_FILE:
			Participant participant = generateParticipant(line);
			if (participant != null)
				this.participants.add(participant);
			break;
		case POT_FILE:
			Pot pot = generatePot(line);
			if (pot != null)
				this.potHistory.add(pot);
			break;
		default:
			break;
		
		}
	}
	
	private String getFileURL(TypeFile typeFile){
		switch(typeFile){
		case PARTICIPANT_FILE:
			return URL_FILE_PARTICIPANT;
		case POT_FILE:
			return URL_FILE_POT;
		default:
			return "";
		}
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
		LOG.debug("Creating Pot of line [" + line + "]");

		Pot pot = new Pot();
		String campos[] = line.split("\\|");

		String date = campos[0].trim();
		String value = campos[1].trim();
		String bet = campos[2].trim();
		String won = campos[3].trim();
		String valid = campos[4].trim();

		pot.setPotDate(date);
		pot.setPotValue(Float.parseFloat(value));
		pot.setPotBet(Float.parseFloat(bet));
		pot.setPotWon(Float.parseFloat(won));
		pot.setPotValid(Integer.parseInt(valid));
		
		LOG.debug("Pot [" + pot.toString() + "]");

		return pot;
	}
	
	/**
	 * Genera un objeto Pot a partir de una linea;
	 * NAME|FUND
	 * 
	 * @param linea
	 *            Datos del bote
	 * @return Objeto Pot con los datos del mismo o null en caso de que la
	 *         linea no sea valida.
	 */
	private Participant generateParticipant(String line) {
		LOG.debug("Creating Participant of line [" + line + "]");

		Participant participant = new Participant();
		String campos[] = line.split("\\|");

		String name = campos[0].trim();
		String fund = campos[1].trim();

		participant.setParticipantName(name);
		participant.setParticipantFund(Float.parseFloat(fund));
		
		LOG.debug("Pot [" + participant.toString() + "]");
		
		return participant;

	}
}
