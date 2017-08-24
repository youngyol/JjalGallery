package org.horaapps.leafpic.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.data.bookmark.Bookmark;
import org.horaapps.leafpic.views.SquareRelativeLayout;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import horaapps.org.liz.ThemedAdapter;
import horaapps.org.liz.ThemedViewHolder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by nasos on 2017-08-20.
 */

public class BookmarksAdapter extends ThemedAdapter<BookmarksAdapter.ViewHolder> {
    private ArrayList<Bookmark> bookmarks;
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();
    private Drawable placeholder;
    public BookmarksAdapter(Context context,ArrayList<Bookmark> items) {
        super(context);
        bookmarks = items;
        placeholder = getThemeHelper().getPlaceHolder();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder VHItem = (ViewHolder) holder;
        holder.icon.setVisibility(View.GONE);
        File imgFile = new File(bookmarks.get(position).path);

        RequestOptions options = new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .centerCrop()
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
       if (bookmarks.get(position).path.contains("https://")){
           Log.d("bookmark", bookmarks.get(position).path);
           if(bookmarks.get(position).path.contains(".GIF?")){



               holder.gifIcon.setVisibility(View.VISIBLE);
//               holder.gifIcon.setBackgroundColor(Color.parseColor("#64000000") );
               holder.gifIcon.setIcon((GoogleMaterial.Icon.gmd_gif));
           }
           Glide.with(holder.imageView.getContext())
                   .asBitmap()
                   .load(bookmarks.get(position).path)
                   .apply(options)
                    .thumbnail(0.07f)
                   .into(VHItem.imageView);
       }
       else{

           if(bookmarks.get(position).path.contains(".gif")){

               holder.gifIcon.setVisibility(View.VISIBLE);
//             holder.gifIcon.setBackgroundColor(Color.parseColor("#64000000") );
               holder.gifIcon.setIcon((GoogleMaterial.Icon.gmd_gif));


           }
            Glide.with(holder.imageView.getContext())
                   .asBitmap()
                   .load(imgFile)
                   .apply(options)
                   .thumbnail(0.5f)
                   .into(VHItem.imageView);
       }
    }


    public void clear() {
        int count = bookmarks.size();
        if (count > 0) {
            bookmarks.clear();
            notifyDataSetChanged();
        }
    }

    public Observable<Integer> getClicks() {
        return onClickSubject;
    }


    @Override
    public int getItemCount() {
        return bookmarks.size();
    }


    static class ViewHolder extends ThemedViewHolder {

        @BindView(R.id.photo_preview)
        ImageView imageView;
        @BindView(R.id.photo_path)
        TextView path;
        @BindView(R.id.gif_icon)
        IconicsImageView gifIcon;
        @BindView(R.id.icon)
        IconicsImageView icon;
        @BindView(R.id.media_card_layout)
        SquareRelativeLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void refreshTheme(ThemeHelper themeHelper) {
            icon.setColor(Color.WHITE);
            Log.wtf("asd", "asdasd");
        }
    }

}
