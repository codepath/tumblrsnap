package com.codepath.apps.tumblrsnap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Application;
import android.content.SharedPreferences;

public class TumblrSnapApp extends Application {
    private static TumblrSnapApp instance;

    public static TumblrSnapApp getInstance() {
        return instance;
    }

    public static SharedPreferences getSharedPreferences() {
        if (instance != null) {
            return instance.getSharedPreferences("tumblrsnap", 0);
        }

        return null;
    }

    public static TumblrClient getClient() {
        return (TumblrClient) TumblrClient.getInstance(TumblrClient.class,
                instance);
    }

    @Override
    public void onCreate() {
        TumblrSnapApp.instance = this;
        super.onCreate();

        // Create global configuration and initialize ImageLoader with this
        // configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(
                defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }
}
