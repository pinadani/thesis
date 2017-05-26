package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.ar.libgdx.Engine;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ConnectionPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IConnectionView;
import cz.cvut.fit.pinadani.cardgamear.renderer.ArActivity;
import cz.cvut.fit.pinadani.cardgamear.service.NearbyConnections;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.DeviceListActivity;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.dialog.DeviceListDialog;
import nucleus.factory.RequiresPresenter;

/**
 * Fragment to get forgot password
 * Created by Daniel Pina
 **/
@RequiresPresenter(ConnectionPresenter.class)
public class ConnectionFragment extends BaseNucleusFragment<ConnectionPresenter>
        implements IConnectionView {
    public static final String TAG = ConnectionFragment.class.getName();

    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Bind(R.id.button_scan)
    Button mScanButton;

    @Bind(R.id.paired_devices)
    ListView mPairedListView;

    @Bind(R.id.new_devices)
    ListView mNewListView;

    @Bind(R.id.title_paired_devices)
    TextView mPairedDevicesTitle;

    @Bind(R.id.title_new_devices)
    TextView mNewDevicesTitle;

    /** The current state of the application **/
    @NearbyConnections.NearbyConnectionState
    private int mState = NearbyConnections.STATE_IDLE;


    private DeviceListDialog mMyListDialog;

    /**
     * Newly discovered devices
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        ButterKnife.bind(this, view);

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.device_name);

        mPairedListView.setAdapter(mPairedDevicesArrayAdapter);
        mPairedListView.setOnItemClickListener(getPresenter().getDeviceClickListener());

        mNewListView.setAdapter(mNewDevicesArrayAdapter);
        mNewListView.setOnItemClickListener(getPresenter().getDeviceClickListener());

        return view;
    }

    @OnClick(R.id.button_scan)
    public void onScanButtonCLicked(){
        getPresenter().doDiscovery();
        mNewDevicesArrayAdapter.clear();
        mScanButton.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister broadcast listeners
        getActivity().unregisterReceiver(mReceiver);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConnectionPresenter.REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    getPresenter().connectDevice(data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
                }
                break;
            case ConnectionPresenter.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    getPresenter().setupBluetooth(this);
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            case ArActivity.REQUEST_CODE:
                getPresenter().increasePoints(resultCode == Engine.WIN);
                break;

//            case REQUEST_ENABLE_BT:
//                // When the request to enable Bluetooth returns
//                if (resultCode == Activity.RESULT_OK) {
//                    // Bluetooth is now enabled, so set up a chat session
//                    setupChat();
//                } else {
//                    // User did not enable Bluetooth or an error occurred
//                    Log.d(TAG, "BT not enabled");
//                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
//                            Toast.LENGTH_SHORT).show();
//                    getActivity().finish();
//                }
        }
    }

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                showProgress(false);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                mScanButton.setEnabled(true);
            }

        }
    };
    @Override
    protected String getTitle() {
        return getString(R.string.connect_bluetooth_title);
    }

    @Override
    public void pressBack() {
    }

    @Override
    protected void initAB() {
        baseSettingsAB();
    }

    @Override
    public void setPairedDevices(Set<BluetoothDevice> pairedDevices) {
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {

            mPairedDevicesTitle.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    @Override
    public void setNewDevicesTitleVisibility(int visibility) {
        mNewDevicesTitle.setVisibility(visibility);
    }

    @Override
    public void updateViewVisibility(int stateAdvertising) {
        mState = stateAdvertising;
        switch (mState) {
            case NearbyConnections.STATE_IDLE:
                // The GoogleAPIClient is not connected, we can't yet start advertising or
                // discovery so hide all buttons
                break;
            case NearbyConnections.STATE_READY:
                // The GoogleAPIClient is connected, we can begin advertising or discovery.
                getActivity().findViewById(R.id.button_connect).setVisibility(View.VISIBLE);
                break;
            case NearbyConnections.STATE_ADVERTISING:
                break;
            case NearbyConnections.STATE_DISCOVERING:
                break;
            case NearbyConnections.STATE_CONNECTED:
                // We are connected to another device via the Connections API, so we can
                // show the message UI.
                getActivity().findViewById(R.id.button_connect).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showConnectionRequestDialog(AlertDialog connectionRequestDialog) {
        connectionRequestDialog.show();
    }

    @Override
    public void showEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        // This device is discovering endpoints and has located an advertiser. Display a dialog to
        // the user asking if they want to connect, and send a connection request if they do.
        if (mMyListDialog == null) {
            // Configure the AlertDialog that the MyListDialog wraps
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Endpoint(s) Found")
                    .setCancelable(true)
                    .setNegativeButton("Cancel", (dialog, which) -> mMyListDialog.dismiss());

            // Create the MyListDialog with a listener
            mMyListDialog = new DeviceListDialog(getActivity(), builder, (dialog, which) -> {
                String selectedEndpointName = mMyListDialog.getItemKey(which);
                String selectedEndpointId = mMyListDialog.getItemValue(which);

                getPresenter().connectTo(selectedEndpointId, selectedEndpointName);
                mMyListDialog.dismiss();
            });
        }

        mMyListDialog.addItem(endpointName, endpointId);
        mMyListDialog.show();
    }

    @Override
    public void removeEndpoint(String s) {
        if (mMyListDialog != null) {
            mMyListDialog.removeItemByValue(s);
        }
    }

    @OnClick(R.id.button_start_bluetooth)
    public void onStartClicked(){
        getPresenter().connectBluetooth(getFragmentActivity());
    }

    @OnClick(R.id.button_connect)
    public void onConnectClicked(){
        getPresenter().createBluetooth(getActivity());
    }


}
