package com.example.OKM.domain.task;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import com.example.OKM.domain.service.LocationHelper;
import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by Jakub on 17.01.2016
 */
abstract public class CompassListener {
    protected final MainMapPresenter mainMapPresenter;
    protected float currentDegree;

    public CompassListener(final MainMapPresenter mainMapPresenter){
        this.mainMapPresenter = mainMapPresenter;
    }

    protected void update(Integer degree){
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
}
