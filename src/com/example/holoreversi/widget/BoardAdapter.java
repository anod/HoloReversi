package com.example.holoreversi.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.holoreversi.R;
import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;

public class BoardAdapter implements Board.Callback, OnClickListener  {
	final private Board mBoard;
	private Context mContext;
	private BoardView mBoardView;
	private Drawable mDrawableWhite;
	private Drawable mDrawableBlack;
	private Drawable mDrawableEmpty;
	
	public BoardAdapter(Board board) {
		mBoard = board;
		mBoard.addCallbackListener(this);
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public void setBoardView(BoardView boardView) {
		mBoardView = boardView;
	}

	public void init() {
		Resources r = mContext.getResources();
		mDrawableWhite = r.getDrawable(R.drawable.white);
		mDrawableBlack = r.getDrawable(R.drawable.blue);
		mDrawableEmpty = r.getDrawable(R.drawable.empty);

		initBoardView(mBoard.getSize());
		setInitialState(mBoard.getSize(),mBoard.getAll());
	}

	private void setInitialState(int size, Cell[][] all) {
		for (int i=0; i< size; i++) {
			for(int j=0; j< size; j++) {
				drawState(all[i][j]);
			}
		}
		
	}

	private void drawState(Cell cell) {
		TableRow row = (TableRow)mBoardView.getChildAt(cell.y + 1);
		ImageButton btn = (ImageButton)row.getChildAt(cell.x + 1);
		switch (cell.contents) {
		case Board.WHITE:
			btn.setImageDrawable(mDrawableWhite);
			break;
		case Board.BLACK:
			btn.setImageDrawable(mDrawableBlack);
			break;
		default:
			btn.setImageDrawable(mDrawableEmpty);
			break;
		}
	}

	private void initBoardView(int size) {
		TableRow tr;
		final LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//add header
		tr = createHintRow(size, li);
		mBoardView.addView(tr);
		
		for(int i=0; i<size; i++) {
			tr = (TableRow)li.inflate(R.layout.board_row, null);
			for(int j=0; j<size + 2; j++) {
				if (j == 0 || j == size+1) {
					final TextView label = (TextView)li.inflate(R.layout.board_view_pos, null);
					label.setText(""+(i+1));
					tr.addView(label);
				} else {
					final ImageButton btn = (ImageButton)li.inflate(R.layout.board_view_btn, null);
					btn.setTag(new Cell(j-1, i));
					btn.setOnClickListener(this);
					tr.addView(btn);
				}
			}
			mBoardView.addView(tr);
		}

		for(int i=1; i<=size; i++) {
			mBoardView.setColumnStretchable(i, true);
		}
		
		tr = createHintRow(size, li);
		mBoardView.addView(tr);

		
	}
	

	@Override
	public void onClick(View v) {
		Cell cell = (Cell)v.getTag();
		mBoard.move(cell);
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
	public void onBoardUpdate(Board board, Cell cell, int newState) {

	}
}
