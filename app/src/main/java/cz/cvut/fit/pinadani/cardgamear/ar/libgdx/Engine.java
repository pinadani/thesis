package cz.cvut.fit.pinadani.cardgamear.ar.libgdx;

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
    //RoundCornerProgressBar hpProgress;

    JoyStick joystick;
    Handler mHandler;

    private FPSLogger fps;
    private VuforiaRenderer vuforiaRenderer;
    private Display mDisplay;

    public Engine(VuforiaRenderer vuforiaRenderer) {
        this.vuforiaRenderer = vuforiaRenderer;
    }

    @Override
    public void create() {
        mDisplay = new Display(vuforiaRenderer);
        mDisplay.setJoystick(joystick);
        mDisplay.getRenderer().setButtons(pauseBtn, joystick, attackFirstBtn, attackSecondBtn,
                defenceBtn, pausedOverlay, null, mHandler);
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
                                   pausedOverlay, ProgressBar hpProgress, Handler handler) {
        this.pauseBtn = pauseBtn;
        this.attackFirstBtn = attackFirstBtn;
        this.attackSecondBtn = attackSecondBtn;
        this.defenceBtn = defenceBtn;
        this.joystick = joystick;
        this.pausedOverlay = pausedOverlay;
        //this.hpProgress = hpProgress;
        this.mHandler = handler;
    }
}
