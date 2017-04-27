package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import cz.cvut.fit.pinadani.cardgamear.model.User;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IStatisticsView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * Presenter for statistics logic
 * Created by daniel.pina@ackee.cz
 **/
public class StatisticsPresenter extends BasePresenter<IStatisticsView> {
    public static final String TAG = StatisticsPresenter.class.getName();

    private DatabaseReference mDatabase;

    private ArrayList<User> mUsers;
    private FirebaseUser mFirebaseUser;

    private boolean showInitProgress = true;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onTakeView(IStatisticsView statisticsView) {
        super.onTakeView(statisticsView);


        if (mUsers != null) {
            statisticsView.setData(mUsers);
            showInitProgress = false;
        } else {
            statisticsView.showProgress(showInitProgress);
            mDatabase.child("users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            showInitProgress = false;
                            getView().showProgress(showInitProgress);

                            mUsers = new ArrayList<>();

                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                User user = postSnapshot.getValue(User.class);
                                mUsers.add(user);
                            }
                            Collections.sort(mUsers);
                            getView().setData(mUsers);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            showInitProgress = false;
                            getView().showProgress(showInitProgress);
                        }
                    });
        }
    }


}
