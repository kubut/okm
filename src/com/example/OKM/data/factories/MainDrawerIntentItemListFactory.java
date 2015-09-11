package com.example.OKM.data.factories;

import android.content.Intent;
import com.example.OKM.R;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.domain.model.MainDrawerItemModel;
import com.example.OKM.presentation.presenter.MainMapPresenter;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-05.
 */
public class MainDrawerIntentItemListFactory implements IMainDrawerItemListFactory{
    private MainMapPresenter presenter;

    public MainDrawerIntentItemListFactory(MainMapPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public ArrayList<IMainDrawerItem> getItemsList() {
        ArrayList<IMainDrawerItem> itemsList = new ArrayList<>();

        // settings
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.drawer_settings), R.drawable.settings, this.presenter ){
            @Override
            public void click(){
                this.getPresenter().getActivity().goToSettings();
            }
        });

        // exit
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.drawer_exit), R.drawable.exit, this.presenter ){
            @Override
            public void click(){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.getPresenter().getContext().startActivity(intent);
            }
        });

        return itemsList;
    }
}
