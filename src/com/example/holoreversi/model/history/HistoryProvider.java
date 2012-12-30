package com.example.holoreversi.model.history;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HistoryProvider extends ContentProvider {
	private static final int HISTORY = 1;
	private static final int HISTORY_ID = 2;
	private static final int GAME_ID = 10;

	/**
	 * 0-relative position ID segment in the path part of a ID URI
	 */
	public static final int GAME_ID_PATH_POSITION = 1;

	// Creates a UriMatcher object.
	private static final UriMatcher sUriMatcher;
	static {

		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(HistoryContract.CONTENT_AUTHORITY, "history", HISTORY);
		sUriMatcher.addURI(HistoryContract.CONTENT_AUTHORITY, "history/*", HISTORY_ID);
		sUriMatcher.addURI(HistoryContract.CONTENT_AUTHORITY, "game/*", GAME_ID);

	}

	private HistoryDatabase mOpenHelper;

	@Override
	public boolean onCreate() {
		mOpenHelper = new HistoryDatabase(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String gameId;
		String orderBy = null;
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case HISTORY: {
			qb.setTables(HistoryContract.Tables.HISTORY);
			qb.appendWhere(HistoryContract.History.DEFAULT_SELECTION);
			orderBy = HistoryContract.History.DEFAULT_SORT;
			break;
		}
		case HISTORY_ID: {
			qb.setTables(HistoryContract.Tables.HISTORY);
			gameId = uri.getPathSegments().get(GAME_ID_PATH_POSITION);
			qb.appendWhere(HistoryContract.History.buildHistoryIdSelection(gameId));
			break;
		}
		case GAME_ID: {
			qb.setTables(HistoryContract.Tables.GAME);
			gameId = uri.getPathSegments().get(GAME_ID_PATH_POSITION);
			qb.appendWhere(HistoryContract.Game.buildGameIdSelection(gameId));
			break;
		}
		default:
			// If the URI doesn't match any of the known patterns, throw an
			// exception.
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// If no sort order is specified, uses the default
		if (orderBy == null) {
			orderBy = sortOrder;
		}

		/*
		 * Performs the query. If no problems occur trying to read the database,
		 * then a Cursor object is returned; otherwise, the cursor variable
		 * contains null. If no records were selected, then the Cursor object is
		 * empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(db, // The database to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				orderBy // The sort order
		);

		// Tells the Cursor what URI to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case HISTORY: {
			long gameId = db.insertOrThrow(HistoryContract.Tables.HISTORY, null, values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return HistoryContract.History.buildHistoryUri(gameId);
		}
		case GAME_ID: {
			db.insertOrThrow(HistoryContract.Tables.GAME, null, values);
			getContext().getContentResolver().notifyChange(uri, null, false);
			return HistoryContract.Game.buildGameUri(values.getAsLong(HistoryContract.GameColumns.COLUMN_NAME_REL));
		}
		default: {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int retVal = 0;
		switch (match) {
		case HISTORY_ID: {
			retVal = db.update(HistoryContract.Tables.HISTORY, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null, false);
			break;
		}
		default: {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		}
		return retVal;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException("Unknown uri: " + uri);
	}

	@Override
	public String getType(Uri uri) {
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case HISTORY:
			return HistoryContract.History.CONTENT_TYPE;
		case HISTORY_ID:
			return HistoryContract.History.CONTENT_ITEM_TYPE;
		case GAME_ID:
			return HistoryContract.History.CONTENT_ITEM_TYPE;
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

}