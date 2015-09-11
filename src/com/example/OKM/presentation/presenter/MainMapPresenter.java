package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.os.AsyncTask;
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

    public MainMapPresenter(){
        this.okapiService = new OkapiService();
        this.mapInteractor = new MapInteractor();
        this.markerList = new CacheMarkerCollectionModel();
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

    public void setCaches(boolean show){
        if(show){
            this.showToast(this.getContext().getString(R.string.toast_downloading));

            final PreferencesService preferencesService = new PreferencesService(this.getContext());
            final String uuid = preferencesService.getUuid();

            if(uuid == null && preferencesService.getUsername() != null){
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
                        markerList.append(transformService.getCacheMarkersByJson(new JSONObject(result)));
                        applyCaches();
                    }
                } catch (Exception e){
                    showToast(getContext().getString(R.string.toast_downloading_error));
                    setDrawerOptionState(getContext().getString(R.string.drawer_caches), false);
                    e.printStackTrace();
                }
            }
        }.execute(url);
    }

    public void applyCaches(){
        mapInteractor.setCachesOnMap(markerList);
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
        for(IMainDrawerItem item : this.getActivity().mainDrawerActionItemsList){
            if(item.getTitle().equals(key)){
                item.setActive(state);
            }
        }
        this.getActivity().syncDrawerItems();
    }

    public void cancelDownloader(){
        if(this.markersDownloader != null){
            this.markersDownloader.cancel(true);
        }
        if(this.uuidDownloader != null){
            this.uuidDownloader.cancel(true);
        }
    }
}
