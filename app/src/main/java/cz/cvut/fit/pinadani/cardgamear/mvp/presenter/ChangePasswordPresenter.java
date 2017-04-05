package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IChangePasswordView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * Presenter for change password logic
 **/
public class ChangePasswordPresenter extends BasePresenter<IChangePasswordView> {
    public static final String TAG = ChangePasswordPresenter.class.getName();

    @Inject
    ISPInteractor mSpInteractor;

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onTakeView(IChangePasswordView changePasswordView) {
        super.onTakeView(changePasswordView);
    }

    public void changePassword(String oldPassword, String newPassword) {
        getView().showProgress(true);

        mAuth.signInWithEmailAndPassword(mSpInteractor.getEmail(), oldPassword)
                .addOnCompleteListener(getView().getFragmentActivity(), task -> {

                    if (!task.isSuccessful()) {
                        getView().showProgress(false);
                        getView().showSnackbar(R.string.password_update_fail);
                    } else {
                        mFirebaseUser.updatePassword(newPassword)
                                .addOnCompleteListener(task2 -> {
                                    getView().showProgress(false);

                                    if (!task2.isSuccessful()) {
                                        Log.w(TAG, "change password:failed", task2.getException());
                                        try {
                                            throw task2.getException();
                                        } catch (FirebaseException e) {
                                            getView().showSnackbar(R.string.sign_up_fail_check);
                                        } catch (Exception e) {
                                            getView().showSnackbar(R.string.password_update_fail);
                                        }
                                    } else {
                                        getView().showSnackbar(R.string.password_update_success);
                                    }
                                });
                    }
                });


    }
}
