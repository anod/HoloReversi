package com.example.holoreversi.widget;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.holoreversi.model.*;

public class DSsqlite extends SQLiteOpenHelper implements DataStore{

	private static final String MOVES_TABLE_NAME = "HISTORY_MOVES";
	private static final String GAMES_TABLE_NAME = "HISTORY_GAMES";
	private static final String COULMN_ID = "ID";
	private static final String COLUMN_NAME_KIND = "COLOR";
	private static final String COLUMN_NAME_X = "X";
	private static final String COLUMN_NAME_Y = "Y";
	private static final String COLUMN_NAME_REL = "GAMEID";
	private static final String COLUMN_NAME_TIME = "STARTTIME";
	private static final String COLUMN_NAME_SCORE1 = "SCOREBLACK";
	private static final String COLUMN_NAME_SCORE2 = "SCOREWHITE";
	public DSsqlite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean open() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insert(ArrayList<Cell> cell) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Cell> get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + MOVES_TABLE_NAME)
		db.execSQL("CREATE TABLE " + C + " ("
                + COULMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME_REL + " INTEGER,"
                + COLUMN_NAME_KIND + " INTEGER,"
                + COLUMN_NAME_X + " INTEGER,"
                + COLUMN_NAME_Y + " INTEGER,"
                + "FOREIGN KEY ("+COLUMN_NAME_REL+") REFERENCES "+GAMES_TABLE_NAME+" ("+COLUMN_ID+")"
                + ");");

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
