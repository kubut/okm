package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.util.Log;
import com.example.OKM.data.services.OkapiCommunication;
import com.example.OKM.domain.model.CacheMarkerCollectionModel;
import com.example.OKM.domain.service.JsonTransformService;
import com.example.OKM.domain.service.OkapiService;
import com.example.OKM.presentation.interactor.MapInteractor;
import com.example.OKM.presentation.view.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONObject;

import java.io.Console;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created by kubut on 2015-07-12.
 */
public class MainMapPresenter {
    private MainActivity mainActivity;
    private SupportMapFragment mapFragment;
    private OkapiService okapiService;
    private MapInteractor mapInteractor;
    private CacheMarkerCollectionModel markerList;

    public MainMapPresenter(){
        this.okapiService = new OkapiService();
        this.mapInteractor = new MapInteractor();
    }

    public void connectContext(MainActivity mainActivity, SupportMapFragment map){
        this.mainActivity = mainActivity;
        this.mapFragment = map;

        if(map != null && map.getMap() != null){
            this.mapInteractor.connectMap(map.getMap());
        }
    }

    public void disconnectContext(){
        this.mainActivity = null;
        this.mapFragment = null;
        this.mapInteractor.disconnectMap();
    }

    public void setSatelliteMode(boolean modeOn){
        if(mapFragment == null || mapFragment.getMap() == null){
            return;
        }

        if(modeOn){
            mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void setGpsMode(boolean modeOn){
        if(mapFragment == null || mapFragment.getMap() == null){
            return;
        }

        mapFragment.getMap().setMyLocationEnabled(modeOn);
    }

    public void getCaches(boolean show){
        if(show){
            String url = okapiService.getCacheCollectionURL(this.mainActivity, mapFragment.getMap().getCameraPosition().target, -1);

            new OkapiCommunication(){
                @Override
                public void onPostExecute(String result){
                    try{
                        JsonTransformService transformService = new JsonTransformService();
                        markerList = transformService.getCacheMarkersByJson(new JSONObject(result));
                        mapInteractor.setCachesOnMap(markerList);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.execute(url);
        } else {
            markerList.clear();
            mapFragment.getMap().clear();
        }
    }

    public Context getContext(){
        return this.mainActivity.getApplicationContext();
    }

    public MainActivity getActivity() {
        return this.mainActivity;
    }
}
