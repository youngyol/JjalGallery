package com.example.nasos.jjalgallery.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.model.Images;
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

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(imgFile))
                .setResizeOptions(new ResizeOptions(90, 90))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        VHItem.imageThumbnail.setController(controller);


//        if (items.get(position).getImageType().contains("gif")) {
//            Glide.with(context)
//                    .load(imgFile)
//                    .asGif()
//                    .placeholder(R.mipmap.ic_launcher)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(VHItem.imageThumbnail);
//        } else {
//            Glide.with(context)
//                    .load(imgFile)
//                    .placeholder(R.drawable.app_icon)
//                    .error(R.drawable.app_icon)
//                    .into(VHItem.imageThumbnail);
//        }


    }


    class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_thumbnail_user)
        SimpleDraweeView imageThumbnail;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
