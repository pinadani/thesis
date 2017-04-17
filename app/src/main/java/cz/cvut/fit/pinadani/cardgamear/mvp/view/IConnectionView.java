package cz.cvut.fit.pinadani.cardgamear.mvp.view;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * View of forgot password screen
 * Created by Daniel Pina
 **/
public interface IConnectionView extends IBaseView {

    void setPairedDevices(Set<BluetoothDevice> pairedDevices);

    void setNewDevicesTitleVisibility(int visibility);

    void updateViewVisibility(int stateAdvertising);

    void showEndpointFound(String s, String s1, String s2, String s3);

    void removeEndpoint(String s);

    void showConnectionRequestDialog(AlertDialog connectionRequestDialog);
}
