package cz.cvut.fit.pinadani.cardgamear.mvp.view;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Interface for views that are able to retry api calls when an error occurs.
 * Created by Jan Stanek[jan.stanek@ackee.cz] on {28.6.16}
 **/
public interface IShowReloginView {

    /**
     * Show dialog and relogin fragment.
     *
     * @param uuid Unique id
     */
    void showUnauthorizedDialog(@NonNull UUID uuid);
}
