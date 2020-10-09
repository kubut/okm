package com.opencachingkubutmaps.data.services;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Verb;

public class OkapiOauthApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://opencaching.pl/okapi/services/oauth/authorize?oauth_token=%s";

    protected OkapiOauthApi() {
    }

    private static class InstanceHolder {
        private static final OkapiOauthApi INSTANCE = new OkapiOauthApi();
    }

    public static OkapiOauthApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint(){
        return "https://opencaching.pl/okapi/services/oauth/access_token";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://opencaching.pl/okapi/services/oauth/request_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    @Override
    public OAuth1SignatureType getSignatureType() {
        return OAuth1SignatureType.QueryString;
    }
}
