package cz.cvut.fit.pinadani.cardgamear.model;

import java.io.Serializable;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {4/21/2017}
 **/
public class ModelState implements Serializable {
    public float finishX;
    public float finishY;
    public double angle;
    public double bulletAngle;
    public int oponentHp;
    public boolean paused;
    public boolean visibleBullet;
    public int animation;
    public boolean started;
    public float[] matrix4;

    public ModelState(float finishX, float finishY,
                      double angle,
                      double bulletAngle, boolean paused, boolean visibleBullet, int animation,
                      float[] matrix4) {
        this.finishX = finishX;
        this.finishY = finishY;
        this.angle = angle;
        this.bulletAngle = bulletAngle;
        this.paused = paused;
        this.visibleBullet = visibleBullet;
        this.animation = animation;
        this.matrix4 = matrix4;
    }
}
