package com.example.holoreversi.model;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class DSsqlite implements DataStore{
	
	private static final String MOVES_TABLE_NAME = "HISTORY_MOVES";
	private static final String GAMES_TABLE_NAME = "HISTORY_GAMES";
	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_NAME_X = "X";
	private static final String COLUMN_NAME_Y = "Y";
	private static final String COLUMN_NAME_REL = "GAMEID";
	private static final String COLUMN_NAME_TIME = "STARTTIME";
	private static final String COLUMN_NAME_KIND = "COLOR";
	private static final String COLUMN_NAME_SCORE1 = "SCOREBLACK";
	private static final String COLUMN_NAME_SCORE2 = "SCOREWHITE";
	private static final String COLUMN_NAME_NUMBEROFMOVES = "NUMBEROFMOVES";
	private static final String DATABASE_FILE_NAME = "HoloReversi.db";
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_FILE_NAME, null, 1);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			String test = "CREATE TABLE " + GAMES_TABLE_NAME + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY,"
					+ COLUMN_NAME_TIME + " TEXT,"
					+ COLUMN_NAME_NUMBEROFMOVES + " INTEGER,"
					+ COLUMN_NAME_SCORE1 + " INTEGER,"
					+ COLUMN_NAME_SCORE2 + " INTEGER"
					+ ");";
			db.execSQL("CREATE TABLE " + GAMES_TABLE_NAME + " ("
				+ COLUMN_ID + " INTEGER PRIMARY KEY,"
				+ COLUMN_NAME_TIME + " TEXT,"
				+ COLUMN_NAME_NUMBEROFMOVES + " INTEGER,"
				+ COLUMN_NAME_SCORE1 + " INTEGER,"
				+ COLUMN_NAME_SCORE2 + " INTEGER"
				+ ");");
			test = "CREATE TABLE " + MOVES_TABLE_NAME + " ("
	                + COLUMN_ID + " INTEGER PRIMARY KEY,"
	                + COLUMN_NAME_REL + " INTEGER,"
	                + COLUMN_NAME_KIND + " INTEGER,"
	                + COLUMN_NAME_X + " INTEGER,"
	                + COLUMN_NAME_Y + " INTEGER,"
	                + "FOREIGN KEY ("+COLUMN_NAME_REL+") REFERENCES "+GAMES_TABLE_NAME+" ("+COLUMN_ID+")"
	                + ");";
			db.execSQL("CREATE TABLE " + MOVES_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME_REL + " INTEGER,"
                + COLUMN_NAME_KIND + " INTEGER,"
                + COLUMN_NAME_X + " INTEGER,"
                + COLUMN_NAME_Y + " INTEGER,"
                + "FOREIGN KEY ("+COLUMN_NAME_REL+") REFERENCES "+GAMES_TABLE_NAME+" ("+COLUMN_ID+")"
                + ");");

		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// There is no upgrade at the moment will use only one DB
		}
	}
	
	private DatabaseHelper mOpenHelper = null; // private object to hold DB

	public DSsqlite(Context context) {
		mOpenHelper = new DatabaseHelper(context);
	}

	@Override
	public long insertMove(long gid, Cell cell) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_X, cell.x);
		values.put(COLUMN_NAME_Y, cell.y);
		values.put(COLUMN_NAME_REL, gid);
		values.put(COLUMN_NAME_KIND, cell.contents);
		long rowid = db.insert(MOVES_TABLE_NAME, null, values);
		return rowid;
	}

	@Override
	public long insertGame() {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		long timeMilliseconds = (new Date()).getTime();
		values.put(COLUMN_NAME_TIME, String.valueOf(timeMilliseconds));
		//values.put(COLUMN_NAME_SCORE1, 0);
		//values.put(COLUMN_NAME_SCORE2, 0);
		//values.put(COLUMN_NAME_NUMBEROFMOVES, 0);
		long rowid = db.insert(GAMES_TABLE_NAME, COLUMN_NAME_TIME, values);
		return rowid;
	}

	@Override
	public ArrayList<Game> getGames() {
		ArrayList<Game> ret = new ArrayList<Game>();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(GAMES_TABLE_NAME);
        Cursor c = qb.query(mOpenHelper.getReadableDatabase(), null, null, null, null, null, null);
		while (c.moveToNext()) {
			Game g = new Game();
			g.ID = c.getInt(c.getColumnIndex(COLUMN_ID));
			g.date = c.getInt(c.getColumnIndex(COLUMN_NAME_TIME));
			g.scoreBlack = c.getInt(c.getColumnIndex(COLUMN_NAME_SCORE1));
			g.scoreWhite = c.getInt(c.getColumnIndex(COLUMN_NAME_SCORE2));
			ret.add(g);
		}
   		return ret;
	}

	@Override
	public ArrayList<Cell> getMoves(long gid) {
		ArrayList<Cell> ret = new ArrayList<Cell>();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MOVES_TABLE_NAME);
        String whereClause = COLUMN_NAME_REL + " = " + gid;
        String sortOrder = COLUMN_ID + " desc";
        Cursor c = qb.query(mOpenHelper.getReadableDatabase(), null, whereClause, null, null, null, sortOrder);
        int xColumn = c.getColumnIndex(COLUMN_NAME_X);
        int yColumn = c.getColumnIndex(COLUMN_NAME_Y);
        int contentsColumn = c.getColumnIndex(COLUMN_NAME_KIND);
        while (c.moveToNext()) {
        	int x = c.getInt(xColumn);
        	int y = c.getInt(yColumn);
        	Cell cell = new Cell(x, y);
        	cell.contents = c.getInt(contentsColumn);
        	ret.add(cell);
        }
        return ret;
	}


}
