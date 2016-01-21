package com.example.OKM.domain.task;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import com.example.OKM.domain.service.LocationHelper;
import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by Jakub on 17.01.2016
 */
public class CompassOrientationListener extends CompassListener implements SensorEventListener {
    public CompassOrientationListener(MainMapPresenter mainMapPresenter){
        super(mainMapPresenter);
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        this.update(Math.round(sensorEvent.values[0]));
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int i) {

    }
}
