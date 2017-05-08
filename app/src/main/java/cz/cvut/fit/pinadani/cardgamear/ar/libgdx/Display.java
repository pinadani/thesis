package cz.cvut.fit.pinadani.cardgamear.ar.libgdx;

import android.app.Activity;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.erz.joysticklibrary.JoyStick;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.ar.vuforia.VuforiaRenderer;
import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.model.Model3DList;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * Screen implementation responsible for model loading and calling renderer properly.
 */
public class Display implements Screen {


    @Inject
    ISPInteractor mSpInteractor;

    private JoyStick joystick;

    public Model3DList mModels;
    public ModelBatch modelBatch;

    private Renderer mRenderer;
    private boolean mSinglePlayer;

    public Display(VuforiaRenderer vuforiaRenderer, Activity activity, boolean singlePlayer) {
        App.getAppComponent().inject(this);

        mSinglePlayer = singlePlayer;

        modelBatch = new ModelBatch();

        mModels = new Model3DList(mSpInteractor.isStartPlayer(), mSinglePlayer);

        mRenderer = new Renderer(vuforiaRenderer, activity);
    }


    @Override
    public void render(float delta) {
        mRenderer.render(mModels);
    }

    @Override
    public void dispose() {
        mRenderer.dispose();
    }


    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void setJoystick(JoyStick joystick) {
        this.joystick = joystick;
    }

    public Renderer getRenderer() {
        return mRenderer;
    }

    public void setSinglePlayer(boolean singlePlayer) {
        mSinglePlayer = singlePlayer;
    }
}
