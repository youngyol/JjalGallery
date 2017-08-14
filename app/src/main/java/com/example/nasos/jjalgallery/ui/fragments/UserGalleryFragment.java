package com.example.nasos.jjalgallery.ui.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.nasos.jjalgallery.model.Images;
import com.example.nasos.jjalgallery.model.UserGalleryDB;
import com.example.nasos.jjalgallery.ui.activities.ImagesActivity;
import com.example.nasos.jjalgallery.ui.adapters.AlbumsAdapter;
import com.example.nasos.jjalgallery.ui.base.BaseFragment;
import com.example.nasos.jjalgallery.util.RecyclerItemClickListener;

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
    private UserGalleryDB userGalleryQueryHelper;


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
        userGalleryQueryHelper = new UserGalleryDB(ctx);
        staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredLayoutManager);
        getPhotoList();
        setListener();

    }

    private void setListener() {

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ctx, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String tmppath = albumItemDatas.get(position).getAlbumID();
                        Cursor albumImageCursor = userGalleryQueryHelper.getAlbum(tmppath);
                        ArrayList<Images> albumImages = new ArrayList<Images>();

                        int path = albumImageCursor.getColumnIndex(
                                MediaStore.Images.Media.DATA);
                        int imgId = albumImageCursor.getColumnIndex(
                                MediaStore.Images.Media._ID);

                        int imageType = albumImageCursor.getColumnIndex(
                                MediaStore.Images.Media.MIME_TYPE);
                        if (albumImageCursor.moveToFirst()) {

                            do {
                                String filePath = albumImageCursor.getString(path);
                                String imgID = albumImageCursor.getString(imgId);
                                String thumPath = userGalleryQueryHelper.getThumbnailPath(imgID);
                                String imgType = albumImageCursor.getString(imageType);

                                albumImages.add(new Images(thumPath, filePath, imgID, imgType));
                            } while (albumImageCursor.moveToNext());

                        }


//                        Toast.makeText(ctx, albumImages.get(0).getImgPath(), Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(getActivity(), ImagesActivity.class);
                        intent.putExtra("albumImages", albumImages);
                        intent.putExtra("albumName", albumItemDatas.get(position).getAlbumName());
                        getActivity().startActivity(intent);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getPhotoList() {
        Cursor albumCursor = userGalleryQueryHelper.getAlbumList();
        albumItemDatas = new ArrayList<Album>();
        if (albumCursor.moveToFirst()) {
            String bucketName, bucketId, bucketData;
            int bucketNameColumn = albumCursor.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIDColumn = albumCursor.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_ID);
            int dataColumn = albumCursor.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            do {
                // Get the field values
                bucketName = albumCursor.getString(bucketNameColumn);
                bucketData = albumCursor.getString(dataColumn);
                bucketId = albumCursor.getString(bucketIDColumn);

                albumItemDatas.add(new Album(bucketData, bucketId, bucketName));

            } while (albumCursor.moveToNext());

            albumCursor.close();
            albumsAdapter = new AlbumsAdapter(ctx, albumItemDatas);
            recyclerView.setAdapter(albumsAdapter);
        }


    }
}
