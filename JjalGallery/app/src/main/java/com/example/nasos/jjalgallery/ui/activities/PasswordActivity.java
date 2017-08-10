package com.example.nasos.jjalgallery.ui.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.nasos.jjalgallery.R;
import com.example.nasos.jjalgallery.ui.base.BaseActivity;
import com.example.nasos.jjalgallery.util.SharedPreferencesService;

import butterknife.BindView;

/**
 * Created by nasos on 2017-08-08.
 */


public class PasswordActivity extends BaseActivity {

    @BindView(R.id.pin_lock_view)
    PinLockView pinLockView ;
    @BindView(R.id.indicator_dots)
    IndicatorDots indicatorDots ;
    @BindView(R.id.profile_name)
    TextView passwordTxt;

    public static final String TAG = "PinLockView";
    private  String password = "";
    public static final int FIRSTPASSWORD = 1;
    public static final int CHANGEPASSWORD = 2;
    public static final int WELCOME = 3;
    private int state ;
    private int flagForChangePW = 0 ;
    private SharedPreferencesService myPref;


    @Override
    protected void initLayout() {


        Intent intent = getIntent();
        state =intent.getExtras().getInt("state");
        setPwText();


        myPref = new SharedPreferencesService().getInstance();
        myPref.load(getApplicationContext());
        password = myPref.getPrefStringData("password");


        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(pinLockListener);
        pinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        pinLockView.enableLayoutShuffling();

        pinLockView.setPinLength(4);
        pinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        indicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }

    @Override
    protected int getActivityId() {
        return R.layout.activity_password;
    }

    private PinLockListener pinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
//             Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
//            startActivity(intent);
            switch (state){
                case  FIRSTPASSWORD:
                   setFirstPw(pin);
                    break;
                case CHANGEPASSWORD:
                    changePw(pin);
                    break;
                case WELCOME:
                    check(pin);
                    break;
                default:
                    break;

            }
            pinLockView.resetPinLockView();
//
//            if (pin.equals("1111")) {
//                Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
////                intent.putExtra("code", pin);
//                startActivity(intent);
//                finish();
//            } else {
//                pinLockView.resetPinLockView();
//                Toast.makeText(PasswordActivity.this, "Failed code, try again!", Toast.LENGTH_SHORT).show();
//            }
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

    private void check(String pin) {

        ViewGroup.LayoutParams params = passwordTxt.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        if(password.equals(pin)){
            finish();
        }
        else{
            passwordTxt.setText(R.string.check_previous_pw);
            passwordTxt.setTextSize(18);
            passwordTxt.setLayoutParams(params);
        }

    }

    private void changePw(String pin) {
        if (flagForChangePW == 0) {
            if (pin.equals(password)) {
                passwordTxt.setText(R.string.new_pw);
                pinLockView.resetPinLockView();
                flagForChangePW = 1;
            }
        } else if (flagForChangePW == 1){
            password = pin;
            flagForChangePW = 2;
            passwordTxt.setText(R.string.check_pw);
        }
        else {
            if (pin.equals(password)) {
                myPref.setPrefData("password", pin);
                myPref.setPrefData("hasPassword", true);
                Toast.makeText(getApplicationContext(),"변경 완료 ",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    private void setFirstPw(String pin) {
        if(password.equals("")){
            password = pin;
            passwordTxt.setText(R.string.check_pw);
        }
        else{
            if(pin.equals(password)) {

                myPref.setPrefData("password", pin);
                myPref.setPrefData("hasPassword", true);
                Toast.makeText(getApplicationContext(),"확인",Toast.LENGTH_SHORT).show();
                finish();
            }
            else  Toast.makeText(getApplicationContext(),"다시 ",Toast.LENGTH_SHORT).show();

        }
    }


    private void setPwText(){
        ViewGroup.LayoutParams params = passwordTxt.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        switch (state){
            case  FIRSTPASSWORD:
                passwordTxt.setText(R.string.new_pw);
                passwordTxt.setTextSize(18);
                passwordTxt.setLayoutParams(params);
                break;
            case CHANGEPASSWORD:
                passwordTxt.setText(R.string.check_previous_pw);
                passwordTxt.setTextSize(18);
                passwordTxt.setLayoutParams(params);
                break;
            default:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());

        finish();

    }

}