package org.horaapps.leafpic.data.bookmark;

import java.util.List;

/**
 * Created by nasos on 2017-08-17.
 */

public interface IBookmarkDao {
    public Bookmark fetchBookmarkById(int bookmark);
    public List<Bookmark> fetchAllBookmarks();
    // add user
    public boolean addBookmark(Bookmark bookmark);
    // add users in bulk
    public boolean addBookmarks(List<Bookmark> bookmarks);
    public boolean deleteAllBookmarks();
}
