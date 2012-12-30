package com.example.holoreversi.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.example.holoreversi.model.history.HistoryProviderHelper;


public class HistoryCursorLoader extends AsyncTaskLoader<Cursor> {
	final ForceLoadContentObserver mObserver;

	Cursor mCursor;

	/**
	 * Creates an empty unspecified CursorLoader. You must follow this with
	 * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc to
	 * specify the query to perform.
	 */
	public HistoryCursorLoader(Context context) {
		super(context);
		mObserver = new ForceLoadContentObserver();
	}

	/* Runs on a worker thread */
	@Override
	public Cursor loadInBackground() {
		try {
			HistoryProviderHelper dataStore = new HistoryProviderHelper(getContext());
			Cursor cursor = dataStore.getGames();
			if (cursor != null) {
				// Ensure the cursor window is filled
				cursor.getCount();
				registerContentObserver(cursor, mObserver);
			}
			return cursor;
		} finally {
		}
	}

	/**
	 * Registers an observer to get notifications from the content provider
	 * when the cursor needs to be refreshed.
	 */
	void registerContentObserver(Cursor cursor, ContentObserver observer) {
		cursor.registerContentObserver(mObserver);
	}

	/* Runs on the UI thread */
	@Override
	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			if (cursor != null) {
				cursor.close();
			}
			return;
		}
		Cursor oldCursor = mCursor;
		mCursor = cursor;

		if (isStarted()) {
			super.deliverResult(cursor);
		}

		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	/**
	 * Starts an asynchronous load of the contacts list data. When the
	 * result is ready the callbacks will be called on the UI thread. If a
	 * previous load has been completed and is still valid the result may be
	 * passed to the callbacks immediately.
	 * 
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStartLoading() {
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	/**
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		mCursor = null;
	}

}