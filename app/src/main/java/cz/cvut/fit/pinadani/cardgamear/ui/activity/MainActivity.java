package cz.cvut.fit.pinadani.cardgamear.ui.activity;

import android.os.Bundle;

import cz.cvut.fit.pinadani.cardgamear.ui.fragment.MainMenuFragment;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getFragmentName() {
        return MainMenuFragment.class.getName();
    }
}
