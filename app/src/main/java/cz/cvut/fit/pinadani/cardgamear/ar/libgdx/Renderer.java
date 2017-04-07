package cz.cvut.fit.pinadani.cardgamear.ar.libgdx;

import android.view.View;
import android.widget.ImageButton;

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

/**
 * Class responsible for rendering and scene transformations.
 */
public class Renderer {

    ImageButton pauseBtn;
    ImageButton attackFirstBtn;
    ImageButton attackSecondBtn;
    ImageButton defenceBtn;
    View pausedOverlay;

    JoyStick joystick;

    private PerspectiveCamera mCamera;
    private Environment mEnvironment;
    private ModelBatch modelBatch;
    private VuforiaRenderer vuforiaRenderer;

    private boolean mPausedGame = false;

    public Renderer(VuforiaRenderer arRenderer) {
        mEnvironment = new Environment();
        mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        mEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

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
            modelBatch.render(model.getModel(), mEnvironment);
        }

        modelBatch.end();

        gl.glDisable(GL20.GL_CULL_FACE);
        gl.glDisable(GL20.GL_DEPTH_TEST);
        gl.glDisable(GL20.GL_BLEND);
    }

    private void setProjectionAndCamera(TrackableResult[] trackables, float filedOfView, Model3DList models) {

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

            if (!mPausedGame) {
                models.updateModels(joystick.getAngleDegrees(), joystick.getPower(), new Vector2(data[4], data[5]));
            }
            //update filed of view
            mCamera.fieldOfView = filedOfView;

        } else {
            mCamera.position.set(100, 100, 100);
            mCamera.lookAt(1000, 1000, 1000);
        }

        mCamera.update();
    }

    public void dispose() {
        modelBatch.dispose();
    }

    public void setButtons(ImageButton pauseBtn, JoyStick joystick, ImageButton attackFirstBtn, ImageButton attackSecondBtn, ImageButton defenceBtn, View pausedOverlay) {
        this.pauseBtn = pauseBtn;
        this.attackFirstBtn = attackFirstBtn;
        this.attackSecondBtn = attackSecondBtn;
        this.defenceBtn = defenceBtn;
        this.joystick = joystick;
        this.pausedOverlay = pausedOverlay;

        setListeners();
    }

    private void setListeners() {
        pausedOverlay.findViewById(R.id.btn_play).setOnClickListener(view -> {
            pausedOverlay.setVisibility(View.GONE);
            mPausedGame = false;
        });
        pauseBtn.setOnClickListener(view -> {
            pausedOverlay.setVisibility(View.VISIBLE);
            mPausedGame = true;
        });

        attackFirstBtn.setOnClickListener(view -> {
            //TODO
        });

        attackSecondBtn.setOnClickListener(view -> {
            //TODO
        });

        defenceBtn.setOnClickListener(view -> {
            //TODO
        });
    }
}
