package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.example.OKM.R;
import com.example.OKM.data.services.OkapiCommunication;
import com.example.OKM.domain.model.CacheMarkerCollectionModel;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.domain.service.JsonTransformService;
import com.example.OKM.domain.service.OkapiService;
import com.example.OKM.domain.service.PreferencesService;
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
    private AsyncTask markersDownloader, uuidDownloader;
    private Toast toast;
    private PreferencesService preferencesService;
    private boolean downloadTask, isGPS, isSattelite;

    public MainMapPresenter(MainActivity activity){
        this.okapiService = new OkapiService();
        this.mapInteractor = new MapInteractor();
        this.markerList = new CacheMarkerCollectionModel();
        this.mainActivity = activity;
        this.downloadTask = false;
        this.isGPS = false;
        this.isSattelite = false;
    }

    public void sync(){
        this.syncProgressBar();
        this.applyCaches();
        this.setGpsMode(this.isGPS);
        this.setSatelliteMode(this.isSattelite);
    }

    public void connectContext(MainActivity mainActivity, SupportMapFragment map){
        this.mainActivity = mainActivity;
        this.mapFragment = map;

        if(map != null && map.getMap() != null){
            this.mapInteractor.connectMap(map.getMap(), mainActivity.getApplicationContext());
        }
    }

    public void disconnectContext(){
        this.mainActivity = null;
        this.mapFragment = null;
        this.mapInteractor.disconnectMap();
    }

    public void setSatelliteMode(boolean modeOn){
        this.isSattelite = modeOn;

        this.setDrawerOptionState(getContext().getString(R.string.drawer_satellite), modeOn);

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
        this.isGPS = modeOn;

        if(mapFragment == null || mapFragment.getMap() == null){
            return;
        }

        mapFragment.getMap().setMyLocationEnabled(modeOn);
        this.setDrawerOptionState(getContext().getString(R.string.drawer_gps), modeOn);
    }

    public void setCaches(boolean show){
        this.downloadTask = show;

        if(show){
            this.syncProgressBar();
            this.showToast(this.getContext().getString(R.string.toast_downloading));

            final PreferencesService preferencesService = new PreferencesService(this.getContext());
            final String uuid = preferencesService.getUuid();

            if(uuid == null && preferencesService.getUsername() != null && preferencesService.isHideFound()){
                try{
                    String url = okapiService.getUuidURL(this.mainActivity, preferencesService.getUsername());

                    uuidDownloader = new OkapiCommunication(){
                        @Override
                        public void onPostExecute(String result){
                            try {
                                JsonTransformService transformService = new JsonTransformService();
                                String newUuid = transformService.getUuidByJson(new JSONObject(result));
                                preferencesService.setUuid(newUuid);

                                getAndApplyCaches(newUuid);
                            } catch (Exception e){
                                getAndApplyCaches(uuid);
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                } catch (Exception e){
                    e.printStackTrace();
                    getAndApplyCaches(uuid);
                }
            } else {
                getAndApplyCaches(uuid);
            }
        } else {
            this.cancelDownloader();
            markerList.clear();
            mapFragment.getMap().clear();
        }
    }

    public void getAndApplyCaches(String uuid){
        String url = okapiService.getCacheCollectionURL(this.mainActivity, mapFragment.getMap().getCameraPosition().target, uuid);

        markersDownloader = new OkapiCommunication(){
            @Override
            public void onPostExecute(String result){
                try{
                    if(!this.isCancelled()){
                        JsonTransformService transformService = new JsonTransformService();
                        markerList.append(transformService.getCacheMarkersByJson(getContext(), new JSONObject(result)));
                        applyCaches();
                    }
                } catch (Exception e){
                    showToast(getContext().getString(R.string.toast_downloading_error));
                    setDrawerOptionState(getContext().getString(R.string.drawer_caches), false);
                    e.printStackTrace();
                }
                downloadTask = false;
                syncProgressBar();
            }
        }.execute(url);
    }

    public void syncProgressBar(){
        this.getActivity().displayProgressBar(this.downloadTask);
    }

    public void applyCaches(){
        mapInteractor.setCachesOnMap(markerList);

        this.setDrawerOptionState(getContext().getString(R.string.drawer_caches), !this.markerList.isEmpty() || this.downloadTask);
    }

    public Context getContext(){
        return this.mainActivity.getApplicationContext();
    }

    public MainActivity getActivity() {
        return this.mainActivity;
    }

    public void hideDrawer(){
        this.mainActivity.hideNavigationDrawer();
    }

    public void showToast(String text){
        if(this.toast != null){
            this.toast.cancel();
        }

        this.toast = Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setDrawerOptionState(String key, boolean state){
        if(this.getActivity().mainDrawerActionItemsList == null){
            return;
        }

        for(IMainDrawerItem item : this.getActivity().mainDrawerActionItemsList){
            if(item.getTitle().equals(key)){
                item.setActive(state);
            }
        }
        this.getActivity().syncDrawerItems();
    }

    public void cancelDownloader(){
        if(this.markersDownloader != null){
            this.markersDownloader.cancel(false);
        }
        if(this.uuidDownloader != null){
            this.uuidDownloader.cancel(false);
        }
        this.syncProgressBar();
    }

    public void setMapPosition(){
        this.preferencesService = new PreferencesService(this.getContext());

        if(this.preferencesService.isMapAutoposition()){
            this.mapInteractor.setLastMapPosition();
        } else {
            this.mapInteractor.setMapPosition(this.preferencesService.getMapPosition());
        }
    }

    public void setLastLocation(@Nullable Location location){
        this.mapInteractor.setLastLocation(location);
    }
}
