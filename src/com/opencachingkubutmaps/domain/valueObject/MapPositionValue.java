package com.opencachingkubutmaps.domain.valueObject;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kubut on 2015-09-16.
 */
public class MapPositionValue {
    private final LatLng position;
    private final float zoom;

    public MapPositionValue(final LatLng position, final float zoom){
        this.position = position;
        this.zoom = zoom;
    }

    public LatLng getPosition() {
        return this.position;
    }

    public float getZoom() {
        return this.zoom;
    }

    @Override
    public String toString(){
        final Double lat = this.position.latitude;
        final Double lng = this.position.longitude;

        return lat.toString() + ";" + lng.toString() + ";" + this.zoom;
    }
}
