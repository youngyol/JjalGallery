package org.horaapps.leafpic.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.base.SharedMediaActivity;
import org.horaapps.leafpic.data.Jjal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JjalActivity  extends SharedMediaActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String tmp;



    @BindView(R.id.drawer_jjal_layout)
    DrawerLayout drawer;
    @BindView(R.id.coordinator_jjal_layout)
    CoordinatorLayout mainLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjal);
        ButterKnife.bind(this);

        initFirebaseDatabase();
        initUi();

    }

    private void initUi() {
//        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawer,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) { }
            public void onDrawerOpened(View drawerView) { }
        };

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setStatusBarColor();
        setNavBarColor();
    }


    private void initFirebaseDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Animal");

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Jjal jjal = dataSnapshot.getValue(Jjal.class);
                Log.d("1dad11a", jjal.getUrl());
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


}
