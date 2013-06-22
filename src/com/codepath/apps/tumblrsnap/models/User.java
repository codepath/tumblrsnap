package com.codepath.apps.tumblrsnap.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.tumblrsnap.TumblrSnapApp;

public class User extends BaseModel {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;

        if (user == null) {
            TumblrSnapApp.getSharedPreferences().edit().remove("current_user")
                    .commit();
        } else {
            TumblrSnapApp.getSharedPreferences().edit()
                    .putString("current_user", user.getJSONString()).commit();
        }
    }

    public static User currentUser() {
        if (currentUser == null) {
            // Attempt to retrieve the current user from shared preferences
            String userJsonString = TumblrSnapApp.getSharedPreferences()
                    .getString("current_user", null);
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
