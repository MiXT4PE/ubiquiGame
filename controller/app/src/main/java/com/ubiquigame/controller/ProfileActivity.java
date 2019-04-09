package com.ubiquigame.controller;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ubiquigame.network.Volley;
import com.ubiquigame.utility.User;
import com.ubiquigame.utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AccountAuthenticatorActivity {

    private EditText usernameEdit, emailEdit, passwordEdit = null;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout = null;
    private ImageView imageViewAvatar;
    private Button editButton = null;
    private Drawable originalDrawable = null;
    private String userPass, image = null;
    private boolean locked = true;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // enable portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        userPass = AccountManager.get(this).getPassword(AccountManager.get(this).getAccountsByType(Utility.ACCOUNT_TYPE)[0]);
        usernameEdit = findViewById(R.id.editTextUsername);
        emailEdit    = findViewById(R.id.editTextEmail);
        passwordEdit = findViewById(R.id.editTextPassword);
        editButton   = findViewById(R.id.editButton);
        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout    = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);

        usernameEdit.setText(User.u.getUsername());
        emailEdit.setText(User.u.getEmail());
        passwordEdit.setText(userPass);
        originalDrawable = usernameEdit.getBackground();

        usernameEdit.setFocusable(false);
        usernameEdit.setCursorVisible(false);
        usernameEdit.setBackgroundColor(Color.TRANSPARENT);
        emailEdit.setFocusable(false);
        emailEdit.setCursorVisible(false);
        emailEdit.setBackgroundColor(Color.TRANSPARENT);
        passwordEdit.setFocusable(false);
        passwordEdit.setCursorVisible(false);
        passwordEdit.setBackgroundColor(Color.TRANSPARENT);

        imageViewAvatar.setEnabled(false);
        imageViewAvatar.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        // set profile picture
        if(User.u.getAvatar() != null) {
            imageViewAvatar.setImageBitmap(User.u.getAvatar());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            try { // read file data
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                if(inputStream != null) {
                    byte[] b = new byte[1024];
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    int c;
                    while ((c = inputStream.read(b)) != -1) {
                        os.write(b, 0, c);
                    }
                    inputStream.close();
                    byte[] arr = os.toByteArray();
                    if (arr.length < 63000){
                        image = Base64.encodeToString(arr, Base64.DEFAULT);
                        imageViewAvatar.setImageBitmap(BitmapFactory.decodeByteArray(arr, 0, arr.length));
                    } else {
                        Utility.u.showError("Maximum file size is 60KB.", this);
                    }
                } else{
                    Utility.u.showError("Nullpointer Exception: InputStream is null, aborting edit", this);
                    startActivity(new Intent(this, OverviewActivity.class));
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void edit(View view){
        if (locked){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You will be logged out if you change your user data");
            builder.setPositiveButton("Continue", (dialog, which) -> {
                locked = !locked;
                activate(usernameEdit);
                activate(emailEdit);
                activate(passwordEdit);
                imageViewAvatar.setEnabled(true);
                editButton.setText(getResources().getString(R.string.save));
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.create().show();
        } else {
            String tempName = usernameEdit.getText().toString();
            String tempMail = emailEdit.getText().toString();
            String tempPass = passwordEdit.getText().toString();

            if (image != null || !tempName.equals(User.u.getUsername()) || !tempMail.equals(User.u.getEmail()) || !tempPass.equals(userPass)) {
                Utility.u.allValid(tempName, tempMail, tempPass, this, allValid -> {
                    if (allValid){
                        // hide keyboard
                        Utility.hideKeyboard(this);

                        // send request
                        Volley.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.POST, "https://heess.me/external/php/updateUser.php", output -> {
                            if (output.contains("success")) {
                                Toast.makeText(getApplicationContext(), "User data successfully updated", Toast.LENGTH_SHORT).show();
                                Utility.u.removeAccount(this);
                                startActivity(new Intent(this, ChooseActivity.class));
                            } else {
                                Utility.u.showError(output, this);
                            }
                        }, error -> Utility.u.showError(error.toString(), this)) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("userid", String.valueOf(User.u.getId()));
                                if (image != null) params.put("avatar", image);
                                params.put("newUsername", tempName);
                                params.put("newEmail", tempMail);
                                params.put("newPassword", tempPass);
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
                        Utility.u.validUsername(tempName, this, err -> usernameLayout.setError(err));
                        Utility.u.validEmail(tempMail, this, err -> emailLayout.setError(err));
                        Utility.u.validPassword(tempPass, err -> passwordLayout.setError(err));
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No information updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void logoutUser(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Logout from account?");
        builder.setPositiveButton("Logout", (dialog, which) -> {
            Utility.u.removeAccount(this);
            startActivity(new Intent(this, ChooseActivity.class));
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    private void activate(EditText editText){
        //editText.setGravity(Gravity.LEFT);
        editText.setFocusableInTouchMode(true);
        editText.setCursorVisible(true);
        editText.setBackground(originalDrawable);
        editText.setSelection(editText.getText().length());
    }
}
