package cz.cvut.fit.pinadani.cardgamear.ar.libgdx;

import android.app.Activity;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.erz.joysticklibrary.JoyStick;
import com.vuforia.Matrix44F;
import com.vuforia.Tool;
import com.vuforia.TrackableResult;
import com.vuforia.VIDEO_BACKGROUND_REFLECTION;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.ar.vuforia.SampleMath;
import cz.cvut.fit.pinadani.cardgamear.ar.vuforia.VuforiaRenderer;
import cz.cvut.fit.pinadani.cardgamear.model.Model3D;
import cz.cvut.fit.pinadani.cardgamear.model.Model3DList;
import cz.cvut.fit.pinadani.cardgamear.model.ModelState;
import cz.cvut.fit.pinadani.cardgamear.service.BluetoothService;
import cz.cvut.fit.pinadani.cardgamear.utils.App;
import cz.cvut.fit.pinadani.cardgamear.utils.SerializationUtils;

//import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

/**
 * Class responsible for rendering and scene transformations.
 */
public class Renderer {


    private ImageButton pauseBtn;
    private ImageButton attackFirstBtn;
    private ImageButton attackSecondBtn;
    private ImageButton defenceBtn;
    private View pausedMeOverlay;
    private View pausedOponentOverlay;
    private View startOverlay;
    RoundCornerProgressBar hpProgress;

    private boolean mSendEverySecond = true;
    private JoyStick joystick;

    private PerspectiveCamera mCamera;
    private Environment mEnvironment;
    private ModelBatch modelBatch;
    private VuforiaRenderer vuforiaRenderer;
    private BluetoothService mBluetoothService;

    private boolean isAttackFirstDown = false;
    private boolean isAttackSecondDown = false;
    private boolean isDefenceDown = false;

    private boolean mMyPausedGame = false;
    private boolean mOponentPausedGame = false;
    private boolean mGameStarted = false;
    private boolean mGameStartedFull = false;
    private long mTimeGameStart = 0;
    private ModelState mModelState = null;

    private final Activity mActivity;
    private boolean mSinglePlayer = true;

    public Renderer(VuforiaRenderer arRenderer, Activity activity) {
        mActivity = activity;
        mEnvironment = new Environment();
        mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        mEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        mBluetoothService = ((App) App.getContext()).getBluetoothService();
        if (mBluetoothService != null) {
            mBluetoothService.setRenderer(this);
        }
        mCamera = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mCamera.near = 1f;
        mCamera.far = 30000f;
        //set mCamera into "Vuforia - style" direction
        mCamera.position.set(new Vector3(70f, 70f, 70f));
        mCamera.lookAt(new Vector3(0, 0, 1));

        this.vuforiaRenderer = arRenderer;

        modelBatch = new ModelBatch();

    }

    public void render(Model3DList models) {

        GL20 gl = Gdx.gl;

        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        TrackableResult[] results = null;

        if (vuforiaRenderer.mIsActive) {
            //render mCamera background and find targets
            results = vuforiaRenderer.processFrame();
        }

        gl.glEnable(GL20.GL_DEPTH_TEST);
        gl.glEnable(GL20.GL_CULL_FACE);


        setProjectionAndCamera(results, (float) Math.toDegrees(vuforiaRenderer
                .fieldOfViewRadians), models);
        modelBatch.begin(mCamera);

        gl.glDepthMask(true);
        for (Model3D model : models.getModels()) {
            if (model.isVisible()) {
                modelBatch.render(model.getModel(), mEnvironment);
            }
            if (model.isVisibleBullet()) {
                modelBatch.render(model.getBulletModel(), mEnvironment);
                //model.checkBulletCollision(models.getModels());
            }
        }

        models.getOponentModel().reduceHP(models.getOponentModel().checkGetHits(models.getMyModel()));
        if(mModelState != null && mModelState.oponentHp < models.getMyModel().getHP()){
            models.getMyModel().setHP(mModelState.oponentHp);
            updateHp(models.getMyModel().getHP(), models.getMyModel().getMaxHP());
        }

        modelBatch.end();

        gl.glDisable(GL20.GL_CULL_FACE);
        gl.glDisable(GL20.GL_DEPTH_TEST);
        gl.glDisable(GL20.GL_BLEND);
    }

    private void updateHp(int hp, int maxHp) {
        mActivity.runOnUiThread(() -> {
            hpProgress.setProgress(hp);
        });
    }

    private void setProjectionAndCamera(TrackableResult[] trackables, float filedOfView, Model3DList models) {

        updateMyModelState(models, mMyPausedGame, mGameStarted);


        if(!mGameStartedFull) {
            if (!mGameStarted) {
                mActivity.runOnUiThread(() -> startOverlay.setVisibility(View.VISIBLE));
            } else {
                TextView textView = (TextView) startOverlay.findViewById(R.id.start_overlay_text);
                if (mModelState != null && mModelState.started) {
                    if (mTimeGameStart == 0) {
                        mTimeGameStart = System.currentTimeMillis();
                    } else {
                        long timeFromStart = System.currentTimeMillis() - mTimeGameStart;
                        if (timeFromStart < 1000) {
                            mActivity.runOnUiThread(() ->textView.setText("3"));
                        } else {
                            if (timeFromStart < 2000) {
                                mActivity.runOnUiThread(() ->textView.setText("2"));
                            } else {
                                if (timeFromStart < 3000) {
                                    mActivity.runOnUiThread(() ->textView.setText("1"));
                                } else {
                                    if (timeFromStart < 4000) {
                                        mActivity.runOnUiThread(() ->textView.setText("GO"));
                                    } else {
                                        if (timeFromStart < 5000) {
                                            mActivity.runOnUiThread(() -> startOverlay
                                                    .setVisibility(View.GONE));
                                            mGameStartedFull = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    textView.setText(R.string.wait_for_oponent_start);
                }
            }
            return;
        }

        if (!mMyPausedGame) {
            mActivity.runOnUiThread(() -> pausedMeOverlay.setVisibility(View.GONE));
            if (mModelState != null && mModelState.paused) {
                mOponentPausedGame = true;
                mActivity.runOnUiThread(() -> pausedOponentOverlay.setVisibility(View.VISIBLE));
            } else {
                mOponentPausedGame = false;
                mActivity.runOnUiThread(() -> pausedOponentOverlay.setVisibility(View.GONE));
            }
        } else {
            mActivity.runOnUiThread(() -> pausedMeOverlay.setVisibility(View.VISIBLE));
        }

        if (trackables != null && trackables.length > 0) {
            //transform all content
            TrackableResult trackable = trackables[0];

            Matrix44F modelViewMatrix = Tool.convertPose2GLMatrix(trackable.getPose());
            float[] raw = modelViewMatrix.getData();

            float[] rotated;
            //switch axis and rotate to compensate coordinates change
            if (com.vuforia.Renderer.getInstance().getVideoBackgroundConfig().getReflection() == VIDEO_BACKGROUND_REFLECTION.VIDEO_BACKGROUND_REFLECTION_ON) {
                // Front mCamera
                rotated = new float[]{
                        raw[1], raw[0], raw[2], raw[3],
                        raw[5], raw[4], raw[6], raw[7],
                        raw[9], raw[8], raw[10], raw[11],
                        raw[13], raw[12], raw[14], raw[15]
                };
            } else {
                // Back mCamera
                rotated = new float[]{
                        raw[1], -raw[0], raw[2], raw[3],
                        raw[5], -raw[4], raw[6], raw[7],
                        raw[9], -raw[8], raw[10], raw[11],
                        raw[13], -raw[12], raw[14], raw[15]
                };
            }
            Matrix44F rot = new Matrix44F();
            rot.setData(rotated);
            Matrix44F inverse = SampleMath.Matrix44FInverse(rot);
            Matrix44F transp = SampleMath.Matrix44FTranspose(inverse);

            float[] data = transp.getData();
            mCamera.position.set(data[12], data[13], data[14]);
            mCamera.up.set(data[4], data[5], data[6]);
            mCamera.direction.set(data[8], data[9], data[10]);

            if (!mMyPausedGame && !mOponentPausedGame) {
                models.updateModels(joystick.getAngleDegrees(), joystick.getPower(),
                        isAttackFirstDown, isAttackSecondDown, isDefenceDown, new Vector2
                                (data[4], data[5]), mModelState);
                mModelState = null;
            }

            //update filed of view
            mCamera.fieldOfView = filedOfView;

        } else {
            mCamera.position.set(100, 100, 100);
            mCamera.lookAt(1000, 1000, 1000);
        }

        mCamera.update();
    }

    private void updateMyModelState(Model3DList models3D, boolean myPausedGame, boolean gameStarted) {
        if (mBluetoothService != null && mSendEverySecond) {
            ModelState modelState = models3D.getMyModel().getStateBundle(myPausedGame);
            modelState.oponentHp = models3D.getOponentModel().getHP();
            modelState.started = gameStarted;
            mBluetoothService.write(SerializationUtils.serialize(modelState));
            mSendEverySecond = false;
        } else {
            mSendEverySecond = true;
        }
    }

    public void dispose() {
        modelBatch.dispose();
    }

    public void setButtons(ImageButton pauseBtn, JoyStick joystick, ImageButton attackFirstBtn,
                           ImageButton attackSecondBtn, ImageButton defenceBtn, View
                                   pausedOverlay, View pausedOponentOverlay, View startOverlay,
                           RoundCornerProgressBar
                                   hpProgress, Handler handler) {
        this.pauseBtn = pauseBtn;
        this.attackFirstBtn = attackFirstBtn;
        this.attackSecondBtn = attackSecondBtn;
        this.defenceBtn = defenceBtn;
        this.joystick = joystick;
        this.pausedMeOverlay = pausedOverlay;
        this.pausedOponentOverlay = pausedOponentOverlay;
        this.startOverlay = startOverlay;
        this.hpProgress = hpProgress;

        setListeners();
    }

    private void setListeners() {
        pausedMeOverlay.findViewById(R.id.btn_play).setOnClickListener(view -> {
            pausedMeOverlay.setVisibility(View.GONE);
            mMyPausedGame = false;
        });

        pauseBtn.setOnClickListener(view -> {
            pausedMeOverlay.setVisibility(View.VISIBLE);
            mMyPausedGame = true;
        });

        startOverlay.setOnClickListener(view -> {
            mGameStarted = true;
        });

        attackFirstBtn.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDefenceDown = false;
                    isAttackSecondDown = false;
                    isAttackFirstDown = true;
                    break;

                case MotionEvent.ACTION_UP:
                    isAttackFirstDown = false;
                    break;
            }
            return true;
        });

        attackSecondBtn.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isAttackFirstDown = false;
                    isDefenceDown = false;
                    isAttackSecondDown = true;
                    break;

                case MotionEvent.ACTION_UP:
                    isAttackSecondDown = false;
                    break;
            }
            return true;
        });

        defenceBtn.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isAttackFirstDown = false;
                    isAttackSecondDown = false;
                    isDefenceDown = true;
                    break;

                case MotionEvent.ACTION_UP:
                    isDefenceDown = false;
                    break;
            }
            return true;
        });
    }

    public void setModelState(byte[] buf) {
        ModelState modelState = (ModelState) SerializationUtils.deserialize(buf);

        mModelState = modelState;
    }

    public void setSinglePlayer(boolean singlePlayer) {
        mSinglePlayer = singlePlayer;
        mModelState = new ModelState();
        mModelState.started = true;
    }
}
