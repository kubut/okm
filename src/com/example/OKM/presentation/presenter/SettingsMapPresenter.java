package com.example.OKM.presentation.presenter;

import com.example.OKM.domain.service.PreferencesService;
import com.example.OKM.domain.valueObject.MapPositionValue;
import com.example.OKM.presentation.interactor.MapInteractor;
import com.example.OKM.presentation.view.SettingsMapActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Created by kubut on 2015-09-16.
 */
public class SettingsMapPresenter {
    private SettingsMapActivity activity;
    private SupportMapFragment mapFragment;
    private PreferencesService preferencesService;
    private MapInteractor mapInteractor;

    public SettingsMapPresenter(SettingsMapActivity activity){
        this.activity = activity;
        this.preferencesService = new PreferencesService(activity.getApplicationContext());
        this.mapInteractor = new MapInteractor();
    }

    public void connectContext(SettingsMapActivity activity, SupportMapFragment map){
        this.activity = activity;
        this.mapFragment = map;
        this.preferencesService = new PreferencesService(activity.getApplicationContext());
        this.mapInteractor.connectMap(map.getMap(), activity.getApplicationContext());
    }

    public void disconnectContext(){
        this.activity = null;
        this.mapFragment = null;
        this.mapInteractor.disconnectMap();
    }

    public void savePosition(){
        CameraPosition camera = this.mapFragment.getMap().getCameraPosition();

        this.preferencesService.setMapPosition(new MapPositionValue( camera.target, camera.zoom ));
    }

    public void setMapPosition(){
        if(this.preferencesService.isMapAutoposition()){
            this.mapInteractor.setLastMapPosition();
        } else {
            this.mapInteractor.setMapPosition(this.preferencesService.getMapPosition());
        }
    }
}
