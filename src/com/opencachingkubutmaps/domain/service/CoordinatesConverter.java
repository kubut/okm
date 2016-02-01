package com.opencachingkubutmaps.domain.service;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by Jakub on 12.01.2016
 * source: https://en.wikipedia.org/wiki/Geographic_coordinate_conversion
 */
public class CoordinatesConverter {
    public static String decimalToDM(final LatLng coordinates){
        return CoordinatesConverter.decimalToDM(coordinates.latitude) + ", " + CoordinatesConverter.decimalToDM(coordinates.longitude);
    }

    private static String decimalToDM(double coord){
        final String degrees, minutes;

        degrees = String.valueOf((int)coord);

        coord = (coord % 1) * 60;
        if (coord < 0) {
            coord *= -1;
        }

        final DecimalFormat decimalFormat = new DecimalFormat("00.000");
        minutes = decimalFormat.format(coord);

        return degrees + "° " + minutes + "'";
    }
}
