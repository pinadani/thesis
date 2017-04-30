package cz.cvut.fit.pinadani.cardgamear.mvp.view;

import cz.cvut.fit.pinadani.cardgamear.model.User;

/**
 * TODO
 **/
public interface IProfileView extends IBaseView {
    void setUserData(User user, boolean defaultJoystickType);

    void updateSuccess();

    void setJoystickData(boolean defaultJoystickType);
}
