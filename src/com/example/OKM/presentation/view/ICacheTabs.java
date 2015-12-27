package com.example.OKM.presentation.view;

import android.content.Context;
import com.example.OKM.domain.model.CacheModel;

/**
 * Created by Jakub on 12.12.2015.
 */
public interface ICacheTabs {
    void syncView();
    void setView(Context context, CacheModel cacheModel);
}
