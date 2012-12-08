package com.example.holoreversi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

public class BoardView extends TableLayout {
	private BoardAdapter mAdapter;
	
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BoardView(Context context) {
		super(context);
	}

	public void setAdapter(BoardAdapter adapter) {
		mAdapter = adapter;
		mAdapter.setContext(getContext());
		mAdapter.setBoardView(this);
		mAdapter.init();
	}
}
