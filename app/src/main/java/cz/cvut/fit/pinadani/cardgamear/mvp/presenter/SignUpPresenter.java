package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.model.User;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.ISignUpView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * TODO add class description
 **/
public class SignUpPresenter extends BasePresenter<ISignUpView> {
    public static final String TAG = SignUpPresenter.class.getName();
    @Inject
    ISPInteractor mSpInteractor;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onTakeView(ISignUpView signUpView) {
        super.onTakeView(signUpView);
    }

    public void signUp(String email, String password) {
        if (!isValid(email, password)) {
            return;
        }
        getView().showProgress(true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getView().getFragmentActivity(), task -> {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signUp:failed", task.getException());

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            getView().showSnackbar(R.string.login_fail_invalid_email);
                        } catch (FirebaseException e) {
                            getView().showSnackbar(R.string.sign_up_fail_check);
                        } catch (Exception e) {
                            getView().showSnackbar(R.string.sign_up_fail);
                        }
                    } else {
                        onAuthSuccess(task.getResult().getUser());
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "createUserinDatabase:onComplete:" + task.isSuccessful());
                    if (getView() != null) {
                        getView().showProgress(false);
                    }

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "createUserinDatabase:failed", task.getException());
                        getView().showSnackbar(R.string.sign_up_fail);
                    } else {
                        mSpInteractor.setUserEmail(email);
                        mSpInteractor.setUserId(userId);
                        getView().showSnackbar(R.string.sign_up_success);

                        getView().signedUp();
                    }
                });
    }

    private boolean isValid(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            getView().showValidFailEmpty();
            return false;
        }
        if (TextUtils.isEmpty(password)) {

            getView().showValidFailEmptyPassword();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getView().showValidFailEmail();
            return false;
        }
        return true;
    }
}
