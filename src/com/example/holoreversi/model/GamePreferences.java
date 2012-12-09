package com.example.holoreversi.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class GamePreferences {

	private static final String DEFAULT_BOARD_SIZE = "8";
	private static final String PREF_BOARD_SIZE = "board_size";
	final private SharedPreferences mPrefs;

	public GamePreferences(Context context) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void saveBoardSize(String size) {
		Editor edit = mPrefs.edit();
		edit.putString(PREF_BOARD_SIZE, size);
		edit.commit();
	}
	
	public String loadBoardSize() {
		return mPrefs.getString(PREF_BOARD_SIZE, DEFAULT_BOARD_SIZE);
	}
}
