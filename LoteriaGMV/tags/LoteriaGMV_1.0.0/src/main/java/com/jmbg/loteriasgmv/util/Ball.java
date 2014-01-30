package com.jmbg.loteriasgmv.util;

public class Ball {

	public enum TypeBall {
		NUMBER, STAR
	}

	private TypeBall typeBall;
	private int number;

	public Ball(int number, TypeBall typeBall) {
		this.number = number;
		this.typeBall = typeBall;
	}

	public Ball() {
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public TypeBall getTypeBall() {
		return typeBall;
	}

	public void setTypeBall(TypeBall typeBall) {
		this.typeBall = typeBall;
	}


	@Override
	public boolean equals(Object object) {
		if (object instanceof Ball) {
			Ball other = (Ball) object;
			return (this.getNumber() == other.getNumber())
					&& (this.getTypeBall() == other.getTypeBall());
		}else{
			return false;
		}

	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(number).append(
				getTypeBall() == TypeBall.STAR ? "E"
						: getTypeBall() == TypeBall.NUMBER ? "N" : "?");
		return sb.toString();
	}

}
