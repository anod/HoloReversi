package com.example.holoreversi.model.history;

import android.net.Uri;
import android.provider.BaseColumns;

public class HistoryContract {
    public static final String CONTENT_AUTHORITY = "com.example.holoreversi";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    
    private static final String PATH_HISOTRY = "history";
    private static final String PATH_GAMES = "game";
    		
	static final String MOVES_TABLE_NAME = "HISTORY_MOVES";
	static final String GAMES_TABLE_NAME = "HISTORY_GAMES";

    interface Tables {
    	String HISTORY = "history";
    	String GAME = "game";
    }
	
	public interface HistoryColumns extends BaseColumns {
		static final String COLUMN_NAME_TIME = "STARTTIME";
		static final String COLUMN_NAME_SCORE1 = "SCOREBLACK";
		static final String COLUMN_NAME_SCORE2 = "SCOREWHITE";
		static final String COLUMN_NAME_NUMBEROFMOVES = "NUMBEROFMOVES";

		static final int IDX_TIME = 1;
		static final int IDX_NUMBEROFMOVES = 2;
		static final int IDX_SCOREBLACK = 3;
		static final int IDX_SCOREWHITE = 4;
	}

	public interface GameColumns extends BaseColumns {
		static final String COLUMN_NAME_X = "X";
		static final String COLUMN_NAME_Y = "Y";
		static final String COLUMN_NAME_REL = "GAMEID";
		static final String COLUMN_NAME_KIND = "COLOR";
	}
	
	public static class History implements HistoryColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_HISOTRY).build();

		public static final String DEFAULT_SORT = HistoryColumns._ID + " DESC ";
		public static final String DEFAULT_SELECTION = HistoryColumns.COLUMN_NAME_NUMBEROFMOVES + ">0";

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.holoreversi.history";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.holoreversi.history";
        
		public static String buildHistoryIdSelection(String gameId) {
			return HistoryColumns._ID+"="+gameId;
		}
		
		public static Uri buildHistoryUri(long gameId) {
			return CONTENT_URI.buildUpon().appendPath(Long.toString(gameId)).build();
		}
	}
	
	public static class Game implements GameColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).build();
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.holoreversi.game";
        
		public static Uri buildGameUri(long gameId) {
			return CONTENT_URI.buildUpon().appendPath(Long.toString(gameId)).build();
		}

		public static CharSequence buildGameIdSelection(String gameId) {
			return GameColumns._ID+"="+gameId;
		}

	}
	
	
	private HistoryContract() {
	}
}