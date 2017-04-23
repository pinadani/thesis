package cz.cvut.fit.pinadani.cardgamear.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector2;
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

    String[] modelNames = {"charm3.g3db", "charm3.g3db"};

    Model3D mMyModel = null;
    Model3D mOponentModel = null;

    public Model3DList() {
        boolean firstModel = true;
        for (String name : modelNames) {
            Model3D model3D = new Model3D(mModelLoader, name);
            if (firstModel) {
                mMyModel = model3D;
                firstModel = false;
            } else {
                mOponentModel = model3D;
                mOponentModel.getModel().transform.translate(400, 0, 0);
            }
            mModels.add(model3D);
        }
    }

    public void updateModels(double angleDegrees, double power, boolean attackFirstClicked, boolean attackSecondClicked, boolean defenceClicked, Vector2 cameraPosition, ModelState modelState) {
        mMyModel.updateByButtons(attackFirstClicked, attackSecondClicked, defenceClicked);
        mMyModel.updateByJoystick(angleDegrees, power, cameraPosition);
        mOponentModel.updateState(modelState);
        float delta = Gdx.graphics.getDeltaTime();
        for (Model3D model : mModels) {
            model.update(delta, mModels);
        }
    }

    public ArrayList<Model3D> getModels() {
        return mModels;
    }
}
