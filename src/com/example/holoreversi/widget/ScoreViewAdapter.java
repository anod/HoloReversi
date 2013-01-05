package com.example.holoreversi.widget;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.board.Board;
import com.example.holoreversi.model.board.Board.Callback;

public class ScoreViewAdapter implements Callback {
	private TextView mScoreWhite;
	private TextView mScoreBlack;
	private ImageButton mPlayerWhite;
	private ImageButton mPlayerBlack;

	public void setupWidgets(TextView scoreWhite, TextView scoreBlack, ImageButton playerWhite, ImageButton playerBlack) {
		mScoreWhite = scoreWhite;
		mScoreBlack = scoreBlack;
		mPlayerWhite = playerWhite;
		mPlayerBlack = playerBlack;
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


	public void init(Board board) {
		setScoreView(board.getScoreBlack(),board.getScoreWhite());
		setPlayerView(board.currentPlayer());
		board.addCallbackListener(this);
	}


	@Override
	public void onNextPlayer(int nextPlayer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onBoardUpdate(Board board) {
		setPlayerView(board.currentPlayer());
		setScoreView(board.getScoreBlack(), board.getScoreWhite());
	}

	@Override
	public void onGameEnd(Board board) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCellUndo(Cell cell, int kind) {
		// TODO Auto-generated method stub
		
	}
}
