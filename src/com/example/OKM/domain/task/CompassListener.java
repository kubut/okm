package com.example.OKM.domain.task;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.Surface;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import com.example.OKM.domain.service.LocationHelper;
import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by Jakub on 08.10.2015.
 */
public class CompassListener implements SensorEventListener {
    private MainMapPresenter mainMapPresenter;
    private float currentDegree = 0f;

    public CompassListener(MainMapPresenter mainMapPresenter){
        this.mainMapPresenter = mainMapPresenter;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Integer degree = Math.round(sensorEvent.values[0]);
        degree = LocationHelper.getMarkerAzimuth(-degree);

        if(degree != null) {
            degree -= this.mainMapPresenter.getScreenRotation();

            RotateAnimation rotateAnimation = new RotateAnimation(
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
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
