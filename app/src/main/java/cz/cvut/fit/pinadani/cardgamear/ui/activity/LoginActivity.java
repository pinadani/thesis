package cz.cvut.fit.pinadani.cardgamear.ui.activity;

import android.os.Bundle;

import cz.cvut.fit.pinadani.cardgamear.ui.fragment.LoginFragment;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getFragmentName() {
        return LoginFragment.class.getName();
    }
}
