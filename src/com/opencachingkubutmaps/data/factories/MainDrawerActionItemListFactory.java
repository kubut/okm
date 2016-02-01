package com.opencachingkubutmaps.data.factories;

import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.IMainDrawerItem;
import com.opencachingkubutmaps.domain.model.MainDrawerItemModel;
import com.opencachingkubutmaps.presentation.presenter.MainMapPresenter;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-03.
 */
public class MainDrawerActionItemListFactory implements IMainDrawerItemListFactory {
    private final MainMapPresenter presenter;

    public MainDrawerActionItemListFactory(final MainMapPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public ArrayList<IMainDrawerItem> getItemsList() {
        final ArrayList<IMainDrawerItem> itemsList = new ArrayList<>();

        // Satellite
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.drawer_satellite), R.drawable.ic_satellite_black_36dp, this.presenter ){
            @Override
            public void click(){
                super.click();
                this.getPresenter().setSatelliteMode(this.isActive());
            }
        });

        // GPS
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.drawer_gps), R.drawable.ic_gps_fixed_black_36dp, this.presenter ){
            @Override
            public void click(){
                super.click();
                this.getPresenter().setGpsMode(this.isActive());
            }
        });

        // Caches
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.drawer_caches), R.drawable.ic_get_app_black_36dp, this.presenter ){
            @Override
            public void click(){
                super.click();
                this.getPresenter().setCaches(this.isActive());
            }
        });
        return itemsList;
    }
}
