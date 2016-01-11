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
    private final SettingsFragment settingsFragment;
    private final PreferencesService preferencesService;
    private final EditTextPreference usernamePreference;
    private final ListPreference serversList;
    private final Preference seletMapPosition;

    public SettingsPresenter(final SettingsFragment settingsFragment){
        this.settingsFragment = settingsFragment;
        this.preferencesService = new PreferencesService(this.settingsFragment.getActivity());

        this.usernamePreference = (EditTextPreference) this.settingsFragment.findPreference("prefUsername");
        this.serversList = (ListPreference) this.settingsFragment.findPreference("prefServer");
        final SwitchPreference isMapAutoposition = (SwitchPreference) this.settingsFragment.findPreference("prefMapAutoPosition");
        this.seletMapPosition = this.settingsFragment.findPreference("prefSelectMap");

        isMapAutoposition.setChecked(this.isMapSelectDisabled());
    }

    private String getUsernameSummary(){
        String username = this.preferencesService.getUsername();

        if(username == null){
            username = this.settingsFragment.getString(R.string.settings_username_blank);
        }

        return username;
    }

    private String getServerSummary(){
        return this.preferencesService.getServerName();
    }

    private boolean isMapSelectDisabled(){
        return this.preferencesService.isMapAutoposition();
    }

    public void syncPreferencesSummary(){
        this.usernamePreference.setSummary(this.getUsernameSummary());
        this.serversList.setSummary(this.getServerSummary());
        this.seletMapPosition.setEnabled(!this.isMapSelectDisabled());
    }

    public void resetUuid(){
        this.preferencesService.setUuid(null);
    }

    @SuppressWarnings("MethodMayBeStatic")
    public void resetMap(){
        final MainMapPresenter mainMapPresenter = MainMapPresenter.getInstanceIfExist();

        if(mainMapPresenter != null){
            mainMapPresenter.setCaches(false);
            mainMapPresenter.setDrawerOptionState(mainMapPresenter.getContext().getString(R.string.drawer_caches), false);
        }
    }

    public void reloadMap(){
        final MainMapPresenter mainMapPresenter = MainMapPresenter.getInstanceIfExist();

        if((mainMapPresenter != null) && mainMapPresenter.hasCaches()){
            this.resetMap();
            mainMapPresenter.setCaches(true);
        }
    }
}
