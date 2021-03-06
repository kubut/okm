package com.opencachingkubutmaps.data.factories;

import android.content.Intent;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.IMainDrawerItem;
import com.opencachingkubutmaps.domain.model.MainDrawerItemModel;
import com.opencachingkubutmaps.presentation.presenter.MainMapPresenter;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-05.
 */
public class MainDrawerIntentItemListFactory implements IMainDrawerItemListFactory{
    private final MainMapPresenter presenter;

    public MainDrawerIntentItemListFactory(final MainMapPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public ArrayList<IMainDrawerItem> getItemsList() {
        final ArrayList<IMainDrawerItem> itemsList = new ArrayList<>();

        // settings
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.drawer_settings), R.drawable.ic_settings_black_36dp, this.presenter ){
            @Override
            public void click(){
                this.getPresenter().getActivity().goToSettings();
            }
        });

        // exit
        itemsList.add(new MainDrawerItemModel( this.presenter.getContext().getString(R.string.drawer_exit), R.drawable.ic_exit_to_app_black_36dp, this.presenter ){
            @Override
            public void click(){
                final Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.getPresenter().getContext().startActivity(intent);
            }
        });

        return itemsList;
    }
}
