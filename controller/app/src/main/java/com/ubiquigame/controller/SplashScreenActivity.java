package com.ubiquigame.controller;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ubiquigame.network.Volley;
import com.ubiquigame.utility.User;
import com.ubiquigame.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar = null;
    private TextView loadingTextView = null;
    private Button retryButton, proceedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash_screen);
        super.onCreate(savedInstanceState);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // load views
        progressBar = findViewById(R.id.progressBar);
        loadingTextView = findViewById(R.id.loadingTextView);
        retryButton = findViewById(R.id.retryButton);
        proceedButton = findViewById(R.id.proceedButton);

        loadingTextView.setVisibility(View.VISIBLE);

        // do once at start
        loadAccount(null);
    }

    public void proceed(View view){
        Utility.u.removeAccount(this);
        startActivity(new Intent(this, ChooseActivity.class));
    }

    public void loadAccount(View view){
        if(Utility.u.isNetworkAvailable(this)) {
            // inform about reason for load
            progressBar.setVisibility(View.VISIBLE);
            loadingTextView.setText(getResources().getString(R.string.checking));
            retryButton.setVisibility(View.GONE);
            proceedButton.setVisibility(View.GONE);

            // get device accounts
            AccountManager accManager = AccountManager.get(this);
            Account[] accs = accManager.getAccountsByType(Utility.ACCOUNT_TYPE);
            if (accs.length > 0) {
                String email = accs[0].name;
                String password = accManager.getPassword(accs[0]);

                Volley.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, "https://heess.me/external/php/signIn.php", output -> {
                    try {
                        if (!output.equals("null")) {
                            JSONObject JSONOutput = new JSONObject(output);
                            byte[] byteArray = Base64.decode(String.valueOf(JSONOutput.get("avatar")).getBytes(), Base64.DEFAULT);
                            Bitmap avatar = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            int id = Integer.valueOf(JSONOutput.getString("id"));
                            String username = JSONOutput.getString("username");
                            int score = Integer.valueOf(JSONOutput.getString("score"));
                            User.u = new User(id, avatar, username, email, score);
                            startActivity(new Intent(SplashScreenActivity.this, OverviewActivity.class));
                        } else {
                            Utility.u.removeAccount(this);
                            startActivity(new Intent(SplashScreenActivity.this, ChooseActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    Utility.u.removeAccount(this);
                    startActivity(new Intent(SplashScreenActivity.this, ChooseActivity.class));
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("identifier", email);
                        params.put("password", password);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                });
            } else {
                startActivity(new Intent(SplashScreenActivity.this, ChooseActivity.class));
            }
        } else{
            // inform user about connection issue
            progressBar.setVisibility(View.GONE);
            loadingTextView.setText(getResources().getString(R.string.noNetwork));
            retryButton.setVisibility(View.VISIBLE);
            proceedButton.setVisibility(View.VISIBLE);
        }
    }
}
