package org.horaapps.leafpic.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
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

    private  int size = 10 ;
    private Drawable placeholder;
    private Context ctx;

    public JjalAdapter(Context context, ArrayList<Jjal> items) {
        super(context);
        ctx = context;
        jjals = items;
        placeholder = getThemeHelper().getPlaceHolder();

    }

    public void loadMore(){
        size+=10;
    }

    public int getSize(){
        return size;
    }
    @Override
    public JjalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JjalAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photo, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder VHItem = (ViewHolder) holder;
        holder.gifIcon.setVisibility(View.GONE);


        if(jjals.get(position).getUrl().contains(".GIF?raw=")){
            holder.gifIcon.setVisibility(View.VISIBLE);
            holder.gifIcon.setIcon((GoogleMaterial.Icon.gmd_gif));
        }
        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .centerCrop()
                .placeholder(placeholder)
                .override(100, 100)
                .error(org.horaapps.leafpic.R.drawable.ic_error)
                //.animate(R.anim.fade_in)//TODO:DONT WORK WELL
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);


        Glide.with(ctx)
                .asBitmap()
                .load(jjals.get(position).getUrl())
                .apply(options)
                .into(VHItem.imageView);

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
        if(jjals.size() > 10) return  size;
        else return jjals.size();
    }

    static class ViewHolder extends ThemedViewHolder {

        @BindView(R.id.photo_preview)
        ImageView imageView;
        @BindView(R.id.media_card_layout)
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
