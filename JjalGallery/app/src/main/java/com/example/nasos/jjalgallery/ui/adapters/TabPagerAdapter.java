package com.example.nasos.jjalgallery.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.nasos.jjalgallery.ui.fragments.UserGalleryFragment;

/**
 * Created by nasos on 2017-08-07.
 */


public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                return new UserGalleryFragment();
            default:
                return new UserGalleryFragment();
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}