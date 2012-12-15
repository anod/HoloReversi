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

public class MainActivity extends SherlockActivity {

	private Context mContext;
	private GamePreferences mGamePrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = (Context)this;
		mGamePrefs = new GamePreferences(this);
		
		final Spinner selector = (Spinner)findViewById(R.id.boardSizeSelector);
		final String[] boardValues = getResources().getStringArray(R.array.board_sizes_values);
		String savedSize = mGamePrefs.loadBoardSize();
		for (int i=0; i<boardValues.length; i++) {
			if (savedSize.equals(boardValues[i])) {
				selector.setSelection(i);
				break;
			}
		}
		
		Button start = (Button)findViewById(R.id.button2Player);
		start.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BoardActivity.class);
				
				String selected = boardValues[selector.getSelectedItemPosition()];
				mGamePrefs.saveBoardSize(selected);
				intent.putExtra(BoardActivity.EXTRA_BOARD_SIZE ,Integer.parseInt(selected));
				intent.putExtra(BoardActivity.EXTRA_COMPUTER_PLAYER, 0);
				startActivity(intent);
			}
		});
		Button single = (Button)findViewById(R.id.button1Player);
		single.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BoardActivity.class);
				
				String selected = boardValues[selector.getSelectedItemPosition()];
				mGamePrefs.saveBoardSize(selected);
				intent.putExtra(BoardActivity.EXTRA_BOARD_SIZE ,Integer.parseInt(selected));
				intent.putExtra(BoardActivity.EXTRA_COMPUTER_PLAYER, 1);
				startActivity(intent);
			}
		});
		
		Button howTo = (Button)findViewById(R.id.buttonHowTo);
		howTo.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://en.wikipedia.org/wiki/Reversi"));
				startActivity(browserIntent);
			}
		});
	}


}
