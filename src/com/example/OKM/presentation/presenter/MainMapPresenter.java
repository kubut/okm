package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.example.OKM.R;
import com.example.OKM.data.services.OkapiCommunication;
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
import org.json.JSONObject;

/**
 * Created by kubut on 2015-07-12.
 */
public final class MainMapPresenter {
    private static MainMapPresenter singleton;

    private MainActivity mainActivity;
    private final MapInteractor mapInteractor;
    private final CacheMarkerCollectionModel markerList;
    private AsyncTask markersDownloader, uuidDownloader;
    private Toast toast;
    private PreferencesService preferencesService;
    private boolean downloadTask;
    private boolean isGPS;
    private boolean isSattelite;
    private final boolean isInfowindow;
    private MapPositionValue mapPosition;
    private GoogleMap googleMap;
    private final InfowindowPresenter infowindowPresenter;

    private MainMapPresenter(final MainActivity activity){
        this.mapInteractor = new MapInteractor();
        this.markerList = new CacheMarkerCollectionModel();
        this.mainActivity = activity;
        this.downloadTask = false;
        this.isGPS = false;
        this.isSattelite = false;
        this.isInfowindow = false;
        this.infowindowPresenter = new InfowindowPresenter(this);
    }

    public static MainMapPresenter getInstance(final MainActivity activity){
        if(singleton == null){
            singleton = new MainMapPresenter(activity);
        }

        return singleton;
    }

    @Nullable
    public static MainMapPresenter getInstanceIfExist(){
        return singleton;
    }

    public void sync(){
        this.infowindowPresenter.sync();
        this.syncProgressBar();
        this.applyCaches();
        this.setGpsMode(this.isGPS);
        this.setSatelliteMode(this.isSattelite);
        if(this.isInfowindow){
            this.getActivity().showInfowindow(false);
        }
        if(this.mapPosition != null){
            this.mapInteractor.setMapPosition(this.mapPosition);
        }
    }

    public void connectContext(final MainActivity mainActivity, final SupportMapFragment map){
        this.mainActivity = mainActivity;
        this.preferencesService = new PreferencesService(this.getContext());

        if((map != null) && (map.getMap() != null)){
            this.googleMap = map.getMap();
            this.mapInteractor.connectMap(this.googleMap, mainActivity.getApplicationContext());
        }

        this.infowindowPresenter.sync();
    }

    public void disconnectContext(){
        this.infowindowPresenter.stop();
        this.mainActivity = null;
        this.mapInteractor.disconnectMap();
        this.googleMap = null;
    }

    public void setSatelliteMode(final boolean modeOn){
        this.isSattelite = modeOn;

        this.setDrawerOptionState(this.getContext().getString(R.string.drawer_satellite), modeOn);

        if(this.googleMap == null){
            return;
        }

        if(modeOn){
            this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void setGpsMode(final boolean modeOn){
        this.isGPS = modeOn;

        if(modeOn){
            this.infowindowPresenter.start();
        } else {
            this.infowindowPresenter.stop();
        }

        if(this.googleMap == null){
            return;
        }

        this.googleMap.setMyLocationEnabled(modeOn);
        this.setDrawerOptionState(this.getContext().getString(R.string.drawer_gps), modeOn);
    }

    public void setCaches(final boolean show){
        this.downloadTask = show;

        if(show){
            this.syncProgressBar();
            this.showToast(this.getContext().getString(R.string.toast_downloading));

            final String uuid = this.preferencesService.getUuid();

            if((uuid == null) && (this.preferencesService.getUsername() != null) && this.preferencesService.isHideFound()){
                try{
                    final String url = OkapiService.getUuidURL(this.mainActivity, this.preferencesService.getUsername());

                    this.uuidDownloader = new OkapiCommunication(){
                        @Override
                        public void onPostExecute(final String result){
                            try {
                                final String newUuid = JsonTransformService.getUuidByJson(new JSONObject(result));
                                MainMapPresenter.this.preferencesService.setUuid(newUuid);

                                MainMapPresenter.this.getAndApplyCaches(newUuid);
                            } catch (final Exception e){
                                MainMapPresenter.this.getAndApplyCaches(null);
                                e.printStackTrace();
                            }
                        }
                    }.execute(url);
                } catch (final Exception e){
                    e.printStackTrace();
                    this.getAndApplyCaches(null);
                }
            } else {
                this.getAndApplyCaches(uuid);
            }
        } else {
            this.cancelDownloader();
            this.markerList.clear();
            this.googleMap.clear();
            this.infowindowPresenter.close();
        }
    }

    private void getAndApplyCaches(final String uuid){
        final String url = OkapiService.getCacheCollectionURL(this.mainActivity, this.googleMap.getCameraPosition().target, uuid);

        this.markersDownloader = new OkapiCommunication(){
            @Override
            public void onPostExecute(final String result){
                try{
                    if(!this.isCancelled()){
                        MainMapPresenter.this.markerList.append(JsonTransformService.getCacheMarkersByJson(MainMapPresenter.this.getContext(), new JSONObject(result)));
                        MainMapPresenter.this.applyCaches();
                    }
                } catch (final Exception e){
                    MainMapPresenter.this.showToast(MainMapPresenter.this.getContext().getString(R.string.toast_downloading_error));
                    MainMapPresenter.this.setDrawerOptionState(MainMapPresenter.this.getContext().getString(R.string.drawer_caches), false);
                    e.printStackTrace();
                }
                MainMapPresenter.this.downloadTask = false;
                MainMapPresenter.this.syncProgressBar();
            }
        }.execute(url);
    }

    private void syncProgressBar(){
        this.getActivity().displayProgressBar(this.downloadTask);
    }

    private void applyCaches(){
        this.mapInteractor.setCachesOnMap(this.markerList);

        this.setDrawerOptionState(this.getContext().getString(R.string.drawer_caches), !this.markerList.isEmpty() || this.downloadTask);
    }

    public void hideDrawer(){
        this.mainActivity.hideNavigationDrawer();
    }

    private void showToast(final String text){
        if(this.toast != null){
            this.toast.cancel();
        }

        this.toast = Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT);
        this.toast.show();
    }

    public void setDrawerOptionState(final String key, final boolean state){
        if(this.getActivity().mainDrawerActionItemsList == null){
            return;
        }

        for(final IMainDrawerItem item : this.getActivity().mainDrawerActionItemsList){
            if(item.getTitle().equals(key)){
                item.setActive(state);
            }
        }
        this.getActivity().syncDrawerItems();
    }

    private void cancelDownloader(){
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

    public void setLastLocation(@Nullable final Location location){
        this.mapInteractor.setLastLocation(location);
    }

    public void saveMapPosition(){
        if(this.googleMap != null){
            final CameraPosition cameraPosition = this.googleMap.getCameraPosition();

            this.mapPosition = new MapPositionValue(
                    cameraPosition.target,
                    cameraPosition.zoom
            );
        }
    }

    public int getScreenRotation(){
        return this.getActivity().getWindowManager().getDefaultDisplay().getRotation();
    }

    public CacheMarkerCollectionModel getMarkerList(){
        return this.markerList;
    }

    public Context getContext(){
        return this.mainActivity.getApplicationContext();
    }

    public MainActivity getActivity() {
        return this.mainActivity;
    }

    public boolean isGPSEnabled(){
        return this.isGPS;
    }

    public InfowindowPresenter getInfowindowPresenter() {
        return this.infowindowPresenter;
    }

    @Nullable
    public GoogleMap getGoogleMap(){
        return this.googleMap;
    }

    public boolean hasCaches(){
        return !this.markerList.isEmpty() || this.downloadTask;
    }

}
