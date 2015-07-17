package com.example.OKM.presentation.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.example.OKM.R;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kubut on 2015-07-12.
 */
public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback{
    private SupportMapFragment map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map_activity);

        map = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    public void onMapReady(GoogleMap map) {

    }
}