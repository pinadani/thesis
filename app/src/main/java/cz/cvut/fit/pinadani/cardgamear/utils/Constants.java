package cz.cvut.fit.pinadani.cardgamear.utils;

import cz.cvut.fit.pinadani.cardgamear.BuildConfig;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {3/18/2017}
 **/
public class Constants {

    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static final int CIRCEL_IN_DEGREES = 360;



    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static final String SINGLE_PLAYER = "single_player";
    public static final String POSITION_X = "position_x";
    public static final String POSITION_Y = "position_y";
    public static final String ANGLE = "angle";
    public static final String HP = "hp";
    public static final String ACTION = "action";
}
