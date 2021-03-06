package com.opencachingkubutmaps.presentation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.presentation.presenter.SettingsPresenter;

/**
 * Created by kubut on 2015-08-17.
 */
public class SettingsFragment extends com.fnp.materialpreferences.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SettingsPresenter presenter;

    @Override
    public int addPreferencesFromResource() {
        return R.xml.settings;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);

        final Preference selectMap = this.findPreference("prefSelectMap");

        selectMap.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                final Intent intent = new Intent(SettingsFragment.this.getActivity().getApplicationContext(), SettingsMapActivity.class);
                SettingsFragment.this.startActivity(intent);
                return false;
            }
        });

        if(this.presenter == null){
            this.presenter = new SettingsPresenter(this);
        }

        this.presenter.syncPreferencesSummary();
    }

    @SuppressWarnings("SwitchStatementWithoutDefaultBranch")
    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if(this.isAdded() && (this.presenter != null)){
            this.presenter.syncPreferencesSummary();

            switch (key){
                case "prefUsername":
                    this.presenter.resetUuid();
                    if(this.getPreferenceManager().getSharedPreferences().getBoolean("prefHideFound", true)){
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
                case "prefCompass":
                    if(this.getPreferenceManager().getSharedPreferences().getBoolean("prefCompass", false)){
                        new MaterialDialog.Builder(this.getActivity())
                                .title(R.string.settings_compass_warning_label)
                                .content(R.string.settings_compass_warning)
                                .positiveText(R.string.settings_compass_warning_ok)
                                .show();
                    }
                    break;
            }
        }
    }
}
