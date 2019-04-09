package com.ubiquigame.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.ubiquigame.network.ControllerOutput;
import com.ubiquigame.utility.Utility;

import ubiquigame.common.impl.InputState;

public class ControllerActivity extends AppCompatActivity {

    private Bundle parameters = null;

    public interface BoolCallback {
        void set(boolean b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // user left lobby
        Utility.u.setInLobby(false);

        // enable fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // enable auto rotate
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        parameters = getIntent().getExtras();
        if (parameters != null && parameters.containsKey("isViewing") &&  parameters.containsKey("layout") && parameters.getBoolean("isViewing")){
            // show controller layout
            setContentView(parameters.getInt("layout"));

        } else if (parameters != null && parameters.containsKey("layout") && parameters.containsKey("ip")) {
            // show controller layout
            setContentView(parameters.getInt("layout"));

            // start playing
            Utility.u.setPlaying(true);
            new Thread(new ControllerOutput((String) parameters.get("ip"))).start();

            //Set onTouchListener for all Buttons
            setOnTouch(b -> InputState.s.setAccelerator(b), R.id.accelerateButton);
            setOnTouch(b -> InputState.s.setBoost(b), R.id.boostButton);
            setOnTouch(b -> InputState.s.setLeft(b), R.id.leftButton);
            setOnTouch(b -> InputState.s.setRight(b), R.id.rightButton);
            setOnTouch(b -> InputState.s.setUp(b), R.id.upButton);
            setOnTouch(b -> InputState.s.setDown(b), R.id.downButton);
            setOnTouch(b -> InputState.s.setA(b), R.id.aButton);
            setOnTouch(b -> InputState.s.setB(b), R.id.bButton);
            setOnTouch(b -> InputState.s.setNext(b), R.id.nextButton);
        }  else{
            Utility.u.stopThreads();
            startActivity(new Intent(this, OverviewActivity.class));
            Utility.u.showError("Something went terribly wrong.", this);
        }
    }

    /**
     * Sets the on touch listener for one Button
     */
    private void setOnTouch(BoolCallback callback, int id){
        try {
            findViewById(id).setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    callback.set(true);
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vib.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else{
                        vib.vibrate(10);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    callback.set(false);
                    v.performClick();
                }
                return true;
            });
        } catch (NullPointerException ignored) {}
    }

    @Override
    public void onBackPressed() {
        if (parameters.getBoolean("isViewing")){
            startActivity(new Intent(ControllerActivity.this, ViewControllerActivity.class));
        } else{
            // do you really want to exit the game?
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to quit the game and return to search?");
            builder.setPositiveButton("Quit", (dialog, which) -> {
                Utility.u.stopThreads();
                startActivity(new Intent(this, OverviewActivity.class));
            });
            builder.setNegativeButton("Stay", (dialog, which) -> dialog.cancel());
            builder.create().show();
        }
    }
}