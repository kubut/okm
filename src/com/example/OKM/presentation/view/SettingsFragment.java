package com.example.OKM.presentation.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.example.OKM.R;

/**
 * Created by kubut on 2015-08-17.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
