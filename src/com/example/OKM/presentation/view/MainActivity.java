package com.example.OKM.presentation.view;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.OKM.R;

import com.example.OKM.data.IMainDrawerItemListFactory;
import com.example.OKM.data.MainDrawerActionItemListFactory;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.domain.model.MainDrawerItemModel;
import com.example.OKM.presentation.adapter.*;
import com.example.OKM.presentation.presenter.MainMapPresenter;
import com.google.android.gms.maps.*;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-07-12.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private SupportMapFragment map;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;
    private LinearLayout mDrawerManuLayout;
    private MainDrawerListAdapter drawerAdapter;
    private ArrayList<IMainDrawerItem> mainDrawerItemsList;
    private MainMapPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        if(presenter == null){
            presenter = new MainMapPresenter(this, map);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.main_layout);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        this.initializeDrawerMenu(mDrawerLayout, toolbar);
    }

    private void initializeDrawerMenu(DrawerLayout mDrawerLayout, Toolbar toolbar){
        IMainDrawerItemListFactory factory = new MainDrawerActionItemListFactory(presenter);
        mainDrawerItemsList = factory.getItemsList();

        mDrawerManuLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerListView = (ListView) mDrawerManuLayout.findViewById(R.id.left_drawer_list_view);
        drawerAdapter = new MainDrawerListAdapter(getApplicationContext(), mainDrawerItemsList);

        mDrawerListView.setAdapter(drawerAdapter);

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
        mDrawerListView.setOnItemClickListener(new SlideMenuClickListener());
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerAdapter.getItem(position).click();
            drawerAdapter.notifyDataSetChanged();
        }
    }

    public void onMapReady(GoogleMap map) {

    }
}