package com.example.holoreversi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.holoreversi.widget.ReplayFragment;

public class ReplayActivity extends SherlockFragmentActivity {
	private static final String TAG = "ReplayActivity";
	public static final String EXTRA_GAME_ID = "gameId";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Show the Up button in the action bar.
		if (getSupportActionBar()!=null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		Intent extra = getIntent();
		if (extra == null) {
			Log.e(TAG, "No extra");
			finish();
			return;
		}
        FragmentManager fm = getSupportFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
        	ReplayFragment content = ReplayFragment.newInstance(extra.getLongExtra(EXTRA_GAME_ID, -1));
            fm.beginTransaction().add(android.R.id.content, content).commit();
        }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}