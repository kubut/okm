package com.opencachingkubutmaps.domain.model;

import com.opencachingkubutmaps.domain.valueObject.*;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Jakub on 05.12.2015.
 */

public class CacheModel {
    private String code;
    private String url;
    private String owner;
    private String description;
    private String hint;
    private LatLng location;
    private CacheTypeValue type;
    private CacheSizeValue size;
    private int rating;
    private final ArrayList<CacheAttributeValue> attrs;
    private final ArrayList<CacheLogValue> logs;
    private final ArrayList<CachePhotoValue> photos;

    public CacheModel(){
        this.attrs = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.photos = new ArrayList<>();
        this.rating = 0;
    }

    public void appendAttrs(final ArrayList<CacheAttributeValue> attrsToAppend){
        this.attrs.addAll(attrsToAppend);
    }

    public void appendLogs(final ArrayList<CacheLogValue> logsToAppend){
        this.logs.addAll(logsToAppend);
    }

    public boolean isHint(){
        return (this.hint != null) && !this.hint.isEmpty();
    }

    public boolean hasPhotos(){
        return !this.photos.isEmpty();
    }

    public void appendPhotos(final ArrayList<CachePhotoValue> photosToAppend){
        this.photos.addAll(photosToAppend);
    }

    public boolean hasAttributes(){
        return !this.attrs.isEmpty();
    }

    public boolean hasLogs(){
        return !this.logs.isEmpty();
    }

    public ArrayList<CachePhotoValue> getPhotos(){
        return this.photos;
    }
    public ArrayList<CacheAttributeValue> getAttrs(){
        return this.attrs;
    }
    public ArrayList<CacheLogValue> getLogs(){
        return this.logs;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(final String code) {
        this.code = code;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(final String url) {
        this.url = url;
    }
    public String getOwner() {
        return this.owner;
    }
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(final String description) {
        this.description = description;
    }
    public String getHint() {
        return this.hint;
    }
    public void setHint(final String hint) {
        this.hint = hint;
    }
    public LatLng getLocation() {
        return this.location;
    }
    public void setLocation(final LatLng location) {
        this.location = location;
    }
    public CacheTypeValue getType() {
        return this.type;
    }
    public void setType(final CacheTypeValue type) {
        this.type = type;
    }
    public CacheSizeValue getSize() {
        return this.size;
    }
    public void setSize(final CacheSizeValue size) {
        this.size = size;
    }
    public int getRating() {
        return this.rating;
    }
    public void setRating(final int rating) {
        this.rating = rating;
    }
}
