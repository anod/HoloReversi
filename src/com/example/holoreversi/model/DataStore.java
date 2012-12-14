package com.example.holoreversi.model;

import java.util.ArrayList;


public interface DataStore {
	
	long insertMove(long gid ,Cell cell);
	long insertGame();
	ArrayList<Cell> getMoves(long gid);
	ArrayList<Game> getGames();
	
}
