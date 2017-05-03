package cz.cvut.fit.pinadani.cardgamear.model;

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
    public float quaternionX;
    public float quaternionY;
    public float quaternionZ;
    public float quaternionW;
    public double angle;
    public double bulletAngle;
    public int oponentHp;
    public boolean paused;
    public boolean visibleBullet;
    public int animation;
    public boolean started;

    public ModelState(float x, float y, float z, float finishX, float finishY, float quaternionX, float quaternionY, float quaternionZ, float quaternionW, double angle, double bulletAngle, boolean paused, boolean visibleBullet, int animation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.finishX = finishX;
        this.finishY = finishY;
        this.quaternionX = quaternionX;
        this.quaternionY = quaternionY;
        this.quaternionZ = quaternionZ;
        this.quaternionW = quaternionW;
        this.angle = angle;
        this.bulletAngle = bulletAngle;
        this.paused = paused;
        this.visibleBullet = visibleBullet;
        this.animation = animation;
    }

    public ModelState(float x, float y, float z, float finishX, float finishY, float quaternionX, float quaternionY, float quaternionZ, float quaternionW, double angle, double bulletAngle, int oponentHp, boolean paused, boolean visibleBullet, int animation, boolean started) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.finishX = finishX;
        this.finishY = finishY;
        this.quaternionX = quaternionX;
        this.quaternionY = quaternionY;
        this.quaternionZ = quaternionZ;
        this.quaternionW = quaternionW;
        this.angle = angle;
        this.bulletAngle = bulletAngle;
        this.oponentHp = oponentHp;
        this.paused = paused;
        this.visibleBullet = visibleBullet;
        this.animation = animation;
        this.started = started;
    }

    public ModelState() {

    }
}
