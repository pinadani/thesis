package cz.cvut.fit.pinadani.cardgamear.mvp.view;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * View of forgot password screen
 * Created by Daniel Pina
 **/
public interface IConnectBluetoothView extends IBaseView {

    void setPairedDevices(Set<BluetoothDevice> pairedDevices);

    void setNewDevicesTitleVisibility(int visibility);
}
