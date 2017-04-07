package cz.cvut.fit.pinadani.cardgamear.model;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import cz.cvut.fit.pinadani.cardgamear.utils.Constants;

public class Model3D {
    private static final String ANIMATION_WALK = "walk";
    public static final String ANIMATION_STAND = "dab";
    public static final String ANIMATION_ATTACK1 = "attack";
    public static final String ANIMATION_ATTACK2 = "attack";
    public static final String ANIMATION_DEFENCE = "defence";
    public static final String ANIMATION_WIN = "win";
    public static final String ANIMATION_DII = "die";

    private static final float MODEL_SCALE = 8.0f;

    private Vector2 mFinishPosition = new Vector2();
    /**
     * Uhel 0° je pokud model hledi z znacky rovne ke hraci. Pocatecni pozice.
     */
    private double mAngle = 90;
    private double mAngleBetweenActualAndFinishPosition;
    private double mDistanceBetweenActualAndFinishPosition;

    ModelInstance mModel;
    String mName;
    int mId;

    private String mNextAnim = null;

    private float mSpeed = 200;
    private float mSpeedOfChangeDirection = 50;

    private AnimationController mAnimationController;

    private Quaternion defaultQuaternion;
    private Vector3 defaultVector;

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
        mAnimationController.setAnimation(getNextAnim());
    }


    /**
     * Aktualizace pozice modelu
     *
     * @param delta cas od posledni aktualizace
     */
    void update(float delta) {
        mAnimationController.update(delta);

        updateAngel(delta);
        updatePosition(delta);

        //checkAchieve();
    }

    private boolean amINear(Vector3 firstPosition, Vector2 secondPosition, int deflection) {
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
        if (amINear(actualPosition, mFinishPosition, 1)) {
            return;
        }

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
    private double getAngleBetweenTwoPoints(Vector3 actualPosition, Vector2 finishPosition) {
        return Math.atan2(actualPosition.y - finishPosition.y, actualPosition.x - finishPosition.x) * 180.0d / Math.PI;
    }

    /**
     * Ziskani uhlu mezi aktualni pozici a cilovou pozici.
     */
    private double getDistanceBetweenTwoPoints(Vector3 actualPosition, Vector2 finishPosition) {
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
        double distance = getDistanceBetweenTwoPoints(mModel.transform.getTranslation(new Vector3()), mFinishPosition);
        if (distance < 10) {
            return distance / 10;
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


    void updateByJoystick(double angleDegrees, double power, Vector2 cameraPosition) {
        Vector3 actualPosition = mModel.transform.getTranslation(new Vector3());


        if (power != 0) {
            mAnimationController.setAnimation(ANIMATION_WALK, -1);
            //setNextAnimation(ANIMATION_WALK);
            angleDegrees = (angleDegrees + 180) % 360;
            Vector2 joystickVector = new Vector2((float) Math.cos(Math.toRadians(angleDegrees)),
                    (float) Math.sin(Math.toRadians(angleDegrees * 1)));

            float finalAngle = cameraPosition.angle(joystickVector);

            finalAngle *= -1;

            finalAngle = (finalAngle + 360) % 360;

            Vector2 finalVector = new Vector2(0, -1);
            finalVector.rotate(finalAngle);
            mFinishPosition.x = (int) (actualPosition.x + finalVector.x * 800 * power / 100);
            mFinishPosition.y = (int) (actualPosition.y + finalVector.y * 800 * power / 100);
        } else {
            mAnimationController.setAnimation(ANIMATION_STAND, -1);
            //setNextAnimation(null);
            mFinishPosition.x = actualPosition.x;
            mFinishPosition.y = actualPosition.y;
        }
    }

    public String getNextAnim() {
        if (mNextAnim == null) {
            return ANIMATION_STAND;
        }
        String nextAnim = mNextAnim;
        mNextAnim = null;
        return nextAnim;
    }

    public void setNextAnimation(String nextAnimation) {
        mNextAnim = nextAnimation;
    }
}
