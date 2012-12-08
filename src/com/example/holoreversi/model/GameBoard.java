package com.example.holoreversi.model;

import java.util.ArrayList;

public class GameBoard implements Board {

	private int scoreWhite;
	private int scoreBlack;
	private int boardSize;
	private Cell tiles[][] = null;
	@Override
	public void moveWhite(int x, int y) {
		if(move(x,y,1))
			calculateScore();
		else
		{ 
			// error occured
		}
	}

	@Override
	public void moveBlack(int x, int y) {
		if(move(x,y,2))
			calculateScore();
		else
		{ 
			// error occured
		}
	}

	private void calculateScore() {
		scoreBlack = 0;
		scoreWhite = 0;
		for (Cell[] c : tiles) {
			for (Cell single : c) {
				switch (single.contents) {
				case 2:
					scoreBlack++;
					break;
				case 1:
					scoreWhite++;
					break;
				default:
					break;
				}
			}
		}
	}
	private Cell[] getEmpty()
	{
		ArrayList<Cell> arr = new ArrayList<Cell>();
		for (Cell[] c : tiles) {
			for (Cell single : c) {
				if(single.contents == 0)
					arr.add(single);
			}
		}
		return (Cell[]) arr.toArray();
	}
	@Override
	public Cell[] getAllowedMovesWhite() {
		Cell[] emptyCells = getEmpty();
		return null;
	}

	@Override
	public Cell[] getAllowedMovesBlack() {
		Cell[] emptyCells = getEmpty();
		return null;
	}

	@Override
	public void addCallbackListener(Callback callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSize() {
		return boardSize;
	}

	@Override
	public int getScoreWhite() {
		return scoreWhite;
	}

	@Override
	public int getScoreBlack() {
		return scoreBlack;
	}
	
	private boolean move(int x,int y, int player)
	{
		return true;
	}
}
