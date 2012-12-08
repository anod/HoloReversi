package com.example.holoreversi.model;

public interface Board {

	interface Callback {
		void onScoreUpdate(int score);
		void onBoardUpdate(Board board);
	}
	
	void moveWhite(int x,int y);
	void moveBlack(int x, int y);
	Cell[] getAllowedMovesWhite();
	Cell[] getAllowedMovesBlack();
	void setCallback(Callback callback);
}
