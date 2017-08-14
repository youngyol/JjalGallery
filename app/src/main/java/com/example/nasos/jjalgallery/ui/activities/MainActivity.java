package com.example.nasos.jjalgallery.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.ui.adapters.TabPagerAdapter;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;
import com.example.nasos.jjalgallery.util.SharedPreferencesService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.mTabLayout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SharedPreferencesService myPref;
//    private int flag ;


    @OnClick(R.id.settingBtn)
    public void onClickToolbar(View v) {
//        flag = 1;
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        Fresco.initialize(this);
//        flag = 0;
        if (checkPW()) {
            Intent intent = new Intent(this, PasswordActivity.class);
            intent.putExtra("state", 3);
            startActivity(intent);
        }
        setContentView(getActivityId());
        ButterKnife.bind(this);
    }


    private boolean checkPW() {
        myPref = new SharedPreferencesService().getInstance();
        myPref.load(getApplicationContext());
//        Toast.makeText(this, myPref.getPrefBooleanData("hasPassword") + "", Toast.LENGTH_SHORT).show();
        return myPref.getPrefBooleanData("hasPassword");
    }

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



//    @Override
//    protected void onUserLeaveHint() {
//        // TODO Auto-generated method stub
//        super.onUserLeaveHint();
//        if (checkPW() && flag == 0) {
//
//            Intent intent = new Intent(this, PasswordActivity.class);
//            intent.putExtra("state", 3);
//            startActivity(intent);
//
//        }
//    }

    private void requestPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                initLayout();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };


        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
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
