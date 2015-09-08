package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import com.example.OKM.R;
import com.example.OKM.domain.service.PreferencesService;
import com.example.OKM.presentation.view.SettingsFragment;

/**
 * Created by kubut on 2015-08-27.
 */
public class SettingsPresenter {
    SettingsFragment settingsFragment;
    PreferencesService preferencesService;
    EditTextPreference username;
    ListPreference languagesList, serversList;

    public SettingsPresenter(SettingsFragment settingsFragment){
        this.settingsFragment = settingsFragment;
        this.preferencesService = new PreferencesService(this.settingsFragment.getActivity());

        username = (EditTextPreference) this.settingsFragment.findPreference("prefUsername");
        languagesList = (ListPreference) this.settingsFragment.findPreference("prefLanguage");
        serversList = (ListPreference) this.settingsFragment.findPreference("prefServer");
    }

    public String getUsernameSummary(){
        String username = this.preferencesService.getUsername();

        if(username == null){
            username = this.settingsFragment.getString(R.string.settings_username_blank);
        }

        return username;
    }

    public String getLanguageSummary(){
        return this.preferencesService.getLanguageName();
    }

    public String getServerSummary(){
        return this.preferencesService.getServerName();
    }

    public void syncPreferencesSummary(){
        username.setSummary(this.getUsernameSummary());
        languagesList.setSummary(this.getLanguageSummary());
        serversList.setSummary(this.getServerSummary());
    }

    public void resetUuid(){
        this.preferencesService.setUuid(null);
    }
}
