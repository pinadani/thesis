package cz.cvut.fit.pinadani.cardgamear.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * TODO add class description
 **/
@IgnoreExtraProperties
public class User implements Comparable<User> {
    private String mUsername;
    private String mEmail;
    private String mName;
    private int mScore;

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

    public User(String username, String email, String name, int score) {
        mUsername = username;
        mEmail = email;
        mName = name;
        mScore = score;
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

    @Override
    public int compareTo(User f) {

        if (mScore < f.mScore) {
            return 1;
        } else if (mScore > f.mScore) {
            return -1;
        } else {
            return 0;
        }

    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }
}
