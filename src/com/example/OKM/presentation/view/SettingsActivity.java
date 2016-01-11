package com.example.OKM.presentation.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.example.OKM.R;

/**
 * Created by kubut on 2015-08-05.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.settings_activity);

        final Toolbar toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        this.setSupportActionBar(toolbar);

        assert this.getSupportActionBar() != null;

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(R.string.drawer_settings);

        this.getFragmentManager().beginTransaction().replace(R.id.settingsFrame, new SettingsFragment()).commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SettingsActivity.this.onBackPressed();
            }
        });
    }

}
