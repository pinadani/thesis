package cz.cvut.fit.pinadani.cardgamear.mvp.view;

import android.support.v4.app.FragmentActivity;

/**
 * Base view from which should every other view inherits
 */
public interface IBaseView {
    String TAG = IBaseView.class.getName();

    void pressBack();

    void showSnackbar(int textResId);

    void showProgress(boolean show);

    FragmentActivity getFragmentActivity();
}