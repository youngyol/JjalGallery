package com.example.nasos.jjalgallery.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by nasos on 2017-08-06.
 */

public abstract class BaseActivity  extends AppCompatActivity {

    protected abstract void initLayout();
    protected abstract int getActivityId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityId());
        ButterKnife.bind(this);
        initLayout();
    }

}
