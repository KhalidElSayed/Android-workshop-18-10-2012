package com.emilsjolander.androidworkshopexampleapp;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends BaseAdapter {
	
	private Cursor data;
	private LayoutInflater inflater;
	private int layout = R.layout.tweet_list_item;
	private LruCache<String, Bitmap> cache;
	
	public TweetAdapter(Context c, Cursor data) {
		this.data = data;
		inflater = LayoutInflater.from(c);
		cache = new LruCache<String, Bitmap>(100);
	}
	
	public void setCursor(Cursor data){
		this.data = data;
		notifyDataSetChanged();
	}

	public Cursor getCursor() {
		return data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.getCount();
	}

	@Override
	public Object getItem(int position) {
		data.moveToPosition(position);
		return data;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		data.moveToPosition(position);
		Tweet t = Tweet.fromCursor(data);
		ViewHolder holder;
		
		if(convertView == null){
			convertView = inflater.inflate(layout, null);
			holder = new ViewHolder();

			holder.user = (TextView) convertView.findViewById(R.id.user_name);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.picUrl = t.getProfileImageUrl();
		holder.user.setText("@"+t.getFromUser());
		holder.text.setText(t.getText());
		
		Bitmap profilePic = cache.get(t.getProfileImageUrl());
		if(profilePic != null){
			holder.profilePic.setImageBitmap(profilePic);
		}else{
			holder.profilePic.setImageDrawable(null);
			ImageLoaderTask task = new ImageLoaderTask(holder);
			task.execute();
		}
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView user;
		TextView text;
		ImageView profilePic;
		String picUrl;
	}
	
	private class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap>{
		
		private ViewHolder holder;
		private String picUrl;

		public ImageLoaderTask(ViewHolder holder) {
			this.holder = holder;
			this.picUrl = holder.picUrl;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap b = null;
            try {
                InputStream in = new URL(picUrl).openStream();
                b = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
			return b;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result != null){
				cache.put(picUrl, result);
				if(holder.picUrl.equals(picUrl)){
					holder.profilePic.setImageBitmap(result);
				}
			}
		}
		
	}

}
