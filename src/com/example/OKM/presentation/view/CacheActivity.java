package com.example.OKM.presentation.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.domain.model.CacheModel;
import com.example.OKM.presentation.presenter.CachePresenter;
import com.example.OKM.presentation.adapter.ViewPagerAdapter;
import com.rey.material.widget.ProgressView;

public class CacheActivity extends AppCompatActivity {
    private CachePresenter presenter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout bottomPanel;
    private ProgressView progressView;
    private RelativeLayout tryAgainView;
    private String cacheCode;
    private ICacheTabs tabDetails, tabLogs, tabGallery;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);

        this.toolbar        = (Toolbar) findViewById(R.id.toolbar);
        this.tabLayout      = (TabLayout) findViewById(R.id.tabs);
        this.viewPager      = (ViewPager) findViewById(R.id.viewpager);
        this.progressView   = (ProgressView) findViewById(R.id.progressCircle);
        this.tryAgainView   = (RelativeLayout) findViewById(R.id.tryAgainView);
        this.bottomPanel    = (LinearLayout) findViewById(R.id.bottom_panel);

        this.presenter = (CachePresenter) getLastCustomNonConfigurationInstance();
        if(this.presenter == null){
            this.presenter = new CachePresenter(this);
        }
        this.presenter.connectContext(this);

        this.cacheCode = null;
        String cacheName = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.cacheCode = extras.getString("code");
            cacheName = extras.getString("name");
        } else {
            onBackPressed();
        }

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(cacheName);
        getSupportActionBar().setSubtitle(this.cacheCode);

        this.tabDetails = new CacheDetailsFragment();
        this.tabLogs = new CacheLogsFragment();
        this.tabGallery = new CacheGalleryFragment();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.presenter.loadCacheDetails(this.cacheCode);
        this.switchToLoader();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance(){
        return presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cache_details_actions, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nav:
                this.presenter.goToNavigation();
                return true;
            case R.id.action_maps:
                this.presenter.goToGoogleMaps();
                return true;
            case R.id.action_www:
                this.presenter.goToWebsite();
                return true;
            case R.id.action_hint:
                this.presenter.showHint();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.presenter.disconnectContext();
    }

    public void loadCache(View v){
        this.presenter.loadCacheDetails(this.cacheCode);
        this.switchToLoader();
    }

    public void showAttributes(View v){
        this.presenter.showAttributes();
    }

    public void switchToCache(CacheModel cacheModel){
        setupViewPager(this.viewPager, cacheModel.hasPhotos());
        tabLayout.setupWithViewPager(this.viewPager);

        this.progressView.setVisibility(View.GONE);
        this.tryAgainView.setVisibility(View.GONE);
        this.viewPager.setVisibility(View.VISIBLE);
        this.tabLayout.setVisibility(View.VISIBLE);
        this.bottomPanel.setVisibility(View.VISIBLE);
    }

    public void switchToTryAgain(){
        this.progressView.setVisibility(View.GONE);
        this.tryAgainView.setVisibility(View.VISIBLE);
        this.viewPager.setVisibility(View.GONE);
        this.tabLayout.setVisibility(View.GONE);
        this.bottomPanel.setVisibility(View.GONE);

    }

    public void switchToLoader(){
        this.progressView.setVisibility(View.VISIBLE);
        this.tryAgainView.setVisibility(View.GONE);
        this.viewPager.setVisibility(View.GONE);
        this.tabLayout.setVisibility(View.GONE);
        this.bottomPanel.setVisibility(View.GONE);

    }

    public void setCacheDetails(CacheModel cacheModel){
        TextView type   = (TextView) this.bottomPanel.findViewById(R.id.infoCacheType);
        TextView size   = (TextView) this.bottomPanel.findViewById(R.id.infoCacheSize);
        TextView owner  = (TextView) this.bottomPanel.findViewById(R.id.infoCacheOwner);

        if(this.menu != null){
            this.menu.findItem(R.id.action_nav).setVisible(true);
            this.menu.findItem(R.id.action_maps).setVisible(true);
            this.menu.findItem(R.id.action_www).setVisible(true);

            if(cacheModel.isHint()){
                this.menu.findItem(R.id.action_hint).setVisible(true);
            }
        }

        type.setText(cacheModel.getType().getName());
        size.setText(cacheModel.getSize().getName());
        owner.setText(cacheModel.getOwner());

        this.getTabDetails().setView(this, cacheModel);
        this.getTabLogs().setView(this, cacheModel);
        this.getTabGallery().setView(this, cacheModel);
    }

    public ICacheTabs getTabDetails(){
        return this.tabDetails;
    }

    public ICacheTabs getTabLogs(){
        return this.tabLogs;
    }

    public ICacheTabs getTabGallery(){
        return this.tabGallery;
    }

    private void setupViewPager(ViewPager viewPager, boolean gallery) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment((Fragment) this.tabDetails, getString(R.string.cache_tabs_details));
        adapter.addFragment((Fragment) this.tabLogs, getString(R.string.cache_tabs_logs));

        if(gallery){
            adapter.addFragment((Fragment) this.tabGallery, getString(R.string.cache_tabs_gallery));
        }

        viewPager.setAdapter(adapter);
    }
}
