package com.opencachingkubutmaps.domain.task;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.opencachingkubutmaps.presentation.presenter.MainMapPresenter;

/**
 * Created by Jakub on 17.01.2016
 */
public class CompassOrientationListener extends CompassListener implements SensorEventListener {
    public CompassOrientationListener(final MainMapPresenter mainMapPresenter){
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
