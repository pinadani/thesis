package cz.cvut.fit.pinadani.cardgamear.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.ArrayList;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {3/18/2017}
 **/
public class Model3DList {
    ArrayList<Model3D> mModels = new ArrayList<>();
    // Model3D loader needs a binary json reader to decode
    UBJsonReader mJsonReader = new UBJsonReader();
    // Create a model loader passing in our json reader
    G3dModelLoader mModelLoader = new G3dModelLoader(mJsonReader);

    String[] modelNames = {"maxanim.g3db"};

    public Model3DList() {
        for (String name: modelNames) {
            mModels.add(new Model3D(mModelLoader, name));
        }
    }

    public void updateModels() {
        float delta = Gdx.graphics.getDeltaTime();
        for (Model3D model: mModels) {
            model.update(delta);
        }
    }

    public ArrayList<Model3D> getModels() {
        return mModels;
    }
}
