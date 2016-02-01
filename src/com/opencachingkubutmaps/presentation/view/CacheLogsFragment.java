package com.opencachingkubutmaps.presentation.view;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.CacheModel;
import com.opencachingkubutmaps.domain.valueObject.CacheLogValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CacheLogsFragment extends Fragment implements ICacheTabs{
    private WebView logsView;
    private boolean ready, loaded;
    private String htmlLogs;

    public CacheLogsFragment(){
        this.ready = false;
        this.loaded = false;
        this.htmlLogs = "";
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_cache_logs, container, false);

        this.logsView = (WebView) view.findViewById(R.id.cache_logs);
        this.logsView.setBackgroundColor(Color.TRANSPARENT);

        this.ready = true;
        this.syncView();

        return view;
    }

    @Override
    public void syncView() {
        if(this.loaded && this.ready){
            this.logsView.loadDataWithBaseURL(null, this.htmlLogs, "text/html", "utf-8", null);
        }
    }

    @Override
    public void setView(final Context context, final CacheModel cacheModel) {
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        for(final CacheLogValue log : cacheModel.getLogs()){
            this.htmlLogs += "<div style='margin-top: 10px; border-top: 4px solid " + String.format("#%06X", (0xFFFFFF & log.getColor())) + "; background-color: #F5F5F5; box-shadow: 0px 2px 13px -1px rgba(0,0,0,0.35); border-radius: 5px; padding: 5px 15px'>";
            this.htmlLogs += "<p style='float:left; font-weight: bold'>"+df.format(log.getDate())+"</p>";
            this.htmlLogs += "<p style='float:right; font-weight: bold'>"+log.getUser()+"</p>";
            this.htmlLogs += "<p style='width: 100%; position: relative; display: block; clear: both'>"+log.getComment()+"</p>";
            this.htmlLogs += "</div>";
        }

        this.loaded = true;
        this.syncView();
    }
}
