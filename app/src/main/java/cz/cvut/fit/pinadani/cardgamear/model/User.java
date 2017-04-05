package cz.cvut.fit.pinadani.cardgamear.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * TODO add class description
 **/
@IgnoreExtraProperties
public class User {
    private String mUsername;
    private String mEmail;
    private String mName;

    public User() {
    }

    public User(String username, String email) {
        this.mUsername = username;
        this.mEmail = email;
    }

    public User(String username, String email, String name) {
        mUsername = username;
        mEmail = email;
        mName = name;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
