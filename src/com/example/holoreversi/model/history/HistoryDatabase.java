package com.example.holoreversi.model.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_FILE_NAME = "HoloReversi.db";
	private static final int DATABASE_VERSION = 3;
	
	public HistoryDatabase(Context context) {
		super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + HistoryContract.Tables.HISTORY + " ("
			+ HistoryContract.HistoryColumns._ID + " INTEGER PRIMARY KEY,"
			+ HistoryContract.HistoryColumns.COLUMN_NAME_TIME + " TEXT,"
			+ HistoryContract.HistoryColumns.COLUMN_NAME_NUMBEROFMOVES + " INTEGER,"
			+ HistoryContract.HistoryColumns.COLUMN_NAME_SCORE1 + " INTEGER,"
			+ HistoryContract.HistoryColumns.COLUMN_NAME_SCORE2 + " INTEGER"
			+ ");");
		db.execSQL("CREATE TABLE " + HistoryContract.Tables.GAME + " ("
            + HistoryContract.GameColumns._ID + " INTEGER PRIMARY KEY,"
            + HistoryContract.GameColumns.COLUMN_NAME_REL + " INTEGER,"
            + HistoryContract.GameColumns.COLUMN_NAME_KIND + " INTEGER,"
            + HistoryContract.GameColumns.COLUMN_NAME_X + " INTEGER,"
            + HistoryContract.GameColumns.COLUMN_NAME_Y + " INTEGER,"
            + "FOREIGN KEY ("+HistoryContract.GameColumns.COLUMN_NAME_REL+") REFERENCES "
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
