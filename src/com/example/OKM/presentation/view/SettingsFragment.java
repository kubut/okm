package com.example.OKM.presentation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
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
            this.presenter.syncPreferencesSummary();

            switch (key){
                case "prefUsername":
                    this.presenter.resetUuid();
                    if(getPreferenceManager().getSharedPreferences().getBoolean("prefHideFound", true)){
                        this.presenter.reloadMap();
                    }
                    break;
                case "prefServer":
                    this.presenter.resetUuid();
                    this.presenter.resetMap();
                    break;
                case "prefHideFound":
                    this.presenter.reloadMap();
                    break;
            }
        }
    }
}
