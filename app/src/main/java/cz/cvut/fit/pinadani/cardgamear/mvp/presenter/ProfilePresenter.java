package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.model.User;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IProfileView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;
import cz.cvut.fit.pinadani.cardgamear.utils.NetworkUtils;


/**
 * Presenter managing profile
 * Created by daniel.pina@ackee.cz
 * 2/13/2016
 **/
public class ProfilePresenter extends BasePresenter<IProfileView> {
    public static final String TAG = ProfilePresenter.class.getName();

    @Inject
    ISPInteractor mSpInteractor;

    private DatabaseReference mDatabase;

    private User mUser;
    private FirebaseUser mFirebaseUser;

    private boolean mUpdateEmailInProgress = false;
    private boolean mUpdateUserInProgress = false;

    private boolean showInitProgress = true;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onTakeView(IProfileView profileView) {
        super.onTakeView(profileView);
        profileView.setJoystickData(mSpInteractor.isDefaultJoystickType());

        if(!NetworkUtils.isNetworkAvailable(profileView.getFragmentActivity())) {
            profileView.showSnackbar(R.string.message_no_internet);
            return;
        }
        if (mUser != null) {
            profileView.setUserData(mUser, mSpInteractor.isDefaultJoystickType());
            showInitProgress = false;
        } else {
            profileView.showProgress(showInitProgress);
            mDatabase.child("users")
                    .child(mFirebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            showInitProgress = false;
                            getView().showProgress(showInitProgress);
                            if (dataSnapshot.exists()) {
                                mUser = dataSnapshot.getValue(User.class);
                                profileView.setUserData(mUser, mSpInteractor.isDefaultJoystickType());
                            } else {
                                getView().showSnackbar(R.string.get_data_fail);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            showInitProgress = false;
                            getView().showProgress(showInitProgress);
                        }
                    });
        }

    }

    public void saveChanges(String username, String name, String email) {

        if (!TextUtils.equals(mUser.getEmail(), email)) {
            updateEmail(email);
        }

        if (!TextUtils.equals(mUser.getUsername(), username) ||
                !TextUtils.equals(mUser.getName(), name) ||
                !TextUtils.equals(mUser.getEmail(), email)) {
            updateUser(username, name, email);
        }
    }

    public void changeDirectionType(boolean defaultType){
        mSpInteractor.setDefaultJoystickType(defaultType);
    }

    private void updateUser(String username, String name, String email) {
        mUpdateUserInProgress = true;
        getView().showProgress(true);

        mUser.setEmail(email);
        mUser.setName(name);
        mUser.setUsername(username);

        mDatabase
                .child("users")
                .child(mFirebaseUser.getUid())
                .setValue(mUser)
                .addOnCompleteListener(task -> {
                    mUpdateUserInProgress = false;
                    if (!mUpdateEmailInProgress) {
                        if (getView() != null) {
                            getView().showProgress(false);
                        }
                    }

                    if (!task.isSuccessful()) {
                        Log.w(TAG, "createUserinDatabase:failed", task.getException());
                        getView().showSnackbar(R.string.update_fail);
                    } else {
                        mSpInteractor.setUserEmail(email);
                        mSpInteractor.setUserId(mFirebaseUser.getUid());
                        getView().showSnackbar(R.string.update_success);
                        getView().updateSuccess();
                    }
                });
    }

    private void updateEmail(String email) {
        getView().showProgress(true);
        mUpdateEmailInProgress = true;

        mFirebaseUser.updateEmail(email)
                .addOnCompleteListener(task -> {
                    mUpdateEmailInProgress = false;
                    if (!mUpdateUserInProgress) {
                        if (getView() != null) {
                            getView().showProgress(false);
                        }
                    }

                    if (task.isSuccessful()) {
                        getView().showSnackbar(R.string.changes_saved);
                        Log.d(TAG, "User email address updated.");
                    }
                });
    }
}
