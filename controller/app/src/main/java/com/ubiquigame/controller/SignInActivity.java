package com.ubiquigame.controller;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ubiquigame.network.Volley;
import com.ubiquigame.utility.User;
import com.ubiquigame.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AccountAuthenticatorActivity implements TextWatcher {

    private EditText identifierInput, passwordInput = null;
    private String identifier, password = null;
    private Button signInButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // declare view items
        identifierInput = findViewById(R.id.editTextIdentifier);
        passwordInput = findViewById(R.id.editTextPassword);
        signInButton  = findViewById(R.id.signInButton);

        // listen for focus change
        identifierInput.addTextChangedListener(this);
        passwordInput.addTextChangedListener(this);

        // disable button
        signInButton.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
        signInButton.setEnabled(!TextUtils.isEmpty(identifierInput.getText().toString()) && !TextUtils.isEmpty(passwordInput.getText().toString()));
    }

    public void checkCredentials(View view){
        // disable button
        signInButton.setEnabled(false);

        // hide keyboard
        Utility.hideKeyboard(this);

        // get input
        identifier = identifierInput.getText().toString();
        password = passwordInput.getText().toString();

        // send request
        Volley.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, "https://heess.me/external/php/signIn.php", output -> {
            try {
                if (!output.equals("null")) {
                    JSONObject JSONOutput = new JSONObject(output);
                    byte[] byteArray = Base64.decode(String.valueOf(JSONOutput.get("avatar")).getBytes(), Base64.DEFAULT);
                    Bitmap avatar = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    int id = Integer.valueOf(JSONOutput.getString("id"));
                    String username = JSONOutput.getString("username");
                    String email = JSONOutput.getString("email");
                    int score = Integer.valueOf(JSONOutput.getString("score"));
                    // login
                    User.u = new User(id, avatar, username, email, score);
                    // save login to Android Account Manager
                    Utility.u.addAccount(this, User.u.getEmail(), password);
                    // start next activity
                    startActivity(new Intent(SignInActivity.this, OverviewActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong " + (identifier.contains("@") ? "E-mail" : "Username") + " or Password", Toast.LENGTH_LONG).show();
                    passwordInput.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Utility.u.showError("Network not available", this)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("identifier", identifier);
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

        // re-enable button
        signInButton.setEnabled(true);
    }
    @Override
    public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {}
    @Override
    public void afterTextChanged(Editable editable) {}
}