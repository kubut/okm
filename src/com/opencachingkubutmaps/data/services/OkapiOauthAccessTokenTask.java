package com.opencachingkubutmaps.data.services;

import android.os.AsyncTask;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class OkapiOauthAccessTokenTask extends AsyncTask<String, Void, OAuth1AccessToken> {
    @Override
    protected OAuth1AccessToken doInBackground(final String... params) {
        String verifier = params[0];
        OAuth1AccessToken response = null;

        String consumerKey = "599yAKYxPVjsVG4TTLQG"; //api key
        String consumerSecret = "HG3bfbesfetbdtzE3q2HabYWHSFsvnY4TS5YTagZ"; //api secret

        OAuth10aService service = new ServiceBuilder(consumerKey)
                .apiSecret(consumerSecret)
                .debug()
                .build(OkapiOauthApi.instance());

        try {
            final OAuth1RequestToken requestToken = service.getRequestToken();
            response = service.getAccessToken(requestToken, verifier);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (OAuthException e) {
            e.printStackTrace();
        }

        return response;
    }
}
