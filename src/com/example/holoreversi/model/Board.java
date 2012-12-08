package com.example.holoreversi.model;

public interface Board {
	final public static int BLACK = 2;
	final public static int WHITE = 1;
	final public static int EMPTY = 0;
	
	interface Callback {
		void onBoardUpdate(Board board, Cell cell, int newState);
	}

	int getScoreWhite();
	int getScoreBlack();
	void moveWhite(int x,int y);
	void moveBlack(int x, int y);
	Cell[] getAllowedMovesWhite();
	Cell[] getAllowedMovesBlack();
	Cell[][] getAll();
	void addCallbackListener(Callback callback);
	int getSize();
	void move(Cell cell);
}
