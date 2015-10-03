package com.example.OKM.domain.model;

import android.support.annotation.Nullable;

import java.util.*;

/**
 * Created by kubut on 2015-09-04.
 */
public class CacheMarkerCollectionModel {
    private HashMap<String, CacheMakerModel> list;

    public CacheMarkerCollectionModel(){
        list = new HashMap<>();
    }

    public void addCache(CacheMakerModel cache){
        this.list.put(cache.getCode(), cache);
    }

    public Collection<CacheMakerModel> getList(){
        return this.list.values();
    }

    public void clear(){
        this.list.clear();
    }

    public void append(HashMap<String, CacheMakerModel> listToAppend){
        list.putAll(listToAppend);
    }

    public boolean isEmpty(){
        return this.getList().isEmpty();
    }

    @Nullable
    public CacheMakerModel getMarker(String code){
        return this.list.get(code);
    }
}
