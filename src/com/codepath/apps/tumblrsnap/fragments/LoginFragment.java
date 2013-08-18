package com.codepath.apps.tumblrsnap.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.apps.tumblrsnap.R;
import com.codepath.apps.tumblrsnap.TumblrClient;
import com.codepath.apps.tumblrsnap.models.User;
import com.codepath.oauth.OAuthLoginFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginFragment extends OAuthLoginFragment<TumblrClient> {
    private OnLoginHandler loginHandler;

    public interface OnLoginHandler {
        public void onLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button button = (Button) view.findViewById(R.id.btnLogin);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().connect();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnLoginHandler) {
            loginHandler = (OnLoginHandler) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginHandler = null;
    }

    @Override
    public void onLoginFailure(Exception arg0) {
        getClient().clearAccessToken();
        Log.d("DEBUG", "on login failure: " + arg0.toString());
    }

    @Override
    public void onLoginSuccess() {
        Log.d("DEBUG", "on login success");
        getClient().getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int code, JSONObject response) {
                try {
                    JSONObject jsonUser = response.getJSONObject("response")
                            .getJSONObject("user");
                    User.setCurrentUser(User.fromJson(jsonUser));
                    if (loginHandler != null) {
                        loginHandler.onLogin();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d("DEBUG", error.toString());
            }
        });
    }
}
