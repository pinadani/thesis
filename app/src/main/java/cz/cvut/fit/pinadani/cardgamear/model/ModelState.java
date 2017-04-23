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
    public double angle;
    public int hp;

    public ModelState(float x, float y, float z, double angle, int hp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
        this.hp = hp;
    }
}
