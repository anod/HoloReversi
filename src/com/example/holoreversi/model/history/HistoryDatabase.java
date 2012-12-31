package com.example.holoreversi.model.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_FILE_NAME = "HoloReversi.db";
	private static final int DATABASE_VERSION = 5;
	
	public HistoryDatabase(Context context) {
		super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + HistoryContract.Tables.HISTORY + " ("
			+ HistoryContract.HistoryColumns._ID + " INTEGER PRIMARY KEY,"
			+ HistoryContract.HistoryColumns.TIME + " TEXT,"
			+ HistoryContract.HistoryColumns.NUMBEROFMOVES + " INTEGER,"
			+ HistoryContract.HistoryColumns.SCORE1 + " INTEGER,"
			+ HistoryContract.HistoryColumns.SCORE2 + " INTEGER,"
			+ HistoryContract.HistoryColumns.SIZE + " INTEGER"
			+ ");");
		db.execSQL("CREATE TABLE " + HistoryContract.Tables.GAME + " ("
            + HistoryContract.GameColumns._ID + " INTEGER PRIMARY KEY,"
            + HistoryContract.GameColumns.REL + " INTEGER,"
            + HistoryContract.GameColumns.KIND + " INTEGER,"
            + HistoryContract.GameColumns.COL_X + " INTEGER,"
            + HistoryContract.GameColumns.COL_Y + " INTEGER,"
            + "FOREIGN KEY ("+HistoryContract.GameColumns.REL+") REFERENCES "
            + HistoryContract.Tables.HISTORY+" ("+HistoryContract.GameColumns._ID+")"
            + ");");

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           // Kills the table and existing data
           db.execSQL("DROP TABLE IF EXISTS " +  HistoryContract.Tables.HISTORY);
           db.execSQL("DROP TABLE IF EXISTS " + HistoryContract.Tables.GAME);

           // Recreates the database with a new version
           onCreate(db);
	}
}
