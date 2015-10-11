package com.example.OKM.domain.service;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Jakub on 03.10.2015.
 */
public  class LocationHelper {
    @Nullable
    private static Location myLocatino, markerLocation;
    @Nullable
    private static Integer lastDistance;

    public static Location getLocationFromLatLng(@NonNull LatLng position){
        Location markerLocation = new Location("");
        markerLocation.setLatitude(position.latitude);
        markerLocation.setLongitude(position.longitude);
        markerLocation.setTime(new Date().getTime());

        return markerLocation;
    }

    @Nullable
    public static String getDistance(@Nullable Location location1, @Nullable Location location2){
        if(location1 == null || location2 == null){
            return null;
        }

        myLocatino = location1;
        markerLocation = location2;

        String distanceString;
        int distance = (int) Math.floor(location1.distanceTo(location2));
        lastDistance = distance;

        if(distance >= 1000){
            double distanceDouble = (double) distance;
            distanceString = Double.toString(Math.round((distanceDouble/1000d) * 10d) / 10d) + "km";
        } else if(distance >= 500){
            distanceString = Integer.toString(Math.round(distance / 50) * 50) + "m";
        } else if(distance >= 100){
            distanceString = Integer.toString(Math.round(distance / 10) * 10) + "m";
        } else if(distance >= 50){
            distanceString = Integer.toString(Math.round(distance / 5) * 5) + "m";
        } else {
            distanceString = Integer.toString(Math.round(distance)) + "m";
        }

        return distanceString;
    }

    public static int getInterval(){
        double interval;

        if(lastDistance == null){
            interval = 2;
        } else if(lastDistance > 1000){
            interval = 10;
        } else if (lastDistance > 500){
            interval = 5;
        } else if (lastDistance > 150){
            interval = 2;
        } else {
            interval = 1;
        }

        return (int) (interval * 1000);
    }

    @Nullable
    public static Integer getMarkerAzimuth(int north){
        if(myLocatino == null || markerLocation == null){
            return null;
        }

        float degree = myLocatino.bearingTo(markerLocation);

        return Math.round((degree + north) % 360);
    }
}
