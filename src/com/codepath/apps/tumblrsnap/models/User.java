package com.codepath.apps.tumblrsnap.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codepath.apps.tumblrsnap.TumblrSnapApp;

public class User {
    private static User currentUser;
    protected JSONObject jsonObject;
    
    public String getBlogHostname() {
    	try {
	    	JSONArray blogs = jsonObject.getJSONArray("blogs");
	    	JSONObject blog = (JSONObject)blogs.get(0);
	    	return blog.getString("name") + ".tumblr.com";
    	} catch (Exception e) {
    		return null;
    	}
    }

    public static void setCurrentUser(User user) {
        currentUser = user;

        if (user == null) {
            TumblrSnapApp.getSharedPreferences().edit().remove("current_user")
                    .commit();
        } else {
            TumblrSnapApp.getSharedPreferences().edit()
                    .putString("current_user", user.jsonObject.toString()).commit();
        }
    }

    public static User currentUser() {
        if (currentUser == null) {
            // Attempt to retrieve the current user from shared preferences
            String userJsonString = TumblrSnapApp.getSharedPreferences()
                    .getString("current_user", null);
            Log.d("DEBUG", "current_user: " + userJsonString);
            if (userJsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(userJsonString);
                    if (jsonObject != null) {
                        currentUser = User.fromJson(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return currentUser;
    }

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        user.jsonObject = jsonObject;
        return user;
    }
}
