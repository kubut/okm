package com.example.OKM.presentation.view;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.example.OKM.R;
import com.example.OKM.data.factories.IMainDrawerItemListFactory;
import com.example.OKM.data.factories.MainDrawerActionItemListFactory;
import com.example.OKM.data.factories.MainDrawerIntentItemListFactory;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.presentation.adapter.MainDrawerListAdapter;
import com.example.OKM.presentation.presenter.MainMapPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-07-12.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private SupportMapFragment map;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerActionListView, mDrawerIntentListView;
    private RelativeLayout mDrawerManuLayout;
    private MainDrawerListAdapter drawerActionAdapter, drawerIntentAdapter;
    public ArrayList<IMainDrawerItem> mainDrawerActionItemsList, mainDrawerIntentItemsList;
    private MainMapPresenter presenter;
    private DrawerLayout mDrawerLayout;
    private ProgressView progressBar;
    private Animation animationUp, animationBottom;
    private LinearLayout infowindow;
    private Toolbar toolbar, toolbar_infowindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        this.toolbar            = (Toolbar) findViewById(R.id.app_bar);
        this.toolbar_infowindow = (Toolbar) findViewById(R.id.app_bar_infowindow);
        this.mDrawerLayout      = (DrawerLayout) findViewById(R.id.main_layout);
        this.infowindow         = (LinearLayout) findViewById(R.id.infowindow);
        this.map                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.progressBar        = (ProgressView) findViewById(R.id.progressBar);
        this.animationUp        = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        this.animationBottom    = AnimationUtils.loadAnimation(this, R.anim.up_bottom);

        this.infowindow.setVisibility(View.GONE);
        this.toolbar_infowindow.setVisibility(View.GONE);

        if(map != null){
            map.getMapAsync(this);
        }

        presenter = (MainMapPresenter) getLastCustomNonConfigurationInstance();
        if(presenter == null){
            presenter = new MainMapPresenter(this);
        }
        presenter.connectContext(this, map);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        this.initializeDrawerMenu(mDrawerLayout, toolbar);

        presenter.sync();
    }

    @Override
    public void onStop(){
        super.onStop();
        this.presenter.unregisterLocationTimer();
    }

    @Override
    public void onPause(){
        super.onPause();
        this.presenter.unregisterLocationTimer();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        this.presenter.registerLocationTimer();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance(){
        return presenter;
    }

    @Override
    public void onDestroy(){
        presenter.saveMapPosition();
        presenter.disconnectContext();
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        MenuItem downloadItem = menu.findItem(R.id.action_appendCaches);
        MenuItem infoItem = menu.findItem(R.id.action_cacheInfo);

        if(this.presenter.isInfowindowOpen()){
            downloadItem.setVisible(false);
            infoItem.setVisible(true);
        } else {
            downloadItem.setVisible(true);
            infoItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_appendCaches:
                this.presenter.setCaches(true);
                return true;
            case R.id.action_cacheInfo:
                this.goToCacheInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeDrawerMenu(DrawerLayout mDrawerLayout, Toolbar toolbar){
        IMainDrawerItemListFactory actionFactory = new MainDrawerActionItemListFactory(presenter);
        IMainDrawerItemListFactory intentFactory = new MainDrawerIntentItemListFactory(presenter);

        mainDrawerActionItemsList = actionFactory.getItemsList();
        mainDrawerIntentItemsList = intentFactory.getItemsList();

        mDrawerManuLayout = (RelativeLayout) findViewById(R.id.left_drawer);

        mDrawerActionListView = (ListView) mDrawerManuLayout.findViewById(R.id.left_drawer_action_list_view);
        mDrawerIntentListView = (ListView) mDrawerManuLayout.findViewById(R.id.left_drawer_intent_list_view);

        drawerActionAdapter = new MainDrawerListAdapter(getApplicationContext(), mainDrawerActionItemsList);
        drawerIntentAdapter = new MainDrawerListAdapter(getApplicationContext(), mainDrawerIntentItemsList);

        mDrawerActionListView.setAdapter(drawerActionAdapter);
        mDrawerIntentListView.setAdapter(drawerIntentAdapter);

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
        mDrawerActionListView.setOnItemClickListener(new SlideMenuActionClickListener());
        mDrawerIntentListView.setOnItemClickListener(new SlideMenuIntentClickListener());
    }

    private class SlideMenuActionClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerActionAdapter.getItem(position).click();
            drawerActionAdapter.notifyDataSetChanged();
        }
    }

    private class SlideMenuIntentClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerIntentAdapter.getItem(position).click();
            drawerIntentAdapter.notifyDataSetChanged();
        }
    }

    public void onMapReady(GoogleMap map) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        this.presenter.setLastLocation(location);
        this.presenter.setMapPosition();

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                presenter.onMarkerClick(marker);
                return true;
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                presenter.onMapClick();
            }
        });
    }

    public void goToSettings(){
        Intent intent = new Intent(this.getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void goToCacheInfo(){

    }

    public void hideNavigationDrawer(){
        this.mDrawerLayout.closeDrawers();
    }

    public void syncDrawerItems(){
        drawerActionAdapter.notifyDataSetChanged();
        drawerIntentAdapter.notifyDataSetChanged();
    }

    public void displayProgressBar(boolean show){
        if(show){
            this.progressBar.setVisibility(View.VISIBLE);
        } else {
            this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public LinearLayout getInfowindowLayout(){
        return this.infowindow;
    }

    public TextView getDistanceLabel(){
        return (TextView) this.infowindow.findViewById(R.id.distance);
    }

    public ImageView getCompass(){
        return (ImageView) this.infowindow.findViewById(R.id.compass);
    }

    public void setToolbarState(boolean info){
        if(info){
            this.toolbar.setVisibility(View.GONE);
            this.toolbar_infowindow.setVisibility(View.VISIBLE);
            setSupportActionBar(this.toolbar_infowindow);
        } else {
            this.toolbar_infowindow.setVisibility(View.GONE);
            this.toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(this.toolbar);
        }
    }

    public void hideInfowindow(){
        this.infowindow.startAnimation(this.animationBottom);
        this.infowindow.setVisibility(View.GONE);
    }

    public void showInfowindow(boolean animation){
        if(animation){
            this.infowindow.startAnimation(this.animationUp);
        }
        this.infowindow.setVisibility(View.VISIBLE);
    }

    public void animateCompass(Animation animation){
        this.getCompass().startAnimation(animation);
    }
}