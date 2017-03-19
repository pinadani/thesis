package cz.cvut.fit.pinadani.cardgamear.model;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import cz.cvut.fit.pinadani.cardgamear.utils.Constants;

public class Model3D {
    private static final String ANIMATION_WALK = "walk";
    public static final String ANIMATION_STAND = "stand";

    private static final float MODEL_SCALE = 8.0f;

    Vector3 mFinishPosition = new Vector3(400, 0, 0);
    /**
     * Uhel 0° je pokud model hledi z znacky rovne ke hraci. Pocatecni pozice.
     */
    double mAngle = 90;
    double mAngleBetweenActualAndFinishPosition;
    double mDistanceBetweenActualAndFinishPosition;

    ModelInstance mModel;
    String mName;
    int mId;

    float mSpeed = 130;
    float mSpeedOfChangeDirection = 25;

    private AnimationController mAnimationController;

    Quaternion defaultQuaternion;
    Vector3 defaultVector;

    /**
     * Konstruktor 3D modelu. Probehne inicializace modelu a animace
     *
     * @param modelLoader Loader pro nacteni ze souboru
     * @param name        Nazev modelu
     */
    public Model3D(G3dModelLoader modelLoader, String name) {
        initModel(modelLoader, name);
        initAnimationController();
    }

    /**
     * Inicializace modelu. Nacteni ze souboru a otoceni do spravne polohy.
     *
     * @param modelLoader Loader pro nacteni ze souboru
     * @param name        Nazev modelu
     */
    private void initModel(G3dModelLoader modelLoader, String name) {
        Model model = modelLoader.loadModel(Gdx.files.getFileHandle(name, Files.FileType.Internal));
        mModel = new ModelInstance(model);

        mModel.transform.set(new Matrix4());
        mModel.transform.rotate(1.0F, 0.0F, 0.0F, 90.0F);
        mModel.transform.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);

        defaultQuaternion = mModel.transform.getRotation(new Quaternion());
        defaultVector = mModel.transform.getTranslation(new Vector3());
    }

    /**
     * Inicializace animace pro model. Automaticky se nastavuje animace stani na miste.
     */
    private void initAnimationController() {
        mAnimationController = new AnimationController(mModel);

        // Pick the current animation by name
        mAnimationController.setAnimation(ANIMATION_WALK, 1, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animation) {
                // this will be called when the current animation is done.
                // queue up another animation called "balloon".
                // Passing a negative to loop count loops forever.  1f for speed is normal speed.
                mAnimationController.queue(ANIMATION_WALK, -1, 1f, null, 0f);
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
            }
        });
    }


    /**
     * Aktualizace pozice modelu
     *
     * @param delta cas od posledni aktualizace
     */
    public void update(float delta) {
        mAnimationController.update(delta);

        updateAngel(delta);
        updatePosition(delta);

        checkAchieve();
    }

    private void checkAchieve() {
        Vector3 actualPosition = mModel.transform.getTranslation(new Vector3());
        if (areTwoPointsNear(actualPosition, mFinishPosition, 5)) {
            mFinishPosition = new Vector3(-800, 0, 0);
        }
    }

    private boolean areTwoPointsNear(Vector3 firstPosition, Vector3 secondPosition, int deflection) {
        if (firstPosition.x > secondPosition.x + deflection ||
                firstPosition.x < secondPosition.x - deflection ||
                firstPosition.y > secondPosition.y + deflection ||
                firstPosition.y < secondPosition.y - deflection) {
            return false;
        }
        return true;
    }

    /**
     * Aktualizace pozice modelu. Zavisle na aktualni pozici, cilove pozici a uhlu modelu.
     *
     * @param delta cas od posledni aktualizace
     */
    private void updatePosition(float delta) {
        double angleCoefficient = getAngleCoefficient();
        double distanceCoefficient = getDistanceCoefficient();
        float x = (float) (Math.cos(Math.toRadians(mAngle)) * -1 * angleCoefficient *
                distanceCoefficient * mSpeed * delta);
        float y = (float) (Math.sin(Math.toRadians(mAngle)) * -1 * angleCoefficient *
                distanceCoefficient * mSpeed * delta);

        mModel.transform.trn(x, y, 0);
    }

    /**
     * Aktualizace uhlu modelu. Zavisle na aktualni  a cilove pozici.
     *
     * @param delta cas od posledni aktualizace
     */
    private void updateAngel(float delta) {
        Vector3 actualPosition = mModel.transform.getTranslation(new Vector3());

        double left = (getAngleBetweenTwoPoints(actualPosition, mFinishPosition) - mAngle +
                Constants.CIRCEL_IN_DEGREES * 2) % Constants.CIRCEL_IN_DEGREES;
        double right = Constants.CIRCEL_IN_DEGREES - left;

        boolean leftSide = right > left;
        mAngleBetweenActualAndFinishPosition = leftSide ? left : right;
        mDistanceBetweenActualAndFinishPosition = getDistanceBetweenTwoPoints(actualPosition, mFinishPosition);

        if (0 != mAngleBetweenActualAndFinishPosition) {
            float angle = delta * mSpeedOfChangeDirection;

            if (angle > mAngleBetweenActualAndFinishPosition) {
                angle = (float) mAngleBetweenActualAndFinishPosition;
            }

            if (!leftSide) {
                angle *= -1;
            }

            mModel.transform.rotate(0, 1, 0, angle);
            mAngle += angle;
            mAngle += mAngle < 0 ? Constants.CIRCEL_IN_DEGREES : 0; //convert <0
        }

    }

    /**
     * Ziskani uhlu mezi aktualni pozici a cilovou pozici.
     */
    private double getAngleBetweenTwoPoints(Vector3 actualPosition, Vector3 finishPosition) {
        return Math.atan2(actualPosition.y - finishPosition.y, actualPosition.x - finishPosition.x) * 180.0d / Math.PI;
    }

    /**
     * Ziskani uhlu mezi aktualni pozici a cilovou pozici.
     */
    private double getDistanceBetweenTwoPoints(Vector3 actualPosition, Vector3 finishPosition) {
        return Math.sqrt(Math.pow((actualPosition.x - finishPosition.x), 2) + Math.pow(
                (actualPosition.y - finishPosition.y), 2));
    }

    private double getAngleCoefficient() {
        if (mAngleBetweenActualAndFinishPosition > 120) {
            return 0;
        }

        if (mAngleBetweenActualAndFinishPosition > 90) {
            return 0.1;
        }

        if (mAngleBetweenActualAndFinishPosition > 60) {
            return 0.3;
        }

        if (mAngleBetweenActualAndFinishPosition > 30) {
            return 0.6;
        }

        return 1;
    }

    private double getDistanceCoefficient() {
        if (mDistanceBetweenActualAndFinishPosition < 10) {
            return mDistanceBetweenActualAndFinishPosition / 10;
        }

        return 1;
    }

    /**
     * Getter pro model.
     *
     * @return
     */
    public ModelInstance getModel() {
        return mModel;
    }

    public void setFinishPosition(Vector3 vector3) {
        mFinishPosition = vector3;
    }
}
