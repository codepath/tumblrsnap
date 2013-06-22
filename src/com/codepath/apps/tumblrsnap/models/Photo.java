package com.codepath.apps.tumblrsnap.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Photo extends BaseModel {

    public String getId() {
        return getString("id");
    }

    public String getBlogName() {
        return getString("blog_name");
    }

    public long getTimestamp() {
        return getLong("timestamp");
    }

    public String getAvatarUrl() {
        return String.format(
                "http://api.tumblr.com/v2/blog/%s.tumblr.com/avatar/64",
                getBlogName());
    }

    public String getCaption() {
        return getString("caption");
    }

    public String getPhotoUrl() {
        try {
            return getPhotoMeta().getString("url");
        } catch (Exception e) {
            return null;
        }
    }

    public int getPhotoHeight() {
        try {
            return getPhotoMeta().getInt("height");
        } catch (Exception e) {
            return 0;
        }
    }

    public int getPhotoWidth() {
        try {
            return getPhotoMeta().getInt("width");
        } catch (Exception e) {
            return 0;
        }
    }

    private JSONObject getPhotoMeta() {
        try {
            JSONArray altSizes = ((JSONObject) getJSONArray("photos").get(0))
                    .getJSONArray("alt_sizes");
            for (int i = 0; i < altSizes.length(); i++) {
                JSONObject altSize = (JSONObject) altSizes.get(i);
                int width = altSize.getInt("width");
                if (width < 1000) {
                    return altSize;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static Photo fromJson(JSONObject jsonObject) {
        Photo photo = new Photo();
        photo.jsonObject = jsonObject;
        return photo;
    }

    public static ArrayList<Photo> fromJson(JSONArray jsonArray) {
        ArrayList<Photo> photos = new ArrayList<Photo>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject photoJson = jsonArray.getJSONObject(i);
                if (photoJson.getString("type").equals("photo")) {
                    photos.add(Photo.fromJson(photoJson));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return photos;
    }

}
