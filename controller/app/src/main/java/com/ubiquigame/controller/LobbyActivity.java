package com.ubiquigame.controller;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubiquigame.network.Lobby;
import com.ubiquigame.utility.User;
import com.ubiquigame.utility.Utility;

import static android.view.View.GONE;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // set flag
        Utility.u.setInLobby(true);

        // get parameters
        Bundle parameters = getIntent().getExtras();

        // initialize views
        CardView player1 = findViewById(R.id.player1);
        CardView player2 = findViewById(R.id.player2);
        CardView player3 = findViewById(R.id.player3);
        CardView player4 = findViewById(R.id.player4);
        TextView[] players = {player1.findViewById(R.id.playerInfo), player2.findViewById(R.id.playerInfo), player3.findViewById(R.id.playerInfo), player4.findViewById(R.id.playerInfo)};
        TextView[] playersReady = {player1.findViewById(R.id.playerReady), player2.findViewById(R.id.playerReady), player3.findViewById(R.id.playerReady), player4.findViewById(R.id.playerReady)};
        ImageView[] playersAvatar = {player1.findViewById(R.id.playerThumb), player2.findViewById(R.id.playerThumb), player3.findViewById(R.id.playerThumb), player4.findViewById(R.id.playerThumb)};
        // start lobby thread
        if (parameters != null && parameters.containsKey("IP")){
            new Thread(new Lobby(parameters.getString("IP"), players, playersReady, playersAvatar, this)).start();
        } else{
            Utility.u.showError( "Wrong activity parameters: parameter IP not found!", this);
        }

    }

    public void ready(View view){
        User.u.setReady(true); // Player.user.setReady(true)
        findViewById(R.id.readyButton).setVisibility(GONE);
    }

    @Override
    public void onBackPressed() {
        // do you really want to exit the lobby?
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit the lobby and return to search?");
        builder.setPositiveButton("Quit", (dialog, which) -> {
            Utility.u.stopThreads();
            finish();
        });
        builder.setNegativeButton("Stay", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }
}
