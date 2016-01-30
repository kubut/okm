package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CompassModel;
import com.example.OKM.domain.service.CacheRatingHelper;
import com.example.OKM.domain.service.PreferencesService;
import com.example.OKM.domain.task.CompassMagneticListener;
import com.example.OKM.domain.task.CompassOrientationListener;
import com.example.OKM.domain.task.TimerTask;
import com.example.OKM.presentation.view.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.concurrent.Callable;

/**
 * Created by Jakub on 11.10.2015.
 */
public class InfowindowPresenter {
    private final MainMapPresenter mainMapPresenter;
    private CacheMakerModel selectedMarker;
    private TextView type, size, owner, found, rating;
    private ActionBar actionBar;
    private SensorManager sensorManager;
    private CompassOrientationListener compassOrientationListener;
    private CompassMagneticListener compassMagneticListener;
    private Thread thread;
    private boolean showCompass;
    private final CompassModel compassModel;
    private final Sensor mAccelerometer;
    private final Sensor mMagnetometer;
    private final Sensor mOrientation;
    private CompassModel.Mode selectedCompassType;

    public InfowindowPresenter(final MainMapPresenter mainMapPresenter){
        this.mainMapPresenter = mainMapPresenter;
        this.compassModel = new CompassModel(this);
        this.sync();
        this.compassModel.setColor();
        this.mAccelerometer = this.getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mMagnetometer = this.getSensorManager().getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.mOrientation = this.getSensorManager().getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    public void sync(){
        final LinearLayout infowindow = this.mainMapPresenter.getActivity().getInfowindowLayout();
        final PreferencesService preferencesService = new PreferencesService(this.mainMapPresenter.getContext());

        this.type                   = (TextView) infowindow.findViewById(R.id.infoCacheType);
        this.size                   = (TextView) infowindow.findViewById(R.id.infoCacheSize);
        this.owner                  = (TextView) infowindow.findViewById(R.id.infoCacheOwner);
        this.found                  = (TextView) infowindow.findViewById(R.id.infoCacheLastfound);
        this.rating                 = (TextView) infowindow.findViewById(R.id.infoCacheRating);
        this.actionBar              = this.mainMapPresenter.getActivity().getSupportActionBar();
        this.showCompass            = preferencesService.isCompass();
        this.selectedCompassType    = preferencesService.getCompassMode();

        final Typeface font = Typeface.createFromAsset(this.getContext().getAssets(), "fontawesome-webfont.ttf");
        this.rating.setTypeface(font);

        this.compassModel.sync(
                this.showCompass,
                (ImageView) infowindow.findViewById(R.id.compass),
                (TextView) infowindow.findViewById(R.id.distance)
        );

        if(this.isOpen()){
            this.syncInfo();
            this.startLocationTask();
        }

        this.syncToolbar();
    }

    public void show(final Marker marker){
        if(!this.isOpen()){
            this.mainMapPresenter.getActivity().showInfowindow(true);
        }

        this.selectedMarker = this.mainMapPresenter.getMarkerList().getMarker(marker.getTitle());

        if(this.selectedMarker == null){
            return;
        }

        this.syncToolbar();
        this.syncInfo();
        this.startLocationTask();
        this.compassModel.syncMode();
    }

    public void close(){
        if(this.isOpen()){
            this.selectedMarker = null;
            this.stop();
            this.mainMapPresenter.getActivity().invalidateOptionsMenu();
            this.syncToolbar();
            this.mainMapPresenter.getActivity().hideInfowindow();
        }
    }

    public void stop(){
        this.stopLocationTask();
        this.compassModel.reset();
    }

    public void start(){
        if(this.isOpen()){
            this.startLocationTask();
        }
    }

    public boolean isOpen(){
        return this.selectedMarker != null;
    }

    public CompassModel getCompass(){
        return this.compassModel;
    }

    public MainActivity getActivity(){
        return this.mainMapPresenter.getActivity();
    }

    public Context getContext(){
        return this.mainMapPresenter.getContext();
    }

    @Nullable
    public String getSelectedMarkerCode(){
        if(this.selectedMarker != null){
            return this.selectedMarker.getCode();
        } else {
            return null;
        }
    }

    @Nullable
    public String getSelectedMarkerName(){
        if(this.selectedMarker != null){
            return this.selectedMarker.getTitle();
        } else {
            return null;
        }
    }

    @Nullable
    public String getSelectedMarkerPosition(){
        if(this.selectedMarker != null){
            return " [" + this.selectedMarker.getDMPosition() + "]";
        } else {
            return null;
        }
    }

    private void syncToolbar(){
        String title = null;
        String subtitle = null;

        this.mainMapPresenter.getActivity().setToolbarState(this.isOpen());
        this.actionBar = this.mainMapPresenter.getActivity().getSupportActionBar();

        if(this.selectedMarker != null){
            title = this.selectedMarker.getTitle();
            subtitle = this.selectedMarker.getCode() + " [" + this.selectedMarker.getDMPosition() + "]";
        }

        if(this.actionBar != null){
            this.actionBar.setTitle(title);
            this.actionBar.setSubtitle(subtitle);
        }
    }

    private void syncInfo(){
        this.syncToolbar();

        this.mainMapPresenter.getActivity().invalidateOptionsMenu();

        String lastFound = this.selectedMarker.getLastFound();

        if((lastFound != null) && !lastFound.equals("null")){
            lastFound = lastFound.substring(0,10);
        } else {
            lastFound = this.getContext().getResources().getString(R.string.label_last_found_none);
        }

        this.type.setText(this.selectedMarker.getType().getName());
        this.size.setText(this.selectedMarker.getSize().getName());
        this.owner.setText(this.selectedMarker.getOwner());
        this.found.setText(lastFound);

        CacheRatingHelper.setStars(this.selectedMarker.getRating(), this.getContext(), this.rating);
    }

    private void startLocationTask(){
        this.stopLocationTask();

        if(!this.mainMapPresenter.checkLocationPermission()){
            return;
        }

        if(this.showCompass){
            this.registerCompassListener();
        }

        final GoogleMap googleMap = this.mainMapPresenter.getGoogleMap();

        if(this.mainMapPresenter.isGPSEnabled() && (googleMap != null) && (this.selectedMarker != null)){
            if(!googleMap.isMyLocationEnabled()){
                googleMap.setMyLocationEnabled(true);
            }
            this.compassModel.updateDistance(googleMap, this.selectedMarker.getPosition());

            this.thread = new Thread(new TimerTask(
                    new Handler(),
                    500,
                    new Callable() {
                        @Override
                        public Object call() throws Exception {
                            InfowindowPresenter.this.compassModel.updateDistance(googleMap, InfowindowPresenter.this.selectedMarker.getPosition());
                            return null;
                        }
                    }
            ));
            this.thread.start();
        }

        this.compassModel.syncMode();
    }

    private void registerCompassListener(){
        switch(this.selectedCompassType){
            case MAGNETIC:
                this.getSensorManager().registerListener(
                        this.getCompassMagneticListener(),
                        this.mAccelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL
                );

                this.getSensorManager().registerListener(
                        this.getCompassMagneticListener(),
                        this.mMagnetometer,
                        SensorManager.SENSOR_DELAY_NORMAL
                );
                break;
            case ORIENTATION:
                this.getSensorManager().registerListener(
                        this.getCompassOrientationListener(),
                        this.getSensorManager().getDefaultSensor(Sensor.TYPE_ORIENTATION),
                        SensorManager.SENSOR_DELAY_GAME
                );
                break;
        }
    }

    private void stopLocationTask(){
        this.compassModel.syncMode();

        switch(this.selectedCompassType){
            case MAGNETIC:
                this.getSensorManager().unregisterListener(this.getCompassMagneticListener(), this.mMagnetometer);
                this.getSensorManager().unregisterListener(this.getCompassMagneticListener(), this.mAccelerometer);
                break;
            case ORIENTATION:
                this.getSensorManager().unregisterListener(this.getCompassOrientationListener(), this.mOrientation);
                break;
        }

        if(this.thread != null){
            this.thread.interrupt();
        }
    }

    private SensorManager getSensorManager(){
        if(this.sensorManager == null){
            this.sensorManager = (SensorManager) this.mainMapPresenter.getActivity().getSystemService(Context.SENSOR_SERVICE);
        }

        return this.sensorManager;
    }

    private CompassMagneticListener getCompassMagneticListener(){
        if(this.compassMagneticListener == null){
            this.compassMagneticListener = new CompassMagneticListener(this.mainMapPresenter, this.mAccelerometer, this.mMagnetometer);
        }

        return this.compassMagneticListener;
    }

    private CompassOrientationListener getCompassOrientationListener(){
        if(this.compassOrientationListener == null){
            this.compassOrientationListener = new CompassOrientationListener(this.mainMapPresenter);
        }

        return this.compassOrientationListener;
    }
}
