package com.opencachingkubutmaps.presentation.presenter;

import com.opencachingkubutmaps.domain.service.PreferencesService;
import com.opencachingkubutmaps.domain.valueObject.MapPositionValue;
import com.opencachingkubutmaps.presentation.interactor.MapInteractor;
import com.opencachingkubutmaps.presentation.view.SettingsMapActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Created by kubut on 2015-09-16.
 */
public class SettingsMapPresenter {
    private SupportMapFragment mapFragment;
    private PreferencesService preferencesService;
    private final MapInteractor mapInteractor;

    public SettingsMapPresenter(final SettingsMapActivity activity){
        this.preferencesService = new PreferencesService(activity.getApplicationContext());
        this.mapInteractor = new MapInteractor();
    }

    public void connectContext(final SettingsMapActivity activity, final SupportMapFragment map){
        this.mapFragment = map;
        this.preferencesService = new PreferencesService(activity.getApplicationContext());
        this.mapInteractor.connectMap(map.getMap(), activity.getApplicationContext());
    }

    public void disconnectContext(){
        this.mapFragment = null;
        this.mapInteractor.disconnectMap();
    }

    public void savePosition(){
        final CameraPosition camera = this.mapFragment.getMap().getCameraPosition();

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
