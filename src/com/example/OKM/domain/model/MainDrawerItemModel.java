package com.example.OKM.domain.model;

import com.example.OKM.presentation.presenter.MainMapPresenter;

/**
 * Created by kubut on 2015-08-02.
 */
public class MainDrawerItemModel implements IMainDrawerItem {
    private int icon;
    private String title;
    private boolean active;
    private MainMapPresenter presenter;

    public MainDrawerItemModel(String title, int icon, MainMapPresenter presenter){
        this.icon = icon;
        this.title = title;
        this.active = false;
        this.presenter = presenter;
    }

    @Override
    public void click() {
        this.active = !this.active;
    }

    @Override
    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int getIcon() {
        return this.icon;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public MainMapPresenter getPresenter(){
        return this.presenter;
    }
}
