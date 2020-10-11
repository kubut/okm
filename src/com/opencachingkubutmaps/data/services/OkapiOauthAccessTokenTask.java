package com.opencachingkubutmaps.data.services;

import android.content.Context;
import android.os.AsyncTask;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class OkapiOauthAccessTokenTask extends AsyncTask<String, Void, OAuth1AccessToken> {
    private final WeakReference<Context> weakContext;

    OkapiOauthAccessTokenTask(Context context) {
        this.weakContext = new WeakReference<Context>(context);
    }

    @Override
    protected OAuth1AccessToken doInBackground(final String... params) {
        OAuth1AccessToken response = null;

        String verifier = params[0];
        String consumerKey = params[1];
        String consumerSecret = params[2];
        String requestToken = params[3];
        String requestTokenSecret = params[4];

        OAuth10aService service = new ServiceBuilder(consumerKey)
                .apiSecret(consumerSecret)
                .debug()
                .build(OkapiOauthApi.getInstance(weakContext.get()));

        try {
            final OAuth1RequestToken token = new OAuth1RequestToken(requestToken, requestTokenSecret);
            response = service.getAccessToken(token, verifier);
        } catch (IOException | InterruptedException | ExecutionException | OAuthException e) {
            e.printStackTrace();
        }

        return response;
    }
}
