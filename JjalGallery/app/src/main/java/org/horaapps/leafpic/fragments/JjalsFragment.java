package org.horaapps.leafpic.fragments;

import android.content.Context;
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

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.adapters.JjalAdapter;
import org.horaapps.leafpic.data.Jjal;
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

public class JjalsFragment extends BaseFragment {

    @BindView(R.id.jjal_album)
    RecyclerView rv;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refresh;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String tmp;

    private static final String TAG = "asd";


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
        ctx = getContext();
        int spanCount = 2;
        spacingDecoration = new GridSpacingItemDecoration(spanCount, Measure.pxToDp(3, getContext()), true);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(spacingDecoration);
        rv.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        rv.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));



        refresh.setOnRefreshListener(this::display);
        adapter = new JjalAdapter(getContext(), jjalItemDatas);

        rv.setAdapter(adapter);

//        setListener();

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

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Jjal jjal = dataSnapshot.getValue(Jjal.class);
                Log.d("1dad11a", jjal.getUrl());
                jjalItemDatas.add(jjal);
                Log.d("1da2222", jjalItemDatas.get(0).getUrl());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Jjal jjal = dataSnapshot.getValue(Jjal.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);

    }


    private void setListener() {


//        rv.addOnItemTouchListener(
//                new RecyclerItemClickListener(rv, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        String tmppath = bookmarkItemDatas.get(position).path;
//
//
//                        Intent intent = new Intent(getActivity(), SingleImageActivity.class);
//                        intent.putExtra("bookmarkImg", bookmarkItemDatas);
//                        intent.putExtra("position", position);
//                        getActivity().startActivity(intent);
//
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//                    }
//                }));

    }
    private void display() {

        adapter.clear();
        jjalItemDatas.clear();
        initFirebaseDatabase();

        adapter = new JjalAdapter(getContext(), jjalItemDatas);
        rv.setAdapter(adapter);

        refresh.setRefreshing(false);

    }
}
