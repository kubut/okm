package com.opencachingkubutmaps.presentation.interactor;

import android.content.Context;
import android.location.Location;
import androidx.annotation.Nullable;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.CacheMakerModel;
import com.opencachingkubutmaps.domain.model.CacheMarkerCollectionModel;
import com.opencachingkubutmaps.domain.valueObject.MapPositionValue;
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

    public void connectMap(final GoogleMap map, final Context context){
        this.map = map;
        this.context = context;
    }

    public void setLastLocation(@Nullable final Location lastLocation){
        this.lastLocation = lastLocation;
    }

    public void disconnectMap(){
        this.map = null;
        this.context = null;
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    public void setCachesOnMap(final CacheMarkerCollectionModel list){
        if(this.map != null){
            this.map.clear();

            for(final CacheMakerModel cache : list.getList()){
                this.map.addMarker(new MarkerOptions()
                                .position(cache.getPosition())
                                .title(cache.getCode())
                                .icon(BitmapDescriptorFactory.fromResource(cache.getType().getIcon()))
                );
            }
        }
    }

    public void setMapPosition(final MapPositionValue mapPosition){
        if((mapPosition != null) && (this.map != null)){
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapPosition.getPosition(), mapPosition.getZoom()));
        } else {
            this.setLastMapPosition();
        }
    }

    public void setLastMapPosition(){
        if((this.lastLocation != null) && (this.map != null)){
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.lastLocation.getLatitude(), this.lastLocation.getLongitude()), 10));
        } else {
            this.setDefaultPosition();
        }
    }

    private void setDefaultPosition(){
        if(this.map == null){
            return;
        }

        final String encoded = this.context.getString(R.string.default_map_position);

        final String[] parts = encoded.split(";");

        final LatLng position = new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
        final float zoom = Float.parseFloat(parts[2]);

        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }
}
