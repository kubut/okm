package com.opencachingkubutmaps.domain.service;

import android.content.Context;
import androidx.annotation.Nullable;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.data.repositories.PreferencesRepository;
import com.opencachingkubutmaps.domain.model.CompassModel;
import com.opencachingkubutmaps.domain.valueObject.MapPositionValue;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by kubut on 2015-08-22.
 */
@SuppressWarnings("MethodMayBeStatic")
public class PreferencesService {
    private final PreferencesRepository repository;
    private final Context context;

    public PreferencesService(final Context context){
        this.repository = new PreferencesRepository(context);
        this.context = context;
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

    public int getLpc(){
        return this.isSaveMode() ? 35 : 70;
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

    public boolean isCompass(){
        return this.repository.isCompass();
    }

    public CompassModel.Mode getCompassMode(){
        if(this.repository.getCompassMode().equals("magnetic")){
            return CompassModel.Mode.MAGNETIC;
        } else {
            return CompassModel.Mode.ORIENTATION;
        }
    }

    public String getCompassModeName(){
        String modeName = "";
        final String mode = this.repository.getCompassMode();
        final String[] entries = this.context.getResources().getStringArray(R.array.settings_compass_mode_entries);
        final String[] values = this.context.getResources().getStringArray(R.array.settings_compass_mode_values);

        for(int i=0; i<values.length; i++){
            if(values[i].equals(mode)){
                modeName = entries[i];
            }
        }

        return modeName;
    }

    public String getLanguageCode(){
        return Locale.getDefault().getLanguage();
    }

    public String getServerName(){
        return this.repository.getServer();
    }

    public String getServerAPI(){
        return "https://www." + this.getServerName() + "/okapi/";
    }

    public long getLastRunTime(){
        return this.repository.getLastRunTime();
    }

    public void setLastRunTime(){
        this.repository.setLastRunTime(new java.util.Date().getTime());
    }

    public void setServerName(final String serverName){
        this.repository.setServer(serverName);
    }
}
