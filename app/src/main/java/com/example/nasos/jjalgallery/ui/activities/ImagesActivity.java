package com.example.nasos.jjalgallery.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Images;
import com.example.nasos.jjalgallery.ui.adapters.ImagesAdapter;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;
import com.example.nasos.jjalgallery.util.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;

public class ImagesActivity extends BaseActivity {
    private Context ctx;
    private ArrayList<Images> albumImageItems;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private ImagesAdapter albumImagesAdapter;


    @BindView(R.id.rcv_images_user)
    RecyclerView recyclerView;
    @BindView(R.id.album_name_toolbar)
    TextView albumNameText;
    @BindView(R.id.album_toolbar)
    Toolbar toolbar;

    @Override
    protected void initLayout() {
        ctx = this;
        albumImageItems = new ArrayList<Images>();
        staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredLayoutManager);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        albumImageItems =(ArrayList<Images>)intent.getExtras().get("albumImages");
        String albumName = intent.getExtras().getString("albumName");


        albumNameText.setText(albumName);
        albumImagesAdapter = new ImagesAdapter(this,albumImageItems);
        recyclerView.setAdapter(albumImagesAdapter);
        setListener();
    }

    private void setListener() {

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ctx, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        Intent intent = new Intent(ctx, DetailImageActivity.class);
                        intent.putExtra("albumImages", albumImageItems);
                            intent.putExtra("position", position);
                        startActivity(intent);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));


    }

    @Override
    protected int getActivityId() {
        return R.layout.activity_images;
    }


}
