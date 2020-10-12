package com.opencachingkubutmaps.presentation.view;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.data.services.OkapiOauthDanceService;
import com.opencachingkubutmaps.data.services.OkapiOauthSignedRequest;
import com.opencachingkubutmaps.domain.model.CacheModel;
import com.opencachingkubutmaps.domain.service.OkapiService;
import com.opencachingkubutmaps.domain.service.PreferencesService;
import com.opencachingkubutmaps.domain.valueObject.CacheLogValue;
import com.opencachingkubutmaps.presentation.presenter.CachePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class CacheLogsFragment extends Fragment implements ICacheTabs {
    private WebView logsView;
    private FloatingActionButton addLog;
    private boolean ready, loaded;
    private String htmlLogs;
    private CacheModel cacheModel;
    private PreferencesService _preferencesService;
    private CachePresenter cachePresenter;

    public CacheLogsFragment(CachePresenter cachePresenter) {
        this.ready = false;
        this.loaded = false;
        this.htmlLogs = "";
        this.cachePresenter = cachePresenter;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cache_logs, container, false);

        this.logsView = (WebView) view.findViewById(R.id.cache_logs);
        this.addLog = view.findViewById(R.id.add_log);
        this.logsView.setBackgroundColor(Color.argb(1, 0, 0, 0));

        this.ready = true;
        this.setListeners();
        this.syncView();

        return view;
    }

    @Override
    public void syncView() {
        if (this.loaded && this.ready) {
            this.logsView.loadDataWithBaseURL(null, this.htmlLogs, "text/html", "utf-8", null);
        }
    }

    @Override
    public void setView(final Context context, final CacheModel cacheModel) {
        this.cacheModel = cacheModel;

        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        this.htmlLogs = "";

        for (final CacheLogValue log : cacheModel.getLogs()) {
            this.htmlLogs += "<div style='margin-top: 10px; border-top: 4px solid " + String.format("#%06X", (0xFFFFFF & log.getColor())) + "; background-color: #F5F5F5; box-shadow: 0px 2px 13px -1px rgba(0,0,0,0.35); border-radius: 5px; padding: 5px 15px'>";
            this.htmlLogs += "<p style='float:left; font-weight: bold'>" + df.format(log.getDate()) + "</p>";
            this.htmlLogs += "<p style='float:right; font-weight: bold'>" + log.getUser() + "</p>";
            this.htmlLogs += "<p style='width: 100%; position: relative; display: block; clear: both'>" + log.getComment() + "</p>";
            this.htmlLogs += "</div>";
        }

        if (cacheModel.getLogs().isEmpty()) {
            this.htmlLogs += "<h1 style='margin-top: 50px; text-align: center'>"+context.getString(R.string.label_last_found_none)+"</h1>";
        }

        this.htmlLogs += "<div class='paddingBottom' style='height: 70px'></div>";

        this.loaded = true;
        this.syncView();
    }

    public void createLogCallback(String logType, String comment, int rating, String pass) {
        final String apiUrl = getPreferencesService().getServerAPI();

        final String consumerKey = OkapiService.getOkapiKey(getContext(), apiUrl);
        final String consumerSecret = OkapiService.getOkapiSecret(getContext(), apiUrl);
        final OAuth1AccessToken accessToken = getPreferencesService().getAccessToken();

        final String endpointUrl = OkapiService.getLogSubmitUrl(this.getContext(), cacheModel.getCode(), logType, comment, rating, pass);

        new OkapiOauthSignedRequest(getContext()) {
            @Override
            public void onPostExecute(final String result) {
                if (result == null) {
                    showModal(getString(R.string.create_log_error_title), getString(R.string.create_log_error_general));
                    return;
                }

                try {
                    final JSONObject jsonResult = new JSONObject(result);

                    if (jsonResult.has("error")) {
                        handleApiError(jsonResult);
                        return;
                    }

                    if (jsonResult.has("success")) {
                        if (jsonResult.getBoolean("success")) {
                            showModal(getString(R.string.create_log_success_title), getString(R.string.create_log_success));
                            cachePresenter.loadCacheDetails(cacheModel.getCode(), true);
                        } else {
                            showModal(getString(R.string.create_log_error_title), jsonResult.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showModal(getString(R.string.create_log_error_title), getString(R.string.create_log_error_general));
                }
            }
        }
                .execute(consumerKey, consumerSecret, accessToken.getToken(), accessToken.getTokenSecret(), endpointUrl);
    }

    private void handleApiError(JSONObject jsonResult) throws JSONException {
        final JSONObject jsonError = jsonResult.getJSONObject("error");
        final JSONArray jsonReasonStack = jsonError.getJSONArray("reason_stack");

        for (int i = 0; i < jsonReasonStack.length(); i++) {
            if ("invalid_token".equals(jsonReasonStack.get(i))) {
                showModal(getString(R.string.create_log_error_title), getString(R.string.create_log_error_auth));
                getPreferencesService().setAccessToken(null, null);

                return;
            }
        }

        showModal(getString(R.string.create_log_error_title), getString(R.string.create_log_error_general));
    }

    private void setListeners() {
        this.addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OAuth1AccessToken accessToken = getPreferencesService().getAccessToken();

                if (accessToken == null) {
                    new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                            .title(getString(R.string.oauth_begin_title))
                            .content(getString(R.string.oauth_begin_subtitle))
                            .positiveText(getString(R.string.ok))
                            .negativeText(getString(R.string.cancel))
                            .negativeColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    OkapiOauthDanceService.MakeDance(getContext(), new Runnable() {
                                        @Override
                                        public void run() {
                                            openLogEditor();
                                        }
                                    });
                                }
                            })
                            .show();
                } else {
                    openLogEditor();
                }
            }
        });
    }

    private void openLogEditor() {
        CreateLogFragment custom = CreateLogFragment.newInstance(cacheModel);
        custom.show(CacheLogsFragment.this.getChildFragmentManager(), "dialog_fragment");
    }

    private void showModal(String title, String content) {
        new MaterialDialog.Builder(getContext())
                .title(title)
                .content(content)
                .positiveText(R.string.ok)
                .show();
    }

    private PreferencesService getPreferencesService() {
        if (this._preferencesService == null) {
            this._preferencesService = new PreferencesService(getContext());
        }

        return this._preferencesService;
    }
}
