package com.opencachingkubutmaps.data.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class OkapiOauthAuthorizationUrlTask extends AsyncTask<String, Void, String> {
    private final WeakReference<Context> weakContext;
    public OAuth1RequestToken requestToken;

    OkapiOauthAuthorizationUrlTask(Context context) {
        this.weakContext = new WeakReference<Context>(context);
    }

    @Override
    protected String doInBackground(final String... params) {
        String response = null;

        String consumerKey = params[0];
        String consumerSecret = params[1];

        OAuth10aService service = new ServiceBuilder(consumerKey)
                .debug()
                .apiSecret(consumerSecret)
                .build(OkapiOauthApi.getInstance(weakContext.get()));

        try {
            this.requestToken = service.getRequestToken();
            response = service.getAuthorizationUrl(requestToken);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }
}
