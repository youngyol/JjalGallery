package org.horaapps.leafpic.data.bookmark;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nasos on 2017-08-17.
 */

public class BookmarkDB {
    private static final String TAG = "MyDatabase";
    private static final String DATABASE_NAME = "bookmark_database.db";
    private DatabaseHelper mDbHelper;
    // Increment DB Version on any schema change
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;
    public static BookmarkDao mBookmarkDao;



    public BookmarkDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

        mBookmarkDao = new BookmarkDao(mDb);

        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public BookmarkDB(Context context) {
        this.mContext = context;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IBookmarkSchema.BOOKMARK_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w(TAG, "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            db.execSQL("DROP TABLE IF EXISTS "
                    + IBookmarkSchema.BOOKMARK_TABLE);
            onCreate(db);

        }
    }


}
