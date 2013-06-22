package com.codepath.apps.tumblrsnap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.tumblrsnap.models.Photo;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_photos, container, false);
        TumblrClient client = ((TumblrClient) TumblrClient.getInstance(
                TumblrClient.class, getActivity()));
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

        return view;
    }
}
