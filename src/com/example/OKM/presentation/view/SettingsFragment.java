package com.example.OKM.presentation.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import com.example.OKM.R;
import com.example.OKM.presentation.presenter.SettingsPresenter;

/**
 * Created by kubut on 2015-08-17.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    SettingsPresenter presenter;
    Preference selectMap;


    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);

        selectMap = findPreference("prefSelectMap");

        selectMap.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingsMapActivity.class);
                startActivity(intent);
                return false;
            }
        });

        if(presenter == null){
            presenter = new SettingsPresenter(this);
        }

        presenter.syncPreferencesSummary();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(isAdded()){
            presenter.syncPreferencesSummary();

            if(key.equals("prefUsername")){
                presenter.resetUuid();
            }
        }
    }
}
