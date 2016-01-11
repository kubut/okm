package com.example.OKM.domain.task;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import com.example.OKM.domain.service.LocationHelper;
import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by Jakub on 08.10.2015.
 */
public class CompassListener implements SensorEventListener {
    private final MainMapPresenter mainMapPresenter;
    private float currentDegree;

    public CompassListener(final MainMapPresenter mainMapPresenter){
        this.mainMapPresenter = mainMapPresenter;
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        Integer degree = Math.round(sensorEvent.values[0]);
        //noinspection ConstantConditions
        degree = LocationHelper.getMarkerAzimuth(-degree);

        if(degree != null) {
            degree -= this.mainMapPresenter.getScreenRotation();

            final RotateAnimation rotateAnimation = new RotateAnimation(
                    this.currentDegree,
                    degree,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
            );

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);

            this.currentDegree = degree;

            this.mainMapPresenter.getInfowindowPresenter().getCompass().updateCompass(rotateAnimation, degree);
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int i) {

    }
}
