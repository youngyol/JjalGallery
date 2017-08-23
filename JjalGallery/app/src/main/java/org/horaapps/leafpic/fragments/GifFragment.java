package org.horaapps.leafpic.fragments;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.data.Jjal;
import org.horaapps.leafpic.data.Media;
import org.horaapps.leafpic.data.bookmark.Bookmark;
import org.horaapps.leafpic.util.CircleProgressBarDrawable;

import java.io.File;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by dnld on 18/02/16.
 */
public class GifFragment extends Fragment {

    private Media gif;
    private int deviceWidth;
    private int deviceHeight;

    private String bookmarkImgPath;
    private String jjalImgPath;

    private void calculate() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        deviceHeight = displaymetrics.heightPixels;
        deviceWidth = displaymetrics.widthPixels;
    }

    public static GifFragment newInstance(Media media) {
        GifFragment gifFragment = new GifFragment();

        Bundle args = new Bundle();
        args.putParcelable("gif", media);
        gifFragment.setArguments(args);

        return gifFragment;

    }


    public static GifFragment newInstance(Bookmark media) {
        GifFragment imageFragment = new GifFragment();
        Bundle args = new Bundle();
        args.putString("bookmark", media.path);
        imageFragment.setArguments(args);
        return imageFragment;
    }

    public static GifFragment newInstance(Jjal media) {
        GifFragment imageFragment = new GifFragment();
        Bundle args = new Bundle();
        args.putString("jjal", media.getUrl());
        imageFragment.setArguments(args);
        return imageFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calculate();
        gif = getArguments().getParcelable("gif");
        bookmarkImgPath = getArguments().getString("bookmark", " ");
        jjalImgPath = getArguments().getString("jjal", " ");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        PhotoView photoView = new PhotoView(container.getContext());
//        Ion.with(getContext())
//                .load(gif.getPath())
//                .intoImageView(photoView);
//
//

        PhotoDraweeView img = new PhotoDraweeView(container.getContext());

        Uri uri;
        File imgFile;
        if (bookmarkImgPath.equals(" ") && jjalImgPath.equals(" ")) {
            imgFile = new File(gif.getPath());
            uri = Uri.fromFile(imgFile);
        } else if (jjalImgPath.equals(" ")) {
            if (bookmarkImgPath.contains("https://"))
                uri = Uri.parse(bookmarkImgPath);
            else {
                imgFile = new File(bookmarkImgPath);
                uri = Uri.fromFile(imgFile);
            }
        } else {
            uri = Uri.parse(jjalImgPath);
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(deviceWidth, 50))
                .build();
        PipelineDraweeControllerBuilder controller1 = Fresco.newDraweeControllerBuilder();
        controller1.setImageRequest(request);
        controller1.setOldController(img.getController());
        controller1.setAutoPlayAnimations(true);
        controller1.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || img == null) {
                    return;
                }
                img.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });


        final CircleProgressBarDrawable progressBar = new CircleProgressBarDrawable();
//        progressBar.setColor(getResources().getColor(R.color.md_red_A400));
//        progressBar.setBackgroundColor(getResources().getColor(R.color.md_grey_400));

        progressBar.setBackgroundColor(getResources().getColor(R.color.md_grey_400));
        progressBar.setColor(getResources().getColor(R.color.md_red_A400));
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        builder.setFadeDuration(30).setProgressBarImage(progressBar).build();
        img.getHierarchy().setProgressBarImage(progressBar);
        img.setController(controller1.build());


//        img.setOnClickListener(view -> ((SingleMediaActivity) getActivity()).toggleSystemUI());
        return img;
    }
}