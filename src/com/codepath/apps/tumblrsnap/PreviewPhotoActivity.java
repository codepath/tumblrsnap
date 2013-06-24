package com.codepath.apps.tumblrsnap;

import java.io.File;
import java.io.IOException;

import org.scribe.builder.api.TumblrApi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.PhotoPost;

public class PreviewPhotoActivity extends Activity {
	private String photoUri;
	private Bitmap photoBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_photo);

		photoUri = getIntent().getStringExtra("photo_uri");
		photoBitmap = BitmapFactory.decodeFile(photoUri);
		photoBitmap = Bitmap.createScaledBitmap(photoBitmap, 120, 120, false);
		ImageView ivPhoto = (ImageView)findViewById(R.id.ivPhoto);
		ivPhoto.setImageBitmap(photoBitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview_photo, menu);
		return true;
	}

	public void onSaveButton(MenuItem menuItem) {
				String requestToken = getSharedPreferences("OAuth_" + TumblrApi.class.getSimpleName() + "_" + TumblrClient.REST_CONSUMER_KEY, 0).getString("request_token", "");
				String requestTokenSecret = getSharedPreferences("OAuth_" + TumblrApi.class.getSimpleName() + "_" + TumblrClient.REST_CONSUMER_KEY, 0).getString("request_token_secret", "");
				JumblrClient jumblr = new JumblrClient(TumblrClient.REST_CONSUMER_KEY, TumblrClient.REST_CONSUMER_SECRET, requestToken, requestTokenSecret);
				try {
					PhotoPost photoPost = jumblr.newPost("timothy1ee.tumblr.com", PhotoPost.class);
					photoPost.setData(new File(photoUri));
					photoPost.save(); 
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


		//		TumblrClient client = ((TumblrClient) TumblrClient.getInstance(
		//				TumblrClient.class, this));
		//		File file = new File(photoUri);
		//		client.createPhotoPost(file, new AsyncHttpResponseHandler() {
		//			@Override
		//			public void onSuccess(int arg0, String arg1) {
		//				// TODO Auto-generated method stub
		//				super.onSuccess(arg0, arg1);
		//			}
		//			
		//			@Override
		//			public void onFailure(Throwable arg0, String arg1) {
		//				Log.d("DEBUG", arg0.toString());
		//				Log.d("DEBUG", arg1);
		//			}
		//		});

//		TumblrClient client = ((TumblrClient) TumblrClient.getInstance(
//				TumblrClient.class, this));
//		client.createPhotoPost(photoBitmap, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int arg0, String arg1) {
//				// TODO Auto-generated method stub
//				super.onSuccess(arg0, arg1);
//			}
//
//			@Override
//			public void onFailure(Throwable arg0, String arg1) {
//				Log.d("DEBUG", arg0.toString());
//				Log.d("DEBUG", arg1);
//			}
//		});
	}
}
