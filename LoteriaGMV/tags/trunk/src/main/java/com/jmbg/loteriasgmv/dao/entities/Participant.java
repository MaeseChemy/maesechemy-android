package com.jmbg.loteriasgmv.dao.entities;

import java.text.SimpleDateFormat;

import com.jmbg.loteriasgmv.ws.LectorDatosWS;

public class Participant extends AbstractEntity implements
		ParticipantBaseColumns, Comparable<Participant> {

	/** Fecha del bote */
	private String participantName;

	/** Cantidad del bote */
	private float participantFund;

	/** **/
	private byte[] participantImageURL;

	public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FIELD_PARTICIPANT_NAME + " TEXT, " 
			+ FIELD_PARTICIPANT_FUND + " TEXT, " 
			+ FIELD_PARTICIPANT_IMG_URL + " BLOB" + "); "
			+ "CREATE INDEX IF NOT EXISTS IDX_" + TABLE_NAME + "_"
			+ FIELD_PARTICIPANT_NAME + " ON TABLE_NAME ("
			+ FIELD_PARTICIPANT_NAME + ");";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			AbstractEntity.CURRENT_DATE);

	public Participant() {
	}

	public Participant(String participantName, float participantFund,
			byte[] participantImageURL) {
		super();
		this.participantName = participantName;
		this.participantFund = participantFund;
		this.participantImageURL = participantImageURL;
	}

	@Override
	public String getTableName() {
		return ParticipantBaseColumns.TABLE_NAME;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("id").append("=").append(this.id).append("]");
		sb.append("[").append("participantName").append("=")
				.append(this.participantName).append("]");
		sb.append("[").append("participantFund").append("=")
				.append(this.participantFund).append("]");
		sb.append("[").append("participantImageURL").append("=")
				.append(this.participantImageURL).append("]");
		return sb.toString();
	}

	public float getParticipantFund() {
		return participantFund;
	}

	public void setParticipantFund(float participantFund) {
		this.participantFund = participantFund;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public byte[] getParticipantImageURL() {
		return this.participantImageURL;
	}

	public void setParticipantImageURL(byte[] participantImageURL) {
		this.participantImageURL = participantImageURL;
	}

	@Override
	public int compareTo(Participant another) {
		int compare = this.getParticipantName().compareTo(
				another.getParticipantName());
		return compare;
	}

}
