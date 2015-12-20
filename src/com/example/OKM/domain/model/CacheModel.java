package com.example.OKM.domain.model;

import com.example.OKM.domain.valueObject.CacheSizeValue;
import com.example.OKM.domain.valueObject.CacheTypeValue;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jakub on 05.12.2015.
 */
//attr_codes
public class CacheModel {
    private String code, name, url, owner, description, hint, attrs;
    private LatLng location;
    private CacheTypeValue type;
    private CacheSizeValue size;

    public boolean isHint(){
        return this.hint != null && !this.hint.equals("");
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public String getAttrs() {
        return attrs;
    }
    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }
    public LatLng getLocation() {
        return location;
    }
    public void setLocation(LatLng location) {
        this.location = location;
    }
    public CacheTypeValue getType() {
        return type;
    }
    public void setType(CacheTypeValue type) {
        this.type = type;
    }
    public CacheSizeValue getSize() {
        return size;
    }
    public void setSize(CacheSizeValue size) {
        this.size = size;
    }
}
