package org.horaapps.leafpic.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.SingleMediaActivity;
import org.horaapps.leafpic.data.Media;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by dnld on 18/02/16.
 */

@SuppressWarnings("ResourceType")
public class ImageFragment extends Fragment {

    View view;
    private Media img;

    @BindView(R.id.subsampling_view)
    SubsamplingScaleImageView subsampling;

    @BindView(R.id.photo_view)
    PhotoView photoView;


    private int deviceWidth;
    private int deviceHeight;

    public static ImageFragment newInstance(Media media) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable("image", media);
        imageFragment.setArguments(args);
        return imageFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img = getArguments().getParcelable("image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);

        calculate();
        displayMedia();
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SingleMediaActivity) getActivity()).toggleSystemUI();
            }
        });
        photoView.setMaximumScale(8.0F);
        photoView.setMediumScale(3.0F);


        /*if (Hawk.get(getString(R.string.preference_sub_scaling), true)) {
            SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(getContext());
            imageView.setImage(ImageSource.uri(img.getUri()).tilingEnabled());
            imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_0);
            imageView.setOnClickListener(v -> ((SingleMediaActivity) getActivity()).toggleSystemUI());
            return imageView;
        } else {

        }*/

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!getActivity().isDestroyed())
            Glide.with(getContext()).clear(photoView);
    }


    private void calculate() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        deviceHeight = displaymetrics.heightPixels;
        deviceWidth = displaymetrics.widthPixels;
    }

    /* private void rotateLoop() { //april fools
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                photoView.setRotationBy(1);
                rotateLoop();
            }
        }, 5);
    }*/

    private void displayMedia() {
        //PreferenceUtil SP = PreferenceUtil.make(getContext());

       /* .asBitmap()
                .signature(useCache ? img.getSignature(): new StringSignature(new Date().getTime()+""))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.5f)
                //.transform(new RotateTransformation(getContext(), img.getOrientation(), false))
                .animate(R.anim.fade_in)*/


        RequestOptions options = new RequestOptions()
                .signature(img.getSignature())
                //.centerCrop()
                .override(deviceWidth, 500)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(getContext())
                .load(img.getUri())
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //addZoomableView();
                        return false;
                    }
                })
                .into(photoView);

//        if (img != null) {
//
//
//        }
//        else if( bookmarkImgPath != null){
//
//            RequestOptions options = new RequestOptions()
//                    //.centerCrop()
//                    .override(deviceWidth, 500)
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .format(DecodeFormat.PREFER_ARGB_8888)
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//            Glide.with(getContext())
//                    .load(bookmarkImgPath)
//                    .apply(options)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            //addZoomableView();
//                            return false;
//                        }
//                    })
//                    .into(photoView);
//
//        }

    }

    private void addZoomableView() {

        subsampling.setMaxScale(10f);
        subsampling.setImage(ImageSource.uri(img.getUri()));
        subsampling.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
        subsampling.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onImageLoaded() {
                Log.wtf("Asd", "yeeeee");
                subsampling.setVisibility(View.VISIBLE);
                subsampling.setDoubleTapZoomScale(getDoubleTapZoomScale());
            }

            @Override
            public void onPreviewLoadError(Exception e) {
                subsampling.setVisibility(View.GONE);
            }

            @Override
            public void onImageLoadError(Exception e) {
                subsampling.setVisibility(View.GONE);
            }

            @Override
            public void onTileLoadError(Exception e) {
                subsampling.setVisibility(View.GONE);

            }

            @Override
            public void onPreviewReleased() {

            }
        });
    }


    public Bitmap resizeImage(
            Uri tempImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        InputStream in = null;
        try {
            in = getActivity().getContentResolver().openInputStream(tempImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);

        return bitmap;
    }

    private float getDoubleTapZoomScale() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(img.getPath(), options);
        int width = options.outWidth;
        int height = options.outHeight;
        float bitmapAspectRatio = (float) height / (float) (width);

        if (getContext() == null)
            return 2f;

        // TODO: 8/8/17

        /*
        return if (context.portrait && bitmapAspectRatio <= 1f) {
            ViewPagerActivity.screenHeight / height.toFloat()
        } else if (!context.portrait && bitmapAspectRatio >= 1f) {
            ViewPagerActivity.screenWidth / width.toFloat()
        } else {
            2f
        }
        */


        return 2f;
    }

    public boolean rotatePicture(int rotation) {
        // TODO: 28/08/16 not working yet
        /*PhotoView photoView = (PhotoView) getView();

        int orientation = Measure.rotateBy(img.getOrientation(), rotation);
        Log.wtf("asd", img.getOrientation()+" + "+ rotation+" = " +orientation);

        if(photoView != null && img.setOrientation(orientation)) {
            Glide.clear(photoView);
            Glide.with(getContext())
                    .load(img.getUri())
                    .asBitmap()
                    .signature(img.getSignature())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    //.thumbnail(0.5f)
                    .transform(new RotateTransformation(getContext(), rotation , true))
                    .into(photoView);

            return true;
        }*/
        return false;
    }
}