package com.example.nasos.jjalgallery.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Album;
import com.example.nasos.jjalgallery.ui.base.BaseAdapter;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nasos on 2017-08-08.
 */

public class AlbumsAdapter extends BaseAdapter<Album>{

    public AlbumsAdapter(Context context, ArrayList<Album> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.album_thumbnails, null);
        return new AlbumViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlbumViewHolder VHItem = (AlbumViewHolder) holder;

        File imgFile = new File(items.get(position).getImgPath());
        Glide.with(context)
                .load(imgFile)
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .into(VHItem.albumThumbnailImg);

        VHItem.albumNameTxt.setText(items.get(position).getAlbumName());
    }



    class AlbumViewHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.album_thumbnail_img)
        ImageView albumThumbnailImg;
        @BindView(R.id.album_thumbnail_name)
        TextView albumNameTxt;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
