package com.example.nasos.jjalgallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.ui.base.BaseFragment;

/**
 * Created by nasos on 2017-08-07.
 */


public class UserGalleryFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_gallery_fragment, container, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_gallery_fragment;
    }

    @Override
    protected void initLayout() {

    }
}
