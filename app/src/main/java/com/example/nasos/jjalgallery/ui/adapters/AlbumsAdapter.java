package com.example.nasos.jjalgallery.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Album;
import com.example.nasos.jjalgallery.ui.base.BaseAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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


        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(imgFile))
                .setResizeOptions(new ResizeOptions(90, 90))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        VHItem.albumThumbnailImg.setController(controller);





        VHItem.albumNameTxt.setText(items.get(position).getAlbumName());
    }



    class AlbumViewHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.album_thumbnail_img)
        SimpleDraweeView albumThumbnailImg;
        @BindView(R.id.album_thumbnail_name)
        TextView albumNameTxt;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
