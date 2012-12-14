package com.example.holoreversi.widget;

import java.util.ArrayList;

import com.example.holoreversi.model.Board;
import com.example.holoreversi.model.Cell;

public interface DataStore {
	
	interface Callback {
		void onBoardUpdate(Board board);
	}
	boolean isOpen();
	boolean open();
	boolean insert(ArrayList<Cell> cell);
	ArrayList<Cell> get();
	
}
