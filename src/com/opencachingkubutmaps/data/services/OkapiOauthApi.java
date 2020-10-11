package com.opencachingkubutmaps.data.services;

import android.content.Context;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Verb;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.service.PreferencesService;

public class OkapiOauthApi extends DefaultApi10a {
    private String authorizeUrl;
    private String requestTokenUrl;
    private String accessTokenUrl;
    private static OkapiOauthApi instance = null;

    protected OkapiOauthApi(Context context) {
        final PreferencesService preferencesService = new PreferencesService(context);
        final String apiUrl = preferencesService.getServerAPI();

        authorizeUrl = apiUrl + context.getString(R.string.okapi_oath_authorize);
        requestTokenUrl = apiUrl + context.getString(R.string.okapi_oath_request_token);
        accessTokenUrl = apiUrl + context.getString(R.string.okapi_oauth_access_token);
    }

    public static OkapiOauthApi getInstance(Context context) {
        if (instance == null) {
            instance = new OkapiOauthApi(context);
        }

        return instance;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return accessTokenUrl;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return authorizeUrl+"?oauth_token=%s";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return requestTokenUrl;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(authorizeUrl+"?oauth_token=%s", requestToken.getToken());
    }

    @Override
    public OAuth1SignatureType getSignatureType() {
        return OAuth1SignatureType.QueryString;
    }
}
