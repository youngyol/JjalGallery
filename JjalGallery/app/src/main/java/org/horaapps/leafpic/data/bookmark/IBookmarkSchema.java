package org.horaapps.leafpic.data.bookmark;

/**
 * Created by nasos on 2017-08-17.
 */

public interface IBookmarkSchema {
    String BOOKMARK_TABLE = "bookmarks";
    String COLUMN_ID = "_id";
    String COLUMN_BOOKMARK_PATH = "path";
    String BOOKMARK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + BOOKMARK_TABLE
            + " ("
            + COLUMN_ID
            + " INTEGER PRIMARY KEY, "
            + COLUMN_BOOKMARK_PATH
            + " TEXT NOT NULL, "
            + ")";

    String[] BOOKMARK_COLUMNS = new String[] { COLUMN_ID,
            COLUMN_BOOKMARK_PATH};
}
