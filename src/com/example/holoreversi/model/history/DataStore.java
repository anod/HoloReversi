package com.example.holoreversi.model.history;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.example.holoreversi.model.Cell;


public interface DataStore extends BaseColumns {
	static final String MOVES_TABLE_NAME = "HISTORY_MOVES";
	static final String GAMES_TABLE_NAME = "HISTORY_GAMES";
	static final String COLUMN_NAME_X = "X";
	static final String COLUMN_NAME_Y = "Y";
	static final String COLUMN_NAME_REL = "GAMEID";
	static final String COLUMN_NAME_TIME = "STARTTIME";
	static final String COLUMN_NAME_KIND = "COLOR";
	static final String COLUMN_NAME_SCORE1 = "SCOREBLACK";
	static final String COLUMN_NAME_SCORE2 = "SCOREWHITE";
	static final String COLUMN_NAME_NUMBEROFMOVES = "NUMBEROFMOVES";
	static final String DATABASE_FILE_NAME = "HoloReversi.db";
	
	public static final int IDX_TIME = 1;
	public static final int IDX_NUMBEROFMOVES = 2;
	public static final int IDX_SCOREBLACK = 3;
	public static final int IDX_SCOREWHITE = 4;
	
	/**
	 * 
	 * @param gid
	 * @param cell
	 * @return
	 */
	long insertMove(long gid ,Cell cell);
	/**
	 * 
	 * @param gameId
	 * @param scoresBalck
	 * @param scoresWhite
	 * @param numMoves
	 * @return the number of rows affected
	 */
	int updateScores(long gameId, int scoresBalck, int scoresWhite, int numMoves);
	/**
	 * 
	 * @return gameId
	 */
	long insertGame();
	Cursor getMoves(long gid);
	Cursor getGames();
	Cursor getGameById(long gameId);
	
}
