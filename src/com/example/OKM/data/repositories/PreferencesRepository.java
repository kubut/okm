package com.example.OKM.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kubut on 2015-08-22.
 */
public class PreferencesRepository {
    public static final String USERNAME = null;
    public static final boolean SAVE_MODE = false;
    public static final String SERVER = "opencaching.us";
    public static final String UUID = null;
    public static final boolean HIDE_FOUND = false;
    public static final String MAP_POSITION = null;
    public static final boolean MAP_AUTOPOSITION = true;

    Context context;
    SharedPreferences sharedPref;

    public PreferencesRepository(Context context){
        this.context = context;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    public boolean isHideFound(){
        return sharedPref.getBoolean("prefHideFound", HIDE_FOUND);
    }

    public boolean isMapAutoposition(){
        return sharedPref.getBoolean("prefMapAutoPosition", MAP_AUTOPOSITION);
    }

    public void setMapPosition(String mapPosition){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("prefMapPosition", mapPosition);
        editor.apply();
    }

    public String getMapPosition(){
        return sharedPref.getString("prefMapPosition", MAP_POSITION);
    }

    public String getUsername(){
        return sharedPref.getString("prefUsername", USERNAME);
    }

    public String getUuid(){
        return sharedPref.getString("prefUudi", UUID);
    }

    public void setUuid(String uuid){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("prefUudi", uuid);
        editor.apply();
    }

    public boolean getSaveMode(){
        return sharedPref.getBoolean("prefLimit", SAVE_MODE);
    }

    public String getServer(){
        return sharedPref.getString("prefServer", SERVER);
    }
}
