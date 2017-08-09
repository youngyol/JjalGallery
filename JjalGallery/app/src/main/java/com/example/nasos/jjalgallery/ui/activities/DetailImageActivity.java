package com.example.nasos.jjalgallery.ui.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Images;
import com.example.nasos.jjalgallery.ui.adapters.ImagesPagerAdapter;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class DetailImageActivity extends BaseActivity {
    @BindView(R.id.images_pager)
    ViewPager imgPager;
    private ArrayList<Images>  albumImgList;
    private int position;

    @Override
    protected void initLayout() {
        Intent intent = getIntent();
        albumImgList =(ArrayList<Images>)intent.getExtras().get("albumImages");
        position =intent.getExtras().getInt("position");
        Toast.makeText(getApplicationContext(),"pos "+position+"    " +albumImgList.get(position).getImgPath(),Toast.LENGTH_SHORT).show();
        ImagesPagerAdapter adapter= new ImagesPagerAdapter(getSupportFragmentManager(),albumImgList);
        imgPager.setAdapter(adapter);
        imgPager.setCurrentItem(position);

    }

    @Override
    protected int getActivityId() {
        return R.layout.activity_detail_image;
    }


}
