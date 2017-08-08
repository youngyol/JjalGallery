package com.example.nasos.jjalgallery.ui.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Images;
import com.example.nasos.jjalgallery.ui.adapters.ImagesAdapter;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class ImagesActivity extends BaseActivity {

    private ArrayList<Images> albumImageItems;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private ImagesAdapter albumImagesAdapter;
    @BindView(R.id.rcv_images_user)
    RecyclerView recyclerView;

    @BindView(R.id.album_name_toolbar)
    TextView albumNameText;

    @Override
    protected void initLayout() {
        albumImageItems = new ArrayList<Images>();
        staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredLayoutManager);


        Intent intent = getIntent();
        albumImageItems =(ArrayList<Images>)intent.getExtras().get("albumImages");
        String albumName = intent.getExtras().getString("albumName");


        albumNameText.setText(albumName);
        albumImagesAdapter = new ImagesAdapter(this,albumImageItems);
        recyclerView.setAdapter(albumImagesAdapter);

    }

    @Override
    protected int getActivityId() {
        return R.layout.activity_images;
    }


}
