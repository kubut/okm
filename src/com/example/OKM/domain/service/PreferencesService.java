package com.example.OKM.domain.service;

import android.content.Context;
import android.support.annotation.Nullable;
import com.example.OKM.data.repositories.PreferencesRepository;
import com.example.OKM.domain.valueObject.MapPositionValue;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by kubut on 2015-08-22.
 */
public class PreferencesService {
    private final PreferencesRepository repository;

    public PreferencesService(final Context context){
        this.repository = new PreferencesRepository(context);
    }

    @Nullable
    public String getUsername(){
        String username = this.repository.getUsername();

        if(username != null){
            username = username.trim();

            return username.isEmpty() ? null : username;
        } else {
            return null;
        }
    }

    private boolean isSaveMode(){
        return this.repository.getSaveMode();
    }

    public boolean isMapAutoposition(){
        return this.repository.isMapAutoposition();
    }

    public int getCachesLimit(){
        return this.isSaveMode() ? 50 : 200;
    }

    public String getUuid() {
        return this.repository.getUuid();
    }

    public void setMapPosition(final MapPositionValue position){
        this.repository.setMapPosition(position.toString());
    }

    public MapPositionValue getMapPosition(){
        final String encoded = this.repository.getMapPosition();

        if(encoded == null){
            return null;
        }

        final String[] parts = encoded.split(";");

        final LatLng position = new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
        final float zoom = Float.parseFloat(parts[2]);

        return new MapPositionValue(position, zoom);
    }

    public void setUuid(final String uuid){
        this.repository.setUuid(uuid);
    }

    public boolean isHideFound(){
        return this.repository.isHideFound();
    }

    @SuppressWarnings("MethodMayBeStatic")
    public String getLanguageCode(){
        return Locale.getDefault().getLanguage();
    }

    public String getServerName(){
        return this.repository.getServer();
    }

    public String getServerAPI(){
        return "http://www." + this.getServerName() + "/okapi/";
    }
}
