package com.emilsjolander.androidworkshopexampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements TweetListFragment.Callback{
	
	private TweetListFragment tweetListFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tweetListFrag = (TweetListFragment) getFragmentManager().findFragmentById(R.id.tweet_list_fragment);
        tweetListFrag.setCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_sync:
    		startService(new Intent(this, TwitterSyncService.class));
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void tweetClicked(Tweet t) {
		//TODO
	}
    
}
