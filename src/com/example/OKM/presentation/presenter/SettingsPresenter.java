package com.example.OKM.presentation.presenter;

import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
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
    ListPreference serversList;
    SwitchPreference isMapAutoposition;
    Preference seletMapPosition;

    public SettingsPresenter(SettingsFragment settingsFragment){
        this.settingsFragment = settingsFragment;
        this.preferencesService = new PreferencesService(this.settingsFragment.getActivity());

        username = (EditTextPreference) this.settingsFragment.findPreference("prefUsername");
        serversList = (ListPreference) this.settingsFragment.findPreference("prefServer");
        isMapAutoposition = (SwitchPreference) this.settingsFragment.findPreference("prefMapAutoPosition");
        seletMapPosition = this.settingsFragment.findPreference("prefSelectMap");

        isMapAutoposition.setChecked(this.isMapSelectDisabled());
    }

    public String getUsernameSummary(){
        String username = this.preferencesService.getUsername();

        if(username == null){
            username = this.settingsFragment.getString(R.string.settings_username_blank);
        }

        return username;
    }

    public String getServerSummary(){
        return this.preferencesService.getServerName();
    }

    public boolean isMapSelectDisabled(){
        return this.preferencesService.isMapAutoposition();
    }

    public void syncPreferencesSummary(){
        username.setSummary(this.getUsernameSummary());
        serversList.setSummary(this.getServerSummary());
        seletMapPosition.setEnabled(!this.isMapSelectDisabled());
    }

    public void resetUuid(){
        this.preferencesService.setUuid(null);
    }
}
