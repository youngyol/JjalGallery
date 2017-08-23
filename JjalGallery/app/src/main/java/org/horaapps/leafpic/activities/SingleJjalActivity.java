package org.horaapps.leafpic.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.orhanobut.hawk.Hawk;
import com.yalantis.ucrop.UCrop;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.base.SharedMediaActivity;
import org.horaapps.leafpic.adapters.JjalsPagerAdapter;
import org.horaapps.leafpic.animations.DepthPageTransformer;
import org.horaapps.leafpic.data.Jjal;
import org.horaapps.leafpic.data.bookmark.Bookmark;
import org.horaapps.leafpic.data.bookmark.BookmarkDB;
import org.horaapps.leafpic.util.Measure;
import org.horaapps.leafpic.views.HackyViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ColorPalette;

public class SingleJjalActivity extends SharedMediaActivity {
    private static final String TAG = SingleBookMarkActivity.class.getSimpleName();

    private boolean isBookmarkChecked = false;

    private boolean fullScreenMode, customUri = false;
    private static final String ISLOCKED_ARG = "isLocked";
    private Bitmap mSaveBm;

    @BindView(R.id.jjals_pager)
    HackyViewPager mViewPager;

    @BindView(R.id.jjalPager_Layout)
    RelativeLayout activityBackground;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private JjalsPagerAdapter adapter;
    private int position;


    private MenuItem menuBookmarkItem;
    private ArrayList<Jjal> jjalImageItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_jjal);

        ButterKnife.bind(this);

        jjalImageItems = new ArrayList<Jjal>();
        Intent intent = getIntent();
        jjalImageItems = (ArrayList<Jjal>) intent.getExtras().get("jjal");
        position = intent.getExtras().getInt("position");

        adapter = new JjalsPagerAdapter(getSupportFragmentManager(), jjalImageItems);


        initUi();
    }

    private void initUi() {
        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });


        setupSystemUI();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int i) {
                        if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) showSystemUI();
                        else hideSystemUI();
                    }
                });

        updatePageTitle(position);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SingleJjalActivity.this.position = position;

                updatePageTitle(position);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation() == Surface.ROTATION_90) {
            Configuration configuration = new Configuration();
            configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;
            onConfigurationChanged(configuration);
        }
    }

    private void updatePageTitle(int position) {
        getSupportActionBar().setTitle((position + 1) + " " + getString(R.string.of) + " " + adapter.getCount());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getApplicationContext()).clearMemory();
        Glide.get(getApplicationContext()).trimMemory(TRIM_MEMORY_COMPLETE);
        System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jjal_pager, menu);
        menu.findItem(R.id.action_share).setIcon(getToolbarIcon(CommunityMaterial.Icon.cmd_share));
        menu.findItem(R.id.action_bookmark).setIcon(getToolbarIcon(CommunityMaterial.Icon.cmd_heart_outline));
        menu.findItem(R.id.action_download).setIcon(getToolbarIcon(CommunityMaterial.Icon.cmd_download));

        menuBookmarkItem = menu.findItem(R.id.action_bookmark);
        isBookmarkChecked = checkDBForBookmark(jjalImageItems.get(position).getUrl());
        setBookmarkIconColor();
        return true;
    }

    @CallSuper
    public void updateUiElements() {
        super.updateUiElements();
        /**** Theme ****/
        toolbar.setBackgroundColor(
                themeOnSingleImgAct()
                        ? ColorPalette.getTransparentColor(getPrimaryColor(), getTransparency())
                        : ColorPalette.getTransparentColor(getDefaultThemeToolbarColor3th(), 175));

        toolbar.setPopupTheme(getPopupToolbarStyle());


        activityBackground.setBackgroundColor(getBackgroundColor());

        setStatusBarColor();
        setNavBarColor();
        setRecentApp(getString(R.string.app_name));


        /**** SETTINGS ****/

        if (Hawk.get("set_max_luminosity", false))
            updateBrightness(1.0F);
        else try {
            // TODO: 12/4/16 redo
            float brightness = android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            brightness = brightness == 1.0F ? 255.0F : brightness;
            updateBrightness(brightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (Hawk.get("set_picture_orientation", false))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

    }

    private void setupSystemUI() {
        toolbar.animate().translationY(Measure.getStatusBarHeight(getResources())).setInterpolator(new DecelerateInterpolator())
                .setDuration(0).start();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    private void showSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                toolbar.animate().translationY(Measure.getStatusBarHeight(getResources())).setInterpolator(new DecelerateInterpolator())
                        .setDuration(240).start();
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                fullScreenMode = false;
                changeBackGroundColor();
            }
        });
    }

    private void changeBackGroundColor() {
        int colorTo;
        int colorFrom;
        if (fullScreenMode) {
            colorFrom = getBackgroundColor();
            colorTo = (ContextCompat.getColor(SingleJjalActivity.this, R.color.md_black_1000));
        } else {
            colorFrom = (ContextCompat.getColor(SingleJjalActivity.this, R.color.md_black_1000));
            colorTo = getBackgroundColor();
        }
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(240);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                activityBackground.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }


    private void hideSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator())
                        .setDuration(100).start();
                getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        Log.wtf(TAG, "ui changed: " + visibility);
                    }
                });
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);

                fullScreenMode = true;
                changeBackGroundColor();
            }
        });
    }


    private Boolean checkDBForBookmark(String path) {
        return BookmarkDB.mBookmarkDao.fetchBookmarkByPath(path);
    }

    private void setBookmarkIconColor() {

        Drawable normalDrawable = menuBookmarkItem.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        if (isBookmarkChecked) {
            //북마크 테이블 이미 벌써 존재
            DrawableCompat.setTint(wrapDrawable, getResources().getColor(R.color.md_deep_orange_800));
        } else {
            //없어
            DrawableCompat.setTint(wrapDrawable, getResources().getColor(R.color.md_white_1000));
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mViewPager != null) {
            outState.putBoolean(ISLOCKED_ARG, mViewPager.isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    public void toggleSystemUI() {
        if (fullScreenMode)
            showSystemUI();
        else hideSystemUI();
    }


    @Override
    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (themeOnSingleImgAct())
                if (isTranslucentStatusBar() && isTransparencyZero())
                    getWindow().setStatusBarColor(ColorPalette.getObscuredColor(getPrimaryColor()));
                else
                    getWindow().setStatusBarColor(ColorPalette.getTransparentColor(getPrimaryColor(), getTransparency()));
            else
                getWindow().setStatusBarColor(ColorPalette.getTransparentColor(
                        ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), 175));
        }
    }

    @Override
    public void setNavBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (themeOnSingleImgAct())
                if (isNavigationBarColored())
                    getWindow().setNavigationBarColor(ColorPalette.getTransparentColor(getPrimaryColor(), getTransparency()));
                else
                    getWindow().setNavigationBarColor(ColorPalette.getTransparentColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), getTransparency()));
            else
                getWindow().setNavigationBarColor(ColorPalette.getTransparentColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), 175));
        }
    }


    @SuppressWarnings("ResourceAsColor")
    private UCrop.Options getUcropOptions() {

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionQuality(90);
        options.setActiveWidgetColor(getAccentColor());
        options.setToolbarColor(getPrimaryColor());
        options.setStatusBarColor(isTranslucentStatusBar() ? ColorPalette.getObscuredColor(getPrimaryColor()) : getPrimaryColor());
        options.setCropFrameColor(getAccentColor());
        options.setFreeStyleCropEnabled(true);

        return options;
    }

    private void updateBrightness(float level) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = level;
        getWindow().setAttributes(lp);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_share:
//                Intent share = new Intent(Intent.ACTION_SEND);
//                Uri uri = Uri.parse(jjalImageItems.get(position).getUrl());
//                share.setType("image/*");
//                share.putExtra(Intent.EXTRA_STREAM, uri);
//                startActivity(Intent.createChooser(share, getString(R.string.send_to)));


//                ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
//                PhotoDraweeView img = new PhotoDraweeView(getApplicationContext());
//

//                Bitmap imgBitmap = GetImageFromURL()
                new ImageDownload().execute(jjalImageItems.get(position).getUrl());
                break;


            //                 new Thread(new Runnable() {
//                    public void run() {
//                        shareBitmap (getBitmapFromURL(jjalImageItems.get(position).getUrl()));
//
//                    }
//                }).start();
//                return true;
//
//            case R.id.action_bookmark:
//                Bookmark tmp = new Bookmark();
//                tmp.path = bookmarkImageItems.get(position).path;
//
//
//                long removedID = BookmarkDB.mBookmarkDao.getBookmarkIDByPath(tmp.path);
//                Boolean tmp1 = BookmarkDB.mBookmarkDao.deleteData(removedID);
//                isBookmarkChecked = false;
//
////                setBookmarkIconColor();
//
//
//                bookmarkImageItems.remove(position);
//
//                if (bookmarkImageItems.size() == 0) {
//                    displayAlbums();
//                }
//
//                adapter.notifyDataSetChanged();
//                updatePageTitle(mViewPager.getCurrentItem());
//                break;


            case R.id.action_bookmark:
                Bookmark tmp = new Bookmark();
                tmp.path = jjalImageItems.get(position).getUrl();
                if (!isBookmarkChecked) {
                    isBookmarkChecked = true;
                    if (BookmarkDB.mBookmarkDao.addBookmark(tmp)) {
                        Toast.makeText(getApplicationContext(), "insert  " + isBookmarkChecked, Toast.LENGTH_SHORT).show();
                    }
                } else {

                    long removedID = BookmarkDB.mBookmarkDao.getBookmarkIDByPath(tmp.path);
                    Boolean tmp1 = BookmarkDB.mBookmarkDao.deleteData(removedID);
                    isBookmarkChecked = false;
                }

                setBookmarkIconColor();
                break;


            case R.id.action_download:
                new ImageDownload().execute(jjalImageItems.get(position).getUrl());
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                //return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

//    public Bitmap getBitmapFromURL(String src) {
//        HttpURLConnection connection = null;
//        try {
//            URL url = new URL(src);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (connection != null) connection.disconnect();
//        }
//    }
//
//
//    public void shareBitmap(Bitmap bitmap) {
//        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
//                bitmap, "Image Description", null);
//        Uri bmpUri = Uri.parse(path);
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//        shareIntent.setType("image/*");
//
//        startActivity(Intent.createChooser(shareIntent, "Share Image"));
//    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection)

                    url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
            return imageBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class ImageDownload extends AsyncTask<String, Void, Void> {


        /**
         * 파일명
         */

        private String fileName;


        /**
         * 저장할 폴더
         */

        private final String SAVE_FOLDER = "/Jjal_gallery";
        boolean isDuplicated = false;

        @Override

        protected Void doInBackground(String... params) {


            //다운로드 경로를 지정

            String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;


            File dir = new File(savePath);

            //상위 디렉토리가 존재하지 않을 경우 생성

            if (!dir.exists()) {

                dir.mkdirs();

            }


            //파일 이름 :날짜_시간

            Date day = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);

            fileName = String.valueOf(sdf.format(day));


            //웹 서버 쪽 파일이 있는 경로

            String fileUrl = jjalImageItems.get(position).getUrl();


            String imgName = fileUrl.split("/Animal/")[1].split("\\?raw")[0];
            fileName = imgName.split("\\.")[0];
            Log.d("dada", fileName);
            //다운로드 폴더에 동일한 파일명이 존재하는지 확인

            if (new File(savePath + "/" + imgName).exists() == false) {
                Log.d("sddsddsd", "존재");
            } else {
                isDuplicated = true;
            }


            String localPath = savePath + "/" + imgName;


            try {

                URL imgUrl = new URL(fileUrl);

                //서버와 접속하는 클라이언트 객체 생성

                HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();

                int len = conn.getContentLength();

                byte[] tmpByte = new byte[len];

                //입력 스트림을 구한다

                InputStream is = conn.getInputStream();

                File file = new File(localPath);

                //파일 저장 스트림 생성

                FileOutputStream fos = new FileOutputStream(file);

                int read;

                //입력 스트림을 파일로 저장

                for (; ; ) {

                    read = is.read(tmpByte);

                    if (read <= 0) {

                        break;

                    }

                    fos.write(tmpByte, 0, read); //file 생성

                }

                is.close();

                fos.close();

                conn.disconnect();

            } catch (Exception e) {

                e.printStackTrace();

            }


            return null;

        }


        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

//

            //저장한 이미지 열기

//            Intent i = new Intent(Intent.ACTION_VIEW);
//
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            String targetDir = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;

            File file = new File(targetDir + "/" + fileName + ".jpg");

            //type 지정 (이미지)
//
//            i.setDataAndType(Uri.fromFile(file), "image/*");
//
//            getApplicationContext().startActivity(i);


            //이미지 스캔해서 갤러리 업데이트

            if (!isDuplicated) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                Toast.makeText(getApplicationContext(), "저장완료!", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(getApplicationContext(), "이미 존재 !", Toast.LENGTH_SHORT).show();

        }

    }


}
