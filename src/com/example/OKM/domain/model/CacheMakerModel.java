package com.example.OKM.domain.model;

import android.content.Context;
import com.example.OKM.domain.valueObject.CacheTypeValue;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

/**
 * Created by kubut on 2015-09-04.
 */
public class CacheMakerModel {
    private LatLng position;
    private String title;
    private String code;
    private CacheTypeValue type;
    private String owner;
    private String lastFound;
    private String size;


    public CacheMakerModel(
            Context context,
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
        this.type = new CacheTypeValue(context, type);
        this.owner = owner;
        this.lastFound = lastFound;
    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other == this) return true;
        if(!(other instanceof CacheMakerModel)) return false;

        CacheMakerModel otherObj = (CacheMakerModel) other;

        return this.code.equals(otherObj.getCode());
    }

    @Override
    public int hashCode(){
        return this.code.hashCode();
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

    public CacheTypeValue getType() {
        return type;
    }

    public void setType(CacheTypeValue type) {
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
