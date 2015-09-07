package com.example.OKM.domain.model;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-09-04.
 */
public class CacheMarkerCollectionModel {
    private ArrayList<CacheMakerModel> list;

    public CacheMarkerCollectionModel(){
        list = new ArrayList<>();
    }

    public void addCache(CacheMakerModel cache){
        this.list.add(cache);
    }

    public ArrayList<CacheMakerModel> getList(){
        return this.list;
    }

    public void clear(){
        this.list.clear();
    }
}
