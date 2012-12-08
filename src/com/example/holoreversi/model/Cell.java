package com.example.holoreversi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cell implements Parcelable{
	public int x;
	public int y;
	
	public int contents;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.contents = Board.EMPTY; // represents empty cell;
	}

	public Cell(Parcel in) {
		x = in.readInt();
		y = in.readInt();
		contents = in.readInt();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(contents);
	}
}
