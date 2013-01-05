package com.example.holoreversi.model.board;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.history.HistoryContract;
import com.example.holoreversi.model.history.HistoryProviderHelper;

public class HistoryRecordBoard implements Board, Board.Callback{
	
	public interface OnGameIdChangeListener {
		void onChange(long gameId);
	}
	
	private Board mBoard;
	private HistoryProviderHelper mHistoryProvider;
	private long mGameId = 0;
	private int mMoves;
	private OnGameIdChangeListener mGameIdChangeListener;
	
	public HistoryRecordBoard(Board board, HistoryProviderHelper historyProvider, OnGameIdChangeListener listener) {
		mBoard = board;
		mHistoryProvider = historyProvider;
		mGameIdChangeListener = listener;
		mBoard.addCallbackListener(this);
	}
	
	public void createNewGame() {
		mGameId = mHistoryProvider.insertGame(mBoard.getSize());
		mMoves = 0;
		mGameIdChangeListener.onChange(mGameId);
	}
	
	public void loadGame(long gameId) {
		mGameId = gameId;
		Cursor cursor = mHistoryProvider.getGameById(gameId);
		if (cursor == null) {
			Log.e("HistoryRecordBoard", "Game cannot be loaded, id: " + gameId);
			return;
		}
		mMoves = cursor.getInt(HistoryContract.HistoryColumns.IDX_NUMBEROFMOVES);
		cursor.close();
		
	}
	
	
	@Override
	public void resetBoard() {
		createNewGame();
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
	public boolean move(final Cell cell) {
		int currentPlayer = mBoard.currentPlayer();
		boolean moved = mBoard.move(cell);
		if (moved) {
			mHistoryProvider.insertMove(mGameId, cell, currentPlayer, true);
			mMoves++;
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
		boolean changed = mBoard.undoMove();
		if (changed) {
			mMoves--;
		}
		return changed;
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

	@Override
	public void onNextPlayer(int nextPlayer) {
		// Nothing so far
	}

	@Override
	public void onBoardUpdate(final Board board) {
		// Nothing so far
	}

	@Override
	public void onGameEnd(final Board board) {
		mHistoryProvider.updateScores(mGameId, board.getScoreBlack(), board.getScoreWhite(), mMoves);
		
	}

	@Override
	public void onCellUndo(Cell cell, int kind) {
		mHistoryProvider.insertMove(mGameId, cell, kind, false);
	}

}
