package com.opencachingkubutmaps.data.services;

import android.content.Context;
import android.os.AsyncTask;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.lang.ref.WeakReference;

public class OkapiOauthSignedRequest extends AsyncTask<String, Void, String> {
    private final WeakReference<Context> weakContext;

    public OkapiOauthSignedRequest(Context context) {
        this.weakContext = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(final String... params) {
        String response = "";

        String consumerKey = params[0];
        String consumerSecret = params[1];
        String accessToken = params[2];
        String accessTokenSecret = params[3];
        String endpoint = params[4];

        OAuth1AccessToken token = new OAuth1AccessToken(accessToken, accessTokenSecret);

        OAuth10aService service = new ServiceBuilder(consumerKey)
                .debug()
                .apiSecret(consumerSecret)
                .build(OkapiOauthApi.getInstance(weakContext.get()));

        try {
            final OAuthRequest request = new OAuthRequest(Verb.GET, endpoint);
            service.signRequest(token, request);

            response = service.execute(request).getBody();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
