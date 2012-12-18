package com.example.holoreversi.model;

import java.util.ArrayList;

public class AIPlayer implements Player {
	private Board mBoard;

	public AIPlayer(Board board) {
		mBoard = board;
	}

	/* (non-Javadoc)
	 * @see com.example.holoreversi.model.Player#play()
	 */
	@Override
	public Cell play() {
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
