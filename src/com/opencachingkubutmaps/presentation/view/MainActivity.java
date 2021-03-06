package com.opencachingkubutmaps.presentation.view;

import android.Manifest;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.data.factories.IMainDrawerItemListFactory;
import com.opencachingkubutmaps.data.factories.MainDrawerActionItemListFactory;
import com.opencachingkubutmaps.data.factories.MainDrawerIntentItemListFactory;
import com.opencachingkubutmaps.domain.model.FirstRunModel;
import com.opencachingkubutmaps.domain.model.IMainDrawerItem;
import com.opencachingkubutmaps.presentation.adapter.MainDrawerListAdapter;
import com.opencachingkubutmaps.presentation.adapter.NoInfowindowAdapter;
import com.opencachingkubutmaps.presentation.presenter.MainMapPresenter;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-07-12.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActionBarDrawerToggle mDrawerToggle;
    private MainDrawerListAdapter drawerActionAdapter, drawerIntentAdapter;
    public ArrayList<IMainDrawerItem> mainDrawerActionItemsList;
    private MainMapPresenter presenter;
    private DrawerLayout mDrawerLayout;
    private ProgressView progressBar;
    private Animation animationUp, animationBottom;
    private LinearLayout infowindow;
    private Toolbar toolbar, toolbar_infowindow;
    private boolean doubleBackToExitPressedOnce;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);

        this.toolbar = (Toolbar) this.findViewById(R.id.app_bar);
        this.toolbar_infowindow = (Toolbar) this.findViewById(R.id.app_bar_infowindow);
        this.mDrawerLayout = (DrawerLayout) this.findViewById(R.id.main_layout);
        this.infowindow = (LinearLayout) this.findViewById(R.id.infowindow);
        final SupportMapFragment map = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        this.progressBar = (ProgressView) this.findViewById(R.id.progressBar);
        this.animationUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        this.animationBottom = AnimationUtils.loadAnimation(this, R.anim.up_bottom);

        this.infowindow.setVisibility(View.GONE);
        this.toolbar_infowindow.setVisibility(View.GONE);

        if (map != null) {
            map.getMapAsync(this);
        }

        this.presenter = (MainMapPresenter) this.getLastCustomNonConfigurationInstance();
        if (this.presenter == null) {
            this.presenter = MainMapPresenter.getInstance(this);
        }
        this.presenter.connectContext(this);

        this.setSupportActionBar(this.toolbar);

        assert this.getSupportActionBar() != null;

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setTitle(null);

        this.initializeDrawerMenu(this.mDrawerLayout, this.toolbar);

        this.presenter.sync();

        if (!this.presenter.checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.presenter.setGpsMode(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.presenter.getInfowindowPresenter().stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.presenter.getInfowindowPresenter().stop();
    }

    @Override
    public void onResume() {
        super.onResume();

        FirstRunModel.showIfFirstTime(this);

        if (this.presenter.getInfowindowPresenter().isOpen()) {
            this.presenter.getInfowindowPresenter().sync();
            this.showInfowindow(false);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return this.presenter;
    }

    @Override
    public void onDestroy() {
        this.presenter.saveMapPosition();
        this.presenter.disconnectContext();
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        final MenuItem downloadItem = menu.findItem(R.id.action_appendCaches);
        final MenuItem infoItem = menu.findItem(R.id.action_cacheInfo);

        if (this.presenter.getInfowindowPresenter().isOpen()) {
            downloadItem.setVisible(false);
            infoItem.setVisible(true);
        } else {
            downloadItem.setVisible(true);
            infoItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_appendCaches:
                this.presenter.setCaches(true);
                return true;
            case R.id.action_cacheInfo:
                this.goToCacheInfo();
                return true;
            case android.R.id.home:
                if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    this.mDrawerLayout.closeDrawers();
                } else {
                    this.mDrawerLayout.openDrawer(GravityCompat.START);
                }
                this.mDrawerToggle.syncState();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            this.presenter.cleanCaches();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        this.presenter.showToast(this.getString(R.string.toast_click_again_to_leave));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //noinspection UnqualifiedFieldAccess
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        final LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        final Criteria criteria = new Criteria();
        final String bestProvider = locationManager.getBestProvider(criteria, false);

        Location location = null;

        this.presenter.connectMap(map);

        if (this.presenter.checkLocationPermission()) {
            location = locationManager.getLastKnownLocation(bestProvider);
        }

        this.presenter.setLastLocation(location);
        this.presenter.setMapPosition();

        if (this.presenter.checkLocationPermission()) {
            this.presenter.setGpsMode(true);
        }

        map.setInfoWindowAdapter(new NoInfowindowAdapter(this));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                MainActivity.this.presenter.getInfowindowPresenter().show(marker);
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                MainActivity.this.presenter.getInfowindowPresenter().close();
            }
        });

        map.setBuildingsEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private void initializeDrawerMenu(final DrawerLayout mDrawerLayout, final Toolbar toolbar) {
        final IMainDrawerItemListFactory actionFactory = new MainDrawerActionItemListFactory(this.presenter);
        final IMainDrawerItemListFactory intentFactory = new MainDrawerIntentItemListFactory(this.presenter);

        this.mainDrawerActionItemsList = actionFactory.getItemsList();
        final ArrayList<IMainDrawerItem> mainDrawerIntentItemsList = intentFactory.getItemsList();

        final RelativeLayout mDrawerManuLayout = (RelativeLayout) this.findViewById(R.id.left_drawer);

        final ListView mDrawerActionListView = (ListView) mDrawerManuLayout.findViewById(R.id.left_drawer_action_list_view);
        final ListView mDrawerIntentListView = (ListView) mDrawerManuLayout.findViewById(R.id.left_drawer_intent_list_view);

        this.drawerActionAdapter = new MainDrawerListAdapter(this.getApplicationContext(), this.mainDrawerActionItemsList);
        this.drawerIntentAdapter = new MainDrawerListAdapter(this.getApplicationContext(), mainDrawerIntentItemsList);

        mDrawerActionListView.setAdapter(this.drawerActionAdapter);
        mDrawerIntentListView.setAdapter(this.drawerIntentAdapter);

        this.mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(final View view) {
                super.onDrawerClosed(view);
                MainActivity.this.invalidateOptionsMenu();
                this.syncState();
            }

            @Override
            public void onDrawerOpened(final View drawerView) {
                super.onDrawerOpened(drawerView);
                MainActivity.this.invalidateOptionsMenu();
                this.syncState();
            }

            @Override
            public void onDrawerSlide(final View drawerView, final float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }
        };

        this.mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(this.mDrawerToggle);
        mDrawerActionListView.setOnItemClickListener(new SlideMenuActionClickListener());
        mDrawerIntentListView.setOnItemClickListener(new SlideMenuIntentClickListener());
    }

    private class SlideMenuActionClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            MainActivity.this.drawerActionAdapter.getItem(position).click();
            MainActivity.this.drawerActionAdapter.notifyDataSetChanged();
        }
    }

    private class SlideMenuIntentClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            MainActivity.this.drawerIntentAdapter.getItem(position).click();
            MainActivity.this.drawerIntentAdapter.notifyDataSetChanged();
        }
    }

    public void goToSettings() {
        final Intent intent = new Intent(this.getApplicationContext(), SettingsActivity.class);
        this.startActivity(intent);
    }

    public void goToCacheInfo() {
        final String code = this.presenter.getInfowindowPresenter().getSelectedMarkerCode();
        final String name = this.presenter.getInfowindowPresenter().getSelectedMarkerName();
        final String position = this.presenter.getInfowindowPresenter().getSelectedMarkerPosition();

        if ((code != null) && (name != null) && (position != null)) {
            final Intent intent = new Intent(this.getApplicationContext(), CacheActivity.class);
            intent.putExtra("code", code);
            intent.putExtra("name", name);
            intent.putExtra("position", position);
            this.startActivity(intent);
        }
    }

    public void hideNavigationDrawer() {
        this.mDrawerLayout.closeDrawers();
    }

    public void syncDrawerItems() {
        this.drawerActionAdapter.notifyDataSetChanged();
        this.drawerIntentAdapter.notifyDataSetChanged();
    }

    public void displayProgressBar(final boolean show) {
        if (show) {
            this.progressBar.setVisibility(View.VISIBLE);
        } else {
            this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public LinearLayout getInfowindowLayout() {
        return this.infowindow;
    }

    private ImageView getCompass() {
        return (ImageView) this.infowindow.findViewById(R.id.compass);
    }

    public void setToolbarState(final boolean info) {
        if (info) {
            this.toolbar.setVisibility(View.GONE);
            this.toolbar_infowindow.setVisibility(View.VISIBLE);
            this.setSupportActionBar(this.toolbar_infowindow);
        } else {
            this.toolbar_infowindow.setVisibility(View.GONE);
            this.toolbar.setVisibility(View.VISIBLE);
            this.setSupportActionBar(this.toolbar);
        }
    }

    public void hideInfowindow() {
        this.infowindow.startAnimation(this.animationBottom);
        this.infowindow.setVisibility(View.GONE);
    }

    public void showInfowindow(final boolean animation) {
        if (animation) {
            this.infowindow.startAnimation(this.animationUp);
        }
        this.infowindow.setVisibility(View.VISIBLE);
    }

    public void animateCompass(final Animation animation) {
        this.presenter.getInfowindowPresenter().getCompass().syncMode();
        this.getCompass().startAnimation(animation);
    }
}