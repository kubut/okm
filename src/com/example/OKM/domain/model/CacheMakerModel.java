package com.example.OKM.domain.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kubut on 2015-09-04.
 */
public class CacheMakerModel {
    private LatLng position;
    private String title;
    private String code;
    private String type;
    private String owner;
    private String lastFound;
    private String size;


    public CacheMakerModel(
            LatLng position,
            String title,
            String code,
            String type,
            String owner,
            String lastFound,
            String size
    ){
        this.position = position;
        this.code = code;
        this.size = size;
        this.title = title;
        this.type = type;
        this.owner = owner;
        this.lastFound = lastFound;
    }


    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLastFound() {
        return lastFound;
    }

    public void setLastFound(String lastFound) {
        this.lastFound = lastFound;
    }
}
