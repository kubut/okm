package com.example.OKM.presentation.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.example.OKM.R;
import com.example.OKM.presentation.presenter.SettingsPresenter;

/**
 * Created by kubut on 2015-08-17.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    SettingsPresenter presenter;


    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);

        if(presenter == null){
            presenter = new SettingsPresenter(this);
        }

        presenter.syncPreferencesSummary();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(isAdded()){
            presenter.syncPreferencesSummary();
        }
    }
}
