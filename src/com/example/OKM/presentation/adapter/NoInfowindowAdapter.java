package com.example.OKM.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.example.OKM.R;
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

    @Override
    public View getInfoWindow(final Marker marker) {
        return ((Activity) this.context).getLayoutInflater().inflate(R.layout.no_info_window, null);
    }

    @Override
    public View getInfoContents(final Marker marker) {
        return null;
    }
}
