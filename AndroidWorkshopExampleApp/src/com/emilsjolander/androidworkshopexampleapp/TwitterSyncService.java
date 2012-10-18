package com.emilsjolander.androidworkshopexampleapp;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class TwitterSyncService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			
			@Override
			protected Void doInBackground(Void... params) {
				loadTweets();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				stopSelf();
			}
		};
		task.execute();
	}
	
	private void loadTweets() {
		try {
		    HttpClient client = new DefaultHttpClient();  
		    HttpGet get = new HttpGet(getString(R.string.twitter_search_string));
		    HttpResponse responseGet = client.execute(get);  
		    HttpEntity resEntityGet = responseGet.getEntity();  
		    if (resEntityGet != null) {  
		        String response = EntityUtils.toString(resEntityGet);
		        JSONArray tweetsAsJson = new JSONObject(response).getJSONArray("results");
		        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		        ContentProviderClient provider = getContentResolver().acquireContentProviderClient(TweetContentProvider.AUTHORITY);
		        for(int i = 0 ; i<tweetsAsJson.length() ; i++){
		        	Tweet t = Tweet.fromJson(tweetsAsJson.getJSONObject(i));
		        	boolean allreadyInDB = provider.query(Tweet.CONTENT_URI, null, Tweet.ID + "='" + t.getId() +"'", null, null).getCount()>0;
		        	if(t != null && !allreadyInDB){
		        		operations.add(ContentProviderOperation.newInsert(Tweet.CONTENT_URI).withValues(t.getContentProviderValues()).build());
		        	}
		        }
		        provider.applyBatch(operations);
		        provider.release();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

}
