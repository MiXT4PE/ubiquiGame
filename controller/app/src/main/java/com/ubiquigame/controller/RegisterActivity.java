package com.ubiquigame.controller;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ubiquigame.network.Volley;
import com.ubiquigame.utility.User;
import com.ubiquigame.utility.Utility;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AccountAuthenticatorActivity implements View.OnFocusChangeListener {

    private EditText usernameInput, emailInput, passwordInput = null;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout = null;
    private String username, email, password = null;
    private Button registerButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // declare view items
        usernameInput  = findViewById(R.id.editTextUsername);
        emailInput     = findViewById(R.id.editTextEmail);
        passwordInput  = findViewById(R.id.editTextPassword);
        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout    = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        registerButton = findViewById(R.id.registerButton);

        // listen for focus change
        usernameInput.setOnFocusChangeListener(this);
        emailInput.setOnFocusChangeListener(this);
        passwordInput.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View input, boolean hasFocus) {
        if (input.equals(usernameInput) && !hasFocus) {
            Utility.u.validUsername(usernameInput.getText().toString(), this, err -> usernameLayout.setError(err));
        }
        if (input.equals(emailInput) && !hasFocus) {
            Utility.u.validEmail(emailInput.getText().toString(), this, err -> emailLayout.setError(err));
        }
        if (input.equals(passwordInput) && !hasFocus) {
            Utility.u.validPassword(passwordInput.getText().toString(), err -> passwordLayout.setError(err));
        }
    }

    public void createUser(View view){
        // get inputs
        username = usernameInput.getText().toString();
        email    = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        Utility.u.allValid(username, email, password, this, allValid -> {
            if (allValid){
                // disable button
                registerButton.setEnabled(false);

                // hide keyboard
                Utility.hideKeyboard(this);

                // send request
                Volley.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, "https://heess.me/external/php/register.php", output -> {
                    if (TextUtils.isDigitsOnly(output)) {
                        // create local user
                        Log.d("Debug: ", output);
                        User.u = new User(Integer.valueOf(output),null, username, email, 0);
                        Toast.makeText(getApplicationContext(), "User successfully registered", Toast.LENGTH_LONG).show();

                        //save login to Android Account Manager
                        Utility.u.addAccount(this, User.u.getEmail(), password);

                        // start next activity
                        startActivity(new Intent(RegisterActivity.this, OverviewActivity.class));
                    } else {
                        // show error
                        Utility.u.showError(output, this);
                    }
                }, error -> Utility.u.showError("Network not available", this)) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("email", email);
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
                registerButton.setEnabled(true);
            } else {
                Utility.u.validUsername(usernameInput.getText().toString(), this, err -> usernameLayout.setError(err));
                Utility.u.validEmail(emailInput.getText().toString(), this, err -> emailLayout.setError(err));
                Utility.u.validPassword(passwordInput.getText().toString(), err -> passwordLayout.setError(err));
            }
        });
    }
}
