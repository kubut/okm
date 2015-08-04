package com.example.OKM.domain.model;

import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by kubut on 2015-08-02.
 */
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
