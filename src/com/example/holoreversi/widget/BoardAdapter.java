package com.example.holoreversi.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.holoreversi.R;
import com.example.holoreversi.model.Board;

public class BoardAdapter implements Board.Callback, OnClickListener  {
	final private Board mBoard;
	final private Context mContext;
	final private TableLayout mBoardView;
	
	public BoardAdapter(TableLayout boardView, Context context, Board board) {
		mBoard = board;
		mContext = context;
		mBoardView = boardView;
		mBoard.setCallback(this);
	}
	
	public void init() {
		initBoardView(mBoard.getSize());
	}
	
	@Override
	public void onBoardUpdate(Board board) {
		
	}

	@Override
	public void onClick(View v) {
		
	}

	private void initBoardView(int size) {
		TableRow tr;
		final LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//add header
		tr = createHintRow(size, li);
		mBoardView.addView(tr);
		
		int middle = (int)(size / 2) - 1;

		
		for(int i=0; i<size; i++) {
			tr = (TableRow)li.inflate(R.layout.board_row, null);
			for(int j=0; j<size + 2; j++) {
				if (j == 0 || j == size+1) {
					final TextView label = (TextView)li.inflate(R.layout.board_view_pos, null);
					label.setText(""+i);
					tr.addView(label);
				} else {
					final ImageButton btn = (ImageButton)li.inflate(R.layout.board_view_btn, null);
					btn.setOnClickListener(this);
					// TODO: move to model?
					if (middle == i && middle+1 == j) {
						btn.setImageResource(R.drawable.blue);
					} else if (middle == i && middle+2 == j) {
						btn.setImageResource(R.drawable.white);
					} else if (middle+1 == i && middle+1 == j) {
						btn.setImageResource(R.drawable.white);
					} else if (middle+1 == i && middle+2 == j) {
						btn.setImageResource(R.drawable.blue);
					}
					tr.addView(btn);
				}
			}
			mBoardView.addView(tr);
		}

		tr = createHintRow(size, li);
		mBoardView.addView(tr);

		
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
}
