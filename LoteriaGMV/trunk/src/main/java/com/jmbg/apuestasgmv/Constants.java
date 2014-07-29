package com.jmbg.apuestasgmv;

public class Constants {
	public static final String SENDER_ID = "129772070612";
	public static final String PROPERTY_REG_ID = "registration_id";

	public static final int MIN_POT_VALUE = 14;

	public enum TypeAppData {
		HOME, PARTICIPANT, BET, PRICE, POT
	};
	
	public enum NotificationTypes {
		UPD_BETS, UPD_PRICES, UPD_POT, UPD_PARTICIPANTS, UPD_OTHER
	};
}
