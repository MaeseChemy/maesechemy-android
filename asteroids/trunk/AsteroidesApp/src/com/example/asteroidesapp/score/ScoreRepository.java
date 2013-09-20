package com.example.asteroidesapp.score;

import java.util.Vector;


public interface ScoreRepository {
	public void saveScore(String username, int score);
	public Vector<Score> getOrderScored(int size);
	public Vector<String> getLineOrderScored(int size);
}
