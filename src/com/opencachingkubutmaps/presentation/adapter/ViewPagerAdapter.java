package com.opencachingkubutmaps.presentation.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(final FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(final int position) {
        return this.mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.mFragmentList.size();
    }

    public void addFragment(final Fragment fragment, final String title) {
        this.mFragmentList.add(fragment);
        this.mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return this.mFragmentTitleList.get(position);
    }
}