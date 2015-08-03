package com.example.OKM.presentation.presenter;

import android.content.Context;
import com.example.OKM.presentation.view.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by kubut on 2015-07-12.
 */
public class MainMapPresenter {
    private MainActivity mainActivity;
    private SupportMapFragment map;

    public MainMapPresenter(MainActivity mainActivity, SupportMapFragment map){
        this.mainActivity = mainActivity;
        this.map = map;
    }

    public void setSatelliteMode(boolean modeOn){
        if(map == null || map.getMap() == null){
            return;
        }

        if(modeOn){
            map.getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            map.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public Context getContext(){
        return this.mainActivity.getApplicationContext();
    }
}
