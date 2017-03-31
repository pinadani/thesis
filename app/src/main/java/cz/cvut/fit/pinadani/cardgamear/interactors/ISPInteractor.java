package cz.cvut.fit.pinadani.cardgamear.interactors;

/**
 * Interactor that stores data to shared preferences
 * Created by daniel.pina@ackee.cz
 * 2/12/2016
 **/
public interface ISPInteractor {

    void storeAccessToken(String token);

    String getAccessToken();

    void clearAll();


    void setUserEmail(String email);

    String getEmail();

    void setUserId(String userId);

    String getUserId();
}
