package com.example.asteroidesapp.score;

import java.util.Date;

public class Score implements Comparable<Score> {

	private String username;
	private int score;
	private Date date;

	public Score(String username, int score) {
		this.username = username;
		this.score = score;
		this.date = new Date();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int compareTo(Score other) {
		return this.score > other.getScore() ? +1 : this.score < other
				.getScore() ? -1 : 0;
	}

	public String toString() {
		return " [" + this.score + "] - " + this.username;
	}

}
