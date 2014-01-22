package com.jmbg.loteriasgmv.dao.entities;

import java.text.SimpleDateFormat;

public class Participant extends AbstractEntity implements
ParticipantBaseColumns, Comparable<Participant>{
	
	/** Fecha del bote */
	private String participantName;
	
	/** Cantidad del bote */
	private float participantFund;

	public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FIELD_PARTICIPANT_NAME + " TEXT, " 
			+ FIELD_PARTICIPANT_FUND + " TEXT" + "); "
			+ "CREATE INDEX IF NOT EXISTS IDX_" + TABLE_NAME + "_" + FIELD_PARTICIPANT_NAME + " ON TABLE_NAME (" + FIELD_PARTICIPANT_NAME + ");";

	private static SimpleDateFormat sdf = new SimpleDateFormat(AbstractEntity.CURRENT_TIMESTAMP);
	
	public Participant() {}

	public Participant(String participantName, float participantFund) {
		super();
		this.participantName = participantName;
		this.participantFund =participantFund;
	}

	@Override
	public String getTableName() {
		return ParticipantBaseColumns.TABLE_NAME;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("id").append("=").append(this.id).append("]");
		sb.append("[").append("participantName").append("=").append(this.participantName).append("]");
		sb.append("[").append("participantFund").append("=").append(this.participantFund).append("]");
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

	@Override
	public int compareTo(Participant another) {
		int compare = this.getParticipantName().compareTo(another.getParticipantName());
		return compare;
	}


}
