package com.opencachingkubutmaps.domain.model;

import com.opencachingkubutmaps.presentation.presenter.MainMapPresenter;

/**
 * Created by kubut on 2015-08-02.
 */
@SuppressWarnings("unused")
public interface IMainDrawerItem {
    void click();

    void setIcon(int icon);
    void setTitle(String title);
    void setActive(boolean active);

    int getIcon();
    String getTitle();
    boolean isActive();
    MainMapPresenter getPresenter();
}
