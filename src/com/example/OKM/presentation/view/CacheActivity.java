package com.example.OKM.presentation.view;

import android.graphics.Typeface;
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
import com.example.OKM.domain.service.CacheRatingHelper;
import com.example.OKM.presentation.presenter.CachePresenter;
import com.example.OKM.presentation.adapter.ViewPagerAdapter;
import com.rey.material.widget.ProgressView;

public class CacheActivity extends AppCompatActivity {
    private CachePresenter presenter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout bottomPanel;
    private ProgressView progressView;
    private RelativeLayout tryAgainView;
    private String cacheCode;
    private ICacheTabs tabDetails, tabLogs, tabGallery;
    private Menu menu;
    private boolean loaded;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cache);

        final Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.tabLayout      = (TabLayout) this.findViewById(R.id.tabs);
        this.viewPager      = (ViewPager) this.findViewById(R.id.viewpager);
        this.progressView   = (ProgressView) this.findViewById(R.id.progressCircle);
        this.tryAgainView   = (RelativeLayout) this.findViewById(R.id.tryAgainView);
        this.bottomPanel    = (LinearLayout) this.findViewById(R.id.bottom_panel);

        this.presenter = (CachePresenter) this.getLastCustomNonConfigurationInstance();
        if(this.presenter == null){
            this.presenter = new CachePresenter(this);
        }

        this.cacheCode = null;
        String cacheName = null;
        String cachePosition = null;
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.cacheCode = extras.getString("code");
            cacheName = extras.getString("name");
            cachePosition = extras.getString("position");
        } else {
            this.onBackPressed();
        }

        this.setSupportActionBar(toolbar);

        assert this.getSupportActionBar() != null;

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(cacheName);
        this.getSupportActionBar().setSubtitle(this.cacheCode + " " + cachePosition);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CacheActivity.this.onBackPressed();
            }
        });

        this.switchToLoader();
        this.presenter.connectContext(this);
        this.presenter.loadCacheDetails(this.cacheCode);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance(){
        return this.presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.cache_details_actions, menu);
        this.menu = menu;

        if(this.loaded){
            this.initMenu();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
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

    @SuppressWarnings("UnusedParameters")
    public void loadCache(final View v){
        this.presenter.loadCacheDetails(this.cacheCode);
        this.switchToLoader();
    }

    @SuppressWarnings("UnusedParameters")
    public void showAttributes(final View v){
        this.presenter.showAttributes();
    }

    public void switchToCache(){
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

    private void switchToLoader(){
        this.progressView.setVisibility(View.VISIBLE);
        this.tryAgainView.setVisibility(View.GONE);
        this.viewPager.setVisibility(View.GONE);
        this.tabLayout.setVisibility(View.GONE);
        this.bottomPanel.setVisibility(View.GONE);

    }

    public void setCacheDetails(final CacheModel cacheModel){
        final TextView type         = (TextView) this.bottomPanel.findViewById(R.id.infoCacheType);
        final TextView size         = (TextView) this.bottomPanel.findViewById(R.id.infoCacheSize);
        final TextView owner        = (TextView) this.bottomPanel.findViewById(R.id.infoCacheOwner);
        final TextView attributes   = (TextView) this.bottomPanel.findViewById(R.id.infoCacheAttributes);
        final TextView rating       = (TextView) this.bottomPanel.findViewById(R.id.infoCacheRating);

        final Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        rating.setTypeface(font);

        if(!cacheModel.hasAttributes()){
            attributes.setVisibility(View.GONE);
        }

        this.setupViewPager(cacheModel.hasPhotos(), cacheModel.hasLogs());
        this.tabLayout.setupWithViewPager(this.viewPager);

        this.initMenu();

        type.setText(cacheModel.getType().getName());
        size.setText(cacheModel.getSize().getName());
        owner.setText(cacheModel.getOwner());

        this.tabDetails.setView(this, cacheModel);

        if(cacheModel.hasLogs()){
            this.tabLogs.setView(this, cacheModel);
        }

        if(cacheModel.hasPhotos()){
            this.tabGallery.setView(this, cacheModel);
        }

        CacheRatingHelper.setStars(cacheModel.getRating(), this, rating);

        this.loaded = true;
    }

    private void setupViewPager(final boolean gallery, final boolean logs) {
        this.initTabs();

        final ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment((Fragment) this.tabDetails, this.getString(R.string.cache_tabs_details));

        if(logs){
            adapter.addFragment((Fragment) this.tabLogs, this.getString(R.string.cache_tabs_logs));
        }

        if(gallery){
            adapter.addFragment((Fragment) this.tabGallery, this.getString(R.string.cache_tabs_gallery));
        }

        this.viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private void initTabs(){
        final String name = "android:switcher:" + R.id.viewpager + ":";
        this.tabDetails = (ICacheTabs) this.getSupportFragmentManager().findFragmentByTag(name+"0");
        this.tabLogs = (ICacheTabs) this.getSupportFragmentManager().findFragmentByTag(name+"1");
        this.tabGallery = (ICacheTabs) this.getSupportFragmentManager().findFragmentByTag(name+"2");

        if(this.tabDetails == null){
            this.tabDetails = new CacheDetailsFragment();
        }
        if(this.tabLogs == null){
            this.tabLogs = new CacheLogsFragment();
        }
        if(this.tabGallery == null){
            this.tabGallery = new CacheGalleryFragment();
        }
    }

    private void initMenu(){
        if(this.menu != null){
            this.menu.findItem(R.id.action_nav).setVisible(true);
            this.menu.findItem(R.id.action_maps).setVisible(true);
            this.menu.findItem(R.id.action_www).setVisible(true);

            if(this.presenter.isHint()){
                this.menu.findItem(R.id.action_hint).setVisible(true);
            }
        }
    }
}
