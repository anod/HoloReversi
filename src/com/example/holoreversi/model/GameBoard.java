package com.example.holoreversi.model;

import java.util.ArrayList;

//import com.example.holoreversiEngine.move;
//import com.example.holoreversiEngine.Board.TKind;

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
		return getAllowedCells(1);
	}

	@Override
	public Cell[] getAllowedMovesBlack() {
		return getAllowedCells(2);
	}
	private Cell[] getAllowedCells(int kind)
	{
		ArrayList<Cell> arr = new ArrayList<Cell>();
		Cell[] emptyCells = getEmpty();
		for (Cell cell : emptyCells) {
			if(isValid(cell, 1))
				arr.add(cell);
		}
		return (Cell[]) arr.toArray();
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
	private int checkCell(int x,int y, int incx, int incy, int kind , boolean set)  {
		// totally based with limited understanding on reversi.java.net
		int opponent;
		if (kind == 2) opponent=1; else opponent=2;
		int n_inc=0;
		x+=incx; y+=incy;
		while ((x<boardSize) && (x>=0) && (y<boardSize) && (y>=0) && (tiles[x][y].contents==opponent)) {
			x+=incx; y+=incy;
			n_inc++;
		}
		if ((n_inc != 0) && (x<boardSize) && (x>=0) && (y<boardSize) && (y>=0) && (tiles[x][y].contents==kind)) {
			 if (set)
			 for (int j = 1 ; j <= n_inc ; j++) {
				x-=incx; y-=incy;
				 //set(new cell.x,cell.y(x,y),kind);
			 }
			return n_inc;
		}
		else return 0;
	}
	private boolean isValid(Cell cell, int kind) {
		// check increasing x 
		if (checkCell(cell.x,cell.y,1,0,kind,false) != 0) return true;
		// check decreasing x 
		if (checkCell(cell.x,cell.y,-1,0,kind,false) != 0) return true;
		// check increasing y 
		if (checkCell(cell.x,cell.y,0,1,kind,false) != 0) return true;
		// check decreasing y 
		if (checkCell(cell.x,cell.y,0,-1,kind,false) != 0) return true;
		// check diagonals 
		if (checkCell(cell.x,cell.y,1,1,kind,false) != 0) return true;
		if (checkCell(cell.x,cell.y,-1,1,kind,false) != 0) return true;
		if (checkCell(cell.x,cell.y,1,-1,kind,false) != 0) return true;
		if (checkCell(cell.x,cell.y,-1,-1,kind,false) != 0) return true;
		return false;
	}
	private boolean move(int x,int y, int player)
	{
		return true;
	}
}
