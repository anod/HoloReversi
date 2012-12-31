package com.example.holoreversi;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.holoreversi.widget.ReplayFragment;

public class ReplayActivity extends SherlockFragmentActivity {
	public static final String EXTRA_GAME_ID = "gameId";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Show the Up button in the action bar.
		if (getSupportActionBar()!=null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
        FragmentManager fm = getSupportFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
        	ReplayFragment content = new ReplayFragment();
            fm.beginTransaction().add(android.R.id.content, content).commit();
        }
	}
}