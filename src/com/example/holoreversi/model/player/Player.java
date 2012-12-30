package com.example.holoreversi.model.player;

import com.example.holoreversi.model.Cell;

public interface Player {

	public interface OnPlayListener {
		void OnPlay(Cell cell);
	}
	
	public void setOnPlayeListener(OnPlayListener listener);
	public void play();

}