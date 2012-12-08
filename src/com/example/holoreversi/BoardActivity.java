package com.example.holoreversi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
	
	
	private static final String STATE_BOARD = "state_board";
	public static final String EXTRA_BOARD_SIZE = "BoardSize";
	private TextView mScoreWhite;
	private TextView mScoreBlack;
	private ImageButton mPlayerWhite;
	private ImageButton mPlayerBlack;
	private GameBoard mBoard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			int boardSize = getIntent().getIntExtra(EXTRA_BOARD_SIZE, 0);
			if (boardSize == 0) {
				Log.e("BoardActivity", "Invalid board size");
				finish();
				return;
			}
			mBoard = new GameBoard(boardSize);
		} else {
			mBoard = (GameBoard)savedInstanceState.get(STATE_BOARD);
		}
		final BoardView boardView = (BoardView)findViewById(R.id.board);
		mScoreWhite = (TextView)findViewById(R.id.scoreWhite);
		mScoreBlack = (TextView)findViewById(R.id.scoreBlack);
		mPlayerWhite = (ImageButton)findViewById(R.id.playerWhite);
		mPlayerBlack = (ImageButton)findViewById(R.id.playerBlack);
		
		BoardAdapter adapter = new BoardAdapter(mBoard);
		boardView.setAdapter(adapter);
		mBoard.addCallbackListener(this);
		
		setScoreView(mBoard.getScoreBlack(),mBoard.getScoreWhite());
		setPlayerView(mBoard.currentPlayer());
		
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
	public void onBoardUpdate(Board board) {
		setPlayerView(board.currentPlayer());
		setScoreView(board.getScoreBlack(), board.getScoreWhite());
	}

	private void showFinishDialog(int player) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String playerStr;
		if (player == Board.WHITE) {
			playerStr = getString(R.string.player_name_white);
		} else {
			playerStr = getString(R.string.player_name_blue);
		}
		builder
			.setMessage(playerStr +  getString(R.string.player_won))
			.setCancelable(true)
			.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					//Restart game
				}
			})
			.create()
			.show();
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(STATE_BOARD, mBoard);
		super.onSaveInstanceState(outState);
	}
	
	
}
