package cz.cvut.fit.pinadani.cardgamear.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.model.User;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ProfilePresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IProfileView;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.BaseFragmentActivity;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.UiUtils;
import nucleus.factory.RequiresPresenter;

/**
 * Fragment with profile
 */
@RequiresPresenter(ProfilePresenter.class)
public class ProfileFragment extends BaseNucleusFragment<ProfilePresenter> implements IProfileView {
    public static final String TAG = ProfileFragment.class.getName();

    @Bind(R.id.txt_name)
    TextView mName;

    @Bind(R.id.txt_email)
    TextView mEmail;

    @Bind(R.id.txt_username)
    TextView mUsername;

    @Bind(R.id.img_edit_name)
    ImageView mEditName;

    @Bind(R.id.img_edit_username)
    ImageView mEditUsername;

    @Bind(R.id.img_edit_email)
    ImageView mEditEmail;

    @Bind(R.id.direction_checkox)
    CheckBox mCheckBox;

    private MenuItem mDoneMenuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setOnEditClickListeners();
        UiUtils.hideKeyboard(getActivity().getWindow());
        return view;
    }

    private void setOnEditClickListeners() {
        mEditName.setOnClickListener(view -> {
                    mName.setEnabled(true);
                    mEditName.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_black));
                    UiUtils.showKeyboard(getActivity().getWindow());
                    mDoneMenuItem.setVisible(true);
                }
        );

        mEditEmail.setOnClickListener(view -> {
                    mEmail.setEnabled(true);
                    mEditEmail.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit_black));
                    UiUtils.showKeyboard(getActivity().getWindow());
                    mDoneMenuItem.setVisible(true);
                }
        );

        mEditUsername.setOnClickListener(view -> {
                    mUsername.setEnabled(true);
                    mEditUsername.setImageDrawable(ContextCompat.getDrawable(getActivity(), R
                            .drawable.ic_edit_black));
                    UiUtils.showKeyboard(getActivity().getWindow());
                    mDoneMenuItem.setVisible(true);
                }
        );
    }

    @Override
    public void initAB() {
        baseSettingsAB();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.profile);
    }

    @Override
    public void pressBack() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.done, menu);
        mDoneMenuItem = menu.findItem(R.id.action_done);
        mDoneMenuItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_done:
                getPresenter().saveChanges(mUsername.getText().toString(), mName.getText()
                        .toString(), mEmail.getText().toString());
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserData(User user, boolean defaultJoystickType) {
        if (user != null) {
            mName.setText(user.getName());
            mEmail.setText(user.getEmail());
            mUsername.setText(user.getUsername());
        }

        mCheckBox.setChecked(defaultJoystickType);
    }

    @Override
    public void updateSuccess() {
        mName.setEnabled(false);
        mUsername.setEnabled(false);
        mEmail.setEnabled(false);
        mEditName.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mode_edit));
        mEditUsername.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mode_edit));
        mEditEmail.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mode_edit));

        mDoneMenuItem.setVisible(false);
    }

    @Override
    public void setJoystickData(boolean defaultJoystickType) {
        mCheckBox.setChecked(defaultJoystickType);
    }

    @OnClick(R.id.btn_change_password)
    public void onChangePasswordClicked() {
        BaseFragmentActivity.startActivity(getActivity(), ChangePasswordFragment.class.getName());
    }

    @OnCheckedChanged(R.id.direction_checkox)
    public void OnCheckedChanged() {
        getPresenter().changeDirectionType(mCheckBox.isChecked());
    }
}