package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IArView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * Presenter for game screen
 **/
public class ArPresenter extends BasePresenter<IArView> {
    public static final String TAG = ArPresenter.class.getName();

    @Inject
    ISPInteractor mSpInteractor;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onTakeView(IArView arView) {
        super.onTakeView(arView);
    }

}
