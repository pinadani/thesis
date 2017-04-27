package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IConnectionView;
import cz.cvut.fit.pinadani.cardgamear.renderer.ArActivity;
import cz.cvut.fit.pinadani.cardgamear.service.BluetoothService;
import cz.cvut.fit.pinadani.cardgamear.service.NearbyConnections;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.DeviceListActivity;
import cz.cvut.fit.pinadani.cardgamear.utils.App;
import cz.cvut.fit.pinadani.cardgamear.utils.Constants;

/**
 * TODO
 **/
public class ConnectionPresenter extends BasePresenter<IConnectionView> {
    public static final String TAG = ConnectionPresenter.class.getName();

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_ENABLE_BT = 3;

    public static String DEVICE_ADDRESS = "device_address";

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothService mBluetoothService = null;

    private NearbyConnections mWifiService = null;

    @Inject
    ISPInteractor mSpInteractor;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onTakeView(IConnectionView connectionView) {
        super.onTakeView(connectionView);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            connectionView.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            setupBluetooth(connectionView);
        }
    }

    public void setupBluetooth(IConnectionView connectionView) {
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        connectionView.setPairedDevices(pairedDevices);

        if(mWifiService == null) {
            mWifiService = new NearbyConnections(connectionView);
        }

        if((mBluetoothService != null) && (mBluetoothService.getState() != 0)) {
            //mBluetoothService.stop();
        }

        if (mBluetoothService == null) {
            // Initialize the BluetoothChatService to perform bluetooth connections
            mBluetoothService = new BluetoothService(connectionView.getFragmentActivity(), mHandler);
        }

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBluetoothService.start();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (getView() != null) {
                FragmentActivity activity = getView().getFragmentActivity();
                switch (msg.what) {
                    case Constants.MESSAGE_DEVICE_NAME:
                        // save the connected device's name
                        mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                        if (null != activity) {
                            Toast.makeText(activity, "Connected to "
                                    + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                            // Launch the DeviceListActivity to see devices and do scan
                            ((App)App.getContext()).setHandler(mHandler);
                            ((App)App.getContext()).setBluetoothService(mBluetoothService);

                            Intent multiPlayerIntent = new Intent(activity, ArActivity.class);
                            multiPlayerIntent.putExtra(Constants.SINGLE_PLAYER, false);
                            activity.startActivity(multiPlayerIntent);
                        }
                        break;
                    case Constants.MESSAGE_TOAST:
                        if (null != activity) {
                            Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constants.MESSAGE_WRITE:
                        byte[] writeBuf = (byte[]) msg.obj;
                        // construct a string from the buffer
                        String writeMessage = new String(writeBuf);
                        mConversationArrayAdapter.add("Me:  " + writeMessage);
                        break;
                    case Constants.MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        // construct a string from the valid bytes in the buffer
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                        break;
                }
            } else {

            }
        }
    };

    /**
            * Sends a message.
     *
             * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    public void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        getView().showProgress(true);

        // Turn on sub-title for new devices
        getView().setNewDevicesTitleVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = (av, v, arg2, arg3) -> {
                // Cancel discovery because it's costly and we're about to connect
                mBluetoothAdapter.cancelDiscovery();

                // Get the device MAC address, which is the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);

                connectDevice(address);
            };

    /**
     * Establish connection with other device
     *
     * @param address device MAC address
     */
    public void connectDevice(String address) {
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, true);
    }

    public AdapterView.OnItemClickListener getDeviceClickListener() {
        return mDeviceClickListener;
    }

    public void createBluetooth(FragmentActivity activity) {
// Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(activity, DeviceListActivity.class);
        activity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }

    public void connectBluetooth(FragmentActivity activity) {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            activity.startActivity(discoverableIntent);
        }
    }

    public void connectWifi() {
        mWifiService.startDiscovery();
    }

    public void createWifi() {
        mWifiService.startAdvertising();
    }

    public void connectTo(String selectedEndpointId, String selectedEndpointName) {
        mWifiService.connectTo(selectedEndpointId, selectedEndpointName);
    }
}
