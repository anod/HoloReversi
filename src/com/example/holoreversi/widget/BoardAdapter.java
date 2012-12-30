package com.example.holoreversi.widget;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.holoreversi.R;
import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.player.AIPlayer;
import com.example.holoreversi.model.player.HumanPlayer;
import com.example.holoreversi.model.player.Player;

public class BoardAdapter implements Board.Callback, OnClickListener, Player.OnPlayListener  {
	final private Board mBoard;
	private Context mContext;
	private BoardView mBoardView;
	private Drawable mDrawableWhite;
	private Drawable mDrawableBlack;
	private Drawable mDrawableEmpty;
	private Drawable mDrawableAllowed;
	private Animation mCellAnim1;
	private Animation mCellAnim2;
	private Player mSecondPlayer;
	public BoardAdapter(Board board, boolean isComputerPlayer) {
		mBoard = board;
		mBoard.addCallbackListener(this);
		
		if (isComputerPlayer) {
			mSecondPlayer = new AIPlayer(board);
		} else {
			mSecondPlayer = new HumanPlayer();
		}
		mSecondPlayer.setOnPlayeListener(this);
		
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public void setBoardView(BoardView boardView) {
		mBoardView = boardView;
	}

	public void init() {
		initDrawables();
		
		initBoardView(mBoard.getSize());
		mCellAnim1 = AnimationUtils.loadAnimation(mContext, R.anim.board_cell);
		mCellAnim2 = AnimationUtils.loadAnimation(mContext, R.anim.board_cell);
		drawBoard(mBoard.getAll(), mBoard.getAllowedMoves());
	}

	private void initDrawables() {
		Resources r = mContext.getResources();
		mDrawableWhite = r.getDrawable(R.drawable.reversi_stone_white);
		mDrawableBlack = r.getDrawable(R.drawable.reversi_stone_blue);
		mDrawableEmpty = r.getDrawable(R.drawable.reversi_stone_empty);
		mDrawableAllowed = r.getDrawable(R.drawable.reversi_allowed_move);
	}

	private void drawBoard(Cell[][] all, ArrayList<Cell> allowed) {

		for (int i=0; i< all.length; i++) {
			for(int j=0; j< all.length; j++) {
				drawState(all[i][j]);
			}
		}
		for (int i=0; i<allowed.size(); i++) {
			drawAllowed(allowed.get(i));
		}
	}

	private void drawAllowed(Cell cell) {
		ImageButton btn = getImageButton(cell);
		btn.setImageDrawable(mDrawableAllowed);
	}

	private void drawState(Cell cell) {
		ImageButton btn = getImageButton(cell);
		Cell currentCell = (Cell)btn.getTag();
		//no need to update
		if (currentCell.contents != Board.EMPTY && currentCell.contents == cell.contents) {
			return;
		}
		
		switch (cell.contents) {
		case Board.WHITE:
			btn.setImageDrawable(mDrawableWhite);
			currentCell.contents = Board.WHITE;
			break;
		case Board.BLACK:
			btn.setImageDrawable(mDrawableBlack);
			currentCell.contents = Board.BLACK;
			break;
		default:
			btn.setImageDrawable(mDrawableEmpty);
			currentCell.contents = Board.EMPTY;
			break;
		}
	}

	private ImageButton getImageButton(Cell cell) {
		TableRow row = (TableRow)mBoardView.getChildAt(cell.y + 1);
		ImageButton btn = (ImageButton)row.getChildAt(cell.x + 1);
		return btn;
	}

	private void initBoardView(int size) {
		TableRow tr;
		final LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//add header
		tr = createHintRow(size, li);
		mBoardView.addView(tr);
		
		int minSize = getMinCellSize(size);
		
		for(int i=0; i<size; i++) {
			tr = (TableRow)li.inflate(R.layout.board_row, null);
			for(int j=0; j<size + 2; j++) {
				if (j == 0 || j == size+1) {
					final TextView label = (TextView)li.inflate(R.layout.board_view_pos, null);
					label.setText(""+(i+1));
					label.setMinimumHeight(minSize);
					tr.addView(label);
				} else {
					final ImageButton btn = (ImageButton)li.inflate(R.layout.board_view_btn, null);
					btn.setTag(new Cell(j-1, i));
					btn.setOnClickListener(this);
					btn.setMinimumHeight(minSize);
					btn.setMinimumWidth(minSize);
					tr.addView(btn);
				}
			}
			mBoardView.addView(tr);
		}

		tr = createHintRow(size, li);
		mBoardView.addView(tr);

		
	}

	private int getMinCellSize(int size) {
		int minSize = 0;
		if (size == 4) {
			minSize = mContext.getResources().getDimensionPixelSize(R.dimen.cell_size_4);
		} else if (size == 6) {
			minSize = mContext.getResources().getDimensionPixelSize(R.dimen.cell_size_6);
		} else if (size == 8) {
			minSize = mContext.getResources().getDimensionPixelSize(R.dimen.cell_size_8);
		}
		return minSize;
	}
	

	@Override
	public void onClick(View v) {
		Cell cell = (Cell)v.getTag();
		if (mBoard.move(cell)) {
			v.startAnimation(mCellAnim1);
		}
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

	@Override
	public void onBoardUpdate(Board board) {
		drawBoard(board.getAll(), board.getAllowedMoves());
	}

	@Override
	public void onNextPlayer(int nextPlayer) {
		if (nextPlayer == Board.WHITE) {
			mSecondPlayer.play();
		}
	}

	@Override
	public void OnPlay(Cell cell) {
		if (cell != null) {
			ImageButton btn = getImageButton(cell);
			btn.startAnimation(mCellAnim2);
		}
	}

	@Override
	public void onGameEnd(Board board) {
		// TODO Auto-generated method stub
		
	}
	
}
