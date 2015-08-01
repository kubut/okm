package com.example.OKM.presentation.view;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.example.OKM.R;

import com.google.android.gms.maps.*;

/**
 * Created by kubut on 2015-07-12.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private SupportMapFragment map;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.main_layout);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    public void onMapReady(GoogleMap map) {

    }
}