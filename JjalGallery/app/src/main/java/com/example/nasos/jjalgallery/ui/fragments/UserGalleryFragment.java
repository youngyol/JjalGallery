package com.example.nasos.jjalgallery.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Album;
import com.example.nasos.jjalgallery.model.UserGalleryDB;
import com.example.nasos.jjalgallery.ui.adapters.AlbumsAdapter;
import com.example.nasos.jjalgallery.ui.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by nasos on 2017-08-07.
 */


public class UserGalleryFragment extends BaseFragment {
    private Unbinder unbinder;
    private AlbumsAdapter albumsAdapter;
    private ArrayList<Album> albumItemDatas;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private Context ctx;

    @BindView(R.id.rcv_album)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_gallery_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ctx = getContext();
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_gallery_fragment;
    }

    @Override
    protected void initLayout() {
        staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredLayoutManager);
        getPhotoList();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getPhotoList() {
        Cursor imageCursor = new UserGalleryDB(ctx).getAlbumList();
        albumItemDatas = new ArrayList<Album>();
        if (imageCursor.moveToFirst()) {
            String bucketName, bucketId, bucketData;
            int bucketNameColumn = imageCursor.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIDColumn = imageCursor.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_ID);
            int dataColumn = imageCursor.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            do {
                // Get the field values
                bucketName = imageCursor.getString(bucketNameColumn);
                bucketData = imageCursor.getString(dataColumn);
                bucketId = imageCursor.getString(bucketIDColumn);

                albumItemDatas.add(new Album(bucketData,bucketId,bucketName));

            } while (imageCursor.moveToNext());

            imageCursor.close();
            albumsAdapter = new AlbumsAdapter(ctx,albumItemDatas);
            recyclerView.setAdapter(albumsAdapter);
        }


    }
}
