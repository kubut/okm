package com.example.OKM.domain.service;

import android.content.Context;
import com.example.OKM.R;
import com.example.OKM.data.repositories.PreferencesRepository;

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

    public String getUsername(){
        return repository.getUsername();
    }

    public void setUsername(String username){
        repository.setUsername(username);
        repository.setUuid(-1);
    }

    public boolean isSaveMode(){
        return repository.getSaveMode();
    }

    public void setSaveMode(boolean saveMode){
        repository.setSaveMode(saveMode);
    }

    public int getCachesLimit(){
        return this.isSaveMode() ? 50 : 200;
    }

    public int getUuid() {
        return repository.getUuid();
    }

    public boolean isHideFound(){
        return repository.isHideFound();
    }

    public String getLanguageSymbol(){
        return repository.getLanguage();
    }

    public String getLanguageName(){
        String name = "";
        String language = this.getLanguageSymbol();
        String[] names = context.getResources().getStringArray(R.array.settings_languages_list_entries);
        String[] codes = context.getResources().getStringArray(R.array.settings_languages_list_values);

        for(int i=0; i<codes.length; i++){
            if(codes[i].equals(language)){
                name = names[i];
            }
        }

        return name;
    }

    public void setLanguage(String language){
        repository.setLanguage(language);
    }

    public String getServerName(){
        return repository.getServer();
    }

    public String getServerAPI(){
        return "http://www." + this.getServerName() + "/okapi/";
    }

    public void setServer(String server){
        repository.setServer(server);
    }
}
