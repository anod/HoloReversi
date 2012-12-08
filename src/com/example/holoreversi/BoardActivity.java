package com.example.holoreversi;

import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.GameBoard;
import com.example.holoreversi.widget.BoardAdapter;
import com.example.holoreversi.widget.BoardView;

public class BoardActivity extends SherlockActivity implements Board.Callback {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		final BoardView boardView = (BoardView)findViewById(R.id.board);
		GameBoard board = new GameBoard();
		BoardAdapter adapter = new BoardAdapter(board);
		boardView.setAdapter(adapter);
		board.addCallbackListener(this);
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

	@Override
	public void onBoardUpdate(Board board, Cell cell, int newState) {
		// TODO Auto-generated method stub
		
	}

}
