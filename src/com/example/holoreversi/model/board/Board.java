package com.example.holoreversi.model.board;

import java.util.ArrayList;

import com.example.holoreversi.model.Cell;

public interface Board {
	final public static int BLACK = 2;
	final public static int WHITE = 1;
	final public static int EMPTY = 0;
	
	interface Callback {
		void onNextPlayer(int nextPlayer);
		void onBoardUpdate(final Board board);
		void onGameEnd(final Board board);
		void onCellUndo(Cell cell, int kind);
	}

	void resetBoard();
	boolean hasUndo();
	int currentPlayer();
	int getSize();
	int getScoreWhite();
	int getScoreBlack();
	Cell[][] getAll();
	boolean move(final Cell cell);
	ArrayList<Cell> getAllowedMoves();
	void addCallbackListener(Callback callback);
	boolean undoMove();
	int winner();
	boolean isGameEnded();
	int checkNextMove(Cell cell);
}
