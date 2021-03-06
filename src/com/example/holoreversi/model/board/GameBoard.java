package com.example.holoreversi.model.board;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.holoreversi.model.Cell;

public class GameBoard implements Board,Parcelable {

	private int scoreWhite;
	private int scoreBlack;
	private int boardSize;
	private Cell tiles[][] = null;
	private ArrayList<ArrayList<Cell>> stepChanges;
	private ArrayList<Cell> currentStep;
	//private ArrayList<Cell> stepChangesBlack;
	//private ArrayList<Cell> stepChangesWhite;
	private ArrayList<Callback> listenrs;
	private int step;

	public GameBoard(int size)
	{
		//stepChangesBlack = new ArrayList<Cell>();
		//stepChangesWhite = new ArrayList<Cell>();
		stepChanges = new ArrayList<ArrayList<Cell>>();
		listenrs = new ArrayList<Board.Callback>();
		boardSize = size;
		resetBoardInternal(false);
	}
	
    public GameBoard(Parcel in) {
		//stepChanges = new ArrayList<Cell>();
		listenrs = new ArrayList<Board.Callback>();
    	boardSize = in.readInt();
    	tiles = new Cell[boardSize][boardSize];
		for (int i=0;i<boardSize;i++) {
			for (int j=0;j<boardSize;j++) {
				tiles[i][j] = new Cell(i, j);
				tiles[i][j].contents = in.readInt();
				// it might be more correct to write and read entire arrays but i;m not sure how to test it
				
			}
		}
		step = in.readInt();
		//int size = in.readInt();
		//stepChanges = new ArrayList<Cell>();
		//for(int i=0;i<size;i++)
		//{
		//	stepChanges.add(new Cell(in));
		//}
		calculateScore();
		stepChanges = new ArrayList<ArrayList<Cell>>();
	}

	public static final Parcelable.Creator<GameBoard> CREATOR
    	= new Parcelable.Creator<GameBoard>() {
    		public GameBoard createFromParcel(Parcel in) {
    			return new GameBoard(in);
    			}
    		public GameBoard[] newArray(int size) {
    			return null; // i think it is correct since there can only be one board
    		}
    };
    

	@Override
	public boolean hasUndo() {
		if(step == 0)
			return false;
		//if(currentPlayer() == BLACK && stepChangesBlack.size()== 0)
		//	return false; // means either the black did undo so it is now the white turn and he still has one undo left.
		return true;
	}
	
	
	@Override
    public void resetBoard()
    {
		resetBoardInternal(true);
    }
	
	
    
	private void resetBoardInternal(boolean notify) {
		//stepChangesBlack.clear();
		//stepChangesWhite.clear();
		stepChanges.clear();
		currentStep = null;
    	tiles = new Cell[boardSize][boardSize];
		for (int i=0;i<boardSize;i++) {
			for (int j=0;j<boardSize;j++) {
				tiles[i][j] = new Cell(i, j);
			}
		}
		tiles[boardSize/2-1][boardSize/2].contents=BLACK;
		tiles[boardSize/2][boardSize/2-1].contents=BLACK;
		tiles[boardSize/2-1][boardSize/2-1].contents=WHITE;
		tiles[boardSize/2][boardSize/2].contents=WHITE;
		calculateScore();
		step = 0;

		if (notify) {
			notifyCellUpdate();
		}
	}

	private void calculateScore() {
		scoreBlack = 0;
		scoreWhite = 0;
		for (Cell[] c : tiles) {
			for (Cell single : c) {
				switch (single.contents) {
				case BLACK:
					scoreBlack++;
					break;
				case WHITE:
					scoreWhite++;
					break;
				default:
					break;
				}
			}
		}
	}
	private ArrayList<Cell> getEmpty()
	{
		ArrayList<Cell> arr = new ArrayList<Cell>();
		for (Cell[] c : tiles) {
			for (Cell single : c) {
				if(single.contents == EMPTY)
					arr.add(single);
			}
		}
		return arr;
	}
	@Override
	public ArrayList<Cell> getAllowedMoves() {
		int kind = currentPlayer();
		if (kind == BLACK) {
			return getAllowedCells(BLACK);
		}
		return getAllowedCells(WHITE);
	}

	private ArrayList<Cell> getAllowedCells(int kind)
	{
		ArrayList<Cell> arr = new ArrayList<Cell>();
		ArrayList<Cell> emptyCells = getEmpty();
		for (Cell cell : emptyCells) {
			if(isValid(cell, kind))
				arr.add(cell);
		}
		return arr;
	}
	
	@Override
	public void addCallbackListener(Callback callback) {
		listenrs.add(callback);
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
		if (kind == BLACK) {
			opponent=WHITE;
		} else {
			opponent=BLACK;
		}
		int n_inc=0;
		x+=incx; y+=incy;
		while ((x<boardSize) && (x>=0) && (y<boardSize) && (y>=0) && (tiles[x][y].contents==opponent)) {
			x+=incx; y+=incy;
			n_inc++;
		}
		if ((n_inc != 0) && (x<boardSize) && (x>=0) && (y<boardSize) && (y>=0) && (tiles[x][y].contents==kind)) {
			 if (set) {
				 for (int j = 1 ; j <= n_inc ; j++) {
					x-=incx; y-=incy;
					updateTile(x, y,kind);
				 }
			 }
			return n_inc;
		}
		return 0;
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

	/**
	 * 
	 * @param cell
	 * @return number of changed tiles
	 */
	@Override
	public int checkNextMove(Cell cell) {
		return checkNextMove(cell, false);
	}
	
	private int checkNextMove(Cell cell, boolean change) {
		int kind = currentPlayer();
		int x = cell.x;
		int y = cell.y;
		// check increasing x
		int j=checkCell(x,y, 1,0,kind,change);
		// check decreasing x
		j+=checkCell(x,y, -1,0,kind,change);
		// check increasing y
		j+=checkCell(x,y, 0,1,kind,change);
		// check decreasing y
		j+=checkCell(x,y, 0,-1,kind,change);
		// check diagonals
		j+=checkCell(x,y, 1,1,kind,change);
		j+=checkCell(x,y, -1,1,kind,change);
		j+=checkCell(x,y, 1,-1,kind,change);
		j+=checkCell(x,y, -1,-1,kind,change);
		return j;
	}
	
	
	private int doMove(Cell cell)
	{
		int j = checkNextMove(cell, true);
		if (j != 0)  {
			int kind = currentPlayer();
			updateTile(cell.x, cell.y, kind);
		}
		return j;
	}
	
	public boolean undoMove()
	{	
		if (!hasUndo()) {
			return false;
		}
		step--; // because current player has already been changed
		// undoing a step
		ArrayList<Cell> curr = stepChanges.remove(step);
		for(Cell cell : curr)
		{
			int kind = tiles[cell.x][cell.y].contents;
			notifyCellUndo(cell, kind);
			tiles[cell.x][cell.y].contents = cell.contents;
		}
/*		if(currentPlayer() == BLACK)
		{
			for (Cell cell : stepChangesBlack) {
				int kind = tiles[cell.x][cell.y].contents;
				notifyCellUndo(cell, kind);
				tiles[cell.x][cell.y].contents = cell.contents; 
			}
			stepChangesBlack.clear();
		}
		else
		{
			for (Cell cell : stepChangesWhite) {
				int kind = tiles[cell.x][cell.y].contents;
				notifyCellUndo(cell, kind);
				tiles[cell.x][cell.y].contents = cell.contents; 
			}
			stepChangesWhite.clear();
		}
*/
		calculateScore();
		notifyCellUpdate();
		if(curr.size() == 0)
			undoMove();
		return true;
	}
	
	private void notifyCellUndo(Cell cell, int kind) {
		for (Callback callback : listenrs) {
			callback.onCellUndo(cell, kind);
		}
	}

	@Override
	public int describeContents() {
		// not so sure what should be in here
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(boardSize);
		for (Cell[] row : tiles) {
			for(Cell cell : row) {
				dest.writeInt(cell.contents);
			}
		}
		dest.writeInt(step);
		//dest.writeInt(stepChanges.size());
		//for (Cell cell : stepChanges) {
		//	cell.writeToParcel(dest, flags);
		//}
	}
	
	@Override
	public int currentPlayer()
	{
		if(step % 2 == 0)
			return BLACK;
		return WHITE;
	}

	@Override
	public Cell[][] getAll() {
		return tiles;
	}

	@Override
	public boolean move(Cell cell) {
		if(cell.contents != EMPTY)
			return false;
		currentStep = new ArrayList<Cell>();
		/*
		if(currentPlayer() == BLACK)
		{
			stepChangesBlack.clear();
		}
		else
		{
			stepChangesWhite.clear();
		}
		*/
		int changed = doMove(cell);
		if (changed == 0) {
			return false;
		}
		calculateScore();
		stepChanges.add(currentStep);
		step++;
		currentStep = null;
		if(getAllowedMoves().size() == 0) {
			step++;
			stepChanges.add(new ArrayList<Cell>());
		}
		if (isGameEnded()) {
			notifyGameEnd();
		}
		
		notifyCellUpdate();
		notifyNextPlayer();
		return true;
	}
	

	private void updateTile(int x, int y,int kind)
	{
		Cell temp = new Cell(x, y);
		temp.contents = tiles[x][y].contents;
		/*
		if(kind == BLACK)
			stepChangesBlack.add(temp);
		else
			stepChangesWhite.add(temp);
		*/
		currentStep.add(temp);
		tiles[x][y].contents = kind;
	}
	
	private void notifyCellUpdate()
	{
		for (Callback callback : listenrs) {
			callback.onBoardUpdate(this);
		}
	}
	private void notifyNextPlayer() {
		int nextPlayer = currentPlayer();
		for (Callback callback : listenrs) {
			callback.onNextPlayer(nextPlayer);
		}
	}
	private void notifyGameEnd() {
		for (Callback callback : listenrs) {
			callback.onGameEnd(this);
		}
	}
	
	@Override
	public boolean isGameEnded()
	{
		if(scoreBlack+scoreWhite == boardSize*boardSize)
			return true;
		if(getAllowedMoves().size() == 0)
			return true;
		return false;
	}
	@Override
	public int winner()
	{
		if(!isGameEnded())
		{
			return -1;
		}
		if(scoreBlack > scoreWhite){
			return BLACK;
		}
		else if (scoreWhite > scoreBlack) {
			return WHITE;
		}
		return EMPTY;
	}

	public void undo(Cell cell) {
		// TODO Auto-generated method stub
		
	}
	

}
