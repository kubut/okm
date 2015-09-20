package com.example.OKM.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.OKM.R;

/**
 * Created by kubut on 2015-08-22.
 */
public class PreferencesRepository {
    public static final String USERNAME = null;
    public static final boolean SAVE_MODE = false;
    public static final String LANGUAGE = "en_US";
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

    public void setHideFound(boolean hideFound){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("prefHideFound", hideFound);
        editor.apply();
    }

    public boolean isMapAutoposition(){
        return sharedPref.getBoolean("prefMapAutoPosition", MAP_AUTOPOSITION);
    }

    public void setMapAutoposition(boolean autoposition){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("prefMapAutoPosition", autoposition);
        editor.apply();
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
        String username = sharedPref.getString("prefUsername", USERNAME);

        if(username.trim().equals("")){
            username = USERNAME;
        }

        return username;
    }

    public void setUsername(String username){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("prefUsername", username);
        editor.apply();
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

    public void setSaveMode(boolean saveMode){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("prefLimit", saveMode);
        editor.apply();
    }

    public String getLanguage(){
        return sharedPref.getString("prefLanguage", LANGUAGE);
    }

    public void setLanguage(String language){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("prefLanguage", language);
        editor.apply();
    }

    public String getServer(){
        return sharedPref.getString("prefServer", SERVER);
    }

    public void setServer(String server){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("prefServer", server);
        editor.apply();
    }
}
