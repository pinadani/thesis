package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.MainMenuPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IMainMenuView;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.BaseFragmentActivity;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(MainMenuPresenter.class)
public class MainMenuFragment extends BaseNucleusFragment<MainMenuPresenter> implements IMainMenuView {
    public static final String TAG = MainMenuFragment.class.getName();

    @Bind(R.id.btn_login_logout)
    Button mLoginLogoutButton;

    @Bind(R.id.layout_user)
    FrameLayout mUserLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Override
    public void pressBack() {

    }

    @Override
    protected void initAB() {
        if(getFragmentActivity().getSupportActionBar() != null) {
            getFragmentActivity().getSupportActionBar().hide();
        }
    }

    @OnClick(R.id.btn_login_logout)
    public void onLoginLogoutBtnClicked() {
        getPresenter().onLoginLogoutClicked();
    }

    @Override
    public void setLoginButtonText(int textResId) {
        mLoginLogoutButton.setText(getString(textResId));
    }

    @Override
    public void showProfileButton(boolean show) {
        mUserLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.btn_multiplayer)
    public void onMultiPlayerBtnClicked() {
        BaseFragmentActivity.startActivity(getActivity(), ConnectBluetoothFragment.class.getName());
    }

    @OnClick(R.id.btn_user)
    public void onUserBtnClicked() {
        BaseFragmentActivity.startActivity(getActivity(), ProfileFragment.class.getName());
    }
}