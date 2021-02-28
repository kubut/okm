package com.opencachingkubutmaps.data.services;

import android.content.Context;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Verb;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.service.PreferencesService;

public class OkapiOauthApi extends DefaultApi10a {
    private String authorizeServiceUrl;
    private String requestTokenServiceUrl;
    private String accessTokenServiceUrl;
    private static OkapiOauthApi instance = null;
    private PreferencesService preferencesService;

    protected OkapiOauthApi(Context context) {
        this.preferencesService = new PreferencesService(context);

        authorizeServiceUrl = context.getString(R.string.okapi_oath_authorize);
        requestTokenServiceUrl = context.getString(R.string.okapi_oath_request_token);
        accessTokenServiceUrl = context.getString(R.string.okapi_oauth_access_token);
    }

    public static OkapiOauthApi getInstance(Context context) {
        if (instance == null) {
            instance = new OkapiOauthApi(context);
        }

        return instance;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return preferencesService.getServerAPI() + accessTokenServiceUrl;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return preferencesService.getServerAPI() + requestTokenServiceUrl;
    }

    public String getAuthorizationEndpoint() {
        return preferencesService.getServerAPI() + authorizeServiceUrl;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return getAuthorizationEndpoint() + "?oauth_token=%s";
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(getAuthorizationEndpoint() + "?oauth_token=%s", requestToken.getToken());
    }

    @Override
    public OAuth1SignatureType getSignatureType() {
        return OAuth1SignatureType.QueryString;
    }
}
