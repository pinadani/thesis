package cz.cvut.fit.pinadani.cardgamear.model;

import com.badlogic.gdx.math.Matrix4;

import java.io.Serializable;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {4/21/2017}
 **/
public class ModelState implements Serializable {
    public float x;
    public float y;
    public float z;
    public float finishX;
    public float finishY;
    public double angle;
    public double bulletAngle;
    public int hp;
    public boolean paused;
    public boolean visibleBullet;
    public Matrix4 model;
    public Matrix4 bullet;

    public ModelState(float x, float y, float z, float finishX, float finishY, double angle,
                      double bulletAngle, int hp, boolean paused, boolean visibleBullet, Matrix4
                              model, Matrix4 bullet) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.finishX = finishX;
        this.finishY = finishY;
        this.angle = angle;
        this.bulletAngle = bulletAngle;
        this.hp = hp;
        this.paused = paused;
        this.visibleBullet = visibleBullet;
        this.model = model;
        this.bullet = bullet;
    }
}
