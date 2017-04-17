package cz.cvut.fit.pinadani.cardgamear.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IConnectionView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * TODO add class description
 **/
public class NearbyConnections implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener {

    private static final String TAG = NearbyConnections.class.getSimpleName();

    /**
     * Timeouts (in millis) for startAdvertising and startDiscovery.  At the end of these time
     * intervals the app will silently stop advertising or discovering.
     *
     * To set advertising or discovery to run indefinitely, use 0L where timeouts are required.
     */
    private static final long TIMEOUT_ADVERTISE = 1000L * 30L;
    private static final long TIMEOUT_DISCOVER = 1000L * 30L;

    /**
     * Possible states for this application:
     *      IDLE - GoogleApiClient not yet connected, can't do anything.
     *      READY - GoogleApiClient connected, ready to use Nearby NearbyConnections API.
     *      ADVERTISING - advertising for peers to connect.
     *      DISCOVERING - looking for a peer that is advertising.
     *      CONNECTED - found a peer.
     */
    @Retention(RetentionPolicy.CLASS)
    @IntDef({STATE_IDLE, STATE_READY, STATE_ADVERTISING, STATE_DISCOVERING, STATE_CONNECTED})
    public @interface NearbyConnectionState {}
    public static final int STATE_IDLE = 1023;
    public static final int STATE_READY = 1024;
    public static final int STATE_ADVERTISING = 1025;
    public static final int STATE_DISCOVERING = 1026;
    public static final int STATE_CONNECTED = 1027;

    /** GoogleApiClient for connecting to the Nearby NearbyConnections API **/
    private GoogleApiClient mGoogleApiClient;

    /** The current state of the application **/
    @NearbyConnectionState
    private int mState = STATE_IDLE;

    /** The endpoint ID of the connected peer, used for messaging **/
    private String mOtherEndpointId;

    private IConnectionView mConnectionView = null;

    public NearbyConnections(IConnectionView iConnectBluetoothView) {
        mConnectionView = iConnectBluetoothView;

        // Initialize Google API Client for Nearby Connections. Note: if you are using Google+
        // sign-in with your project or any other API that requires Authentication you may want
        // to use a separate Google API Client for Nearby Connections.  This API does not
        // require the user to authenticate so it can be used even when the user does not want to
        // sign in or sign-in has failed.
        mGoogleApiClient = new GoogleApiClient.Builder(App.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();

        mGoogleApiClient.connect();
    }

    /**
     * Check if the device is connected (or connecting) to a WiFi network.
     * @return true if connected or connecting, false otherwise.
     */
    private boolean isConnectedToNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return (info != null && info.isConnectedOrConnecting());
    }

    /**
     * Begin advertising for Nearby Connections, if possible.
     */
    public void startAdvertising() {

        if (!isConnectedToNetwork()) {
            return;
        }

        // Advertising with an AppIdentifer lets other devices on the network discover
        // this application and prompt the user to install the application.
        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(App.getContext().getPackageName()));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        // Advertise for Nearby Connections. This will broadcast the service id defined in
        // AndroidManifest.xml. By passing 'null' for the name, the Nearby Connections API
        // will construct a default name based on device model such as 'LGE Nexus 5'.
        String name = null;
        Nearby.Connections.startAdvertising(mGoogleApiClient, name, appMetadata, TIMEOUT_ADVERTISE,
                this).setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
            @Override
            public void onResult(Connections.StartAdvertisingResult result) {
                Log.d(TAG, "startAdvertising:onResult:" + result);
                if (result.getStatus().isSuccess()) {
                    mConnectionView.updateViewVisibility(STATE_ADVERTISING);
                } else {

                    // If the user hits 'Advertise' multiple times in the timeout window,
                    // the error will be STATUS_ALREADY_ADVERTISING
                    int statusCode = result.getStatus().getStatusCode();
                    if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_ADVERTISING) {
                    } else {
                        mConnectionView.updateViewVisibility(STATE_READY);
                    }
                }
            }
        });
    }

    /**
     * Begin discovering devices advertising Nearby Connections, if possible.
     */
    public void startDiscovery() {
        if (!isConnectedToNetwork()) {
            return;
        }

        // Discover nearby apps that are advertising with the required service ID.
        String serviceId = App.getContext().getString(R.string.service_id);
        Nearby.Connections.startDiscovery(mGoogleApiClient, serviceId, TIMEOUT_DISCOVER, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mConnectionView.updateViewVisibility(STATE_DISCOVERING);
                        } else {
                            // If the user hits 'Discover' multiple times in the timeout window,
                            // the error will be STATUS_ALREADY_DISCOVERING
                            int statusCode = status.getStatusCode();
                            if (statusCode == ConnectionsStatusCodes.STATUS_ALREADY_DISCOVERING) {
                                //TODO debugLog("STATUS_ALREADY_DISCOVERING");
                            } else {
                                mConnectionView.updateViewVisibility(STATE_READY);
                            }
                        }
                    }
                });
    }

    /**
     * Send a reliable message to the connected peer. Takes the contents of the EditText and
     * sends the message as a byte[].
     */
    private void sendMessage() {
        // Sends a reliable message, which is guaranteed to be delivered eventually and to respect
        // message ordering from sender to receiver. Nearby.Connections.sendUnreliableMessage
        // should be used for high-frequency messages where guaranteed delivery is not required, such
        // as showing one player's cursor location to another. Unreliable messages are often
        // delivered faster than reliable messages.

//        String msg = mMessageText.getText().toString();
//        Nearby.Connections.sendReliableMessage(mGoogleApiClient, mOtherEndpointId, msg.getBytes());
//
//        mMessageText.setText(null);
    }

    /**
     * Send a connection request to a given endpoint.
     * @param endpointId the endpointId to which you want to connect.
     * @param endpointName the name of the endpoint to which you want to connect. Not required to
     *                     make the connection, but used to display after success or failure.
     */
    public void connectTo(String endpointId, final String endpointName) {

        // Send a connection request to a remote endpoint. By passing 'null' for the name,
        // the Nearby Connections API will construct a default name based on device model
        // such as 'LGE Nexus 5'.
        String myName = null;
        byte[] myPayload = null;
        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, myName, endpointId, myPayload,
                new Connections.ConnectionResponseCallback() {
                    @Override
                    public void onConnectionResponse(String endpointId, Status status,
                                                     byte[] bytes) {
                        Log.d(TAG, "onConnectionResponse:" + endpointId + ":" + status);
                        if (status.isSuccess()) {
                            Toast.makeText(App.getContext(), "Connected to " + endpointName,
                                    Toast.LENGTH_SHORT).show();

                            mOtherEndpointId = endpointId;
                            mConnectionView.updateViewVisibility(STATE_CONNECTED);
                        } else {
                        }
                    }
                }, this);
    }


    //TODO
//    @Override
//    public void onClick(View view) {
//        switch(v.getId()) {
//            case R.id.button_advertise:
//                startAdvertising();
//                break;
//            case R.id.button_discover:
//                startDiscovery();
//                break;
//            case R.id.button_send:
//                sendMessage();
//                break;
//        }
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mConnectionView.updateViewVisibility(STATE_READY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mConnectionView.updateViewVisibility(STATE_IDLE);

        // Try to re-connect
        mGoogleApiClient.reconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mConnectionView.updateViewVisibility(STATE_IDLE);
    }

    @Override
    public void onConnectionRequest(final String endpointId, String deviceId, String endpointName, byte[] payload) {
// This device is advertising and has received a connection request. Show a dialog asking
        // the user if they would like to connect and accept or reject the request accordingly.
        AlertDialog connectionRequestDialog = new AlertDialog.Builder(mConnectionView.getFragmentActivity())
                .setTitle("Connection Request")
                .setMessage("Do you want to connect to " + endpointName + "?")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        byte[] payload = null;
                        Nearby.Connections.acceptConnectionRequest(mGoogleApiClient, endpointId,
                                payload, NearbyConnections.this)
                                .setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        if (status.isSuccess()) {
                                            mOtherEndpointId = endpointId;
                                            mConnectionView.updateViewVisibility(STATE_CONNECTED);
                                        } else {
                                            //debugLog("acceptConnectionRequest: FAILURE");
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, endpointId);
                    }
                }).create();
        //TODO updateView();
        mConnectionView.showConnectionRequestDialog(connectionRequestDialog);
    }

    @Override
    public void onEndpointFound(String s, String s1, String s2, String s3) {
        //TODO updateView();
        mConnectionView.showEndpointFound(s, s1, s2, s3);

    }

    @Override
    public void onEndpointLost(String s) {
        mConnectionView.removeEndpoint(s);
    }

    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b) {
        // A message has been received from a remote endpoint.
        //TODO debugLog("onMessageReceived:" + endpointId + ":" + new String(payload));
    }

    @Override
    public void onDisconnected(String s) {
        mConnectionView.updateViewVisibility(STATE_READY);
    }
}
