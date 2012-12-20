package com.example.holoreversi.model.history;

import java.util.ArrayList;

import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;

public class HistoryRecordBoard implements Board{
	private Board mBoard;
	private DataStore mDataStore;
	private long mGameId = 0;
	public HistoryRecordBoard(Board board, DataStore dataStore, long gameId) {
		mBoard = board;
		mDataStore = dataStore;
	}
	
	@Override
	public void resetBoard() {
		mBoard.resetBoard();
	}

	@Override
	public boolean hasUndo() {
		return mBoard.hasUndo();
	}

	@Override
	public int currentPlayer() {
		return mBoard.currentPlayer();
	}

	@Override
	public int getSize() {
		return mBoard.getSize();
	}

	@Override
	public int getScoreWhite() {
		return mBoard.getScoreWhite();
	}

	@Override
	public int getScoreBlack() {
		return mBoard.getScoreBlack();
	}

	@Override
	public Cell[][] getAll() {
		return mBoard.getAll();
	}

	@Override
	public boolean move(Cell cell) {
		boolean moved = mBoard.move(cell);
		if (moved) {
			mDataStore.insertMove(mGameId, cell);
		}
		return moved;
	}

	@Override
	public ArrayList<Cell> getAllowedMoves() {
		return mBoard.getAllowedMoves();
	}

	@Override
	public void addCallbackListener(Callback callback) {
		mBoard.addCallbackListener(callback);
	}

	@Override
	public boolean undoMove() {
		return mBoard.undoMove();
	}

	@Override
	public int winner() {
		return mBoard.winner();
	}

	@Override
	public boolean isGameEnded() {
		return mBoard.isGameEnded();
	}

	@Override
	public int checkNextMove(Cell cell) {
		return mBoard.checkNextMove(cell);
	}

}
