package com.example.OKM.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kubut on 2015-09-04.
 */
public class CacheMarkerCollectionModel {
    private Set<CacheMakerModel> list;

    public CacheMarkerCollectionModel(){
        list = new HashSet<>();
    }

    public void addCache(CacheMakerModel cache){
        this.list.add(cache);
    }

    public Set<CacheMakerModel> getList(){
        return this.list;
    }

    public void clear(){
        this.list.clear();
    }

    public void append(Set<CacheMakerModel> listToAppend){
        list.addAll(listToAppend);
    }

    public boolean isEmpty(){
        return this.getList().isEmpty();
    }
}
