package org.horaapps.leafpic;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import org.horaapps.leafpic.data.Album;
import org.horaapps.leafpic.data.HandlingAlbums;
import org.horaapps.leafpic.data.bookmark.BookmarkDB;

/**
 * Created by dnld on 28/04/16.
 */
public class App extends /*horaapps.org.liz.App*/ Application {

    public static final String TAG = App.class.getSimpleName();
    public static BookmarkDB mDb;
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        // Normal app init code...

        Hawk.init(this).build();

        mDb = new BookmarkDB(this);
        mDb.open();
        mInstance = this;
    }

    public static App getInstance() {
        return mInstance;
    }

    @Deprecated
    public Album getAlbum() {
        return Album.getEmptyAlbum();
    }

    @Deprecated
    public HandlingAlbums getAlbums() {
        return HandlingAlbums.getInstance(getApplicationContext());
    }


    @Override
    public void onTerminate() {
        mDb.close();
        super.onTerminate();
    }
}