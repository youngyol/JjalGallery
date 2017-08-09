package com.example.nasos.jjalgallery.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nasos.jjalgallery.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by nasos on 2017-08-09.
 */

public class ImageFragment extends Fragment {
    @BindView(R.id.detail_img)
    PhotoDraweeView img;
    private Context ctx;
    private Unbinder unbinder;
    private String path;

    private int deviceWidth;
    private int deviceHeight ;


    private void calculate() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        deviceHeight = displaymetrics.heightPixels;
        deviceWidth = displaymetrics.widthPixels;
    }


    // newInstance constructor for creating fragment with arguments
    public static ImageFragment newInstance(String path) {
        ImageFragment fragmentFirst = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        calculate();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        path = getArguments().getString("path");
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ctx = getContext();
        setImageInViewPager();
        return view;

    }

    private void setImageInViewPager(){
        Log.d("setImg", path);
        File imgFile = new File(path);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(imgFile))
                .setResizeOptions(new ResizeOptions(deviceWidth, 100))
                .build();

//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .build();
//        img.setController(controller);




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
        img.setController(controller1.build());
    }

    public void setImage(String path) {
        this.path = path;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }


}
