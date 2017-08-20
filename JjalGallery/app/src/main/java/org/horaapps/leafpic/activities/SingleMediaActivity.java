package org.horaapps.leafpic.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.orhanobut.hawk.Hawk;
import com.yalantis.ucrop.UCrop;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.base.SharedMediaActivity;
import org.horaapps.leafpic.adapters.MediaPagerAdapter;
import org.horaapps.leafpic.animations.DepthPageTransformer;
import org.horaapps.leafpic.data.Album;
import org.horaapps.leafpic.data.AlbumSettings;
import org.horaapps.leafpic.data.Media;
import org.horaapps.leafpic.data.MediaHelper;
import org.horaapps.leafpic.data.StorageHelper;
import org.horaapps.leafpic.data.bookmark.Bookmark;
import org.horaapps.leafpic.data.bookmark.BookmarkDB;
import org.horaapps.leafpic.util.AlertDialogsHelper;
import org.horaapps.leafpic.util.Measure;
import org.horaapps.leafpic.util.Security;
import org.horaapps.leafpic.util.StringUtils;
import org.horaapps.leafpic.views.HackyViewPager;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ColorPalette;

/**
 * Created by dnld on 18/02/16.
 */
@SuppressWarnings("ResourceAsColor")
public class SingleMediaActivity extends SharedMediaActivity {

    private static final String TAG = SingleMediaActivity.class.getSimpleName();


    private static final String ISLOCKED_ARG = "isLocked";
    public static final String ACTION_OPEN_ALBUM = "org.horaapps.leafpic.intent.VIEW_ALBUM";
    private static final String ACTION_REVIEW = "com.android.camera.action.REVIEW";
    private boolean isBookmarkChecked = false;


    @BindView(R.id.photos_pager)
    HackyViewPager mViewPager;

    @BindView(R.id.PhotoPager_Layout)
    RelativeLayout activityBackground;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private boolean fullScreenMode, customUri = false;
    int position;

    private Album album;
    private ArrayList<Media> media;
    private MediaPagerAdapter adapter;

    private MenuItem menuBookmarkItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_media);
        ButterKnife.bind(this);


        String action = getIntent().getAction();

        if (action != null && action.equals(ACTION_OPEN_ALBUM)) {
            album = getIntent().getParcelableExtra("album");
            position = getIntent().getIntExtra("position", 0);
            media = getIntent().getParcelableArrayListExtra("media");
        } else if (getIntent().getData() != null) {
            loadUri(getIntent().getData());
        }

        if (savedInstanceState != null) {
            mViewPager.setLocked(savedInstanceState.getBoolean(ISLOCKED_ARG, false));
        }

        adapter = new MediaPagerAdapter(getSupportFragmentManager(), media);
        initUi();

    }

    private void loadUri(Uri uri) {
        album = new Album(uri.toString(), uri.getPath());
        album.settings = AlbumSettings.getDefaults();


        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) inputStream.close();
        } catch (Exception ex) {
            //TODO: EMOJI EASTER EGG - THERE'S NOTHING TO SHOW
            ((TextView) findViewById(R.id.nothing_to_show_text_emoji_easter_egg)).setText(R.string.error_occured_open_media);
            findViewById(R.id.nothing_to_show_placeholder).setVisibility(Hawk.get("emoji_easter_egg", 0) == 0 ? View.VISIBLE : View.GONE);
            findViewById(R.id.ll_emoji_easter_egg).setVisibility(Hawk.get("emoji_easter_egg", 0) == 1 ? View.VISIBLE : View.GONE);
        }

        media = new ArrayList<>(Collections.singletonList(new Media(getApplicationContext(), uri)));
        position = 0;
        customUri = true;
    }


    private void initUi() {

        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setupSystemUI();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) showSystemUI();
                    else hideSystemUI();
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
                SingleMediaActivity.this.position = position;

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

        //TODO: EMOJI EASTER EGG - THERE'S NOTHING TO SHOW
        ((TextView) findViewById(R.id.emoji_easter_egg)).setTextColor(getSubTextColor());
        ((TextView) findViewById(R.id.nothing_to_show_text_emoji_easter_egg)).setTextColor(getSubTextColor());


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
        getMenuInflater().inflate(R.menu.menu_view_pager, menu);

        menu.findItem(R.id.action_delete).setIcon(getToolbarIcon(CommunityMaterial.Icon.cmd_delete));
        menu.findItem(R.id.action_share).setIcon(getToolbarIcon(CommunityMaterial.Icon.cmd_share));
        menu.findItem(R.id.action_bookmark).setIcon(getToolbarIcon(CommunityMaterial.Icon.cmd_heart_outline));
        menu.findItem(R.id.action_edit).setIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_crop_rotate));
        menuBookmarkItem = menu.findItem(R.id.action_bookmark);

        isBookmarkChecked =checkDBForBookmark(media.get(position).getPath());
        setBookmarkIconColor();
        return true;
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            params.setMargins(0, 0, Measure.getNavigationBarSize(SingleMediaActivity.this).x, 0);
        else
            params.setMargins(0, 0, 0, 0);

        toolbar.setLayoutParams(params);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {

        menu.setGroupVisible(R.id.only_photos_options, !getCurrentMedia().isVideo());

        if (customUri) {
            menu.setGroupVisible(R.id.on_internal_storage, false);
            menu.setGroupVisible(R.id.only_photos_options, false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    final Uri imageUri = UCrop.getOutput(data);
                    if (imageUri != null && imageUri.getScheme().equals("file")) {
                        try {
                            //copyFileToDownloads(imageUri);
                            // TODO: 21/08/16 handle this better
                            if (StorageHelper.copyFile(getApplicationContext(), new File(imageUri.getPath()), new File(this.album.getPath()))) {
                                //((ImageFragment) adapter.getRegisteredFragment(this.album.getCurrentMediaIndex())).displayMedia(true);
                                Toast.makeText(this, R.string.new_file_created, Toast.LENGTH_SHORT).show();
                            }
                            //adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("ERROS - uCrop", imageUri.toString(), e);
                        }
                    } else
                        StringUtils.showToast(getApplicationContext(), "errori random");
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }


    private void displayAlbums() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void deleteCurrentMedia() {
        Media currentMedia = getCurrentMedia();

        boolean success = MediaHelper.deleteMedia(getApplicationContext(), currentMedia);

        if (success) {
            media.remove(currentMedia);

            if (media.size() == 0) {
                displayAlbums();
            }
        } else {
            Toast.makeText(this, R.string.delete_error, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        updatePageTitle(mViewPager.getCurrentItem());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(getCurrentMedia().getMimeType());
                share.putExtra(Intent.EXTRA_STREAM, getCurrentMedia().getUri());

                Toast.makeText(getApplicationContext(),getCurrentMedia().getMimeType()+"    "+getCurrentMedia().getUri()+" ",Toast.LENGTH_SHORT).show();
                startActivity(Intent.createChooser(share, getString(R.string.send_to)));
                return true;

            case R.id.action_edit:
                Uri mDestinationUri = Uri.fromFile(new File(getCacheDir(), "croppedImage.png"));
                Uri uri = Uri.fromFile(new File(getCurrentMedia().getPath()));
                UCrop uCrop = UCrop.of(uri, mDestinationUri);
                uCrop.withOptions(getUcropOptions());
                uCrop.start(SingleMediaActivity.this);
                break;

            case R.id.action_bookmark:
                media.get(position).setBookmark(!media.get(position).isBookmark());
                Bookmark tmp = new Bookmark();
                tmp.path = media.get(position).getPath();
                if(!isBookmarkChecked) {
                    isBookmarkChecked = true;
                    if(BookmarkDB.mBookmarkDao.addBookmark(tmp)) {
                        Toast.makeText(getApplicationContext(),"insert  "+isBookmarkChecked,Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                   long removedID = BookmarkDB.mBookmarkDao.getBookmarkIDByPath(tmp.path);
                    Boolean tmp1 = BookmarkDB.mBookmarkDao.deleteData(removedID);
                    isBookmarkChecked = false;
                 }

                 setBookmarkIconColor();


                break;

            case R.id.action_delete:
                final AlertDialog textDialog = AlertDialogsHelper.getTextDialog(SingleMediaActivity.this, R.string.delete, R.string.delete_photo_message);
                textDialog.setButton(DialogInterface.BUTTON_NEGATIVE, this.getString(R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textDialog.dismiss();
                    }
                });
                textDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.delete).toUpperCase(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Security.isPasswordOnDelete(getApplicationContext())) {

                            Security.askPassword(SingleMediaActivity.this, new Security.PasswordInterface() {
                                @Override
                                public void onSuccess() {
                                    deleteCurrentMedia();
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else
                            deleteCurrentMedia();
                    }
                });
                textDialog.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                //return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean checkDBForBookmark(String path) {
        return BookmarkDB.mBookmarkDao.fetchBookmarkByPath(path);
    }

    public Media getCurrentMedia() {
        return media.get(position);
    }

    private void updateBrightness(float level) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = level;
        getWindow().setAttributes(lp);
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mViewPager != null) {
            outState.putBoolean(ISLOCKED_ARG, mViewPager.isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    public void toggleSystemUI() {
        if ( getScreenMode())
            showSystemUI();
        else hideSystemUI();
    }

    public boolean getScreenMode(){

        return  fullScreenMode;
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
            colorTo = (ContextCompat.getColor(SingleMediaActivity.this, R.color.md_black_1000));
        } else {
            colorFrom = (ContextCompat.getColor(SingleMediaActivity.this, R.color.md_black_1000));
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

}

