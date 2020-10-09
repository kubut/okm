package com.opencachingkubutmaps.data.services;

import android.os.AsyncTask;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class OkapiOauthAuthorizationUrlTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(final String... params) {
        String response = null;

        String consumerKey = "599yAKYxPVjsVG4TTLQG"; //api key
        String consumerSecret = "HG3bfbesfetbdtzE3q2HabYWHSFsvnY4TS5YTagZ"; //api secret

        OAuth10aService service = new ServiceBuilder(consumerKey)
                .apiSecret(consumerSecret)
                .build(OkapiOauthApi.instance());

        try {
            final OAuth1RequestToken requestToken = service.getRequestToken();
            response = service.getAuthorizationUrl(requestToken);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }
}
