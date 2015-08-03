package com.example.OKM.data;

import android.content.Context;
import com.example.OKM.R;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.domain.model.MainDrawerItemModel;
import com.example.OKM.presentation.presenter.MainMapPresenter;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-03.
 */
public class MainDrawerActionItemListFactory implements IMainDrawerItemListFactory {
    private MainMapPresenter presenter;

    public MainDrawerActionItemListFactory(MainMapPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public ArrayList<IMainDrawerItem> getItemsList() {
        ArrayList<IMainDrawerItem> itemsList = new ArrayList<>();
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.satellite), this.presenter ){
            @Override
            public void click(){
                super.click();
                this.getPresenter().setSatelliteMode(this.isActive());
            }
        });
        return itemsList;
    }
}
