package com.example.OKM.domain.model;

import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by kubut on 2015-08-02.
 */
public class MainDrawerItemModel implements IMainDrawerItem {
    private int icon;
    private String title;
    private boolean active;
    private final MainMapPresenter presenter;

    public MainDrawerItemModel(final String title, final int icon, final MainMapPresenter presenter){
        this.icon = icon;
        this.title = title;
        this.active = false;
        this.presenter = presenter;
    }

    @Override
    public void click() {
        this.active = !this.active;
        this.presenter.hideDrawer();
    }

    @Override
    public void setIcon(final int icon) {
        this.icon = icon;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }

    @Override
    public int getIcon() {
        return this.icon;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public MainMapPresenter getPresenter(){
        return this.presenter;
    }
}
