package com.jmbg.loteriasgmv.util;

import java.util.ArrayList;
import java.util.List;

public class Ticket {

	public enum TypeTicket {
		EUROMILLONES, PRIMITIVA, OTHER
	}

	private TypeTicket type;
	private List<Ball> numbers;
	private String date;

	public Ticket(TypeTicket type, List<Ball> numbers, String date) {
		super();
		this.type = type;
		this.numbers = numbers;
		this.date = date;
	}

	public Ticket() {
	}

	public TypeTicket getType() {
		return type;
	}

	public void setType(TypeTicket type) {
		this.type = type;
	}

	public List<Ball> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Ball> numbers) {
		this.numbers = numbers;
	}

	public List<Ball> hitNumber(Ticket other) {
		List<Ball> hits = new ArrayList<Ball>();
		for (Ball ball : this.getNumbers()) {
			if (other.getNumbers().contains(ball))
				hits.add(ball);
		}

		return hits;
	}

	public boolean equal(Ticket other) {
		if (this.getType() == other.getType()) {
			for (Ball ball : this.getNumbers()) {
				if (!other.getNumbers().contains(ball))
					return false;
			}

			return true;
		} else {
			return false;
		}
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(Ball ball : this.numbers){
			sb.append(ball.toString()).append(" ");
		}
		return sb.toString();
	}
}
