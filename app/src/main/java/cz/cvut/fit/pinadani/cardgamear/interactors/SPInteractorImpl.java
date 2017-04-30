package cz.cvut.fit.pinadani.cardgamear.interactors;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

/**
 * Implementation of shared preferences interactor
 * Created by daniel.pina@ackee.cz
 * 2/12/2016
 **/
public class SPInteractorImpl implements ISPInteractor {
    public static final String TAG = SPInteractorImpl.class.getName();
    private static final String SP_NAME = "sp_name";
    private static final String ACCESS_TOKEN_KEY = "auth_token";
    private static final String USER_EMAIL_KEY = "user_email";
    private static final String USER_ID = "user_id";
    private static final String START_PLAYER = "start_player";
    private static final String JOYSTICK_TYPE = "joystick_type";

    private SharedPreferences mSharedPreferences;
    private Context mCtx;
    private Handler mHandler = null;

    public SPInteractorImpl(Context ctx) {
        mCtx = ctx;
        mSharedPreferences = mCtx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void storeAccessToken(String token) {
//        if(token != null) {
//            mSharedPreferences.edit().putString(ACCESS_TOKEN_KEY, Constants.ACCCES_TOKEN_PREFIX + token).apply();
//        } else {
//            mSharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply();
//        }
    }

    @Override
    public String getAccessToken() {
        return mSharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    @Override
    public void setUserEmail(String email) {
        mSharedPreferences.edit().putString(USER_EMAIL_KEY, email).apply();
    }

    @Override
    public String getEmail() {
        return mSharedPreferences.getString(USER_EMAIL_KEY, null);
    }

    @Override
    public void setUserId(String userId) {
        mSharedPreferences.edit().putString(USER_ID, userId).apply();
    }

    @Override
    public String getUserId() {
        return mSharedPreferences.getString(USER_ID, null);
    }

    @Override
    public Handler getBTHandler() {
        return mHandler;
    }

    @Override
    public void setBTHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void setStartPlayer(boolean startPlayer) {
        mSharedPreferences.edit().putBoolean(START_PLAYER, startPlayer).apply();
    }

    @Override
    public boolean isStartPlayer() {
        return mSharedPreferences.getBoolean(START_PLAYER, false);
    }

    @Override
    public void setDefaultJoystickType(boolean defaultJoystickType) {
        mSharedPreferences.edit().putBoolean(JOYSTICK_TYPE, defaultJoystickType).apply();
    }

    @Override
    public boolean isDefaultJoystickType() {
        return mSharedPreferences.getBoolean(JOYSTICK_TYPE, true);
    }

    @Override
    public void clearAll() {
        mSharedPreferences.edit().clear().apply();
    }
}
