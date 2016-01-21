package com.example.OKM.domain.task;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.IntegerRes;
import android.view.Surface;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import com.example.OKM.domain.service.LocationHelper;
import com.example.OKM.presentation.presenter.MainMapPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 08.10.2015.
 */
public class CompassMagneticListener extends CompassListener implements SensorEventListener {
    private final Sensor mAccelerometer;
    private final Sensor mMagnetometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet;
    private boolean mLastMagnetometerSet;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    public CompassMagneticListener(final MainMapPresenter mainMapPresenter, final Sensor mAccelerometer, final Sensor mMagnetometer){
        super(mainMapPresenter);
        this.mAccelerometer = mAccelerometer;
        this.mMagnetometer = mMagnetometer;
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor == mAccelerometer) {
            System.arraycopy(sensorEvent.values, 0, mLastAccelerometer, 0, sensorEvent.values.length);
            mLastAccelerometerSet = true;
        } else if (sensorEvent.sensor == mMagnetometer) {
            System.arraycopy(sensorEvent.values, 0, mLastMagnetometer, 0, sensorEvent.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInDegress = (float) (Math.toDegrees(mOrientation[0]) + 360) % 360;

            this.update(Math.round(azimuthInDegress));
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int i) {

    }
}
