package com.opencachingkubutmaps.domain.service;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    public static Location getLocationFromLatLng(@NonNull final LatLng position){
        final Location markerLocation = new Location("");
        markerLocation.setLatitude(position.latitude);
        markerLocation.setLongitude(position.longitude);
        markerLocation.setTime(new Date().getTime());

        return markerLocation;
    }

    @SuppressWarnings({"IfStatementWithTooManyBranches", "StringConcatenationMissingWhitespace"})
    @Nullable
    public static String getDistance(@Nullable final Location location1, @Nullable final Location location2){
        if((location1 == null) || (location2 == null)){
            return null;
        }

        myLocatino = location1;
        markerLocation = location2;

        final String distanceString;
        final int distance = (int) Math.floor(location1.distanceTo(location2));
        lastDistance = distance;

        if(distance >= 1000){
            final double distanceDouble = (double) distance;
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

    @SuppressWarnings("IfStatementWithTooManyBranches")
    public static int getInterval(){
        final double interval;

        if(lastDistance == null){
            interval = 2;
        } else if(lastDistance > 1000){
            interval = 5;
        } else if (lastDistance > 500){
            interval = 3;
        } else if (lastDistance > 150){
            interval = 2;
        } else {
            interval = 1;
        }

        return (int) (interval * 1000);
    }

    @Nullable
    public static Integer getMarkerAzimuth(final int north){
        if((myLocatino == null) || (markerLocation == null)){
            return null;
        }

        final float degree = myLocatino.bearingTo(markerLocation);

        return Math.round((degree + north) % 360);
    }
}
