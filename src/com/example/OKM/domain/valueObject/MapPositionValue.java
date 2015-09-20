package com.example.OKM.domain.valueObject;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kubut on 2015-09-16.
 */
public class MapPositionValue {
    private LatLng position;
    private float zoom;

    public MapPositionValue(LatLng position, float zoom){
        this.position = position;
        this.zoom = zoom;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    @Override
    public String toString(){
        Double lat = this.position.latitude;
        Double lng = this.position.longitude;

        return lat.toString() + ";" + lng.toString() + ";" + this.zoom;
    }
}
