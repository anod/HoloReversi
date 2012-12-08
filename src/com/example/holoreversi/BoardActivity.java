package com.example.holoreversi;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;

public class BoardActivity extends SherlockActivity {
	private static final int SIZE_BOARD_4 = 4;
	private Board mBoard;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		initBoardView(SIZE_BOARD_4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_board, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void initBoardView(int size) {
		final TableLayout boardView = (TableLayout)findViewById(R.id.board);
		TableRow tr;
		final LayoutInflater li = getLayoutInflater();
		//add header
		tr = createHintRow(size, li);
		boardView.addView(tr);
		
		int middle = (int)(size / 2) - 1;

		
		for(int i=0; i<size; i++) {
			tr = (TableRow)li.inflate(R.layout.board_row, null);
			for(int j=0; j<size + 2; j++) {
				if (j == 0 || j == size+1) {
					final TextView label = (TextView)li.inflate(R.layout.board_view_pos, null);
					label.setText(""+i);
					tr.addView(label);
				} else {
					final ImageButton btn = (ImageButton)li.inflate(R.layout.board_view_btn, null);
					if (middle == i && middle+1 == j) {
						btn.setImageResource(R.drawable.blue);
					} else if (middle == i && middle+2 == j) {
						btn.setImageResource(R.drawable.white);
					} else if (middle+1 == i && middle+1 == j) {
						btn.setImageResource(R.drawable.white);
					} else if (middle+1 == i && middle+2 == j) {
						btn.setImageResource(R.drawable.blue);
					}
					tr.addView(btn);
				}
			}
			boardView.addView(tr);
		}

		tr = createHintRow(size, li);
		boardView.addView(tr);

		
	}

	private TableRow createHintRow(int size, LayoutInflater li) {
		TableRow tr = (TableRow)li.inflate(R.layout.board_row, null);
		for(int i=0; i<size+2; i++) {
			TextView label = (TextView)li.inflate(R.layout.board_view_pos, null);
			if (i == 0 || i == size+1) {
				label.setText(" ");
			} else {
				label.setText("" + (char)(96+i));
			}
			tr.addView(label);
		}
		return tr;
	}
}
