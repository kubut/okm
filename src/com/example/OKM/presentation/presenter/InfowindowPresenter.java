package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Surface;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CompassModel;
import com.example.OKM.domain.service.LocationHelper;
import com.example.OKM.domain.task.CompassListener;
import com.example.OKM.domain.task.TimerTask;
import com.example.OKM.presentation.view.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.concurrent.Callable;

/**
 * Created by Jakub on 11.10.2015.
 */
public class InfowindowPresenter {
    private MainMapPresenter mainMapPresenter;
    private CacheMakerModel selectedMarker;
    private TextView type, size, owner, found;
    private ActionBar actionBar;
    private SensorManager sensorManager;
    private CompassListener compassListener;
    private Thread thread;
    private CompassModel compassModel;

    public InfowindowPresenter(MainMapPresenter mainMapPresenter){
        this.mainMapPresenter = mainMapPresenter;
        this.compassModel = new CompassModel(this);
        this.sync();
        this.compassModel.setColor();
    }

    public void sync(){
        LinearLayout infowindow = this.mainMapPresenter.getActivity().getInfowindowLayout();
        this.type = (TextView) infowindow.findViewById(R.id.infoCacheType);
        this.size = (TextView) infowindow.findViewById(R.id.infoCacheSize);
        this.owner = (TextView) infowindow.findViewById(R.id.infoCacheOwner);
        this.found = (TextView) infowindow.findViewById(R.id.infoCacheLastfound);
        this.actionBar = this.mainMapPresenter.getActivity().getSupportActionBar();

        this.compassModel.sync(
                (ImageView) infowindow.findViewById(R.id.compass),
                (TextView) infowindow.findViewById(R.id.distance)
        );

        if(this.isOpen()){
            this.syncInfo();
            this.startLocationTask();
        }

        this.syncToolbar();
    }

    public void show(Marker marker){
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

    private void syncToolbar(){
        String title = null;
        String subtitle = null;

        this.mainMapPresenter.getActivity().setToolbarState(this.isOpen());
        this.actionBar = this.mainMapPresenter.getActivity().getSupportActionBar();

        if(this.selectedMarker != null){
            title = this.selectedMarker.getTitle();
            subtitle = this.selectedMarker.getCode();
        }

        if(this.actionBar != null){
            this.actionBar.setTitle(title);
            this.actionBar.setSubtitle(subtitle);
        }
    }

    private void syncInfo(){
        if(actionBar != null){
            actionBar.setTitle(this.selectedMarker.getTitle());
            actionBar.setSubtitle(this.selectedMarker.getCode());
        }

        this.mainMapPresenter.getActivity().invalidateOptionsMenu();

        type.setText(this.selectedMarker.getType().getName());
        size.setText(this.selectedMarker.getSize().getName());
        owner.setText(this.selectedMarker.getOwner());
        found.setText(this.selectedMarker.getLastFound());
    }

    private void startLocationTask(){
        this.stopLocationTask();

        SensorManager sensorManager = this.getSensorManager();

        sensorManager.registerListener(
                this.getCompassListener(),
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME
        );

        final GoogleMap googleMap = this.mainMapPresenter.getGoogleMap();

        if(this.mainMapPresenter.isGPSEnabled() && googleMap != null && this.selectedMarker != null){
            if(!googleMap.isMyLocationEnabled()){
                googleMap.setMyLocationEnabled(true);
            }
            this.compassModel.updateDistance(googleMap, this.selectedMarker.getPosition());

            thread = new Thread(new TimerTask(
                    new Handler(),
                    500,
                    new Callable() {
                        @Override
                        public Object call() throws Exception {
                            compassModel.updateDistance(googleMap, selectedMarker.getPosition());
                            return null;
                        }
                    }
            ));
            thread.start();
        }

        this.compassModel.syncMode();
    }

    private void stopLocationTask(){
        this.compassModel.syncMode();
        this.getSensorManager().unregisterListener(this.getCompassListener());
        if(this.thread != null){
            thread.interrupt();
        }
    }

    private SensorManager getSensorManager(){
        if(this.sensorManager == null){
            this.sensorManager = (SensorManager) this.mainMapPresenter.getActivity().getSystemService(Context.SENSOR_SERVICE);
        }

        return this.sensorManager;
    }

    private CompassListener getCompassListener(){
        if(this.compassListener == null){
            this.compassListener = new CompassListener(this.mainMapPresenter);
        }

        return this.compassListener;
    }
}
