package com.example.holoreversi.model;

import java.util.ArrayList;

public interface Board {
	final public static int BLACK = 2;
	final public static int WHITE = 1;
	final public static int EMPTY = 0;
	
	interface Callback {
		void onBoardUpdate(Board board);
	}

	void resetBoard();
	boolean hasUndo();
	int currentPlayer();
	int getSize();
	int getScoreWhite();
	int getScoreBlack();
	Cell[][] getAll();
	boolean move(Cell cell);
	ArrayList<Cell> getAllowedMoves();
	void addCallbackListener(Callback callback);
	boolean undoMove();
	int winner();
	boolean isGameEnded();
	Cell Play(); 
	
}
