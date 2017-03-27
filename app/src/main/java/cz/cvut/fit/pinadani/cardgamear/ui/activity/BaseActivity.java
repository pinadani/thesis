package cz.cvut.fit.pinadani.cardgamear.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import cz.cvut.fit.pinadani.cardgamear.R;


/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {12/2/2016}
 **/
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getName();

    private static final String EXTRA_FRAGMENT_NAME = "fragment";
    private static final String EXTRA_ARGUMENTS = "arguments";
    public static final int CONTENT_VIEW_ID = R.id.fragment_container;

    private View mContentView;

    protected String getFragmentName() {
        return getIntent().getStringExtra(EXTRA_FRAGMENT_NAME);
    }

    /**
     * gets intent for starting new activity with fragment defined by fragment name and passes extras to the starting intent
     *
     * @param ctx
     * @param fragmentName fragment to instantiate
     * @param args         to pass to the instantiated fragment
     */
    public static Intent generateIntent(Context ctx, String fragmentName, Bundle args) {
        return new Intent(ctx, BaseActivity.class).putExtra(EXTRA_FRAGMENT_NAME, fragmentName).putExtra
                (EXTRA_ARGUMENTS, args);
    }

    public static void startActivity(Context ctx, String fragmentName) {
        Intent intent = new Intent(ctx, BaseActivity.class).putExtra(EXTRA_FRAGMENT_NAME, fragmentName);
        ctx.startActivity(intent);
    }

    public View getContentView() {
        return mContentView;
    }

    /**
     * Method that creates content view with fragment container. Container must have id set to R.id.fragmentContainer
     */

    protected View onCreateContentView() {
        return getLayoutInflater().inflate(R.layout.activity_base, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        mContentView = onCreateContentView();
//
//        setContentViewInternal(mContentView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        initToolbar();
//        String fragmentName = getFragmentName();
//        if (fragmentName == null) {
//            finish();
//            return;
//        }
//
//        Bundle args = getIntent().getBundleExtra(EXTRA_ARGUMENTS);
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentName);
//        if ((fragment == null) && (savedInstanceState == null)) {
//            fragment = instantiateFragment(fragmentName);
//            if (args != null) {
//                fragment.setArguments(args);
//            }
//            getSupportFragmentManager().beginTransaction().add(CONTENT_VIEW_ID, fragment, fragment.getClass().getName()).commit();
//        }

    }

    protected void setContentViewInternal(View view, ViewGroup.LayoutParams params) {
        setContentView(view, params);
    }


    /**
     * Init activity actionbar as toolbar from layout. Child activity should override this if it dont want actionbar present
     */
    protected void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
    }

    /**
     * instantiates the fragment
     *
     * @return
     */
    protected Fragment instantiateFragment(String fragmentName) {
        return Fragment.instantiate(this, fragmentName);
    }
}
