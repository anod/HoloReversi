package com.example.holoreversi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.holoreversi.model.GamePreferences;

public class MainActivity extends SherlockActivity implements OnClickListener{

	private Context mContext;
	private GamePreferences mGamePrefs;
	private String[] mBoardValues;
	private Spinner mSelector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = (Context)this;
		mGamePrefs = new GamePreferences(this);
		
		mSelector = (Spinner)findViewById(R.id.boardSizeSelector);
		mBoardValues = getResources().getStringArray(R.array.board_sizes_values);
		String savedSize = mGamePrefs.loadBoardSize();
		for (int i=0; i<mBoardValues.length; i++) {
			if (savedSize.equals(mBoardValues[i])) {
				mSelector.setSelection(i);
				break;
			}
		}
		
		Button start = (Button)findViewById(R.id.button2Player);
		start.setOnClickListener(this);
		
		Button single = (Button)findViewById(R.id.button1Player);
		single.setOnClickListener(this);
		
		Button howTo = (Button)findViewById(R.id.buttonHowTo);
		howTo.setOnClickListener(this);
		
		Button history = (Button)findViewById(R.id.buttonHistory);
		history.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.button1Player:
			startGame(true);
			break;
		case R.id.button2Player:
			startGame(false);
			break;
		case R.id.buttonHistory:
			Intent historyIntent = new Intent(mContext, HistoryActivity.class);
			startActivity(historyIntent);
			break;
		case R.id.buttonHowTo:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://en.wikipedia.org/wiki/Reversi"));
			startActivity(browserIntent);
			break;

		default:
			break;
		}
		
	}

	private void startGame(boolean isComputerPlayer) {
		Intent intent = new Intent(mContext, BoardActivity.class);
		
		String selected = mBoardValues[mSelector.getSelectedItemPosition()];
		mGamePrefs.saveBoardSize(selected);
		intent.putExtra(BoardActivity.EXTRA_BOARD_SIZE ,Integer.parseInt(selected));
		intent.putExtra(BoardActivity.EXTRA_COMPUTER_PLAYER, isComputerPlayer);
		startActivity(intent);
	}
}
