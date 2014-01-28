package com.jmbg.loteriasgmv.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.jmbg.loteriasgmv.dao.entities.Bet;
import com.jmbg.loteriasgmv.dao.entities.Participant;
import com.jmbg.loteriasgmv.dao.entities.Pot;
import com.jmbg.loteriasgmv.dao.entities.Price;
import com.jmbg.loteriasgmv.util.LogManager;

import android.content.Context;

public class LectorDatosWS {

	public final static String URL_BASE = "http://54.229.202.59/loteria_gmv";

	private LogManager LOG = LogManager.getLogger(this.getClass());

	@SuppressWarnings(value = { "unused" })
	private Context context;

	private List<Pot> potHistory;
	private final static String URL_FILE_POT = URL_BASE + "/pot.txt";

	private List<Participant> participants;
	private final static String URL_FILE_PARTICIPANT = URL_BASE
			+ "/participants.txt";

	private List<Bet> bets;
	private final static String URL_FILE_BET = URL_BASE + "/bets.txt";
	
	private List<Price> prices;
	private final static String URL_FILE_PRICE = URL_BASE + "/loteria_res.txt";


	public enum TypeFile {
		POT_FILE, PARTICIPANT_FILE, BET_FILE, PRICE_FILE
	}

	public LectorDatosWS(Context context) {
		this.context = context;
		this.potHistory = new ArrayList<Pot>();
		this.participants = new ArrayList<Participant>();
		this.bets = new ArrayList<Bet>();
		this.prices = new ArrayList<Price>();
	}

	public List<Pot> readPotHistory() {
		this.potHistory.clear();
		readFile(TypeFile.POT_FILE);
		return this.potHistory;
	}

	public List<Participant> readParticipant() {
		this.participants.clear();
		readFile(TypeFile.PARTICIPANT_FILE);
		return this.participants;
	}

	public List<Bet> readBet() {
		this.bets.clear();
		readFile(TypeFile.BET_FILE);
		return this.bets;
	}
	
	public List<Price> readPrice(){
		this.prices.clear();
		readFile(TypeFile.PRICE_FILE);
		return this.prices;
	}

	private void readFile(TypeFile typeFile) {
		HttpURLConnection con = null;
		InputStream is = null;

		try {
			String fileURL = this.getFileURL(typeFile);
			if (fileURL.length() != 0) {
				con = (HttpURLConnection) (new URL(fileURL)).openConnection();
				con.setRequestMethod("GET");
				con.setDoInput(true);
				con.setDoOutput(true);
				con.connect();

				is = con.getInputStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line = null;
				int numLine = 0;
				while ((line = br.readLine()) != null) {
					// Descartamos la cabecera
					if (numLine != 0)
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

	private void generateRegister(String line, TypeFile typeFile) {
		if(line != null && line.length()!=0){
			switch (typeFile) {
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
			case BET_FILE:
				Bet bet = generateBet(line);
				if (bet != null)
					this.bets.add(bet);
				break;
			case PRICE_FILE:
				Price price = generatePrice(line);
				if (price != null)
					this.prices.add(price);
			default:
				break;
			}
		}
	}

	private String getFileURL(TypeFile typeFile) {
		switch (typeFile) {
		case PARTICIPANT_FILE:
			return URL_FILE_PARTICIPANT;
		case POT_FILE:
			return URL_FILE_POT;
		case BET_FILE:
			return URL_FILE_BET;
		case PRICE_FILE:
			return URL_FILE_PRICE;
		default:
			return "";
		}
	}

	/**
	 * Genera un objeto Pot a partir de una linea; DATE|POT_VALUE|POT_VALID
	 * 
	 * @param linea
	 *            Datos del bote
	 * @return Objeto Pot con los datos del mismo o null en caso de que la linea
	 *         no sea valida.
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
	 * Genera un objeto Pot a partir de una linea; NAME|FUND
	 * 
	 * @param linea
	 *            Datos del bote
	 * @return Objeto Pot con los datos del mismo o null en caso de que la linea
	 *         no sea valida.
	 */
	private Participant generateParticipant(String line) {
		LOG.debug("Creating Participant of line [" + line + "]");

		Participant participant = new Participant();
		String campos[] = line.split("\\|");

		String name = campos[0].trim();
		String imagePath = campos[1].trim();
		String fund = campos[2].trim();

		participant.setParticipantName(name);
		participant.setParticipantFund(Float.parseFloat(fund));

		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpGet mHttpGet = new HttpGet(URL_BASE + "/" + imagePath);
		HttpResponse mHttpResponse;
		try {
			mHttpResponse = mHttpClient.execute(mHttpGet);

			if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = mHttpResponse.getEntity();
				if (entity != null) {
					// insert to database
					participant.setParticipantImageURL(EntityUtils
							.toByteArray(entity));
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.debug("Pot [" + participant.toString() + "]");

		return participant;

	}
	
	/**
	 * Genera un objeto Pot a partir de una linea; NAME|FUND
	 * 
	 * @param linea
	 *            Datos del bote
	 * @return Objeto Pot con los datos del mismo o null en caso de que la linea
	 *         no sea valida.
	 */
	private Bet generateBet(String line) {
		LOG.debug("Creating Bet of line [" + line + "]");

		Bet bet = new Bet();
		String campos[] = line.split("\\|");

		String date = campos[0].trim();
		String type = campos[1].trim();
		String numbers = campos[2].trim();
		String imagePath = campos[3].trim();
		String active = campos[4].trim();

		bet.setBetDate(date);
		bet.setBetType(type);
		bet.setBetNumbers(numbers);
		bet.setBetActive(Integer.parseInt(active));

		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpGet mHttpGet = new HttpGet(URL_BASE + "/" + imagePath);
		HttpResponse mHttpResponse;
		try {
			mHttpResponse = mHttpClient.execute(mHttpGet);

			if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = mHttpResponse.getEntity();
				if (entity != null) {
					// insert to database
					bet.setBetImage(EntityUtils
							.toByteArray(entity));
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.debug("Bet [" + bet.toString() + "]");

		return bet;
	}
	
	/**
	 * Genera un objeto Pot a partir de una linea; TYPE|FECHA_SORTEO|NUMEROS|FECHA_SIG_SORTEO|BOTE_SIG_SORTEO
	 * 
	 * @param linea
	 *            Datos del bote
	 * @return Objeto Pot con los datos del mismo o null en caso de que la linea
	 *         no sea valida.
	 */
	private Price generatePrice(String line) {
		LOG.debug("Creating Price of line [" + line + "]");

		Price price = new Price();
		String campos[] = line.split("\\|");

		String type = campos[0].trim();
		String date = campos[1].trim();
		String numbers = campos[2].trim();
		String next_date = campos[3].trim();
		String next_pot = campos[4].trim();

		price.setPriceType(type);
		price.setPriceDate(convertPriceDate(date));
		price.setPriceNumbers(numbers);
		price.setPriceNextDate(convertPriceDate(next_date));
		price.setPriceNextPot(Integer.parseInt(next_pot));

		LOG.debug("Price [" + price.toString() + "]");

		return price;
	}
	
	private String convertPriceDate(String date){
		//jueves 23 de enero de 2014
		String campos[] = date.split(" ");
		//20/01/2014
		String convertDate = campos[1] + "/" + getPriceMonth(campos[3]) + "/" + campos[5];
		
		return convertDate;
	}
	
	private String getPriceMonth(String nameMonth){
		if(nameMonth.equals("enero")){
			return "01";
		}else if(nameMonth.equals("febrero")){
			return "02";
		}else if(nameMonth.equals("marzo")){
			return "03";
		}else if(nameMonth.equals("abril")){
			return "04";
		}else if(nameMonth.equals("mayo")){
			return "05";
		}else if(nameMonth.equals("junio")){
			return "06";
		}else if(nameMonth.equals("julio")){
			return "07";
		}else if(nameMonth.equals("agosto")){
			return "08";
		}else if(nameMonth.equals("septiembre")){
			return "09";
		}else if(nameMonth.equals("noviembre")){
			return "10";
		}else if(nameMonth.equals("diciembre")){
			return "11";
		}else{
			return "12";
		}
	}
}
