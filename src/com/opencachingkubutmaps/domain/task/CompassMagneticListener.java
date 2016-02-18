package com.opencachingkubutmaps.domain.task;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.opencachingkubutmaps.presentation.presenter.MainMapPresenter;

/**
 * Created by Jakub on 08.10.2015.
 */
public class CompassMagneticListener extends CompassListener implements SensorEventListener {
    private final Sensor mAccelerometer;
    private final Sensor mMagnetometer;

    private final float[] mLastAccelerometer = new float[3];
    private final float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet;
    private boolean mLastMagnetometerSet;
    private final float[] mR = new float[9];
    private final float[] mOrientation = new float[3];

    public CompassMagneticListener(final MainMapPresenter mainMapPresenter, final Sensor mAccelerometer, final Sensor mMagnetometer){
        super(mainMapPresenter);
        this.mAccelerometer = mAccelerometer;
        this.mMagnetometer = mMagnetometer;
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        //noinspection ObjectEquality
        if (sensorEvent.sensor == this.mAccelerometer) {
            System.arraycopy(sensorEvent.values, 0, this.mLastAccelerometer, 0, sensorEvent.values.length);
            this.mLastAccelerometerSet = true;
        } else //noinspection ObjectEquality
            if (sensorEvent.sensor == this.mMagnetometer) {
            System.arraycopy(sensorEvent.values, 0, this.mLastMagnetometer, 0, sensorEvent.values.length);
            this.mLastMagnetometerSet = true;
        }
        if (this.mLastAccelerometerSet && this.mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(this.mR, null, this.mLastAccelerometer, this.mLastMagnetometer);
            SensorManager.getOrientation(this.mR, this.mOrientation);
            final float azimuthInDegress = (float) (Math.toDegrees(this.mOrientation[0]) + 360) % 360;

            this.update(Math.round(azimuthInDegress));
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int i) {

    }
}
