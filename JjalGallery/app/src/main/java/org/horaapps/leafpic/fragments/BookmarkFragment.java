package org.horaapps.leafpic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.MainActivity;
import org.horaapps.leafpic.adapters.BookmarksAdapter;
import org.horaapps.leafpic.data.bookmark.Bookmark;
import org.horaapps.leafpic.data.bookmark.BookmarkDB;
import org.horaapps.leafpic.util.Measure;
import org.horaapps.leafpic.views.GridSpacingItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import horaapps.org.liz.ThemeHelper;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * Created by nasos on 2017-08-20.
 */

public class BookmarkFragment  extends BaseFragment {
    private static final String TAG = "asd";
    @BindView(R.id.bookmark_album)
    RecyclerView rv;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;


    private GridSpacingItemDecoration spacingDecoration;
    private BookmarksAdapter adapter;
    private ArrayList<Bookmark> bookmarkItemDatas;

    private MainActivity act;

    @Override
    public boolean editMode() {
        return  false;
    }

    @Override
    public void clearSelected() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        int spanCount = 2;
        spacingDecoration = new GridSpacingItemDecoration(spanCount, Measure.pxToDp(3, getContext()), true);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(spacingDecoration);
        rv.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        rv.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
        bookmarkItemDatas = getBookmarks();


        refresh.setOnRefreshListener(this::display);
        adapter = new BookmarksAdapter(getContext(),bookmarkItemDatas);
        rv.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bookmark, null);
        ButterKnife.bind(this, v);


//        Toast.makeText(getActivity(),bookmarkItemDatas.size(),Toast.LENGTH_SHORT).show();
        return v;
    }




    public void refreshTheme(ThemeHelper t) {
        rv.setBackgroundColor(t.getBackgroundColor());
        adapter.refreshTheme(t);
        refresh.setColorSchemeColors(t.getAccentColor());
        refresh.setProgressBackgroundColorSchemeColor(t.getBackgroundColor());

    }

    public ArrayList<Bookmark> getBookmarks() {
        ArrayList<Bookmark> bookmarks =  new ArrayList<Bookmark>();
        return BookmarkDB.mBookmarkDao.fetchAllBookmarks();
    }

    private void display() {

        adapter.clear();
        bookmarkItemDatas=BookmarkDB.mBookmarkDao.fetchAllBookmarks();
        adapter = new BookmarksAdapter(getContext(),bookmarkItemDatas);
        rv.setAdapter(adapter);

        refresh.setRefreshing(false);

    }


}
