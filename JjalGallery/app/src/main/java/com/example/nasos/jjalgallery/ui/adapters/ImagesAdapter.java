package com.example.nasos.jjalgallery.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Images;
import com.example.nasos.jjalgallery.ui.base.BaseAdapter;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nasos on 2017-08-09.
 */

public class ImagesAdapter extends BaseAdapter<Images> {

    public ImagesAdapter(Context context, ArrayList<Images> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.image_thumbnails, null);
        return new ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder VHItem = (ImageViewHolder) holder;

        File imgFile = new File(items.get(position).getImgPath());
        Glide.with(context)
                .load(imgFile)
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .into(VHItem.imageThumbnail);

    }



    class ImageViewHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.image_thumbnail_user)
        ImageView imageThumbnail;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
