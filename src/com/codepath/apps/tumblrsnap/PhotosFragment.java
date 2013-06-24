package com.codepath.apps.tumblrsnap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.TumblrApi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.tumblrsnap.models.Photo;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosFragment extends Fragment {
	private static final int TAKE_PHOTO_CODE = 1;
	private static final int PICK_PHOTO_CODE = 2;
	
	private String photoUri;
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_photos, container, false);
		TumblrClient client = ((TumblrClient) TumblrClient.getInstance(
				TumblrClient.class, getActivity()));
		
		Log.d("DEBUG", getActivity().getSharedPreferences("OAuth_" + TumblrApi.class.getSimpleName() + "_" + TumblrClient.REST_CONSUMER_KEY, 0).getString("request_token", ""));
		Log.d("DEBUG", getActivity().getSharedPreferences("OAuth_" + TumblrApi.class.getSimpleName() + "_" + TumblrClient.REST_CONSUMER_KEY, 0).getString("request_token_secret", ""));
		Log.d("DEBUG", "boo");
		
//		06-24 00:58:29.295: D/DEBUG(14417): hsM8YGTInD1R5hdYwRXMmKdK3EpQdsUdRBAclbnDmQfI1RsVGH
//		06-24 00:58:29.295: D/DEBUG(14417): trMMiH0mBDIbvwO989UhTQbCzkAHNfarTc27kW5a4ZSkz9lQDB

		client.getTaggedPhotos(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int code, JSONObject response) {
				try {
					JSONArray photosJson = response.getJSONArray("response");
					ArrayList<Photo> photos = Photo.fromJson(photosJson);
					PhotosAdapter adapter = new PhotosAdapter(getActivity(),
							photos);
					ListView lvPhotos = (ListView) getActivity().findViewById(
							R.id.lvPhotos);
					lvPhotos.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				Log.d("DEBUG", arg0.toString());
			}
		});

		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.photos, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_take_photo:
			{
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File photoFile = getOutputMediaFile(); // create a file to save the image
			    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile)); // set the image file name
			    photoUri = photoFile.getAbsolutePath();
			    startActivityForResult(i, TAKE_PHOTO_CODE);
			}
			break;
			case R.id.action_use_existing:
			{
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, PICK_PHOTO_CODE);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == TAKE_PHOTO_CODE) {
				startPreviewPhotoActivity();
			} else if (requestCode == PICK_PHOTO_CODE) {
	            photoUri = getFileUri(data.getData());
	            startPreviewPhotoActivity();
			}
		}
	}
	
	private String getFileUri(Uri mediaStoreUri) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(mediaStoreUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String fileUri = cursor.getString(columnIndex);
        cursor.close();
        
        return fileUri;
	}
	
	private void startPreviewPhotoActivity() {
		Intent i = new Intent(getActivity(), PreviewPhotoActivity.class);
        i.putExtra("photo_uri", photoUri);
        startActivity(i);
	}
	
	private static File getOutputMediaFile() {
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "tumblrsnap");
	    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
	        return null;
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}
}
