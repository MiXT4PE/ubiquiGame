package com.ubiquigame.utility;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.os.ConfigurationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ubiquigame.controller.OverviewActivity;
import com.ubiquigame.network.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Contains every important utility variable or method
 */
public class Utility {

    public static Utility u = new Utility();

    private volatile boolean playing, searching, connecting, inLobby;

    public static final String ACCOUNT_TYPE = "com.ubiquigame.controller.account";

    static final Locale appLocale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

    public interface StringCallback {
        void set(String s);
    }
    public interface BoolCallback {
        void set(boolean b);
    }
    public interface BitmapCallback {
        void set(Bitmap b);
    }

    public void allValid(String username, String email, String password, Context context, BoolCallback callback){
        validUsername(username, context, a -> validEmail(email, context, b -> validPassword(password, c -> callback.set(a==null && b==null && c==null))));
    }

    public void validUsername(String username, Context context, StringCallback callback){
        if (username.length() < 3 || username.length() > 20) {
            callback.set("Username has an invalid length");
        } else if (!username.matches("[a-zA-Z0-9]{3,20}")){
            callback.set("Username has an invalid format");
        } else {
            checkTaken(username, "", context, taken -> callback.set(taken ? "Username already taken" : null));
        }
    }

    public void validEmail(String email, Context context, StringCallback callback){
        if (email.length() < 5 || email.length() > 50) {
            callback.set("Email has an invalid length");
        } else if (!email.matches("(?=.{5,50}$)[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])")){
            callback.set("Email has an invalid format");
        } else {
            checkTaken("", email, context, taken -> callback.set(taken ? "Email already taken - Sign in?" : null));
        }
    }

    public void validPassword(String password, StringCallback callback){
        if (password.length() < 3 || password.length() > 20) {
            callback.set("Password has an invalid length");
        } else if (!password.matches("[a-zA-Z0-9?!$%&*°-]{3,100}")){
            callback.set("Password has an invalid format");
        } else {
            callback.set(null);
        }
    }

    private void checkTaken(String username, String email, Context context, BoolCallback callback){
        Volley.getInstance(context).addToRequestQueue(new StringRequest(Request.Method.POST, "https://heess.me/external/php/checkUser.php",
            // check if the "taken" email or username is signed in on this device
            output -> callback.set(!(User.u != null && (username.equals(User.u.getUsername()) || email.equals(User.u.getEmail()))) && output.contains("invalid")),
            error -> Utility.u.showError("Network not available", context)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addAccount(AccountAuthenticatorActivity activity, String email, String password){
        AccountManager.get(activity).addAccountExplicitly(new Account(email, Utility.ACCOUNT_TYPE), password,null);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, User.u.getEmail());
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Utility.ACCOUNT_TYPE);
        activity.setAccountAuthenticatorResult(intent.getExtras());
        activity.setResult(RESULT_OK, intent);
    }

    public void removeAccount(Activity activity){
        AccountManager accManager = AccountManager.get(activity.getApplicationContext());
        Account[] accs = accManager.getAccountsByType(Utility.ACCOUNT_TYPE);
        // remove all associated accounts
        for (Account acc : accs) {
            accManager.removeAccount(acc, activity, null, null);
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showError(String err, Context context){
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, "Error: " + err, Toast.LENGTH_LONG).show());
    }

    public void getProfilePicture(Context context, BitmapCallback callback, String username){
        Volley.getInstance(context).addToRequestQueue(new StringRequest(Request.Method.POST, "https://heess.me/external/php/getAvatar.php", output -> {
            try {
                String avatar = String.valueOf(new JSONObject(output).get("avatar"));
                if (!avatar.equals("")) {
                    byte[] byteArray = Base64.decode(avatar.getBytes(), Base64.DEFAULT);
                    callback.set(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                } else{
                    callback.set(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> callback.set(null)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        });
    }

    public void bindNetworkToProcess(Context context, int transportType){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities.hasTransport(transportType)) {
                connectivityManager.bindProcessToNetwork(network);
            }
        }
    }

    public void stopThreads(){
        Utility.u.setSearching(false);
        Utility.u.setConnecting(false);
        Utility.u.setInLobby(false);
        Utility.u.setPlaying(false);
        User.u.setReady(false);
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public boolean isConnecting() {
        return connecting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public boolean isInLobby() {
        return inLobby;
    }

    public void setInLobby(boolean inLobby) {
        this.inLobby = inLobby;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}