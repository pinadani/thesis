package cz.cvut.fit.pinadani.cardgamear.ar.libgdx;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

//import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.erz.joysticklibrary.JoyStick;

import cz.cvut.fit.pinadani.cardgamear.ar.vuforia.VuforiaRenderer;

/**
 * Instance of libgdx Game class responsible for rendering 3D content over augmented reality.
 */
public class Engine extends Game {
    ImageButton pauseBtn;
    ImageButton attackFirstBtn;
    ImageButton attackSecondBtn;
    ImageButton defenceBtn;
    View pausedOverlay;
    View pausedOponentOverlay;
    //RoundCornerProgressBar hpProgress;

    JoyStick joystick;
    Handler mHandler;

    private FPSLogger fps;
    private VuforiaRenderer vuforiaRenderer;
    private Display mDisplay;
    private Activity mActivity;

    public Engine(VuforiaRenderer vuforiaRenderer, Activity activity) {
        this.vuforiaRenderer = vuforiaRenderer;
        this.mActivity = activity;
    }

    @Override
    public void create() {
        mDisplay = new Display(vuforiaRenderer, mActivity);
        mDisplay.setJoystick(joystick);
        mDisplay.getRenderer().setButtons(pauseBtn, joystick, attackFirstBtn, attackSecondBtn,
                defenceBtn, pausedOverlay, pausedOponentOverlay, null, mHandler);
        setScreen(mDisplay);
        vuforiaRenderer.initRendering();
        fps = new FPSLogger();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Log.d("ENGINE", "Resize: " + width + "x" + height);
        vuforiaRenderer.onSurfaceChanged(width, height);
    }

    @Override
    public void render() {
        super.render();
        fps.log();
    }

    public void setButtons(ImageButton pauseBtn, JoyStick joystick, ImageButton attackFirstBtn,
                           ImageButton attackSecondBtn, ImageButton defenceBtn, View
                                   pausedOverlay, View pausedOponentOverlay, ProgressBar hpProgress, Handler handler) {
        this.pauseBtn = pauseBtn;
        this.attackFirstBtn = attackFirstBtn;
        this.attackSecondBtn = attackSecondBtn;
        this.defenceBtn = defenceBtn;
        this.joystick = joystick;
        this.pausedOverlay = pausedOverlay;
        this.pausedOponentOverlay = pausedOponentOverlay;
        //this.hpProgress = hpProgress;
        this.mHandler = handler;
    }
}
