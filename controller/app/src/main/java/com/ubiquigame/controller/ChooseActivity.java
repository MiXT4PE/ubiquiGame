package com.ubiquigame.controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ubiquigame.utility.User;

import java.util.Random;

public class ChooseActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void startSignIn(View view){
        startActivity(new Intent(this, SignInActivity.class));
    }

    public void startRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void startAsGuest(View view){
        User.u = new User(0, null, "guest#" + String.valueOf(new Random().nextInt(9999)), "guest", -1);
        startActivity(new Intent(this, OverviewActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.backAgain), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 3000);
        }
    }
}