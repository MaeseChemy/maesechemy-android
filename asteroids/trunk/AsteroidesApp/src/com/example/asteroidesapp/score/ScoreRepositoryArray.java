package com.example.asteroidesapp.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;


public class ScoreRepositoryArray implements ScoreRepository {

	private List<Score> scores;

	public ScoreRepositoryArray() {
		this.scores = new ArrayList<Score>();
		/*Putuaciones de ejemplo*/
		this.saveScore("Antonio Lopez",152460);
		this.saveScore("Pedro Martinez",111000);
		this.saveScore("Fray Lorenzo Antunez",125600);
	}

	@Override
	public void saveScore(String name, int score) {
		this.scores.add(new Score(name, score));
		Collections.sort(this.scores, Collections.reverseOrder());
	}

	@Override
	public Vector<Score> getOrderScored(int size) {
		Vector<Score> vector = new Vector<Score>();
		for(int i = 0; i < size && i < this.scores.size(); i++){
			vector.add(this.scores.get(i));
		}
		return vector;
	}
	
	@Override
	public Vector<String> getLineOrderScored(int size){
		Vector<String> vector = new Vector<String>();
		for(int i = 0; i < size && i < this.scores.size(); i++){
			vector.add(this.scores.get(i).toString());
		}
		return vector;
	}

}
