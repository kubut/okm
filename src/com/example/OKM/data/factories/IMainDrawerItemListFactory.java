package com.example.OKM.data.factories;

import com.example.OKM.domain.model.IMainDrawerItem;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-03.
 */
public interface IMainDrawerItemListFactory {
    ArrayList<IMainDrawerItem> getItemsList();
}
