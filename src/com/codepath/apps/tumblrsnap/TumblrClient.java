package com.codepath.apps.tumblrsnap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TumblrApi;

import android.content.Context;
import android.graphics.Bitmap;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TumblrClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TumblrApi.class;
    public static final String REST_URL = "http://api.tumblr.com/v2";
    public static final String REST_CONSUMER_KEY = "BcnUeYPIxBaVCz5sYcs4SkytRqM8azgLclb1PUpeFcknic9RYY";
    public static final String REST_CONSUMER_SECRET = "FoiHHbknPRFeyiaBpOxizSGzbYsflp6DiFfwBi85kCYEznKGGh";
    public static final String REST_CALLBACK_URL = "oauth://tumblrsnap";

    public TumblrClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getTaggedPhotos(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("tag", "lol");
        params.put("api_key", REST_CONSUMER_KEY);
        client.get(getApiUrl("tagged"), params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        client.get(getApiUrl("user/info"), null, handler);
    }
    
    public void createPhotoPost(File file, AsyncHttpResponseHandler handler) {
    	RequestParams params = new RequestParams();
    	params.put("api_key", REST_CONSUMER_KEY);
    	params.put("type", "photo");
    	
//    	params.put("source", "http://media.tumblr.com/tumblr_llv2lrbX241qfvlsy.jpg");
    	try {
			params.put("data", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	client.post(getApiUrl("blog/timothy1ee.tumblr.com/post"), params, handler);
    }
    
    public void createPhotoPost(Bitmap bitmap, final AsyncHttpResponseHandler handler) {   
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] bytes = stream.toByteArray();
        String url = "http://api.tumblr.com/v2/blog/nathan-codepath.tumblr.com/post?api_key=" + REST_CONSUMER_KEY + "&type=photo";
        RequestParams params = new RequestParams();
    	params.put("type", "photo");
    	params.put("data", new ByteArrayInputStream(bytes), "image.png");
        client.post(url, params, handler);
    }
}
