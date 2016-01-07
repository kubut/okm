package com.example.OKM.domain.service;

import android.content.Context;
import android.support.annotation.Nullable;
import com.example.OKM.R;
import com.example.OKM.data.repositories.PreferencesRepository;
import com.example.OKM.domain.valueObject.MapPositionValue;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by kubut on 2015-08-22.
 */
public class PreferencesService {
    PreferencesRepository repository;
    Context context;

    public PreferencesService(Context context){
        this.context = context;
        repository = new PreferencesRepository(context);
    }

    @Nullable
    public String getUsername(){
        String username = repository.getUsername();

        if(username != null){
            username = username.trim();

            return username.equals("") ? null : username;
        } else {
            return null;
        }
    }

    public boolean isSaveMode(){
        return repository.getSaveMode();
    }

    public boolean isMapAutoposition(){
        return repository.isMapAutoposition();
    }

    public int getCachesLimit(){
        return this.isSaveMode() ? 50 : 200;
    }

    public String getUuid() {
        return repository.getUuid();
    }

    public void setMapPosition(MapPositionValue position){
        repository.setMapPosition(position.toString());
    }

    public MapPositionValue getMapPosition(){
        String encoded = repository.getMapPosition();

        if(encoded == null){
            return null;
        }

        String[] parts = encoded.split(";");

        LatLng position = new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
        float zoom = Float.parseFloat(parts[2]);

        return new MapPositionValue(position, zoom);
    }

    public void setUuid(String uuid){
        repository.setUuid(uuid);
    }

    public boolean isHideFound(){
        return repository.isHideFound();
    }

    public String getLanguageCode(){
        return Locale.getDefault().getLanguage();
    }

    public String getServerName(){
        return repository.getServer();
    }

    public String getServerAPI(){
        return "http://www." + this.getServerName() + "/okapi/";
    }
}
