package com.example.holoreversi.model.history;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;

public class HistoryRecordBoard implements Board, Board.Callback{
	
	public interface OnGameIdChangeListener {
		void onChange(long gameId);
	}
	
	private Board mBoard;
	private DataStore mDataStore;
	private long mGameId = 0;
	private int mMoves;
	private OnGameIdChangeListener mGameIdChangeListener;
	
	private static final Object[] sLock = new Object[0];
	
	public HistoryRecordBoard(Board board, DataStore dataStore, OnGameIdChangeListener listener) {
		mBoard = board;
		mDataStore = dataStore;
		mGameIdChangeListener = listener;
		mBoard.addCallbackListener(this);
	}
	
	public void createNewGame() {
		mGameId = mDataStore.insertGame();
		mMoves = 0;
		mGameIdChangeListener.onChange(mGameId);
	}
	
	public void loadGame(long gameId) {
		mGameId = gameId;
		Cursor cursor = mDataStore.getGameById(gameId);
		if (cursor == null) {
			Log.e("HistoryRecordBoard", "Game cannot be loaded, id: " + gameId);
			return;
		}
		mMoves = cursor.getInt(DataStore.IDX_NUMBEROFMOVES);
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
		boolean moved = mBoard.move(cell);
		if (moved) {
			new Thread(new Runnable() {
				public void run() {
					synchronized (sLock) {
						mDataStore.insertMove(mGameId, cell);
						mMoves++;
					}
				}
			}).start();
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
		boolean result = mBoard.undoMove();
		if (result) {
			synchronized (sLock) {
				mMoves--;
			}
		}
		return result;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBoardUpdate(final Board board) {
		new Thread(new Runnable() {
		public void run() {
			synchronized (sLock) {
				mDataStore.updateScores(mGameId, board.getScoreBlack(), board.getScoreWhite(), mMoves);
			}
		}}).start();
		
	}

}
