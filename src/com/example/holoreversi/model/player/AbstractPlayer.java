package com.example.holoreversi.model.player;

import com.example.holoreversi.model.Cell;

public abstract class AbstractPlayer implements Player {
	private OnPlayListener mOnPlayListener;
	
	@Override
	public void setOnPlayeListener(OnPlayListener listener) {
		mOnPlayListener = listener;
	}

	protected void notify(Cell cell) {
		if (mOnPlayListener != null) {
			mOnPlayListener.OnPlay(cell);
		}
	}

}
