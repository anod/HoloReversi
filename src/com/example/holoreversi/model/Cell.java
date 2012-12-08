package com.example.holoreversi.model;

public class Cell {
	public int x;
	public int y;
	
	public int contents;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.contents = 0; // represents empty cell;
	}
}
