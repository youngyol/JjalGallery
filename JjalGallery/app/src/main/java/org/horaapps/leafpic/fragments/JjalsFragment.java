package org.horaapps.leafpic.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.SingleJjalActivity;
import org.horaapps.leafpic.adapters.JjalAdapter;
import org.horaapps.leafpic.data.Jjal;
import org.horaapps.leafpic.util.Measure;
import org.horaapps.leafpic.util.RecyclerItemClickListener;
import org.horaapps.leafpic.views.GridSpacingItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import horaapps.org.liz.ThemeHelper;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * Created by nasos on 2017-08-20.
 */

public class JjalsFragment extends BaseFragment {


    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private GridLayoutManager mLayoutManager;

    @BindView(R.id.jjal_album)
    RecyclerView rv;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.progress_bar_jjal)
    SmoothProgressBar mProgressBar;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private String tmp;

    private static final String TAG = "asd";

    private int startIndex = 0;
    private int  GETTING_DATA_COUNT =10;

    private int MAX_DATA_COUNT =130;

    private GridSpacingItemDecoration spacingDecoration;
    private Context ctx;
    private JjalAdapter adapter;
    private ArrayList<Jjal> jjalItemDatas;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebaseDatabase();
    }

    @Override
    public boolean editMode() {
        return false;
    }

    @Override
    public void clearSelected() {

    }

    @Override
    public void refreshTheme(ThemeHelper themeHelper) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        mProgressBar.progressiveStart();
        ctx = getContext();


        int spanCount = 2;
        spacingDecoration = new GridSpacingItemDecoration(spanCount, Measure.pxToDp(3, getContext()), true);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(spacingDecoration);





        mLayoutManager = new GridLayoutManager(getActivity(), spanCount);

        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));


        refresh.setOnRefreshListener(this::display);

        adapter = new JjalAdapter(getContext(), jjalItemDatas);

        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    int lastPosition = ((GridLayoutManager) manager).findLastVisibleItemPosition();
                     if ((lastPosition + 2) >= (adapter.getSize() - 1) && startIndex + GETTING_DATA_COUNT == adapter.getSize()) {
                        if (startIndex + GETTING_DATA_COUNT < MAX_DATA_COUNT) {
                            startIndex = adapter.getSize();
                            adapter.loadMore();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        setListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_jjals, null);

        return v;
    }

    private void initFirebaseDatabase() {

        jjalItemDatas = new ArrayList<Jjal>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Animal");

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Jjal jjal = ds.getValue(Jjal.class);
                    Log.d("onChildAdded", jjal.getUrl());
                    jjalItemDatas.add(jjal);
                }

                Log.d("onDataChange", "data loading end");

                mProgressBar.progressiveStop();
                mProgressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //mDatabaseReference.addChildEventListener(mChildEventListener);
        mDatabaseReference.addValueEventListener(mValueEventListener);

    }


    private void setListener() {
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), SingleJjalActivity.class);
                        intent.putExtra("jjal", jjalItemDatas);
                        intent.putExtra("position", position);
                        ctx.startActivity(intent);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }


    private void display() {

        adapter.clear();
        jjalItemDatas.clear();
        initFirebaseDatabase();

        adapter = new JjalAdapter(getContext(), jjalItemDatas);
        rv.setAdapter(adapter);
        startIndex = 0;
        setListener();
        refresh.setRefreshing(false);

    }
}
