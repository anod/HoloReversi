package com.example.holoreversi;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.GameBoard;
import com.example.holoreversi.widget.BoardAdapter;
import com.example.holoreversi.widget.BoardView;

public class BoardActivity extends SherlockActivity implements Board.Callback {
	
	
	public static final String EXTRA_BOARD_SIZE = "BoardSize";
	private TextView mScoreWhite;
	private TextView mScoreBlack;
	private ImageButton mPlayerWhite;
	private ImageButton mPlayerBlack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		int boardSize = getIntent().getIntExtra(EXTRA_BOARD_SIZE, 0);
		if (boardSize == 0) {
			Log.e("BoardActivity", "Invalid board size");
			finish();
			return;
		}
		final BoardView boardView = (BoardView)findViewById(R.id.board);
		mScoreWhite = (TextView)findViewById(R.id.scoreWhite);
		mScoreBlack = (TextView)findViewById(R.id.scoreBlack);
		mPlayerWhite = (ImageButton)findViewById(R.id.playerWhite);
		mPlayerBlack = (ImageButton)findViewById(R.id.playerBlack);
		
		GameBoard board = new GameBoard(boardSize);
		BoardAdapter adapter = new BoardAdapter(board);
		boardView.setAdapter(adapter);
		board.addCallbackListener(this);
		
		setScoreView(board.getScoreBlack(),board.getScoreWhite());
		setPlayerView(board.currentPlayer());
		
	}

	private void setPlayerView(int currentPlayer) {
		if (currentPlayer == Board.BLACK) {
			mPlayerBlack.setEnabled(true);
			mPlayerWhite.setEnabled(false);
		} else {
			mPlayerBlack.setEnabled(false);
			mPlayerWhite.setEnabled(true);
		}
		
	}

	private void setScoreView(int scoreBlack, int scoreWhite) {
		mScoreWhite.setText(""+scoreWhite);
		mScoreBlack.setText(""+scoreBlack);
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
	public void onBoardUpdate(Board board, Cell cell) {
		setPlayerView(board.currentPlayer());
		setScoreView(board.getScoreBlack(), board.getScoreWhite());
	}

}
