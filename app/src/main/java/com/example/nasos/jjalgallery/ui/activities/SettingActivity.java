package com.example.nasos.jjalgallery.ui.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;
import com.example.nasos.jjalgallery.util.SharedPreferencesService;
import com.zcw.togglebutton.ToggleButton;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.setting_pw_toggle)
    ToggleButton toggleBtn;
    @BindView(R.id.setting_change_pw_layout)
    RelativeLayout changeLayout;
    @BindView(R.id.setting_change_pw_btn)
    Button changeBtn;

    @OnClick(R.id.setting_change_pw_btn)
    public void onClick(View v) {
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra("state", 2);
        startActivity(intent);
    }

    private SharedPreferencesService myPref;
    private String password;

    @Override
    protected void initLayout() {
        myPref = new SharedPreferencesService().getInstance();
        myPref.load(getApplicationContext());
        password = myPref.getPrefStringData("password");

        initToggle();
        setListener();
//        changeLayout.setVisibility(View.INVISIBLE);


    }

    private void initToggle() {

        if (myPref.getPrefBooleanData("hasPassword")) {
            toggleBtn.setToggleOn();
            changeLayout.setVisibility(View.VISIBLE);
        } else {
            toggleBtn.setToggleOff();
            changeLayout.setVisibility(View.INVISIBLE);

        }
    }

    private void setListener() {

        toggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    if (password.equals("")) {
                        Intent intent = new Intent(SettingActivity.this, PasswordActivity.class);
                        intent.putExtra("state", 1);
                        startActivity(intent);
                    }
                    myPref.setPrefData("hasPassword", true);
                    changeLayout.setVisibility(View.VISIBLE);
                } else {
                    myPref.setPrefData("hasPassword", false);
                    changeLayout.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    @Override
    protected int getActivityId() {
        return R.layout.activity_setting;
    }


}
