package com.example.holoreversi;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;

import com.example.holoreversi.model.history.DataStore;
import com.example.holoreversi.model.history.SQLiteDataStore;

public class HistoryActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DataStore dataStore = new SQLiteDataStore(this);
		Cursor cursor = dataStore.getGames();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
			this, android.R.layout.simple_list_item_2, cursor,
			new String[] { DataStore.COLUMN_NAME_TIME, DataStore.COLUMN_NAME_NUMBEROFMOVES},
			new int[] { android.R.id.text1, android.R.id.text2 }
		);
		
		setListAdapter(adapter);
	}


}
