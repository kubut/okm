package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.OKM.R;
import com.example.OKM.data.services.OkapiCommunication;
import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CacheMarkerCollectionModel;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.domain.service.JsonTransformService;
import com.example.OKM.domain.service.OkapiService;
import com.example.OKM.domain.service.PreferencesService;
import com.example.OKM.domain.valueObject.MapPositionValue;
import com.example.OKM.presentation.interactor.MapInteractor;
import com.example.OKM.presentation.view.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import org.json.JSONObject;

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
    private boolean downloadTask, isGPS, isSattelite, isInfowindow;
    private Marker selectedMarker;
    private MapPositionValue mapPosition;

    public MainMapPresenter(MainActivity activity){
        this.okapiService = new OkapiService();
        this.mapInteractor = new MapInteractor();
        this.markerList = new CacheMarkerCollectionModel();
        this.mainActivity = activity;
        this.downloadTask = false;
        this.isGPS = false;
        this.isSattelite = false;
        this.isInfowindow = false;
    }

    public void sync(){
        this.syncProgressBar();
        this.applyCaches();
        this.setGpsMode(this.isGPS);
        this.setSatelliteMode(this.isSattelite);
        this.setInfowindow(this.selectedMarker);
        this.syncToolbar();
        if(this.isInfowindow){
            this.getActivity().showInfowindow(false);
        }
        if(this.mapPosition != null){
            this.mapInteractor.setMapPosition(this.mapPosition);
        }
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
                                getAndApplyCaches(null);
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                } catch (Exception e){
                    e.printStackTrace();
                    getAndApplyCaches(null);
                }
            } else {
                getAndApplyCaches(uuid);
            }
        } else {
            this.cancelDownloader();
            markerList.clear();
            mapFragment.getMap().clear();
            this.hideInfowindow();
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

    public void syncToolbar(){
        this.getActivity().setToolbarState(this.isInfowindow);
        ActionBar actionBar = this.getActivity().getSupportActionBar();
        String title = null;
        String subtitle = null;

        if(this.selectedMarker != null && this.isInfowindow){
            CacheMakerModel cache = this.markerList.getMarker(this.selectedMarker.getTitle());

            if(cache == null){
                return;
            }

            title = cache.getTitle();
            subtitle = cache.getCode();
        }

        if(actionBar != null){
            actionBar.setTitle(title);
            actionBar.setSubtitle(subtitle);
        }
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
        if(this.mapPosition != null){
            this.mapInteractor.setMapPosition(this.mapPosition);
        } else {
            this.preferencesService = new PreferencesService(this.getContext());

            if(this.preferencesService.isMapAutoposition()){
                this.mapInteractor.setLastMapPosition();
            } else {
                this.mapInteractor.setMapPosition(this.preferencesService.getMapPosition());
            }
        }
    }

    public void setLastLocation(@Nullable Location location){
        this.mapInteractor.setLastLocation(location);
    }

    public void onMarkerClick(Marker marker){
        if(!this.isInfowindow){
            this.isInfowindow = true;
            this.syncToolbar();
            this.getActivity().showInfowindow(true);
        }

        this.selectedMarker = marker;
        this.setInfowindow(marker);
    }

    public void onMapClick(){
        this.hideInfowindow();
    }

    public void hideInfowindow(){
        if(isInfowindow){
            this.isInfowindow = false;
            this.syncToolbar();
            this.getActivity().hideInfowindow();
            this.selectedMarker = null;
            this.getActivity().invalidateOptionsMenu();
        }
    }

    public void setInfowindow(@Nullable Marker marker){
        if(marker != null){
            CacheMakerModel cache = this.markerList.getMarker(marker.getTitle());

            if(cache == null){
                return;
            }

            LinearLayout infowindow = this.getActivity().getInfowindowLayout();
            TextView type = (TextView) infowindow.findViewById(R.id.infoCacheType);
            TextView size = (TextView) infowindow.findViewById(R.id.infoCacheSize);
            TextView owner = (TextView) infowindow.findViewById(R.id.infoCacheOwner);
            TextView found = (TextView) infowindow.findViewById(R.id.infoCacheLastfound);

            ActionBar actionBar = this.getActivity().getSupportActionBar();

            if(actionBar != null){
                actionBar.setTitle(cache.getTitle());
                actionBar.setSubtitle(cache.getCode());
            }

            this.getActivity().invalidateOptionsMenu();

            type.setText(cache.getType().getName());
            size.setText(cache.getSize().getName());
            owner.setText(cache.getOwner());
            found.setText(cache.getLastFound());
        }
    }

    public boolean isInfowindowOpen(){
        return  this.isInfowindow;
    }

    public void saveMapPosition(){
        GoogleMap map =  this.mapFragment.getMap();
        if(map != null){
            CameraPosition cameraPosition = map.getCameraPosition();

            this.mapPosition = new MapPositionValue(
                    cameraPosition.target,
                    cameraPosition.zoom
            );
        }
    }
}
