package com.opencachingkubutmaps.data.factories;

import com.opencachingkubutmaps.domain.model.IMainDrawerItem;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-03.
 */
public interface IMainDrawerItemListFactory {
    ArrayList<IMainDrawerItem> getItemsList();
}
