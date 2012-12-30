package com.example.holoreversi;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import com.example.holoreversi.model.history.DataStore;
import com.example.holoreversi.model.history.SQLiteDataStore;

public class HistoryActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
        	HistoryListFragment list = new HistoryListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
	}

	public static class HistoryListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
		// This is the Adapter being used to display the list's data.
		SimpleCursorAdapter mAdapter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Give some text to display if there is no data. In a real
			// application this would come from a resource.
			setEmptyText(getActivity().getString(R.string.no_games_in_history));

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			// Create an empty adapter we will use to display the loaded data.
			mAdapter = new SimpleCursorAdapter(getActivity(),
					android.R.layout.simple_list_item_2, null,
					new String[] { DataStore.COLUMN_NAME_TIME, DataStore.COLUMN_NAME_NUMBEROFMOVES },
					new int[] { android.R.id.text1, android.R.id.text2 }, 0
			);
			setListAdapter(mAdapter);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			return new GamesCursorLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// Swap the new cursor in.  (The framework will take care of closing the
	        // old cursor once we return.)
	        mAdapter.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
	        // This is called when the last Cursor provided to onLoadFinished()
	        // above is about to be closed.  We need to make sure we are no
	        // longer using it.
	        mAdapter.swapCursor(null);
		}

	}

	public static class GamesCursorLoader extends AsyncTaskLoader<Cursor> {
		final ForceLoadContentObserver mObserver;

		Cursor mCursor;

		/**
		 * Creates an empty unspecified CursorLoader. You must follow this with
		 * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc to
		 * specify the query to perform.
		 */
		public GamesCursorLoader(Context context) {
			super(context);
			mObserver = new ForceLoadContentObserver();
		}

		/* Runs on a worker thread */
		@Override
		public Cursor loadInBackground() {
			try {
				DataStore dataStore = new SQLiteDataStore(getContext());
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

}
