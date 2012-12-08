package com.example.holoreversi.model;

public interface Board {

	interface Callback {
		void onBoardUpdate(Board board, Cell cell, int newState);
	}

	int getScoreWhite();
	int getScoreBlack();
	void moveWhite(int x,int y);
	void moveBlack(int x, int y);
	Cell[] getAllowedMovesWhite();
	Cell[] getAllowedMovesBlack();
	void addCallbackListener(Callback callback);
	int getSize();
}
