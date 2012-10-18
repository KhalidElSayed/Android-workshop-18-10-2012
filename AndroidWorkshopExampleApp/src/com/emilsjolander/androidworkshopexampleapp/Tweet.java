package com.emilsjolander.androidworkshopexampleapp;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class Tweet {

	public static final String TABLE_NAME = "tweettable";
	
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + TweetContentProvider.AUTHORITY + "/"+ TABLE_NAME);

    public static final String TEXT = "text";

    public static final String CREATED_AT = "created_at";

    public static final String FROM_USER = "from_user";

    public static final String ID = "id";

    public static final String PROFILE_IMAGE_URL = "profile_image_url";

	public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
			TEXT + " text, " +
			CREATED_AT + " text, " +
			FROM_USER + " text, " +
			ID + " text, " +
			PROFILE_IMAGE_URL + " text" +
			");";

	public static final String DROP = "DROP TABLE IF EXISTS "+TABLE_NAME;
	
	public static HashMap<String, String> getProjectionMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TEXT, TEXT);
		map.put(CREATED_AT, CREATED_AT);
		map.put(FROM_USER, FROM_USER);
		map.put(ID, ID);
		map.put(PROFILE_IMAGE_URL, PROFILE_IMAGE_URL);
		return map;
	}
	
	private String text;
	private String createdAt;
	private String fromUser;
	private String id;
	private String profileImageUrl;
	
	public String getText() {
		return text;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}

	public String getFromUser() {
		return fromUser;
	}

	public String getId() {
		return id;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static Tweet fromJson(JSONObject object) {
		Tweet t = new Tweet();
		try {
			t.text = object.getString("text");
			t.createdAt = object.getString("created_at");
			t.fromUser = object.getString("from_user");
			t.id = object.getString("id_str");
			t.profileImageUrl = object.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return t;
	}

	public ContentValues getContentProviderValues() {
		ContentValues values = new ContentValues();
		values.put(TEXT, text);
		values.put(CREATED_AT, createdAt);
		values.put(FROM_USER, fromUser);
		values.put(ID, id);
		values.put(PROFILE_IMAGE_URL, profileImageUrl);
		return values;
	}
	
	public static Tweet fromCursor(Cursor c){
		Tweet t = new Tweet();
		t.text = c.getString(c.getColumnIndexOrThrow(TEXT));
		t.createdAt = c.getString(c.getColumnIndexOrThrow(CREATED_AT));
		t.fromUser = c.getString(c.getColumnIndexOrThrow(FROM_USER));
		t.id = c.getString(c.getColumnIndexOrThrow(ID));
		t.profileImageUrl = c.getString(c.getColumnIndexOrThrow(PROFILE_IMAGE_URL));
		return t;
	}

}
