package com.example.OKM.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.OKM.R;

import com.example.OKM.data.factories.IMainDrawerItemListFactory;
import com.example.OKM.data.factories.MainDrawerActionItemListFactory;
import com.example.OKM.data.factories.MainDrawerIntentItemListFactory;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.presentation.adapter.*;
import com.example.OKM.presentation.presenter.MainMapPresenter;
import com.google.android.gms.maps.*;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final Toolbar toolbar   = (Toolbar) findViewById(R.id.app_bar);
        this.mDrawerLayout      = (DrawerLayout) findViewById(R.id.main_layout);
        map                     = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.progressBar        = (ProgressView) findViewById(R.id.progressBar);

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
    public Object onRetainCustomNonConfigurationInstance(){
        return presenter;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        presenter.disconnectContext();
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

    }

    public void goToSettings(){
        Intent intent = new Intent(this.getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
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
}