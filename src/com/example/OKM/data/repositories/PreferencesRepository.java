package com.example.OKM.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kubut on 2015-08-22.
 */
public class PreferencesRepository {
    private static final String USERNAME = null;
    private static final boolean SAVE_MODE = false;
    private static final String SERVER = "opencaching.us";
    private static final String UUID = null;
    private static final boolean HIDE_FOUND = false;
    private static final String MAP_POSITION = null;
    private static final boolean MAP_AUTOPOSITION = true;
    private static final boolean COMPASS = false;
    private static final String COMPASS_MODE = "orientation";

    private final SharedPreferences sharedPref;

    public PreferencesRepository(final Context context){
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isHideFound(){
        return this.sharedPref.getBoolean("prefHideFound", HIDE_FOUND);
    }

    public boolean isMapAutoposition(){
        return this.sharedPref.getBoolean("prefMapAutoPosition", MAP_AUTOPOSITION);
    }

    public boolean isCompass() {
        return  this.sharedPref.getBoolean("prefCompass", COMPASS);
    }

    public String getCompassMode(){
        return this.sharedPref.getString("prefCompassMode", COMPASS_MODE);
    }

    public void setMapPosition(final String mapPosition){
        final SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putString("prefMapPosition", mapPosition);
        editor.apply();
    }

    public String getMapPosition(){
        return this.sharedPref.getString("prefMapPosition", MAP_POSITION);
    }

    public String getUsername(){
        return this.sharedPref.getString("prefUsername", USERNAME);
    }

    public String getUuid(){
        return this.sharedPref.getString("prefUudi", UUID);
    }

    public void setUuid(final String uuid){
        final SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putString("prefUudi", uuid);
        editor.apply();
    }

    public boolean getSaveMode(){
        return this.sharedPref.getBoolean("prefLimit", SAVE_MODE);
    }

    public String getServer(){
        return this.sharedPref.getString("prefServer", SERVER);
    }
}
