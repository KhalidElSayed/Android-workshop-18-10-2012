package com.emilsjolander.androidworkshopexampleapp;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TweetContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.emilsjolander.androidworkshopexampleapp.TweetContentProvider";
	private static final String DATABASE_NAME = "tweets.db";
	private static final int DATABASE_VERSION = 1;

	private static final int TWEET = 1;

	private static final UriMatcher uriMatcher;
	private static final HashMap<String, String> tweetProjectionMap;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Tweet.CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(Tweet.DROP);
			onCreate(db);
		}
	}

	private DatabaseHelper dbHelper;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, Tweet.TABLE_NAME, TWEET);

		tweetProjectionMap = Tweet.getProjectionMap();
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = -1;
		switch (uriMatcher.match(uri)) {    
		case TWEET:
			rowId = db.insert(Tweet.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (rowId > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
			Uri insertedUri = ContentUris.withAppendedId(uri, rowId);
			return insertedUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (uriMatcher.match(uri)) {    
		case TWEET:
			qb.setTables(Tweet.TABLE_NAME);
			qb.setProjectionMap(tweetProjectionMap);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
