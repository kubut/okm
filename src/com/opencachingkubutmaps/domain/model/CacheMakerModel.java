package com.opencachingkubutmaps.domain.model;

import android.content.Context;
import com.opencachingkubutmaps.domain.service.CoordinatesConverter;
import com.opencachingkubutmaps.domain.valueObject.CacheSizeValue;
import com.opencachingkubutmaps.domain.valueObject.CacheTypeValue;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kubut on 2015-09-04.
 */
public class CacheMakerModel {
    private final LatLng position;
    private final String title;
    private final String code;
    private final CacheTypeValue type;
    private final String owner;
    private final String lastFound;
    private final CacheSizeValue size;
    private final int rating;

    public CacheMakerModel(
            final Context context,
            final LatLng position,
            final String title,
            final String code,
            final String type,
            final String owner,
            final String lastFound,
            final String size,
            final int rating
    ){
        this.position = position;
        this.code = code;
        this.size = new CacheSizeValue(context, size);
        this.title = title;
        this.type = new CacheTypeValue(context, type);
        this.owner = owner;
        this.lastFound = lastFound;
        this.rating = rating;
    }

    @Override
    public boolean equals(final Object other){
        if(other == null) return false;
        if(other == this) return true;
        if(!(other instanceof CacheMakerModel)) return false;

        final CacheMakerModel otherObj = (CacheMakerModel) other;

        return this.code.equals(otherObj.getCode());
    }

    @Override
    public int hashCode(){
        return this.code.hashCode();
    }

    public LatLng getPosition() {
        return this.position;
    }

    public String getDMPosition(){
        return CoordinatesConverter.decimalToDM(this.getPosition());
    }

    public String getTitle() {
        return this.title;
    }

    public String getCode() {
        return this.code;
    }

    public CacheTypeValue getType() {
        return this.type;
    }

    public CacheSizeValue getSize() {
        return this.size;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getLastFound() {
        return this.lastFound;
    }

    public int getRating(){
        return this.rating;
    }

}
