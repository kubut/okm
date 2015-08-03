package com.example.OKM.domain.model;

import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by kubut on 2015-08-02.
 */
public interface IMainDrawerItem {
    void click();

    void setTitle(String title);
    void setActive(boolean active);

    boolean isActive();
    String getTitle();
    MainMapPresenter getPresenter();
}
