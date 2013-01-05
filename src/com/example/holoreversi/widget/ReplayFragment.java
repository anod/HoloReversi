package com.example.holoreversi.widget;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.holoreversi.R;
import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.board.GameBoard;
import com.example.holoreversi.model.history.HistoryContract;
import com.example.holoreversi.model.history.HistoryProviderHelper;

public class ReplayFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<Cursor>, OnClickListener {
	
	private static final String GAME_ID = "game_id";
	private static final String STATE_BOARD = "state_board";
	private static final String GAME_MOVES = "moves";
	private static final String BOARD_SIZE = "board_size";
	private static final String POSITION = "position";
	
	private GameBoard mBoard;
	private GameInfo mGameInfo;
	private int mCurrentPos;
	private Cursor mCursor;
	private BoardView mBoardView;
	private ScoreViewAdapter mScoreAdapter;
	/**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static ReplayFragment newInstance(long gameId) {
    	ReplayFragment f = new ReplayFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putLong(GAME_ID, gameId);
        f.setArguments(args);

        return f;
    }
    
    static class GameInfo {
    	long id;
    	int moves;
    	int boardSize;
    	GameInfo(long gameId) {
    		this.id = gameId;
    	}
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
    	
		HistoryProviderHelper historyProvider = new HistoryProviderHelper(getActivity());
		if (savedInstanceState == null) {
			long gameId = getArguments().getLong(GAME_ID, -1);
			if (gameId == -1) {
				Log.e("ReplayFragment", "Invalid game id");
			}
			mGameInfo = new GameInfo(gameId);
			Cursor gameInfoCursor = historyProvider.getGameById(gameId);
			
			if (gameInfoCursor != null && gameInfoCursor.moveToFirst()) { 
				mGameInfo.moves = gameInfoCursor.getInt(HistoryContract.HistoryColumns.IDX_NUMBEROFMOVES);
				mGameInfo.boardSize = gameInfoCursor.getInt(HistoryContract.HistoryColumns.IDX_SIZE);
				mBoard = new GameBoard(mGameInfo.boardSize);
				gameInfoCursor.close();
			}
		} else {
			mBoard = (GameBoard)savedInstanceState.get(STATE_BOARD);
			long gameId = savedInstanceState.getLong(GAME_ID);
			mGameInfo = new GameInfo(gameId);
			mGameInfo.moves = savedInstanceState.getInt(GAME_MOVES);
			mGameInfo.boardSize = savedInstanceState.getInt(BOARD_SIZE);
			mCurrentPos = savedInstanceState.getInt(POSITION);
		}
		
		// Create an empty adapter we will use to display the loaded data.
		mCursor = null;
		
		BoardAdapter adapter = new BoardAdapter(mBoard, false);
		adapter.setClickEnabled(false);
		mBoardView.setAdapter(adapter);

		mBoardView.setVisibility(View.GONE);
		mScoreAdapter.init(mBoard);

		historyProvider.getMoves(mGameInfo.id);
		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.activity_replay, container, false);
		mBoardView = (BoardView)layout.findViewById(R.id.board);

		ImageButton buttonPlay = (ImageButton)layout.findViewById(R.id.buttonPlay);
		ImageButton buttonNext = (ImageButton)layout.findViewById(R.id.buttonNext);
		ImageButton buttonPrev = (ImageButton)layout.findViewById(R.id.buttonPrev);
		
		buttonPlay.setOnClickListener(this);
		buttonNext.setOnClickListener(this);
		buttonPrev.setOnClickListener(this);
		
		mScoreAdapter = new ScoreViewAdapter();
		mScoreAdapter.setupWidgets(
			(TextView)layout.findViewById(R.id.scoreWhite),
			(TextView)layout.findViewById(R.id.scoreBlack),
			(ImageButton)layout.findViewById(R.id.playerWhite),
			(ImageButton)layout.findViewById(R.id.playerBlack)
		);
        return layout;
    }
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(STATE_BOARD, mBoard);
		outState.putLong(GAME_ID, mGameInfo.id);
		outState.putInt(GAME_MOVES, mGameInfo.moves);
		outState.putInt(BOARD_SIZE, mGameInfo.boardSize);
		outState.putInt(POSITION, mCurrentPos);
		super.onSaveInstanceState(outState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = HistoryContract.GameColumns._ID + " ASC";
        Uri url = HistoryContract.Game.buildGameUri(mGameInfo.id);
		return new CursorLoader(getActivity(), url, null, null, null, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
		mCursor = data;
		mCursor.moveToPosition(mCurrentPos-1);
		DatabaseUtils.dumpCursor(mCursor);
		mBoardView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
		mCursor = null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.buttonPlay) {
			play();
		} else if (id == R.id.buttonNext) {
			nextMove();
		} else if (id == R.id.buttonPrev) {
			prevMove();
		}
	}

	private void play() {
		// TODO Auto-generated method stub
		
	}

	private void prevMove() {
		if (mCursor != null) {
			if (mCurrentPos > 0 ) {
			Cell cell = readCurrentCell();
			mBoard.undoMove();
			mCursor.moveToPrevious();
			mCurrentPos--;
			}
		}
	}

	private void nextMove() {
		if (mCursor != null && mCursor.moveToNext()) {
			mCurrentPos++;
			Cell cell = readCurrentCell();
			mBoard.move(cell);
		}
	}

	private Cell readCurrentCell() {
		int x = mCursor.getInt(HistoryContract.GameColumns.IDX_X);
		int y = mCursor.getInt(HistoryContract.GameColumns.IDX_Y);
		Cell cell = new Cell(x, y);
		return cell;
	}
	
}
