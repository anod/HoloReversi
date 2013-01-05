package com.example.holoreversi.model.player;

import java.util.ArrayList;

import android.os.Handler;

import com.example.holoreversi.model.Cell;
import com.example.holoreversi.model.board.Board;

public class AIPlayer extends AbstractPlayer {
	private Board mBoard;

	public AIPlayer(Board board) {
		mBoard = board;
	}

	/* (non-Javadoc)
	 * @see com.example.holoreversi.model.Player#play()
	 */
	@Override
	public void play() {
		// SLEEP 2 SECONDS HERE ...
	    Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	Cell cell = playDelayed();
	        	AIPlayer.this.notify(cell);
	         }
	    }, 300); 

	}
	
	protected Cell playDelayed() {
		Cell ret = new Cell(0, 0);
		ArrayList<Cell> allowd = mBoard.getAllowedMoves();
		int min = 0;
		for (Cell cell : allowd) {
			int temp = mBoard.checkNextMove(cell);
			if (temp > min) {
				min = temp;
				ret.x = cell.x;
				ret.y = cell.y;
			}
		}
		if (min != 0) {
			mBoard.move(ret);
			return ret;
		}
		return null;
	}
}
