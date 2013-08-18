package com.codepath.apps.tumblrsnap.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.tumblrsnap.R;
import com.codepath.apps.tumblrsnap.TumblrSnapApp;
import com.codepath.apps.tumblrsnap.models.User;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public void onLogoutButton(View view) {
        TumblrSnapApp.getClient().clearAccessToken();
        User.setCurrentUser(null);

        finish();
    }

}
