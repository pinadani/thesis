package cz.cvut.fit.pinadani.cardgamear.interactors;

import android.os.Handler;

/**
 * Interactor that stores data to shared preferences
 * Created by daniel.pina@ackee.cz
 * 2/12/2016
 **/
public interface ISPInteractor {

    void storeAccessToken(String token);

    String getAccessToken();

    void setDefaultJoystickType(boolean defaultJoystickType);

    boolean isDefaultJoystickType();

    void clearAll();


    void setUserEmail(String email);

    String getEmail();

    void setUserId(String userId);

    String getUserId();

    Handler getBTHandler();

    void setBTHandler(Handler handler);

    void setStartPlayer(boolean startPlayer);

    boolean isStartPlayer();
}
