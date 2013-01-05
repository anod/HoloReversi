package com.example.holoreversi.model.history;

import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.holoreversi.model.Cell;

public class HistoryProviderHelper  {
	
	private Context mContext;
	private ContentResolver mContentResolver;

	public HistoryProviderHelper(Context context) {
		mContext = context;
		mContentResolver = mContext.getContentResolver();
	}

	public void insertMove(long gameId, Cell cell, int kind, boolean isMove) {
		ContentValues values = new ContentValues();
		values.put(HistoryContract.GameColumns.COL_X, cell.x);
		values.put(HistoryContract.GameColumns.COL_Y, cell.y);
		values.put(HistoryContract.GameColumns.REL, gameId);
		values.put(HistoryContract.GameColumns.KIND, kind);
		values.put(HistoryContract.GameColumns.ACTION, (isMove) ? HistoryContract.Game.ACTION_MOVE : HistoryContract.Game.ACTION_UNDO);
		Uri url = HistoryContract.Game.buildGameUri(gameId);
		mContentResolver.insert(url, values);
	}

	public long insertGame(int boardSize) {
		ContentValues values = new ContentValues();
		long timeMilliseconds = (new Date()).getTime();
		values.put(HistoryContract.HistoryColumns.TIME, String.valueOf(timeMilliseconds));
		values.put(HistoryContract.HistoryColumns.SCORE1, 0);
		values.put(HistoryContract.HistoryColumns.SCORE2, 0);
		values.put(HistoryContract.HistoryColumns.NUMBEROFMOVES, 0);
		values.put(HistoryContract.HistoryColumns.SIZE, boardSize);
		
		Uri historyUri = mContentResolver.insert(HistoryContract.History.CONTENT_URI, values);
		String segment = historyUri.getPathSegments().get(HistoryProvider.GAME_ID_PATH_POSITION);
		long rowid = Long.valueOf(segment);
		return rowid;
	}
	
	public int updateScores(long gameId, int scoresBalck, int scoresWhite, int numMoves) {
		ContentValues values = new ContentValues();
		values.put(HistoryContract.HistoryColumns.SCORE1, scoresBalck);
		values.put(HistoryContract.HistoryColumns.SCORE2, scoresWhite);
		values.put(HistoryContract.HistoryColumns.NUMBEROFMOVES, numMoves);
		
		Uri url = HistoryContract.History.buildHistoryUri(gameId);
		return mContentResolver.update(url, values, HistoryContract.HistoryColumns._ID+ "=?",  new String[] { Long.toString(gameId) });
	}

	public Cursor getGames() {
        String selection = HistoryContract.HistoryColumns.NUMBEROFMOVES + ">?";
        String[] selectionArgs = new String[] { "0" };
		return mContentResolver.query(HistoryContract.History.CONTENT_URI, null, selection, selectionArgs, null);
	}

	public Cursor getMoves(long gameId) {
        String sortOrder = HistoryContract.GameColumns._ID + " DESC";
        Uri url = HistoryContract.Game.buildGameUri(gameId);
		return mContentResolver.query(url, null, null, null, sortOrder);
	}
	public Cursor getGameById(long gameId) {
        Uri url = HistoryContract.History.buildHistoryUri(gameId);
		return mContentResolver.query(url, null, null, null, null);
	}


}
