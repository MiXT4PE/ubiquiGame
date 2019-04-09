package com.ubiquigame.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ubiquigame.network.Search;
import com.ubiquigame.utility.ServerAdapter;
import com.ubiquigame.utility.User;
import com.ubiquigame.utility.Utility;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import ubiquigame.common.impl.Server;

public class OverviewActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private boolean viewControllerLayoutsPressedOnce = false;
    private Button searchButton, editButton, secretButton;
    private TextView textViewScore, textViewUsername;
    private ImageView imageViewAvatar;
    private RecyclerView serverView;
    private ProgressBar loading;
    private BlockingQueue<Server> serverQueue = new ArrayBlockingQueue<>(10);
    private ServerAdapter adapter;
    private ArrayList<Server> serverList = new ArrayList<>();

    // add new Servers to list
    @SuppressLint("HandlerLeak") // fix https://stackoverflow.com/questions/11407943/this-handler-class-should-be-static-or-leaks-might-occur-incominghandler
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            serverList.add((Server) msg.obj);
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // set flag
        Utility.u.setSearching(false);

        // ini layout elements and adapter
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewScore    = findViewById(R.id.textViewScore);
        editButton       = findViewById(R.id.editButton);
        searchButton     = findViewById(R.id.searchButton);
        secretButton     = findViewById(R.id.invisibleButton);
        serverView       = findViewById(R.id.serverView);
        loading          = findViewById(R.id.loading);
        adapter = new ServerAdapter(this, serverList);

        // change edit if guest
        if (User.u.getId() == 0) {
            editButton.setText(getResources().getString(R.string.account));
        }

        // ini recyclerView
        serverView = findViewById(R.id.serverView);
        serverView.setLayoutManager(new LinearLayoutManager(this));
        serverView.setAdapter(adapter);

        // set profile picture
        if(User.u.getAvatar() != null) {
            imageViewAvatar.setImageBitmap(User.u.getAvatar());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        search();

        // load user info in TextViews
        textViewUsername.setText(User.u.getUsername());
        String formatScore = getResources().getString(R.string.score) + User.u.getScore();
        textViewScore.setText(User.u.getId() > 0 ? formatScore : "");
    }

    public void toggleSearch(View view){
        // flip bool on button press
        Utility.u.setSearching(!Utility.u.isSearching());
        search();
    }

    private void search(){
        if (Utility.u.isSearching()){
            // visual feedback search began
            secretButton.setVisibility(View.GONE);
            imageViewAvatar.setVisibility(View.GONE);
            textViewUsername.setVisibility(View.GONE);
            textViewScore.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            serverView.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            searchButton.setText(getResources().getString(R.string.stopSearch));

            // set wifi only
            Utility.u.bindNetworkToProcess(this, NetworkCapabilities.TRANSPORT_WIFI);

            // start searching
            new Thread(new Search(serverQueue)).start();

            // start receiving servers
            new Thread(() -> {
                while (Utility.u.isSearching()) {
                    try {
                        // look for inputQueue objects
                        Message msg = handler.obtainMessage();
                        msg.obj = serverQueue.take();
                        handler.sendMessage(msg);  // more like handler.yeet(msg);)
                        runOnUiThread(() -> loading.setVisibility(View.GONE));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else{
            // clear server list
            int i = serverList.size();
            serverList.clear();
            adapter.notifyItemRangeChanged(0, i);

            // visual feedback search ended
            secretButton.setVisibility(View.VISIBLE);
            serverView.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            imageViewAvatar.setVisibility(View.VISIBLE);
            textViewUsername.setVisibility(View.VISIBLE);
            textViewScore.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            searchButton.setText(getResources().getString(R.string.search));
        }
    }

    public void editProfile(View view){
        startActivity(new Intent(this, User.u.getId() > 0 ? ProfileActivity.class : ChooseActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (Utility.u.isSearching()) {
            toggleSearch(null);
        } else {
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

    public void showControllerLayouts(View view) {
        if(viewControllerLayoutsPressedOnce){
            startActivity(new Intent(this, ViewControllerActivity.class));
        } else{
            viewControllerLayoutsPressedOnce = true;
            new Handler().postDelayed(() -> viewControllerLayoutsPressedOnce = false, 300);
        }
    }
}