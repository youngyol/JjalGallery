package org.horaapps.leafpic.data.bookmark;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nasos on 2017-08-17.
 */

public class BookmarkDao extends DbContentProvider
        implements IBookmarkSchema, IBookmarkDao {

    private Cursor cursor;
    private ContentValues initialValues;
    public BookmarkDao(SQLiteDatabase db) {
        super(db);
    }
    public Bookmark fetchBookmarkById(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = "_id" + " = ?";
        Bookmark bookmark = new Bookmark();
        cursor = super.query(BOOKMARK_TABLE, BOOKMARK_COLUMNS, selection,
                selectionArgs, COLUMN_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                bookmark = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return bookmark;
    }



    public int delete(String path ) {


        return mDb.delete(IBookmarkSchema.BOOKMARK_TABLE, IBookmarkSchema.COLUMN_BOOKMARK_PATH + " = "+path, null);
    }

    public Boolean fetchBookmarkByPath(String path) {
        final String selectionArgs[] = { path };
        final String selection = "path" + " = ?";
         cursor = super.query(BOOKMARK_TABLE, BOOKMARK_COLUMNS, selection,
                selectionArgs, COLUMN_ID);
        if (cursor != null  && (cursor.getCount() > 0) ) {
            cursor.close();
            return true;
        }
        return false;
    }



    public long getBookmarkIDByPath(String path) {
        final String selectionArgs[] = { path };
        final String selection = "path" + " = ?";
        cursor = super.query(BOOKMARK_TABLE, BOOKMARK_COLUMNS, selection,
                selectionArgs, COLUMN_ID);

        Bookmark tmp = new Bookmark();
        if (cursor != null  && (cursor.getCount() > 0) ) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    tmp = cursorToEntity(cursor);
                    cursor.moveToNext();
                }
                cursor.close();

//            return true;
        }
//        return false;
        return tmp.id;
    }


    public List<Bookmark> fetchAllBookmarks() {
        List<Bookmark> bookList = new ArrayList<Bookmark>();
        cursor = super.query(BOOKMARK_TABLE, BOOKMARK_COLUMNS, null,
                null, COLUMN_ID);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Bookmark user = cursorToEntity(cursor);
                bookList.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return bookList;
    }

    public boolean addBookmark(Bookmark bookmark) {
        // set values
        setContentValue(bookmark);
        try {
            return super.insert(BOOKMARK_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Database", ex.getMessage());
            return false;
        }
    }


    @Override
    public boolean addBookmarks(List<Bookmark> bookmarks) {
        return false;
    }

    @Override
    public boolean deleteAllBookmarks() {
        return false;
    }

    protected Bookmark cursorToEntity(Cursor cursor) {

        Bookmark bookmark = new Bookmark();

        int idIndex;
        int bookmarkPathIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(COLUMN_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                bookmark.id = cursor.getInt(idIndex);
            }
            if (cursor.getColumnIndex(COLUMN_BOOKMARK_PATH) != -1) {
                bookmarkPathIndex = cursor.getColumnIndexOrThrow(
                        COLUMN_BOOKMARK_PATH);
                bookmark.path = cursor.getString(bookmarkPathIndex);
            }


        }
        return bookmark;
    }

    private void setContentValue(Bookmark bookmark) {
        initialValues = new ContentValues();
        initialValues.put(COLUMN_BOOKMARK_PATH, bookmark.path);
    }


    public boolean deleteData(long rowId) {
        return mDb.delete(IBookmarkSchema.BOOKMARK_TABLE, IBookmarkSchema.COLUMN_ID + "=" + rowId, null) > 0;
    }


    private ContentValues getContentValue() {
        return initialValues;
    }

}