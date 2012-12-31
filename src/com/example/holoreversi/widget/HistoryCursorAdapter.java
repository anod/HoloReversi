package com.example.holoreversi.widget;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.holoreversi.R;
import com.example.holoreversi.model.history.HistoryContract;

public class HistoryCursorAdapter extends SimpleCursorAdapter {


	public HistoryCursorAdapter(Context context) {
		super(context,
			R.layout.history_list_item, null,
			new String[] { 
				HistoryContract.HistoryColumns.TIME,
				HistoryContract.HistoryColumns.NUMBEROFMOVES,
				HistoryContract.HistoryColumns.SCORE1,
				HistoryContract.HistoryColumns.SCORE2,
				HistoryContract.HistoryColumns.SIZE
			},
			new int[] { 
				android.R.id.text1, 
				android.R.id.text2,
				R.id.text3,
				R.id.text4,
				android.R.id.icon
			}, 0
		);
		setViewBinder(new RowViewBinder(context));
		
	}

	
	static class RowViewBinder implements SimpleCursorAdapter.ViewBinder {

		private Context mContext;

		public RowViewBinder(Context context) {
			mContext = context;
		}
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			
			if (columnIndex == HistoryContract.HistoryColumns.IDX_TIME) {
				String dateTime = DateUtils.formatDateTime(mContext, cursor.getLong(columnIndex),
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
				);
				((TextView)view).setText(dateTime);
			} else if (columnIndex == HistoryContract.HistoryColumns.IDX_NUMBEROFMOVES) {
				((TextView)view).setText(mContext.getString(R.string.moves_num, cursor.getInt(columnIndex)));
			} else if (columnIndex == HistoryContract.HistoryColumns.IDX_SCOREBLACK) {
				((TextView)view).setText(mContext.getString(R.string.scoreBlack, cursor.getInt(columnIndex)));
			} else if (columnIndex == HistoryContract.HistoryColumns.IDX_SCOREWHITE) {
				((TextView)view).setText(mContext.getString(R.string.scoreWhite, cursor.getInt(columnIndex)));
			} else if (columnIndex == HistoryContract.HistoryColumns.IDX_SIZE) {
				int boardSize = cursor.getInt(columnIndex);
				((ImageView)view).setImageResource(getBoardIcon(boardSize));
			}
			return true;
		}
		
		private int getBoardIcon(int boardSize) {
			if (boardSize == 4){
				return R.drawable.ic_board_4;
			}
			if (boardSize == 6){
				return R.drawable.ic_board_6;
			}
			return R.drawable.ic_board_8;
		}
	}

}
