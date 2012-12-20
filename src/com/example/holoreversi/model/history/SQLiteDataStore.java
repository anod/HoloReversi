package com.example.holoreversi.model.history;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.holoreversi.model.Cell;

public class SQLiteDataStore implements DataStore {
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		private static final int DATABASE_VERSION = 3;
		public DatabaseHelper(Context context) {
			super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			String test = "CREATE TABLE " + GAMES_TABLE_NAME + " ("
					+ _ID + " INTEGER PRIMARY KEY,"
					+ COLUMN_NAME_TIME + " TEXT,"
					+ COLUMN_NAME_NUMBEROFMOVES + " INTEGER,"
					+ COLUMN_NAME_SCORE1 + " INTEGER,"
					+ COLUMN_NAME_SCORE2 + " INTEGER"
					+ ");";
			db.execSQL("CREATE TABLE " + GAMES_TABLE_NAME + " ("
				+ _ID + " INTEGER PRIMARY KEY,"
				+ COLUMN_NAME_TIME + " TEXT,"
				+ COLUMN_NAME_NUMBEROFMOVES + " INTEGER,"
				+ COLUMN_NAME_SCORE1 + " INTEGER,"
				+ COLUMN_NAME_SCORE2 + " INTEGER"
				+ ");");
			test = "CREATE TABLE " + MOVES_TABLE_NAME + " ("
	                + _ID + " INTEGER PRIMARY KEY,"
	                + COLUMN_NAME_REL + " INTEGER,"
	                + COLUMN_NAME_KIND + " INTEGER,"
	                + COLUMN_NAME_X + " INTEGER,"
	                + COLUMN_NAME_Y + " INTEGER,"
	                + "FOREIGN KEY ("+COLUMN_NAME_REL+") REFERENCES "+GAMES_TABLE_NAME+" ("+_ID+")"
	                + ");";
			db.execSQL("CREATE TABLE " + MOVES_TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME_REL + " INTEGER,"
                + COLUMN_NAME_KIND + " INTEGER,"
                + COLUMN_NAME_X + " INTEGER,"
                + COLUMN_NAME_Y + " INTEGER,"
                + "FOREIGN KEY ("+COLUMN_NAME_REL+") REFERENCES "+GAMES_TABLE_NAME+" ("+_ID+")"
                + ");");

		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	           // Kills the table and existing data
	           db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE_NAME);
	           db.execSQL("DROP TABLE IF EXISTS " + MOVES_TABLE_NAME);

	           // Recreates the database with a new version
	           onCreate(db);
		}
	}
	
	private DatabaseHelper mOpenHelper = null; // private object to hold DB

	public SQLiteDataStore(Context context) {
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
	public Cursor getGames() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(GAMES_TABLE_NAME);
        Cursor c = qb.query(mOpenHelper.getReadableDatabase(), null, null, null, null, null, null);
        return c;
	}

	@Override
	public Cursor getMoves(long gid) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MOVES_TABLE_NAME);
        String whereClause = COLUMN_NAME_REL + " = " + gid;
        String sortOrder = _ID + " desc";
        Cursor c = qb.query(mOpenHelper.getReadableDatabase(), null, whereClause, null, null, null, sortOrder);
        return c;
	}


}
