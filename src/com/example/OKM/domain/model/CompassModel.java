package com.example.OKM.domain.model;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.domain.service.LocationHelper;
import com.example.OKM.presentation.presenter.InfowindowPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jakub on 30.11.2015.
 */
public class CompassModel {
    private InfowindowPresenter infowindowPresenter;
    private Boolean actual, compassMode;
    private TextView distance;
    private ImageView compass;
    private float degree;

    public CompassModel(InfowindowPresenter infowindowPresenter){
        this.infowindowPresenter = infowindowPresenter;
        this.compassMode = false;
        this.actual = false;
    }

    public void sync(ImageView compass, TextView distance){
        this.compass = compass;
        this.distance = distance;
        this.compassMode = null;
    }

    public void updateDistance(GoogleMap map, LatLng position){
        Location myLocation = map.getMyLocation();
        Location markerLocation = LocationHelper.getLocationFromLatLng(position);

        this.actual = myLocation != null;

        if(this.actual){
            this.distance.setText(LocationHelper.getDistance(myLocation, markerLocation));
        }
    }

    public void setColor(){
        int color = this.infowindowPresenter.getContext().getResources().getColor(R.color.colorPrimaryLight);
        PorterDuffColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        this.compass.setColorFilter(filter);
    }

    public void updateCompass(RotateAnimation animation, float degree){
        if(this.actual){
            this.infowindowPresenter.getActivity().animateCompass(animation);
            this.degree = degree;
        }
    }

    public void syncMode(){
        int color;

        if(this.compassMode != null && this.actual == this.compassMode){
            return;
        }

        this.compassMode = this.actual;

        if(this.actual){
            color = this.infowindowPresenter.getContext().getResources().getColor(R.color.textColorPrimary);
        } else {
            color = this.infowindowPresenter.getContext().getResources().getColor(R.color.colorPrimaryLight);
            this.compass.invalidate();
            this.distance.setText(this.infowindowPresenter.getContext().getString(R.string.label_no_gps));
        }

        PorterDuffColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        this.compass.setColorFilter(filter);
        this.distance.setTextColor(color);
    }

    public void reset(){
        int color = this.infowindowPresenter.getContext().getResources().getColor(R.color.colorPrimaryLight);

        PorterDuffColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        this.distance.setText(this.infowindowPresenter.getContext().getString(R.string.label_no_gps));
        this.compass.setColorFilter(filter);
        this.distance.setTextColor(color);

        RotateAnimation rotateAnimation = new RotateAnimation(
                this.degree,
                0,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);

        this.updateCompass(rotateAnimation, 0);

        this.degree = 0;
        this.actual = false;

    }
}
