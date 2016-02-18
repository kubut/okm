package com.opencachingkubutmaps.domain.model;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by kubut on 2015-09-04.
 */
public class CacheMarkerCollectionModel {
    private final HashMap<String, CacheMakerModel> list;

    public CacheMarkerCollectionModel(){
        this.list = new HashMap<>();
    }

    public Collection<CacheMakerModel> getList(){
        return this.list.values();
    }

    public void clear(){
        this.list.clear();
    }

    public void append(final HashMap<String, CacheMakerModel> listToAppend){
        this.list.putAll(listToAppend);
    }

    public boolean isEmpty(){
        return this.getList().isEmpty();
    }

    @Nullable
    public CacheMakerModel getMarker(final String code){
        return this.list.get(code);
    }
}
