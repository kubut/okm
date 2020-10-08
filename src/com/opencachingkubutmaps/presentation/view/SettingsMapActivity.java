package com.opencachingkubutmaps.presentation.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.presentation.presenter.SettingsMapPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by kubut on 2015-09-16.
 */
public class SettingsMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SettingsMapPresenter presenter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.settings_map_activity);

        final Toolbar toolbar   = (Toolbar) this.findViewById(R.id.app_bar);
        final SupportMapFragment map = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);

        if(map != null){
            map.getMapAsync(this);
        }

        this.presenter = (SettingsMapPresenter) this.getLastCustomNonConfigurationInstance();
        if(this.presenter == null){
            this.presenter = new SettingsMapPresenter(this);
        }
        this.presenter.connectContext(this);

        this.setSupportActionBar(toolbar);

        assert this.getSupportActionBar() != null;

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setTitle(R.string.settings_set_map_title);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu items for use in the action bar
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.settings_map_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_set_map_ok:
                this.presenter.savePosition();
                this.finish();
                return true;
            case R.id.action_set_map_cancel:
                this.cancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance(){
        return this.presenter;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.presenter.disconnectContext();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.presenter.connectMap(map);
        this.presenter.setMapPosition();
    }

    private void cancel(){
        this.finish();
    }
}
