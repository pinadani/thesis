package cz.cvut.fit.pinadani.cardgamear.model;

import android.text.TextUtils;

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

import java.util.ArrayList;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.utils.App;
import cz.cvut.fit.pinadani.cardgamear.utils.Constants;

public class Model3D {

    @Inject
    ISPInteractor mSpInteractor;

    private static final String ANIMATION_WALK = "walk";
    public static final String ANIMATION_STAND = "stay";
    public static final String ANIMATION_ATTACK1 = "attack";
    public static final String ANIMATION_DEFENCE_BEGIN = "defenceStart";
    public static final String ANIMATION_DEFENCE_END = "defenceEnd";
    public static final String ANIMATION_WIN = "win";
    public static final String ANIMATION_DIE = "die";

    private static final int ANIMATION_WALK_ID = 0;
    public static final int ANIMATION_STAND_ID = 1;
    public static final int ANIMATION_ATTACK1_ID = 2;
    public static final int ANIMATION_DEFENCE_BEGIN_ID = 3;
    public static final int ANIMATION_DEFENCE_END_ID = 4;
    public static final int ANIMATION_WIN_ID = 5;
    public static final int ANIMATION_DIE_ID = 6;


    private static final float BULLET_LIFETIME = 2;

    private static final float MODEL_SCALE = 0.5f;

    private Vector2 mFinishPosition = new Vector2();
    /**
     * Uhel 0° je pokud model hledi z znacky rovne ke hraci. Pocatecni pozice.
     */
    private double mAngle = 90;
    private double mBulletAngle = 90;
    private double mAngleBetweenActualAndFinishPosition;
    private double mDistanceBetweenActualAndFinishPosition;

    private ModelInstance mModel;

    private ModelInstance mBulletModel;

    String mName;
    int mId;

    private ArrayList<String> mNextAnim = new ArrayList<>();

    private float mSpeed = 70;
    private float mBulletSpeed = 140;
    private float mSpeedOfChangeDirection = 70;
    private float mSpace = 30;
    private float mBulletSpace = 30;

    private int mHP = 100;
    private int mMaxHP = 100;

    private int mShortAttackPower = 20;
    private int mLongAttackPower = 15;


    private boolean mVisible = true;
    private boolean mVisibleBullet = false;

    private float mBulletLifeTime = 4;

    private AnimationController mAnimationController;

    private boolean mMakeAttackFirst = false;
    private boolean mMakeHitWithAttackFirst = false;
    private boolean mMakeAttackSecond = false;
    private boolean mMakeDefence = false;
    private boolean mMakeDefenceEnd = false;
    private boolean mMakeDefenceBegin = false;

    private int shortHitRange = 40;
    private boolean mDied = false;
    private boolean mWin = false;
    private boolean mEnd = false;
    private int mActualAnimation = ANIMATION_STAND_ID;

    private boolean mDefaultJoystickType = true;


    /**
     * Konstruktor 3D modelu. Probehne inicializace modelu a animace
     *
     * @param modelLoader Loader pro nacteni ze souboru
     * @param name        Nazev modelu
     */
    public Model3D(G3dModelLoader modelLoader, String name) {
        App.getAppComponent().inject(this);
        mDefaultJoystickType = mSpInteractor.isDefaultJoystickType();
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
        mModel.transform.trn(0, 0, 35);
        mFinishPosition = new Vector2(0, 0);
//        if (TextUtils.equals(name, "charm3.g3db")) {
            Model bulletModel = modelLoader.loadModel(Gdx.files.getFileHandle("fireball.g3db", Files
                    .FileType.Internal));
            mBulletModel = new ModelInstance(bulletModel);

            mBulletModel.transform.set(new Matrix4());
            mBulletModel.transform.rotate(1.0F, 0.0F, 0.0F, 90.0F);
            mBulletModel.transform.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
//        }
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
     *  @param delta  cas od posledni aktualizace
     * @param models
     * @param moveModel
     */
    void update(float delta, ArrayList<Model3D> models, boolean moveModel) {
        if (mHP <= 0) {
            if(!mEnd) {
                mEnd = true;
                mDied = true;
                mAnimationController.setAnimation(ANIMATION_DIE, new AnimationController.AnimationListener() {
                    @Override
                    public void onEnd(AnimationController.AnimationDesc animation) {
                        mMakeDefence = false;
                        mMakeDefenceEnd = false;
                    }

                    @Override
                    public void onLoop(AnimationController.AnimationDesc animation) {

                    }
                });
            }
            mAnimationController.update(delta);
            return;
        }
        if(mWin){
            if(!mEnd) {
                mEnd = true;
                mAnimationController.setAnimation(ANIMATION_WIN, new AnimationController.AnimationListener() {
                    @Override
                    public void onEnd(AnimationController.AnimationDesc animation) {
                        mMakeDefence = false;
                        mMakeDefenceEnd = false;
                        mAnimationController.setAnimation(ANIMATION_STAND, -1);
                    }

                    @Override
                    public void onLoop(AnimationController.AnimationDesc animation) {

                    }
                });
            }
            mAnimationController.update(delta);
            return;
        }
        mAnimationController.update(delta);
        if (mVisibleBullet && moveModel) {
            updateBulletPosition(delta);
        }
        if (!mMakeAttackFirst && !mMakeAttackSecond && !mDied && !mWin && moveModel) {
            updateAngel(delta);
            if (!mMakeDefence) {
                updatePosition(delta, models);
            }
        }
    }

    private void updateBulletPosition(float delta) {
        float x = (float) (Math.cos(Math.toRadians(mBulletAngle)) * -1 * mBulletSpeed * delta);
        float y = (float) (Math.sin(Math.toRadians(mBulletAngle)) * -1 * mBulletSpeed * delta);

        mBulletModel.transform.trn(x, y, 0);

        mBulletLifeTime -= delta;

        if (mBulletLifeTime < 0) {
            mVisibleBullet = false;
        }
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
     * @param delta  cas od posledni aktualizace
     * @param models
     */
    private void updatePosition(float delta, ArrayList<Model3D> models) {
        double angleCoefficient = getAngleCoefficient();
        double distanceCoefficient = getDistanceCoefficient();
        float x = (float) (Math.cos(Math.toRadians(mAngle)) * -1 * angleCoefficient *
                distanceCoefficient * mSpeed * delta);
        float y = (float) (Math.sin(Math.toRadians(mAngle)) * -1 * angleCoefficient *
                distanceCoefficient * mSpeed * delta);

        mModel.transform.trn(x, y, 0);

        if (detectCollissions(models) || isTooFar()) {
            mModel.transform.trn(-x, -y, 0);
        }
    }

    private boolean isTooFar() {
        if(getDistanceBetweenTwoPoints(mModel.transform.getTranslation(new Vector3()),new Vector2
                (0,0)) > 1000 ){
            return true;
        }
        return false;
    }

    private boolean detectCollissions(ArrayList<Model3D> models) {
        for (Model3D model : models) {
            if (model.getModel() != mModel) {
                if (getDistanceBetweenTwoPoints(model.getModel().transform.getTranslation(new
                        Vector3()), mModel.transform.getTranslation(new Vector3())) < model
                        .getSpace() + mSpace) {
                    return true;
                }
            }
        }
        return false;
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

    private double getDistanceBetweenTwoPoints(Vector3 firstPosition, Vector3 secondPosition) {
        return getDistanceBetweenTwoPoints(firstPosition, new Vector2(secondPosition.x, secondPosition.y));
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

    public ModelInstance getBulletModel() {
        return mBulletModel;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public boolean isVisibleBullet() {
        return mVisibleBullet;
    }


    public void updateByButtons(boolean attackFirstClicked, boolean attackSecondClicked, boolean defenceClicked) {
        if (!mMakeAttackFirst && !mMakeAttackSecond && !mDied && !mWin) {

            if (!mMakeDefence && defenceClicked) {
                mMakeDefenceBegin = true;
                mMakeDefence = true;

                mActualAnimation = ANIMATION_DEFENCE_BEGIN_ID;
                mAnimationController.setAnimation(ANIMATION_DEFENCE_BEGIN, new AnimationController.AnimationListener() {
                    @Override
                    public void onEnd(AnimationController.AnimationDesc animation) {
                        mMakeDefenceBegin = false;
                    }

                    @Override
                    public void onLoop(AnimationController.AnimationDesc animation) {

                    }
                });
                return;
            }

            if (mMakeDefence && !defenceClicked && !mMakeDefenceEnd && !mMakeDefenceBegin) {
                mMakeDefenceEnd = true;
                mActualAnimation = ANIMATION_DEFENCE_END_ID;
                mAnimationController.setAnimation(ANIMATION_DEFENCE_END, new AnimationController.AnimationListener() {
                    @Override
                    public void onEnd(AnimationController.AnimationDesc animation) {
                        mMakeDefence = false;
                        mMakeDefenceEnd = false;
                        mAnimationController.setAnimation(ANIMATION_STAND, -1);
                    }

                    @Override
                    public void onLoop(AnimationController.AnimationDesc animation) {

                    }
                });
                return;
            }

            if (mMakeDefence) {
                return;
            }

            if (attackFirstClicked) {
                mMakeAttackFirst = true;
                mMakeHitWithAttackFirst = false;
                mActualAnimation = ANIMATION_ATTACK1_ID;
                mAnimationController.setAnimation(ANIMATION_ATTACK1, new AnimationController.AnimationListener() {
                    @Override
                    public void onEnd(AnimationController.AnimationDesc animation) {
                        mMakeAttackFirst = false;
                        mAnimationController.setAnimation(ANIMATION_STAND, -1);
                    }

                    @Override
                    public void onLoop(AnimationController.AnimationDesc animation) {

                    }
                });
            }

            if (attackSecondClicked && !mVisibleBullet) {
                mMakeAttackSecond = true;
                mActualAnimation = ANIMATION_ATTACK1_ID;
                mAnimationController.setAnimation(ANIMATION_ATTACK1, new AnimationController.AnimationListener() {
                    @Override
                    public void onEnd(AnimationController.AnimationDesc animation) {
                        mMakeAttackSecond = false;
                        fireFireball();
                        mAnimationController.setAnimation(ANIMATION_STAND, -1);
                    }

                    @Override
                    public void onLoop(AnimationController.AnimationDesc animation) {

                    }
                });
            }
        }
    }

    private void fireFireball() {
        Vector3 actualPosition = mModel.transform.getTranslation(new Vector3());
        mBulletModel.transform.setTranslation(actualPosition.x, actualPosition.y, actualPosition
                .z - 40);
        mBulletAngle = mAngle;

        Vector2 bulletVector = new Vector2((float) Math.cos(Math.toRadians(mAngle)) * mSpace * 2
                * -1,
                (float) Math.sin(Math.toRadians(mAngle * -1)) * mSpace * 2);

        mBulletModel.transform.trn(bulletVector.x, bulletVector.y, 0);
        mVisibleBullet = true;

        mBulletLifeTime = BULLET_LIFETIME;
    }

    void updateByJoystick(double angleDegrees, double power, Vector2 cameraPosition) {
        if (!mMakeAttackFirst && !mMakeAttackSecond && !mDied && !mWin) {
            Vector3 actualPosition = mModel.transform.getTranslation(new Vector3());


            if (power != 0) {
                if (!mMakeDefence) {
                    mAnimationController.setAnimation(ANIMATION_WALK, -1);
                    mActualAnimation = ANIMATION_WALK_ID;
                }
                //setNextAnimation(ANIMATION_WALK);
                angleDegrees = (angleDegrees + 180) % 360;
                if(mDefaultJoystickType) {
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
                    if(angleDegrees < 180) {
                        angleDegrees /= 10;
                    } else {
                        angleDegrees = ((angleDegrees - 360) / 10) + 360;
                    }

                    float angle = (float)((mAngle * -1) + 630 + angleDegrees) % 360;
                    Vector2 finalVector = new Vector2(0, 1);
                    finalVector.rotate(angle * -1);
                    mFinishPosition.x = (int) (actualPosition.x + finalVector.x * 800 * power / 100);
                    mFinishPosition.y = (int) (actualPosition.y + finalVector.y * 800 * power / 100);
                }

            } else {
                if (!mMakeDefence) {
                    mAnimationController.setAnimation(ANIMATION_STAND, -1);
                    mActualAnimation = ANIMATION_STAND_ID;
                }
                //setNextAnimation(null);
                mFinishPosition.x = actualPosition.x;
                mFinishPosition.y = actualPosition.y;
            }

        }
    }

    private String getNextAnim() {

        if (mNextAnim.size() == 0) {
            if (mMakeDefence) {
                return null;
            } else {
                mActualAnimation = ANIMATION_STAND_ID;
                return ANIMATION_STAND;
            }
        }

        String nextAnim = mNextAnim.get(0);
        mNextAnim.remove(0);


        if (TextUtils.equals(nextAnim, ANIMATION_DEFENCE_END)) {
            mMakeDefence = false;
        }
        return nextAnim;
    }

    public float getSpace() {
        return mSpace;
    }

    public int checkGetHits(Model3D model3D) {
        return checkShortAttack(model3D) + checkLongAttack(model3D);
    }

    public int checkShortAttack(Model3D model3D) {
        Vector3 actualPosition = mModel.transform.getTranslation(new Vector3());


        if (model3D.mMakeAttackFirst && !model3D.mMakeHitWithAttackFirst) {

            boolean amIInRange = getDistanceBetweenTwoPoints(model3D.getModel().transform.getTranslation(new
                    Vector3()), mModel.transform.getTranslation(new Vector3())) < model3D
                    .getSpace() + getSpace() + shortHitRange;

            boolean lookAtMe = Math.abs(getAngleBetweenTwoPoints(model3D.getModel().transform
                    .getTranslation(new Vector3()), new Vector2(actualPosition.x,
                    actualPosition.y)) - model3D.mAngle) < 20;

            if (amIInRange && lookAtMe) {
                mMakeHitWithAttackFirst = true;
                return model3D.mShortAttackPower;
            }
        }

        return 0;
    }

    public int checkLongAttack(Model3D model3D) {
        if (model3D.isVisibleBullet()) {
            if (getDistanceBetweenTwoPoints(mModel.transform.getTranslation(new
                    Vector3()), model3D.getBulletModel().transform.getTranslation(new Vector3()
            )) < model3D.mBulletSpace + mSpace) {
                model3D.mVisibleBullet = false;
                return model3D.mLongAttackPower;
            }
        }
        return 0;
    }

    public boolean checkBulletCollision(ArrayList<Model3D> models) {
        for (Model3D model : models) {
            if (model.getModel() != mModel) {
                if (getDistanceBetweenTwoPoints(model.getModel().transform.getTranslation(new
                        Vector3()), mBulletModel.transform.getTranslation(new Vector3())) < model
                        .getSpace() + mBulletSpace) {
                    mVisibleBullet = false;
                    return true;
                }
            }
        }
        return false;
    }

    public int getHP() {
        return mHP;
    }

    public void setHP(int HP) {
        mHP = HP;
    }

    public int getMaxHP() {
        return mMaxHP;
    }

    public void setMaxHP(int maxHP) {
        mMaxHP = maxHP;
    }

    public ModelState getStateBundle(boolean myPausedGame) {
        Vector3 actualPosition = mModel.transform.getTranslation(new Vector3());
        Quaternion quaternion = mModel.transform.getRotation(new Quaternion());
        ModelState modelState = new ModelState(actualPosition.x, actualPosition.y, actualPosition
                .z, mFinishPosition.x, mFinishPosition.y, quaternion.x, quaternion.y, quaternion
                .z, quaternion.w, mAngle, mBulletAngle,
                myPausedGame, mVisibleBullet, mActualAnimation);

        return modelState;
    }

    public void updateState(ModelState modelState) {
        if (modelState == null) {
            return;
        }
        mAngle = modelState.angle;
        mModel.transform.setTranslation(modelState.x, modelState.y, modelState.z);
        mFinishPosition.set(modelState.finishX, modelState.finishY);
        mBulletAngle = modelState.bulletAngle;
        mVisibleBullet = modelState.visibleBullet;
        mModel.transform.set(modelState.x, modelState.y, modelState.z, modelState.quaternionX,
                modelState.quaternionY, modelState.quaternionZ, modelState.quaternionW);

//        mModel.transform.set(new Matrix4(new Vector3(modelState.x, modelState.y, modelState.z),
//                new Quaternion(modelState.quaternionX, modelState.quaternionY,modelState
//                        .quaternionZ, modelState.quaternionW), new Vector3(1, 1, 1)));

    }

    public void setAngle(int angle) {
        mAngle = angle;
    }

    public void setFinishPosition(Vector2 position) {
        mFinishPosition = position;
    }

    public void reduceHP(int loseHPs) {
        mHP -= loseHPs;
    }

    public void updateAnimation(int animation) {
        switch (animation) {
            case ANIMATION_WALK_ID:
                mAnimationController.setAnimation(ANIMATION_WALK, -1);
                break;
            case ANIMATION_STAND_ID:
                mAnimationController.setAnimation(ANIMATION_STAND, -1);
                break;
            case ANIMATION_ATTACK1_ID:
                mAnimationController.setAnimation(ANIMATION_ATTACK1, -1);
                break;
            case ANIMATION_DEFENCE_BEGIN_ID:
                mAnimationController.setAnimation(ANIMATION_DEFENCE_BEGIN, -1);
                break;
            case ANIMATION_DEFENCE_END_ID:
                mAnimationController.setAnimation(ANIMATION_DEFENCE_END, -1);
                break;
            case ANIMATION_WIN_ID:
                mAnimationController.setAnimation(ANIMATION_WIN, -1);
                break;
            case ANIMATION_DIE_ID:
                mAnimationController.setAnimation(ANIMATION_DIE, -1);
                break;
        }
    }

    public void setWin(boolean win) {
        mWin = win;
    }

    public boolean isEnd() {
        return mEnd;
    }

    public boolean isWin() {
        return mWin;
    }
}
