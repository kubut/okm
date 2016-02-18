package com.opencachingkubutmaps.presentation.presenter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.*;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.service.PreferencesService;
import com.opencachingkubutmaps.presentation.view.SettingsFragment;

/**
 * Created by kubut on 2015-08-27.
 */
public class SettingsPresenter {
    private final SettingsFragment settingsFragment;
    private final PreferencesService preferencesService;
    private final EditTextPreference usernamePreference;
    private final ListPreference serversList, compassModeList;
    private final Preference seletMapPosition;
    private final CheckBoxPreference compassSwitch;

    public SettingsPresenter(final SettingsFragment settingsFragment){
        this.settingsFragment = settingsFragment;
        this.preferencesService = new PreferencesService(this.settingsFragment.getActivity());

        final CheckBoxPreference isMapAutoposition = (CheckBoxPreference) this.settingsFragment.findPreference("prefMapAutoPosition");

        this.usernamePreference     = (EditTextPreference) this.settingsFragment.findPreference("prefUsername");
        this.serversList            = (ListPreference) this.settingsFragment.findPreference("prefServer");
        this.compassModeList        = (ListPreference) this.settingsFragment.findPreference("prefCompassMode");
        this.compassSwitch          = (CheckBoxPreference) this.settingsFragment.findPreference("prefCompass");
        this.seletMapPosition       = this.settingsFragment.findPreference("prefSelectMap");

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

    private boolean isCompassEnabled(){
        return this.preferencesService.isCompass();
    }

    private String getCompassModeSumamry(){
        return this.preferencesService.getCompassModeName();
    }

    private boolean isMagneticCompassAvaible(){
        final SensorManager sm = (SensorManager) this.settingsFragment.getActivity().getSystemService(Context.SENSOR_SERVICE);
        return (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) && (sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null);
    }

    private boolean isOrientationCompassAvaible(){
        final SensorManager sm = (SensorManager) this.settingsFragment.getActivity().getSystemService(Context.SENSOR_SERVICE);
        //noinspection deprecation
        return sm.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null;
    }

    public void syncPreferencesSummary(){
        this.usernamePreference.setSummary(this.getUsernameSummary());
        this.serversList.setSummary(this.getServerSummary());
        this.compassModeList.setSummary(this.getCompassModeSumamry());
        this.seletMapPosition.setEnabled(!this.isMapSelectDisabled());
        this.compassModeList.setEnabled(this.isCompassEnabled());
        this.compassSwitch.setEnabled(this.isMagneticCompassAvaible() || this.isOrientationCompassAvaible());
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
