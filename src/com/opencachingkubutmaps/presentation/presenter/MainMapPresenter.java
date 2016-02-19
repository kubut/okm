package com.opencachingkubutmaps.presentation.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.data.services.OkapiCommunication;
import com.opencachingkubutmaps.domain.model.CacheMarkerCollectionModel;
import com.opencachingkubutmaps.domain.model.IMainDrawerItem;
import com.opencachingkubutmaps.domain.service.JsonTransformService;
import com.opencachingkubutmaps.domain.service.OkapiService;
import com.opencachingkubutmaps.domain.service.PreferencesService;
import com.opencachingkubutmaps.domain.valueObject.MapPositionValue;
import com.opencachingkubutmaps.presentation.interactor.MapInteractor;
import com.opencachingkubutmaps.presentation.view.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import org.json.JSONObject;

/**
 * Created by kubut on 2015-07-12.
 */
public final class MainMapPresenter {
    private static MainMapPresenter singleton;

    private boolean locationChecked;
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

    public void connectContext(final MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.preferencesService = new PreferencesService(this.getContext());
        this.infowindowPresenter.sync();
    }

    public void connectMap(final GoogleMap map){
        this.googleMap = map;
        this.mapInteractor.connectMap(this.googleMap, this.mainActivity.getApplicationContext());
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
        if(this.googleMap == null){
            return;
        }

        final LocationManager locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        final boolean isLocationGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean isLocationNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!this.locationChecked && (!isLocationGPS || !this.checkLocationPermission())){
            this.locationChecked = true;
            this.showLocationPermissionDialog();
        }

        if(!this.checkLocationPermission() || (!isLocationGPS && !isLocationNetwork)){
            this.locationChecked = false;
            this.setDrawerOptionState(this.getContext().getString(R.string.drawer_gps), false);
            return;
        }

        this.isGPS = modeOn;

        if(modeOn){
            this.infowindowPresenter.start();
        } else {
            this.infowindowPresenter.stop();
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
            this.cleanCaches();
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

    public void showToast(final String text){
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

    public boolean checkLocationPermission(){
        final String permission = "android.permission.ACCESS_FINE_LOCATION";
        final String permission2 = "android.permission.ACCESS_COARSE_LOCATION";
        final int res = this.getContext().checkCallingOrSelfPermission(permission);
        final int res2 = this.getContext().checkCallingOrSelfPermission(permission2);
        return (res == PackageManager.PERMISSION_GRANTED) && (res2 == PackageManager.PERMISSION_GRANTED);
    }

    public void cleanCaches(){
        this.cancelDownloader();
        this.markerList.clear();
        this.infowindowPresenter.close();
        this.googleMap.clear();
    }

    private void showLocationPermissionDialog(){
        new MaterialDialog.Builder(this.getActivity())
                .title(R.string.location_dialog_title)
                .content(R.string.location_dialog_content)
                .positiveText(R.string.location_dialog_ok)
                .negativeText(R.string.location_dialog_cancel)
                .negativeColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark))
                .onPositive(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which){
                        //noinspection UnqualifiedFieldAccess
                        locationChecked = true;
                        final Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainMapPresenter.this.getContext().startActivity(myIntent);
                    }
                })
                .show();
    }
}
