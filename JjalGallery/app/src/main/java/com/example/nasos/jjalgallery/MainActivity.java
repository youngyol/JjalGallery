package com.example.nasos.jjalgallery;

import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.nasos.jjalgallery.ui.TabPagerAdapter;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.mTabLayout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void initLayout() {
        setSupportActionBar(toolbar);
        TypedArray tabIcons = getResources().obtainTypedArray(R.array.tabs_icon);
        for (int index = 0; index < 3; index++) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.getTabAt(index).setIcon(tabIcons.getResourceId(index, R.drawable.ic_gallery_selector));
        }
        makePagerAdapter();
    }

    private void makePagerAdapter() {
        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected int getActivityId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);

        finish();

        android.os.Process.killProcess(android.os.Process.myPid()); 

    }
}
