package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ConnectBluetoothPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IConnectBluetoothView;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import nucleus.factory.RequiresPresenter;

/**
 * Fragment to get forgot password
 * Created by Daniel Pina
 **/
@RequiresPresenter(ConnectBluetoothPresenter.class)
public class ConnectBluetoothFragment extends BaseNucleusFragment<ConnectBluetoothPresenter>
        implements IConnectBluetoothView {
    public static final String TAG = ConnectBluetoothFragment.class.getName();

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
        View view = inflater.inflate(R.layout.fragment_connect_bluetooth, container, false);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        getPresenter().onActivityResult(requestCode, resultCode, data);
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
}
