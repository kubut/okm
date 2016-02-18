package com.opencachingkubutmaps.presentation.presenter;

import com.google.android.gms.maps.GoogleMap;
import com.opencachingkubutmaps.domain.service.PreferencesService;
import com.opencachingkubutmaps.domain.valueObject.MapPositionValue;
import com.opencachingkubutmaps.presentation.interactor.MapInteractor;
import com.opencachingkubutmaps.presentation.view.SettingsMapActivity;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Created by kubut on 2015-09-16.
 */
public class SettingsMapPresenter {
    private PreferencesService preferencesService;
    private final MapInteractor mapInteractor;
    private SettingsMapActivity activity;
    private GoogleMap map;

    public SettingsMapPresenter(final SettingsMapActivity activity){
        this.preferencesService = new PreferencesService(activity.getApplicationContext());
        this.mapInteractor = new MapInteractor();
    }

    public void connectContext(final SettingsMapActivity activity){
        this.preferencesService = new PreferencesService(activity.getApplicationContext());
        this.activity = activity;
    }

    public void connectMap(final GoogleMap map){
        this.map = map;
        this.mapInteractor.connectMap(this.map, this.activity.getApplicationContext());
    }

    public void disconnectContext(){
        this.map = null;
        this.mapInteractor.disconnectMap();
    }

    public void savePosition(){
        if(this.map == null){
            return;
        }

        final CameraPosition camera = this.map.getCameraPosition();

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
