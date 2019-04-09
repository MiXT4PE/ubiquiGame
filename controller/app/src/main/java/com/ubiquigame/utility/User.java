package com.ubiquigame.utility;

import android.graphics.Bitmap;

/**
 * Contains every necessary information for a single user
 * These parameters are the name, email and score of a user aswell as a token (unused)
 * User is build as a singelton because there cant be more than one user logged in at the same time
 */
public class User {

    private int id;
    private Bitmap avatar;
    private String username;
    private String email;
    private int score;
    private long token;
    public static User u;
    private boolean ready;

    public User(int id, Bitmap avatar, String username, String email, int score) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.email = email;
        this.score = score;
        this.token = generateNumericToken();
    }

    /**
     * Generates a 20 digit numeric Token
     * @return numericToken
     */
    private static long generateNumericToken(){
        return (long)(Math.random()*20);
    }

    /**
     * Returns the id of a user
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the avatar of a user
     * @return avatar
     */
    public Bitmap getAvatar() {
        return avatar;
    }

    /**
     * Returns the username of a user
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the email of a user
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the score of a user
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the numericToken of a user
     * @return numericToken
     */
    public long getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * @deprecated
     */
    public String getScoreString() {
        return "Score: " + String.valueOf(score);
    }
}
