package org.horaapps.leafpic.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsImageView;
import com.orhanobut.hawk.Hawk;

import org.horaapps.leafpic.BuildConfig;
import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.base.SharedMediaActivity;
import org.horaapps.leafpic.data.Album;
import org.horaapps.leafpic.data.Media;
import org.horaapps.leafpic.data.StorageHelper;
import org.horaapps.leafpic.data.bookmark.BookmarkDB;
import org.horaapps.leafpic.fragments.AlbumsFragment;
import org.horaapps.leafpic.fragments.BaseFragment;
import org.horaapps.leafpic.fragments.BookmarkFragment;
import org.horaapps.leafpic.fragments.EmptyFragment;
import org.horaapps.leafpic.fragments.JjalsFragment;
import org.horaapps.leafpic.fragments.RvMediaFragment;
import org.horaapps.leafpic.util.AlertDialogsHelper;
import org.horaapps.leafpic.util.StringUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class MainActivity extends SharedMediaActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    AlbumsFragment albumsFragment = new AlbumsFragment();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinator_main_layout)
    CoordinatorLayout mainLayout;

    private boolean pickMode = false;
    private boolean albumsMode = true;

    private View.OnClickListener photosOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Media m = (Media) v.findViewById(R.id.photo_path).getTag();
            if (!pickMode) {
                {
                    // TODO: 4/5/17 moveout
                    if (Hawk.get("video_instant_play", false) && m.isVideo()) {
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setDataAndType(StorageHelper.getUriForFile(getApplicationContext(), m.getFile()), m.getMimeType()));
                    } else {

                        Intent intent = new Intent(MainActivity.this, SingleMediaActivity.class);
                        intent.setAction(SingleMediaActivity.ACTION_OPEN_ALBUM);
                        startActivity(intent);

                    }
                }
            } else {
                setResult(RESULT_OK, new Intent().setData(m.getUri()));
                finish();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null)
            return;

        initUi();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, albumsFragment, "albums")
                .commit();

    }

    private void displayAlbums(boolean hidden) {
        albumsMode = true;
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        albumsFragment.displayAlbums(hidden);
    }

    public void displayMedia(Album album) {
        albumsMode = false;
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, RvMediaFragment.make(album), "media")
                .addToBackStack(null)
                .commit();
    }


    public void goBackToAlbums() {
        albumsMode = true;
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getSupportFragmentManager().popBackStack();
    }

    @Deprecated
    private boolean displayData(Intent data) {

        // TODO: 3/25/17 pick porcodio
        pickMode = data.getBooleanExtra(SplashScreen.PICK_MODE, false);
        switch (data.getIntExtra(SplashScreen.CONTENT, SplashScreen.ALBUMS_BACKUP)) {

            case SplashScreen.PHOTOS_PREFETCHED:
                //TODO ask password if hidden
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        getAlbums().loadAlbums(getApplicationContext(), getAlbum().isHidden());
                    }
                }).start();
                return true;
        }
        return false;
    }

    private void initUi() {

        setSupportActionBar(toolbar);

        // TODO: 3/25/17 organize better
        /**** DRAWER ****/
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
        };


        ((TextView) findViewById(R.id.txtVersion)).setText(BuildConfig.VERSION_NAME);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        findViewById(R.id.ll_drawer_Default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toolbar.setTitle("사용자 갤러리");
                initUi();
                drawer.closeDrawer(GravityCompat.START);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, albumsFragment, "albums")
                        .commit();


            }
        });

        findViewById(R.id.ll_drawer_jjal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                toolbar.setTitle("짤 갤러리");
                drawer.closeDrawer(GravityCompat.START);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new JjalsFragment(), "albums")
                        .commit();
            }
        });

        findViewById(R.id.ll_drawer_Bookmarks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toolbar.setTitle("북마크 갤러리");

                if (BookmarkDB.mBookmarkDao.hasBookmark()) {
                    drawer.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, new BookmarkFragment(), "albums")
                            .commit();
                } else {

                    drawer.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, new EmptyFragment(),"albums")
                            .commit();

                }
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Hawk.get("last_version_code", 0) < BuildConfig.VERSION_CODE) {
                    String titleHtml = String.format(Locale.ENGLISH, "<font color='%d'>%s <b>%s</b></font>", getTextColor(), getString(R.string.changelog), BuildConfig.VERSION_NAME),
                            buttonHtml = String.format(Locale.ENGLISH, "<font color='%d'>%s</font>", getAccentColor(), getString(R.string.view).toUpperCase());
                    Snackbar snackbar = Snackbar
                            .make(mainLayout, StringUtils.html(titleHtml), Snackbar.LENGTH_LONG)
                            .setAction(StringUtils.html(buttonHtml), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialogsHelper.showChangelogDialog(MainActivity.this);
                                }
                            });
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getBackgroundColor());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        snackbarView.setElevation(getResources().getDimension(R.dimen.snackbar_elevation));
                    snackbar.show();
                    Hawk.put("last_version_code", BuildConfig.VERSION_CODE);
                }
            }
        }).start();
    }

    @CallSuper
    @Override
    public void updateUiElements() {
        super.updateUiElements();
        //TODO: MUST BE FIXED
        toolbar.setPopupTheme(getPopupToolbarStyle());
        toolbar.setBackgroundColor(getPrimaryColor());

        /**** SWIPE TO REFRESH ****/

        setStatusBarColor();
        setNavBarColor();

        mainLayout.setBackgroundColor(getBackgroundColor());

        setScrollViewColor((ScrollView) findViewById(R.id.drawer_scrollbar));
        Drawable drawableScrollBar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_scrollbar);
        drawableScrollBar.setColorFilter(new PorterDuffColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP));

        findViewById(R.id.Drawer_Header).setBackgroundColor(getPrimaryColor());
        findViewById(R.id.Drawer_Body).setBackgroundColor(getDrawerBackground());
        findViewById(R.id.drawer_scrollbar).setBackgroundColor(getDrawerBackground());
        findViewById(R.id.Drawer_Body_Divider).setBackgroundColor(getIconColor());

        /** TEXT VIEWS **/
        int color = getTextColor();
        ((TextView) findViewById(R.id.Drawer_Default_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.Drawer_Bookmark_Item)).setTextColor(color);
        ((TextView) findViewById(R.id.Drawer_JJal_Item)).setTextColor(color);

        /** ICONS **/
        color = getIconColor();
        ((IconicsImageView) findViewById(R.id.Drawer_Default_Icon)).setIcon(GoogleMaterial.Icon.gmd_image);
        ((IconicsImageView) findViewById(R.id.Drawer_Bookmark_Icon)).setIcon(GoogleMaterial.Icon.gmd_bookmark_border);
        ((IconicsImageView) findViewById(R.id.Drawer_JJal_Icon)).setIcon(GoogleMaterial.Icon.gmd_insert_emoticon);

        setRecentApp(getString(R.string.app_name));
    }

    public void updateToolbar(String title, IIcon icon, View.OnClickListener onClickListener) {
        updateToolbar(title, icon);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    public void updateToolbar(String title, IIcon icon) {
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(getToolbarIcon(icon));
    }

    public void resetToolbar() {
        updateToolbar(
                "사용자 갤러리",
                GoogleMaterial.Icon.gmd_menu, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                });
    }

    public void nothingToShow(boolean status) {
        findViewById(R.id.nothing_to_show_placeholder).setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Deprecated
    public void checkNothing(boolean status) {
        //TODO: @jibo come vuo fare qua? o anzi sopra!
        ((TextView) findViewById(R.id.emoji_easter_egg)).setTextColor(getSubTextColor());
        ((TextView) findViewById(R.id.nothing_to_show_text_emoji_easter_egg)).setTextColor(getSubTextColor());

        if (status && Hawk.get("emoji_easter_egg", 0) == 1) {
            findViewById(R.id.ll_emoji_easter_egg).setVisibility(View.VISIBLE);
            findViewById(R.id.nothing_to_show_placeholder).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.ll_emoji_easter_egg).setVisibility(View.GONE);
            findViewById(R.id.nothing_to_show_placeholder).setVisibility(View.GONE);
        }
    }

    //region MENU

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (albumsMode) {
            if (!albumsFragment.onBackPressed()) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else{
                    if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
                    {
                        super.onBackPressed();
                        finish();
                    }
                    else
                    {
                        backPressedTime = tempTime;

                        Toasty.Config.getInstance()
                                .setTextColor(getResources().getColor(R.color.md_teal_400))
                                .apply();
                        Toasty.custom(MainActivity.this, "종료하려면 뒤로가기 버튼을 한 번 더 눌러주세요.", getResources().getDrawable(R.drawable.ic_toasty_success),
                                Color.WHITE, Toast.LENGTH_SHORT, true, true).show();
                        Toasty.Config.reset(); // Use this if you want to use the configuration above only once
                     }
                }
            }
        } else {
            if (!((BaseFragment) getSupportFragmentManager().findFragmentByTag("media")).onBackPressed())
                goBackToAlbums();
        }


    }



}
