package com.example.nasos.jjalgallery.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.nasos.jjalgallery.MainActivity;
import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;

import butterknife.BindView;

public class PasswordActivity extends BaseActivity {

    @BindView(R.id.pin_lock_view)
    PinLockView pinLockView ;
    @BindView(R.id.indicator_dots)
    IndicatorDots indicatorDots ;

    public static final String TAG = "PinLockView";



    @Override
    protected void initLayout() {


        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        pinLockView.setPinLength(4);
        pinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        indicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }

    @Override
    protected int getActivityId() {
        return R.layout.activity_password;
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
            Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

}
