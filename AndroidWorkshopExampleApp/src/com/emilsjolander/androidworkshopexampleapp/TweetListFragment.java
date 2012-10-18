package com.emilsjolander.androidworkshopexampleapp;

import android.animation.LayoutTransition;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class TweetListFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	
	public interface Callback{
		void tweetClicked(Tweet t);
	}

	private Callback callback;

	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new TweetAdapter(getActivity(), null));
		getListView().setLayoutTransition(new LayoutTransition());
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(callback != null){
			Cursor c = ((TweetAdapter)getListAdapter()).getCursor();
			c.moveToPosition(position);
			callback.tweetClicked(Tweet.fromCursor(c));
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), Tweet.CONTENT_URI, null, null, null, Tweet.ID + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		((TweetAdapter)getListAdapter()).setCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		((TweetAdapter)getListAdapter()).setCursor(null);
	}

}
