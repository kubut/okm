package com.example.OKM.presentation.interactor;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import com.example.OKM.R;
import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CacheMarkerCollectionModel;
import com.example.OKM.domain.valueObject.MapPositionValue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kubut on 2015-09-06.
 */
public class MapInteractor {
    private GoogleMap map;
    private Location lastLocation;
    private Context context;

    public void connectMap(GoogleMap map, Context context){
        this.map = map;
        this.context = context;
    }

    public void setLastLocation(@Nullable Location lastLocation){
        this.lastLocation = lastLocation;
    }

    public void disconnectMap(){
        this.map = null;
        this.context = null;
    }

    public void setCachesOnMap(CacheMarkerCollectionModel list){
        if(this.map != null){
            this.map.clear();

            for(CacheMakerModel cache : list.getList()){
                this.map.addMarker(new MarkerOptions()
                                .position(cache.getPosition())
                                .title(cache.getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(cache.getType().getIcon()))
                );
            }
        }
    }

    public void setMapPosition(MapPositionValue mapPosition){
        if(mapPosition != null){
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapPosition.getPosition(), mapPosition.getZoom()));
        } else {
            this.setLastMapPosition();
        }
    }

    public void setLastMapPosition(){
        if(lastLocation != null){
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 10));
        } else {
            this.setDefaultPosition();
        }
    }

    public void setDefaultPosition(){
        String encoded = this.context.getString(R.string.default_map_position);

        String[] parts = encoded.split(";");

        LatLng position = new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
        float zoom = Float.parseFloat(parts[2]);

        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }
}
