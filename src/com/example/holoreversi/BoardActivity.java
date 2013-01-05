package com.example.holoreversi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.board.Board;
import com.example.holoreversi.model.board.GameBoard;
import com.example.holoreversi.model.board.HistoryRecordBoard;
import com.example.holoreversi.model.history.HistoryProviderHelper;
import com.example.holoreversi.widget.BoardAdapter;
import com.example.holoreversi.widget.BoardView;
import com.example.holoreversi.widget.FinishDialogFragment;
import com.example.holoreversi.widget.ScoreViewAdapter;

public class BoardActivity extends SherlockActivity implements Board.Callback, HistoryRecordBoard.OnGameIdChangeListener {
	
	
	private static final String GAME_ID = "game_id";
	private static final String STATE_BOARD = "state_board";
	public static final String EXTRA_BOARD_SIZE = "BoardSize";
	public static final String EXTRA_COMPUTER_PLAYER = "ComputerPlayer";
	private ScoreViewAdapter mScoreAdapter;
	private GameBoard mBoard;
	private Button mUndoButton;
	private long mGameId;
	private boolean isComputerPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		// Show the Up button in the action bar.
		if (getSupportActionBar()!=null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mGameId = 0;
		HistoryProviderHelper historyProvider = new HistoryProviderHelper(this);
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
			mGameId = savedInstanceState.getLong(GAME_ID);
		}
		isComputerPlayer = getIntent().getBooleanExtra(EXTRA_COMPUTER_PLAYER, false);
		final BoardView boardView = (BoardView)findViewById(R.id.board);
		
		final HistoryRecordBoard boardWithHistory = new HistoryRecordBoard(mBoard, historyProvider, this);
		if (mGameId > 0) {
			boardWithHistory.loadGame(mGameId);
		} else {
			boardWithHistory.createNewGame();
		}
		
		BoardAdapter adapter = new BoardAdapter(boardWithHistory, isComputerPlayer);
		boardView.setAdapter(adapter);

		mScoreAdapter = new ScoreViewAdapter();
		
		setupWidgets();
		mBoard.addCallbackListener(this);
		
		mScoreAdapter.init(mBoard);
	}

	private void setupWidgets() {
		mScoreAdapter.setupWidgets(
			(TextView)findViewById(R.id.scoreWhite),
			(TextView)findViewById(R.id.scoreBlack),
			(ImageButton)findViewById(R.id.playerWhite),
			(ImageButton)findViewById(R.id.playerBlack)
		);
		mUndoButton = (Button)findViewById(R.id.buttonUndo);
		mUndoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBoard.undoMove();
				if(isComputerPlayer)
				{
					mBoard.undoMove();
				}
			}
		});
		mUndoButton.setEnabled(mBoard.hasUndo());
		final boolean hasActionBar = getResources().getBoolean(R.bool.board_has_actionbar);
		final Button startButtonCompat = (Button)findViewById(R.id.buttonStartCompat);
		if (hasActionBar) {
			startButtonCompat.setVisibility(View.GONE);
		} else {
			startButtonCompat.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					restartGame();
				}
			});
		}
	}

	private void restartGame() {
		mBoard.resetBoard();
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
		case R.id.menu_newgame:
			restartGame();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBoardUpdate(Board board) {
		if (board.hasUndo()) {
			mUndoButton.setEnabled(true);
		} else {
			mUndoButton.setEnabled(false);
		}
	}

	
	private void showFinishDialog() {
		FinishDialogFragment.createFinishDialog(
			mBoard.getScoreBlack(), mBoard.getScoreWhite(), mBoard.winner(), this, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					restartGame();
				}
			}
		).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(STATE_BOARD, mBoard);
		outState.putLong(GAME_ID, mGameId);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onNextPlayer(int nextPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChange(long gameId) {
		mGameId = gameId;
	}

	@Override
	public void onGameEnd(Board board) {
		showFinishDialog();
	}

	@Override
	public void onCellUndo(Cell cell, int kind) {
		// TODO Auto-generated method stub
		
	}
	
	
}
