package com.example.OKM.presentation.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.OKM.R;
import com.example.OKM.presentation.presenter.SettingsMapPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by kubut on 2015-09-16.
 */
public class SettingsMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment map;
    private SettingsMapPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_map_activity);

        final Toolbar toolbar   = (Toolbar) findViewById(R.id.app_bar);
        map                     = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if(map != null){
            map.getMapAsync(this);
        }

        presenter = (SettingsMapPresenter) getLastCustomNonConfigurationInstance();
        if(presenter == null){
            presenter = new SettingsMapPresenter(this);
        }
        presenter.connectContext(this, map);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.settings_set_map_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_map_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return presenter;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        presenter.disconnectContext();
    }

    public void onMapReady(GoogleMap map) {
        this.presenter.setMapPosition();
    }

    public void cancel(){
        this.finish();
    }
}
