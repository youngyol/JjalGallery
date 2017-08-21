package org.horaapps.leafpic.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mikepenz.iconics.view.IconicsImageView;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.data.Jjal;
import org.horaapps.leafpic.views.SquareRelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.ThemedAdapter;
import horaapps.org.liz.ThemedViewHolder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by nasos on 2017-08-21.
 */

public class JjalAdapter extends ThemedAdapter<JjalAdapter.ViewHolder> {
    private ArrayList<Jjal> jjals;
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();

    private Drawable placeholder;

    public JjalAdapter(Context context, ArrayList<Jjal> items) {
        super(context);
        jjals = items;
        placeholder = getThemeHelper().getPlaceHolder();

    }



    @Override
    public JjalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JjalAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder VHItem = (ViewHolder) holder;
        holder.gifIcon.setVisibility(View.GONE);

//        holder.icon.setVisibility(View.GONE);

//        RequestOptions options = new RequestOptions()
//                .format(DecodeFormat.PREFER_ARGB_8888)
//                .centerCrop()
//                .placeholder(placeholder)
//                //.animate(R.anim.fade_in)//TODO:DONT WORK WELL
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//
//        Glide.with(holder.imageView.getContext())
//                .load(jjals.get(position).getUrl())
//                .apply(options)
//                .thumbnail(0.2f)
//                .into(VHItem.imageView);

        if(jjals.get(position).getUrl().contains("GIF?raw=true")) {

            DraweeController gifController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(jjals.get(position).getUrl()))
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(
                                String id,
                                ImageInfo imageInfo,
                                Animatable anim) {
                            if (anim != null) {
                                // start the animation with anim.start() whenever you want
                            }
                        }
                    })
                    .build();
            holder.gifIcon.setVisibility(View.VISIBLE);
             holder.gifIcon.setBackgroundColor(Color.parseColor("#64000000") );

            holder.gifIcon.setPaddingDp(2);
            VHItem.imageView.setController(gifController);
        }
        else {
            ImageRequest request  = ImageRequestBuilder.newBuilderWithSource(Uri.parse(jjals.get(position).getUrl()))
                    .setResizeOptions(new ResizeOptions(70, 70))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();

            VHItem.imageView.setController(controller);
        }



    }


    public void clear() {
        int count = jjals.size();
        if (count > 0) {
            jjals.clear();
            notifyDataSetChanged();
        }
    }


    public Observable<Integer> getClicks() {
        return onClickSubject;
    }


    @Override
    public int getItemCount() {
        return jjals.size();
    }
    static class ViewHolder extends ThemedViewHolder {

        @BindView(R.id.img_thumbnail)
        SimpleDraweeView imageView;
        @BindView(R.id.image_card_layout)
        SquareRelativeLayout layout;
        @BindView(R.id.gif_icon)
        IconicsImageView gifIcon;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void refreshTheme(ThemeHelper themeHelper) {
             Log.wtf("asd", "asdasd");
        }
    }


}
