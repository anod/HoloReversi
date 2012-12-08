package com.example.holoreversi.model;

public interface Board {

	interface Callback {
		void onBoardUpdate(Board board);
	}

	int getScoreWhite();
	int getScoreBlack();
	void moveWhite(int x,int y);
	void moveBlack(int x, int y);
	Cell[] getAllowedMovesWhite();
	Cell[] getAllowedMovesBlack();
	void setCallback(Callback callback);
	int getSize();
}
