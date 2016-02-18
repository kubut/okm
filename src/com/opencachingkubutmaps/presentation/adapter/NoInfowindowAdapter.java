package com.opencachingkubutmaps.presentation.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.opencachingkubutmaps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Jakub on 31.01.2016
 */
public class NoInfowindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Context context;

    public NoInfowindowAdapter(final Context context){
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoWindow(final Marker marker) {
        return ((Activity) this.context).getLayoutInflater().inflate(R.layout.no_info_window, null);
    }

    @Override
    public View getInfoContents(final Marker marker) {
        return null;
    }
}
