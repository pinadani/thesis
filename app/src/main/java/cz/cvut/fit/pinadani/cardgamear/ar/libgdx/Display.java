package cz.cvut.fit.pinadani.cardgamear.ar.libgdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import cz.cvut.fit.pinadani.cardgamear.ar.vuforia.VuforiaRenderer;
import cz.cvut.fit.pinadani.cardgamear.model.Model3DList;

/**
 * Screen implementation responsible for model loading and calling renderer properly.
 */
public class Display implements Screen {

    public Model3DList mModels;
    public ModelBatch modelBatch;

    private Renderer mRenderer;

    public Display(VuforiaRenderer vuforiaRenderer) {

        modelBatch = new ModelBatch();

        mModels = new Model3DList();

        mRenderer = new Renderer(vuforiaRenderer);
    }


    @Override
    public void render(float delta) {
        mModels.updateModels();
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
}
