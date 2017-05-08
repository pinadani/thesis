package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vuforia.DataSet;

import butterknife.ButterKnife;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.ar.libgdx.Engine;
import cz.cvut.fit.pinadani.cardgamear.ar.vuforia.AppSession;
import cz.cvut.fit.pinadani.cardgamear.ar.vuforia.VuforiaRenderer;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ArPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IArView;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import nucleus.factory.RequiresPresenter;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {5/5/2017}
 **/
@RequiresPresenter(ArPresenter.class)
public class ArFragment extends BaseNucleusFragment<ArPresenter>
        implements IArView {

    private AppSession session;

    private DataSet posterDataSet;
    private Engine mEngine;

    VuforiaRenderer mRenderer;
    Handler mHandler;
    private boolean isSinglePlayer = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_overlay, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected void initAB() {

    }
}
