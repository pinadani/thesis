package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ChangePasswordPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IChangePasswordView;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.UiUtils;
import nucleus.factory.RequiresPresenter;

/**
 * Fragment to change password
 * Created by Daniel Pina
 **/
@RequiresPresenter(ChangePasswordPresenter.class)
public class ChangePasswordFragment extends BaseNucleusFragment<ChangePasswordPresenter>
        implements IChangePasswordView {
    public static final String TAG = ChangePasswordFragment.class.getName();

    @Bind(R.id.edit_old_password)
    EditText mOldPassword;

    @Bind(R.id.edit_new_password)
    EditText mNewPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.change_password_title);
    }

    @Override
    public void pressBack() {
    }

    @OnClick(R.id.btn_change_password)
    public void onChangePasswordClicked() {
        UiUtils.hideKeyboard(mOldPassword);
        getPresenter().changePassword(mOldPassword.getText().toString(), mNewPassword.getText().toString());
    }

    @Override
    protected void initAB() {
        baseSettingsAB();
    }
}
