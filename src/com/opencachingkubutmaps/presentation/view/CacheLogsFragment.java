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
import com.opencachingkubutmaps.domain.model.CacheModel;
import com.opencachingkubutmaps.domain.service.PreferencesService;
import com.opencachingkubutmaps.domain.valueObject.CacheLogValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class CacheLogsFragment extends Fragment implements ICacheTabs {
    private WebView logsView;
    private FloatingActionButton addLog;
    private boolean ready, loaded;
    private String htmlLogs;

    public CacheLogsFragment() {
        this.ready = false;
        this.loaded = false;
        this.htmlLogs = "";
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
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        for (final CacheLogValue log : cacheModel.getLogs()) {
            this.htmlLogs += "<div style='margin-top: 10px; border-top: 4px solid " + String.format("#%06X", (0xFFFFFF & log.getColor())) + "; background-color: #F5F5F5; box-shadow: 0px 2px 13px -1px rgba(0,0,0,0.35); border-radius: 5px; padding: 5px 15px'>";
            this.htmlLogs += "<p style='float:left; font-weight: bold'>" + df.format(log.getDate()) + "</p>";
            this.htmlLogs += "<p style='float:right; font-weight: bold'>" + log.getUser() + "</p>";
            this.htmlLogs += "<p style='width: 100%; position: relative; display: block; clear: both'>" + log.getComment() + "</p>";
            this.htmlLogs += "</div>";
        }

        this.htmlLogs += "<div class='paddingBottom' style='height: 70px'></div>";

        this.loaded = true;
        this.syncView();
    }

    private void setListeners() {
        this.addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PreferencesService preferencesService = new PreferencesService(getContext());
                OAuth1AccessToken accessToken = preferencesService.getAccessToken();

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
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("Poszło")
                .positiveText(getString(R.string.ok))
                .negativeText(getString(R.string.cancel))
                .negativeColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                .show();
    }
}
