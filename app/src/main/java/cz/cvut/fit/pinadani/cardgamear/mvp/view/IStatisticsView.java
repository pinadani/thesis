package cz.cvut.fit.pinadani.cardgamear.mvp.view;

import java.util.ArrayList;

import cz.cvut.fit.pinadani.cardgamear.model.User;

/**
 * View of statistics screen
 * Created by Daniel Pina
 **/
public interface IStatisticsView extends IBaseView {

    void showProgress(boolean b);

    void setData(ArrayList<User> users);
}
