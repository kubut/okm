package com.example.OKM.presentation.interactor;

import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CacheMarkerCollectionModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kubut on 2015-09-06.
 */
public class MapInteractor  {
    public GoogleMap map;

    public void connectMap(GoogleMap map){
        this.map = map;
    }

    public void disconnectMap(){
        this.map = null;
    }

    public void setCachesOnMap(CacheMarkerCollectionModel list){
        if(this.map != null){
            for(CacheMakerModel cache : list.getList()){
                this.map.addMarker(new MarkerOptions()
                                .position(cache.getPosition())
                                .title(cache.getTitle())
                );
            }
        }
    }
}
