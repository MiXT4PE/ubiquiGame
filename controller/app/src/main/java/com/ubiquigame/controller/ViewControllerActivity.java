package com.ubiquigame.controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ViewControllerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void showControllerFace(View view) {
        Intent intent = new Intent(this, ControllerActivity.class);
        intent.putExtra("layout", this.getResources().getIdentifier(String.valueOf(view.getContentDescription()),"layout", this.getPackageName()));
        intent.putExtra("isViewing",true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, OverviewActivity.class));
    }
}
